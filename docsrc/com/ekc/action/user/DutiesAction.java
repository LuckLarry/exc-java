package com.ekc.action.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ekc.action.BaseAction;
import com.ekc.ifc.TableUseIfc;
/**
* 操作职责表
* @author ZhengXiajun
* 
*/
public class DutiesAction {
	@Controller
	@RequestMapping("/user/duties.do")
	public class CartAction extends BaseAction {
		@Autowired
		TableUseIfc dutiesService;
		@Override
		public TableUseIfc getTabelServer() {
			return dutiesService;
		}		
	}
}
