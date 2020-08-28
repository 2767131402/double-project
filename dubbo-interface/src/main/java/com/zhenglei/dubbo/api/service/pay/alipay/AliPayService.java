package com.zhenglei.dubbo.api.service.pay.alipay;

import com.alipay.api.AlipayApiException;
import com.zhenglei.dubbo.api.domain.pay.alipay.PCAlipayBean;

/*支付服务*/
public interface AliPayService {
    /*支付宝*/
    String aliPay(PCAlipayBean alipayBean) throws AlipayApiException;
}