package com.ekc.service.goods;

import org.springframework.stereotype.Service;

import com.ekc.service.TableUseAbs;
import com.ekc.xml.TName;

/**
 * 仓库库存期间余额表
 * 
 * @author hui
 */
@Service("WBSer")
public class WarehouseBalanceService extends TableUseAbs {
	@Override
	public String getTable() {
		return TName.eWarehouseBalance;
	}

	@Override
	public String getPrimaryKey() {
		return "warehouse_balance_id";
	}
}
