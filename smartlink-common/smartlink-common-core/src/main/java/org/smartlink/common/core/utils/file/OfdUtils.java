package org.smartlink.common.core.utils.file;

import cn.hutool.core.io.FileUtil;
import org.ofdrw.converter.ConvertHelper;
import org.ofdrw.converter.ImageMaker;
import org.ofdrw.converter.export.ImageExporter;
import org.ofdrw.reader.OFDReader;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author shidunkai
 * @title ofd处理工具
 * @description ofd工具处理
 * @date 2022-04
 */
public class OfdUtils {

    public static ByteArrayOutputStream exportOfdToStream(byte[] ofdBytes, String imageFormat, double dpi) throws Exception {
        // 输出流，用于存储生成的图片
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        // 使用 ImageExporter 从 OFD 字节数组生成图片
        try (OFDReader reader = new OFDReader(new ByteArrayInputStream(ofdBytes))) {
            ImageMaker imageMaker = new ImageMaker(reader, dpi);
            // 遍历所有页面并导出图片
            for (int i = 0; i < imageMaker.pageSize(); i++) {
                BufferedImage image = imageMaker.makePage(i);
                // 将生成的图片写入到输出流
                ImageIO.write(image, imageFormat, outputStream);
            }
        }
        // 返回生成图片的字节流
        return outputStream;
    }

    public static byte[] ofdToJpgOne(byte[] bytes) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try(OFDReader reader = new OFDReader(new ByteArrayInputStream(bytes))) {
            ImageMaker imageMaker = new ImageMaker(reader, 15);
            //初始化out图片
            int outHeight=0;
            int outWeight=0;
            //for (int i = 0; i < imageMaker.pageSize(); i++) {
                BufferedImage bufferedImage = imageMaker.makePage(0);
                outHeight+=bufferedImage.getHeight();
                if (bufferedImage.getWidth()>outWeight){
                    outWeight=bufferedImage.getWidth();
                }
            //}
            BufferedImage  imageNew = new  BufferedImage(outWeight,outHeight,BufferedImage.TYPE_INT_ARGB);
            int  newHeight=0;
            //for (int i = 0; i < imageMaker.pageSize(); i++) {
                BufferedImage  imageOne = imageMaker.makePage(0);
                int height = imageOne.getHeight();
                int width = imageOne.getWidth();
                int[]  imageArray  =  new  int[width*height];
                imageArray  =  imageOne.getRGB(0,0,width,height,imageArray,0,width);
                imageNew.setRGB(0,newHeight,width,height,imageArray,0,width);
                newHeight += imageOne.getHeight();
            //}
            ImageIO.write(imageNew, "PNG", out);
            return out.toByteArray();
        }
    }


    public static byte[] ofdToPdf(byte[] bytes) {
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        // 3. OFD转换PDF
        ConvertHelper.toPdf(in, out);
        return out.toByteArray();
    }

    public static byte[] documentToImg(byte[] bytes) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try(OFDReader reader = new OFDReader(new ByteArrayInputStream(bytes))) {
            ImageMaker imageMaker = new ImageMaker(reader, 15);
            //初始化out图片
            int outHeight=0;
            int outWeight=0;
            for (int i = 0; i < imageMaker.pageSize(); i++) {
            BufferedImage bufferedImage = imageMaker.makePage(i);
            outHeight+=bufferedImage.getHeight();
            if (bufferedImage.getWidth()>outWeight){
                outWeight=bufferedImage.getWidth();
            }
            }
            BufferedImage  imageNew = new  BufferedImage(outWeight,outHeight,BufferedImage.TYPE_INT_RGB);
            int  newHeight=0;
            for (int i = 0; i < imageMaker.pageSize(); i++) {
                BufferedImage imageOne = imageMaker.makePage(i);
                int height = imageOne.getHeight();
                int width = imageOne.getWidth();
                int[] imageArray = new int[width * height];
                imageArray = imageOne.getRGB(0, 0, width, height, imageArray, 0, width);
                imageNew.setRGB(0, newHeight, width, height, imageArray, 0, width);
                newHeight += imageOne.getHeight();
            }
            ImageIO.write(imageNew, "PNG", out);
            return out.toByteArray();
        }
    }

    /**
     * 从OFD文件中提取XML内容（使用临时文件）
     * @param ofdBytes OFD文件字节数组
     * @return XML字符串
     * @throws IOException 当提取失败时抛出异常
     */
    public static String extractXmlFromOfdWithTemp(byte[] ofdBytes) throws IOException {
        // 创建临时文件
        java.io.File tempFile = java.io.File.createTempFile("ofd", ".ofd");
        tempFile.deleteOnExit();

        // 写入OFD内容
        java.nio.file.Files.write(tempFile.toPath(), ofdBytes);

        // 读取XML内容
        try (java.util.zip.ZipFile zipFile = new java.util.zip.ZipFile(tempFile)) {
            String xmlPath = "Doc_0/Attachs/original_invoice.xml";
            java.util.zip.ZipEntry entry = zipFile.getEntry(xmlPath);
            if (entry == null) {
                throw new RuntimeException(xmlPath + " 不存在");
            }

            try (InputStream xmlInputStream = zipFile.getInputStream(entry)) {
                return new String(xmlInputStream.readAllBytes(), java.nio.charset.StandardCharsets.UTF_8);
            }
        }
    }
}
