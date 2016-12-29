package com.ekc.service.tb;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.ekc.service.TableUseAbs;
import com.ekc.xml.TName;

@Service("menu_Ser")
public class MenuService extends TableUseAbs {
	@Override
	public String getTable() {
		return TName.eMenu;
	}

	@Override
	public String getPrimaryKey() {
		return "Menu_id";
	}

	/**
	 * @throws Exception
	 * @see 需要程序处理树节点
	 */
	public int addRowOnlyId(Map<String, Object> map) throws Exception {
		map = putTree(map);
		return addRow(map);
	}

	/**
	 * @throws Exception
	 * @see 删除菜单的同时，也要删除角色权限里对面的的菜单ID主键
	 */
	public int delete(Map<String, Object> map) throws Exception {
		//删除角色权限里对面的的菜单ID主键 
		//不管有没有删除角色权限ID主键都不作判断
		String Menu_id = map.get("Menu_id").toString();
		String sql = " update e_RolePower set Role_power = replace (Role_power, concat( ?, ','), '') ";
		simpleJdbcTemplate.update(sql, Menu_id);
		
		//删除菜单
		int num1 = delete(getTable(), map);
		return num1;
	}

}
