package org.smartlink.workflow.utils.pdf;

//import org.apache.pdfbox.Loader;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.apache.pdfbox.pdmodel.interactive.form.PDTextField;
import org.smartlink.workflow.common.enums.ResourcePathType;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.util.CollectionUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class PdfProcessUtil {

    /**
     * 根据模板生成PDF文件并返回字节数组
     *
     * @param templatePath 模板路径
     * @param pathType     路径类型枚举
     * @param replacements 替换参数（key: 表单字段名，value: 替换值）
     * @return 生成的PDF文件字节数组
     * @throws IOException 模板读取失败或渲染异常时抛出
     */
    public static byte[] generatePdf(String templatePath, ResourcePathType pathType,
                                     Map<String, Object> replacements) throws IOException {
        // 1. 根据路径类型加载模板
        Resource resource = getResourceByType(templatePath, pathType);

        // 2. 渲染模板并输出为字节数组
        try (InputStream inputStream = getInputStream(resource);
//             PDDocument document = Loader.loadPDF(inputStream.readAllBytes());
             PDDocument document = PDDocument.load(inputStream);
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            fillPdfForm(document, replacements);
            document.save(outputStream);
            return outputStream.toByteArray();
        }
    }

    /**
     * 合并多个PDF文件为一个PDF文件（返回字节数组）
     *
     * @param pdfsToMerge 要合并的PDF字节数组列表
     * @return 合并后的PDF字节数组
     * @throws IOException 如果合并失败
     */
    public static byte[] mergePdfs(List<byte[]> pdfsToMerge) throws IOException {
        PDFMergerUtility merger = new PDFMergerUtility();
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            merger.setDestinationStream(outputStream);

            for (byte[] pdfBytes : pdfsToMerge) {
//                try (PDDocument doc = Loader.loadPDF(pdfBytes)) {
                try (PDDocument doc = PDDocument.load(pdfBytes)) {
                    merger.appendDocument(doc, doc);
                }
            }

            merger.mergeDocuments(null);
            return outputStream.toByteArray();
        }
    }

    /**
     * 填充PDF表单字段
     */
    private static void fillPdfForm(PDDocument document, Map<String, Object> replacements) throws IOException {
        PDDocumentCatalog catalog = document.getDocumentCatalog();
        PDAcroForm acroForm = catalog.getAcroForm();
        if (acroForm == null) {
            return;
        }

        Map<String, Map<String, Object>> mapPage = new LinkedHashMap<>();
        Map<String, Object> mapNotPage = new HashMap<>();
        for (String key : replacements.keySet()) {
            if (key.contains("page")) {
                mapPage.put(key, (Map<String, Object>) replacements.get(key));
            } else {
                mapNotPage.put(key, replacements.get(key));
            }
        }
        if(CollectionUtils.isEmpty(mapPage)){
            PDResources resources = acroForm.getDefaultResources();
            try (InputStream fontStream = new ClassPathResource("fonts/SimSun.ttf").getInputStream()) {
                PDFont font = PDType0Font.load(document, fontStream, false);
                String fontName = resources.add(font).getName();
                String defaultAppearance = "/" + fontName + " 12 Tf 0 g";
                acroForm.setDefaultAppearance(defaultAppearance);

                for (String key : mapNotPage.keySet()) {
                    PDTextField field = (PDTextField) acroForm.getField(key);
                    if (field != null) {
                        field.setDefaultAppearance(defaultAppearance);
                        Object o = mapNotPage.get(key);
                        if(o!=null){
                            field.setValue(o.toString());
                        }
                    }
                }}
        }else {
        for (String pageKey : mapPage.keySet()) {
            // 设置中文字体
            PDResources resources = acroForm.getDefaultResources();
            try (InputStream fontStream = new ClassPathResource("fonts/SimSun.ttf").getInputStream()) {
                PDFont font = PDType0Font.load(document, fontStream, false);
                String fontName = resources.add(font).getName();
                String defaultAppearance = "/" + fontName + " 12 Tf 0 g";
                acroForm.setDefaultAppearance(defaultAppearance);

                for (String key : mapNotPage.keySet()) {
                    PDTextField field = (PDTextField) acroForm.getField(key);
                    if (field != null) {
                        field.setDefaultAppearance(defaultAppearance);
                        Object o = mapNotPage.get(key);
                        if(o!=null){
                            field.setValue(o.toString());
                        }
                    }
                }

                for (String key : mapPage.get(pageKey).keySet()) {
                    PDTextField field = (PDTextField) acroForm.getField(key);
                    if (field != null) {
                        field.setDefaultAppearance(defaultAppearance);
                        Map<String, Object> map = mapPage.get(pageKey);
                        Object o = map.get(key);
                        if(o!=null){
                            field.setValue(o.toString());
                        }

                    }
                }
            }
        }

        }
    }

    /**
     * 根据资源类型获取Resource对象
     */
    private static Resource getResourceByType(String templatePath, ResourcePathType pathType) throws IOException {
        try {
            switch (pathType) {
                case CLASSPATH:
                    ClassPathResource classPathResource = new ClassPathResource(templatePath);
                    if (!classPathResource.exists()) {
                        throw new IOException("Classpath resource not found: " + templatePath);
                    }
                    return classPathResource;

                case FILESYSTEM:
                    FileSystemResource fileSystemResource = new FileSystemResource(templatePath);
                    if (!fileSystemResource.exists()) {
                        throw new IOException("Filesystem resource not found: " + templatePath);
                    }
                    return fileSystemResource;

                case URL:
                    try {
                        UrlResource urlResource = new UrlResource(templatePath);
                        // 测试URL连接是否可用
                        URL url = urlResource.getURL();
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setConnectTimeout(3000);
                        conn.setReadTimeout(5000);
                        conn.setRequestMethod("HEAD");
                        if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                            throw new IOException("URL resource unavailable: " + templatePath);
                        }
                        return urlResource;
                    } catch (Exception e) {
                        throw new IOException("Failed to access URL resource: " + templatePath, e);
                    }

                default:
                    throw new IllegalArgumentException("Unsupported resource type: " + pathType);
            }
        } catch (Exception e) {
            throw new IOException(String.format("Resource loading failed [type:%s, path:%s]",
                pathType, templatePath), e);
        }
    }

    /**
     * 统一处理资源输入流
     */
    private static InputStream getInputStream(Resource resource) throws IOException {
        if (resource instanceof UrlResource) {
            URL url = ((UrlResource) resource).getURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(10000);
            return conn.getInputStream();
        }
        return resource.getInputStream();
    }



    /**
     * 获取 PDF 模板中的所有表单字段名称
     *
     * @param templatePath 模板路径
     * @param pathType     路径类型（classpath / 文件系统等）
     * @return 表单字段名列表
     * @throws IOException 读取模板失败或无 AcroForm 表单
     */
    public static List<String> extractPdfFieldNames(String templatePath, ResourcePathType pathType) throws IOException {
        // 1. 加载资源
        Resource resource = getResourceByType(templatePath, pathType);

        // 2. 加载 PDF 文档并提取字段名
        try (InputStream inputStream = getInputStream(resource);
//             PDDocument document = Loader.loadPDF(inputStream.readAllBytes())) {
             PDDocument document = PDDocument.load(inputStream.readAllBytes())) {

            PDAcroForm acroForm = document.getDocumentCatalog().getAcroForm();

            if (acroForm == null) {
                throw new IOException("PDF 模板中未找到表单字段（AcroForm）");
            }

            List<String> fieldNames = new ArrayList<>();
            for (PDField field : acroForm.getFields()) {
                fieldNames.add(field.getFullyQualifiedName());
            }

            return fieldNames;
        }
    }






}
