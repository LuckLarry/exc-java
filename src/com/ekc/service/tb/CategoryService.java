package com.ekc.service.tb;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekc.ifc.TableUseIfc;
import com.ekc.service.TableUseAbs;
import com.ekc.util.ItemHelper;
import com.ekc.xml.TName;

/**
 * 商品分类表
 * 
 * @author hui 对商品的基本信息进行分类
 */
@Service("eCategory_Ser")
public class CategoryService extends TableUseAbs {
	@Autowired 
	TableUseIfc eGoods_Ser;
	@Autowired
	TableUseIfc eAttributeValues_Ser;
	@Override
	public String getTable() {
		return TName.eCategory;
	}

	@Override
	public String getPrimaryKey() {
		return "category_id";
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
	 * 删除分类，如果分类中包含商品或者属性，则先删除商品或者属性，再删除分类
	 * 如果分类中不包含商品，则删除分类
	 * @throws Exception 
	 */
	public int delete(Map<String, Object> map) throws Exception {
		List<Map<String, Object>> goods=null;
		List<Map<String, Object>> attributes=null;
		if(map.containsKey("category_id")){
		goods=eGoods_Ser.findList("category_id",map.get("category_id").toString());
		attributes= eAttributeValues_Ser.findList("category_id",map.get("category_id").toString());
		}
		int ci=0;
		if ((goods!=null&&goods.size()>0)|| (attributes!=null&&attributes.size()>0)){
			int i=eGoods_Ser.delete("category_id",map.get("category_id").toString());
			int j=eAttributeValues_Ser.delete("category_id",map.get("category_id").toString());
			if(i>0||j>0){
				ci=delete(getTable(), map);
			}
		}else{
			ci=delete(getTable(), map);
		}
		return ci;
	}
	
}
