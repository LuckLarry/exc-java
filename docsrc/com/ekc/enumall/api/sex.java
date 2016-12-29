package com.ekc.enumall.api;

import java.util.HashMap;
import java.util.Map;

import com.ekc.xml.MessageXml;

/**
 *性别
 * 
 *性别类型;0 ，保密；1，男； 2，女
 * 
 * @author ZhengXiajun
 * 
 */
public enum sex {
	/**
	 *保密
	 * 
	 * <pre>
	 * {
	 * 	"messageTxt": "保密",
	 * 	"code": 0
	 * }
	 * </pre>
	 */
	ZERO(0, "保密"),
	/**
	 * 男
	 * 
	 * <pre>
	 * {
	 * 	"messageTxt": "男",
	 * 	"code": 1
	 * }
	 * </pre>
	 */
	ONE(1, "男"),
	/**
	 * 女
	 * 
	 * <pre>
	 * {
	 * 	"messageTxt": "女",
	 * 	"code": 2
	 * }
	 * </pre>
	 */
	TWO(2, "女");

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

	private sex() {
	}

	private sex(int no, String zh) {
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
