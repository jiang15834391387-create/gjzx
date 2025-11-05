package org.smartlink.business.util;

import com.alibaba.excel.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.dromara.pdf.pdfbox.core.base.Document;
import org.dromara.pdf.pdfbox.core.base.Page;
import org.dromara.pdf.pdfbox.core.component.*;
import org.dromara.pdf.pdfbox.core.enums.HorizontalAlignment;
import org.dromara.pdf.pdfbox.core.enums.LineStyle;
import org.dromara.pdf.pdfbox.core.enums.VerticalAlignment;
import org.dromara.pdf.pdfbox.handler.PdfHandler;
import org.smartlink.business.doman.entity.BuyerInfo;
import org.smartlink.business.doman.entity.Invoice;
import org.smartlink.business.doman.entity.InvoiceItem;
import org.smartlink.business.doman.entity.SellerInfo;
import org.smartlink.business.doman.vo.LhdxInvoiceVo;

import java.awt.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Slf4j
public class InvoiceGeneratorXEasyPdf {

    public static void main(String[] args) {

    }
    public static void generateInvoice(Invoice invoice, String outputPath,int pageNum,String invoiceType) {
        Document document = PdfHandler.getDocumentHandler().create();// 创建PDF文档
        for (int i = 0; i < pageNum; i++) {
            if (i == pageNum - 1)
                generatePdfEnd(document, invoice, pageNum,i,invoiceType);
            else
                generatePdfPage(document, invoice, pageNum, i + 1,i,invoiceType);
        }
        document.save(outputPath);
        document.close();// 关闭文档
    }

    public static void generatePdfEnd(Document document,Invoice invoice,int pageNum,int index,String invoiceType){
        Page page = new Page(document);// 创建页面
        createHeader(document, invoice,pageNum,pageNum,invoiceType);// 创建头部

        setTable3(document, invoice.getItems(),index);
        int itemNum = 24 - invoice.getItems().size() % 24;
        float beginY = itemNum * 25F + 60F;
        setTableEmpty(document,beginY + 28F);
        //setTable4(document,invoice,beginY);
        setTable5(document,invoice,beginY + 8F);
        setTable6(document,invoice,beginY - 20F);
        setTextarea( document,"开票人：","楷体", 9F,new Color(128, 0, 0),55F, beginY - 112F,null);
        setTextarea( document,invoice.getDrawer(),"宋体", 9F,Color.BLACK,95F, beginY - 112F,null);

        document.appendPage(page);// 添加页面
    }
    public static void generatePdfPage(Document document,Invoice invoice, int pageNum,int pageSize,int index,String invoiceType) {
        Page page = new Page(document);// 创建页面
        createHeader(document, invoice,pageNum,pageSize,invoiceType);// 创建头部

        setTable3(document, invoice.getItems(),index);
        //setTable4(document,invoice,90F);
        setTextarea( document,"开票人：","楷体", 9F,new Color(128, 0, 0),55F, 45F,null);
        setTextarea( document,invoice.getDrawer(),"宋体", 9F,Color.BLACK,95F, 45F,null);

        document.appendPage(page);// 添加页面
    }

    private static void createHeader(Document document,Invoice invoice,int pageNum,int pageSize,String invoiceType) {
        String invoiceTypeName = invoiceType.equals("10100") ? "电子发票（增值税专用发票）" : "电子发票（增值税普通发票）";
        setTextarea(document, invoiceTypeName, "楷体", 20F,new Color(128, 0, 0),0F, 800F,HorizontalAlignment.CENTER);
        setTextarea(document, "发票号码：", "楷体", 9F,new Color(128, 0, 0),450F, 800F,null);
        setTextarea(document, " " + invoice.getInvoiceCode()+invoice.getInvoiceNumber(), "楷体", 9F,Color.BLACK,490F, 800F,null);

        setTextarea(document, "开票日期：", "楷体", 9F,new Color(128, 0, 0),450F, 783F,null);
        Date issueDate = invoice.getIssueDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String format = dateFormat.format(issueDate);
        setTextarea(document, " " + format, "楷体", 9F,Color.BLACK,490F, 783F,null);

        setLine(document, 792F, new Color(128, 0, 0));
        setLine(document, 789F, new Color(128, 0, 0));

        setTextarea(document, "共"+ pageNum +"页 第"+ pageSize +"页", "宋体", 9F,Color.BLACK,530F, 760F,null);

        setTable(document);
        setTable2(document);

        setTextarea(document,invoice.getBuyer().getName(),"宋体", 9F,Color.BLACK,60F, 740F,null);
        setTextarea(document,invoice.getBuyer().getTaxId(),"Courier New", 11F,Color.BLACK,170F, 713F,null);

        setTextarea(document,invoice.getSeller().getName(),"宋体", 9F,Color.BLACK,346F, 740F,null);
        setTextarea(document,invoice.getSeller().getTaxId(),"Courier New", 11F,Color.BLACK,460F, 713F,null);
    }

    private static void setTable6(Document document,Invoice invoice,float beginY) {
        Table table = new Table(document.getCurrentPage());
        table.setBeginX(12F);
        table.setBeginY(beginY);
        table.setBorderColor(new Color(128, 0, 0));
        table.setCellWidths(18F,554F);
        table.setIsBorder(true);

        TableRow tableRow = new TableRow(table);
        tableRow.setHeight(60F);

        TableCell tableCell = new TableCell(tableRow);
        Textarea textarea = new Textarea(table.getPage());
        textarea.setFontName("楷体");
        textarea.setFontSize(9F);
        textarea.setFontColor(new Color(128, 0, 0));
        textarea.setTextList(Arrays.asList("备"," ","注"));
        tableCell.addComponents(textarea);
        tableCell.setContentVerticalAlignment(VerticalAlignment.CENTER);
        tableCell.setContentHorizontalAlignment(HorizontalAlignment.CENTER);
        tableRow.addCells(tableCell);

        TableCell tableCell1 = new TableCell(tableRow);
        Textarea textarea1 = new Textarea(table.getPage());
        textarea1.setText(" " + invoice.getRemark());
        textarea1.setFontName("宋体");
        textarea1.setFontSize(9F);
        tableCell1.addComponents(textarea1);
        tableRow.addCells(tableCell1);

        table.addRows(tableRow);
        table.render();// 绘制
    }
    private static void setTable5(Document document,Invoice invoice,float beginY) {
        Table table = new Table(document.getCurrentPage());
        table.setBeginX(12F);
        table.setBeginY(beginY);
        table.setBorderColor(new Color(128, 0, 0));
        table.setCellWidths(145F,250F,40F,137F);
        table.setIsBorder(true);

        TableRow tableRow = new TableRow(table);
        tableRow.setHeight(28F);

        TableCell tableCell = new TableCell(tableRow);
        Textarea textarea = new Textarea(table.getPage());
        textarea.setText("价税合计（大写）");
        textarea.setFontName("楷体");
        textarea.setFontSize(9F);
        textarea.setFontColor(new Color(128, 0, 0));
        tableCell.addComponents(textarea);
        tableCell.setContentVerticalAlignment(VerticalAlignment.CENTER);
        tableCell.setContentHorizontalAlignment(HorizontalAlignment.CENTER);
        tableRow.addCells(tableCell);

        TableCell tableCell1 = new TableCell(tableRow);
        Textarea textarea1 = new Textarea(table.getPage());
        textarea1.setText("     " + invoice.getTotalAmountWithoutTaxBig());
        textarea1.setFontName("宋体");
        textarea1.setFontSize(9F);
        tableCell1.addComponents(textarea1);
        tableCell1.setContentVerticalAlignment(VerticalAlignment.CENTER);
        tableCell1.setContentHorizontalAlignment(HorizontalAlignment.LEFT);
        tableCell1.setIsBorderRight(false);
        tableRow.addCells(tableCell1);

        TableCell tableCell2 = new TableCell(tableRow);
        Textarea textarea2 = new Textarea(table.getPage());
        textarea2.setText("（小写）");
        textarea2.setFontName("楷体");
        textarea2.setFontSize(9F);
        textarea2.setFontColor(new Color(128, 0, 0));
        tableCell2.addComponents(textarea2);
        tableCell2.setContentVerticalAlignment(VerticalAlignment.CENTER);
        tableCell2.setContentHorizontalAlignment(HorizontalAlignment.CENTER);
        tableCell2.setIsBorderLeft(false);
        tableCell2.setIsBorderRight(false);
        tableRow.addCells(tableCell2);

        TableCell tableCell3 = new TableCell(tableRow);
        Textarea textarea3 = new Textarea(table.getPage());
        textarea3.setText(" " + invoice.getTotalAmountWithTax());
        textarea3.setFontName("宋体");
        textarea3.setFontSize(9F);
        tableCell3.addComponents(textarea3);
        tableCell3.setContentVerticalAlignment(VerticalAlignment.CENTER);
        tableCell3.setContentHorizontalAlignment(HorizontalAlignment.LEFT);
        tableCell3.setIsBorderLeft(false);
        tableRow.addCells(tableCell3);

        table.addRows(tableRow);
        table.render();// 绘制
    }

    private static void setTableEmpty(Document document,float beginY){
        Table table = new Table(document.getCurrentPage());
        table.setBeginX(12F);
        table.setBeginY(beginY);
        table.setBorderColor(new Color(128, 0, 0));
        table.setCellWidths(572F);
        table.setIsBorder(false);
        table.setIsBorderLeft(true);
        table.setIsBorderRight(true);

        TableRow tableRow = new TableRow(table);
        tableRow.setHeight(28F);
        TableCell tableCell = new TableCell(tableRow);
        Textarea textarea = new Textarea(table.getPage());
        textarea.setText(" ");
        tableRow.addCells(tableCell);
        table.addRows(tableRow);
        table.render();// 绘制
    }

    private static void setTable4(Document document,Invoice invoice,float beginY){
        Table table = new Table(document.getCurrentPage());
        table.setBeginX(12F);
        table.setBeginY(beginY);
        table.setBorderColor(new Color(128, 0, 0));
        table.setCellWidths(105F, 320F, 147F);
        table.setContentMarginTop(5F);
        table.setIsBorder(false);

        TableRow tableRow = new TableRow(table);
        tableRow.setHeight(20F);

        TableCell tableCell = new TableCell(tableRow);
        Textarea textarea = new Textarea(table.getPage());
        textarea.setFontName("楷体");
        textarea.setFontSize(9F);
        textarea.setText("小          计");
        textarea.setFontColor(new Color(128, 0, 0));
        tableCell.addComponents(textarea);
        tableCell.setIsBorder(false);
        tableCell.setIsBorderLeft(true);
        tableCell.setContentHorizontalAlignment(HorizontalAlignment.RIGHT);
        tableRow.addCells(tableCell);

        TableCell tableCell1 = new TableCell(tableRow);
        Textarea textarea1 = new Textarea(table.getPage());
        textarea1.setFontName("宋体");
        textarea1.setFontSize(9F);
        textarea1.setText("¥" + invoice.getTotalAmount());
        tableCell1.addComponents(textarea1);
        tableCell1.setIsBorder(false);
        tableCell1.setContentHorizontalAlignment(HorizontalAlignment.RIGHT);
        tableRow.addCells(tableCell1);

        TableCell tableCell2 = new TableCell(tableRow);
        Textarea textarea2 = new Textarea(table.getPage());
        textarea2.setFontName("宋体");
        textarea2.setFontSize(9F);
        textarea2.setText("¥" +  invoice.getTotalTax());
        tableCell2.addComponents(textarea2);
        tableCell2.setIsBorder(false);
        tableCell2.setIsBorderRight(true);
        tableCell2.setContentHorizontalAlignment(HorizontalAlignment.RIGHT);
        tableRow.addCells(tableCell2);
        table.addRows(tableRow);

        TableRow tableRow2 = new TableRow(table);
        tableRow2.setHeight(20F);

        TableCell tableCell4 = new TableCell(tableRow2);
        Textarea textarea4 = new Textarea(table.getPage());
        textarea4.setFontName("楷体");
        textarea4.setFontSize(9F);
        textarea4.setText("合          计");
        textarea4.setFontColor(new Color(128, 0, 0));
        tableCell4.addComponents(textarea4);
        tableCell4.setIsBorder(false);
        tableCell4.setIsBorderLeft(true);
        tableCell4.setIsBorderBottom(true);
        tableCell4.setContentHorizontalAlignment(HorizontalAlignment.RIGHT);
        tableRow2.addCells(tableCell4);

        TableCell tableCell5 = new TableCell(tableRow);
        Textarea textarea5 = new Textarea(table.getPage());
        textarea5.setFontName("宋体");
        textarea5.setFontSize(9F);
        textarea5.setText("¥" + invoice.getTotalAmount());
        tableCell5.addComponents(textarea5);
        tableCell5.setIsBorder(false);
        tableCell5.setIsBorderBottom(true);
        tableCell5.setContentHorizontalAlignment(HorizontalAlignment.RIGHT);
        tableRow2.addCells(tableCell5);

        TableCell tableCell6 = new TableCell(tableRow);
        Textarea textarea6 = new Textarea(table.getPage());
        textarea6.setFontName("宋体");
        textarea6.setFontSize(9F);
        textarea6.setText("¥" + invoice.getTotalTax());
        tableCell6.addComponents(textarea6);
        tableCell6.setIsBorder(false);
        tableCell6.setIsBorderRight(true);
        tableCell6.setIsBorderBottom(true);
        tableCell6.setContentHorizontalAlignment(HorizontalAlignment.RIGHT);
        tableRow2.addCells(tableCell6);
        table.addRows(tableRow2);

        table.render();// 绘制
    }

    private static void setTable3(Document document, List<InvoiceItem> items, int index){
        Table table = new Table(document.getCurrentPage());
        table.setBeginX(12F);
        table.setBeginY(688F);
        table.setBorderColor(new Color(128, 0, 0));
        table.setCellWidths(105F, 65F, 50F, 65F, 65F, 75F, 70F, 77F);
        table.setContentMarginTop(2F);

        int pageNum = items.size() / 24 + 1;
        int forNum = 24;
        if (pageNum == index + 1) forNum = items.size() % 24;

        for (int i = 0; i < forNum; i++) {
            InvoiceItem invoiceItem = items.get(24 * index + i);
            TableRow tableRow = new TableRow(table);
            tableRow.setHeight(25F);
            for (int i1 = 0; i1 < 8; i1++) {
                TableCell tableCell = new TableCell(tableRow);
                Textarea textarea = new Textarea(table.getPage());
                textarea.setFontName("宋体");
                textarea.setFontSize(9F);
                textarea.setText(selectContext(i1, invoiceItem));
                tableCell.addComponents(textarea);
                tableCell.setIsBorder(false);
                if (i1 == 0)tableCell.setIsBorderLeft(true);
                if (i1 == 7)tableCell.setIsBorderRight(true);
                if (i == 23) tableCell.setIsBorderBottom(true);
                if (i1 == 0|| i1 == 1) {
                    tableCell.setContentHorizontalAlignment(HorizontalAlignment.LEFT);
                }else if (i1 == 2 || i1 == 6) {
                    tableCell.setContentHorizontalAlignment(HorizontalAlignment.CENTER);
                }else {
                    tableCell.setContentHorizontalAlignment(HorizontalAlignment.RIGHT);
                }
                tableRow.addCells(tableCell);
            }
            table.addRows(tableRow);
        }
        table.setIsBorder(true);
        table.setIsBorderTop(false);
        table.render();// 绘制
    }
    public static String selectContext(int index,InvoiceItem invoiceItem){
        return switch (index) {
            case 0 -> invoiceItem.getName();
            case 1 -> invoiceItem.getSpecification();
            case 4 -> invoiceItem.getUnitPrice() + "";
            case 5 -> invoiceItem.getAmount() + "";
            case 6 -> invoiceItem.getTaxRate();
            case 7 -> invoiceItem.getTaxAmount() + "";
            default -> "";
        };
    }

    private static void setTable2(Document document){
        Table table = new Table(document.getCurrentPage());
        table.setBeginX(12F);
        table.setBeginY(698F);
        table.setBorderColor(new Color(128, 0, 0));
        table.setCellWidths(105F, 65F, 50F, 65F, 65F, 75F, 70F, 77F);
        table.setIsBorder(false);
        table.setContentHorizontalAlignment(HorizontalAlignment.CENTER);
        table.setContentVerticalAlignment(VerticalAlignment.CENTER);
        table.setContentMarginTop(2F);

        TableHeader tableHeader = new TableHeader(table);
        TableRow tableRow = new TableRow(table);
        tableRow.setHeight(10F);

        for (int i = 0; i < 8; i++) {
            TableCell tableCell = new TableCell(tableRow);
            Textarea textarea = new Textarea(table.getPage());
            textarea.setFontName("楷体");
            textarea.setFontSize(9F);
            textarea.setFontColor(new Color(128, 0, 0));
            textarea.setText(selectTitle(i));
            tableCell.addComponents(textarea);
            tableCell.setIsBorder(false);
            if (i == 0)tableCell.setIsBorderLeft(true);
            if (i == 7)tableCell.setIsBorderRight(true);
            if (i == 1) {
                tableCell.setContentHorizontalAlignment(HorizontalAlignment.LEFT);
            }else if (i == 0 || i == 2 || i == 6) {
                tableCell.setContentHorizontalAlignment(HorizontalAlignment.CENTER);
            }else {
                tableCell.setContentHorizontalAlignment(HorizontalAlignment.RIGHT);
            }

            tableRow.addCells(tableCell);
        }
        tableHeader.addRows(tableRow);
        table.setHeader(tableHeader);
        table.render();// 绘制
    }

    public static String selectTitle(int index){
        return switch (index) {
            case 0 -> "项目名称";
            case 1 -> " 规格型号";
            case 2 -> "单  位";
            case 3 -> "数  量 ";
            case 4 -> "单  价 ";
            case 5 -> "金  额 ";
            case 6 -> "税率/征收率";
            case 7 -> "税  额 ";
            default -> "";
        };
    }
    // 创建表格
    private static void setTable(Document document) {
        Table table = new Table(document.getCurrentPage());
        table.setBeginX(12F);
        table.setBeginY(758F);
        table.setBorderColor(new Color(128, 0, 0));
        table.setCellWidths(18F, 268F, 18F,268F);// 设置列宽（4列）
        table.setIsBorder(true);// 设置显示边框

        TableRow tableRow = new TableRow(table);// 创建行
        tableRow.setHeight(60F);// 设置行高

        TableCell tableCell = new TableCell(tableRow);
        Textarea textarea = new Textarea(table.getPage());
        textarea.setFontName("楷体");
        textarea.setFontSize(9F);
        textarea.setFontColor(new Color(128, 0, 0));
        textarea.setTextList(Arrays.asList(" "," 购"," 买"," 方"," 信"," 息"));
        tableCell.addComponents(textarea);
        tableRow.addCells(tableCell);

        TableCell tableCell1 = new TableCell(tableRow);
        Textarea textarea1 = new Textarea(table.getPage());
        textarea1.setTextList(Arrays.asList(" "," 名称："," "," "," 统一社会信用代码/纳税人识别号："));
        textarea1.setFontName("楷体");
        textarea1.setFontSize(9F);
        textarea1.setFontColor(new Color(128, 0, 0));
        tableCell1.addComponents(textarea1);
        tableRow.addCells(tableCell1);

        TableCell tableCell2 = new TableCell(tableRow);
        Textarea textarea2 = new Textarea(table.getPage());
        textarea2.setFontName("楷体");
        textarea2.setFontSize(9F);
        textarea2.setFontColor(new Color(128, 0, 0));
        textarea2.setTextList(Arrays.asList(" "," 销"," 售"," 方"," 信"," 息"));
        tableCell2.addComponents(textarea2);
        tableRow.addCells(tableCell2);

        TableCell tableCell3 = new TableCell(tableRow);
        Textarea textarea3 = new Textarea(table.getPage());
        textarea3.setTextList(Arrays.asList(" "," 名称："," "," "," 统一社会信用代码/纳税人识别号："));
        textarea3.setFontName("楷体");
        textarea3.setFontSize(9F);
        textarea3.setFontColor(new Color(128, 0, 0));
        tableCell3.addComponents(textarea3);
        tableRow.addCells(tableCell3);

        table.addRows(tableRow);
        table.render();// 绘制
    }

    // 构建文本组件（单行，自动换行）
    private static void setTextarea(Document document,String text,String fontName,float fontSize,Color color,float beginX, float beginY,HorizontalAlignment horizontalAlignment) {
        Textarea textarea = new Textarea(document.getCurrentPage());
        textarea.setText(text);// 设置文本
        textarea.setFontName(fontName); // 设置字体
        textarea.setFontSize(fontSize); // 设置字体大小
        textarea.setFontColor(color);
        if (beginX != 0) textarea.setBeginX(beginX);
        if (horizontalAlignment != null)textarea.setHorizontalAlignment(horizontalAlignment);
        textarea.setBeginY(beginY);
        textarea.render(); // 绘制
    }    // 构建文本组件（单行，自动换行）

    // 创建线条方法
    private static void setLine(Document document, float beginY, Color lineColor) {
        Line line = new Line(document.getCurrentPage());
        line.setLineStyle(LineStyle.SOLID);// 设置线条样式
        line.setBeginX((float) 187.0);
        line.setBeginY(beginY);
        line.setLineLength((float) 200.0);
        line.setLineColor(lineColor);
        line.setLineWidth((float) 0.8);
        line.render();// 绘制
    }

    // 创建示例发票数据
    public static Invoice createSampleInvoice(List<LhdxInvoiceVo> lhdxInvoiceVoList) {
        LhdxInvoiceVo invoiceVo = lhdxInvoiceVoList.get(0);
        // 创建购买方信息
        BuyerInfo buyer = new BuyerInfo(invoiceVo.getBuyerName(), invoiceVo.getBuyerTaxId());

        // 创建销售方信息
        SellerInfo seller = new SellerInfo(invoiceVo.getSellerName(), invoiceVo.getSellerTaxId());

        // 创建商品明细
        List<InvoiceItem> items = new ArrayList<>();

        for (LhdxInvoiceVo lhdxInvoiceVo : lhdxInvoiceVoList) {
            BigDecimal unitPriceDecimal = null; BigDecimal taxAmountDecimal = null;
            if (StringUtils.isNotBlank(lhdxInvoiceVo.getUnitPrice())) unitPriceDecimal = new BigDecimal(lhdxInvoiceVo.getUnitPrice().replace(",", ""));
            if (StringUtils.isNotBlank(lhdxInvoiceVo.getTaxAmount())) taxAmountDecimal = new BigDecimal(lhdxInvoiceVo.getTaxAmount().replace(",", ""));
            items.add( new InvoiceItem(Integer.parseInt(lhdxInvoiceVo.getInvoiceDetailLine()), lhdxInvoiceVo.getInvoiceItemName(), unitPriceDecimal, unitPriceDecimal, lhdxInvoiceVo.getTaxRate(), taxAmountDecimal));
        }

        String totalAmount = invoiceVo.getTotalAmount().replace(",", "");

        LocalDate invoiceDate = null;
        Date parse = null;
        try {
            // 尝试多种日期格式解析
            DateTimeFormatter[] formatters = {
                DateTimeFormatter.ISO_LOCAL_DATE,              // yyyy-MM-dd
                DateTimeFormatter.ofPattern("yyyy/MM/dd"),     // yyyy/MM/dd
                DateTimeFormatter.ofPattern("yyyy/M/d"),       // yyyy/M/d
                DateTimeFormatter.ofPattern("yyyy/MM/d"),      // yyyy/MM/d
                DateTimeFormatter.ofPattern("yyyy/M/dd"),      // yyyy/M/dd
                DateTimeFormatter.ofPattern("yyyy年MM月dd日"),    // yyyy年MM月dd日
                DateTimeFormatter.ofPattern("yyyy年M月d日"),      // yyyy年M月d日
                DateTimeFormatter.ofPattern("yyyy年MM月d日"),     // yyyy年MM月d日
                DateTimeFormatter.ofPattern("yyyy年M月dd日")      // yyyy年M月dd日
            };

            for (DateTimeFormatter formatter : formatters) {
                try {
                    invoiceDate = LocalDate.parse(invoiceVo.getInvoiceDate(), formatter);
                    parse = Date.from(invoiceDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                    break;
                } catch (DateTimeParseException ignored) {
                    // 继续尝试下一个格式
                }
            }

            if (parse == null) {
                throw new DateTimeParseException("Unable to parse date", invoiceVo.getInvoiceDate(), 0);
            }
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid invoice date format: " + invoiceVo.getInvoiceDate(), e);
        }
        // 创建发票
        System.out.println(invoiceVo.getInvoiceCode() + " " + invoiceVo.getInvoiceDate());
        return new Invoice(invoiceVo.getInvoiceCode(), invoiceVo.getInvoiceNumber(), parse,
            buyer, seller, items, new BigDecimal(totalAmount), MoneyToChineseUtil.convert(new BigDecimal(totalAmount)),
            invoiceVo.getRemarks(), invoiceVo.getDrawer(), invoiceVo.getPayee());
    }

}
