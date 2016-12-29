package com.ekc.action.tb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ekc.action.BaseAction;
import com.ekc.ifc.TableUseIfc;

/**
 * 属性值
 * 
 * @author Gaohui
 * 
 */

@Controller
@RequestMapping("/tb/ava.do")
public class Attributevalues extends BaseAction {
	@Autowired
	TableUseIfc eAttributeValues_Ser;

	@Override
	public TableUseIfc getTabelServer() {
		return eAttributeValues_Ser;
	}
	
}
