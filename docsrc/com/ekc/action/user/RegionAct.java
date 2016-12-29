package com.ekc.action.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ekc.action.BaseAction;
import com.ekc.ifc.TableUseIfc;

/**
* 操作地区数据表
* @author ZhengXiajun
* 
*/
@Controller
@RequestMapping("/user/region.do")
public class RegionAct extends BaseAction {
	@Autowired
	TableUseIfc regionService;
	@Override
	public TableUseIfc getTabelServer() {
		return regionService;
	}	

}
