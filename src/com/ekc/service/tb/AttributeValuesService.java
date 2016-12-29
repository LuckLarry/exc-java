package com.ekc.service.tb;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekc.ifc.TableUseIfc;
import com.ekc.service.TableUseAbs;
import com.ekc.xml.TName;

/**
 * 属性值表
 * 
 * @author hui 属性值表
 */
@Service("eAttributeValues_Ser")
public class AttributeValuesService extends TableUseAbs {
	@Autowired
	TableUseIfc eGoodsAttributeValues_Ser;
	@Override
	public String getTable() {
		return TName.eAttributeValues;
	}

	@Override
	public String getPrimaryKey() {
		return "attribute_value_id";
	}
	
	/**
	 * @throws Exception 
	 * @see
	 * 删除属性值，如果包含商品属性值，则先删除商品属性值
	 * 如果不包含商品属性值，则直接删除属性值
	 */
	public int delete(Map<String, Object> map) throws Exception {
		List<Map<String,Object>>goodsAttibutes=null;
		if(map.containsKey("attribute_value_id")){
		goodsAttibutes= eGoodsAttributeValues_Ser.findList("attribute_value_id",map.get("attribute_value_id").toString());
		}
		int ci=0;
		if (goodsAttibutes!=null&&goodsAttibutes.size()>0) {
			int i=eGoodsAttributeValues_Ser.delete("attribute_value_id",map.get("attribute_value_id").toString());
			if(i>0){
				ci=delete(getTable(), map);
			}
		}else{
			ci=delete(getTable(), map);
		}
		return ci;
	}
}
