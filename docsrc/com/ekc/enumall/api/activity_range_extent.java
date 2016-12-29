package com.ekc.enumall.api;

import java.util.HashMap;
import java.util.Map;

import com.ekc.xml.MessageXml;

/**
 * 优惠范围
 * 
 * 优惠范围；0，全部商品；1，按分类；2，按品牌；3，按商品
 * 
 * @author ZhengXiajun
 * 
 */
public enum activity_range_extent {

	/**
	 * 全部商品
	 * 
	 * <pre>
	 * {
	 * 	"messageTxt": "全部商品",
	 * 	"code": 0
	 * }
	 * </pre>
	 */
	ZERO(0, "全部商品"),
	/**
	 * 按分类
	 * 
	 * <pre>
	 * {
	 * 	"messageTxt": "按分类",
	 * 	"code": 1
	 * }
	 * </pre>
	 */
	ONE(1, "按分类"),
	/**
	 * 按品牌
	 * 
	 * <pre>
	 * {
	 * 	"messageTxt": "按品牌",
	 * 	"code": 2
	 * }
	 * </pre>
	 */
	TWO(2,"按品牌"),
	/**
	 * 按商品
	 * 
	 * <pre>
	 * {
	 * 	"messageTxt": "按商品",
	 * 	"code": 3
	 * }
	 * </pre>
	 */
	THREE(3,"按商品");

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

	private activity_range_extent() {
	}

	private activity_range_extent(int no, String zh) {
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
