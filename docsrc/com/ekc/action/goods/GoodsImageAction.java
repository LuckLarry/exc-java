package com.ekc.action.goods;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ekc.action.BaseAction;
import com.ekc.ifc.TableUseIfc;

/**
 * 操作商品图库表
 * @author ZhengXiajun
 *
 */
@Controller
@RequestMapping("/goods/gdsimg.do")
public class GoodsImageAction extends BaseAction {
	@Autowired
	TableUseIfc  GoodsImageSer;
	@Override
	public TableUseIfc getTabelServer() {
		return GoodsImageSer;
	}	
}
