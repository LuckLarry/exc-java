package com.ekc.service.user;

import org.springframework.stereotype.Service;

import com.ekc.service.TableUseAbs;
import com.ekc.xml.TName;

/**
 *消息表
 * 
 * @author ZhengXiajun  对消息信息进行分类
 */
@Service("messInfoService")
public class MessageInfo extends TableUseAbs {
	@Override
	public String getTable() {
		return TName.eMessageInfo;
	}
	@Override
	public String getPrimaryKey() {
		return "message_id";
	}
}
 



