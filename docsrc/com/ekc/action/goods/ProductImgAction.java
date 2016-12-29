package com.ekc.action.goods;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ekc.action.BaseAction;
import com.ekc.ifc.TableUseIfc;

/**
* 商品图库表
* @author hui
* 
*/
@Controller
@RequestMapping("/goods/pia.do")
public class ProductImgAction extends BaseAction {
	@Autowired
	TableUseIfc  WarehouseLogSer;
	@Override
	public TableUseIfc getTabelServer() {
		return WarehouseLogSer;
	}	
}
