package com.ekc.service.user;

import org.springframework.stereotype.Service;

import com.ekc.service.TableUseAbs;
import com.ekc.xml.TName;

/**
 * 序列化后的session数据表
 * @author ZhengXiajun
 *
 */

@Service("sessiService")
public class Sessions extends TableUseAbs{
	@Override
	public String getTable() {
		return TName.eSessions;
	}
	@Override
	public String getPrimaryKey() {
		return "sesskey";
	}
	
}







