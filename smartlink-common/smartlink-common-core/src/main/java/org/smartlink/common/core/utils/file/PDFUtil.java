package org.smartlink.common.core.utils.file;

import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.FileUtil;
import com.aspose.pdf.Document;
import com.aspose.pdf.FormType;
import com.aspose.pdf.devices.JpegDevice;
import com.aspose.pdf.devices.Resolution;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author:chenjianghong
 * @date: 2021/10/28
 */
public class PDFUtil {
    /**
     * pdf校验内容  是否能提取出文字
     */
    /*public static boolean verifyEInvoice(byte[] files) {
        boolean isCheck = false;
        try (PDDocument document = PDDocument.load(files)) {
            PDFTextStripperByArea stripper = new PDFTextStripperByArea();
            stripper.setSortByPosition(false);
            PDFTextStripper tStripper = new PDFTextStripper();
            String pdfFileInText = tStripper.getText(document).replaceAll("[\r\n\\s*]", "");
            isCheck = StrUtil.isNotEmpty(pdfFileInText) || StrUtil.isNotBlank(pdfFileInText);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isCheck;
    }*/

    /**
     * PDF转图片
     *
     * @throws IOException imgType:转换后的图片类型 jpg,png
     */
    /*public static byte[] PDFToImg(byte[] files, int dpi) throws IOException {
        try (PDDocument document = PDDocument.load(files)) {
            PDFRenderer renderer = new PDFRenderer(document);
            BufferedImage bufferedImage = renderer.renderImageWithDPI(0, dpi);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "JPG", out);
            return out.toByteArray();
        }
    }*/

    /**
     * pdf文件转换成一个整体的图片
     * @param data 文件
     * @param dpi dpi
     * @return 字节
     * @throws Exception 异常
     */
    /*public static byte[] convertPdfImage(byte[] data, int dpi) throws Exception{
            PDDocument document = PDDocument.load(data);
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
    }*/
//
//    public static void main(String[] args) throws Exception {
//        byte[] bytes = convertPdfImage(FileUtil.readBytes(new File("/Users/shidunkai/Downloads/发票test/电子发票/电子发票/销货清单很多.pdf")), 60);
//        FileUtil.writeBytes(bytes,"/Users/shidunkai/Downloads/test.png");
//    }


    /**
     * pdf拆分转为多张图片
     */
    /*public static byte[] PDFToManyImg(byte[] fileDate, int dpi) throws Exception {
        List<byte[]> listByte = new ArrayList<>(16);
        *//* dpi越大转换后越清晰，相对转换速度越慢 *//*
        try (PDDocument doc = PDDocument.load(fileDate)) {
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
    }*/


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

    /**
     * PDF转图片
     * @param bytes pdf一定是pdf，否则报错
     * @return 每一页的byte
     * @throws IOException
     */
    public static List<byte[]> pdfToImg(byte[] bytes) {
        //GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
        //java.awt.Font[] fonts = e.getAllFonts();
        //ArrayList<String> list = Arrays.stream(fonts).map(java.awt.Font::getFontName).collect(Collectors.toCollection(ArrayList::new));
        Document document = new Document(bytes);
        document.getForm().setType(FormType.Standard);
        //com.aspose.pdf.Font[] fonts1 = document.getFontUtilities().getAllFonts();
        //Arrays.stream(fonts1).map(com.aspose.pdf.Font::getFontName).filter(fontName -> !list.contains(fontName)).map(fontName -> "系统缺失字体" + fontName).forEachOrdered(System.out::println);
        Resolution resolution = new Resolution(130);
        JpegDevice jpegDevice = new JpegDevice(resolution);
        List<byte[]> fileByteList=new ArrayList<>(16);
        for (int i = 1; i <= document.getPages().size(); i++){
            ByteArrayOutputStream fileOS = new ByteArrayOutputStream();
            jpegDevice.process(document.getPages().get_Item(i), fileOS);
            //FileUtil.writeBytes(fileOS.toByteArray(),"/Users/shidunkai/Downloads/"+i+".jpeg");
            fileByteList.add(fileOS.toByteArray());
        }
        return fileByteList;
    }
}
