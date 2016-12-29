package com.ekc.action.order;

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



@Controller
@RequestMapping("/order/orderskulist.do")
public class OrderGoodsPriceAction  extends BaseAction {
	@Autowired
	TableUseIfc  OrderSkuList_Ser;
	@Override
	public TableUseIfc getTabelServer() {
		return OrderSkuList_Ser;
	}		

	@RequestMapping(params = {  MethodsXml.find, MethodsXml.dateOne})
	public @ResponseBody
	Map<String, Object> findRow(@RequestBody Map<String ,Object>mapPK) {
		Map<String, Object> mapRe = Message.SUCCESS.getObjMess();
		Map<String, Object> map = null;
		try {
			map = getTabelServer().findRow(mapPK);
			if (map == null || map.size() == 0) {
				mapRe = Message.NO_DATA.getObjMess();
			} else {
				mapRe.put("goods_price",map.get("goods_price"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			mapRe = Message.UN_KNOW.getObjMess(e);
		}
		return mapRe;
	}
}
