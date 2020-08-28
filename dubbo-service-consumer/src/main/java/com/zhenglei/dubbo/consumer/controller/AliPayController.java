package com.zhenglei.dubbo.consumer.controller;

import com.alipay.api.AlipayApiException;
import com.zhenglei.dubbo.api.domain.pay.alipay.PCAlipayBean;
import com.zhenglei.dubbo.consumer.service.pay.alipay.PayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/alipay")
public class AliPayController {

    @Autowired
    private PayService payService;

    /*阿里支付*/
    @ResponseBody
    @PostMapping(value = "alipay")
    public String alipay(String out_trade_no, String subject, String total_amount, String body) throws AlipayApiException {
        PCAlipayBean alipayBean = new PCAlipayBean();
        alipayBean.setBody(body);
        alipayBean.setOutTradeNo(out_trade_no);
        alipayBean.setTotalAmount(new StringBuffer().append(total_amount));
        alipayBean.setSubject(subject);
        return payService.aliPay(alipayBean);
    }
    @GetMapping(value = "index")
    public String index()  {
        return "/index";
    }

}
