package com.ekc.action.goods;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ekc.action.BaseAction;
import com.ekc.ifc.TableUseIfc;

/**
 * 操作仓库区位信息表
 * @author ZhengXiajun
 * 
 */
@Controller
@RequestMapping("/goods/str.do")
public class StoragesAction extends BaseAction {
	@Autowired
	TableUseIfc StoragesSer;
	@Override
	public TableUseIfc getTabelServer() {
		return  StoragesSer;
	}	
}
