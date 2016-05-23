package com.phb.puhuibao.common.alipayapi;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *版本：3.3
 *日期：2012-08-10
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
	
 *提示：如何获取安全校验码和合作身份者ID
 *1.用您的签约支付宝账号登录支付宝网站(www.alipay.com)
 *2.点击“商家服务”(https://b.alipay.com/order/myOrder.htm)
 *3.点击“查询合作者身份(PID)”、“查询安全校验码(Key)”

 *安全校验码查看时，输入支付密码后，页面呈灰色的现象，怎么办？
 *解决方法：
 *1、检查浏览器配置，不让浏览器做弹框屏蔽设置
 *2、更换浏览器或电脑，重新登录查询。
 */

public class AlipayConfig {
	
	//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
	// 合作身份者ID，以2088开头由16位纯数字组成的字符串
	   public static String partner = "2088121116691402";
	    // 商户的私钥
	    public static String key = "xxkqz4brjfqrkqhei985jhtoz4p6uads";
	    
	    public static String private_key = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAMeSmJjRZuZPQOLbKNVdstpdCec/oBQOAshwiBZi8PAb2sWsqTkob+8WziPFvFjjQISzekczg3TM2lUr+k45Qd9xn655VW+m5f+baHwtJG/NPhsUCJDWnoV+wfHWUo19OiLfs6zXZR18q9RxeaAHYjwbzzjhiBkGQqUpq1k03c85AgMBAAECgYA5zcYRDSXGs1HR4zRyiE8TXDtQFWeiJ2S69Y0VHtc2VHsLIjVpbsLs9ygByHYDMN8xEGLsWIlMvUDHxpMbMQ5YzLRBsVaITrLcMR0mw3sun4eyrw+LFCiuGomfxVdma76foyai/g6d3waS5x0hcWTJ9v4b/88lwp4aYwr4MjImAQJBAOZhboBsQCurbifvsRUZuceG8YJ/mlTHYOWQvqgDAufpSt7AcbYqYeLBFZkdHhfesn6Ju+l+JQicuHKlDAxzFmECQQDdxBx0Vf+/6b7K+xnmd2IuTi5ONSETENyJjTNNgKmIYbXOGjtc+s7WdxYpJRIZSq/yIJNpVi8zFVYVKzXrITfZAkAF0VoQBivMroSWn5fyN3cYkdDRgdVs5KgOxAv8hC7zmlGYtHVqq0FTj5qzBig3ZBlP1ryXFedrg0GpK3/VZKahAkAc55jF2QeynebOUhg4H8teObZzXx1mfMYh75OGQITYT9QAfw7Jua8H6RGsXzaSsVdpebF9A0y3ncpQjAdFFFlRAkBcEC3R3zsbuRgm3eAy5v54F+gUFZwTT8VPU2zZnzhtW1jyx0rIUoXmqpBwUmPVc+Z3dR7xo7aq6VvwckCxiZVK";

	    public static String SELLER_EMAIL ="mailbox@jltsgroup.com.cn";
	    
	    public static String service ="mobile.securitypay.pay";
	
	   
	// 支付宝的公钥，无需修改该值
	public static String ali_public_key  = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";

	//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
	

	// 调试用，创建TXT日志文件夹路径
	public static String log_path = "D:\\";

	// 字符编码格式 目前支持 gbk 或 utf-8
	public static String input_charset = "utf-8";
	
	// 签名方式 不需修改
	public static String sign_type = "RSA";

}
