package com.ekc.action.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ekc.action.BaseAction;
import com.ekc.ifc.TableUseIfc;

/**
* 操作 工程报价申请表
* @author ZhengXiajun
* 
*/
@Controller
@RequestMapping("/order/proapply.do")
public class ProjectOfferApplyAction extends BaseAction {
	@Autowired
	TableUseIfc  POASer;
	@Override
	public TableUseIfc getTabelServer() {
		return POASer;
	}	
}
