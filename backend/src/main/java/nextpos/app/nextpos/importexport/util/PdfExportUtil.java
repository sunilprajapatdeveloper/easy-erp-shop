package nextpos.app.nextpos.importexport.util;

import nextpos.app.nextpos.model.entity.Company;
import nextpos.app.nextpos.service.interf.MediaService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public final class PdfExportUtil {

    private PdfExportUtil() {
    }

    public static byte[] generatePdfReport(List<Map<String, Object>> data,
            List<String> headers,
            String module,
            Company company,
            MediaService mediaService) throws IOException {
        try (PDDocument document = new PDDocument()) {
            float margin = 40;
            float headerHeight = 100f; // Increased for better spacing
            float footerHeight = 45f;

            PDType1Font fontBold = PDType1Font.HELVETICA_BOLD;
            PDType1Font fontItalic = PDType1Font.HELVETICA_OBLIQUE;
            PDType1Font fontRegular = PDType1Font.HELVETICA;

            float[] colWidths = calculateAccurateColumnWidths(data, headers, fontRegular, fontBold, 70f, 200f);
            float totalTableWidth = 0;
            for (float w : colWidths)
                totalTableWidth += w;

            float pageWidth = Math.max(PDRectangle.A4.getWidth(), totalTableWidth + (margin * 2));
            PDRectangle pageSize = new PDRectangle(pageWidth, PDRectangle.A4.getHeight());

            int pageNum = 1;

            PDPage page = new PDPage(pageSize);
            document.addPage(page);
            PDPageContentStream stream = new PDPageContentStream(document, page);

            drawHeader(stream, fontBold, fontItalic, pageSize, margin, company, module, data.size());

            float y = pageSize.getHeight() - margin - headerHeight;
            drawTableHeader(stream, fontBold, headers, colWidths, margin, y);
            y -= 25f;

            for (int r = 0; r < data.size(); r++) {
                Map<String, Object> row = data.get(r);
                float rowH = calculateRowHeight(row, headers, colWidths, fontRegular, 7);

                if (y - rowH < margin + footerHeight) {
                    addFooter(stream, pageNum++, pageSize, margin, company);
                    stream.close();

                    page = new PDPage(pageSize);
                    document.addPage(page);
                    stream = new PDPageContentStream(document, page);

                    drawHeader(stream, fontBold, fontItalic, pageSize, margin, company, module, data.size());
                    y = pageSize.getHeight() - margin - headerHeight;
                    drawTableHeader(stream, fontBold, headers, colWidths, margin, y);
                    y -= 25f;
                }

                // Modern Zebra Striping
                if (r % 2 == 0) {
                    stream.setNonStrokingColor(0.97f, 0.98f, 0.99f);
                    stream.addRect(margin, y - rowH, totalTableWidth, rowH);
                    stream.fill();
                }

                stream.setNonStrokingColor(0.2f, 0.2f, 0.2f);
                float x = margin;
                for (int i = 0; i < headers.size(); i++) {
                    stream.setLineWidth(0.2f);
                    stream.setStrokingColor(0.85f, 0.85f, 0.85f);
                    stream.addRect(x, y - rowH, colWidths[i], rowH);
                    stream.stroke();

                    List<String> lines = wrapText(String.valueOf(row.getOrDefault(headers.get(i), "")), colWidths[i],
                            fontRegular, 7);
                    float ty = y - 11;
                    stream.setFont(fontRegular, 7);
                    for (String line : lines) {
                        stream.beginText();
                        stream.newLineAtOffset(x + 5, ty);
                        stream.showText(line);
                        stream.endText();
                        ty -= 9;
                    }
                    x += colWidths[i];
                }
                y -= rowH;
            }

            addFooter(stream, pageNum, pageSize, margin, company);
            stream.close();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            document.save(baos);
            return baos.toByteArray();
        }
    }

    private static void drawHeader(PDPageContentStream stream, PDType1Font bold, PDType1Font italic,
            PDRectangle pageSize, float margin, Company company, String module, int total) throws IOException {
        float startY = pageSize.getHeight();
        float width = pageSize.getWidth();

        // 1. Top Decorative Accent Bar (Modern Branding)
        stream.setNonStrokingColor(0.12f, 0.45f, 0.70f); // Professional Blue
        stream.addRect(0, startY - 5, width, 5);
        stream.fill();

        // 2. Company Identity (Left Side)
        float textY = startY - margin - 15;
        if (company != null) {
            stream.setNonStrokingColor(0.1f, 0.1f, 0.1f);
            stream.setFont(bold, 14);
            drawText(stream, company.getCompanyName() != null ? company.getCompanyName() : "NextPOS Enterprise", margin,
                    textY);

            stream.setFont(PDType1Font.HELVETICA, 8);
            stream.setNonStrokingColor(0.4f, 0.4f, 0.4f);
            float addrY = textY - 14;
            if (company.getAddress() != null) {
                drawText(stream, company.getAddress(), margin, addrY);
                addrY -= 10;
            }
            String contact = (company.getEmail() != null ? company.getEmail() : "") +
                    (company.getPhone() != null ? " | " + company.getPhone() : "");
            drawText(stream, contact, margin, addrY);
        }

        // 3. Report Metadata (Right Side)
        String title = module.toUpperCase().replace("_", " ") + " REPORT";
        stream.setNonStrokingColor(0.12f, 0.45f, 0.70f);
        stream.setFont(bold, 16);
        float titleWidth = getStringWidth(title, bold, 16);
        drawText(stream, title, width - margin - titleWidth, textY);

        stream.setNonStrokingColor(0.5f, 0.5f, 0.5f);
        stream.setFont(italic, 9);
        String meta = "Generated: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm"));
        float metaWidth = getStringWidth(meta, italic, 9);
        drawText(stream, meta, width - margin - metaWidth, textY - 14);

        String countText = "Total Records: " + total;
        float countWidth = getStringWidth(countText, italic, 9);
        drawText(stream, countText, width - margin - countWidth, textY - 26);

        // Header Separator
        stream.setStrokingColor(0.8f, 0.8f, 0.8f);
        stream.setLineWidth(0.5f);
        stream.moveTo(margin, startY - margin - 75);
        stream.lineTo(width - margin, startY - margin - 75);
        stream.stroke();
    }

    private static void drawTableHeader(PDPageContentStream stream, PDType1Font font, List<String> headers,
            float[] widths, float margin, float y, float height) throws IOException {
        float x = margin;
        float totalWidth = 0;
        for (float w : widths)
            totalWidth += w;

        // Header background box
        stream.setNonStrokingColor(0.12f, 0.45f, 0.70f);
        stream.addRect(margin, y - height, totalWidth, height);
        stream.fill();

        stream.setNonStrokingColor(1f, 1f, 1f);
        stream.setStrokingColor(1f, 1f, 1f);
        stream.setLineWidth(0.5f);

        for (int i = 0; i < headers.size(); i++) {
            String txt = headers.get(i).toUpperCase();
            List<String> wrapped = wrapText(txt, widths[i], font, 8);

            float ty = y - 12;
            stream.setFont(font, 8);
            for (String line : wrapped) {
                drawText(stream, line, x + 5, ty);
                ty -= 9;
                if (ty < y - height + 2)
                    break;
            }

            if (i < headers.size() - 1) { // Divider
                stream.moveTo(x + widths[i], y - 2);
                stream.lineTo(x + widths[i], y - height + 2);
                stream.stroke();
            }
            x += widths[i];
        }
    }

    // Overload for compatibility with your calling code
    private static void drawTableHeader(PDPageContentStream stream, PDType1Font font, List<String> headers,
            float[] widths, float margin, float y) throws IOException {
        drawTableHeader(stream, font, headers, widths, margin, y, 25f);
    }

    private static void addFooter(PDPageContentStream stream, int pageNum, PDRectangle pageSize, float margin,
            Company company) throws IOException {
        float footerY = margin;
        float width = pageSize.getWidth();

        // Footer Line
        stream.setStrokingColor(0.9f, 0.9f, 0.9f);
        stream.setLineWidth(0.5f);
        stream.moveTo(margin, footerY + 20);
        stream.lineTo(width - margin, footerY + 20);
        stream.stroke();

        stream.setNonStrokingColor(0.5f, 0.5f, 0.5f);
        stream.setFont(PDType1Font.HELVETICA, 7);

        // Left: Legal/System Tag
        drawText(stream, "System Generated Audit Report | Confidential", margin, footerY + 8);

        // Center: Page Number
        String pg = "Page " + pageNum;
        float pgW = getStringWidth(pg, PDType1Font.HELVETICA, 7);
        drawText(stream, pg, (width / 2) - (pgW / 2), footerY + 8);

        // Right: Copyright
        if (company != null) {
            String copy = "© " + LocalDateTime.now().getYear() + " " + company.getCompanyName();
            float copyW = getStringWidth(copy, PDType1Font.HELVETICA, 7);
            drawText(stream, copy, width - margin - copyW, footerY + 8);
        }
    }

    private static void drawText(PDPageContentStream stream, String text, float x, float y) throws IOException {
        stream.beginText();
        stream.newLineAtOffset(x, y);
        stream.showText(text);
        stream.endText();
    }

    // --- Utility Methods (Logic maintained for stability) ---

    private static float[] calculateAccurateColumnWidths(List<Map<String, Object>> data, List<String> headers,
            PDType1Font body, PDType1Font head, float min, float max) throws IOException {
        float[] widths = new float[headers.size()];
        for (int i = 0; i < headers.size(); i++) {
            widths[i] = Math.max(min, Math.min(getStringWidth(headers.get(i), head, 8) + 20, max));
        }
        int sample = Math.min(data.size(), 100);
        for (int r = 0; r < sample; r++) {
            for (int i = 0; i < headers.size(); i++) {
                float w = getStringWidth(String.valueOf(data.get(r).getOrDefault(headers.get(i), "")), body, 7) + 20;
                widths[i] = Math.max(widths[i], Math.min(w, max));
            }
        }
        return widths;
    }

    private static float getStringWidth(String text, PDType1Font font, float size) throws IOException {
        if (text == null || text.isEmpty())
            return 0;
        try {
            return font.getStringWidth(text.replaceAll("[\\n\\r]", " ")) / 1000f * size;
        } catch (Exception e) {
            return text.length() * size * 0.5f;
        }
    }

    private static float calculateRowHeight(Map<String, Object> row, List<String> headers, float[] widths,
            PDType1Font font, float size) throws IOException {
        int max = 1;
        for (int i = 0; i < headers.size(); i++) {
            max = Math.max(max,
                    wrapText(String.valueOf(row.getOrDefault(headers.get(i), "")), widths[i], font, size).size());
        }
        return (max * 9f) + 10f;
    }

    private static List<String> wrapText(String text, float width, PDType1Font font, float size) throws IOException {
        List<String> lines = new ArrayList<>();
        float limit = width - 12;
        String[] words = text.split(" ");
        StringBuilder current = new StringBuilder();
        for (String word : words) {
            if (getStringWidth(current + " " + word, font, size) < limit) {
                current.append(current.length() == 0 ? "" : " ").append(word);
            } else {
                if (current.length() > 0)
                    lines.add(current.toString());
                current = new StringBuilder(word);
                // If single word is too long, force split it
                while (getStringWidth(current.toString(), font, size) > limit) {
                    int cut = Math.max(1,
                            (int) (current.length() * (limit / getStringWidth(current.toString(), font, size))));
                    lines.add(current.substring(0, cut));
                    current = new StringBuilder(current.substring(cut));
                }
            }
        }
        if (current.length() > 0)
            lines.add(current.toString());
        return lines.isEmpty() ? List.of("") : lines;
    }
}