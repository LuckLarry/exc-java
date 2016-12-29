package com.ekc.service.user;



import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ekc.service.TableUseAbs;
import com.ekc.util.ItemHelper;
import com.ekc.xml.TName;

/**
 * 收藏夹表
 * @author ZhengXiajun 
 *
 */

@Service("favoriteService")
public class Favorite extends TableUseAbs {
	@Override
	public String getTable() {
		return TName.eFavorite;
	}
	@Override
	public String getPrimaryKey() {
		return "favorite_id";
	}
	
	public int addRowOnlyId(Map<String, Object> map) throws Exception {
		Map<String, Object> mapRe = new HashMap<String, Object>();
		String user_id = "";
		String sku_id = "";
		if(map.containsKey("user_id")){
			user_id = map.get("user_id").toString();
			mapRe.put("user_id", user_id);
		}
		if(map.containsKey("sku_id")){
			sku_id = map.get("sku_id").toString();
			mapRe.put("sku_id", sku_id);
		}
		Map<String, Object> mapVa = selectRow(getTable(), "*", mapRe);
		if(mapVa!=null){
			return -999;
		}
		map.put(getPrimaryKey(), ItemHelper.createPrimaryKey());
		return addRow(map);
	}
}


