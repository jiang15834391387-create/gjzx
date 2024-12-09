package org.smartlink.server.nc.ocr.service.yesfp.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * <p>Title: YesfpDeleteData </p>
 * <p>
 * <p>Description: TODO </p>
 *
 * @author L
 * @version 1.0.0
 * @date
 **/
@Getter
@Setter
public class YesfpDeleteData {

    private String nsrsbh;

    private String orgcode;

    private List<YesfpDeleteBills> bills;

}
