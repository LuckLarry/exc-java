package com.ekc.service.goods;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekc.ifc.TableUseIfc;
import com.ekc.service.TableUseAbs;
import com.ekc.util.ItemHelper;
import com.ekc.util.JoinTable;
import com.ekc.xml.TName;

/**
 * 库存查询表
 * 
 * @author ZhengXiajun
 */
@Service("StockCheckSer")
public class StockCheckService extends TableUseAbs {
	@Autowired
	TableUseIfc userphoneSer;

	@Override
	public String getTable() {
		return TName.eStockCheck;
	}

	@Override
	public String getPrimaryKey() {
		return "stock_id";
	}

	public int addRow(Map<String, Object> map) throws Exception {
		if (map.get("user_id") != null || !"".equals(map.get("user_id"))) {
			if (map.get("telephone") != null
					|| !"".equals(map.get("telephone"))) {
				Map<String, Object> mapRe = new HashMap<String, Object>();
				mapRe.put("name", map.get("name"));
				mapRe.put("telephone", map.get("telephone"));
				mapRe.put("source", "ckcx");
				mapRe.put("user_id", map.get("user_id"));
				Map<String, Object> usMap = userphoneSer.findRow(mapRe);
				String p = null;
				if (usMap != null && usMap.containsKey("pk_id")) {
					p = usMap.get("pk_id").toString();
				} else {
					p = ItemHelper.createPrimaryKey();
				    mapRe.put("pk_id", p);
				    userphoneSer.addRow(mapRe);
				}
				map.put("pk_id", p);
			} else {
				throw new Exception("没有电话号码！");
			}
		} else {
			throw new Exception("无登陆账号！");
		}
		map.put("add_time", (new Date()).getTime()); // 修改为最新时间
		return insert(getTable(), map);
	}

	public Map<String, Object> findRow(Map<String, Object> map3)
			throws Exception {
		JoinTable joinTabe = new JoinTable(TName.eStockCheck, "a");
		joinTabe.leftJoin(TName.eUserPhone, "b", "a.pk_id=b.pk_id");
		String userPhoneField = "pk_id;name;telephone;source;user_id;";
		Map<String,Object> map = new HashMap<String,Object>();
		for (String key : map3.keySet()) {
			if (userPhoneField.indexOf(key + ";") != -1) {
				map.put("b." + key, map3.get(key));
			} else {
				map.put("a." + key, map3.get(key));
			}
		}
		return selectRow(joinTabe.toString(), "a.*,b.*", map);
	}

	@Override
	public int update(Map<String, Object> map1, Map<String, Object> wMap1) throws Exception{
		JoinTable joinTabe = new JoinTable(TName.eStockCheck, "a");
		joinTabe.leftJoin(TName.eUserPhone, "b", "a.pk_id=b.pk_id");
		String userPhoneField = "pk_id;name;telephone;source;user_id;";
		Map<String, Object> map=new HashMap<String, Object>();
		for (String key : map1.keySet()) {
			if (userPhoneField.indexOf(key + ";") != -1) {
				map.put("b." + key, map1.get(key));
			} else {
				map.put("a." + key, map1.get(key));
			}
		}
		Map<String, Object> wMap=new HashMap<String, Object>();
		for (String key : wMap1.keySet()) {
			if (userPhoneField.indexOf(key + ";") != -1) {
				wMap.put("b." + key, wMap1.get(key));
			} else {
				wMap.put("a." + key, wMap1.get(key));
			}
		}
		return update(joinTabe.toString(),map, wMap);
	}
}
