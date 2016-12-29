package com.ekc.action.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ekc.action.BaseAction;
import com.ekc.ifc.TableUseIfc;

/**
* 操作 来样/找货/订制申请表
* @author ZhengXiajun
* 
*/
@Controller
@RequestMapping("/order/actor.do")
public class FindOrCustomGoodsApplyAction extends BaseAction {
	@Autowired
	TableUseIfc FOCGASer;
	@Override
	public TableUseIfc getTabelServer() {
		return FOCGASer;
	}	
}
