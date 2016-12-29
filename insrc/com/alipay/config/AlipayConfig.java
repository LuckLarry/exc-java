package com.alipay.config;

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

	// ↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
	// 合作身份者ID，以2088开头由16位纯数字组成的字符串
	public static String partner = "2088901375836336";
	// 收款支付宝账号，以2088开头由16位纯数字组成的字符串，一般情况下收款账号就是签约账号
	public static String seller_id = partner;
	// 收款支付宝账号，一般情况下收款账号就是签约账号
	public static String seller_email = "lolahome@lolahome.cn";
	// 查询安全校验码
	public static String key = "jjtwcjf26mhbz83f3dlcqopak2kun7bt";
	
	// 商户的私钥
	public static String private_key = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBANDcTmNMciATq/dTx82LPI+NaEr1dWl1HpSYO13K6sOS7WdhW1EGMQeC/KVWJkJBZvGa/j7AXVGGRm2qsfywT97OivCbkXMs2Ofx3BXKNC3P8nmBKaGuPecW9Gn0agRqv5cUjBQPOcTwS7tPdZ/N4b/QErG5rcL6eQ+7aUStCHf3AgMBAAECgYBURyGGZhmDIgz5LyBnq3CTiH72CFgJZs4hLdB+x1u18TrExX9AkZwFKPDjkoC7qYJtqZpAHgVJ2UHgDMnnagm/5qk9zUMRCc/BunkE2goYWHnGqgmm9gCxcK3HSQ9Eo/PvYBBOYaHGlkW/oypk02OA7wOl6/ozxN/XNi5A5wzC6QJBAO6sKywGQYctLPTYJnsx5hWx8DwT79agZ+UDsSxPkwEZgQ7Z2FiIzQnV8IvDAm2we9eZVD5CVVCKsJTzHvvCZVUCQQDgBhHH3COclTVbX5Of1OdzI2CVq+0IL3vVFUkIKcyUyf9UzGUvdB/e1COGhsqV7PAJMkDIfBJRchzC6anY6KgbAkEAnjuEr75CCVV4/aaj9H8DdJSwmZP6KQfVeW52MtNauZroBimFFkUz8nZk8LPL017y6+AaiuA9YZVhNo7slqqttQJANHM3VoPEErUcePg3qARA7EQsKN7H8FKtu4XwQ2kF7RAeJ8xzJFkb3R/t8WE9rTx8Nqr9X/MslsUQ1tG+YnqWYQJAOl7Fyzj9Kb5PUpY7xR8wiaU6zajtCIlrIjCno7vZl/4NMa3i7iImc2zP/okgNqhE4ju0UxJmhx/7vgUbKIKjAg==";
	
	// 支付宝的公钥，无需修改该值
	public static String ali_public_key  = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";
	// ↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

	// 调试用，创建TXT日志文件夹路径
	public static String log_path = "D:\\";

	// 字符编码格式 目前支持 gbk 或 utf-8
	public static String input_charset = "utf-8";

	// 签名方式 不需修改
	public static String sign_type = "MD5";//"RSA";//

}