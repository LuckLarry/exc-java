package com.ekc.service.goods;

import org.springframework.stereotype.Service;

import com.ekc.service.TableUseAbs;
import com.ekc.xml.TName;

/**
 * 商品图库表
 * 
 * @author hui
 */
@Service("GoodsImageSer")
public class GoodsImageService extends TableUseAbs {

	@Override
	public String getTable() {
		return TName.eGoodsImage;
	}

	@Override
	public String getPrimaryKey() {
		return "img_id";
	}
}
