package com.ekc.action.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ekc.action.BaseAction;
import com.ekc.ifc.TableUseIfc;

/**
* 操作 物流状态表
* @author ZhengXiajun
* 
*/
@Controller
@RequestMapping("/order/logistatus.do")
public class LogisticsStatusAction extends BaseAction {
	@Autowired
	TableUseIfc LogisticsStatusSer;
	@Override
	public TableUseIfc getTabelServer() {
		return LogisticsStatusSer;
	}	
}
