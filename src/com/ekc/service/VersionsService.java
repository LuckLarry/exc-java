package com.ekc.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.ekc.util.CreatePrimaryKey;
import com.ekc.xml.TName;

@Service("vService")
public class VersionsService extends BaseService {
	// 插入本版号
	public int addVersions(Map<String, Object> accMap) {
		accMap.put("version_id", CreatePrimaryKey.createKey("00", "000"));
		return insert(TName.eversions, accMap);
	}
	
	public Map<String, Object> getVersions(Map<String, Object> whereMap) {
		Map<String, Object> vMap = selectRow(TName.eversions, "version_id,version_name,version_num,version_des",
				whereMap);
		return vMap;
	}
}
