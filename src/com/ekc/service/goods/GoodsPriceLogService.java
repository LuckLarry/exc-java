package com.ekc.service.goods;

import org.springframework.stereotype.Service;

import com.ekc.service.TableUseAbs;
import com.ekc.xml.TName;

/**
 * 商品价格操作日志表
 * 
 * @author hui 记录商品价格的调整日志
 */
@Service("GoodsPriceLogSer")
public class GoodsPriceLogService extends TableUseAbs {
	@Override
	public String getTable() {
		return TName.eGoodsPriceLog;
	}

	@Override
	public String getPrimaryKey() {
		return "log_id";
	}
}
