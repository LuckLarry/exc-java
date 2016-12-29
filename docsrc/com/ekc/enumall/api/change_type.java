package com.ekc.enumall.api;

import java.util.HashMap;
import java.util.Map;

import com.ekc.xml.MessageXml;

/**
 *操作类型
 * 
 *操作类型,0为充值,1,为提现,2为管理员调节,99为其它类型
 * 
 * @author ZhengXiajun
 * 
 */
public enum  change_type {
	/**
	 *充值
	 * 
	 * <pre>
	 * {
	 * 	"messageTxt": "充值",
	 * 	"code": 0
	 * }
	 * </pre>
	 */
	ZERO(0, "充值"),
	/**
	 * 体现
	 * 
	 * <pre>
	 * {
	 * 	"messageTxt": "体现",
	 * 	"code": 1
	 * }
	 * </pre>
	 */
	ONE(1, "体现"),
	/**
	 *管理员调节
	 * 
	 * <pre>
	 * {
	 * 	"messageTxt": "管理员调节",
	 * 	"code": 0
	 * }
	 * </pre>
	 */
	TWO(2, "管理员调节"),
	/**
	 * 其他类型
	 * 
	 * <pre>
	 * {
	 * 	"messageTxt": "其他类型",
	 * 	"code": 1
	 * }
	 * </pre>
	 */
	NINETYNINE(99, "其他类型");
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

	private change_type(){
	}

	private change_type(int no, String zh) {
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
