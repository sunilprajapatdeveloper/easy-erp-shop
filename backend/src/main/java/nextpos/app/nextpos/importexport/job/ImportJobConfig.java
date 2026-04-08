package nextpos.app.nextpos.importexport.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nextpos.app.nextpos.importexport.entity.ImportExportJob;
import nextpos.app.nextpos.importexport.repository.ImportErrorRepository;
import nextpos.app.nextpos.importexport.repository.ImportExportJobRepository;
import nextpos.app.nextpos.importexport.strategy.ImportExportStrategy;
import nextpos.app.nextpos.model.entity.Media;
import nextpos.app.nextpos.repository.MediaRepository;
import nextpos.app.nextpos.service.interf.MediaService;
import org.apache.poi.ss.usermodel.*;
import org.springframework.batch.core.*;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.io.FileSystemResource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class ImportJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final MediaService mediaService;
    private final MediaRepository mediaRepository;
    private final ImportExportJobRepository importExportJobRepository;
    private final ImportErrorRepository errorRepository;

    public Job createImportJob(ImportExportJob job, ImportExportStrategy strategy, Map<String, Object> options) {
        String jobName = "import_" + job.getModule() + "_" + job.getId();

        ImportSkipListener skipListener = new ImportSkipListener(job, errorRepository);

        Step step = new StepBuilder("importStep_" + job.getId(), jobRepository)
                .<Map<String, Object>, Object>chunk(500, transactionManager)
                .reader(createReader(job, options))
                .processor(strategy.getProcessor(options))
                .writer(strategy.getWriter(options))
                .faultTolerant()
                .skip(IllegalArgumentException.class)
                .skip(DataIntegrityViolationException.class)
                .skipLimit(10000)
                .listener(skipListener)
                .listener(new ImportStepListener(job, importExportJobRepository))
                .build();

        return new JobBuilder(jobName, jobRepository)
                .start(step)
                .listener(new ImportJobListener(job, importExportJobRepository))
                .build();
    }

    private ItemReader<Map<String, Object>> createReader(ImportExportJob job, Map<String, Object> options) {
        String fileExtension = (String) options.getOrDefault("fileExtension", "xlsx");
        if ("csv".equalsIgnoreCase(fileExtension)) {
            return createCsvReader(job, options);
        } else {
            return createExcelReader(job, options);
        }
    }

    private ItemReader<Map<String, Object>> createCsvReader(ImportExportJob job, Map<String, Object> options) {
        Media media = mediaRepository.findById(job.getSourceMediaId())
                .orElseThrow(() -> new IllegalStateException("Media not found for job " + job.getId()));

        FileSystemResource resource = new FileSystemResource(media.getFilePath());

        String[] headers;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            String headerLine = reader.readLine();
            if (headerLine == null)
                throw new IllegalStateException("CSV file is empty");
            headers = headerLine.split(",");
            for (int i = 0; i < headers.length; i++) {
                headers[i] = headers[i].trim().replaceAll("^\"|\"$", "");
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read CSV headers", e);
        }

        FlatFileItemReader<Map<String, Object>> reader = new FlatFileItemReader<>();
        reader.setResource(resource);
        reader.setLinesToSkip(1);
        reader.setStrict(true);

        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames(headers);
        tokenizer.setStrict(false);

        DefaultLineMapper<Map<String, Object>> lineMapper = new DefaultLineMapper<>();
        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(fieldSet -> {
            Map<String, Object> map = new HashMap<>();
            for (String name : headers) {
                map.put(name, fieldSet.readString(name));
            }
            return map;
        });
        reader.setLineMapper(lineMapper);

        return reader;
    }

    private ItemReader<Map<String, Object>> createExcelReader(ImportExportJob job, Map<String, Object> options) {
        return new ItemReader<Map<String, Object>>() {
            private Iterator<Map<String, Object>> iterator;

            @Override
            public Map<String, Object> read() throws Exception {
                if (iterator == null) {
                    try (InputStream is = mediaService
                            .loadMediaResourceById(job.getSourceMediaId(), false, job.getCompanyId())
                            .getInputStream()) {
                        Workbook workbook = WorkbookFactory.create(is);
                        Sheet sheet = workbook.getSheetAt(0);
                        List<Map<String, Object>> rows = new ArrayList<>();
                        String[] headers = (String[]) options.get("headers");

                        Row headerRow = sheet.getRow(0);
                        if (headerRow == null) {
                            throw new IllegalStateException("Excel file has no header row");
                        }
                        if (headers == null) {
                            int colCount = headerRow.getLastCellNum();
                            headers = new String[colCount];
                            for (int i = 0; i < colCount; i++) {
                                Cell cell = headerRow.getCell(i);
                                headers[i] = (cell != null ? cell.getStringCellValue() : "col" + i);
                            }
                        }

                        for (int r = 1; r <= sheet.getLastRowNum(); r++) {
                            Row row = sheet.getRow(r);
                            if (row == null)
                                continue;
                            Map<String, Object> rowMap = new HashMap<>();
                            for (int c = 0; c < headers.length; c++) {
                                Cell cell = row.getCell(c);
                                String value = cell != null ? cell.toString() : null;
                                rowMap.put(headers[c], value);
                            }
                            rows.add(rowMap);
                        }
                        iterator = rows.iterator();
                    }
                }
                return iterator.hasNext() ? iterator.next() : null;
            }
        };
    }
}