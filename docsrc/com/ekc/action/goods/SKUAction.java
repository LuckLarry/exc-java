package com.ekc.action.goods;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ekc.action.BaseAction;
import com.ekc.ifc.TableUseIfc;

	/**
	 * 操作商品统一编号信息表
	 * @author ZhengXiajun
	 * 
	 */
@Controller
@RequestMapping("/goods/sku.do")
public class SKUAction extends BaseAction {
	@Autowired
	TableUseIfc SKUSer;
	@Override
	public TableUseIfc getTabelServer() {
		return SKUSer;
	}	
}
