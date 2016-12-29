package com.ekc.service.user;

import org.springframework.stereotype.Service;

import com.ekc.service.TableUseAbs;
import com.ekc.xml.TName;

/**
 * 用户账目日志表
 * @author ZhengXiajun
 *
 */
@Service("userAccLogService")
public class UserAccountLog extends TableUseAbs {
	@Override
	public String getTable() {
		return TName.eUserAccountLog;
	}
	@Override
	public String getPrimaryKey() {
		return "log_id";
	}
}
