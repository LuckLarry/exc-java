package com.ekc.action.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ekc.action.BaseAction;
import com.ekc.ifc.TableUseIfc;
/**
* 操作消息接收表
* @author ZhengXiajun
* 
*/
@Controller
@RequestMapping("/user/messageReceive.do")
public class MessageReceiveAction extends BaseAction{
	@Autowired
	TableUseIfc messRecService;
	@Override
	public TableUseIfc getTabelServer() {
		return messRecService;
	}	
}
