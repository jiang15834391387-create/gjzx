package org.smartlink.workflow.utils.pdf;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.smartlink.workflow.domain.vo.ActHistoryInfoVo;

import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * 审批记录 PDF 导出工具
 */
public class PdfUtil {

    private static final float MARGIN = 40F;
    private static final float ROW_HEIGHT = 22F;
    private static final float TITLE_GAP = 30F;
    private static final float PAGE_BOTTOM_GUARD = 70F;

    private static final int TITLE_FONT_SIZE = 16;
    private static final int HEADER_FONT_SIZE = 12;
    private static final int BODY_FONT_SIZE = 11;

    private static final float[] COL_WIDTHS = {50F, 90F, 80F, 190F, 110F};
    private static final String[] HEADERS = {"序号", "节点", "审批人", "审批意见", "时间"};

    public static void exportHistoryRecordPdf(List<ActHistoryInfoVo> list, OutputStream out) throws Exception {
        try (PDDocument document = new PDDocument()) {
            PDType0Font font = loadCjkFont(document);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            PDPageContentStream cs = null;
            try {
                cs = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.OVERWRITE, true);
                float y = page.getMediaBox().getHeight() - MARGIN;

                drawCenteredTitle(cs, page, font, "审批记录");
                y -= TITLE_GAP;

                y = drawRow(cs, font, HEADER_FONT_SIZE, MARGIN, y, COL_WIDTHS, HEADERS, true);

                for (int i = 0; i < list.size(); i++) {
                    ActHistoryInfoVo vo = list.get(i);

                    // 当前页空间不足时分页，并重画表头
                    if (y - ROW_HEIGHT < PAGE_BOTTOM_GUARD) {
                        cs.close();
                        page = new PDPage(PDRectangle.A4);
                        document.addPage(page);
                        cs = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.OVERWRITE, true);

                        y = page.getMediaBox().getHeight() - MARGIN;
                        y = drawRow(cs, font, HEADER_FONT_SIZE, MARGIN, y, COL_WIDTHS, HEADERS, true);
                    }

                    String[] row = {
                        String.valueOf(i + 1),
                        safe(vo.getName()),
                        safe(vo.getNickName()),
                        safe(vo.getComment()),
                        vo.getEndTime() == null ? "" : sdf.format(vo.getEndTime())
                    };

                    y = drawRow(cs, font, BODY_FONT_SIZE, MARGIN, y, COL_WIDTHS, row, false);
                }
            } finally {
                if (cs != null) {
                    cs.close();
                }
            }

            document.save(out);
        }
    }

    private static PDType0Font loadCjkFont(PDDocument document) throws Exception {
        try (InputStream is = PdfUtil.class.getClassLoader().getResourceAsStream("fonts/SimSun.ttf")) {
            if (is == null) {
                throw new IllegalStateException("缺少中文字体：fonts/SimSun.ttf");
            }
            return PDType0Font.load(document, is, true);
        }
    }

    private static void drawCenteredTitle(PDPageContentStream cs,
                                          PDPage page,
                                          PDType0Font font,
                                          String title) throws Exception {
        float titleWidth = textWidth(font, TITLE_FONT_SIZE, title);
        float titleX = (page.getMediaBox().getWidth() - titleWidth) / 2;
        float titleY = page.getMediaBox().getHeight() - MARGIN;

        cs.beginText();
        cs.setFont(font, TITLE_FONT_SIZE);
        cs.newLineAtOffset(titleX, titleY);
        cs.showText(title);
        cs.endText();
    }

    private static float drawRow(PDPageContentStream cs,
                                 PDType0Font font,
                                 int fontSize,
                                 float startX,
                                 float y,
                                 float[] widths,
                                 String[] texts,
                                 boolean centered) throws Exception {

        float rowBottomY = y - ROW_HEIGHT;
        float baselineY = rowBottomY + (ROW_HEIGHT - fontSize) / 2 + 2;

        float x = startX;
        for (int i = 0; i < widths.length; i++) {
            float width = widths[i];

            // 单元格边框（网格）
            cs.addRect(x, rowBottomY, width, ROW_HEIGHT);
            cs.stroke();

            String raw = i < texts.length ? texts[i] : "";
            String text = truncate(raw, 40);

            float textX;
            if (centered) {
                float w = textWidth(font, fontSize, text);
                textX = x + Math.max(4F, (width - w) / 2);
            } else {
                textX = x + 4F;
            }

            cs.beginText();
            cs.setFont(font, fontSize);
            cs.newLineAtOffset(textX, baselineY);
            cs.showText(text);
            cs.endText();

            x += width;
        }

        return rowBottomY;
    }

    private static float textWidth(PDType0Font font, int fontSize, String text) throws Exception {
        return font.getStringWidth(safe(text)) / 1000F * fontSize;
    }

    private static String truncate(String text, int max) {
        if (text == null) {
            return "";
        }
        return text.length() <= max ? text : text.substring(0, max - 1) + "…";
    }

    private static String safe(String text) {
        return text == null ? "" : text;
    }
}
