package com.ekc.service.order;

import org.springframework.stereotype.Service;

import com.ekc.service.TableUseAbs;
import com.ekc.xml.TName;

/**
 * 来样/找货/订制申请表
 * 
 * @author hui 对商品的基本信息进行分类
 */
@Service("FOCGASer")
public class FindOrCustomGoodsApplyService extends TableUseAbs {
	@Override
	public String getTable() {
		return TName.eFindOrCustomGoodsApply;
	}

	@Override
	public String getPrimaryKey() {
		return "apply_id";
	}
}
