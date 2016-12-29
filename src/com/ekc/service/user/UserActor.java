package com.ekc.service.user;

import org.springframework.stereotype.Service;

import com.ekc.service.TableUseAbs;
import com.ekc.xml.TName;

/**
 * 用户角色对照表
 * @author ZhengXiajun
 *
 */

@Service("userActorService")
public class UserActor extends TableUseAbs {
	@Override
	public String getTable() {
		return TName.eUserActors;
	}
	@Override
	public String getPrimaryKey() {
		return "user_actor_id";
	}
	
}

	