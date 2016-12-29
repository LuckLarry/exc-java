package com.ekc.action.tb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ekc.action.BaseAction;
import com.ekc.ifc.TableUseIfc;
/**
 * 品牌
 * @author Gaohui
 *
 */

@Controller
@RequestMapping("/tb/brand.do")
public class Brand extends BaseAction {
	@Autowired
	TableUseIfc  eBrand_Ser;
	@Override
	public TableUseIfc getTabelServer() {
		return  eBrand_Ser;
	}	
}
