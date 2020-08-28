package com.zhenglei.dubbo.api.domain.pay.alipay;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

/*支付实体对象*/
public class PCAlipayBean implements Serializable {
    /*商户订单号，必填*/
    @JSONField(name = "out_trade_no")
    private String outTradeNo;
    /*订单名称，必填*/
    @JSONField(name = "subject")
    private String subject;
    /*付款金额，必填*/
    @JSONField(name = "total_amount")
    private StringBuffer totalAmount;
    /*商品描述，可空*/
    @JSONField(name = "body")
    private String body;
    /*超时时间参数*/
    @JSONField(name = "timeout_express")
    private String timeoutExpress = "10m";
    @JSONField(name = "product_code")
    private String productCode = "FAST_INSTANT_TRADE_PAY";

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public StringBuffer getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(StringBuffer totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTimeoutExpress() {
        return timeoutExpress;
    }

    public void setTimeoutExpress(String timeoutExpress) {
        this.timeoutExpress = timeoutExpress;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }
}