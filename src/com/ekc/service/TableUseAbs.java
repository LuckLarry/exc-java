package com.ekc.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ekc.ifc.TableUseIfc;
import com.ekc.util.ItemHelper;
import com.ekc.util.WhereTable;

/**
 * 处理经常使用的方法,继承类时根据情况重写
 * 
 * @author pengbei_qq1179009513
 * 
 */
public abstract class TableUseAbs extends BaseService implements TableUseIfc {
	public abstract String getTable();
    public String getPageField(){
    	return "page,page_size,";
    }
	public abstract String getPrimaryKey();

	public Map<String, Object> findRow(String filed, Object fildeValue) throws Exception{
		WhereTable whereTable = new WhereTable(filed, fildeValue);
		return findRow(whereTable.getMap());
	}

	public Map<String, Object> findRow(String pkId) throws Exception{
		return findRow(getPrimaryKey(), pkId);
	}

	public Map<String, Object> findRow(Map<String, Object> map) throws Exception{
		return selectRow(getTable(), "*", map);
	}

	public int addRowOnlyId(Map<String, Object> map) throws Exception {
		map.put(getPrimaryKey(), ItemHelper.createPrimaryKey());
		return addRow(map);
	}

	public int addRow(Map<String, Object> map) throws Exception{
		return insert(getTable(), map);
	}

	public int[] addRows(List<Map<String, Object>> list) throws Exception {
		return insert(getTable(), list);
	}

	public final List<Map<String, Object>> findList(String pkId) throws Exception {
		return findList(getPrimaryKey(), pkId);
	}

	public final List<Map<String, Object>> findList(String field,
			Object fieldValue) throws Exception{
		WhereTable whereTable = new WhereTable(field, fieldValue);
		return findList(whereTable.getMap());
	}

	@Override
	public List<Map<String, Object>> findList(Map<String, Object> map) throws Exception {
		return select(getTable(), "*", map);
	}

	@Override
	public Map<String, Object> findPage(Map<String, Object> map) throws Exception{
		return selectPage(getTable(), "*", map);
	}

	@Override
	public int delete(Object pkValue) throws Exception{
		return delete(getPrimaryKey(), pkValue);
	}

	@Override
	public int delete(String field, Object value) throws Exception{
		WhereTable whereTable = new WhereTable(field, value);
		return delete(whereTable.getMap());
	}

	@Override
	public int delete(Map<String, Object> map) throws Exception {
		return delete(getTable(), map);
	}

	@Override
	public int update(Object pkValue, Map<String, Object> map) throws Exception{
		return update(getPrimaryKey(), pkValue, map);
	}

	@Override
	public int update(String field, Object value, Map<String, Object> map) throws Exception{
		WhereTable whereTable = new WhereTable(field, value);
		return update(map, whereTable.getMap());
	}

	@Override
	public int update(Map<String, Object> map, Map<String, Object> wMap) throws Exception{
		return update(getTable(),map, wMap);
	}
	
	protected Map<String, Object> putTree(Map<String, Object> map) throws Exception{
		Map<String, Object> treeMap = new HashMap<String, Object>();
		if (map.containsKey("parent_row_id")&&!ItemHelper.isEmpty(map.get("parent_row_id"))) {
			Map<String, Object> parentMap = findRow("row_id",
					map.get("parent_row_id"));
			treeMap = getTree(map.get("parent_row_id").toString(), parentMap
					.get("tree_row_id").toString());
		} else {
			treeMap = getTree("", "");
		}
		map.putAll(treeMap);
		return map;
	}
}
