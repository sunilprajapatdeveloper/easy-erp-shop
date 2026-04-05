package nextpos.app.nextpos.importexport.consumer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nextpos.app.nextpos.importexport.entity.ImportExportJob;
import nextpos.app.nextpos.importexport.repository.ImportExportJobRepository;
import nextpos.app.nextpos.importexport.strategy.ImportExportStrategy;
import nextpos.app.nextpos.importexport.strategy.ImportExportStrategyRegistry;
import nextpos.app.nextpos.importexport.util.PdfExportUtil;
import nextpos.app.nextpos.model.dto.request.MediaUploadRequest;
import nextpos.app.nextpos.model.dto.response.MediaResponse;
import nextpos.app.nextpos.model.entity.Company;
import nextpos.app.nextpos.model.enums.MediaType;
import nextpos.app.nextpos.repository.CompanyRepository;
import nextpos.app.nextpos.service.interf.MediaService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExportJobConsumer {

    private final ImportExportJobRepository jobRepository;
    private final ImportExportStrategyRegistry strategyRegistry;
    private final MediaService mediaService;
    private final ObjectMapper objectMapper;
    private final CompanyRepository companyRepository;

    @KafkaListener(topics = "export-job", groupId = "export-group")
    public void consume(Map<String, Object> message) {
        Long jobId = ((Number) message.get("jobId")).longValue();
        log.info("Received export job: {}", jobId);

        ImportExportJob job = jobRepository.findById(jobId).orElse(null);
        if (job == null)
            return;

        ImportExportStrategy strategy = strategyRegistry.getStrategy(job.getModule());
        if (strategy == null) {
            job.setStatus("FAILED");
            job.setErrorSummary("Unsupported module: " + job.getModule());
            jobRepository.save(job);
            return;
        }

        try {
            Map<String, Object> filters = objectMapper.readValue(job.getOptionsJson(), new TypeReference<>() {
            });
            filters.put("companyId", job.getCompanyId());
            filters.put("userId", job.getUserId());

            List<Map<String, Object>> data = strategy.exportData(filters);
            String format = (String) message.get("format");

            // Fetch Company entity for PDF header/footer
            Company company = null;
            if (job.getCompanyId() != null) {
                company = companyRepository.findById(job.getCompanyId()).orElse(null);
            }

            byte[] fileBytes = generateExportFile(data, format, strategy.getColumnHeaders(), job.getModule(), company);

            String filename = "export_" + job.getModule() + "_" + job.getJobNumber() + "." + getExtension(format);
            MediaUploadRequest uploadRequest = MediaUploadRequest.builder()
                    .companyId(job.getCompanyId())
                    .entityType("IMPORT_EXPORT")
                    .entityId(job.getId())
                    .mediaType(MediaType.DOCUMENT)
                    .isPublic(false)
                    .metadata(Map.of("jobId", job.getId().toString(), "module", job.getModule()))
                    .build();

            MultipartFile multipartFile = new ByteArrayMultipartFile(fileBytes, filename, "application/octet-stream");
            MediaResponse media = mediaService.uploadFile(multipartFile, uploadRequest, job.getUserId(),
                    job.getCompanyId());

            job.setResultMediaId(media.getId());
            job.setStatus("COMPLETED");
            job.setCompletedAt(LocalDateTime.now());
            jobRepository.save(job);
        } catch (Exception e) {
            log.error("Export failed", e);
            job.setStatus("FAILED");
            job.setErrorSummary(e.getMessage());
            jobRepository.save(job);
        }
    }

    private byte[] generateExportFile(List<Map<String, Object>> data,
            String format,
            List<String> headers,
            String module,
            Company company) throws IOException {
        return switch (format.toUpperCase()) {
            case "CSV" -> generateCsv(data, headers);
            case "EXCEL" -> generateExcel(data, headers);
            case "PDF" -> PdfExportUtil.generatePdfReport(data, headers, module, company, mediaService);
            default -> throw new IllegalArgumentException("Unsupported format: " + format);
        };
    }

    private byte[] generateCsv(List<Map<String, Object>> data, List<String> headers) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(String.join(",", headers).concat("\n").getBytes());
        for (Map<String, Object> row : data) {
            List<String> values = headers.stream()
                    .map(h -> String.valueOf(row.getOrDefault(h, "")))
                    .toList();
            baos.write(String.join(",", values).concat("\n").getBytes());
        }
        return baos.toByteArray();
    }

    private byte[] generateExcel(List<Map<String, Object>> data, List<String> headers) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Export");
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.size(); i++)
                headerRow.createCell(i).setCellValue(headers.get(i));

            int rowNum = 1;
            for (Map<String, Object> row : data) {
                Row dataRow = sheet.createRow(rowNum++);
                for (int i = 0; i < headers.size(); i++) {
                    dataRow.createCell(i).setCellValue(String.valueOf(row.getOrDefault(headers.get(i), "")));
                }
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            workbook.write(baos);
            return baos.toByteArray();
        }
    }

    private String getExtension(String format) {
        return switch (format.toUpperCase()) {
            case "CSV" -> "csv";
            case "EXCEL" -> "xlsx";
            case "PDF" -> "pdf";
            default -> "dat";
        };
    }

    private record ByteArrayMultipartFile(byte[] content, String name, String contentType) implements MultipartFile {
        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getOriginalFilename() {
            return name;
        }

        @Override
        public String getContentType() {
            return contentType;
        }

        @Override
        public boolean isEmpty() {
            return content == null || content.length == 0;
        }

        @Override
        public long getSize() {
            return content.length;
        }

        @Override
        public byte[] getBytes() {
            return content;
        }

        @Override
        public java.io.InputStream getInputStream() {
            return new java.io.ByteArrayInputStream(content);
        }

        @Override
        public void transferTo(java.io.File dest) throws IOException {
            java.nio.file.Files.write(dest.toPath(), content);
        }
    }
}