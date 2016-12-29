package com.ekc.action.order;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ekc.action.BaseAction;
import com.ekc.enumall.Message;
import com.ekc.ifc.TableUseIfc;
import com.ekc.service.OrderService;
import com.ekc.util.ItemHelper;
import com.ekc.xml.MethodsXml;

/**
 * 操作 订单详细信息表
 * 
 * @author ZhengXiajun
 * 
 */
@Controller
@RequestMapping("/order/ordinfo.do")
public class OrderInfoAction extends BaseAction {
	@Autowired
	TableUseIfc OrderInfoSer;
	@Autowired
	OrderService orderService;

	@Override
	public TableUseIfc getTabelServer() {
		return OrderInfoSer;
	}

	/**
	 * 增加一条记录:66414380
	 * 
	 * @param map
	 * @return <pre>
	 * 	<strong>请求URL：</strong>.../order/ordinfo.do?m=add&amp;date=one&amp;t=2t
	 * 	<strong>ticket:</strong>ticket=?
	 *  <strong>map:</strong>
	 *  {
	 *  	order:[json map格式],
	 *  	skulist:[json list格式]
	 *  }
	 * </pre>
	 */
	@RequestMapping(params = { MethodsXml.add, MethodsXml.dateOne,"t=2t"})
	public Map<String, Object> addOrderInfo(@RequestBody Map<String, Object> map) {
		Map<String, Object> mapRe = Message.SUCCESS.getObjMess();
		try {
			String id = orderService.addSKUList(map);
			if (ItemHelper.isEmpty(id)){
				mapRe.put("order_id", id);
			}else{
				mapRe = Message.UN_KNOW.getObjMess();
			}
		} catch (Exception e) {
			mapRe = Message.UN_KNOW.getObjMess(e);
		}
		return mapRe;
	}
}
