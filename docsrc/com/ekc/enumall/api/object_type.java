package com.ekc.enumall.api;

import java.util.HashMap;
import java.util.Map;

import com.ekc.xml.MessageXml;

/**
 * 所属对象
 * 
 * 所属对象: 0: goods表 ;1：sku表
 * 
 * @author ZhengXiajun
 * 
 */
public enum object_type {

	/**
	 * goods表
	 * 
	 * <pre>
	 * {
	 * 	"messageTxt": " goods表",
	 * 	"code": 0
	 * }
	 * </pre>
	 */
	ZERO(0, " goods表"),
	/**
	 * sku表
	 * 
	 * <pre>
	 * {
	 * 	"messageTxt": "sku表",
	 * 	"code": 1
	 * }
	 * </pre>
	 */
	ONE(1, "sku表");

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

	private object_type() {
	}

	private object_type(int no, String zh) {
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
