package com.ekc.service.user;


import org.springframework.stereotype.Service;

import com.ekc.service.TableUseAbs;
import com.ekc.xml.TName;
/**
 *优惠活动信息表
 * 
 * @author ZhengXiajun 对优惠活动信息进行分类
 */
@Service ("activityInfoService")
public class ActivityInfo extends TableUseAbs{
	@Override
	public String getTable() {	
		return TName.eActivityInfo;
	}
	@Override
	public String getPrimaryKey() {
		return "activity_id";
	}
}





