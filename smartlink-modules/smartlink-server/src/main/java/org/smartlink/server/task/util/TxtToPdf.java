package org.smartlink.server.task.util;
import com.aspose.words.Document;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * <p>Title: TxtToPdf</p>
 * <p>
 * <p>Description:TxtToPdf
 *
 * @author chenjianghong
 * @date 2021-10-28
 **/
public class TxtToPdf {

    public static byte[] txtToPdf(byte[] bytes) {

        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            Document doc = new Document(new ByteArrayInputStream(bytes));
            //全面支持DOC, DOCX, OOXML, RTF HTML, OpenDocument, PDF, EPUB, XPS, SWF 相互转换
            doc.save(byteArrayOutputStream, com.aspose.words.SaveFormat.PDF);
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }






}
