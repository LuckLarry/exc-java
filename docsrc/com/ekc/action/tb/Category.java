package com.ekc.action.tb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ekc.action.BaseAction;
import com.ekc.ifc.TableUseIfc;

/**
 * 分类
 * 
 * @author GaoHui
 * 
 */

@Controller
@RequestMapping("/tb/category.do")
public class Category extends BaseAction {
	@Autowired
	TableUseIfc eCategory_Ser;

	@Override
	public TableUseIfc getTabelServer() {
		return eCategory_Ser;
	}

	
}
