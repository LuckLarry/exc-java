package com.ekc.action.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ekc.action.BaseAction;
import com.ekc.ifc.TableUseIfc;
/**
* 操作 优惠活动表
* @author ZhengXiajun
* 
*/
@Controller
@RequestMapping("/user/actinfo.do")
public class ActivityInfoAction extends BaseAction {
	@Autowired
	TableUseIfc  activityInfoService;
	@Override
	public TableUseIfc getTabelServer() {		
		return activityInfoService;
	}
}
