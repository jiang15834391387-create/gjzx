package org.smartlink.server.nc.ocr.service.ccint.ccintenum;

/**
 * @program: YuYing
 * @description: 合合key对应字段
 * @author: L
 * @create:
 */

public enum CcintKeyValueEnum {
    /**
     * type 为 vat_special_invoice（增值税专用发票）、vat_electronic_invoice（增
     * 值税电子普通发票）、vat_common_invoice（增值税普通发票）、
     * vat_electronic_toll_invoice（增值税电子普通发票（通行费））, key 返回的
     * 类型
     */

    CCINT_KEY_VAT_INVOICE_CORRECT_QE("vat_invoice_qr_code", "vatQR"),
    CCINT_KEY_VAT_INVOICE_CORRECT_CODE("vat_invoice_correct_code", "jym"),

    CCINT_KEY_VAT_INVOICE_DAIMA("vat_invoice_daima","invoiceCode"),

    CCINT_KEY_VAT_INVOICE_HAOMA("vat_invoice_haoma","invoiceNumber"),

    CCINT_KEY_VAT_INVOICE_HAOMA_LARGE_SIZE("vat_invoice_haoma_large_size","字体较大的发票号码"),

    CCINT_KEY_VAT_INVOICE_ISSUE_DATE("vat_invoice_issue_date","invoiceDate"),

    CCINT_KEY_VAT_INVOICE_RATE_PAYER_ID("vat_invoice_rate_payer_id","buyerNo"),

    CCINT_KEY_VAT_INVOICE_TOTAL("vat_invoice_total","sumAmount"),

    CCINT_KEY_VAT_INVOICE_TAX_RATE("vat_invoice_tax_rate","税率"),

    CCINT_KEY_VAT_INVOICE_JIDA_HAOMA("vat_invoice_jida_haoma","machineCode"),

    CCINT_KEY_VAT_INVOICE_SELLER_NAME("vat_invoice_seller_name","sellerName"),

    CCINT_KEY_VAT_INVOICE_SELLER_BANK_ACCOUNT("vat_invoice_seller_bank_account","sellerAccount"),

    CCINT_KEY_VAT_INVOICE_SELLER_ID("vat_invoice_seller_id","sellerNo"),

    CCINT_KEY_VAT_INVOICE_SELLER_ADDR_TELL("vat_invoice_seller_addr_tell","sellerAddr"),

    CCINT_KEY_VAT_INVOICE_PAYER_NAME("vat_invoice_payer_name","buyerName"),

    CCINT_KEY_VAT_INVOICE_PAYER_BANK_ACCOUNT("vat_invoice_payer_bank_account","buyerAccount"),

    CCINT_KEY_VAT_INVOICE_PAYER_ADDR_TELL("vat_invoice_payer_addr_tell","buyerAddr"),

    CCINT_KEY_VAT_INVOICE_TOTAL_COVER_TAX("vat_invoice_total_cover_tax","uppercase"),

    CCINT_KEY_VAT_INVOICE_TOTAL_COVER_TAX_DIGITS("vat_invoice_total_cover_tax_digits","lowercase"),

    CCINT_KEY_VAT_INVOICE_HEADLINE_PAGE_NUMBER("vat_invoice_headline_page_number","标题发票联（普票，电子发票，专票增加字段）"),

    CCINT_KEY_VAT_INVOICE_TOTAL_PRINT("vat_invoice_total_print","invoicePrintTotal"),

    CCINT_KEY_VAT_INVOICE_CORRENT_CODE_PRINT("vat_invoice_corrent_code_print","invoicePrintCheckCode"),

    CCINT_KEY_VAT_INVOICE_TAX_TOTAL("vat_invoice_tax_total","sumTax"),

    CCINT_KEY_VAT_INVOICE_GOODS_LIST("vat_invoice_goods_list","itemNames"),

    CCINT_KEY_VAT_INVOICE_PRICE_LIST("vat_invoice_price_list","amount"),

    CCINT_KEY_VAT_INVOICE_TAX_RATE_LIST("vat_invoice_tax_rate_list","rate"),

    CCINT_KEY_VAT_INVOICE_TAX_LIST("vat_invoice_tax_list","tax"),

    CCINT_KEY_VAT_INVOICE_ZHUAN_YONG_FLAG("vat_invoice_zhuan_yong_flag","invoiceType"),

    CCINT_KEY_VAT_INVOICE_DAI_KAI_FLAG("vat_invoice_dai_kai_flag","invoiceReplaceOpen"),

    CCINT_KEY_VAT_INVOICE_PLATE_SPECIFIC("vat_invoice_plate_specific","standard"),

    CCINT_KEY_VAT_INVOICE_ELECTRANS_UNIT("vat_invoice_electrans_unit","unit"),

    CCINT_KEY_VAT_INVOICE_ELECTRANS_QUANTITY("vat_invoice_electrans_quantity","count"),

    CCINT_KEY_VAT_INVOICE_ELECTRANS_UNIT_PRICE("vat_invoice_electrans_unit_price","price"),

    CCINT_KEY_VAT_INVOICE_DAIMA_RIGHT_SIDE("vat_invoice_daima_right_side","invoiceRightInvoiceCode"),

    CCINT_KEY_VAT_INVOICE_HAOMA_RIGHT_SIDE("vat_invoice_haoma_right_side","invoiceRightInvoiceNumber"),

    CCINT_KEY_VAT_INVOICE_PAGE_NUMBER("vat_invoice_page_number","抵扣联/发票联"),

    CCINT_KEY_VAT_INVOICE_TYPE("vat_invoice_type","发票类型"),

    CCINT_KEY_VAT_INVOICE_TOTAL_NOTE("vat_invoice_total_note","remark"),

    CCINT_KEY_VAT_INVOICE_CIPHER_FIELD("vat_invoice_cipher_field","password1"),

    CCINT_KEY_VAT_INVOICE_DRAWER("vat_invoice_drawer","issuer"),

    CCINT_KEY_VAT_INVOICE_REVIEW("vat_invoice_review","checker"),

    CCINT_KEY_VAT_INVOICE_PAYEE("vat_invoice_payee","payee"),

    CCINT_KEY_EXIST_STAMPLE("exist_stample","existSeal"),


    //OCR-item
    CCINT_KEY_ITEM_VAT_INVOICE_GOODS("vat_invoice_goods", "item_commodityName"),//货物或服务名称
    CCINT_KEY_ITEM_VAT_INVOICE_PLATE_SPECIFIC("vat_invoice_plate_specific", "item_standard"),//规格型号
    CCINT_KEY_ITEM_VAT_INVOICE_ELECTRANS_UNIT("vat_invoice_electrans_unit", "item_unit"),//单位明细
    CCINT_KEY_ITEM_VAT_INVOICE_ELECTRANS_QUANTITY("vat_invoice_electrans_quantity", "item_detailsCount"),//数量明细  //
    CCINT_KEY_ITEM_VAT_INVOICE_ELECTRANS_UNIT_PRICE("vat_invoice_electrans_unit_price", "item_price"),//单价明细
    CCINT_KEY_ITEM_VAT_INVOICE_PRICE("vat_invoice_price", "item_detailAmount"),//金额明细
    CCINT_KEY_ITEM_VAT_INVOICE_TAX_RATE("vat_invoice_tax_rate", "item_taxRate"),//税率明细
    CCINT_KEY_ITEM_VAT_INVOICE_TAX("vat_invoice_tax", "item_tax"),//税额明细


    //    CCINT_KEY_
    CCINT_KEY_VAT_INVOICE_CREDIT_MEMO("vat_invoice_credit_memo","vatRedDashed"),

    CCINT_KEY_VAT_INVOICE_ISSUE_DATE_PRINT("vat_invoice_issue_date_print","vatRightInvoiceDate"),

    CCINT_KEY_VAT_INVOICE_ELEC_PAYMENT_ID("vat_invoice_elec_payment_id","vatElePayId"),

    CCINT_KEY_VAT_INVOICE_DAIMA_PRINT("vat_invoice_daima_print","vatPrintInvoiceCode"),

    CCINT_KEY_VAT_INVOICE_NOTE_CORRECT_CODE("vat_invoice_note_correct_code","invoiceNoteCheckCode"),

    CCINT_KEY_VAT_INVOICE_NOTE_HANDWRITE("vat_invoice_note_handwrite","noteHandWrite"),

    /**
     * type 为 motor_vehicle_sale_invoice（机动车销售统一发票）, KEY 返回的类
     * 型
     */

    CCINT_KEY_VEHICLE_INVOICE_BUYER("vehicle_invoice_buyer","motorBuyer"),

    CCINT_KEY_VEHICLE_INVOICE_BUYER_ID("vehicle_invoice_buyer_id","motorBuyerId"),

    CCINT_KEY_VEHICLE_INVOICE_CAR_MODEL("vehicle_invoice_car_model","vehicleType"),

    CCINT_KEY_VEHICLE_INVOICE_CAR_MADE_PLACE("vehicle_invoice_car_made_place","produceArea"),

    CCINT_KEY_VEHICLE_INVOICE_CERT_ID("vehicle_invoice_cert_id","certificateNumber"),

    CCINT_KEY_VEHICLE_INVOICE_ENGINE_ID("vehicle_invoice_engine_id","carEngineCode"),

    CCINT_KEY_VEHICLE_INVOICE_CAR_VIN("vehicle_invoice_car_vin","carCode"),

    CCINT_KEY_VEHICLE_INVOICE_TOTAL_PRICE("vehicle_invoice_total_price","价税合计"),

    CCINT_KEY_VEHICLE_INVOICE_TOTAL_PRICE_DIGITS("vehicle_invoice_total_price_digits","motorTotal"),

    CCINT_KEY_VEHICLE_INVOICE_PRICE_WITHOUT_TAX("vehicle_invoice_price_without_tax","motorPretaxAmount"),

    CCINT_KEY_VEHICLE_INVOICE_TAX_RATE("vehicle_invoice_tax_rate","motorTaxRate"),

    CCINT_KEY_VEHICLE_INVOICE_TAX_AMOUNT("vehicle_invoice_tax_amount","motorTax"),

    CCINT_KEY_VEHICLE_INVOICE_TELEPHONE("vehicle_invoice_telephone","motorSellerPhone"),

    CCINT_KEY_VEHICLE_INVOICE_ISSUE_DATE("vehicle_invoice_issue_date","motorDate"),

    CCINT_KEY_VEHICLE_INVOICE_DAIMA("vehicle_invoice_daima","motorCode"),

    CCINT_KEY_VEHICLE_INVOICE_HAOMA("vehicle_invoice_haoma","motorNumber"),

    CCINT_KEY_VEHICLE_INVOICE_DEALER("vehicle_invoice_dealer","motorSeller"),

    CCINT_KEY_VEHICLE_INVOICE_JIDA_DAIMA("vehicle_invoice_jida_daima","motorMachineCode"),

    CCINT_KEY_VEHICLE_INVOICE_JIDA_HAOMA("vehicle_invoice_jida_haoma","motorMachineNumber"),

    CCINT_KEY_VEHICLE_INVOICE_MACHINE_ID("vehicle_invoice_machine_id","vehicleInvoiceMachineId"),

    CCINT_KEY_VEHICLE_INVOICE_TAX_AUTHORITH_ID("vehicle_invoice_tax_authorith_id","taxAuthoritiesCode"),

    CCINT_KEY_VEHICLE_INVOICE_TAX_AUTHORITH("vehicle_invoice_tax_authorith","motorTaxAuthorities"),

    CCINT_KEY_VEHICLE_INVOICE_SELLER_BANK_NAME("vehicle_invoice_seller_bank_name","motorSellerBankName"),

    CCINT_KEY_VEHICLE_INVOICE_SELLER_BANK_ACCOUNT("vehicle_invoice_seller_bank_account","motorSellerBankAccount"),

    CCINT_KEY_VEHICLE_INVOICE_SELLER_TAX_ID("vehicle_invoice_seller_tax_id","motorSellerTaxId"),

    CCINT_KEY_VEHICLE_INVOICE_COMMODITY_INSPECTION_ID("vehicle_invoice_commodity_inspection_id","commodityInspectionNo"),

    CCINT_KEY_VEHICLE_INVOICE_IMPORT_CERTIFICATE_ID("vehicle_invoice_import_certificate_id","certificateOfImport"),

    CCINT_KEY_VEHICLE_INVOICE_SELLER_ADDRESS("vehicle_invoice_seller_address","motorSellerAddress"),

    CCINT_KEY_EXIST_INVOICE_SEAL("exist_invoice_seal","existInvoiceStamp"),

    CCINT_KEY_VEHICLE_INVOICE_TAX_CODE("vehicle_invoice_tax_code","motorTaxCode"),
    CCINT_KEY_VEHICLE_INVOICE_QR_CODE("vehicle_invoice_qr_code","motorQrCode"),
    CCINT_KEY_VEHICLE_INVOICE_PAGE_NUMBER("vehicle_invoice_page_number","invoiceCouplet"),

    /**
     *type 为 used_car_purchase_invoice（二手车销售统一发票）, KEY 返回的类
     * 型
     */

    //CCINT_KEY_VEHICLE_INVOICE_DAIMA("vehicle_invoice_daima","购车发票代码"),
    //CCINT_KEY_VEHICLE_INVOICE_HAOMA("vehicle_invoice_haoma","购车发票号码"),
    //CCINT_KEY_VEHICLE_INVOICE_BUYER("vehicle_invoice_buyer","购货单位(人)"),
    //CCINT_KEY_VEHICLE_INVOICE_BUYER_ID("vehicle_invoice_buyer_id","购买身份证号码/组织机构代码"),
    CCINT_KEY_VEHICLE_INVOICE_BUYER_ADDR("vehicle_invoice_buyer_addr","buyerUnitOrIndividualAddress"),
    CCINT_KEY_VEHICLE_INVOICE_BUYER_PHONE("vehicle_invoice_buyer_phone","buyerPhone"),
    CCINT_KEY_VEHICLE_INVOICE_SELLER("vehicle_invoice_seller","usedSeller"),
    CCINT_KEY_VEHICLE_INVOICE_SELLER_ID("vehicle_invoice_seller_id","usedSellerId"),
    CCINT_KEY_VEHICLE_INVOICE_SELLER_ADDR("vehicle_invoice_seller_addr","sellerUnitOrIndividualAddress"),
    CCINT_KEY_VEHICLE_INVOICE_SELLER_PHONE("vehicle_invoice_seller_phone","sellerPhone"),
    CCINT_KEY_VEHICLE_INVOICE_PLATE_NUM("vehicle_invoice_plate_num","usedLicensePlate"),
    CCINT_KEY_VEHICLE_INVOICE_REGISTER_NUM("vehicle_invoice_register_num","registrationNumber"),
    CCINT_KEY_VEHICLE_INVOICE_VEHICLE_TYPE("vehicle_invoice_vehicle_type","usedVehicleType"),
    CCINT_KEY_VEHICLE_INVOICE_VIN("vehicle_invoice_vin","usedCarCode"),
    //CCINT_KEY_VEHICLE_INVOICE_CAR_MODEL("vehicle_invoice_car_model","厂牌型号"),
    CCINT_KEY_VEHICLE_INVOICE_DMV("vehicle_invoice_dmv","inVehicleManageName"),
    CCINT_KEY_VEHICLE_INVOICE_PRICE_TOTAL_PRICE("vehicle_invoice_price_total_price","usedUppercase"),
    //CCINT_KEY_VEHICLE_INVOICE_TOTAL_PRICE_DIGITS("vehicle_invoice_total_price_digits","价税合计小写"),
    CCINT_KEY_VEHICLE_INVOICE_AUCTION_HOUSE("vehicle_invoice_auction_house","businessUnit"),
    CCINT_KEY_VEHICLE_INVOICE_AUCTION_HOUSE_ADDR("vehicle_invoice_auction_house_addr","busmessUnitAddress"),
    CCINT_KEY_VEHICLE_INVOICE_AUCTION_HOUSE_TAX_ID("vehicle_invoice_auction_house_tax_id","businessUnitTaxNo"),
    CCINT_KEY_VEHICLE_INVOICE_AUCTION_HOUSE_BANK_ACCOUNT("vehicle_invoice_auction_house_bank_account","busmessUnitBankAndAccount"),
    CCINT_KEY_VEHICLE_INVOICE_AUCTION_HOUSE_PHONE("vehicle_invoice_auction_house_phone","busmessUnitPhone"),
    CCINT_KEY_VEHICLE_INVOICE_MARKET("vehicle_invoice_market","companyName"),
    CCINT_KEY_VEHICLE_INVOICE_MARKET_ADDR("vehicle_invoice_market_addr","lemonNlarketAddress"),
    CCINT_KEY_VEHICLE_INVOICE_MARKET_TAX_ID("vehicle_invoice_market_tax_id","companyTaxId"),
    CCINT_KEY_VEHICLE_INVOICE_MARKET_BANK_ACOUNT("vehicle_invoice_market_bank_acount","lemonMarketBankAndAccount"),
    CCINT_KEY_VEHICLE_INVOICE_MARKET_PHONE("vehicle_invoice_market_phone","lemonMarketPhone"),
    CCINT_KEY_VEHICLE_INVOICE_NOTE("vehicle_invoice_note","备注"),
    //CCINT_KEY_VEHICLE_INVOICE_ISSUE_DATE("vehicle_invoice_issue_date","购车发票开票日期"),
    CCINT_KEY_VEHICLE_INVOICE_SHUIKONGMA("vehicle_invoice_shuikongma","usedTaxCode"),
    /**
     * type 为 vat_roll_invoice（卷票）, KEY 返回的类型列
     */
    CCINT_KEY_TICKET_INVOICE_HEAD("invoice_head","invoiceHeadJuan"),
    CCINT_KEY_TICKET_INVOICE_CODE("invoice_code","invoiceCodeJuan"),
    CCINT_KEY_TICKET_INVOICE_NUMBER("invoice_number","invoiceNumberJuan"),
    CCINT_KEY_TICKET_SOLD_NAME("sold_name","soldNameJuan"),
    CCINT_KEY_TICKET_SOLD_TAX_NUMBER("sold_tax_number","soldTaxNumberJuan"),
    CCINT_KEY_TICKET_DATE_OF_INVOICE("date_of_invoice","dateOfInvoiceJuan"),
    CCINT_KEY_TICKET_CASHIER_NAME("cashier_name","cashierNameJuan"),
    CCINT_KEY_TICKET_BUY_NAME("buy_name","buyNameJuan"),
    CCINT_KEY_TICKET_BUY_TAX_NUMBER("buy_tax_number","buyTaxNumberJuan"),
    CCINT_KEY_TICKET_GOODS_NAME_LIST("goods_name_list","goodsNameListJuan"),

    CCINT_KEY_TICKET_GOODS_UNIT_PRICE_LIST("goods_unit_price_list","goodsUnitPriceList"),
    CCINT_KEY_TICKET_GOODS_NUMBER_LIST("goods_number_list","goodsNumberListJuan"),
    CCINT_KEY_TICKET_GOODS_MONEY_LIST("goods_money_list","goodsMoneyListJuan"),
    CCINT_KEY_TICKET_TOTAL_MONEY("total_money","totalMoneyJuan"),
    CCINT_KEY_TICKET_TOTAL_MONEY_BIG("total_money_big","totalMoneyBigJuan"),
    CCINT_KEY_TICKET_CHECK_NUMBER("check_number","checkNumberJuan"),
    CCINT_KEY_TICKET_EXIST_INVOICE_SEAL("exist_invoice_seal","existInvoiceSealJuan"),
    CCINT_KEY_TICKET_VAT_INVOICE_SELLER_ID_INVOICE_SEAL("vat_invoice_seller_id_invoice_seal","vatInvoiceSellerIdInvoiceSealJuan"),

    //type （承税汇票）
    CCINT_KEY_CHU_NAME("chu_name","chuName"),
    CCINT_KEY_ZMONEY("money","money"),
    CCINT_KEY_SHOU_NAME("shou_name","shouName"),

    /**
     * type 为 vat_roll_invoice（增值税普通发票（卷票））, KEY 返回的类型列
     */
    CCINT_KEY_INVOICE_HEAD("invoice_head","发票票头"),
    CCINT_KEY_INVOICE_NUMBER("invoice_number","number"),
    CCINT_KEY_INVOICE_CODE("invoice_code","code"),
    CCINT_KEY_MACHINE_NUMBER("machine_number","machineNumber"),
    CCINT_KEY_MACHINE_CODE("machine_code","machineCoding"),
    CCINT_KEY_TOTAL_MONEY("total_money","totalSumSmall"),
    CCINT_KEY_TOTAL_MONEY_BIG("total_money_big","totalSumLarge"),
    CCINT_KEY_DATE_OF_INVOICE("date_of_invoice","date"),
    CCINT_KEY_CHECK_NUMBER("check_number","checkCode"),
    CCINT_KEY_BUY_NAME("buy_name","buyer"),
    CCINT_KEY_BUY_TAX_NUMBER("buy_tax_number","buyerTaxId"),
    CCINT_KEY_SOLD_NAME("sold_name","seller"),
    CCINT_KEY_SOLD_TAX_NUMBER("sold_tax_number","sellerTaxId"),
    CCINT_KEY_GOODS_NAME_LIST("goods_name_list","rollItemNames"),
    CCINT_KEY_GOODS_UNIT_PRICE_LIST("goods_unit_price_list","unitPrice"),
    CCINT_KEY_GOODS_NUMBER_LIST("goods_number_list","countNumber"),
    CCINT_KEY_GOODS_MONEY_LIST("goods_money_list","rollTotal"),

    /**
     * type 为 vehicle_toll（过路过桥费发票、汽车通行费）, KEY 返回的类型列
     */
    CCINT_KEY_TOLL_CODE("toll_code","tollCode"),
    CCINT_KEY_TOLL_NUMBER("toll_number","tollNumber"),
    CCINT_KEY_DATE("date","allDate"),
    CCINT_KEY_TIME("time","allTime"),
    CCINT_KEY_MONEY("money","allTotal"),

    /**
     * type 为 quota_invoice（通用定额发票）, KEY 返回的类型列
     */
    CCINT_KEY_QUOTA_INVOICE_CODE("quota_invoice_code","quotaCode"),
    CCINT_KEY_QUOTA_INVOICE_NUMBER("quota_invoice_number","quotaNumber"),
    CCINT_KEY_MONEY_SMALL("money_small","quotaTotal"),
    CCINT_KEY_MONEY_BIG("money_big","金额(大写)"),
    CCINT_KEY_LOCATION("location","province"),

    /**
     * type 为 taxi_ticket(出租车发票), KEY 返回的类型列
     */
    //CCINT_KEY_INVOICE_CODE("invoice_code","发票代码"),
    CCINT_KEY_INVOICE_NO("invoice_no","taxNumber"),
    //CCINT_KEY_DATE("date","日期"),
    CCINT_KEY_TAXI_NO("taxi_no","licensePlate"),
    CCINT_KEY_BOARDING_TIME("boarding_time","timeGetOn"),
    CCINT_KEY_LANDING_TIME("landing_time","timeGetOff"),
    CCINT_KEY_MILEAGE("mileage","mileage"),
    CCINT_KEY_SUM("sum","taxTotal"),
    //CCINT_KEY_LOCATION("location","发票所在地"),
    CCINT_KEY_OIL("oil","taxiOil"),
    /**
     * type 为 didi_itinerary（行程单）, KEY 返回的类型列
     */
    CCINT_KEY_APPLY_DATE("apply_date","applyDate"),
    CCINT_KEY_START_DATE("start_date","startDate"),
    CCINT_KEY_END_DATE("end_date","endDate"),
    CCINT_PHONE_NUMBER("phone_number","phoneNumber"),
    CCINT_TOTAL_MONEY("total_money","totalMoney"),

    /**
     * type 为 air_transport（行程单）, KEY 返回的类型列
     */
    CCINT_KEY_PASSENGER_NAME("passenger_name","userName"),
    CCINT_KEY_ID_NO("id_no","userId"),
    CCINT_KEY_SERIAL_NUMBER("serial_number","印刷序号"),
    CCINT_KEY_TOTAL("total","total"),
    CCINT_KEY_FARE("fare","fare"),
    CCINT_KEY_CIVIL_AVIATION_FUND("civil_aviation_fund","caacDevelopmentFund"),
    CCINT_KEY_FUEL_SURCHARGE("fuel_surcharge","fuelSurcharge"),
    CCINT_KEY_OTHER_TAXES("other_taxes","flightTax"),
    CCINT_KEY_ISSUED_DATE("issued_date","flightDate"),
    CCINT_KEY_ISSUED_BY("issued_by","issueBy"),
    CCINT_KEY_E_TICKET_NO("e_ticket_no","flightNumber"),
    CCINT_KEY_INSURANCE("insurance","insurance"),

    CCINT_KEY_FROM("from","from"),
    CCINT_KEY_TO("to","to"),
    CCINT_KEY_FLIGHT_NUMBER("flight_number","flightsNumber"),
    CCINT_KEY_SEAT_CLASS("seat_class","flightsSeat"),
    //CCINT_KEY_DATE("date","航班日期"),
    //CCINT_KEY_TIME("time","航班时间"),
    CCINT_KEY_FARE_BASIS("fare_basis","客票级别"),
    CCINT_KEY_ALLOW("allow","行李"),


    /**
     * type 为 train_ticket（火车票）, KEY 返回的类型列
     */
    //CCINT_KEY_PASSENGER_NAME("passenger_name","乘客名称"),
    CCINT_KEY_PASSENGER_ID("passenger_id","railUserId"),
    CCINT_KEY_TRAIN_NUMBER("train_number","trainNumber"),
    CCINT_KEY_DEPARTURE_STATION("departure_station","stationGetOn"),
    CCINT_KEY_DEPARTURE_DATE("departure_date","railDate"),
    CCINT_KEY_CLASS("class","railSeat"),
    CCINT_KEY_TICKET_NUMBER("ticket_number","railNumber"),
    CCINT_KEY_ARRIVAL_STATION("arrival_station","stationGetOff"),
    CCINT_KEY_SEAT_NUMBER("seat_number","seat"),
    CCINT_KEY_PRICE("price","railTotal"),
    CCINT_KEY_CHECK("check","railCheck"),
    CCINT_KEY_TICKET_ID("ticket_id","serialNumber");

    /**
     * type 为 general_machine_invoice（通用机打发票）、
     * highway_passenger_invoice（公路客运发票）、shipping_invoice（船运客
     * 票）、passenger_transport_invoice（旅客运输普票）, KEY 返回的类型列
     */
    //CCINT_KEY_INVOICE_CODE("invoice_code","发票代码"),
    //CCINT_KEY_INVOICE_NUMBER("invoice_number","发票号码"),
    //CCINT_KEY_MONEY("money","金额"),
    //CCINT_KEY_DATE("date","日期"),
    //CCINT_KEY_TIME("time","时间"),

    /**
     * type 为 parking_invoice（停车费发票）, KEY 返回的类型列
     */
    //CCINT_KEY_INVOICE_NUMBER("invoice_number","发票号码"),
    //CCINT_KEY_MONEY("money","金额"),






    private final String keyCode;
    private final String keyValue;

    CcintKeyValueEnum(String keyCode, String keyValue) {
        this.keyCode = keyCode;
        this.keyValue = keyValue;
    }

    public String getKeyCode() {
        return keyCode;
    }

    public String getKeyValue() {
        return keyValue;
    }

    /**
     * 得到name
     */

    public static String getContent(String typeCode) {
        for (CcintKeyValueEnum controlTypeEnum : values()) {
            if (controlTypeEnum.keyCode.equals(typeCode)) {
                return controlTypeEnum.getKeyValue();
            }
        }
        return null;
    }


    /**
     * 得到type
     */

    public static String getCode(String typeName) {
        for (CcintKeyValueEnum controlTypeEnum : values()) {
            if (controlTypeEnum.keyValue.equals(typeName.trim())) {
                return controlTypeEnum.getKeyCode();
            }
        }
        return null;
    }
}
