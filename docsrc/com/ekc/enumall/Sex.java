package com.ekc.enumall;

public enum Sex {
	/**
	 * 性别：保密
	 * 
	 * <pre>
	 * {
	 * 	"message": "secrecy",
	 * 	"messageTxt": "保密",
	 * 	"code": 0
	 * }
	 * *
	 * </pre>
	 */
	SEX_SECRECY(0, "secrecy", "保密"),
	/**
	 * 性别：男
	 * 
	 * <pre>
	 * {
	 * 	"message": "boy",
	 * 	"messageTxt": "男",
	 * 	"code": 1
	 * }
	 * *
	 * </pre>
	 */
	SEX_BOY(1, "boy", "男"),
	/**
	 * 性别：女
	 * 
	 * <pre>
	 * {
	 * 	"message": "girl",
	 * 	"messageTxt": "女",
	 * 	"code": 2
	 * }
	 * *
	 * </pre>
	 */
	SEX_GIRL(2, "girl", "女");

	private int code;
	private String value_en;
	private String value_zh;

	public int getCode() {
		return code;
	}

	public String getValue_en() {
		return value_en;
	}

	public String getValue_zh() {
		return value_zh;
	}

	private Sex() {
	}

	private Sex(int no, String en, String zh) {
		this.code = no;
		this.value_en = en;
		this.value_zh = zh;
	}

	/**
	 * 用户编号
	 */
	public String toString() {
		return String.valueOf(this.code);
	}

}
