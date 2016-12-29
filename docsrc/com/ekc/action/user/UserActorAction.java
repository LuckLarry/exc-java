package com.ekc.action.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ekc.action.BaseAction;
import com.ekc.ifc.TableUseIfc;

/**
* 操作用户角色对照表
* @author ZhengXiajun
* 
*/
@Controller
@RequestMapping("/user/useractor.do")
public class UserActorAction extends BaseAction{
	@Autowired
	TableUseIfc userActorService;
	@Override
	public TableUseIfc getTabelServer() {
		return userActorService;
	}
}
