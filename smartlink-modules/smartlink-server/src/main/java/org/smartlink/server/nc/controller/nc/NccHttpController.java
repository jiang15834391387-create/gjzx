package org.smartlink.server.nc.controller.nc;

import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartlink.common.core.domain.R;
import org.smartlink.common.json.utils.JsonUtils;
import org.smartlink.server.nc.domain.NcResult;
import org.smartlink.server.nc.enums.NcCodeEnum;
import org.smartlink.server.nc.service.nc.NcService;
import org.smartlink.server.task.momain.DataTask;
import org.springframework.web.bind.annotation.*;

/**
 * @description: 对NCC系统提供接口
 * @author: L
 * @create: 2024-11-20
 **/
@RestController
@RequestMapping("/prod-api/Shy")
@AllArgsConstructor
public class NccHttpController {

    private static final Logger log = LoggerFactory.getLogger(NccHttpController.class);
    private final NcService ncService;

    /**
     * 返回体Code编码
     */
    private static final String RSP_CODE = "RspCode";

    /**
     * 返回体msg消息
     */
    private static final String RSP_MSG = "RspMsg";
    /**
     * NCC测试调用测试系统联通性
     * @return 结果
     */
    @ApiOperation("NCC测试调用测试系统联通性")
    @PostMapping("/testNcc")
    public String testNcc() throws DocumentException {
        String result = ncService.testNcc();
        NcResult ncResult = new NcResult(getXmlNodeData(result,RSP_CODE),getXmlNodeData(result,RSP_MSG),getXmlNodeData(result,"data"));
        return JsonUtils.toJsonString(ncResult);
    }

    /**
     * 获取xml指定节点的文本
     * @param xml 返回xml报文
     * @param node 节点名称
     * @return 文本值
     * @throws
     */
    private String getXmlNodeData(String xml,String node) throws DocumentException {
        Document document = DocumentHelper.parseText(xml);
        Element rootElement = document.getRootElement();
        return rootElement.elementText(node);
    }

    /**
     * NCC系统进入首页调用 获取该用户代办任务数量
     * @param xml 入参
     * @return 结果
     */
    @ApiOperation("获取代办任务数量")
    @PostMapping("/getCurrentTaskCount")
    public String getCurrentTaskCount(String xml) throws DocumentException {
        String result = ncService.getCurrentTaskCount(xml);
        NcResult ncResult = new NcResult(getXmlNodeData(result,RSP_CODE),getXmlNodeData(result,RSP_MSG),result);
        return JsonUtils.toJsonString(ncResult);
    }


    /**
     * NCC制单完成后调用 添加影像任务
     * @param xml 入参
     * @return 结果
     */
    @ApiOperation("添加影像任务")
    @PostMapping("/addScanTask")
    public String addScanTask(String xml) throws DocumentException {
        String result = ncService.addScanTask(xml);
        NcResult ncResult = new NcResult(getXmlNodeData(result,RSP_CODE),getXmlNodeData(result,RSP_MSG),result);
        return JsonUtils.toJsonString(ncResult);
    }

    /**
     * NCC删除单据调用 删除影像任务
     * @param xml 入参
     * @return 结果
     */
    @ApiOperation("删除影像任务")
    @PostMapping("/deleteScanTask")
    public String deleteScanTask(String xml) throws DocumentException {
        String result = ncService.deleteScanTask(xml);
        NcResult ncResult = new NcResult(getXmlNodeData(result,RSP_CODE),getXmlNodeData(result,RSP_MSG),result);
        return JsonUtils.toJsonString(ncResult);
    }

    /**
     * 单点登录-影响扫描
     * @param xml 入参
     * @return 结果
     */
    @ApiOperation("单点登陆")
    @PostMapping("/singleLogin")
    public String singleLogin(String xml) throws DocumentException {
        String result = ncService.singleLogin(xml);
        NcResult ncResult = new NcResult(getXmlNodeData(result,RSP_CODE),getXmlNodeData(result,RSP_MSG),result);
        return JsonUtils.toJsonString(ncResult);
    }

    /**
     * NCC调用 获取影像查看页面
     * @param xml 入参
     * @return 结果
     */
    @ApiOperation("获取影像查看链接")
    @PostMapping("/getImageShowUrl")
    public String getImageShowUrl(String xml) throws DocumentException {
        String result = ncService.getImageShowUrl(xml);
        NcResult ncResult = new NcResult(getXmlNodeData(result,RSP_CODE),getXmlNodeData(result,RSP_MSG),result);
        return JsonUtils.toJsonString(ncResult);
    }

    /**
     * NCC调用 更新单据号
     * @param xml 入参
     * @return 结果
     */
    @ApiOperation("更新单据号")
    @PostMapping("/updateBillNo")
    public String updateBillNo(String xml){
        String result = ncService.updateBillNo(xml);
        NcResult ncResult = new NcResult(NcCodeEnum.NC_SUCCESS_STATE.getCode(),NcCodeEnum.NC_SUCCESS_STATE.getCodeName(),result);
        return JsonUtils.toJsonString(ncResult);
    }
    /**
     * 电子档案下载影像
     * @param xml 入参数
     * @return 结果
     */
    @ApiOperation("电子档案下载影像")
    @PostMapping("/downloadImage")
    public String downloadImages(String xml) {
        String result = ncService.downloadImages(xml);
        NcResult ncResult = new NcResult(NcCodeEnum.NC_SUCCESS_STATE.getCode(),NcCodeEnum.NC_SUCCESS_STATE.getCodeName(),result);
        return JsonUtils.toJsonString(ncResult);
    }

    /**
     * NCC驳回单据调用 驳回影像状态
     * @param xml 入参
     * @return 结果
     */
    @ApiOperation("驳回单据时调用驳回影像状态")
    @PostMapping("/rejectImageOnBillReject")
    public String rejectImageOnBillReject(String xml) throws DocumentException {
        String result = ncService.rejectImageOnBillReject(xml);
        NcResult ncResult = new NcResult(getXmlNodeData(result,RSP_CODE),getXmlNodeData(result,RSP_MSG),result);
        return JsonUtils.toJsonString(ncResult);
    }

    /**
     * 收票节点获取影像扫描链接 只能传普通文件上传
     * @param xml 入参
     * @return 结果
     */
    @ApiOperation("收票节点文件上传")
    @PostMapping("/filescan")
    public String fileScan(String xml) throws DocumentException {
        String result = ncService.fileScan(xml);
        NcResult ncResult = new NcResult(getXmlNodeData(result,RSP_CODE),getXmlNodeData(result,RSP_MSG),result);
        return JsonUtils.toJsonString(ncResult);
    }

    /**
     * 凭证节点获取影像查看链接
     * @param xml 入参
     * @return 结果
     */
    @ApiOperation("凭证节点获取影像查看链接")
    @PostMapping("/getCombineImageShowUrl")
    public String getCombineImageShowUrl(String xml) throws DocumentException {
        String result = ncService.getCombineImageShowUrl(xml);
        NcResult ncResult = new NcResult(getXmlNodeData(result,RSP_CODE),getXmlNodeData(result,RSP_MSG),result);
        return JsonUtils.toJsonString(ncResult);
    }

    /**
     * 收票节点获取影像扫描链接 只能传发票识别
     * @param xml 入参
     * @return 结果
     */
    @ApiOperation("收票节点发票扫描")
    @PostMapping("/invoiceScan")
    public String invoiceScan(String xml) throws DocumentException {
        String result = ncService.invoiceScan(xml);
        NcResult ncResult = new NcResult(getXmlNodeData(result,RSP_CODE),getXmlNodeData(result,RSP_MSG),result);
        return JsonUtils.toJsonString(ncResult);
    }

    /**
     * 更改影像状态
     * @param xml 入参数
     * @return 结果
     */
    @ApiOperation("更改影像状态")
    @PostMapping("/updateimagestate")
    public String updateImageState(String xml) throws DocumentException {
        String result = ncService.updateImageState(xml);
        NcResult ncResult = new NcResult(getXmlNodeData(result,RSP_CODE),getXmlNodeData(result,RSP_MSG),result);
        return JsonUtils.toJsonString(ncResult);
    }

    /**
     * 移动审批查看影像   eq:友空间以及轻推使用
     * @param xml 入参数
     * @return 结果
     */
    @ApiOperation("移动审批查看影像")
    @PostMapping("/CMForMobile_OnePicQuery")
    public String mobileImageQuery(String xml) throws DocumentException {
        String result = ncService.mobileImageQuery(xml);
        NcResult ncResult = new NcResult(getXmlNodeData(result,"RSPCODE"),getXmlNodeData(result,"RSPMSG"),result);
        return JsonUtils.toJsonString(ncResult);
    }

    /**
     * 影像提交
     * @param
     * @return 结果
     */
    @ApiOperation("影像提交")
    @GetMapping("/imageSubmission")
    public R<DataTask> imageSubmission( String businessSerialNo) throws Exception {
        log.info("imageSubmission businessSerialNo:{}",businessSerialNo);
        return ncService.imageSubmission(businessSerialNo);
    }
}
