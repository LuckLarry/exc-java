package com.ekc.service.tb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekc.ifc.TableUseIfc;
import com.ekc.service.TableUseAbs;
import com.ekc.util.ItemHelper;
import com.ekc.xml.TName;

/**
 * 商品属性表
 * 
 * @author hui 对商品的基本信息进行分类
 */
@Service("eAttributes_Ser")
public class AttributesService extends TableUseAbs {
	@Autowired 
	TableUseIfc eAttributeValues_Ser;
	@Autowired
	TableUseIfc eGoodsAttributeValues_Ser;
	@Autowired
	TableUseIfc eCategory_Ser;
	
	@Override
	public String getTable() {
		return TName.eAttributes;
	}

	@Override
	public String getPrimaryKey() {
		return "attribute_id";
	}
	
	
	/**
	 * @see
	 * 删除属性，如果属性包含属性值，则先删除属性值
	 * 如果不包含属性值，则直接删除属性
	 */
	public int delete(Map<String, Object> map) throws Exception {
		List<Map<String, Object>> attributes=null;
		List<Map<String,Object>>goodsAttibutes=null;
		if(map.containsKey("attribute_id")){
		attributes= eAttributeValues_Ser.findList("attribute_id",map.get("attribute_id").toString());
		goodsAttibutes= eGoodsAttributeValues_Ser.findList("attribute_id",map.get("attribute_id").toString());
		}
		int ci=0;
		if ((attributes!=null&&attributes.size()>0)||(goodsAttibutes!=null&&goodsAttibutes.size()>0) ){
			int i= eAttributeValues_Ser.delete("attribute_id",map.get("attribute_id").toString());
			int j=eGoodsAttributeValues_Ser.delete("attribute_id",map.get("attribute_id").toString());
			if(i>0||j>0){
				ci=delete(getTable(), map);
			}
		}else{
			ci=delete(getTable(), map);
		}
		return ci;
	}
	
	/**
	 * @throws Exception 
	 * @see
	 * 属性表 和属性值表一起添加
	 */
	@SuppressWarnings("unchecked")
	public int addRowOnlyId(Map<String, Object> map) throws Exception {
		int num1 = 0;
		String pk = getPrimaryKey();
		String pkValue = ItemHelper.createPrimaryKey();
		List<Map<String, Object>> mapValue = null;
		Map<String, Object> mapDel = null;
		String attribute_id = "";
		if(map.containsKey(pk)){
			attribute_id = map.get(pk).toString();
			if(!attribute_id.equals("") && attribute_id !=null){
				mapDel=new HashMap<String, Object>();
				mapDel.put(pk, attribute_id);
				this.delete(mapDel);
			}
		}
		try {
			map.put(pk, pkValue);
			if(map.containsKey("mapValue")){
				mapValue = (List<Map<String, Object>>)map.get("mapValue");
				map.remove("mapValue");
				for (int i=0,length=mapValue.size(); i<length; i++) {
					mapValue.get(i).put(pk, pkValue);
					mapValue.get(i).put(eAttributeValues_Ser.getPrimaryKey(), ItemHelper.createPrimaryKey());
					mapValue.get(i).put("add_time", map.get("add_time").toString());
					mapValue.get(i).put("sort_order", i);
					
				}
			}
			num1 = addRow(map);//属性
			if (num1 == 0){
				return 0;
			}
			int num2[]=eAttributeValues_Ser.addRows(mapValue);//属性值
			
			for (int i=0,length=num2.length; i<length; i++) {
				if (num2[i] == 0)
					return 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return num1;
	}
	
	/**
	 * 获取所选分类的属性（当前分类的属性以及所有父级分类的继承属性并判
	 * 断是否sku	   属性）
	 * @return
	 * @throws Exception 
	 */
	public Map<String, Object> allAttributevalues(Map<String, Object> map) throws Exception{
		Map<String, Object> whereMap = new HashMap<String, Object>();
		List<List<Map<String, Object>>> mapValue = new ArrayList<List<Map<String,Object>>>();
		Map<String, Object> category = eCategory_Ser.findRow(map);
		whereMap.put("row_id", map.get("parent_row_id").toString());
		List<Map<String, Object>> categoryList = eCategory_Ser.findList(whereMap);
		categoryList.add(category);
		for (int i = 0, length=categoryList.size(); i < length; i++) {
			mapValue.add(eAttributeValues_Ser.findList(categoryList.get(i)));
		}
		category.put("mapValue",mapValue);
		return category;
	}
	//禁止修改原先 属性 为  产品编号，规格，等级
	@Override
	public int update(Map<String, Object> map, Map<String, Object> wMap) throws Exception{
		ItemHelper.setIntToMapKey(wMap, 100, "attribute_name not in ('产品编号','规格','等级')");//需要跟erp匹配,不允许改动
		return update(getTable(),map, wMap);
	}
}
