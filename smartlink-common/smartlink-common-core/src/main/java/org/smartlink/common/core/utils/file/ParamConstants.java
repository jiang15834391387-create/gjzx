package org.smartlink.common.core.utils.file;

/**
 * 参数常量信息
 *
 * @author shidunkai
 */
public interface ParamConstants {

    /**
     * 账号自助-验证码开关
     */
    String SYS_ACCOUNT_CAPTCHAONOFF = "sys.account.captchaOnOff";
    /**
     * 主框架页-皮肤主题
     */
    String SYS_INDEX_SKINNAME="sys.index.skinName";
    /**
     * 用户管理-账号初始密码
     */
    String SYS_USER_INITPASSWORD="sys.user.initPassword";
    /**
     * 主框架页-侧边栏主题
     */
    String SYS_INDEX_SIDETHEME="sys.index.sideTheme";
    /**
     * 账号自助-是否开启用户注册功能
     */
    String SYS_ACCOUNT_REGISTERUSER="sys.account.registerUser";
    /**
     * OSS预览列表资源开关
     */
    String SYS_OSS_PREVIEWLISTRESOURCE="sys.oss.previewListResource";
    /**
     * 本地磁盘存储路径
     */
    String SYS_LOCAL_LOCATION="sys.local.location";
    /**
     * 是否开启切图 true开启
     */
    String SYS_OCR_CUT="sys.ocr.cut";
    /**
     * 附件是否支持直接图片预览
     */
    String SYS_OCR_DOCUMENT="sys.ocr.document";
    /**
     * 是否必须上传电子发票源文件
     */
    String SYS_INVOICE_SOURCE="sys.invoice.source";
    /**
     * 允许上传的文件类型
     */
    String SYS_INVOICE_TYPE="sys.invoice.type";
    /**
     * 是否显示小程序上传框
     */
    String SYS_MINI_ICO="sys.mini.ico";
    /**
     * 是否显示查验过滤开关
     */
    String SYS_CHECK_SHOW="sys_check_show";
    /**
     * PC端是否使用查验功能配置开关
     */
    String SYS_CHECK_OFF="sys_check_off";
    /**
     * PC端是否使用识别功能配置开关
     */
    String SYS_OCR_OFF="sys_ocr_off";
    /**
     * 微信小程序端是否使用查验功能配置开关
     */
    String SYS_WECHAT_CHECK_OFF="sys_wechat_check_off";
    /**
     * 微信小程序端是否使用识别功能配置开关
     */
    String SYS_WECHAT_OCR_OFF="sys_wechat_ocr_off";
    /**
     * 单张图片是否切图转正
     */
    String SYS_IMG_ROTATE="sys_img_rotate";

    /**
     * 系统·IP地址，提供给友报帐使用
     */
    String SYS_CONFIG_IP = "sys_config_ip";

    /**
     * 系统·端口号，提供给友报帐使用
     */
    String SYS_CONFIG_PORT = "sys_config_port";

    /**
     * 影像系统平台对外暴露访问IP
     */
    String SYS_VISIT_IP = "sys_visit_ip";

    /**
     * 影像系统平台对外暴露访问PORT
     */
    String SYS_VISIT_PORT = "sys_visit_port";

    /**
     * 是否开启盲水印功能
     */
    String SYS_VM_SWITCH = "sys_vm_switch";

    /**
     * 是否显示合并pdf按钮
     */
    String SYS_MERGE_BUTTON="sys_merge_button";

    /**
     * 临时文件存储位置 建议与配置文件相同，不是必须
     */
    String SYS_SERVLET_MULTIPART="sys_servlet_multipart";

    /**
     * 查看页面水印类型 1 时间 2 用户+时间
     */
    String SYS_VISIT_WATERMARK="sys_visit_watermark";

    /**
     * 是否走与NC业务系统相关代码逻辑
     */
    String SYS_NC_BUSINESS = "sys_nc_business";

    /**
     * 二维码识别服务地址
     */
    String SYS_QRCODE_SERVICE_URL = "sys_qrcode_service_url";

    /**
     * 批扫二维码内容类型 billNumber：单据号；barCode：条码
     */
    String SYS_QRCODE_CONTENT_TYPE = "sys_qrcode_content_type";

    /**
     * 是否走BIP业务系统相关代码逻辑
     */
    String SYS_BIP_BUSINESS = "sys_bip_business";

    /**
     * 是否走NC65税务云模式业务系统相关代码逻辑
     */
    String SYS_NC65_BUSINESS = "sys_nc65_business";

    /**
     * 授权文件des
     */
    String SYS_SECURE_DES="sys_secure_des";
    /**
     * 授权文件clientId
     */
    String SYS_SECURE_CLIENTID="sys_secure_clientid";
    /**
     * 授权文件路径
     */
    String SYS_SECURE_AUTHFILE="sys_secure_authfile";
    /**
     * 补扫能不能删除原流程文件
     */
    String SYS_SECURE_RESCAN="sys_secure_rescan";

    /**
     * 文件限制上传大小
     */
    String SYS_FILE_SIZE="sys_file_size";

    /**
     * 微信小程序APPID
     * */
    String APP_ID="app_id";

    /**
     * 微信小程序APPSECRET
     * */
    String APP_SECRET="app_secret";

    /**
     * 自定义树节点编号
     */
    String TREE_NODE_CODE="tree_node_code";

    /**
     * 页面是否自主获取token
     */
    String WEB_AUTO_AUTONOMOUS="web_auto_autonomous";

    /**
     * 是否开启锁控制
     */
    String SYS_LOCK_CONTROL="sys_lock_control";

    /**
     * NCC抬头校验
     */
    String INVOICE_CHECK_LOOKUP="invoice_check_lookup";

    /**
     * 发票重复查看范围 true整个库 false当前单据
     */
    String INVOICE_CHECK_REPEAT="invoice_check_repeat";

    /**
     * 是否上传文件重复判断-文件名
     */
    String SYS_UPLOAD_FILENAME_OFF="sys_upload_fileName_off";

    /**
     *硬件授权数量
     * */
    String MAC_SUM="mac_sum";

    /**
     *小程序授权数量
     * */
    String MINI_SUM="mini_sum";

    /**
     *查验次数-针对公网
     * */
    String MAX_CHECK="max_check";

    /**
     *ocr次数-针对公网
     * */
    String MAX_OCR="max_ocr";

    /**
     * 是否开启附件推送外部业务系统机制
     */
    String SYS_PUSH_FILE_BUSINESS_SYSTEM="sys_push_file_business_system";

    /**
     * 置信度 率 0-1 0为关闭
     */
    String INVOICE_CONFIDENCE_CODE="invoice_confidence_code";
}
