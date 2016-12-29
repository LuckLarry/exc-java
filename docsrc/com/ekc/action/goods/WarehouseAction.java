package com.ekc.action.goods;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ekc.action.BaseAction;
import com.ekc.ifc.TableUseIfc;

/**
* 操作仓库库存信息表
* @author ZhengXiajun
* 
*/
@Controller
@RequestMapping("/goods/wh.do")
public class WarehouseAction extends BaseAction {
	@Autowired
	TableUseIfc WarehousesSer;
	@Override
	public TableUseIfc getTabelServer() {
		return WarehousesSer;
	}	
}
