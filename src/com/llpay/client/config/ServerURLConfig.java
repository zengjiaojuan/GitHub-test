package com.llpay.client.config;

/**
* 支付配置信息
* @author guoyx e-mail:guoyx@lianlian.com
* @date:2013-6-25 下午01:45:06
* @version :1.0
*
*/
public interface ServerURLConfig {
    String PAY_URL = "https://yintong.com.cn/payment/authpay.htm"; // 连连支付WEB收银台认证支付服务地址
    String QUERY_USER_BANKCARD_URL = "https://yintong.com.cn/traderapi/userbankcard.htm"; // 用户已绑定银行卡列表查询
    String QUERY_BANKCARD_URL = "https://yintong.com.cn/traderapi/bankcardquery.htm"; //银行卡卡bin信息查询
    String UNBIND_BANKCARD_URL = "https://yintong.com.cn/traderapi/bankcardunbind.htm"; //银行卡解约
    String WITHDRAW_URL = "https://yintong.com.cn/traderapi/cardandpay.htm"; //提现
    String WITHDRAW_NOTIFY_URL = "http://101.200.82.182:7001/puhuibao/userAccount/withdrawNotify.shtml"; // 提现回调
    String QUERY_CNAPS_URL = "https://yintong.com.cn/traderapi/CNAPSCodeQuery.htm"; // 行号
    String REFUND_URL = "https://yintong.com.cn/traderapi/refund.htm"; // 退款
    String REFUND_NOTIFY_URL = "http://101.200.82.182:7001/puhuibao/userAccount/refundNotify.shtml"; // 退款回调
}
