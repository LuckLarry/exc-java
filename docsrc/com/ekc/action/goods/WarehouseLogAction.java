package com.ekc.action.goods;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ekc.action.BaseAction;
import com.ekc.ifc.TableUseIfc;

/**
* 操作仓库库存日志表
* @author ZhengXiajun
* 
*/
@Controller
@RequestMapping("/goods/whl.do")
public class WarehouseLogAction extends BaseAction {
	@Autowired
	TableUseIfc  WarehouseLogSer;
	@Override
	public TableUseIfc getTabelServer() {
		return WarehouseLogSer;
	}	
}
