package com.ekc.action.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ekc.action.BaseAction;
import com.ekc.ifc.TableUseIfc;
/**
* 操作 购物车表
* @author ZhengXiajun
* 
*/
@Controller
@RequestMapping("/user/cart.do")
public class CartAction extends BaseAction {
	@Autowired
	TableUseIfc cartService;
	@Override
	public TableUseIfc getTabelServer() {
		return cartService;
	}		
}
	