package com.ekc.service.user;

import org.springframework.stereotype.Service;

import com.ekc.service.TableUseAbs;
import com.ekc.xml.TName;

/**
 * 用户评价表
 * @author ZhengXiajun
 *
 */

@Service("userEvaService")
public class UserEvaluation extends TableUseAbs {
	@Override
	public String getTable() {
		return TName.eUserEvaluation;
	}
	@Override
	public String getPrimaryKey() {
		return "comment_id";
	}
	
}
