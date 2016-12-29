package com.ekc.action.goods;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ekc.action.BaseAction;
import com.ekc.ifc.TableUseIfc;
/**
 * 操作商品品牌表
 * @author ZhengXiajun
 *
 */

@Controller
@RequestMapping("/goods/brand.do")
public class BrandAction extends BaseAction {
	@Autowired
	TableUseIfc  BrandSer;
	@Override
	public TableUseIfc getTabelServer() {
		return  BrandSer;
	}	
}
