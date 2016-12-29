package com.ekc.action.goods;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ekc.action.BaseAction;
import com.ekc.ifc.TableUseIfc;

/**
 * 操作商品属性表
 * @author ZhengXiajun
 *
 */
@Controller
@RequestMapping("/goods/attributes.do")
public class AttributesAction extends BaseAction {
	@Autowired
	 TableUseIfc  AttributesSer;
	@Override
	public TableUseIfc getTabelServer() {
		return AttributesSer;
	}	
}