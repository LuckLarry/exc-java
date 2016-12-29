package com.ekc.service.order;

import org.springframework.stereotype.Service;

import com.ekc.service.TableUseAbs;
import com.ekc.xml.TName;
/**
 * 订单仓库表
 * 
 * @author ZhengXiajun
 *  将订单仓库表信息分类
 */
@Service("OrderCangSer")
public class OrderCangService extends TableUseAbs {

	@Override
	public String getTable() {
		return TName.eOrderCang;
	}

	@Override
	public String getPrimaryKey() {
		return "id";
	}

}
