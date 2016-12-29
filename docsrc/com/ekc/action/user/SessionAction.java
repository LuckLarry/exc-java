package com.ekc.action.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ekc.action.BaseAction;
import com.ekc.ifc.TableUseIfc;

/**
* 操作会话记录表
* @author ZhengXiajun
* 
*/
@Controller
@RequestMapping("/user/session.do")
public class SessionAction extends BaseAction {
	@Autowired
	TableUseIfc sessiService;
	@Override
	public TableUseIfc getTabelServer() {
		return sessiService;
	}	
}
