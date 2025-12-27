package nextpos.app.nextpos.service.helper;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.EAN13Writer;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Random;

@Component
public class BarcodeHelper {

    protected static final Random RANDOM = new Random();

    /**
     * Generates a valid 13-digit EAN-13 barcode.
     * Format: 3-digit prefix (e.g., '890') + 9-digit random + 1-digit checksum
     */
    public String generateBarcode() {
        String prefix = "890"; // Country or company code
        String randomDigits = String.format("%09d", RANDOM.nextInt(1_000_000_000));
        String base = prefix + randomDigits;
        int checksum = calculateEAN13Checksum(base);
        return base + checksum;
    }

    /**
     * Calculates EAN-13 checksum using Modulo 10 algorithm.
     */
    protected int calculateEAN13Checksum(String base) {
        int sum = 0;
        for (int i = 0; i < base.length(); i++) {
            int digit = Character.getNumericValue(base.charAt(i));
            sum += (i % 2 == 0) ? digit : digit * 3;
        }
        int mod = sum % 10;
        return (mod == 0) ? 0 : 10 - mod;
    }

    /**
     * Generates a barcode image in PNG format using ZXing.
     *
     * @param barcodeText The EAN-13 barcode to encode
     * @param filePath    The destination file path (e.g.,
     *                    "barcodes/8901234567890.png")
     */
    public void generateBarcodeImage(String barcodeText, String filePath) throws WriterException, IOException {
        if (barcodeText == null || barcodeText.length() != 13) {
            throw new IllegalArgumentException("Barcode must be 13 digits long.");
        }

        EAN13Writer writer = new EAN13Writer();
        BitMatrix bitMatrix = writer.encode(barcodeText, BarcodeFormat.EAN_13, 300, 150);

        Path path = FileSystems.getDefault().getPath(filePath);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
    }

    /**
     * Generates a basic product code using current timestamp.
     * Format: PRD-<timestamp>
     */
    public String generateProductCode() {
        return "PRD" + System.currentTimeMillis();
    }
}