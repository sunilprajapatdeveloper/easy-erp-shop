package nextpos.app.nextpos.importexport.util;

import org.springframework.web.multipart.MultipartFile;

public class FileTypeDetector {
    public static String detect(MultipartFile file) {
        String original = file.getOriginalFilename();
        if (original != null) {
            int dot = original.lastIndexOf('.');
            if (dot > 0) {
                return original.substring(dot + 1).toLowerCase();
            }
        }
        // Fallback to content type
        String contentType = file.getContentType();
        if ("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet".equals(contentType)) {
            return "xlsx";
        }
        if ("application/vnd.ms-excel".equals(contentType)) {
            return "xls";
        }
        if ("text/csv".equals(contentType)) {
            return "csv";
        }
        return null;
    }
}