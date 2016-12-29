package com.ekc.service.goods;

import org.springframework.stereotype.Service;

import com.ekc.service.TableUseAbs;
import com.ekc.xml.TName;

/**
 * 商品与分类对照表
 * 
 * @author hui
 */
@Service("GoodsCategorySer")
public class GoodsCategoryService extends TableUseAbs {
	@Override
	public String getTable() {
		return TName.eGoodsCategory;
	}

	@Override
	public String getPrimaryKey() {
		return "goods_category_id";
	}
}
