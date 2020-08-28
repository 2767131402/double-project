package com.zhenglei.dubbo.provider.utils;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.zhenglei.dubbo.api.domain.pay.alipay.PCAlipayBean;
import com.zhenglei.dubbo.provider.config.PropertiesConfig;

import java.util.Map;

import org.apache.log4j.Logger;

/* 支付宝 */
public class AlipayUtil {

    private static Logger logger = Logger.getLogger(AlipayUtil.class);

    public static String connect(PCAlipayBean alipayBean) throws AlipayApiException {
        //1、获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(
                PropertiesConfig.getKey("gatewayUrl"),//支付宝网关
                PropertiesConfig.getKey("app_id"),//appid
                PropertiesConfig.getKey("merchant_private_key"),//商户私钥
                "json",
                PropertiesConfig.getKey("charset"),//字符编码格式
                PropertiesConfig.getKey("alipay_public_key"),//支付宝公钥
                PropertiesConfig.getKey("sign_type")//签名方式
        );
        //2、设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        //页面跳转同步通知页面路径
//        alipayRequest.setReturnUrl(PropertiesConfig.getKey("return_url"));
        alipayRequest.setReturnUrl("http://localhost:8081/alipay/checkAlipay?outTradeNo="+alipayBean.getOutTradeNo());
        // 服务器异步通知页面路径
        alipayRequest.setNotifyUrl("http://39.98.152.206:8080/test");
//        alipayRequest.setNotifyUrl(PropertiesConfig.getKey("notify_url"));
        //封装参数
        alipayRequest.setBizContent(JSON.toJSONString(alipayBean));

        //3、请求支付宝进行付款，并获取支付结果
        String result = alipayClient.pageExecute(alipayRequest).getBody();
        //返回付款信息
        return result;
    }


    /**
     * 支付宝异步请求逻辑处理
     */
    public static String notify(Map<String, String> conversionParams) {
        logger.info("支付宝异步请求参数：" + conversionParams);
        //签名验证(对支付宝返回的数据验证，确定是支付宝返回的)
        boolean signVerified = false;
        try {
            //调用SDK验证签名
            signVerified = AlipaySignature.rsaCheckV1(
                    conversionParams,
                    PropertiesConfig.getKey("alipay_public_key"),
                    PropertiesConfig.getKey("charset"),
                    PropertiesConfig.getKey("sign_type")
            );

        } catch (AlipayApiException e) {
            logger.info("验签失败 ！" + e);
            e.printStackTrace();
        }

        //对验签进行处理
        if (signVerified) {
            //验签通过
            //获取需要保存的数据
            String appId = conversionParams.get("app_id");//支付宝分配给开发者的应用Id
            String notifyTime = conversionParams.get("notify_time");//通知时间:yyyy-MM-dd HH:mm:ss
            String gmtCreate = conversionParams.get("gmt_create");//交易创建时间:yyyy-MM-dd HH:mm:ss
            String gmtPayment = conversionParams.get("gmt_payment");//交易付款时间
            String gmtRefund = conversionParams.get("gmt_refund");//交易退款时间
            String gmtClose = conversionParams.get("gmt_close");//交易结束时间
            String tradeNo = conversionParams.get("trade_no");//支付宝的交易号
            String outTradeNo = conversionParams.get("out_trade_no");//获取商户之前传给支付宝的订单号（商户系统的唯一订单号）
            String outBizNo = conversionParams.get("out_biz_no");//商户业务号(商户业务ID，主要是退款通知中返回退款申请的流水号)
            String buyerLogonId = conversionParams.get("buyer_logon_id");//买家支付宝账号
            String sellerId = conversionParams.get("seller_id");//卖家支付宝用户号
            String sellerEmail = conversionParams.get("seller_email");//卖家支付宝账号
            String totalAmount = conversionParams.get("total_amount");//订单金额:本次交易支付的订单金额，单位为人民币（元）
            String receiptAmount = conversionParams.get("receipt_amount");//实收金额:商家在交易中实际收到的款项，单位为元
            String invoiceAmount = conversionParams.get("invoice_amount");//开票金额:用户在交易中支付的可开发票的金额
            String buyerPayAmount = conversionParams.get("buyer_pay_amount");//付款金额:用户在交易中支付的金额
            //TRADE_FINISHED:交易结束并不可退款
            //TRADE_SUCCESS: // 交易支付成功
            //TRADE_CLOSED: // 未付款交易超时关闭或支付完成后全额退款
            //WAIT_BUYER_PAY: // 交易创建并等待买家付款
            String tradeStatus = conversionParams.get("trade_status");// 获取交易状态

            //支付宝官方建议校验的值（out_trade_no、total_amount、sellerId、app_id）
            //处理自己的校验逻辑
            //此处省略..........

            return "success";
        } else {  //验签不通过
            logger.info("验签不通过 ！");
            return "fail";
        }
    }

    /**
     * 向支付宝发起订单查询请求
     */
    public static String checkAlipay(String outTradeNo) {
        logger.info("向支付宝发起查询，查询商户订单号为：" + outTradeNo);
        String result = null;
        try {
            //实例化客户端（参数：网关地址、商户appid、商户私钥、格式、编码、支付宝公钥、加密类型）
            AlipayClient alipayClient = new DefaultAlipayClient(
                    PropertiesConfig.getKey("gatewayUrl"),//支付宝网关
                    PropertiesConfig.getKey("app_id"),//appid
                    PropertiesConfig.getKey("merchant_private_key"),//商户私钥
                    "json",
                    PropertiesConfig.getKey("charset"),//字符编码格式
                    PropertiesConfig.getKey("alipay_public_key"),//支付宝公钥
                    PropertiesConfig.getKey("sign_type")//签名方式
            );
            AlipayTradeQueryRequest alipayTradeQueryRequest = new AlipayTradeQueryRequest();
            alipayTradeQueryRequest.setBizContent("{" +
                    "\"out_trade_no\":\"" + outTradeNo + "\"" +
                    "}");
            AlipayTradeQueryResponse alipayTradeQueryResponse = alipayClient.execute(alipayTradeQueryRequest);
            if (alipayTradeQueryResponse.isSuccess()) {
                switch (alipayTradeQueryResponse.getTradeStatus()) // 判断交易结果
                {
                    case "TRADE_FINISHED": // 交易结束并不可退款
                        result = "交易结束并不可退款";
                        break;
                    case "TRADE_SUCCESS": // 交易支付成功
                        result = "交易支付成功";
                        break;
                    case "TRADE_CLOSED": // 未付款交易超时关闭或支付完成后全额退款
                        result = "未付款交易超时关闭或支付完成后全额退款";
                        break;
                    case "WAIT_BUYER_PAY": // 交易创建并等待买家付款
                        result = "交易创建并等待买家付款";
                        break;
                    default:
                        break;
                }
            } else {
                logger.info("调用支付宝查询接口失败！");
            }
        } catch (AlipayApiException e) {
            logger.info("调用支付宝查询接口失败！");
            e.printStackTrace();
        }
        return result;
    }
}