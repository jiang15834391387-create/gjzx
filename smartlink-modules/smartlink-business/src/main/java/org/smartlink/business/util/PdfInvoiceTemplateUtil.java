package org.smartlink.business.util;

import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDTextField;
import org.smartlink.business.doman.vo.LhdxInvoiceVo;
import org.smartlink.common.entity.domain.business.domain.DataOcrDetails;
import org.smartlink.common.entity.domain.business.domain.DataOcrInfo;
import org.springframework.core.io.ClassPathResource;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class PdfInvoiceTemplateUtil {

    /**
     * 使用模板生成填充后的 PDF 文件
     *
     * @param outputPath   填充后的 PDF 输出路径
     * @param replacements 占位符及其对应值的映射
     * @throws IOException 如果读取或写入文件失败c
     */
    public static void generatePdfFromTemplate(String outputPath, Map<String, Object> replacements,String templateName) {

        try{
            InputStream inputStream = new ClassPathResource("templates/" + templateName).getInputStream();
            PDDocument document = Loader.loadPDF(inputStream.readAllBytes());

            PDDocumentCatalog documentCatalog = document.getDocumentCatalog();
            PDAcroForm acroForm = documentCatalog.getAcroForm();
            if (acroForm != null) {
                PDResources res = acroForm.getDefaultResources();
                InputStream fontIS = new ClassPathResource("fonts/SimSun.ttf").getInputStream();
                PDFont font = PDType0Font.load(document, fontIS, false);
                String fontName = res.add(font).getName();
                String chineseDefaultAppearanceString = "/" + fontName + " 9 Tf 0 g";
                acroForm.setDefaultAppearance(chineseDefaultAppearanceString);

                for (String key : replacements.keySet()) {
                    PDTextField field = (PDTextField) acroForm.getField(key);
                    if (field != null) {
                        field.setDefaultAppearance(chineseDefaultAppearanceString);
                        Object value = replacements.get(key);
                        if (ObjectUtil.isNotNull(value)) {
                            field.setValue(value.toString());
                        }
                    }
                }
            }
            // 保存文档
            document.save(outputPath);
            // 关闭文档
            document.close();
        }catch (Exception e){
            log.error("生成pdf异常",e);
        }

    }

    private static Document parseXmlFile(String filePath) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(new File(filePath));
    }

    private static String getElementText(Document xmlDoc, String tagName) {
        try {
            NodeList nodeList = xmlDoc.getElementsByTagName(tagName);
            if (nodeList.getLength() > 0) {
                if (tagName.equals("GeneralOrSpecialVAT")){
                    NodeList childNodes = nodeList.item(0).getChildNodes();
                    return childNodes.item(1).getTextContent();
                }else{
                    return nodeList.item(0).getTextContent();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void main(String[] args) throws Exception {
        System.out.println("开始生成");

        Document xmlDoc = parseXmlFile("F:\\发票\\增值税专用发票.xml");
        String labelName = getElementText(xmlDoc, "GeneralOrSpecialVAT");
        String invoiceNumber = getElementText(xmlDoc, "InvoiceNumber");//发票号码
        String issueTime = getElementText(xmlDoc, "IssueTime");//开票日期

        String buyerName = getElementText(xmlDoc, "BuyerName");//购方名称
        String buyerIdNum = getElementText(xmlDoc, "BuyerIdNum");//购方纳税人识别号

        String sellerName = getElementText(xmlDoc, "SellerName");//销方名称
        String sellerIdNum = getElementText(xmlDoc, "SellerIdNum");//销方纳税人识别号

        // 商品数据
        String itemName = getElementText(xmlDoc, "ItemName");//商品名称
        String specMod = getElementText(xmlDoc, "SpecMod");
        String meaUnits =getElementText(xmlDoc, "MeaUnits");
        String quantity =getElementText(xmlDoc, "Quantity");
        String unPrice = getElementText(xmlDoc, "UnPrice");

        String amount = getElementText(xmlDoc, "Amount");// 金额
        String taxRate = getElementText(xmlDoc, "TaxRate");//税率
        String taxAmount = getElementText(xmlDoc, "ComTaxAm");//税额

        String totalAmount = getElementText(xmlDoc, "TotalTax-includedAmount");//总价
        String amountInChinese = getElementText(xmlDoc, "TotalTax-includedAmountInChinese");//金额大写

        String remark = getElementText(xmlDoc, "Remark");//备注

        String drawer = getElementText(xmlDoc, "Drawer");//开票人

        Map<String, Object> replacements = new HashMap<>();
        replacements.put("InvoiceNumber", invoiceNumber);
        replacements.put("IssueTime", issueTime);
        replacements.put("BuyerName", buyerName);
        replacements.put("BuyerIdNum", buyerIdNum);
        replacements.put("SellerName", sellerName);
        replacements.put("SellerIdNum", sellerIdNum);
        replacements.put("ItemName", itemName);
        replacements.put("SpecMod", specMod);
        replacements.put("MeaUnits", meaUnits);
        replacements.put("Quantity", quantity);
        replacements.put("UnPrice", unPrice);
        replacements.put("Amount", amount);
        if (taxRate.equals("*")) {
            replacements.put("TaxRate", taxRate);
        }else{
            replacements.put("TaxRate", Double.parseDouble(taxRate) * 100 + "%" );
        }
        replacements.put("ComTaxAm", taxAmount);
        replacements.put("totalAmount", totalAmount);
        replacements.put("amountInChinese", amountInChinese);
        replacements.put("Remark", remark);
        replacements.put("Drawer", drawer);

        String templateName = "Invoice_template.pdf";
        if ("01".equals(labelName)) templateName = "zzs_Invoice_template.pdf";
        generatePdfFromTemplate("F:\\发票\\ppp.pdf", replacements,templateName);
    }

}
