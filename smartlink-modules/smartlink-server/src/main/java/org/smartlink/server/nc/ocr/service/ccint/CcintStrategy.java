package org.smartlink.server.nc.ocr.service.ccint;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.smartlink.server.nc.domain.DataImageFilesInfo;
import org.smartlink.server.nc.ocr.exception.OcrException;
import org.smartlink.server.nc.ocr.properties.OcrProperties;
import org.smartlink.server.nc.ocr.service.abstractd.AbstractOcrStrategy;
import org.smartlink.server.nc.ocr.service.abstractd.ConversionFactory;
import org.smartlink.server.nc.ocr.service.bean.IdentificationData;
import org.smartlink.server.nc.ocr.service.ccint.config.CcintOcrProperties;
import org.smartlink.server.nc.ocr.service.ccint.response.CcintResult;
import org.smartlink.server.nc.ocr.service.ccint.service.DealWithService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @program: YuYing
 * @description: CCOCR识别配置类
 * @author: L
 * @create:
 **/
@Slf4j
@Component
@RequiredArgsConstructor
public class CcintStrategy extends AbstractOcrStrategy {

    private static CcintOcrProperties ccintOcrProperties = new CcintOcrProperties();

    @Autowired
    private DealWithService dealWithService;

    @Override
    public void init(OcrProperties properties) {
        super.init(properties);
        // 初始化配置
        final String detailInfo = this.properties.getDetailInfo();
        final cn.hutool.json.JSONObject jsonObject = JSONUtil.parseObj(detailInfo);
        // JSON转对象
        ccintOcrProperties = JSONUtil.toBean(jsonObject, CcintOcrProperties.class);
        log.info("合合Ocr初始化完成！初始化参数：{}", properties);
        isInit = true;
    }

    @Override
    public List<IdentificationData> getIdentificationData(DataImageFilesInfo dataImageFilesInfo, byte[] bytes) throws OcrException {
        if (StrUtil.equals("0", ccintOcrProperties.getYsOff())) {
            log.info("进入YUSHUI OCR识别");
            OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(100000, TimeUnit.MILLISECONDS).readTimeout(100000, TimeUnit.MILLISECONDS).build();
            MultipartBody.Builder builder = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file", "YSocr.jpg", RequestBody.create(bytes));
            Request request = new Request.Builder()
                    .url(ccintOcrProperties.getUrl())
                    .post(builder.build())
                    .build();
            try {
                Response response = okHttpClient.newCall(request).execute();
                String string = response.body().string();
                // log.info("ocr最原始报文："+string);
                CcintResult res = JSONObject.parseObject(string, CcintResult.class);
                List<IdentificationData> collect = res.getResult().getObject_list().stream().map((e) -> {
                    return ConversionFactory.getConversionFactory(dataImageFilesInfo, e).changeInfo(dataImageFilesInfo, e);
                }).collect(Collectors.toList());
                return collect;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            log.info("进入CCINT OCR识别");
            // 设置缓冲区大小，用以拿到base64
            WebClient webClient = WebClient.builder()
                    .exchangeStrategies(builder ->
                            builder.codecs(codecs -> codecs.defaultCodecs().
                                    maxInMemorySize(20 * 1024 * 1024))).build();
            // 公网请求
            //CcintResult block = webClient.post().uri("https://api.textin.com/robot/v1.0/api/bills_crop?confidence=1&crop_complete_image=1").header("x-ti-app-id", "ad880e427ed5d674df8f58b6d3eb9df3").header("x-ti-secret-code", "03d0b3f9cd5e59394ef4de1fb8946adf").accept(MediaType.APPLICATION_OCTET_STREAM).bodyValue(bytes).retrieve().bodyToMono(CcintResult.class).block();
            //String url = "http://g3p4704077.qicp.vip:25308/cci_ai/service/v1/receipt_crop_and_recog?confidence=1";

            // 私有化部署请求
            CcintResult block = webClient.post().uri(ccintOcrProperties.getUrl()).header("x-ti-app-id", ccintOcrProperties.getAppKey()).header("x-ti-secret-code", ccintOcrProperties.getAppSecret()).accept(MediaType.APPLICATION_OCTET_STREAM).bodyValue(bytes).retrieve().bodyToMono(CcintResult.class).block();
            List<IdentificationData> collect = block.getResult().getObject_list().stream().map(e -> ConversionFactory.getConversionFactory(dataImageFilesInfo, e).changeInfo(dataImageFilesInfo, e)).collect(Collectors.toList());

            //log.info("———————OCR识别结果：" + JSONObject.toJSON(block));

            //进行发票二次校验
            List<IdentificationData> collect2 = new ArrayList<>(16);
            for (IdentificationData identificationData : collect) {
                collect2.add(dealWithService.dealIdentificationData(identificationData));
            }
            return collect;
        }
    }

//    @Override
//    public List<ItemContractListBean> getIdentificationContractData(DataContract dataContract, byte[] bytes) {
//        log.info("进入YUSHUI 合同OCR识别");
//        OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(100000, TimeUnit.MILLISECONDS)
//                .readTimeout(100000, TimeUnit.MILLISECONDS).build();
//        MultipartBody.Builder builder = new MultipartBody.Builder()
//                .setType(MultipartBody.FORM)
//                .addFormDataPart("file", "YSocr.doc", RequestBody.create(bytes));
//        Request request = new Request.Builder()
//                //.url("http://111.202.91.50:8092/mrc/parser/")
//                .url(ccintOcrProperties.getDocUrl())
//                .post(builder.build())
//                .build();
//        try {
//            Response response = okHttpClient.newCall(request).execute();
//            String string = response.body().string();
//            log.info("======调用合同识别OCR响应报文：{}",string);
//            CcintContractResult res = JSONObject.parseObject(string, CcintContractResult.class);
//            List<ItemContractListBean> values = res.getValues();
//            return values;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    @Override
//    public CcintDocumentResult getIdentificationDocument(byte[] file) throws Exception {
//        // 办公文档识别只支持jpg, png, bmp, pdf, tiff, 单帧gif
//        //String documentOcrUrl = "https://api.textin.com/ai/service/v2/recognize/document";
//        //String appId = "f953ba79b5b40674c50f154d31728eaf";
//        //String secretCode = "2e632b990f6e936f1c65694c04ea0305";
//        String documentOcrUrl = ccintOcrProperties.getDocumentOcrUrl();
//        String appId = ccintOcrProperties.getDocumentAppId();
//        String secretCode = ccintOcrProperties.getDocumentSecretCode();
//        BufferedReader in = null;
//        DataOutputStream out = null;
//        String result = "";
//        try {
//            URL realUrl = new URL(documentOcrUrl);
//            HttpURLConnection conn = (HttpURLConnection)realUrl.openConnection();
//            conn.setRequestProperty("connection", "Keep-Alive");
//            conn.setRequestProperty("Content-Type", "application/octet-stream");
//            conn.setRequestProperty("x-ti-app-id", appId);
//            conn.setRequestProperty("x-ti-secret-code", secretCode);
//            conn.setDoOutput(true);
//            conn.setDoInput(true);
//            conn.setRequestMethod("POST"); // 设置请求方式
//            out = new DataOutputStream(conn.getOutputStream());
//            out.write(file);
//            out.flush();
//            out.close();
//            in = new BufferedReader(
//                    new InputStreamReader(conn.getInputStream(), "UTF-8"));
//            String line;
//            while ((line = in.readLine()) != null) {
//                result += line;
//            }
//        } catch (Exception e) {
//            System.out.println("发送 POST 请求出现异常！" + e);
//            e.printStackTrace();
//        }
//        finally {
//            try {
//                if (out != null) {
//                    out.close();
//                }
//                if (in != null) {
//                    in.close();
//                }
//            } catch (IOException ex) {
//                ex.printStackTrace();
//            }
//        }
//        log.info("======请求合合文档全文识别响应报文：{}",result);
//        CcintDocumentResult res = JSONObject.parseObject(result, CcintDocumentResult.class);
//        if (ObjectUtil.isNotEmpty(res) && res.getCode()!=200){
//            throw new Exception(res.getMessage());
//        }
//        return res;
//    }
}
