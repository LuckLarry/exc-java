package com.ekc.service.order;

import org.springframework.stereotype.Service;

import com.ekc.service.TableUseAbs;
import com.ekc.xml.TName;


@Service("OrderSkuList_Ser")
public class OrderGoodsPriceService extends TableUseAbs {
	@Override
	public String getTable() {
		return TName.eOrdersSKUList;
	}

	@Override
	public String getPrimaryKey() {
		return "order_sku_list_id";
	}
}