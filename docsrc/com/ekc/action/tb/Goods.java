package com.ekc.action.tb;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekc.action.BaseAction;
import com.ekc.enumall.Message;
import com.ekc.ifc.TableUseIfc;
import com.ekc.xml.MethodsXml;

/**
 * 商品
 * 
 * @author Gaohui
 * 
 */

@Controller
@RequestMapping("/tb/goods.do")
public class Goods extends BaseAction {
	@Autowired
	TableUseIfc eGoods_Ser;

	@Override
	public TableUseIfc getTabelServer() {
		return eGoods_Ser;
	}

	/**
	 * 增加一条记录 主键id值为进行重写
	 * 
	 * @param pkfiled
	 *            主键
	 * @param map
	 *            添加信息
	 * @return <pre>
	 * 	<strong>请求URL：</strong>...?m=add&amp;date=one&amp;obj=pk
	 * 	<strong>ticket:</strong>ticket=?
	 *  <strong>map:</strong>{[json格式]}
	 * </pre>
	 */
	@RequestMapping(params = { MethodsXml.add, MethodsXml.dateOne,
			MethodsXml.objPK })
	public @ResponseBody
	Map<String, Object> addRowOnlyId(@RequestBody Map<String, Object> map) {
		Map<String, Object> mapRe = Message.SUCCESS.getObjMess();
		try {
			if(map.containsKey("goods_title2")) {
				map.remove("goods_title2");
			}
			int num = getTabelServer().addRowOnlyId(map);
			if (num == 0) {
				mapRe = Message.UNTREATED.getObjMess();
			}
		} catch (Exception e) {
			e.printStackTrace();
			mapRe = Message.UN_KNOW.getObjMess(e);
		}
		return mapRe;
	}
}
