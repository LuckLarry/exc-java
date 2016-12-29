package com.ekc.action.tb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ekc.action.BaseAction;
import com.ekc.ifc.TableUseIfc;
/**
 * 商品属性值
 * @author Gaohui
 *
 */

@Controller
@RequestMapping("/tb/gava.do")
public class Goodsattributevalues extends BaseAction {
	@Autowired  
	TableUseIfc  eGoodsAttributeValues_Ser;
	@Override
	public TableUseIfc getTabelServer() {
		return  eGoodsAttributeValues_Ser;
	}	
}
