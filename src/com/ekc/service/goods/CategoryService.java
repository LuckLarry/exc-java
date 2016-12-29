package com.ekc.service.goods;

import org.springframework.stereotype.Service;

import com.ekc.service.TableUseAbs;
import com.ekc.xml.TName;

/**
 * 商品分类表
 * 
 * @author hui 对商品的基本信息进行分类
 */
@Service("CategorySer")
public class CategoryService extends TableUseAbs {
	@Override
	public String getTable() {
		return TName.eCategory;
	}
	@Override
	public String getPrimaryKey() {
		return "category_id";
	}
}
