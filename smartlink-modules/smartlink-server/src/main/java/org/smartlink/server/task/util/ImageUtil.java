package org.smartlink.server.task.util;

import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

public class ImageUtil {

    public static BufferedImage getImageFromUrl(String imageUrl) throws IOException {
        URL url = new URL(imageUrl);
        URLConnection connection = url.openConnection();
        return ImageIO.read(connection.getInputStream());
    }

    public static byte[] toByteArray(BufferedImage image) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        return baos.toByteArray();
    }

    //返回文件类型
    public static String getExtensionFromMimeType(String mimeType) {
        if ("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet".equals(mimeType)) {
            return "xlsx";
        } else if ("application/vnd.ms-excel".equals(mimeType)) {
            return "xls";
        } else if ("application/vnd.openxmlformats-officedocument.wordprocessingml.document".equals(mimeType)) {
            return "docx";
        } else if ("application/msword".equals(mimeType)) {
            return "doc";
        } else if ("application/vnd.openxmlformats-officedocument.presentationml.presentation".equals(mimeType)) {
            return "pptx";
        } else if ("application/vnd.ms-powerpoint".equals(mimeType)) {
            return "ppt";
        } else if ("image/jpeg".equals(mimeType) || "image/png".equals(mimeType) || "image/gif".equals(mimeType)) {
            return mimeType.split("/")[1];
        } else if ("application/pdf".equals(mimeType)) {
            return "pdf";
        } else if ("text/plain".equals(mimeType)) {
            return "txt";
        } else if ("application/ofd".equals(mimeType)) {
            return "ofd";
        }
        return "";
    }

    /**
     * 根据URL地址获取文件的字节数组
     *
     * @param urlString 文件的URL地址
     * @return 文件的字节数组
     * @throws IOException 如果网络请求或文件读取失败
     */
    public static byte[] getFileBytesFromUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        try {
            // 设置请求方式为GET
            connection.setRequestMethod("GET");
            // 设置超时时间
            connection.setConnectTimeout(5000); // 5秒
            connection.setReadTimeout(5000); // 5秒

            // 获取输入流
            InputStream inputStream = connection.getInputStream();

            // 读取输入流到字节数组
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
            }

            // 关闭输入流
            inputStream.close();

            // 返回字节数组
            return outputStream.toByteArray();
        } finally {
            // 断开连接
            connection.disconnect();
        }
    }

    /**
     * 将多种图像格式的字节数组转换为PDF文件的字节数组
     *
     * @param imageBytes 图像的字节数组
     * @param formatName 图像的格式名，如"jpg"、"jpeg"、"png"或"gif"
     * @return 转换后的PDF文件的字节数组
     * @throws IOException 如果发生I/O错误
     */
    public static byte[] convertImageToPdf(byte[] imageBytes, String formatName) throws IOException {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                // 将字节数组转换为BufferedImage
                BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageBytes));

                // 将BufferedImage转换为PDFImageXObject
                PDImageXObject pdImage = PDImageXObject.createFromByteArray(document, imageBytes, formatName);

                // 计算图像的宽度和高度
                float scaleX = page.getMediaBox().getWidth() / image.getWidth();
                float scaleY = page.getMediaBox().getHeight() / image.getHeight();
                float scale = Math.min(scaleX, scaleY);

                // 在PDF页面上绘制图像
                contentStream.drawImage(pdImage,
                        (page.getMediaBox().getWidth() - image.getWidth() * scale) / 2,
                        (page.getMediaBox().getHeight() - image.getHeight() * scale) / 2,
                        image.getWidth() * scale, image.getHeight() * scale);
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            document.save(outputStream);
            document.close();

            return outputStream.toByteArray();
        }
    }

    public static byte[] mergePdfDocuments(List<byte[]> pdfs) throws IOException {
        PDFMergerUtility utility = new PDFMergerUtility();
        for (byte[] pdf : pdfs) {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(pdf);
            utility.addSource(byteArrayInputStream);
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        utility.setDestinationStream(byteArrayOutputStream);
        utility.mergeDocuments(null);
        return byteArrayOutputStream.toByteArray();


    }


}