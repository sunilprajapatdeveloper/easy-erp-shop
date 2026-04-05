package nextpos.app.nextpos.importexport.consumer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nextpos.app.nextpos.importexport.entity.ImportExportJob;
import nextpos.app.nextpos.importexport.job.ImportJobConfig;
import nextpos.app.nextpos.importexport.repository.ImportExportJobRepository;
import nextpos.app.nextpos.importexport.strategy.ImportExportStrategy;
import nextpos.app.nextpos.importexport.strategy.ImportExportStrategyRegistry;
import nextpos.app.nextpos.model.entity.Media;
import nextpos.app.nextpos.repository.MediaRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class ImportJobConsumer {

    private final JobLauncher jobLauncher;
    private final ImportExportJobRepository jobRepository;
    private final ImportExportStrategyRegistry strategyRegistry;
    private final ImportJobConfig importJobConfig;
    private final ObjectMapper objectMapper;
    private final MediaRepository mediaRepository;

    @KafkaListener(topics = "import-job", groupId = "import-group")
    public void consume(Map<String, Object> message) {
        Long jobId = ((Number) message.get("jobId")).longValue();
        log.info("Received import job: {}", jobId);

        ImportExportJob job = jobRepository.findById(jobId).orElse(null);
        if (job == null) {
            log.error("Job not found: {}", jobId);
            return;
        }

        ImportExportStrategy strategy = strategyRegistry.getStrategy(job.getModule());
        if (strategy == null) {
            log.error("No strategy for module: {}", job.getModule());
            job.setStatus("FAILED");
            job.setErrorSummary("Unsupported module: " + job.getModule());
            jobRepository.save(job);
            return;
        }

        try {
            // Parse options
            Map<String, Object> options = objectMapper.readValue(job.getOptionsJson(),
                    new TypeReference<Map<String, Object>>() {
                    });
            options.put("companyId", job.getCompanyId());
            options.put("userId", job.getUserId());

            // Add column headers from strategy
            String[] headers = strategy.getColumnHeaders().toArray(new String[0]);
            options.put("headers", headers);

            // Determine file extension from the media record
            Media media = mediaRepository.findById(job.getSourceMediaId()).orElse(null);
            if (media != null) {
                String originalName = media.getOriginalFilename();
                if (originalName != null && originalName.toLowerCase().endsWith(".csv")) {
                    options.put("fileExtension", "csv");
                } else {
                    options.put("fileExtension", "xlsx"); // default
                }
            } else {
                options.put("fileExtension", "xlsx");
            }

            // Create Spring Batch job
            Job batchJob = importJobConfig.createImportJob(job, strategy, options);

            JobParameters parameters = new JobParametersBuilder()
                    .addLong("jobId", job.getId())
                    .addLong("timestamp", System.currentTimeMillis())
                    .toJobParameters();
            jobLauncher.run(batchJob, parameters);
        } catch (Exception e) {
            log.error("Failed to process import job: {}", jobId, e);
            job.setStatus("FAILED");
            job.setErrorSummary(e.getMessage());
            jobRepository.save(job);
        }
    }
}