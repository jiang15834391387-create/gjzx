package org.smartlink.server.nc.utils.document;

import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.aspose.pdf.Document;
import com.aspose.pdf.Page;
import com.aspose.pdf.devices.JpegDevice;
import com.aspose.pdf.devices.Resolution;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: L
 * @date:
 */
@Slf4j
public class PDFUtil {
    /**
     * pdf校验内容  是否能提取出文字
     */
    public static boolean verifyEInvoice(byte[] files) {
        boolean isCheck = false;
        try (PDDocument document = Loader.loadPDF(files)) {
            PDFTextStripperByArea stripper = new PDFTextStripperByArea();
            stripper.setSortByPosition(false);
            PDFTextStripper tStripper = new PDFTextStripper();
            String pdfFileInText = tStripper.getText(document).replaceAll("[\r\n\\s*]", "");
            isCheck = StrUtil.isNotEmpty(pdfFileInText) || StrUtil.isNotBlank(pdfFileInText);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isCheck;
    }

    /**
     * pdf拆分转为多张图片
     */
    public static byte[] PDFToManyImg(byte[] fileDate, int dpi) throws Exception {
        List<byte[]> listByte = new ArrayList<>(16);
        /* dpi越大转换后越清晰，相对转换速度越慢 */
        try (PDDocument doc = Loader.loadPDF(fileDate)) {
            PDFRenderer renderer = new PDFRenderer(doc);
            int pages = doc.getNumberOfPages();
            for (int i = 0; i < pages; i++) {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                ImageIO.write(renderer.renderImageWithDPI(i, dpi), "jpg", out);
                byte[] b = out.toByteArray();
                listByte.add(b);
                out.close();
            }
        }
        return listByte.get(0);
    }


    /**
     * 保存图片不带后缀
     *
     * @param filePath
     * @param files
     * @throws IOException
     */
    public static void saveFiles(String filePath, byte[] files) throws IOException {
        //filePath 结尾为fileId
        File file = new File(filePath);
        File parentFile = file.getParentFile();
        if (parentFile != null) {
            parentFile.mkdirs();
            if (!parentFile.isDirectory()) {
                throw new IOException("Unable to create parent directories of " + file);
            }
        }
        FileUtil.writeBytes(files, file);
    }

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            System.out.println(IdWorker.getId());
        }
    }

    /**
     * 保存缩略图不带后缀
     *
     * @param filePath
     * @param files
     * @throws IOException
     */
    public static void saveThumbnail(String filePath, byte[] files) throws IOException {
        File file = new File(filePath);
        File fileThumbnail = new File(filePath + "_jpg");
        ImgUtil.compress(file, fileThumbnail, 0.5f);
    }

    /**
     * 删除磁盘图片
     *
     * @param filePath
     * @throws IOException
     */
    public static void deleteFiles(String filePath) {
        FileUtil.del(filePath);
    }

    public static byte[] PDFToImg(byte[] files, int dpi) throws IOException {
        try (PDDocument document = Loader.loadPDF(files)) {
            PDFRenderer renderer = new PDFRenderer(document);
            BufferedImage bufferedImage = renderer.renderImageWithDPI(0, dpi);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "JPG", out);
            return out.toByteArray();
        }
    }

    /**
     * pdf文件转换成一个整体的图片
     *
     * @param data 文件
     * @param dpi  dpi
     * @return 字节
     * @throws Exception 异常
     */
    public static byte[] convertPdfImage(byte[] data, int dpi) throws IOException {
        PDDocument document = Loader.loadPDF(data);
        PDFRenderer pdfRenderer = new PDFRenderer(document);
        // 不知道图片的宽和高，所以先定义个null
        BufferedImage pdfImage = null;
        // pdf有多少页
        int pageSize = document.getNumberOfPages();
        int y = 0;
        int width = 0;
        int height = 0;

        for (int i = 0; i < pageSize; ++i) {
            BufferedImage bim = pdfRenderer.renderImageWithDPI(i, dpi, ImageType.RGB);
            if (width < bim.getWidth()) {
                width = bim.getWidth();
            }
            height += bim.getHeight();
        }

        pdfImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < pageSize; ++i) {
            BufferedImage bim = pdfRenderer.renderImageWithDPI(i, dpi, ImageType.RGB);
            pdfImage.getGraphics().drawImage(bim, 0, y, null);
            y += bim.getHeight();
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(pdfImage, "png", out);
        return out.toByteArray();
    }

    public static byte[] mergeImages(List<byte[]> imageDatas) throws Exception {
        int maxHeight = 0;
        int totalWidth = 0;
        List<BufferedImage> images = new ArrayList<>();
        for (byte[] imageData : imageDatas) {
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageData));
            images.add(image);
            maxHeight += image.getHeight();
            totalWidth = Math.max(totalWidth, image.getWidth());
        }
        BufferedImage mergedImage = new BufferedImage(totalWidth, maxHeight, BufferedImage.TYPE_INT_RGB);

        Graphics2D g2d = mergedImage.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, totalWidth, maxHeight);

        int currentY = 0;
        for (BufferedImage image : images) {
            g2d.drawImage(image, 0, currentY, null);
            currentY += image.getHeight();
        }

        g2d.dispose();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(mergedImage, "jpg", outputStream);
        return outputStream.toByteArray();
    }


    /**
     * pdf文件转换成一个整体的图片
     *
     * @param data 文件
     * @param dpi  dpi
     * @return 字节
     * @throws Exception 异常
     */
    public static byte[] convertPdfImage2(byte[] data, int dpi) throws IOException {
        Document document = new Document(new ByteArrayInputStream(data));
        int totalWidth = 0;
        int totalHeight = 0;
        List<BufferedImage> pageImages = new ArrayList<>();
        for (Page page : document.getPages()) {
            int pageWidth = (int) (page.getPageInfo().getWidth() * dpi / 72.0);
            int pageHeight = (int) (page.getPageInfo().getHeight() * dpi / 72.0);
            totalWidth = Math.max(totalWidth, pageWidth);
            totalHeight += pageHeight;
            ByteArrayOutputStream imageStream = new ByteArrayOutputStream();
            Resolution resolution = new Resolution(dpi);
            JpegDevice jpegDevice = new JpegDevice(resolution);
            jpegDevice.process(page, imageStream);
            BufferedImage pageImage = ImageIO.read(new ByteArrayInputStream(imageStream.toByteArray()));
            pageImages.add(pageImage);
        }
        BufferedImage combinedImage = new BufferedImage(totalWidth, totalHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = combinedImage.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, totalWidth, totalHeight);
        int currentY = 0;
        for (BufferedImage pageImage : pageImages) {
            int pageHeight = pageImage.getHeight();
            g2d.drawImage(pageImage, 0, currentY, null);
            currentY += pageHeight;
        }
        g2d.dispose();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(combinedImage, "jpg", outputStream);
        return outputStream.toByteArray();
    }

    /**
     * pdf文件转换成每一张的图片
     *
     * @param data 文件
     * @param dpi  dpi
     * @return 字节
     * @throws Exception 异常
     */
    public static List<byte[]> convertPdfImages(byte[] data,int dpi) throws IOException {
        List<byte[]> listByte = new ArrayList<>(16);
        final PDDocument document = Loader.loadPDF(data);
        PDFRenderer renderer = new PDFRenderer(document);
        int pageSize = document.getNumberOfPages();
        for (int i =0; i < pageSize; i++) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(renderer.renderImageWithDPI(i, dpi), "jpg", out);
            byte[] b = out.toByteArray();
            listByte.add(b);
            out.close();
        }
        return listByte;
    }
}
