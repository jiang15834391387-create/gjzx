package org.smartlink.common.core.utils.file;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.smartlink.common.core.domain.model.UrlMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 文件处理工具类
 *
 * @author Lion Li
 */

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileUtils extends FileUtil {

    /**
     * 下载文件名重新编码
     *
     * @param response     响应对象
     * @param realFileName 真实文件名
     */
    public static void setAttachmentResponseHeader(HttpServletResponse response, String realFileName) {
        String percentEncodedFileName = percentEncode(realFileName);
        String contentDispositionValue = "attachment; filename=%s;filename*=utf-8''%s".formatted(percentEncodedFileName, percentEncodedFileName);
        response.addHeader("Access-Control-Expose-Headers", "Content-Disposition,download-filename");
        response.setHeader("Content-disposition", contentDispositionValue);
        response.setHeader("download-filename", percentEncodedFileName);
    }

    /**
     * 百分号编码工具方法
     *
     * @param s 需要百分号编码的字符串
     * @return 百分号编码后的字符串
     */
    public static String percentEncode(String s) {
        String encode = URLEncoder.encode(s, StandardCharsets.UTF_8);
        return encode.replaceAll("\\+", "%20");
    }


    /**
     * 获取文件后缀
     */
    public static String getFileSuffix(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
    }

    /**
     * 获取文件名的后缀
     *
     * @param file 表单文件
     * @return 后缀名
     */
    public static String getExtension(MultipartFile file) {
        return FilenameUtils.getExtension(file.getOriginalFilename());
    }

    /**
     * 大图片压缩方法
     *
     * @param imageFile 文件字节
     * @param extension 文件类型
     * @return 字节
     */
    public static ByteArrayOutputStream thumbnailImage(MultipartFile imageFile, String extension) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(imageFile.getBytes().length);
        byte[] bytes = imageFile.getBytes();
        for (String s : MimeTypeUtils.IMAGE_EXTENSION_TRANSFORMATION) {
            if (extension.equalsIgnoreCase(s)) {
                bytes = TiffToJpgUtils.tiffToJpg(bytes);
                break;
            }
        }
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        BufferedImage readImage = ImageIO.read(byteArrayInputStream);
        int scale = calculateSize(readImage.getWidth(), readImage.getHeight());
        int heightDiv = Integer.parseInt(NumberUtil.decimalFormat("0",NumberUtil.div(readImage.getHeight(), scale)));
        int widthDiv = Integer.parseInt(NumberUtil.decimalFormat("0",NumberUtil.div(readImage.getWidth(), scale)));
        if (bytes.length > 1000 * 1024) {
            //压缩
            for (String s : MimeTypeUtils.IMAGE_TRANS_EXTENSION) {
                if (extension.equalsIgnoreCase(s)) {
                    //转类型
                    extension = "jpg";
                    Thumbnails.of(readImage).size(widthDiv, heightDiv).outputFormat(extension).toOutputStream(byteArrayOutputStream);
                    return byteArrayOutputStream;
                }
            }
            //不转
            Thumbnails.of(readImage).size(widthDiv, heightDiv).outputFormat(extension).toOutputStream(byteArrayOutputStream);
            return byteArrayOutputStream;
        } else {
            for (String s : MimeTypeUtils.IMAGE_TRANS_EXTENSION) {
                if (extension.equalsIgnoreCase(s)) {
                    //转类型
                    extension = "jpg";
                    Thumbnails.of(readImage).size(readImage.getWidth(), readImage.getHeight()).outputFormat(extension).toOutputStream(byteArrayOutputStream);
                    return byteArrayOutputStream;
                }
            }
        }
        byteArrayOutputStream.write(bytes, 0, bytes.length);
        return byteArrayOutputStream;
    }

    /**
     * 大图片压缩方法
     *
     * @param bytes 文件数组
     * @param extension 文件类型
     * @return 字节
     */
    public static ByteArrayOutputStream thumbnailImage(byte[] bytes, String extension) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(bytes.length);
        for (String s : MimeTypeUtils.IMAGE_EXTENSION_TRANSFORMATION) {
            if (extension.equalsIgnoreCase(s)) {
                bytes = TiffToJpgUtils.tiffToJpg(bytes);
                break;
            }
        }
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        BufferedImage readImage = ImageIO.read(byteArrayInputStream);
        int scale = calculateSize(readImage.getWidth(), readImage.getHeight());
        int heightDiv = NumberUtil.round(readImage.getHeight(), scale).intValue();
        int widthDiv = NumberUtil.round(readImage.getWidth(), scale).intValue();
        if (bytes.length > 1000 * 1024) {
            //压缩
            for (String s : MimeTypeUtils.IMAGE_TRANS_EXTENSION) {
                if (extension.equalsIgnoreCase(s)) {
                    //转类型
                    extension = "jpg";
                    Thumbnails.of(readImage).size(widthDiv, heightDiv).outputFormat(extension).toOutputStream(byteArrayOutputStream);
                    return byteArrayOutputStream;
                }
            }
            //不转
            Thumbnails.of(readImage).size(widthDiv, heightDiv).outputFormat(extension).toOutputStream(byteArrayOutputStream);
            return byteArrayOutputStream;
        } else {
            for (String s : MimeTypeUtils.IMAGE_TRANS_EXTENSION) {
                if (extension.equalsIgnoreCase(s)) {
                    //转类型
                    extension = "jpg";
                    Thumbnails.of(readImage).size(readImage.getWidth(), readImage.getHeight()).outputFormat(extension).toOutputStream(byteArrayOutputStream);
                    return byteArrayOutputStream;
                }
            }
        }
        byteArrayOutputStream.write(bytes, 0, bytes.length);
        return byteArrayOutputStream;
    }

    /**
     * 生成缩略图方法
     *
     * @param imageBytes 文件字节
     * @return BufferedImage buffer
     */
    public static ByteArrayOutputStream thumbnailSmall(byte[] imageBytes) throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(imageBytes);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Thumbnails.of(in).scale(0.3f).toOutputStream(byteArrayOutputStream);
        return byteArrayOutputStream;
    }

    /**
     * 根据图片宽高计算压缩尺寸
     *
     * @param srcWidth  图片宽度
     * @param srcHeight 图片高度
     * @return 压缩比例
     */
    private static int calculateSize(int srcWidth, int srcHeight) {
        srcWidth = srcWidth % 2 == 1 ? srcWidth + 1 : srcWidth;
        srcHeight = srcHeight % 2 == 1 ? srcHeight + 1 : srcHeight;
        int longSide = Math.max(srcWidth, srcHeight);
        int shortSide = Math.min(srcWidth, srcHeight);
        float scale = ((float) shortSide / longSide);
        if (scale <= 1 && scale > 0.5625) {
            if (longSide < 1664) {
                return 1;
            } else if (longSide < 4990) {
                return 2;
            } else if (longSide > 4990 && longSide < 10240) {
                return 4;
            } else {
                return longSide / 1280;
            }
        } else if (scale <= 0.5625 && scale > 0.5) {
            return longSide / 1280 == 0 ? 1 : longSide / 1280;
        } else {
            return (int) Math.ceil(longSide / (1280.0 / scale));
        }
    }


    public static String getFileUrlResource(String fileUrl,String fileId) {
        if (fileUrl.contains(Constants.RESOURCE_PREFIX)) {
            fileUrl = fileUrl.substring(0,fileUrl.lastIndexOf(File.separator)+1)+fileId;
        }else{
            fileUrl = fileUrl.substring(0,fileUrl.lastIndexOf(File.separator)+1)+fileId;
        }
        return fileUrl;
    }
    /**
     * 切图
     *
     * @param fileBytes 大图
     * @param region    坐标
     * @param base64    小图base64
     * @param option    是否启动base64切分
     */
    public static byte[] imageCut(byte[] fileBytes, String[] region, String base64, String option, int orientation) throws IOException {
        if (region == null || region.length < 2) {
            return fileBytes;
        }
        if (StrUtil.equals(option, "base64")) {
            return Base64.decode(base64);
        } else {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(fileBytes);
            int[] intRegion = Arrays.stream(region).mapToInt(Integer::parseInt).toArray();
            Rectangle rectangle = new Rectangle(intRegion[0], intRegion[1], intRegion[2] - intRegion[0], intRegion[3] - intRegion[1]);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            Thumbnails.of(byteArrayInputStream).sourceRegion(rectangle).size(intRegion[2] - intRegion[0], intRegion[3] - intRegion[1]).outputQuality(1f).rotate(orientation).toOutputStream(byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        }
    }

    /**
     * 从邮件内容中提取文件链接
     * @param emailContent 邮件内容
     * @return 文件链接
     */
    public static String extractFileUrlFromEmail(String emailContent) {
        // 示例：提取邮件内容中的 URL
        Pattern urlPattern = Pattern.compile("http[s]?://[^\\s]+");
        Matcher matcher = urlPattern.matcher(emailContent);
        return matcher.find() ? matcher.group() : null;
    }

    public static MultipartFile downloadFileFromUrl(String fileUrl) throws Exception {
        HttpURLConnection connection = (HttpURLConnection) new URL(fileUrl).openConnection();
        connection.setRequestMethod("GET");

        // 检查响应码
        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new IOException("Failed to download file, HTTP response code: " + responseCode);
        }

        // 获取文件内容
        try (InputStream inputStream = connection.getInputStream();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            byte[] fileContent = outputStream.toByteArray();
            String fileName = Paths.get(new URL(fileUrl).getPath()).getFileName().toString();
            String contentType = Files.probeContentType(Paths.get(fileName));

            return new UrlMultipartFile(fileContent, fileName, contentType);
        }
    }

}
