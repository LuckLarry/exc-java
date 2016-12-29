package com.ekc.action.goods;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ekc.action.BaseAction;
import com.ekc.ifc.TableUseIfc;

/**
 * 操作商品分类对照表
 * @author ZhengXiajun
 *
 */
@Controller
@RequestMapping("/goods/gdsctg.do")
public class GoodsCategoryAction  extends BaseAction {
	@Autowired
	TableUseIfc  GoodsCategorySer;
	@Override
	public TableUseIfc getTabelServer() {
		return GoodsCategorySer;
	}	
}
