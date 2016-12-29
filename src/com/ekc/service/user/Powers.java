package com.ekc.service.user;

import org.springframework.stereotype.Service;

import com.ekc.service.TableUseAbs;
import com.ekc.xml.TName;
/**
 * 权限信息表
 * @author ZhengXiajun
 *
 */
@Service("powerService")
public class Powers extends TableUseAbs {
	@Override
	public String getTable() {
		return TName.ePowers;
	}
	@Override
	public String getPrimaryKey() {
		return "power_id";
	}
}










