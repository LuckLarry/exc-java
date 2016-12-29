package com.ekc.service.goods;

import org.springframework.stereotype.Service;

import com.ekc.service.TableUseAbs;
import com.ekc.xml.TName;

/**
 * 商品统一编号信息表
 * 
 * @author hui
 */
@Service("SKUSer")
public class SKUService extends TableUseAbs {
	@Override
	public String getTable() {
		return TName.eSKU;
	}

	@Override
	public String getPrimaryKey() {
		return "sku_id";
	}
}
