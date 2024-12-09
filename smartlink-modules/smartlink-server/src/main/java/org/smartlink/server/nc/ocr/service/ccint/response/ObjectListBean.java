package org.smartlink.server.nc.ocr.service.ccint.response;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ObjectListBean {
    /**
     * 切割后的单张票据图像角度
     */
    private int image_angle;
    /**
     * 切割后的单张票据 type 字段对应的中文描述
     */
    private String type_description;
    /**
     * 切割后的单张票据旋转后图片宽度
     */
    private int rotated_image_width;
    /**
     * 图片票据大类类型，具体类型请见文档下方 class 类型说明
     */
    @JsonProperty("class")
    private String classX;
    /**
     * 切割后的单张票据旋转后图片高度
     */
    private int rotated_image_height;
    /**
     * 票据用途类型 – 英文字段
     */
    private String kind;
    /**
     * 票据用途类型 – 中文字段
     */
    private String kind_description;
    /**
     * 图片票据具体类型，具体类型请见文档下方 type 类型说明
     */
    private String type;
    /**
     * 切割后的单张票据在图片中的坐标
     */
    private List<Integer> position;
    /**
     * 切割后的单张票据 OCR 识别结果
     */
    private List<ItemListBean> item_list;

    /**
     * 增票明细行
     * 合合公有云, 禹水私有合合结构返回
     */
    private List<List<ItemListBean>> product_list;

    /**
     * 飞机行程单字段
     */
    @JsonProperty("flight_data_list")
    private ArrayList<ItemListBean>[] flight_data_list;
    /**
     * 1、 关键字段切片图像的 base64 编码
     * 2、 多张票据图像中切割后单张票据图像的 base64 编码
     */
    @JSONField(serialize=false)
    private String image;
}
