package com.ekc.enumall.api;

import java.util.HashMap;
import java.util.Map;

import com.ekc.xml.MessageXml;

/**
 *用户评论的类型
 * 
 *用户评论的类型;0评论的是商品,1评论的是文章
 * 
 * @author ZhengXiajun
 * 
 */
public enum comment_type {
	/**
	 *评论的是商品
	 * 
	 * <pre>
	 * {
	 * 	"messageTxt": "评论的是商品",
	 * 	"code": 0
	 * }
	 * </pre>
	 */
	ZERO(0, "评论的是商品"),
	/**
	 * 评论的是文章
	 * 
	 * <pre>
	 * {
	 * 	"messageTxt": "评论的是文章",
	 * 	"code": 1
	 * }
	 * </pre>
	 */
	ONE(1, "评论的是文章");

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

	private comment_type() {
	}

	private comment_type(int no, String zh) {
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
