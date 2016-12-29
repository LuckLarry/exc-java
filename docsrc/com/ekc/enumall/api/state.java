package com.ekc.enumall.api;

import java.util.HashMap;
import java.util.Map;

import com.ekc.xml.MessageXml;

/**
 *状态
 * 
 *状态: 0：等待 ， 1：通过  ， -1：驳回
 * 
 * @author ZhengXiajun
 * 
 */
public enum  state {

	/**
	 * 等侍
	 * 
	 * <pre>
	 * {
	 * 	"messageTxt": "等侍",
	 * 	"code":0
	 * }
	 * </pre>
	 */
	
	ZERO(0, "等侍"),
	/**
	 * 通过
	 * 
	 * <pre>
	 * {
	 * 	"messageTxt": " 通过",
	 * 	"code": 1
	 * }
	 * </pre>
	 */
	ONE(1," 通过"),

	/**
	 *驳回
	 * 
	 * <pre>
	 * {
	 * 	"messageTxt": "驳回",
	 * 	"code":-1
	 * }
	 * </pre>
	 */
	MINUS_ONE(-1,"驳回");
	

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

	private  state() {
	}

	private  state(int no, String zh) {
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



