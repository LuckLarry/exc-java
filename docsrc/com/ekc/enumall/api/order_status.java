package com.ekc.enumall.api;

import java.util.HashMap;
import java.util.Map;

import com.ekc.xml.MessageXml;

/**
 * 订单状态
 * 
 * 订单的状态;0未确认,1确认,2已取消,3无效,4退货
 * 
 * @author ZhengXiajun
 * 
 */
public enum order_status {
	/**
	 * 未确认
	 * 
	 * <pre>
	 * {
	 * 	"messageTxt": "未确认",
	 * 	"code": 0
	 * }
	 * </pre>
	 */
	ZERO(0, "未确认"),
	/**
	 * 确认
	 * 
	 * <pre>
	 * {
	 * 	"messageTxt": "确认",
	 * 	"code": 1
	 * }
	 * </pre>
	 */
	ONE(1, "确认"),
	/**
	 * 已取消
	 * 
	 * <pre>
	 * {
	 * 	"messageTxt": "已取消",
	 * 	"code": 2
	 * }
	 * </pre>
	 */
	TWO(2,"已取消"),
	/**
	 * 无效
	 * 
	 * <pre>
	 * {
	 * 	"messageTxt": "无效",
	 * 	"code": 3
	 * }
	 * </pre>
	 */
	THREE(3,"无效"),
	/**
	 * 退货
	 * 
	 * <pre>
	 * {
	 * 	"messageTxt": "退货",
	 * 	"code": 4
	 * }
	 * </pre>
	 */
	FOUR(4,"退货");

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

	private order_status() {
	}

	private order_status(int no, String zh) {
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
