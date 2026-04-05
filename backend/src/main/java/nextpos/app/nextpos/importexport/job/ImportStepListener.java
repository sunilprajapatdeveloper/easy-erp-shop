package nextpos.app.nextpos.importexport.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nextpos.app.nextpos.importexport.entity.ImportExportJob;
import nextpos.app.nextpos.importexport.repository.ImportExportJobRepository;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
public class ImportStepListener implements StepExecutionListener {

    private final ImportExportJob job;
    private final ImportExportJobRepository jobRepository;

    @Override
    public void beforeStep(StepExecution stepExecution) {
        job.setStartedAt(LocalDateTime.now());
        job.setStatus("PROCESSING");
        jobRepository.save(job);
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        // Convert long counts to int
        job.setProcessedRecords((int) stepExecution.getReadCount());
        job.setSuccessRecords((int) stepExecution.getWriteCount());
        job.setErrorRecords((int) stepExecution.getSkipCount());
        job.setCompletedAt(LocalDateTime.now());
        if (stepExecution.getExitStatus().getExitCode().equals(ExitStatus.COMPLETED.getExitCode())) {
            job.setStatus("COMPLETED");
        } else {
            job.setStatus("FAILED");
            job.setErrorSummary("Step failed: " + stepExecution.getExitStatus().getExitDescription());
        }
        jobRepository.save(job);
        return stepExecution.getExitStatus();
    }
}