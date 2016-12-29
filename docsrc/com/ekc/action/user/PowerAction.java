package com.ekc.action.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ekc.action.BaseAction;
import com.ekc.ifc.TableUseIfc;
/**
* 操作权限表
* @author ZhengXiajun
* 
*/
@Controller
@RequestMapping("/user/power.do")
public class PowerAction extends BaseAction{
	@Autowired
	TableUseIfc powerService;
	@Override
	public TableUseIfc getTabelServer() {
		return powerService;
	}	
}
