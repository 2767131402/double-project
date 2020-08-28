package com.zhenglei.dubbo.api.service.pay.alipay;

import com.alipay.api.AlipayApiException;
import com.zhenglei.dubbo.api.domain.pay.alipay.PCAlipayBean;

import javax.servlet.http.HttpServletRequest;

/*支付服务*/
public interface AliPayService {
    /**
     * 支付宝支付接口
     */
    String aliPay(PCAlipayBean alipayBean) throws AlipayApiException;

    /**
     * 支付宝支付成功后.异步处理调用参数
     */
    String notify(HttpServletRequest request);

    /**
     * 向支付宝发起订单查询请求
     */
    String checkAlipay(String outTradeNo);
}