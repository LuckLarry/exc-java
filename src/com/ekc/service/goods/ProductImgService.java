package com.ekc.service.goods;

import org.springframework.stereotype.Service;

import com.ekc.service.TableUseAbs;
import com.ekc.xml.TName;

/**
 * 商品图库表
 * 
 * @author hui 商品图库表
 */
@Service("ProductImgSer")
public class ProductImgService extends TableUseAbs {
	@Override
	public String getTable() {
		return TName.eProductImg;
	}

	@Override
	public String getPrimaryKey() {
		return "product_img_id";
	}
}
