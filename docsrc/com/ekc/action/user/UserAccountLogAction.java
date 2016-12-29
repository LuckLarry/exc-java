package com.ekc.action.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ekc.action.BaseAction;
import com.ekc.ifc.TableUseIfc;
/**
* 操作用户账目日志表
* @author ZhengXiajun
* 
*/

@Controller
@RequestMapping("/user/ual.do")
public class UserAccountLogAction extends BaseAction{
	@Autowired
	TableUseIfc userAccLogService;
	@Override
	public TableUseIfc getTabelServer() {
		return userAccLogService;
	}
}
