package com.ekc.service.user;


import org.springframework.stereotype.Service;

import com.ekc.service.TableUseAbs;
import com.ekc.xml.TName;
/**
 * 消息接收表
 * @author ZhengXiajun 
 * @Date 2015-12-12
 *
 */

@Service("messRecService")
public class MessageReceive extends TableUseAbs {
	@Override
	public String getTable() {
		return TName.eMessageReceive;
	}
	@Override
	public String getPrimaryKey() {
		return "receive_id";
	}
}





