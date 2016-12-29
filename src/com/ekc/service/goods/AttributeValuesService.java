package com.ekc.service.goods;

import org.springframework.stereotype.Service;

import com.ekc.service.TableUseAbs;
import com.ekc.xml.TName;

/**
 * 属性值表
 * 
 * @author hui 属性值表
 */
@Service("AVS")
public class AttributeValuesService extends TableUseAbs {
	@Override
	public String getTable() {
		return TName.eAttributeValues;
	}

	@Override
	public String getPrimaryKey() {
		return "attribute_value_id";
	}
}
