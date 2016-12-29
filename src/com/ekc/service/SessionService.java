package com.ekc.service;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

import com.ekc.xml.TName;

/**
 * 
 * @author pengbei_qq1179009513
 * 
 */
@Service("sessionService")
public class SessionService extends BaseService {
	/**
	 * 插入session表或者修改session
	 * 
	 * @param map
	 * @return
	 * @throws RuntimeException
	 */
	public int insertOrUpdate(Map<String, Object> map) throws RuntimeException {
		Map<String, Object> sessMap = get(map);
		int no = 0;
		if (sessMap != null) {
			no = ref(map);
		} else {
			no = insert(TName.eSessions, map);
		}
		return no;
	}

	/**
	 *  查
	 * @param map
	 * @return
	 */
	public Map<String, Object> get(Map<String, Object> map) {
		Map<String, Object> mapWhere = new HashMap<String, Object>();
		mapWhere.put("user_name", map.get("user_name"));
		return selectRow(TName.eSessions, "user_name,sesskey", mapWhere);
	}

	/**
	 *  改
	 * @param map
	 * @return
	 */
	public int ref(Map<String, Object> map) {
		Map<String, Object> mapWhere = new HashMap<String, Object>();
		mapWhere.put("user_name", map.get("user_name"));
		map.remove("user_name");
		return update(TName.eSessions, map, mapWhere);
	}
}
