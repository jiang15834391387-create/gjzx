package org.smartlink.web.ocr.service.yesfp.config;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import lombok.Data;
import org.smartlink.common.json.utils.JsonUtils;
import org.smartlink.common.redis.utils.RedisUtils;
import org.smartlink.web.constant.OcrConstant;
import org.smartlink.web.ocr.properties.OcrProperties;

/**
 * @author L
 * @titles 税务云配置类
 * @description 税务云配置类
 * @date
 */
@Data
public class YesfpProperties {
    /**
     * appid
     */
    private String appId;
    /**
     * 请求ocr识别地址
     */
    private String ocrUrl;
    /**
     * 请求查验地址
     */
    private String checkUrl;
    /**
     * 非增值税发票保存台账地址
     */
    private String saveUrl;
    /**
     * 证书密码
     */
    private String password;
    /**
     * 纳税人识别号
     */
    private String nsrsbh;
    /**
     * 组织编号
     */
    private String orgCode;
    /**
     * 证书获取路径
     */
    private String p12Path;
    /**
     * 证书格式 CA PEM
     */
    private String privateKeyType;
    /**
     * 厂商code
     */
    private String factorycode;
    /**
     * 税务云电票识别接口
     */
    private String pdfOcrUrl;
    /**
     * 增值税发票保存台账地址
     */
    private String submitUrl;
    /**
     * 删除台账地址
     */
    private String deleteUrl;
    /**
     * bip模式下为bip税务云网关地址 yesfp3.0模式下为税务云基础地址
     */
    private String baseUrl;
    /**
     * 接口调用方式 bip(新) yesfp(旧)
     */
    private String interFaceType;
    /**
     * appkey
     */
    private String appKey;
    /**
     * app
     */
    private String appSecret;


    public static YesfpProperties getDefaultYesfpProperties(){
        String type = RedisUtils.getCacheObject(OcrConstant.CACHE_CONFIG_KEY);
        Object json = RedisUtils.getCacheObject(OcrConstant.SYS_OCR_KEY + type);
        OcrProperties properties = JsonUtils.parseObject(json.toString(), OcrProperties.class);
        String detailInfo = properties.getDetailInfo();
        cn.hutool.json.JSONObject jsonObject = JSONUtil.parseObj(detailInfo);
        return JSONUtil.toBean(jsonObject,YesfpProperties.class);
    }



    public static void main(String[] args) {
        YesfpProperties yesfpProperties = new YesfpProperties();
        yesfpProperties.setAppId("commontesterCA");
        yesfpProperties.setOcrUrl("https://yesfp.yonyoucloud.com/input-tax/api/ocr/v2/recognise");
        yesfpProperties.setCheckUrl("https://yesfp.yonyoucloud.com/invoiceclient-web/api/reimburseCollection/ncc/verify_and_save");
        yesfpProperties.setSaveUrl("https://yesfp.yonyoucloud.com/input-tax/api/bill-collections/ocr-save");
        yesfpProperties.setSubmitUrl("https://yesfp.yonyoucloud.com/invoiceclient-web/api/reimburseCollection/ncc/submit");
        yesfpProperties.setPassword("password");
        yesfpProperties.setNsrsbh("201609140000001");
        yesfpProperties.setOrgCode("20160914001");
        yesfpProperties.setP12Path("C://pro22.pfx");
        yesfpProperties.setPrivateKeyType("CA2");
        yesfpProperties.setDeleteUrl("https://yesfp.yonyoucloud.com/input-tax/api/bill-collections/delete");
        yesfpProperties.setPdfOcrUrl("https://yesfp.yonyoucloud.com/input-tax/invoiceclient-web/api/reimburseCollection/v4/uploadpdf");
        System.out.println(JSON.toJSONString(yesfpProperties));
    }

}

