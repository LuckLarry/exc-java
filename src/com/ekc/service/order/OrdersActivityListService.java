package com.ekc.service.order;

import org.springframework.stereotype.Service;

import com.ekc.service.TableUseAbs;
import com.ekc.xml.TName;

/**
 * 订单中的活动列表
 * 
 * @author hui 对商品的基本信息进行分类
 */
@Service("OALSer")
public class OrdersActivityListService extends TableUseAbs {
	@Override
	public String getTable() {
		return TName.eOrdersActivityList;
	}

	@Override
	public String getPrimaryKey() {
		return "order_activity_list_id";
	}
}
