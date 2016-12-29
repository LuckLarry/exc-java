package com.ekc.enumall;

public enum LoginType {
	/**
	 * 普通用户
	 * <p>
	 * type = 0
	 */
	PUTONG(0),
	/**
	 * 微信用户
	 * <p>
	 * type = 0
	 */
	WEIXIN(1),
	/**
	 * QQ用户
	 * <p>
	 * type = 0
	 */
	QQ(2);
	private int type;

	public int getType() {
		return type;
	}

	LoginType(int type) {
		this.type = type;
	}

	public String toString() {
		return String.valueOf(type);
	}
}
