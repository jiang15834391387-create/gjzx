package org.smartlink.server.task.util;

import com.aspose.cells.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

/**
 * <p>Title: ExcelToPdf</p>
 * <p>
 * <p>Description:ExcelToPdf
 *
 * @author chenjianghong
 * @date 2021-10-28
 **/
public class ExcelToPdf {



    /**
     * excelToPdf方法的作用是将Excel文件转换为PDF格式
     * 参数说明
     * 入参：
     * bytes：类型为byte[]，表示一个Excel文件的内容。通常，这个字节数组是从文件读取得到的，或者是从其他源（如网络请求）获得的数据。
     * 返回值
     * 出参：
     * byte[]：表示转换后的PDF文件内容。如果转换成功，返回的字节数组可以直接写入文件系统或作为HTTP响应的一部分发送给客户端。
     * @param bytes
     * @return
     */
    public static byte[] excelToPdf(byte[] bytes) {
        String sss = "<License><Data><Products><Product>Aspose.Total for Java</Product><Product>Aspose.Words for Java</Product></Products><EditionType>Enterprise</EditionType>" +
                "<SubscriptionExpiry>20991231</SubscriptionExpiry><LicenseExpiry>20991231</LicenseExpiry><SerialNumber>8bfe198c-7f0c-4ef8-8ff0-acc3237bf0d7</SerialNumber>" +
                "</Data>" +
                "<Signature>sNLLKGMUdF0r8O1kKilWAGdgfs2BvJb/2Xp8p5iuDVfZXmhppo+d0Ran1P9TKdjV4ABwAgKXxJ3jcQTqE/2IRfqwnPf8itN8aFZlV3TJPYeD3yWE7IT55Gz6EijUpC7aKeoohTb4w2fpox58wWoF3SNp6sK6jDfiAUGEHYJ9pjU=</Signature>" +
                "</License>";

        try {
            License aposeLic = new License();
            aposeLic.setLicense(new ByteArrayInputStream(sss.getBytes(StandardCharsets.UTF_8)));
            // 新建一个空白文档
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            Workbook doc = new Workbook(new ByteArrayInputStream(bytes));
            WorksheetCollection worksheetCollection = doc.getWorksheets();
            for (int i = 0; i < worksheetCollection.getCount(); i++) {
                Worksheet sheet = worksheetCollection.get(i);
                sheet.getHorizontalPageBreaks().clear(); ;
                sheet.getVerticalPageBreaks().clear();
                sheet.getPageSetup().setPrintArea("");
                ConditionalFormattingCollection conditionalFormattingCollection = sheet.getConditionalFormattings();
                for (int j = 0; j < conditionalFormattingCollection.getCount(); j++) {
                    FormatConditionCollection formatConditionCollection = conditionalFormattingCollection.get(j);
                    while (formatConditionCollection.getCount() > 0) {
                        formatConditionCollection.removeCondition(0);
                    }
                }
                Cells cells = sheet.getCells();
                cells.deleteBlankColumns();
                cells.deleteBlankRows();
                int rowNum = cells.getMaxDataRow();
                int colNum = sheet.getCells().getMaxColumn();
                int count = cells.getMaxDataColumn();
                for (int m = rowNum; m >= 0; m--) {
                    for (int n = colNum; n > count; n--) {
                        cells.deleteColumn(n);
                    }
                }

            }
            PdfSaveOptions saveOptions = new PdfSaveOptions();
            saveOptions.setAllColumnsInOnePagePerSheet(true);
            doc.save(byteArrayOutputStream, saveOptions);
            doc.dispose();
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
