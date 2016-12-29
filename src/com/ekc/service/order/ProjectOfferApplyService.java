package com.ekc.service.order;

import org.springframework.stereotype.Service;

import com.ekc.service.TableUseAbs;
import com.ekc.xml.TName;

/**
 * 工程报价申请表
 * 
 * @author hui
 */
@Service("POASer")
public class ProjectOfferApplyService extends TableUseAbs {
	@Override
	public String getTable() {
		return TName.eProjectOfferApply;
	}

	@Override
	public String getPrimaryKey() {
		return "apply_id";
	}
}
