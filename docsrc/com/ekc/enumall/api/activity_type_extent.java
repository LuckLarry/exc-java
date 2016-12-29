package com.ekc.enumall.api;

import java.util.HashMap;
import java.util.Map;

import com.ekc.xml.MessageXml;

/**
 * 参加活动的优惠方式
 * 
 * 参加活动的优惠方式；0，送赠品或优惠购买；1，现金减免；价格打折优惠
 * 
 * @author ZhengXiajun
 * 
 */
public enum activity_type_extent {

	/**
	 * 送赠品或优惠购买
	 * 
	 * <pre>
	 * {
	 * 	"messageTxt": "送赠品或优惠购买",
	 * 	"code": 0
	 * }
	 * </pre>
	 */
	ZERO(0, "送赠品或优惠购买"),
	/**
	 * 现金减免；价格打折优惠
	 * 
	 * <pre>
	 * {
	 * 	"messageTxt": "现金减免；价格打折优惠",
	 * 	"code": 1
	 * }
	 * </pre>
	 */
	ONE(1, "现金减免；价格打折优惠");
	

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

	private activity_type_extent() {
	}

	private activity_type_extent(int no, String zh) {
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
