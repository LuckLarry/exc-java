package com.ekc.service.goods;

import org.springframework.stereotype.Service;

import com.ekc.service.TableUseAbs;
import com.ekc.xml.TName;

/**
 * 商品属性值表
 * 
 * @author hui
 */
@Service("GAVSer")
public class GoodsAttributesValueService extends TableUseAbs {

	@Override
	public String getTable() {
		return TName.eGoodsAttributesValue;
	}

	@Override
	public String getPrimaryKey() {
		return "goods_attribute_id";
	}
}
