package com.ekc.service.user;

import org.springframework.stereotype.Service;

import com.ekc.service.TableUseAbs;
import com.ekc.xml.TName;

/**
 * 地区数据表
 * @author ZhengXiajun
 *
 */

@Service("regionService")
public class Region extends TableUseAbs{
	@Override
	public String getTable() {
		return TName.eRegion;
	}
	@Override
	public String getPrimaryKey() {
		return "region_id";
	}
}








