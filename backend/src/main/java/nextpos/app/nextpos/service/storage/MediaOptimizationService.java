package nextpos.app.nextpos.service.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.resizers.configurations.ScalingMode;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class MediaOptimizationService {

    @Async
    public byte[] optimizeImage(byte[] imageBytes, String mimeType, int maxWidth, int maxHeight) throws IOException {
        if (!mimeType.startsWith("image/")) {
            return imageBytes;
        }

        try (InputStream inputStream = new ByteArrayInputStream(imageBytes);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            Thumbnails.of(inputStream)
                    .size(maxWidth, maxHeight)
                    .scalingMode(ScalingMode.PROGRESSIVE_BILINEAR)
                    .outputFormat(getFormat(mimeType))
                    .outputQuality(0.85)
                    .toOutputStream(outputStream);

            return outputStream.toByteArray();
        }
    }

    private String getFormat(String mimeType) {
        if (mimeType == null) {
            return "jpg";
        }

        switch (mimeType.toLowerCase()) {
            case "image/jpeg":
            case "image/jpg":
                return "jpg";
            case "image/png":
                return "png";
            case "image/webp":
                return "webp";
            default:
                return "jpg";
        }
    }

    public byte[] compressPdf(byte[] pdfBytes) throws IOException {
        // Implement PDF compression using libraries like PDFBox
        // For now, return original
        return pdfBytes;
    }

    public byte[] compressVideo(byte[] videoBytes) throws IOException {
        // Implement video compression using FFmpeg
        // For now, return original
        return videoBytes;
    }
}