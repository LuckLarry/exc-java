package com.ekc.action.tb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ekc.action.BaseAction;
import com.ekc.ifc.TableUseIfc;
/**
 * SKU
 * 
 * @author Gaohui
 * 
 */

@Controller
@RequestMapping("/tb/sku.do")
public class SKU extends BaseAction {
	@Autowired
	TableUseIfc eSKU_Ser;

	@Override
	public TableUseIfc getTabelServer() {
		return eSKU_Ser;
	}
}
