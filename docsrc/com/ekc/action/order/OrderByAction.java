package com.ekc.action.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ekc.action.BaseAction;
import com.ekc.ifc.TableUseIfc;
/**
* 操作 仓库排序表
* @author ZhengXiajun
* 
*/
@Controller
@RequestMapping("/order/orderby.do")
public class OrderByAction  extends BaseAction{
    @Autowired
    TableUseIfc OrderBySer;
	@Override
	public TableUseIfc getTabelServer() {	
		return OrderBySer;
	}

}
