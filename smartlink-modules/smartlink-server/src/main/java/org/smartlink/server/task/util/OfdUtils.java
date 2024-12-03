package org.smartlink.server.task.util;

import org.ofdrw.converter.ConvertHelper;
import org.ofdrw.converter.ImageMaker;
import org.ofdrw.reader.OFDReader;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * @author L
 * @title ofd处理工具
 * @description ofd工具处理
 * @date
 */
public class OfdUtils {
    /**
     * 该方法将OFD文件转换为单页的PNG图像。目前的方法实现仅转换第一页，但是代码中有注释掉的部分表明它原本是打算转换整个文档的每一页，并将它们拼接成一张大的图像
     * 入参
     * bytes：类型为byte[]，表示一个OFD文件的内容。通常，这个字节数组是从文件读取得到的，或者是从其他源（如网络请求）获得的数据。
     * 出参
     * byte[]：表示转换后的单页PNG图像内容。如果转换成功，返回的字节数组可以直接写入文件系统或作为HTTP响应的一部分发送给客户端。
     * @param bytes
     * @return
     * @throws Exception
     */
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
            BufferedImage  imageNew = new  BufferedImage(outWeight,outHeight,BufferedImage.TYPE_INT_RGB);
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


    /**
     * 该方法将OFD文件转换为PDF格式的文件。
     * 入参
     * bytes：类型为byte[]，表示一个OFD文件的内容。
     * 出参
     * byte[]：表示转换后的PDF文件内容。如果转换成功，返回的字节数组可以直接写入文件系统或作为HTTP响应的一部分发送给客户端。
     * @param bytes
     * @return
     */
    public static byte[] ofdToPdf(byte[] bytes) {
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        // 3. OFD转换PDF
        ConvertHelper.toPdf(in, out);
        return out.toByteArray();
    }

    /**
     * 该方法将OFD文件转换为多页的PNG图像，并将这些图像拼接成一张大的图像。
     * 入参
     * bytes：类型为byte[]，表示一个OFD文件的内容。
     * 出参
     * byte[]：表示转换后的多页PNG图像内容。如果转换成功，返回的字节数组可以直接写入文件系统或作为HTTP响应的一部分发送给客户端。
     * @param bytes
     * @return
     * @throws Exception
     */
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
}
