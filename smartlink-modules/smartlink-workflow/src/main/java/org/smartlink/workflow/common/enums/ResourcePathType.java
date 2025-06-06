package org.smartlink.workflow.common.enums;

/**
 * 资源路径类型枚举
 */
public enum ResourcePathType {
    /**
     * 类路径资源（src/main/resources/...）
     */
    CLASSPATH("classpath", "类路径资源"),

    /**
     * 文件系统资源（绝对路径或相对路径）
     */
    FILESYSTEM("filesystem", "文件系统资源"),

    PDF_SUFFIX(".pdf", "pdf文件后缀"),
    CONTENT_TYPE_PDF("application/pdf", "内容类型PDF"),

    /**
     *
     * 网络资源（HTTP/HTTPS）
     */
    URL("url", "网络资源");

    private final String value;
    private final String chineseName;  // 新增的中文含义属性

    ResourcePathType(String value, String chineseName) {
        this.value = value;
        this.chineseName = chineseName;
    }

    public String getValue() {
        return value;
    }

    /**
     * 获取中文含义
     */
    public String getChineseName() {
        return chineseName;
    }

    /**
     * 根据字符串值获取对应枚举
     * @param value 输入值（不区分大小写）
     * @return 匹配的枚举，未找到时抛出IllegalArgumentException
     */
    public static ResourcePathType fromValue(String value) {
        for (ResourcePathType type : values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("无效的资源路径类型: " + value +
               "，可选值: classpath/filesystem/url");
    }

    /**
     * 判断是否为网络资源
     */
    public boolean isUrl() {
        return this == URL;
    }

    /**
     * 判断是否为文件系统资源
     */
    public boolean isFileSystem() {
        return this == FILESYSTEM;
    }

    /**
     * 判断是否为类路径资源
     */
    public boolean isClasspath() {
        return this == CLASSPATH;
    }
}
