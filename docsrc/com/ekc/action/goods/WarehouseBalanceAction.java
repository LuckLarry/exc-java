package com.ekc.action.goods;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ekc.action.BaseAction;
import com.ekc.ifc.TableUseIfc;

/**
* 操作仓库库存期间余额表
* @author ZhengXiajun
* 
*/
@Controller
@RequestMapping("/goods/whb.do")
public class WarehouseBalanceAction extends BaseAction {
	@Autowired
	TableUseIfc  WBSer;
	@Override
	public TableUseIfc getTabelServer() {
		return WBSer;
	}	
}
