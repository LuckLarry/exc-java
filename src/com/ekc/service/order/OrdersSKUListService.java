package com.ekc.service.order;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ekc.service.TableUseAbs;
import com.ekc.util.ItemHelper;
import com.ekc.xml.MessageXml;
import com.ekc.xml.TName;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * 订单对应的商品信息
 * 
 * @author hui
 */
@Service("OSKULSer")
public class OrdersSKUListService extends TableUseAbs {
	@Override
	public String getTable() {
		return TName.eOrdersSKUList;
	}

	@Override
	public String getPrimaryKey() {
		return "order_sku_list_id";
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> findPage(Map<String, Object> map) throws Exception{
		Map<String, Object> mapRe = selectPage(getTable(), "*", map);
		if(mapRe != null){
			if(mapRe.containsKey(MessageXml.list_key)){
				List<Map<String, Object>> list = (List<Map<String, Object>>)mapRe.get(MessageXml.list_key);
				GsonBuilder gb = new GsonBuilder();
				Gson g = gb.create();
				Map<String, Object> m=null;
				for (int i = 0, length = list.size(); i < length; i++) {
					String sku_name = list.get(i).get("sku_name").toString();
					try{
						m= g.fromJson(sku_name, Map.class);
					}catch (Exception e) {
						m=null;
					}
					list.get(i).put("sku_name",m==null?sku_name:m);
				}
			}
		}
		return mapRe;
	}
	
	public int[] addRows(List<Map<String, Object>> list) throws Exception {
		for (int i = 0,length = list.size(); i < length; i++) {
			list.get(i).put(getPrimaryKey(), ItemHelper.createPrimaryKey());
		}
		return insert(getTable(), list);
	}
}
