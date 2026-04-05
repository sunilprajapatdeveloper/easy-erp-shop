package nextpos.app.nextpos.importexport.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nextpos.app.nextpos.importexport.entity.ImportError;
import nextpos.app.nextpos.importexport.entity.ImportExportJob;
import nextpos.app.nextpos.importexport.repository.ImportErrorRepository;
import org.springframework.batch.core.SkipListener;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class ImportSkipListener implements SkipListener<Map<String, Object>, Object> {

    private final ImportExportJob job;
    private final ImportErrorRepository errorRepository;

    @Override
    public void onSkipInRead(Throwable t) {
        log.warn("Skipping row due to read error: {}", t.getMessage());
    }

    @Override
    public void onSkipInWrite(Object item, Throwable t) {
        log.warn("Skipping item during write: {}", t.getMessage());
    }

    @Override
    public void onSkipInProcess(Map<String, Object> item, Throwable t) {
        log.warn("Skipping row during processing: {}", t.getMessage());
        ImportError error = ImportError.builder()
                .jobId(job.getId())
                .rowNumber(0) // row number not tracked; we can store raw data
                .errorMessage(t.getMessage())
                .rawData(item != null ? item.toString() : null)
                .build();
        errorRepository.save(error);
    }
}