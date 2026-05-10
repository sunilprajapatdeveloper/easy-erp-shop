package nextpos.app.nextpos.importexport.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nextpos.app.nextpos.importexport.dto.request.ExportRequest;
import nextpos.app.nextpos.importexport.dto.request.ImportRequest;
import nextpos.app.nextpos.importexport.dto.response.ImportErrorResponse;
import nextpos.app.nextpos.importexport.dto.response.JobResponse;
import nextpos.app.nextpos.importexport.dto.response.JobStatusResponse;
import nextpos.app.nextpos.importexport.entity.ImportError;
import nextpos.app.nextpos.importexport.entity.ImportExportJob;
import nextpos.app.nextpos.importexport.repository.ImportErrorRepository;
import nextpos.app.nextpos.importexport.repository.ImportExportJobRepository;
import nextpos.app.nextpos.importexport.strategy.ImportExportStrategy;
import nextpos.app.nextpos.importexport.strategy.ImportExportStrategyRegistry;
import nextpos.app.nextpos.model.dto.request.MediaUploadRequest;
import nextpos.app.nextpos.model.dto.response.MediaResponse;
import nextpos.app.nextpos.model.enums.MediaType;
import nextpos.app.nextpos.security.context.UserContext;
import nextpos.app.nextpos.service.interf.MediaService;
import nextpos.app.nextpos.importexport.producer.ImportJobProducer;
import nextpos.app.nextpos.importexport.producer.ExportJobProducer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImportExportService {

    private final ImportExportJobRepository jobRepository;
    private final ImportErrorRepository errorRepository;
    private final MediaService mediaService;
    private final ImportJobProducer importJobProducer;
    private final ExportJobProducer exportJobProducer;
    private final ImportExportStrategyRegistry strategyRegistry;
    private final ObjectMapper objectMapper;

    private static final AtomicLong jobNumberGenerator = new AtomicLong(System.currentTimeMillis());

    @Transactional
    public JobResponse startImport(ImportRequest request) {
        Long companyId = UserContext.getCurrentCompanyId();
        Long userId = UserContext.getCurrentUserId();

        // Validate module exists
        ImportExportStrategy strategy = strategyRegistry.getStrategy(request.getModule());
        if (strategy == null) {
            throw new IllegalArgumentException("Unsupported module: " + request.getModule());
        }

        // Create job
        ImportExportJob job = ImportExportJob.builder()
                .jobNumber(jobNumberGenerator.incrementAndGet())
                .companyId(companyId)
                .userId(userId)
                .module(request.getModule())
                .type("IMPORT")
                .status("PENDING")
                .createdBy(userId)
                .updatedBy(userId)
                .build();

        // Save to get ID
        job = jobRepository.save(job);

        // Upload file using MediaService
        try {
            MediaUploadRequest uploadRequest = MediaUploadRequest.builder()
                    .companyId(companyId)
                    .entityType("IMPORT_EXPORT")
                    .entityId(job.getId())
                    .mediaType(MediaType.DOCUMENT)
                    .isPublic(false)
                    .generateThumbnail(false)
                    .metadata(Map.of("jobId", job.getId().toString(), "module", request.getModule()))
                    .build();

            MediaResponse media = mediaService.uploadFile(request.getFile(), uploadRequest);
            job.setSourceMediaId(media.getId());
        } catch (IOException e) {
            log.error("Failed to upload file for job {}", job.getId(), e);
            job.setStatus("FAILED");
            job.setErrorSummary("File upload failed: " + e.getMessage());
            jobRepository.save(job);
            throw new RuntimeException("File upload failed", e);
        }

        // Save options as JSON
        try {
            job.setOptionsJson(objectMapper.writeValueAsString(request.getOptions()));
        } catch (JsonProcessingException e) {
            log.warn("Failed to serialize options", e);
        }

        jobRepository.save(job);

        final Long finalJobId = job.getId();
        final Long finalCompanyId = companyId;
        final String finalModule = job.getModule();
        final String finalSourceMediaId = job.getSourceMediaId();
        final Map<String, Object> finalOptions = request.getOptions();

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                Map<String, Object> message = new HashMap<>();
                message.put("jobId", finalJobId);
                message.put("companyId", finalCompanyId);
                message.put("module", finalModule);
                message.put("sourceMediaId", finalSourceMediaId);
                message.put("options", finalOptions);
                importJobProducer.send(message);
            }
        });

        return toJobResponse(job);
    }

    @Transactional
    public JobResponse startExport(ExportRequest request) {
        Long companyId = UserContext.getCurrentCompanyId();
        Long userId = UserContext.getCurrentUserId();

        ImportExportStrategy strategy = strategyRegistry.getStrategy(request.getModule());
        if (strategy == null) {
            throw new IllegalArgumentException("Unsupported module: " + request.getModule());
        }

        ImportExportJob job = ImportExportJob.builder()
                .jobNumber(jobNumberGenerator.incrementAndGet())
                .companyId(companyId)
                .userId(userId)
                .module(request.getModule())
                .type("EXPORT")
                .status("PENDING")
                .createdBy(userId)
                .updatedBy(userId)
                .build();

        try {
            job.setOptionsJson(objectMapper.writeValueAsString(request.getFilters()));
        } catch (JsonProcessingException e) {
            log.warn("Failed to serialize filters", e);
        }

        job = jobRepository.save(job);

        final Long finalJobId = job.getId();
        final Long finalCompanyId = companyId;
        final String finalModule = job.getModule();
        final String finalFormat = request.getFormat();
        final Map<String, Object> finalFilters = request.getFilters();

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                Map<String, Object> message = new HashMap<>();
                message.put("jobId", finalJobId);
                message.put("companyId", finalCompanyId);
                message.put("module", finalModule);
                message.put("format", finalFormat);
                message.put("filters", finalFilters);
                exportJobProducer.send(message);
            }
        });

        return toJobResponse(job);
    }

    public JobStatusResponse getJobStatus(Long jobId) {
        Long companyId = UserContext.getCurrentCompanyId();

        ImportExportJob job = jobRepository.findByIdAndCompanyId(jobId, companyId)
                .orElseThrow(() -> new IllegalArgumentException("Job not found"));

        JobStatusResponse response = new JobStatusResponse();
        response.setId(job.getId());
        response.setStatus(job.getStatus());
        response.setTotalRecords(job.getTotalRecords());
        response.setProcessedRecords(job.getProcessedRecords());
        response.setSuccessRecords(job.getSuccessRecords());
        response.setErrorRecords(job.getErrorRecords());
        response.setErrorSummary(job.getErrorSummary());
        response.setStartedAt(job.getStartedAt());
        response.setCompletedAt(job.getCompletedAt());

        // Generate signed URLs if result or error files exist
        if (job.getResultMediaId() != null) {
            response.setResultUrl(mediaService.getSignedUrl(job.getResultMediaId(), 60));
        }
        if (job.getErrorMediaId() != null) {
            response.setErrorUrl(mediaService.getSignedUrl(job.getErrorMediaId(), 60));
        }

        return response;
    }

    public Page<ImportErrorResponse> getJobErrors(Long jobId, Pageable pageable) {
        Long companyId = UserContext.getCurrentCompanyId();

        // Ensure job belongs to company
        jobRepository.findByIdAndCompanyId(jobId, companyId)
                .orElseThrow(() -> new IllegalArgumentException("Job not found"));

        return errorRepository.findByJobIdOrderByRowNumberAsc(jobId, pageable)
                .map(this::toErrorResponse);
    }

    public Page<JobResponse> getHistory(String module, String type, Pageable pageable) {
        Long companyId = UserContext.getCurrentCompanyId();

        if (module != null) {
            return jobRepository.findByCompanyIdAndModuleOrderByCreatedAtDesc(companyId, module, pageable)
                    .map(this::toJobResponse);
        } else if (type != null) {
            return jobRepository.findByCompanyIdAndTypeAndStatus(companyId, type, null, pageable)
                    .map(this::toJobResponse);
        } else {
            // Default: all jobs for company, sorted by created date
            return jobRepository.findByCompanyIdAndModuleOrderByCreatedAtDesc(companyId, null, pageable)
                    .map(this::toJobResponse);
        }
    }

    @Transactional
    public void cancelJob(Long jobId) {
        Long companyId = UserContext.getCurrentCompanyId();
        Long userId = UserContext.getCurrentUserId();

        ImportExportJob job = jobRepository.findByIdAndCompanyId(jobId, companyId)
                .orElseThrow(() -> new IllegalArgumentException("Job not found"));

        if (!"PENDING".equals(job.getStatus()) && !"PROCESSING".equals(job.getStatus())) {
            throw new IllegalStateException("Job cannot be cancelled in status: " + job.getStatus());
        }

        job.setStatus("CANCELLED");
        job.setCompletedAt(LocalDateTime.now());
        job.setUpdatedBy(userId);
        jobRepository.save(job);
    }

    private JobResponse toJobResponse(ImportExportJob job) {
        JobResponse response = new JobResponse();
        response.setId(job.getId());
        response.setJobNumber(job.getJobNumber());
        response.setModule(job.getModule());
        response.setType(job.getType());
        response.setStatus(job.getStatus());
        response.setSourceMediaId(job.getSourceMediaId());
        response.setResultMediaId(job.getResultMediaId());
        response.setErrorMediaId(job.getErrorMediaId());
        response.setTotalRecords(job.getTotalRecords());
        response.setProcessedRecords(job.getProcessedRecords());
        response.setSuccessRecords(job.getSuccessRecords());
        response.setErrorRecords(job.getErrorRecords());
        response.setStartedAt(job.getStartedAt());
        response.setCompletedAt(job.getCompletedAt());
        response.setErrorSummary(job.getErrorSummary());
        response.setOptionsJson(job.getOptionsJson());
        response.setCreatedAt(job.getCreatedAt());
        return response;
    }

    private ImportErrorResponse toErrorResponse(ImportError error) {
        ImportErrorResponse response = new ImportErrorResponse();
        response.setId(error.getId());
        response.setRowNumber(error.getRowNumber());
        response.setColumnName(error.getColumnName());
        response.setErrorMessage(error.getErrorMessage());
        response.setRawData(error.getRawData());
        return response;
    }
}