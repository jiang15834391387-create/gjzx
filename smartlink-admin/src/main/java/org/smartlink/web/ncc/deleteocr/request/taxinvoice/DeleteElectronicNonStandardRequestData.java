package org.smartlink.web.ncc.deleteocr.request.taxinvoice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: 非标准删除电子发票台账请求类data
 * @author: ChenJiangHong
 * @create: 2023-01-10 18:10
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeleteElectronicNonStandardRequestData {

    private String fpDm;

    private String fpHm;

}
