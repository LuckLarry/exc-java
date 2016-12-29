package com.ekc.service.goods;

import org.springframework.stereotype.Service;

import com.ekc.service.TableUseAbs;
import com.ekc.xml.TName;

/**
 * 商品基本信息表
 * 
 * @author hui 仅存储商品的基础信息
 */
@Service("GoodsSer")
public class GoodsService extends TableUseAbs {
	@Override
	public String getTable() {
		return TName.eGoods;
	}

	@Override
	public String getPrimaryKey() {
		return "goods_id";
	}
}
