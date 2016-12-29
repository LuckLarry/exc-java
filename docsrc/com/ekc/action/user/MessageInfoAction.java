package com.ekc.action.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ekc.action.BaseAction;
import com.ekc.ifc.TableUseIfc;
/**
* 操作消息表
* @author ZhengXiajun
* 
*/
@Controller
@RequestMapping("/user/messageInfo.do")
public class MessageInfoAction extends BaseAction {
	@Autowired
	TableUseIfc messInfoService;
	@Override
	public TableUseIfc getTabelServer() {
		return messInfoService;
	}		
}
