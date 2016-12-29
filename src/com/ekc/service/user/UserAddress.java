package com.ekc.service.user;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.ekc.service.TableUseAbs;
import com.ekc.util.WhereTable;
import com.ekc.xml.TName;

/**
 * 收货信息表
 * 
 * @author ZhengXiajun
 * 
 */

@Service("userAddService")
public class UserAddress extends TableUseAbs {
	@Override
	public String getTable() {
		return TName.eUserAddress;
	}

	@Override
	public String getPrimaryKey() {
		return "address_id";
	}

	@Override
	public int update(Map<String, Object> map, Map<String, Object> wMap)
			throws Exception {
		if (map.containsKey("isDefault") && "1".equals(map.get("isDefault"))) {// 设置默认地址时修改
			if (map.containsKey("user_id") && map.get("user_id") != null) {
				WhereTable w = new WhereTable("user_id", map.get("user_id"));
				WhereTable sMap = new WhereTable("isDefault", "0");
				update(getTable(), sMap.getMap(), w.getMap());
			}
		}
		return update(getTable(), map, wMap);
	}
}
