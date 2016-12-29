package com.ekc.xml;


/**
 * 
 * @author Zheng Xiajun
 * 
 */
public class CftXml {
	/**
	 * key 交易密钥
	 */
	public final static String key = "c15c118bcedfec1aa64e00f1384ded4f";
	/**
	 * 商户id
	 */
	public final static String partner = "1246508401";
	/**
	 * 交易完成后跳转的URL
	 */
	public final static String return_url = "pay/cft/payReturnUrl.jsp";
	/**
	 * 接收财付通通知的URL
	 */
	public final static String notify_url = "pay/cft/payNotifyUrl.jsp";
	
	
	/**
	 * 发送方证书路径(商户证书-私钥) 绝对路径
	 * @return 发送方证书路径(商户证书-私钥) 绝对路径
	 
	public static String getDnz_LouLan(){
		return getDnz_LouLan(ItemHelper.getContextPath());
	}
	/**
	 * 发送方证书路径(商户证书-私钥) 绝对路径
	 * @param path 项目路径
	 * @return 发送方证书路径(商户证书-私钥) 绝对路径
	 */
	/**
	public static String getDnz_LouLan(String path){
		return path + dnz_LouLan;
	}
	/**
	 * 银联网络服务器证书JKS
	 * @return
	
	public static String getJks(){
		return getJks(ItemHelper.getContextPath());
	}
	/**
	 * 银联网络服务器证书JKS
	 * @param path  项目路径
	 * @return
	
	public static String getJks(String path){
		return path+jks;
	}
	 */
}
