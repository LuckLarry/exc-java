package com.ekc.service.goods;

import org.springframework.stereotype.Service;

import com.ekc.service.TableUseAbs;
import com.ekc.xml.TName;

/**
 * 仓库库存日志表
 * 
 * @author hui 记录仓库库存操作的日志
 */
@Service("WarehouseLogSer")
public class WarehouseLogService extends TableUseAbs {
	@Override
	public String getTable() {
		return TName.eWarehouseLog;
	}

	@Override
	public String getPrimaryKey() {
		return "log_id";
	}
}
