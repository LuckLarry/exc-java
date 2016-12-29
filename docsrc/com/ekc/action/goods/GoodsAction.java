package com.ekc.action.goods;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekc.action.BaseAction;
import com.ekc.enumall.Message;
import com.ekc.ifc.TableUseIfc;
import com.ekc.xml.MessageXml;
import com.ekc.xml.MethodsXml;

/**
 * 操作商品基本信息表
 * @author ZhengXiajun
 *
 */
@Controller
@RequestMapping("/goods/goods.do")
public class GoodsAction extends  BaseAction {
	@Autowired
	TableUseIfc  GoodsSer;
	@Override
	public TableUseIfc getTabelServer() {
		return  GoodsSer;
	}
	
	/**
	 * 分页查询SKU列表,过滤属性列表
	 * URL : ...?m=get&date=page&g=h
	 * @param map
	 * @return 
	 */
	@RequestMapping(params = { MethodsXml.find, MethodsXml.datePage, "g=h"})
	public @ResponseBody
	Map<String, Object> findPage1(@RequestBody Map<String, Object> map) {
		Map<String, Object> mapRe = Message.SUCCESS.getObjMess();
		Map<String, Object> mapData = null;
		try {
			mapData = null;
			mapRe.put(MessageXml.data_key, mapData);
		} catch (Exception e) {
			e.printStackTrace();
			mapRe = Message.UN_KNOW.getObjMess(e);
		}
		return mapRe;
	}
}
