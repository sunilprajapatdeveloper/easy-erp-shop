package nextpos.app.nextpos.importexport.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nextpos.app.nextpos.importexport.entity.ImportExportJob;
import nextpos.app.nextpos.importexport.repository.ImportExportJobRepository;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
public class ImportJobListener implements JobExecutionListener {

    private final ImportExportJob job;
    private final ImportExportJobRepository jobRepository;

    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info("Starting import job for job {}", job.getId());
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus().isUnsuccessful()) {
            job.setStatus("FAILED");
            job.setErrorSummary("Job failed: " + jobExecution.getAllFailureExceptions().stream()
                    .map(Throwable::getMessage)
                    .findFirst().orElse("Unknown error"));
            job.setCompletedAt(LocalDateTime.now());
            jobRepository.save(job);
        }
    }
}