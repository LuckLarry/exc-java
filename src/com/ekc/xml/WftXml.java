package com.ekc.xml;

import com.ekc.util.ItemHelper;

/**
 * <pre>
 * 网付通参数
 * 若在接口开发程序中有疑问的,可直接致电到银联技术部蔡越锐，020-85635321 或 陆苇020-85545314
 * </pre>
 * 
 * @author pengbei_qq1179009513
 * 
 */
public class WftXml {
	/**
	 * PKey 交易密钥
	 */
	public final static String PKey = "30818902818100D5C828ABC3B360C687EE8DC3CDF209D24EA8E72473D617705DF17DF80100C9D59F55FE605A5F6802A493BA87811D468909B266FC0506A85BBBDEC36E0194040D578AF7FB575AFB6DCACF765D6B89AD0DFC443B0E6578E6E91A306A2C8FA67340956F614EBEECAE0418346CDE8EA4D7FE4DC7388C43B6940BB6B17DD8A7C20EE90203010001";
	/**
	 * MerId 商户id
	 */
	public final static String MerId = "DNZ";
	/**
	 * UserId 用户账号
	 */
	public final static String UserId = "T10635";
	/**
	 * Pwd 用户密码
	 */
	public final static String Pwd = "N9P4UA";
	/**
	 * UserName 用户名称
	 */
	public final static String UserName = "T10635";
	/**
	 * shopId 商户编号
	 */
	public final static String shopId = "898440650210037";
	/**
	 * 提交支付 生产环境form跳转链接
	 */
	public final static String action = "https://www.gnete.com/bin/scripts/OpenVendor/gnete/V34/GetOvOrder.asp";
	// 根证书的相对路径
	private final static String gente_ = "WEB-INF\\cert\\wft\\GNETE.cer";
	// 发送方证书路径(商户证书-私钥)
	private final static String dnz_LouLan = "WEB-INF\\cert\\wft\\DNZ_LOULAN.pfx";
	//银联网络服务器证书JKS
	private final static String jks="WEB-INF\\cert\\wft\\www.gnetpg.com.jks"; 
	//JKS密码
	public final static String JKSPwd="111111";
	/**
	 * 发送方证书密码
	 */
	public final static String SendCertPWD = "123456";
	/**
	 * 根证书路径
	 * 
	 * @return 根证书的绝对路径
	 */
	public static String getGNETE(){
		return getGNETE(ItemHelper.getContextPath());
	}
	/**
	 * 根证书路径
	 * 
	 * @param path
	 *            项目路径
	 * @return 根证书的绝对路径
	 */
	public static String getGNETE(String path) {
		return path + gente_;
	}
	/**
	 * 发送方证书路径(商户证书-私钥) 绝对路径
	 * @return 发送方证书路径(商户证书-私钥) 绝对路径
	 */
	public static String getDnz_LouLan(){
		return getDnz_LouLan(ItemHelper.getContextPath());
	}
	/**
	 * 发送方证书路径(商户证书-私钥) 绝对路径
	 * @param path 项目路径
	 * @return 发送方证书路径(商户证书-私钥) 绝对路径
	 */
	public static String getDnz_LouLan(String path){
		return path + dnz_LouLan;
	}
	/**
	 * 银联网络服务器证书JKS
	 * @return
	 */
	public static String getJks(){
		return getJks(ItemHelper.getContextPath());
	}
	/**
	 * 银联网络服务器证书JKS
	 * @param path  项目路径
	 * @return
	 */
	public static String getJks(String path){
		return path+jks;
	}
}
