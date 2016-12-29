package com.ekc.enumall.api;

import java.util.HashMap;
import java.util.Map;

import com.ekc.xml.MessageXml;

/**
 * 支付状态
 * 
 * 支付状态：0 未付款;  1 付款中;  2 已付款
 * 
 * @author ZhengXiajun
 * 
 */
public enum pay_status {

	/**
	 * 未付款
	 * 
	 * <pre>
	 * {
	 * 	"messageTxt": "未付款",
	 * 	"code": 0
	 * }
	 * </pre>
	 */
	ZERO(0, "未付款"),
	/**
	 * 付款中
	 * 
	 * <pre>
	 * {
	 * 	"messageTxt": "付款中",
	 * 	"code": 1
	 * }
	 * </pre>
	 */
	ONE(1, "付款中"),
	/**
	 * 已付款
	 * 
	 * <pre>
	 * {
	 * 	"messageTxt": "已付款",
	 * 	"code": 2
	 * }
	 * </pre>
	 */
	TWO(2,"已付款");
	

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

	private pay_status() {
	}

	private pay_status(int no, String zh) {
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
