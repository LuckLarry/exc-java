package com.ekc.service.user;


import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ekc.service.TableUseAbs;
import com.ekc.util.ItemHelper;
import com.ekc.xml.TName;
/**
 *购物车信息表
 * 
 * @author ZhengXiajun
 */
@Service("cartService")
public class Cart extends TableUseAbs {
	@Override
	public String getTable() {	
		return TName.eCart;
	}
	@Override
	public String getPrimaryKey() {
		return "cart_id";
	}
	
	public int[] addRows(List<Map<String, Object>> list) throws Exception {
		for (int i = 0, length = list.size(); i < length; i++) {
			list.get(i).put(getPrimaryKey(), ItemHelper.createPrimaryKey());
		}
		return insert(getTable(), list);
	}
}
