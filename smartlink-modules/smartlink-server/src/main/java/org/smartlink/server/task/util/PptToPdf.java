package org.smartlink.server.task.util;

import com.aspose.slides.License;
import com.aspose.slides.Presentation;
import com.aspose.slides.SaveFormat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
/**
 * <p>Title: PptToPdf</p>
 * <p>
 * <p>Description:PptToPdf
 *
 * @author chenjianghong
 * @date 2021-10-28
 **/
public class PptToPdf {

    /**
     * PPT转PDF
     * @param bytes
     * @return
     */
    public static byte[] pptToPdf(byte[] bytes) {
        String sss = "<License><Data><Products><Product>Aspose.Total for Java</Product><Product>Aspose.Words for Java</Product></Products><EditionType>Enterprise</EditionType>" +
                "<SubscriptionExpiry>20991231</SubscriptionExpiry><LicenseExpiry>20991231</LicenseExpiry><SerialNumber>8bfe198c-7f0c-4ef8-8ff0-acc3237bf0d7</SerialNumber>" +
                "</Data>" +
                "<Signature>sNLLKGMUdF0r8O1kKilWAGdgfs2BvJb/2Xp8p5iuDVfZXmhppo+d0Ran1P9TKdjV4ABwAgKXxJ3jcQTqE/2IRfqwnPf8itN8aFZlV3TJPYeD3yWE7IT55Gz6EijUpC7aKeoohTb4w2fpox58wWoF3SNp6sK6jDfiAUGEHYJ9pjU=</Signature>" +
                "</License>";
            License aposeLic = new License();
            aposeLic.setLicense(new ByteArrayInputStream(sss.getBytes(StandardCharsets.UTF_8)));
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            Presentation pres = new Presentation(new ByteArrayInputStream(bytes));
            pres.save(byteArrayOutputStream, SaveFormat.Pdf);
            return byteArrayOutputStream.toByteArray();
    }


}
