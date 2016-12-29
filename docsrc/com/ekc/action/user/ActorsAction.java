package com.ekc.action.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ekc.action.BaseAction;
import com.ekc.ifc.TableUseIfc;
/**
* 操作 角色表
* @author ZhengXiajun
* 
*/
@Controller
@RequestMapping("/user/actors.do")
public class ActorsAction extends BaseAction {
	@Autowired
	 TableUseIfc actorsService;
	@Override
	public TableUseIfc getTabelServer() {
		return actorsService;
	}		
}
	
	
	
	
	
