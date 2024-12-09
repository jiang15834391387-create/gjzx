package org.smartlink.server.nc.utils;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.smartlink.common.core.utils.file.MimeTypeUtils;
import org.smartlink.common.redis.utils.RedisUtils;
import org.smartlink.server.nc.constant.Constants;
import org.smartlink.server.nc.constant.ParamConstants;
import org.smartlink.server.nc.domain.DataImageFilesInfo;
import org.smartlink.server.nc.utils.file.FileUtils;

import java.util.List;
import java.util.Map;

/**
 * @description: 针对nc接口返回处理类
 * @author: L
 * @create:
 **/
public class ResultUtil {


    /**
     * 组装Data返回数据
     * @param code 状态码
     * @param message 状态信息
     * @param map 组装参数
     * @return 返回体
     */
    public static String getRespXML(String code, String message, Map<String,Object> map) {
        Document response = DocumentHelper.createDocument();
        response.setXMLEncoding("utf-8");
        Element body = response.addElement("DATA");
        Element RspCode = body.addElement("RspCode");
        RspCode.setText(code);
        Element RspMsg = body.addElement("RspMsg");
        RspMsg.setText(message);
        if(ObjectUtil.isNotEmpty(map)){
            map.forEach((key,value)->{
                Element RspUrl = body.addElement(key);
                RspUrl.setText(value.toString());
            });
        }
        return response.asXML();
    }

    public static String getRespXmlForMobile(String code, String message, Map<String,Object> map, Map<String, List<DataImageFilesInfo>> dataImageFilesInfoMap, String batchId){
        Document response = DocumentHelper.createDocument();
        response.setXMLEncoding("utf-8");
        Element body = response.addElement("CMDATA");
        Element RspCode = body.addElement("RSPCODE");
        RspCode.setText(code);
        Element RspMsg = body.addElement("RSPMSG");
        RspMsg.setText(message);
        if(ObjectUtil.isNotEmpty(map)){
            map.forEach((key,value)->{
                Element RspUrl = body.addElement(key);
                RspUrl.setText(value.toString());
            });
        }
        if(CollectionUtil.isNotEmpty(dataImageFilesInfoMap)){
            Element batchElement = body.addElement("BATCH");
            Element batchIdElement = batchElement.addElement("BATCHID");
            if(StrUtil.isNotEmpty(batchId)){
                batchIdElement.setText(batchId);
            }else {
                batchIdElement.setText("");
            }
            Element documentsElement = batchElement.addElement("DOCUMENTS");
            dataImageFilesInfoMap.forEach((key,value)->{
                Element documentElement = documentsElement.addElement("DOCUMENT");
                Element docNameElement = documentElement.addElement("DOCNAME");
                if(StrUtil.isNotEmpty(key)){
                    docNameElement.setText(key);
                }else {
                    docNameElement.setText("");
                }
                Element descElement = documentElement.addElement("DESC");
                if(StrUtil.isNotEmpty(key)){
                    descElement.setText(key);
                }else {
                    descElement.setText("");
                }
                Element filesElement = documentElement.addElement("FILES");
                for (DataImageFilesInfo imageFilesInfo : value) {
                    Element fileElement = filesElement.addElement("FILE");
                    Element urlElement = fileElement.addElement("URL");
                    String url;
                    String suffix = FileUtils.getFileSuffix(imageFilesInfo.getFileName());
                    if(ArrayUtil.containsIgnoreCase(MimeTypeUtils.IMAGE_EXTENSION, suffix)){
                        url = FileUtils.getFileUrlResource(RedisUtils.getCacheObject(Constants.SYS_CONFIG_KEY + ParamConstants.SYS_CONFIG_IP) + ":" + RedisUtils.getCacheObject(Constants.SYS_CONFIG_KEY + ParamConstants.SYS_CONFIG_PORT) + imageFilesInfo.getIurl(), imageFilesInfo.getFileId());
                    }else{
                        // 文件格式的取url源文件字段
                        url = FileUtils.getFileUrlResource(RedisUtils.getCacheObject(Constants.SYS_CONFIG_KEY + ParamConstants.SYS_CONFIG_IP) + ":" + RedisUtils.getCacheObject(Constants.SYS_CONFIG_KEY + ParamConstants.SYS_CONFIG_PORT) + imageFilesInfo.getUrl(), imageFilesInfo.getFileId());
                    }
                    if(StrUtil.isNotEmpty(url)){
                        urlElement.setText(url);
                    }else {
                        urlElement.setText("");
                    }
                    Element versionElement = fileElement.addElement("VERSION");
                    versionElement.setText("1");
                    Element fileSeqElement = fileElement.addElement("FILE_SEQ");
                    fileSeqElement.setText("0");
                    Element fileTypeElement = fileElement.addElement("FILE_TYPE");
                    fileTypeElement.setText("1");
                    Element fileNameElement = fileElement.addElement("FILE_NAME");
                    if(StrUtil.isNotEmpty(imageFilesInfo.getFileName())){
                        fileNameElement.setText(imageFilesInfo.getFileName());
                    }else {
                        fileNameElement.setText("");
                    }
                    Element fileFormatElement = fileElement.addElement("FILE_FORMAT");
                    fileFormatElement.setText("0");
                    Element fileSizeElement = fileElement.addElement("FILE_SIZE");
                    if(StrUtil.isNotEmpty(imageFilesInfo.getFileSize())){
                        fileSizeElement.setText(imageFilesInfo.getFileSize());
                    }else {
                        fileSizeElement.setText("0");
                    }
                    Element fileMd5Element = fileElement.addElement("FILE_MD5");
                    if(StrUtil.isNotEmpty(imageFilesInfo.getFileMd5())){
                        fileMd5Element.setText(imageFilesInfo.getFileMd5());
                    }else {
                        fileMd5Element.setText("");
                    }
                }
            });
        }
        return response.asXML();
    }

    /**
     * 组装对外暴露URL地址
     * @param url 返回地址
     * @param map 拼接参数列表
     * @return 完整地址
     */
    public static String getUrl(String url,Map<String,Object> map){
        StringBuffer buffer = new StringBuffer(url);
        if(ObjectUtil.isNotEmpty(map)){
            map.forEach((key,value)->{
                buffer.append("&"+key+"="+value);
            });
        }
        return buffer.toString();
    }


}
