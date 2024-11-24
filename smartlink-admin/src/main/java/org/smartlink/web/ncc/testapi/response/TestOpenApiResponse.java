package org.smartlink.web.ncc.testapi.response;

import lombok.Data;

/**
 * @description: 测试Open Api接口联通性返回实体类
 * @author: L
 * @create:
 **/
@Data
public class TestOpenApiResponse {

    /**
     * 是否成功
     */
    private String success;

    /**
     * 提示信息
     */
    private String message;

    /**
     * data
     */
    private TestOpenApiResponseData data;


}
