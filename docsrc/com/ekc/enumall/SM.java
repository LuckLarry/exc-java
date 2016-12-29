package com.ekc.enumall;

public enum SM {
	/**
	 * 楼兰家居
	 * 
	 * <pre>
	 * {
	 * 	"message": "LOLA",
	 * 	"messageTxt": "【楼兰家居】",
	 * 	"code": 1
	 * }
	 * *
	 * </pre>
	 */
	IDENTI(1, "LOLA", "【楼兰家居】"),
	/**
	 * 类型
	 * 
	 * <pre>
	 * {
	 * 	"message": "type",
	 * 	"messageTxt": "类型",
	 * 	"code": 2
	 * }
	 * *
	 * </pre>
	 */
	TYPE(2, "type", "类型"),
	/**
	 * 短信：营销类
	 * 
	 * <pre>
	 * {
	 * 	"message": "yingxiao",
	 * 	"messageTxt": "营销类",
	 * 	"code": 3
	 * }
	 * *
	 * </pre>
	 */
	YINGXIAO(3, "yingxiao", "营销类"),
	/**
	 * 短信：通知类
	 * 
	 * <pre>
	 * {
	 * 	"message": "tongzhi",
	 * 	"messageTxt": "通知类",
	 * 	"code": 4
	 * }
	 * *
	 * </pre>
	 */
	TONGZHI(4, "tongzhi", "通知类"),
	/**
	 * 消息
	 * 
	 * <pre>
	 * {
	 * 	"message": "message",
	 * 	"messageTxt": "消息",
	 * 	"code": 5
	 * }
	 * *
	 * </pre>
	 */
	MESSAGE(5, "message", "消息"),
	/**
	 * 手机号
	 * 
	 * <pre>
	 * {
	 * 	"message": "phone",
	 * 	"messageTxt": "手机号",
	 * 	"code": 6
	 * }
	 * *
	 * </pre>
	 */
	PHONE(6, "phone", "手机号"),
	/**
	 * 错误手机号信息
	 * 
	 * <pre>
	 * {
	 * 	"message": "fail",
	 * 	"messageTxt": "错误手机号信息",
	 * 	"code": 7
	 * }
	 * *
	 * </pre>
	 */
	FAIL(7, "fail", "错误手机号信息");

	private int code;
	private String mess_en;
	private String mess_zh;

	public int getCode() {
		return code;
	}

	public String getMess_en() {
		return mess_en;
	}

	public String getMess_zh() {
		return mess_zh;
	}

	private SM() {
	}

	private SM(int no, String en, String zh) {
		this.code = no;
		this.mess_en = en;
		this.mess_zh = zh;
	}

	public String toString() {
		return String.valueOf(this.code);
	}
}
