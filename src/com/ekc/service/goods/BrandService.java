package com.ekc.service.goods;

import org.springframework.stereotype.Service;

import com.ekc.service.TableUseAbs;
import com.ekc.xml.TName;

/**
 * 商品品牌表
 * 
 * @author hui
 */
@Service("BrandSer")
public class BrandService extends TableUseAbs {

	@Override
	public String getTable() {
		return TName.eBrand;
	}

	@Override
	public String getPrimaryKey() {
		return "brand_id";
	}
}
