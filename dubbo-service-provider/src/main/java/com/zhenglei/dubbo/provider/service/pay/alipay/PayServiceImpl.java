package com.zhenglei.dubbo.provider.service.pay.alipay;

import com.alipay.api.AlipayApiException;
import com.zhenglei.dubbo.api.domain.pay.alipay.PCAlipayBean;
import com.zhenglei.dubbo.api.service.pay.alipay.AliPayService;
import com.zhenglei.dubbo.provider.utils.AlipayUtil;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

@Service
@DubboService
public class PayServiceImpl implements AliPayService {
    @Override
    public String aliPay(PCAlipayBean alipayBean) throws AlipayApiException {
        return AlipayUtil.connect(alipayBean);
    }
}
