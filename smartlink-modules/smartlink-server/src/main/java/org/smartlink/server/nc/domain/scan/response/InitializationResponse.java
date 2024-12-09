package org.smartlink.server.nc.domain.scan.response;


import lombok.Data;
import org.smartlink.server.nc.domain.DataBillType;
import org.smartlink.server.nc.domain.DataCurrentTask;
import org.smartlink.server.nc.domain.hardwaremessage.HardWareMessage;
import org.smartlink.server.nc.domain.initialization.vo.ShowScannerDriverInfoVO;
import org.smartlink.server.nc.domain.vo.SysOcrConfigVo;

import java.util.List;

/**
 * @author: 史敦凯
 * @date: 2021/11/9 13:36
 * @description: 初始化接口返回
 */
@Data
public class InitializationResponse {
    /**
     * 批次号
     */
    String batchId;
    /**
     * 扫描仪驱动信息
     */
    List<ShowScannerDriverInfoVO> scannerList;
    /**
     * 可使用的图片类型
     */
    List<String> imageType;
    /**
     * 上传文件名称的长度
     */
    String docNameLength;
    /**
     * 查验识别厂商信息
     */
    List<SysOcrConfigVo> ocrConfigList;
    /**
     * 单据信息
     */
    DataCurrentTask dataCurrentTask;
    /**
     * 单据类型信息
     */
    DataBillType dataBillType;
    /**
     * 是否显示查验过滤开关
     */
    String checkStatus;
    /**
     * 小程序是否需要暂时 1展示
     */
    String weiChatStatus;
    /**
     * 用户token
     */
    String ssoToken;
    /**
     * true 显示预览
     */
    String showSource;
    /**
     * 是否识别
     */
    String isOcr;
    /**
     * 是否查验
     */
    String isCheck;
    /**
     * 补扫能不能删除原流程文件
     */
    String reScanDelete;
    /**
     * 重扫fileids
     */
    List<String> reScanFileIds;
    /**
     * 是否开启乐观锁(0=开启, 1=不开启)
     */
    String isLock;

    /**
     * 上传图片是否弹窗
     */
    String isUploadAlter;
    /**
     * 文件限制上传大小
     */
    String sysFileSize;
    /**
     * 微信小程序APPID
     * */
    String appId;
    /**
     * 微信小程序APPSECRET
     * */
    String appSecret;

    /**
     * 是否开启上传文件重复判断-文件名
     */
    String isUploadFileName;


    /**
     * 授权内容
     */
    HardWareMessage hardWareMessage;

    /**
     * 置信度
     */
    String confidence;

    /**
     * 是否外围系统预览
     */
    String fileViewType;

    /**
     * 外部附件展示URL
     */
    String fileViewUrl;

    /**
     * 是否开启发票检索功能
     */
    String invoiceRetrieval;
    /**
     * 是否开启ncc高级包附件推送,1=开启
     */
    String filePush;

}
