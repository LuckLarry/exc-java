package com.ekc.service.tb;

import org.springframework.stereotype.Service;

import com.ekc.service.TableUseAbs;
import com.ekc.xml.TName;

@Service("euserrank_Ser")
public class UserRankService extends  TableUseAbs {
	@Override
	public String getTable() {
		return TName.eUserRank;
	}

	@Override
	public String getPrimaryKey() {
		return "rank_id";
	}
}
