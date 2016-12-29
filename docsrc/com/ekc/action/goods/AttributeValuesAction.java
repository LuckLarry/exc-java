package com.ekc.action.goods;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ekc.action.BaseAction;
import com.ekc.ifc.TableUseIfc;

/**
* 属性值表
* @author hui
* 
*/
@Controller
@RequestMapping("/goods/ava.do")
public class AttributeValuesAction extends BaseAction {
	@Autowired
	TableUseIfc  WarehouseLogSer;
	@Override
	public TableUseIfc getTabelServer() {
		return WarehouseLogSer;
	}	
}
