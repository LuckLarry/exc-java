package com.ekc.service.user;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.ekc.service.TableUseAbs;
import com.ekc.xml.TName;

@Service("userphoneSer")
public class UserPhoneService extends TableUseAbs {

	@Override
	public String getTable() {
		return TName.eUserPhone;
	}

	@Override
	public String getPrimaryKey() {
		return "pk_id";
	}
	public int addRow(Map<String, Object> map) throws Exception{		
		return insert(getTable(), map);
	}
}
