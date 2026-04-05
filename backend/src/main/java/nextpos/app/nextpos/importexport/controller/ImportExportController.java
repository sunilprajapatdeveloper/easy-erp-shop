package nextpos.app.nextpos.importexport.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nextpos.app.nextpos.importexport.dto.request.ExportRequest;
import nextpos.app.nextpos.importexport.dto.request.ImportRequest;
import nextpos.app.nextpos.importexport.dto.response.ImportErrorResponse;
import nextpos.app.nextpos.importexport.dto.response.JobResponse;
import nextpos.app.nextpos.importexport.dto.response.JobStatusResponse;
import nextpos.app.nextpos.importexport.service.ImportExportService;

import java.io.IOException;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api/v1/import-export")
@RequiredArgsConstructor
public class ImportExportController {

    private final ImportExportService importExportService;
    private final ObjectMapper objectMapper;

    @PostMapping("/import")
    public ResponseEntity<JobResponse> startImport(
            @RequestPart("module") String module,
            @RequestPart("file") MultipartFile file,
            @RequestPart(value = "options", required = false) String optionsJson) throws IOException {

        ImportRequest request = new ImportRequest();
        request.setModule(module);
        request.setFile(file);
        if (optionsJson != null) {
            request.setOptions(objectMapper.readValue(optionsJson, new TypeReference<Map<String, Object>>() {
            }));
        }
        return ResponseEntity.ok(importExportService.startImport(request));
    }

    @PostMapping("/export")
    public ResponseEntity<JobResponse> startExport(@RequestBody @Valid ExportRequest request) {
        JobResponse job = importExportService.startExport(request);
        return ResponseEntity.accepted().body(job);
    }

    @GetMapping("/jobs/{jobId}")
    public ResponseEntity<JobStatusResponse> getJobStatus(@PathVariable Long jobId) {
        JobStatusResponse status = importExportService.getJobStatus(jobId);
        return ResponseEntity.ok(status);
    }

    @GetMapping("/jobs/{jobId}/errors")
    public ResponseEntity<Page<ImportErrorResponse>> getJobErrors(
            @PathVariable Long jobId,
            Pageable pageable) {
        Page<ImportErrorResponse> errors = importExportService.getJobErrors(jobId, pageable);
        return ResponseEntity.ok(errors);
    }

    @GetMapping("/history")
    public ResponseEntity<Page<JobResponse>> getHistory(
            @RequestParam(required = false) String module,
            @RequestParam(required = false) String type,
            Pageable pageable) {
        Page<JobResponse> history = importExportService.getHistory(module, type, pageable);
        return ResponseEntity.ok(history);
    }

    @DeleteMapping("/jobs/{jobId}")
    public ResponseEntity<Void> cancelJob(@PathVariable Long jobId) {
        importExportService.cancelJob(jobId);
        return ResponseEntity.noContent().build();
    }
}