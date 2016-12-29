package com.ekc.service.tb;

import org.springframework.stereotype.Service;

import com.ekc.service.TableUseAbs;
import com.ekc.xml.TName;

@Service("rolePower_Ser")
public class RolePowerService extends  TableUseAbs {
	@Override
	public String getTable() {
		return TName.eRolePower;
	}

	@Override
	public String getPrimaryKey() {
		return "Role_power_id";
	}
}
