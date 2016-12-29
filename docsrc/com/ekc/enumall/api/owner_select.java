package com.ekc.enumall.api;

import java.util.HashMap;
import java.util.Map;

import com.ekc.xml.MessageXml;

/**
 *查看界面权限
 * 
 * 查看界面权限:    0：无  ,1：有
 * 
 * @author ZhengXiajun
 * 
 */
public enum owner_select {
	/**
	 * 无
	 * 
	 * <pre>
	 * {
	 * 	"messageTxt": "无",
	 * 	"code": 0
	 * }
	 * </pre>
	 */
	ZERO(0, "无"),
	/**
	 * 有
	 * 
	 * <pre>
	 * {
	 * 	"messageTxt": "有",
	 * 	"code": 1
	 * }
	 * </pre>
	 */
	ONE(1, "有");

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

	private owner_select() {
	}

	private owner_select(int no, String zh) {
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
