package com.ekc.service.order;

import org.springframework.stereotype.Service;

import com.ekc.service.TableUseAbs;
import com.ekc.xml.TName;

/**
 * 物流状态表
 * 
 * @author hui
 */
@Service("LogisticsStatusSer")
public class LogisticsStatusService extends TableUseAbs {
	@Override
	public String getTable() {
		return TName.eLogisticsStatus;
	}

	@Override
	public String getPrimaryKey() {
		return "logistics_status_id";
	}
}
