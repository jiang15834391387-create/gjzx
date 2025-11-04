package org.smartlink.business.doman.entity;

import lombok.Data;

@Data
public class BuyerInfo {
    private String name;              // 名称
    private String taxId;             // 纳税人识别号
    private String addressAndPhone;   // 地址、电话
    private String bankAndAccount;    // 开户行及账号

    public BuyerInfo() {
    }

    public BuyerInfo(String name, String taxId, String addressAndPhone, String bankAndAccount) {
        this.name = name;
        this.taxId = taxId;
        this.addressAndPhone = addressAndPhone;
        this.bankAndAccount = bankAndAccount;
    }

    public BuyerInfo(String name, String taxId) {
        this.name = name;
        this.taxId = taxId;
    }

}


