package com.ekc.action.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ekc.action.BaseAction;
import com.ekc.ifc.TableUseIfc;
/**
* 操作 订单仓库表
* @author ZhengXiajun
* 
*/
@Controller
@RequestMapping("/order/ordercang.do")
public class OrderCangAction extends BaseAction{
    @Autowired
    TableUseIfc OrderCangSer;
	@Override
	public TableUseIfc getTabelServer() {
		return OrderCangSer;
	}

}
