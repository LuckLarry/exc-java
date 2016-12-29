package com.ekc.service.tb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekc.ifc.TableUseIfc;
import com.ekc.service.BaseService;
import com.ekc.util.ItemHelper;
import com.ekc.util.JoinTable;
import com.ekc.util.WhereTable;
import com.ekc.xml.TName;

@Service("AttributesBase")
public class AttributesBase extends BaseService {
	@Autowired
	TableUseIfc eAttributes_Ser;
	@Autowired
	TableUseIfc eAttributeValues_Ser;

	/**
	 * @see 属性表 和属性值表一起添加
	 */
	public int addRowOnlyId(Map<String, Object> map) throws Exception {
		String pk = eAttributes_Ser.getPrimaryKey();// 属性主键
		Map<String, Object> attMap = null;// 返回属性表
		int attr_i = 0;
		if (map.containsKey(pk) && !ItemHelper.isEmpty(map.get(pk))) {// 修改属性表
			attMap = mangAtrru(map, map.get(pk).toString());// 返回属性表
			attr_i = eAttributes_Ser.update(pk, attMap.get(pk).toString(),
					attMap);
		} else {// 新增属性
			attMap = mangAtrru(map, null);// 返回属性表
			attr_i = 1;
		}
		return attr_i;
	}

	/**
	 * @see 属性表 和属性值表一起查询
	 */
	public List<Map<String, Object>> findList(Map<String, Object> map)
			throws Exception {
		String pk = eAttributes_Ser.getPrimaryKey();// 属性主键
		List<Map<String, Object>> attrList = eAttributes_Ser.findList(map);
		Map<String, Object> mapList = ItemHelper.findValueToFiled(attrList);
		List<Map<String, Object>> attrList1 = new ArrayList<Map<String, Object>>();

		if (mapList.containsKey(pk)) {
			Map<String, Object> attrListMap = ItemHelper.findFiledToKey(
					eAttributeValues_Ser.findList("attribute_id",
							mapList.get(pk)), pk);
			Object mapVObje = null;
			for (Map<String, Object> attrMap : attrList) {
				mapVObje = attrListMap.get(attrMap.get("attribute_id"));
				if (mapVObje == null) {
					mapVObje = new ArrayList<Map<String, Object>>();
				}
				attrMap.put("mapValue", mapVObje);
				attrList1.add(attrMap);
			}

		}
		return attrList1;
	}

	/**
	 * 处理属性值表 返回属性表集合
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> mangAtrru(Map<String, Object> map, String att_id)
			throws Exception {
		List<Map<String, Object>> mapValue = null;
		if (ItemHelper.isEmpty(att_id)) {// 当不存在属性时 先添加属性表
			Map<String, Object> attrMap = new HashMap<String, Object>();
			att_id = ItemHelper.createPrimaryKey();
			String pk = eAttributes_Ser.getPrimaryKey();// 属性主键
			map.put(pk, att_id);
			attrMap.putAll(map);
			if (map.containsKey("mapValue")) {
				attrMap.remove("mapValue");
			}
			eAttributes_Ser.addRow(attrMap);
		}
		if (map.containsKey("mapValue")) {
			mapValue = (List<Map<String, Object>>) map.get("mapValue");
			map.remove("mapValue");
			// 处理属性值表
			String pk = eAttributeValues_Ser.getPrimaryKey();
			String v = null;
			for (Map<String, Object> attrVMap : mapValue) {
				attrVMap.put("attribute_id", att_id);
				if (attrVMap.containsKey(pk)
						&& !ItemHelper.isEmpty(attrVMap.get(pk))) {// 修改属性值表
					v = attrVMap.get(pk).toString();
					attrVMap.remove(pk);
					eAttributeValues_Ser.update(pk, v, attrVMap);
				} else {// 新增属性值
					eAttributeValues_Ser.addRowOnlyId(attrVMap);
				}
			}
		}
		return map;
	}

	/**
	 * 通过属性值查询 属性值id，没有对应属性值即添加
	 * 
	 * @param attr_v
	 *            属性值
	 * @return
	 * @throws Exception
	 */
	public String findAttributeValueId(Map<String, Object> where)
			throws Exception {
		Map<String, Object> map = eAttributeValues_Ser.findRow(where);
		String attribute_value_id = null;
		if (map != null && map.containsKey("attribute_value_id")
				&& !ItemHelper.isEmpty(map.get("attribute_value_id"))) {
			attribute_value_id = map.get("attribute_value_id").toString();
		} else {
			attribute_value_id = ItemHelper.createPrimaryKey();
			where.put("attribute_value_id", attribute_value_id);
			eAttributeValues_Ser.addRow(where);
		}
		return attribute_value_id;
	}

	/**
	 * 通过属性值查询 属性值id，没有对应属性值即添加(且必须属性设置attribute_input_type为1)
	 * 
	 * @param attribute_id
	 *            属性id
	 * @param attribute_value
	 *            属性值
	 * @return
	 * @throws Exception
	 */
	public String findAttributeValueId(String attribute_id,
			String attribute_value,String valueId) throws Exception {
		JoinTable table = new JoinTable(TName.eAttributeValues, "a");
		table.rightJoin(TName.eAttributes, "b", "a.attribute_id=b.attribute_id and a.attribute_value='"+attribute_value+"'");
		WhereTable whereMap = new WhereTable("b.attribute_id", attribute_id);
		Map<String, Object> map = selectRow(table.toString(),
				"a.attribute_value_id,b.attribute_input_type",
				whereMap.getMap());
		String attribute_value_id = null;
		if (map != null && map.containsKey("attribute_input_type")
				&& (Boolean)map.get("attribute_input_type")) {
			if (map.containsKey("attribute_value_id")
					&& !ItemHelper.isEmpty(map.get("attribute_value_id"))) {
				attribute_value_id = map.get("attribute_value_id").toString();
			} else {
				attribute_value_id = ItemHelper.createPrimaryKey();
				WhereTable where = new WhereTable("attribute_id", attribute_id);
				where.put("attribute_value", attribute_value);
				where.put("attribute_value_id", attribute_value_id);
				eAttributeValues_Ser.addRow(where.getMap());
			}
		}else{
			attribute_value_id=valueId;
		}
		return attribute_value_id;
	}
}
