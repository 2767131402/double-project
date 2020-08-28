package com.zhenglei.dubbo.consumer.service.pay.alipay;

import com.alipay.api.AlipayApiException;
import com.zhenglei.dubbo.api.domain.pay.alipay.PCAlipayBean;
import com.zhenglei.dubbo.api.service.pay.alipay.AliPayService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Service
public class PayService {

    @DubboReference
    private AliPayService aliPayService;


    public String aliPay(PCAlipayBean alipayBean) throws AlipayApiException {
        return aliPayService.aliPay(alipayBean);
    }

    public String async(HttpServletRequest request) {
        return aliPayService.notify(request);
    }

    public String checkAlipay(String outTradeNo) {
        return aliPayService.checkAlipay(outTradeNo);
    }
}
