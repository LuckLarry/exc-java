package com.ekc.service.user;

import org.springframework.stereotype.Service;

import com.ekc.service.TableUseAbs;
import com.ekc.xml.TName;

/**
 * 职责表
 * @author ZhengXiajun
 *
 */

@Service("dutiesService")
public class Duties extends TableUseAbs {
	@Override
	public String getTable() {
		return TName.eDuties;
	}
	@Override
	public String getPrimaryKey() {
		return "duty_id";
	}
}








