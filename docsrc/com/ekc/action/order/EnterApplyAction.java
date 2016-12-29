package com.ekc.action.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ekc.action.BaseAction;
import com.ekc.ifc.TableUseIfc;

/**
* 操作入驻申请表
* @author ZhengXiajun
* 
*/
@Controller
@RequestMapping("/order/enterapply.do")
public class EnterApplyAction extends BaseAction {
	@Autowired
	TableUseIfc  EnterApplySer;
	@Override
	public TableUseIfc getTabelServer() {
		return EnterApplySer;
	}	
}
