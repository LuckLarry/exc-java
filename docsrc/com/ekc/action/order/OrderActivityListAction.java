package com.ekc.action.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ekc.action.BaseAction;
import com.ekc.ifc.TableUseIfc;

/**
* 操作 订单中的活动列表
* @author ZhengXiajun
* 
*/
@Controller
@RequestMapping("/order/ordactlist.do")
public class OrderActivityListAction extends BaseAction  {
	@Autowired
	TableUseIfc OALSer;
	@Override
	public TableUseIfc getTabelServer() {
		return  OALSer;
	}		
}
