package com.ekc.service.goods;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ekc.service.TableUseAbs;
import com.ekc.xml.TName;

/**
 * 商品属性表
 * 
 * @author hui 对商品的基本信息进行分类
 */
@Service("AttributesSer")
public class AttributesService extends TableUseAbs {
	@Override
	public String getTable() {
		return TName.eAttributes;
	}

	@Override
	public String getPrimaryKey() {
		return "attribute_id";
	}
	
	@Override
	public List<Map<String, Object>> findList(Map<String, Object> map) {
		return select(getTable(), "*,replace(attribute_values,'\r\n',',') as av_r", map);
	}
}
