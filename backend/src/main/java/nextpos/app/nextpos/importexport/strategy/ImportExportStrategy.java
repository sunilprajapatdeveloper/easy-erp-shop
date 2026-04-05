package nextpos.app.nextpos.importexport.strategy;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;

import java.util.List;
import java.util.Map;

public interface ImportExportStrategy {

    /**
     * Name of the module this strategy supports (e.g., "Product")
     */
    String getModuleName();

    /**
     * Provide a reader that reads rows from the file (as Map<String, Object>)
     */
    FlatFileItemReader<Map<String, Object>> getReader(String fileUrl, Map<String, Object> options);

    /**
     * Provide a processor that converts a row to the target entity (or DTO)
     */
    ItemProcessor<Map<String, Object>, Object> getProcessor(Map<String, Object> options);

    /**
     * Provide a writer that persists a batch of entities
     */
    ItemWriter<Object> getWriter(Map<String, Object> options);

    /**
     * Export data based on filters, return a list of rows (each row as Map)
     */
    List<Map<String, Object>> exportData(Map<String, Object> filters);

    /**
     * Column headers for export files (for CSV/Excel headers)
     */
    List<String> getColumnHeaders();

    /**
     * Row transformation for export (convert entity to Map)
     */
    Map<String, Object> toExportRow(Object entity);
}