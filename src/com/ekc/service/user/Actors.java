package com.ekc.service.user;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.ekc.service.TableUseAbs;
import com.ekc.util.ItemHelper;
import com.ekc.xml.TName;

/**
 *用户角色表
 * 
 * @author ZhengXiajun
 */

@Service("actorsService")
public class Actors extends TableUseAbs {
	@Override
	public String getTable() {	
		return TName.eActors;
	}
	@Override
	public String getPrimaryKey() {
		return "actor_id";
	}
	/**
	 * @throws Exception 
	 * @see 
	 * 需要程序处理树节点
	 */
	public int addRowOnlyId(Map<String, Object> map) throws Exception {
		map.put(getPrimaryKey(), ItemHelper.createPrimaryKey());
		map=putTree(map);
		return addRow(map);
	}
	/**
	 * @throws Exception 
	 * @see 
	 * 修改时需要程序处理树节点
	 */
	public int update(Object pkValue, Map<String, Object> map) throws Exception{
		String parent_row_id = "parent_row_id";
		if(map.containsKey(parent_row_id)){
			map.remove(parent_row_id);
		}
		return update(getPrimaryKey(), pkValue, map);
	}
}

	