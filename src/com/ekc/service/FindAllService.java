package com.ekc.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.ekc.util.CreatePrimaryKey;
import com.ekc.xml.TName;

@Service("goodsApplyService")
public class FindAllService extends BaseService{
	public int setGoodsApply(Map<String, Object> accMap){
		accMap.put("apply_id", CreatePrimaryKey.createKey("00", "000"));
		return insert(TName.eFindOrCustomGoodsApply,accMap);
	}
	
	public Map<String, Object> getGoodsApplyInfo(Map<String, Object> Map)
			throws IllegalArgumentException, IllegalThreadStateException,
			IllegalAccessException {		
		return selectPage(TName.eFindOrCustomGoodsApply, "name,telephone,qq,province,city,district",Map);
	}	
	
}
