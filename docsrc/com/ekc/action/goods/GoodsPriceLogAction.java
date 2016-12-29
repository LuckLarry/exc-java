package com.ekc.action.goods;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ekc.action.BaseAction;
import com.ekc.ifc.TableUseIfc;

/**
 * 操作商品价格操作日志表
 * @author ZhengXiajun
 * 
 */
@Controller
@RequestMapping("/goods/gdsprilog.do")
public class GoodsPriceLogAction  extends BaseAction {
	@Autowired
	TableUseIfc  GoodsPriceLogSer;
	@Override
	public TableUseIfc getTabelServer() {
		return GoodsPriceLogSer;
	}	
}
