package com.ekc.action.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ekc.action.BaseAction;
import com.ekc.ifc.TableUseIfc;
/**
* 操作 订单对应的商品信息
* @author ZhengXiajun
* 
*/
@Controller
@RequestMapping("/order/ordskulist.do")
public class OrderSKUListAction  extends BaseAction {
	@Autowired
	TableUseIfc  OSKULSer;
	@Override
	public TableUseIfc getTabelServer() {
		return OSKULSer;
	}	
}
