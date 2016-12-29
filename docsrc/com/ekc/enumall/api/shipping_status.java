package com.ekc.enumall.api;

import java.util.HashMap;
import java.util.Map;

import com.ekc.xml.MessageXml;

/**
 * 配送状态
 * 
 * 商品配送情况; 0 未发货 ； 1 已发货；  2 已收货； 4 退货。
 * 
 * @author ZhengXiajun
 * 
 */
public enum shipping_status {
	/**
	 * 未发货
	 * 
	 * <pre>
	 * {
	 * 	"messageTxt": "未发货",
	 * 	"code": 0
	 * }
	 * </pre>
	 */
	ZERO(0, "未发货"),
	/**
	 * 已发货
	 * 
	 * <pre>
	 * {
	 * 	"messageTxt": "已发货",
	 * 	"code": 1
	 * }
	 * </pre>
	 */
	ONE(1, "已发货"),
	/**
	 * 已收货
	 * 
	 * <pre>
	 * {
	 * 	"messageTxt": "已收货",
	 * 	"code": 2
	 * }
	 * </pre>
	 */
	TWO(2,"已收货"),
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

	private shipping_status() {
	}

	private shipping_status(int no, String zh) {
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
	public Map<String,Object> getTextCode(){
		Map<String,Object> map=new HashMap<String, Object>();
		for (shipping_status em:shipping_status.values()) {
			map.put(em.getMess_zh(), em.getCode());
		}
		return map;
	}
}
