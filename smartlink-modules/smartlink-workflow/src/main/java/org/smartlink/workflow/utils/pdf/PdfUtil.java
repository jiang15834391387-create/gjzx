package org.smartlink.workflow.utils.pdf;

import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.smartlink.workflow.domain.vo.ActHistoryInfoVo;
import org.springframework.stereotype.Controller;

import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PdfUtil {

    public static void exportHistoryRecordPdf(List<ActHistoryInfoVo> list,
                                              OutputStream out) throws Exception {

        try (PDDocument document = new PDDocument()) {

            PDType0Font font = loadCjkFont(document);

            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            float margin = 40;
            float y = page.getMediaBox().getHeight() - margin;

            PDPageContentStream cs = new PDPageContentStream(
                    document,
                    page,
                    PDPageContentStream.AppendMode.OVERWRITE,
                    true
            );

            // 标题
            cs.beginText();
            cs.setFont(font, 16);
            cs.newLineAtOffset(margin, y);
            cs.showText("审批记录");
            cs.endText();

            y -= 30;

            float[] colWidths = {100, 100, 180, 120};
            String[] headers = {"节点", "审批人", "审批意见", "时间"};

            y = drawRow(cs, font, 12, margin, y, colWidths, headers);

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            for (ActHistoryInfoVo vo : list) {

                if (y < 80) {
                    cs.close();
                    page = new PDPage(PDRectangle.A4);
                    document.addPage(page);
                    y = page.getMediaBox().getHeight() - margin;

                    cs = new PDPageContentStream(
                            document,
                            page,
                            PDPageContentStream.AppendMode.OVERWRITE,
                            true
                    );
                    y = drawRow(cs, font, 12, margin, y, colWidths, headers);
                }

                SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String[] row = {
                    safe(vo.getName()),
                    safe(vo.getAssignee()),
                    safe(vo.getNickName()),
                    vo.getEndTime() == null ? "" : SDF.format(vo.getEndTime())
                };

                y = drawRow(cs, font, 11, margin, y, colWidths, row);
            }

            cs.close();
            document.save(out);
        }
    }

    private static PDType0Font loadCjkFont(PDDocument document) throws Exception {
        try (InputStream is = PdfUtil.class.getClassLoader()
                .getResourceAsStream("fonts/SimSun.ttf")) {

            if (is == null) {
                throw new IllegalStateException("缺少中文字体：fonts/SimSun.ttf");
            }
            return PDType0Font.load(document, is, true);
        }
    }

    private static float drawRow(PDPageContentStream cs,
                                 PDType0Font font,
                                 int fontSize,
                                 float x,
                                 float y,
                                 float[] widths,
                                 String[] texts) throws Exception {

        float rowHeight = 20;
        float textY = y - 14;

        float cursorX = x;
        for (int i = 0; i < widths.length; i++) {
            cs.beginText();
            cs.setFont(font, fontSize);
            cs.newLineAtOffset(cursorX, textY);
            cs.showText(truncate(texts[i], 40));
            cs.endText();
            cursorX += widths[i];
        }
        return y - rowHeight;
    }

    private static String truncate(String s, int max) {
        if (s == null) {
            return "";
        }
        return s.length() <= max ? s : s.substring(0, max - 1) + "…";
    }

    private static String safe(String s) {
        return s == null ? "" : s;
    }
}
