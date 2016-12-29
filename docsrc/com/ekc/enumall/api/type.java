package com.ekc.enumall.api;

import java.util.HashMap;
import java.util.Map;

import com.ekc.xml.MessageXml;

/**
 * 需求类型
 * 
 * 需求类型： 1.工程报价2.特殊加工3.瓷砖大小样4.电子相册5.瓷砖贴图6.家装效果图7.价格表
 * 
 * @author ZhengXiajun
 * 
 */
public enum type {
	
	/**
	 * 工程报价
	 * 
	 * <pre>
	 * {
	 * 	"messageTxt": "工程报价",
	 * 	"code": 1
	 * }
	 * </pre>
	 */
	ONE(1, "工程报价"),
	/**
	 * 特殊加工
	 * 
	 * <pre>
	 * {
	 * 	"messageTxt": "特殊加工",
	 * 	"code": 2
	 * }
	 * </pre>
	 */
	TWO(2,"特殊加工"),
	/**
	 * 瓷砖大小样
	 * 
	 * <pre>
	 * {
	 * 	"messageTxt": "瓷砖大小样",
	 * 	"code": 3
	 * }
	 * </pre>
	 */
	THREE(3,"瓷砖大小样"),
	/**
	 * 电子相册
	 * 
	 * <pre>
	 * {
	 * 	"messageTxt": "电子相册",
	 * 	"code": 4
	 * }
	 * </pre>
	 */
	FOUR(4, "电子相册"),
	/**
	 * 瓷砖贴图
	 * 
	 * <pre>
	 * {
	 * 	"messageTxt": "瓷砖贴图",
	 * 	"code":5
	 * }
	 * </pre>
	 */
	FIVE(5,"瓷砖贴图"),
	/**
	 * 家装效果图
	 * 
	 * <pre>
	 * {
	 * 	"messageTxt": "家装效果图",
	 * 	"code":6
	 * }
	 * </pre>
	 */
	SIX(6,"家装效果图"),
	/**
	 * 价格表
	 * 
	 * <pre>
	 * {
	 * 	"messageTxt": "价格表",
	 * 	"code": 7
	 * }
	 * </pre>
	 */
	SEVEN(7,"价格表");
	

	private int code;
	private String mess_zh;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMess_zh() {
		return mess_zh;
	}

	public void setMess_zh(String mess_zh) {
		this.mess_zh = mess_zh;
	}

	private type() {
	}

	private type(int no, String zh) {
		this.code = no;
		this.mess_zh = zh;
	}

	public String toString() {
		return String.valueOf(this.code);
	}

	public Map<String, Object> getObjMess() {
		return this.theObj(this.code, this.mess_zh);
	}

	public Map<String, Object> getObjMess(Exception e) {
		return this.theObj(this.code, e.getMessage());
	}

	private Map<String, Object> theObj(int code, String messsageTxt) {
		Map<String, Object> mess = new HashMap<String, Object>();
		mess.put(MessageXml.code_key, code);
		mess.put(MessageXml.messageTxt_key, messsageTxt);
		return mess;
	}
}
