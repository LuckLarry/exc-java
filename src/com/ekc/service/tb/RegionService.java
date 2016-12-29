package com.ekc.service.tb;

import org.springframework.stereotype.Service;

import com.ekc.service.TableUseAbs;
import com.ekc.xml.TName;

 @Service("regionservice")
public class RegionService extends TableUseAbs {
	@Override
	public String getTable() {
		return TName.eRegion;
	}

	@Override
	public String getPrimaryKey() {
		return "region_id";
	}
}
