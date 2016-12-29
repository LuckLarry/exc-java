package com.ekc.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.ekc.util.CreatePrimaryKey;
import com.ekc.xml.TName;

@Service("projectService")
public class ProjectOfferService extends BaseService {

	public int setProjectOffer(Map<String, Object> accMap) {
		accMap.put("apply_id", CreatePrimaryKey.createKey("00", "000"));
		return insert(TName.eProjectOfferApply, accMap);
	}
	
	public Map<String, Object> getProjectOfferInfo(Map<String, Object> Map)
			throws IllegalArgumentException, IllegalThreadStateException,
			IllegalAccessException {		
		return selectPage(TName.eProjectOfferApply, "name,telephone,qq,province,city,district",Map);
	}
}
