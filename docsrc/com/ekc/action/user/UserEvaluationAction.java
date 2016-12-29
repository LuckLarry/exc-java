package com.ekc.action.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ekc.action.BaseAction;
import com.ekc.ifc.TableUseIfc;

/**
* 操作用户评价表
* @author ZhengXiajun
* 
*/
@Controller
@RequestMapping("/user/usereva.do")
public class UserEvaluationAction extends BaseAction {
	@Autowired
	TableUseIfc  userEvaService;
	@Override
	public TableUseIfc getTabelServer() {
		return userEvaService;
	}
}
