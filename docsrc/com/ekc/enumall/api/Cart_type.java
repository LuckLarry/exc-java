package com.ekc.enumall.api;

import java.util.HashMap;
import java.util.Map;

import com.ekc.xml.MessageXml;

/**
 * 购物车商品类型
 * 
 * 购物车商品类型:0普通;1团购;2拍卖;3夺宝奇兵
 * 
 * @author ZhengXiajun
 * 
 */
public enum  Cart_type {

	/**
	 * 普通
	 * 
	 * <pre>
	 * {
	 * 	"messageTxt": "普通",
	 * 	"code":0
	 * }
	 * </pre>
	 */
	
	ZERO(0, "普通"),
	/**
	 * 团购
	 * 
	 * <pre>
	 * {
	 * 	"messageTxt": " 团购",
	 * 	"code": 1
	 * }
	 * </pre>
	 */
	ONE(1," 团购"),
	/**
	 * 拍卖
	 * 
	 * <pre>
	 * {
	 * 	"messageTxt": " 拍卖",
	 * 	"code": 2
	 * }
	 * </pre>
	 */
	TWO(2," 拍卖"),
	/**
	 *夺宝奇兵
	 * 
	 * <pre>
	 * {
	 * 	"messageTxt": "夺宝奇兵",
	 * 	"code": 3
	 * }
	 * </pre>
	 */
	THREE(3," 夺宝奇兵");
	

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

	private  Cart_type() {
	}

	private  Cart_type(int no, String zh) {
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


