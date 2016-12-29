package com.ekc.service.tb;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekc.ifc.TableUseIfc;
import com.ekc.service.TableUseAbs;
import com.ekc.xml.TName;

/**
 * 商品属性值表
 * 
 * @author ZhengXiajun
 */
@Service("eGoodsAttributeValues_Ser")
public class GoodsAttributesValueService extends TableUseAbs {
    @Autowired
    TableUseIfc eGoods_Ser;
    @Autowired
    TableUseIfc eAttributeValues_Ser;
    @Autowired
    TableUseIfc eAttributes_Ser;
	@Override
	public String getTable() {
		return TName.eGoodsAttributesValue;
	}

	@Override
	public String getPrimaryKey() {
		return "goods_attr_value_id";
	}
	public int delete(Map<String, Object> map) throws Exception {
		List<Map<String, Object>> goods=null;
		List<Map<String, Object>> attributes=null;
		List<Map<String, Object>> attributevalues=null; 
		if(map.containsKey("goods_attr_value_id")){
		goods=eGoods_Ser.findList("goods_attr_value_id",map.get("goods_attr_value_id").toString());
		attributes= eAttributeValues_Ser.findList("goods_attr_value_id",map.get("goods_attr_value_id").toString());
		attributevalues= eAttributes_Ser.findList("goods_attr_value_id",map.get("goods_attr_value_id").toString());
		}
		int ci=0;
		if ((goods!=null&&goods.size()>0)||(attributes!=null&&attributes.size()>0)||(attributevalues!=null&&attributevalues.size()>0)){
			int i=eGoods_Ser.delete("goods_attr_value_id",map.get("goods_attr_value_id").toString());
			int j=eAttributeValues_Ser.delete("goods_attr_value_id",map.get("goods_attr_value_id").toString());
			int k=eAttributes_Ser.delete("goods_attr_value_id",map.get("goods_attr_value_id").toString());
			if(i>0||j>0||k>0){
				ci=delete(getTable(), map);
			}
		}else{
			ci=delete(getTable(), map);
		}
		return ci;
	}
	
}
