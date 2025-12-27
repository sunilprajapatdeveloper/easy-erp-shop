package nextpos.app.nextpos.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ReferenceNumberGenerator {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    /**
     * Generate a reference number with a given prefix.
     * Example: SALE-20250615103045-1234
     * 
     * @param prefix The prefix for the reference number (e.g. SALE, SALE_RETURN)
     * @return Generated unique reference number string
     */
    public static String generateReferenceNumber(String prefix) {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        int randomNum = (int) (Math.random() * 9000) + 1000; // random 4-digit number
        return prefix + timestamp + randomNum;
    }
}
