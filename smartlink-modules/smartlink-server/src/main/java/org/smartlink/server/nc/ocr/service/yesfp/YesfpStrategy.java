package org.smartlink.server.nc.ocr.service.yesfp;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.smartlink.common.redis.utils.RedisUtils;
import org.smartlink.server.nc.constant.*;
import org.smartlink.server.nc.domain.DataCurrentTask;
import org.smartlink.server.nc.domain.DataImageFilesInfo;
import org.smartlink.server.nc.domain.SysUser;
import org.smartlink.server.nc.domain.invoice.DataOcrDetails;
import org.smartlink.server.nc.domain.invoice.DataOcrInfo;
import org.smartlink.server.nc.ocr.exception.OcrException;
import org.smartlink.server.nc.ocr.properties.OcrProperties;
import org.smartlink.server.nc.ocr.service.abstractd.AbstractOcrStrategy;
import org.smartlink.server.nc.ocr.service.bean.IdentificationData;
import org.smartlink.server.nc.ocr.service.yesfp.config.YesfpProperties;
import org.smartlink.server.nc.ocr.service.yesfp.reponse.BipAddressResult;
import org.smartlink.server.nc.ocr.service.yesfp.reponse.OpenApiAccessTokenData;
import org.smartlink.server.nc.ocr.service.yesfp.reponse.YesfpResult;
import org.smartlink.server.nc.ocr.service.yesfp.request.PdfFiles;
import org.smartlink.server.nc.ocr.service.yesfp.request.YesfpOcrRequest;
import org.smartlink.server.nc.ocr.service.yesfp.request.YesfpPDFOcrRequest;
import org.smartlink.server.nc.ocr.service.yesfp.utils.SignUtils;
import org.smartlink.server.nc.service.nc.IDataCurrentTaskService;
import org.smartlink.server.nc.service.nc.ISysUserService;
import org.smartlink.server.nc.utils.NcTypeConvertUtil;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @description: 税务云识别配置类
 * @author: L
 * @create:
 **/
@Slf4j
@Component
@RequiredArgsConstructor
public class YesfpStrategy extends AbstractOcrStrategy {

    private static YesfpProperties yesfpProperties = new YesfpProperties();

    @Resource
    private IDataCurrentTaskService dataCurrentTaskService;

    @Resource
    private ISysUserService sysUserService;

    @Override
    public void init(OcrProperties properties) {
        super.init(properties);
        // 初始化配置
        final String detailInfo = this.properties.getDetailInfo();
        final cn.hutool.json.JSONObject jsonObject = JSONUtil.parseObj(detailInfo);
        // JSON转对象
        yesfpProperties = JSONUtil.toBean(jsonObject, YesfpProperties.class);
        // 判断是不是bip调用接口方式，如果是bip方式则需要在类初始化时候调用openApi接口获取网关地址和获取token地址并存入redis缓存当中
        if (StrUtil.equals(yesfpProperties.getInterFaceType(), Constants.BIP_YESFP_INTERFACE_TYPE)) {
            // bip租户一旦创建所属数据中心不会再改变，将租户地址对应关系持久化到数据库和缓存中,无需每次更新.
            if (StrUtil.isNotEmpty(RedisUtils.getCacheObject(OcrConstant.CACHE_BIP_YESFP_GATEWAY_KEY)) &&
                    StrUtil.isNotEmpty(RedisUtils.getCacheObject(OcrConstant.CACHE_BIP_YESFP_TOEKEN_KEY))
            ) {
                //缓存获取
                log.info("bip yesfp Ocr使用初始化信息！初始化信息：{},{}", RedisUtils.getCacheObject(OcrConstant.CACHE_BIP_YESFP_GATEWAY_KEY), RedisUtils.getCacheObject(OcrConstant.CACHE_BIP_YESFP_TOEKEN_KEY));
            } else {
                setBipAddress2Cache();
            }
        }
        log.info("yesfp Ocr初始化完成！初始化参数：{}", properties);
        isInit = true;
    }


    private void setBipAddress2Cache() {
        String bipOpenApiAddressUrl = yesfpProperties.getBaseUrl() + "?tenantId=" + yesfpProperties.getAppId();
        BipAddressResult bipAddressResult = WebClient.create().get().uri(bipOpenApiAddressUrl)
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(str -> {
                    log.info("BIP税务云网关地址返回最原始报文：" + str);
                    return Mono.just(JSONObject.parseObject(str, BipAddressResult.class));
                })
                .map(ConversionException::checkBipAddressResult).block();
        // 将结果存入缓存当中，方便后续调用获取
        RedisUtils.setCacheObject(OcrConstant.CACHE_BIP_YESFP_GATEWAY_KEY, bipAddressResult.getData().getGatewayUrl());
        RedisUtils.setCacheObject(OcrConstant.CACHE_BIP_YESFP_TOEKEN_KEY, bipAddressResult.getData().getTokenUrl());
    }

    @Override
    public List<IdentificationData> getIdentificationData(DataImageFilesInfo dataImageFilesInfo, byte[] bytes) throws Exception {
        // 设置缓冲区大小，用以拿到base64
        WebClient webClient = WebClient.builder()
                .exchangeStrategies(builder ->
                        builder.codecs(codecs -> codecs.defaultCodecs().
                                maxInMemorySize(20 * 1024 * 1024))).build();
        if (dataImageFilesInfo.getFileType().equals(InvoiceConstants.DOCUMENT_OFD) || dataImageFilesInfo.getFileType().equals(InvoiceConstants.DOCUMENT_PDF)) {
            log.info("进入yesfp pdfOCR识别");
            return getPdfOcrInfo(dataImageFilesInfo, bytes, webClient);
        }
        log.info("进入yesfp OCR识别");
        YesfpOcrRequest yesfpOcrRequest=new YesfpOcrRequest();
        yesfpOcrRequest.setNsrsbh(yesfpProperties.getNsrsbh());
        yesfpOcrRequest.setOrgcode(yesfpProperties.getOrgCode());
        yesfpOcrRequest.setFile(Base64.encode(bytes));
        String request = JSONObject.toJSONString(yesfpOcrRequest);
        String sign;
        String ocrUrl;
        if(StrUtil.equals(yesfpProperties.getInterFaceType(), Constants.BIP_YESFP_INTERFACE_TYPE)){
            OpenApiAccessTokenData openApiAccessToken;
            try {
                openApiAccessToken = TenantAuthProvider.getOpenApiAccessToken(yesfpProperties.getAppKey(), yesfpProperties.getAppSecret(), RedisUtils.getCacheObject(OcrConstant.CACHE_BIP_YESFP_TOEKEN_KEY));
                sign = openApiAccessToken.getAccess_token();
                ocrUrl = RedisUtils.getCacheObject(OcrConstant.CACHE_BIP_YESFP_GATEWAY_KEY) + yesfpProperties.getOcrUrl() + "?access_token="+sign;
            } catch (Exception e) {
                throw new Exception(e);
            }
        }else{
            try {
                sign = SignUtils.sign(request,yesfpProperties.getPrivateKeyType(), yesfpProperties.getP12Path(), yesfpProperties.getPassword());
                ocrUrl = yesfpProperties.getBaseUrl()+yesfpProperties.getOcrUrl() + "?appid=" + yesfpProperties.getAppId();
            } catch (Exception e) {
                throw new OcrException("签名验证失败！");
            }
        }
        YesfpResult yesfpResult = Objects.requireNonNull(webClient.post()
                .uri(ocrUrl)
                .contentType(MediaType.APPLICATION_STREAM_JSON)
                .bodyValue(request)
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE).header("sign", sign)
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(str -> {
                    log.info("税务云返回最原始报文：" + str);
                    return Mono.just(JSONObject.parseObject(str, YesfpResult.class));
                })
                .map(ConversionException::checkOcrYesfpResult)
                .block());
        JSONArray jsonArray;
        if(StrUtil.equals(yesfpProperties.getInterFaceType(),Constants.BIP_YESFP_INTERFACE_TYPE)){
            jsonArray = yesfpResult.getData();
        }else{
            jsonArray = yesfpResult.getDatas();
        }
        return YesfpConverter.convertInvoiceInfo(dataImageFilesInfo,jsonArray);
    }


    /**
     * 税务云获取电子发票OCR信息结果
     * @param dataImageFilesInfo
     * @param bytes
     * @param webClient
     * @return
     */
    private List<IdentificationData> getPdfOcrInfo(DataImageFilesInfo dataImageFilesInfo, byte[] bytes, WebClient webClient) throws Exception {
        DataCurrentTask dataCurrentTask = dataCurrentTaskService.selectDataCurrentTaskByBusinessSerialNo(dataImageFilesInfo.getBusinessSerialNo());
        List<SysUser> sysUsers = sysUserService.selectListByNcUserId(dataCurrentTask.getUserId());
        String useName = CollUtil.isNotEmpty(sysUsers)?sysUsers.get(0).getNickName():"admin";
        PdfFiles pdfFiles = PdfFiles.builder()
                .fileName(dataImageFilesInfo.getFileName())
                .classification("")
                .content(Base64.encode(bytes))
                .srcBillCode(dataCurrentTask.getBillNum())
                .srcBillType("影像")
                .build();
        YesfpPDFOcrRequest yesfpPDFOcrRequest = YesfpPDFOcrRequest.builder()
                .nsrsbh(yesfpProperties.getNsrsbh())
                .orgcode("")
                .usercode(useName)
                .defaultOrgMode("0")
                .useremail("")
                .usermobile("")
                .pdfFiles(ListUtil.of(pdfFiles))
                .build();
        String request = JSONObject.toJSONString(yesfpPDFOcrRequest);
        String sign;
        String ocrUrl;
        if(StrUtil.equals(yesfpProperties.getInterFaceType(), Constants.BIP_YESFP_INTERFACE_TYPE)){
            OpenApiAccessTokenData openApiAccessToken;
            try {
                openApiAccessToken = TenantAuthProvider.getOpenApiAccessToken(yesfpProperties.getAppKey(), yesfpProperties.getAppSecret(), RedisUtils.getCacheObject(OcrConstant.CACHE_BIP_YESFP_TOEKEN_KEY));
                sign = openApiAccessToken.getAccess_token();
                ocrUrl = RedisUtils.getCacheObject(OcrConstant.CACHE_BIP_YESFP_GATEWAY_KEY) + yesfpProperties.getPdfOcrUrl() + "?access_token="+sign;
            } catch (Exception e) {
                throw new Exception(e);
            }
        }else{
            try {
                sign = SignUtils.sign(request,yesfpProperties.getPrivateKeyType(), yesfpProperties.getP12Path(), yesfpProperties.getPassword());
                ocrUrl = yesfpProperties.getBaseUrl()+yesfpProperties.getPdfOcrUrl() + "?appid=" + yesfpProperties.getAppId();
            } catch (Exception e) {
                throw new OcrException("签名验证失败！");
            }
        }
        YesfpResult yesfpResult = Objects.requireNonNull(webClient.post()
                .uri(ocrUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .header("sign", sign)
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(str -> {
                    log.info("税务云电票上传接口返回最原始报文：" + str);
                    return Mono.just(JSONObject.parseObject(str, YesfpResult.class));
                })
                .map(ConversionException::checkOcrYesfpResult)
                .block());
        JSONArray jsonArray;
        if(StrUtil.equals(yesfpProperties.getInterFaceType(),Constants.BIP_YESFP_INTERFACE_TYPE)){
            jsonArray = yesfpResult.getData();
        }else{
            jsonArray = yesfpResult.getDatas();
        }
        return this.changeInfo(dataImageFilesInfo,jsonArray);
    }


    private List<IdentificationData> changeInfo(DataImageFilesInfo dataImageFilesInfo,JSONArray datas){
        // OCR信息处理
        if(CollectionUtil.isNotEmpty(datas)){
            DataOcrInfo dataOcrInfo = new DataOcrInfo();
            String id = IdUtil.simpleUUID();
            dataOcrInfo.setId(id);
            JSONObject result = datas.getJSONObject(0);
            JSONObject invoice = result.getJSONObject("invoice");
            JSONArray jsonArray = invoice.getJSONArray("items");
            String fileType = NcTypeConvertUtil.getSystemFileType(invoice.getString("fplx"));
            dataImageFilesInfo.setFileType(fileType);
            dataImageFilesInfo.setFileStatus(FileStatusConstants.INVOICE_CHECK_SUCCESS);
            dataOcrInfo.setFileId(dataImageFilesInfo.getFileId());
            dataImageFilesInfo.updateById();
            dataOcrInfo.setChecker(invoice.getString("fhr"));
            dataOcrInfo.setInvoiceCode(invoice.getString("fpDm"));
            dataOcrInfo.setInvoiceNumber(invoice.getString("fpHm"));
            dataOcrInfo.setPassword1(invoice.getString("fpMw"));
            dataOcrInfo.setBuyerAddress(invoice.getString("gmfDzdh"));
            dataOcrInfo.setBuyerName(invoice.getString("gmfMc"));
            dataOcrInfo.setBuyerNo(invoice.getString("gmfNsrsbh"));
            dataOcrInfo.setBuyerAccount(invoice.getString("gmfYhzh"));
            dataOcrInfo.setPretaxAmount(Convert.toBigDecimal(invoice.getString("hjje")));
            dataOcrInfo.setSumAmount(Convert.toBigDecimal(invoice.getString("hjje")));
            dataOcrInfo.setSumTax(Convert.toBigDecimal(invoice.getString("hjse")));
            dataOcrInfo.setTotalUppercase(Convert.digitToChinese(Convert.toBigDecimal(invoice.getString("jshj"))));
            dataOcrInfo.setTotalLowercase(Convert.toBigDecimal(invoice.getString("jshj")));
            dataOcrInfo.setCheckCode(invoice.getString("jym"));
            dataOcrInfo.setInvoiceDate(Convert.toDate(invoice.getString("kprq")));
            dataOcrInfo.setSellerAddress(invoice.getString("xsfDzdh"));
            dataOcrInfo.setSellerName(invoice.getString("xsfMc"));
            dataOcrInfo.setSellerNo(invoice.getString("xsfNsrsbh"));
            dataOcrInfo.setSellerAccount(invoice.getString("xsfYhzh"));
            dataOcrInfo.setCheckInvoice(CheckConstant.SUCCESS_CHECK);
            List<DataOcrDetails> detailsList = new ArrayList<>(16);
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject detailJson = jsonArray.getJSONObject(i);
                DataOcrDetails dataOcrDetails = new DataOcrDetails();
                dataOcrDetails.setId(id);
                dataOcrDetails.setUnit(detailJson.getString("dw"));
                dataOcrDetails.setStandard(detailJson.getString("ggxh"));
                dataOcrDetails.setFileId(dataImageFilesInfo.getFileId());
                dataOcrDetails.setOcrId(id);
                dataOcrDetails.setTax(Convert.toBigDecimal(detailJson.getString("se")));
                dataOcrDetails.setTaxRate(detailJson.getString("sl"));
                dataOcrDetails.setPrice(Convert.toBigDecimal(detailJson.getString("xmdj")));
                dataOcrDetails.setDetailAmount(Convert.toBigDecimal(detailJson.getString("xmje")));
                dataOcrDetails.setName(detailJson.getString("xmmc"));
                dataOcrDetails.setDetailsCount(Convert.toBigDecimal(detailJson.getString("xmsl")));
                detailsList.add(dataOcrDetails);
            }
            dataOcrInfo.setDetails(detailsList);
            dataOcrInfo.setPushBusinessInfoFlag(NcConstant.PUSH_BUSINESS_INFO_SUCCESS);
            return ListUtil.of(new IdentificationData<>(fileType,dataOcrInfo));
        }
        return null;
    }

//    @Override
//    public List<ItemContractListBean> getIdentificationContractData(DataContract dataContract, byte[] bytes) {
//        return null;
//    }
//
//    @Override
//    public CcintDocumentResult getIdentificationDocument(byte[] byteOld) {
//        return null;
//    }

}
