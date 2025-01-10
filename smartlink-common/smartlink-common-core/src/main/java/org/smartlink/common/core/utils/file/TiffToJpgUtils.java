package org.smartlink.common.core.utils.file;

import cn.hutool.core.img.Img;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author shidunkai
 * @title tif转JPG
 * @description tif转JPG
 * @date 2022-04
 */
public class TiffToJpgUtils {
    /**
     * 	将tiff图片转化为jpg
     *  @param bytes 文件字节
     */
    public static byte[] tiffToJpg(byte[] bytes) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            BufferedImage bufferegImage= ImageIO.read(new ByteArrayInputStream(bytes));
            Img.from(bufferegImage).write(out);
        }catch(IOException e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }
}
