package com.ekc.action.goods;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ekc.action.BaseAction;
import com.ekc.ifc.TableUseIfc;

/**
 * 操作商品属性值表
 * @author ZhengXiajun
 *
 */
@Controller
@Deprecated
@RequestMapping("/goods/gdsattval.do")
public class GoodsAttributesValuaAction  extends BaseAction {
	@Autowired
	TableUseIfc  GAVSer;
	@Override
	public TableUseIfc getTabelServer() {
		return  GAVSer;
	}	
}
