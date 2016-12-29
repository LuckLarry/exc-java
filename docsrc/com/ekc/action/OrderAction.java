package com.ekc.action;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekc.enumall.Message;
import com.ekc.ifc.TableUseIfc;
import com.ekc.service.OrderService;
import com.ekc.util.CreatePrimaryKey;
import com.ekc.util.ItemHelper;
import com.ekc.xml.MessageXml;
import com.ekc.xml.MethodsXml;

/**
 * 订单类
 * 
 * @author hui
 * 
 */
@Controller
@RequestMapping("/order.do")
public class OrderAction {
	@Autowired
	OrderService orderService;
	@Autowired
	TableUseIfc OSKULSer;
	@Autowired
	TableUseIfc OrderInfoSer;
	@Autowired
	TableUseIfc erppriceService;
	/**
	 * 添加订单
	 * 
	 * @param order
	 *            <pre>
	 * { 
	 * 		"order_sn":"订单号,唯一",
	 * 		"user_id":"用户id,同e_Users的user_id",
	 * 		"order_status":"订单的状态;0未确认,1确认,2已取消,3无效,4退货",
	 * 		"shipping_status":"商品配送情况;0未发货,1已发货,2已收货,4退货",
	 * 		"pay_status":"支付状态;0未付款;1付款中;2已付款",
	 * 		"address_id":"收货信息ID",
	 * 		"postscript":"订单附言,由用户提交订单前填写",
	 * 		"shipping_id":"用户选择的配送方式id,取值表shipping",
	 * 		"shipping_name":"用户选择的配送方式的名称,取值表shipping",
	 * 		"pay_id":"用户选择的支付方式的id,取值表payment",
	 * 		"pay_name":"用户选择的支付方式名称,取值表payment",
	 * 		"how_out_of_stock":"缺货处理方式,等待所有商品备齐后再发,取消订单;与店主协商",
	 * 		"how_surplus":"根据字段猜测应该是余额处理方式,程序未作这部分实现",
	 * 		"goods_amount":"商品的总金额",
	 * 		"shipping_fee":"配送费用",
	 * 		"pay_fee":"支付费用,跟支付方式的配置相关,取值表payment",
	 * 		"money_paid":"已付款金额",
	 * 		"surplus":"该订单使用金额的数量,取用户设定余额,用户可用余额,订单金额中最小者",
	 * 		"integral":"使用的积分的数量,取用户使用积分,商品可用积分,用户拥有积分中最小者",
	 * 		"integral_money":"使用积分金额",
	 * 		"order_amount":"应付款金额",
	 * 		"referer":"订单的来源页面",
	 * 		"add_time":"订单生成时间",
	 * 		"confirm_time":"订单确认时间",
	 * 		"pay_time":"订单支付时间",
	 * 		"shipping_time":"订单配送时间",
	 * 		"invoice_number":"发货时填写,可在订单查询查看",
	 * 		"extension_code":"通过活动购买的商品的代号,group_buy是团购;auction是拍卖;snatch夺宝奇兵;正常普通产品该处理为空",
	 * 		"extension_id":"通过活动购买的物品id,取值ecs_good_activity;如果是正常普通商品,该处为0",
	 * 		"to_buyer":"商家给客户的留言,当该字段值时可以在订单查询看到",
	 * 		"pay_note":"付款备注,在订单管理编辑修改",
	 * 		"discount":"优惠价钱",
	 * 		"mobile_pay":"是否手机端支付",
	 * 		"recommend_number":"推荐码",
	 * 		"mobile_order":"是否移动端订单",
	 * 		"weixin_out_trade_number":"微信支付号",
	 * 		"abc_pay_order_sn":"农业银行支付号",
	 * 		"old_id":"" 
	 * }
	 * </pre>
	 * @return <pre>
	 * 添加 成功 or 未知错误
	 * </pre>
	 * 
	 *         <h6>例句</h6>
	 * 
	 *         <pre>
	 * <strong>请求 URL：</strong>http://api.llhome.com/order.do?m=add
	 * <strong>JSON 参数：</strong> 
	 * {
	 * 	"order_sn":"2015061901402",
	 * 	"user_id":"00-000-20151102063348000063-0062",
	 * 	"order_status":"1",
	 * 	"shipping_status":"0",
	 * 	"pay_status":"0",
	 * 	"address_id":"",
	 * 	"postscript":"",
	 * 	"shipping_id":"1",
	 * 	"shipping_name":"顺丰速运",
	 * 	"pay_id":"1",
	 * 	"pay_name":"支付宝",
	 * 	"how_out_of_stock":"等待所有商品备齐后再发",
	 * 	"how_surplus":"",
	 * 	"goods_amount":"16987.5",
	 * 	"shipping_fee":"5",
	 * 	"pay_fee":"0",
	 * 	"money_paid":"0",
	 * 	"surplus":"0",
	 * 	"integral":"0",
	 * 	"integral_money":"0",
	 * 	"order_amount":"16992.5",
	 * 	"referer":"管理员添加",
	 * 	"add_time":"0000-00-00 00:00:00",
	 * 	"confirm_time":"0000-00-00 00:00:00",
	 * 	"pay_time":"0000-00-00 00:00:00",
	 * 	"shipping_time":"0000-00-00 00:00:00",
	 * 	"invoice_number":"0000-00-00 00:00:00",
	 * 	"extension_code":"",
	 * 	"extension_id":"0",
	 * 	"to_buyer":"",
	 * 	"pay_note":"",
	 * 	"discount":"0",
	 * 	"mobile_pay":"0",
	 * 	"recommend_number":"",
	 * 	"mobile_order":"0",
	 * 	"weixin_out_trade_number":"",
	 * 	"abc_pay_order_sn":"",
	 * 	"old_id":"377",
	 *  "order_name":"new field",
	 * 	"order_des":"new field"
	 * }
	 * <strong>Headers 参数：</strong>ticket = 45feb928-3ff2-4f98-a4a2-b9ebb3899992
	 * <strong>返回结果：</strong>
	 * {
	 *   "message": "success",
	 *   "messageTxt": "成功",
	 *   "code": 0
	 * }
	 * </pre>
	 */
	@RequestMapping(params = MethodsXml.add)
	public @ResponseBody
	Map<String, Object> add(@RequestBody Map<String, Object> order) {
		Map<String, Object> mapRe = Message.SUCCESS.getObjMess();
		try {
			String order_sn = "order_sn";//订单号,唯一
			String order_sn_value = ItemHelper.createOrderSN();//订单号,唯一
			order.put(order_sn, order_sn_value);
			
			String pk = "order_id";
			String pk_value = CreatePrimaryKey.createKey("00", "000"); 
			order.put(pk, pk_value);
			int no = orderService.add(order);
			if (no <= 0) {
				mapRe = Message.UNTREATED.getObjMess();
			} else {
				mapRe.put(pk, pk_value);
			}
		} catch (Exception e) {
			e.printStackTrace();
			mapRe = Message.UN_KNOW.getObjMess(e);
		}
		return mapRe;
	}

	/**
	 * 查询多条订单数据 分页
	 * 
	 * @param order
	 *            <pre>
	 * {
	 * 	["page_size": 每页显示数据条数,]
	 * 	["page": 当前页码]
	 * }
	 * </pre>
	 * @return 订单全部数据
	 * 
	 *         <h6>例子：</h6>
	 * 
	 *         <pre>
	 * 请求 URL：http://api.llhome.com/order.do?m=get&amp;date=page
	 * JSON 参数：{"page_size": 1,"page": 1}
	 * Headers 参数：ticket = 45feb928-3ff2-4f98-a4a2-b9ebb3899992
	 * 返回结果：
	 * {
	 *   "message": "success",
	 *   "messageTxt": "成功",
	 *   "pageInfo": {
	 *     "page_size": 1,
	 *     "page": 1,
	 *     "list": [
	 *       {
	 *         "order_id": "00-000-20151102033730000001-0001",
	 *         "order_sn": "2015080371284",
	 *         "user_id": "00-000-20151102063348000062-0061",
	 *         "order_status": false,
	 *         "shipping_status": false,
	 *         "pay_status": false,
	 *         "address_id": "",
	 *         "postscript": "",
	 *         "shipping_id": "0",
	 *         "shipping_name": "",
	 *         "pay_id": "29",
	 *         "pay_name": "微信支付",
	 *         "how_out_of_stock": "",
	 *         "how_surplus": "",
	 *         "goods_amount": 52.71,
	 *         "shipping_fee": 25,
	 *         "pay_fee": 0,
	 *         "money_paid": 0,
	 *         "surplus": 0,
	 *         "integral": 0,
	 *         "integral_money": 0,
	 *         "order_amount": 77.71,
	 *         "referer": "本站",
	 *         "add_time": null,
	 *         "confirm_time": null,
	 *         "pay_time": null,
	 *         "shipping_time": null,
	 *         "invoice_number": "",
	 *         "extension_code": "",
	 *         "extension_id": "0",
	 *         "to_buyer": "",
	 *         "pay_note": "",
	 *         "discount": 0,
	 *         "mobile_pay": 0,
	 *         "recommend_number": "",
	 *         "mobile_order": 0,
	 *         "weixin_out_trade_number": "123256380220150803155800",
	 *         "abc_pay_order_sn": "",
	 *         "old_id": "213",
	 *         "order_name": "",
	 *         "order_des": "",
	 *         "sku_id": "600*600",
	 *         "goods_id": "00-000-20151102125614000003-0003",
	 *         "goods_name": "KIKI 瓷片 配套瓷片1 SA9530Y1 70x600",
	 *         "goods_sn": "SA9530Y1",
	 *         "goods_digit": 1,
	 *         "goods_price": 30.39,
	 *         "goods_money": 30.39,
	 *         "b_old_id": "1"
	 *       }
	 *     ],
	 *     "rowCount": 215,
	 *     "page_count": 215
	 *   },
	 *   "code": 0
	 * }
	 * </pre>
	 */
	@RequestMapping(params = { MethodsXml.find, MethodsXml.datePage })
	public @ResponseBody
	Map<String, Object> get(@RequestBody Map<String, Object> order) {
		Map<String, Object> mapRe = Message.SUCCESS.getObjMess();
		try {
			mapRe.put(MessageXml.pageInfo_key, orderService.get(order));
		} catch (Exception e) {
			e.printStackTrace();
			mapRe = Message.UN_KNOW.getObjMess(e);
		}
		return mapRe;
	}

	/**
	 * 取消一条订单数据(删除)
	 * 
	 * @param order
	 *            <pre>
	 * {
	 * 	"order_sn":"订单号,唯一",
	 * 	"address_id":"收货信息ID"
	 * }
	 * </pre>
	 * @return <pre>
	 * 
	 * </pre>
	 * 
	 *         <h6>例子</h6>
	 * 
	 *         <pre>
	 * 请求 URL：http://api.llhome.com/order.do&amp;m=del
	 * JSON 参数：
	 * { 
	 * 	"order_sn":"",
	 * 	"address_id":""
	 * }
	 * Headers 参数：ticket = b4289dcb-4825-4bd5-bd10-39d1128e30ee
	 * 返回取消成功结果:
	 * {
	 *   "message": "success",
	 *   "messageTxt": "成功",
	 *   "code": 0
	 * }
	 * 返回未处理结果：
	 * {
	 *   "message": "untreated",
	 *   "messageTxt": "未处理",
	 *   "code": 5
	 * }
	 * </pre>
	 */
	@RequestMapping(params = MethodsXml.delete)
	public @ResponseBody
	Map<String, Object> del(@RequestBody Map<String, Object> order) {
		Map<String, Object> mapRe = Message.SUCCESS.getObjMess();
		try {
			int no = orderService.del(order);
			if (no <= 0) {
				mapRe = Message.UNTREATED.getObjMess();
			}
		} catch (Exception e) {
			e.printStackTrace();
			mapRe = Message.UN_KNOW.getObjMess(e);
		}
		return mapRe;
	}

	/**
	 * 添加一条数据到购物车
	 * 
	 * @param request
	 * @param cart
	 *            <pre>
	 * {
	 * 	"cart_id":"自增id号",
	 * 	"user_id":"用户登录ID;取自session","session_id":"如果该用户退出,该Session_id对应的购物车中所有记录都将被删除",
	 * 	"goods_id":"商品的ID,取自表goods的goods_id",
	 * 	"goods_serial_number":"商品的货号,取自表goods的goods_sn",
	 * 	"goods_name":"商品名称,取自表goods的goods_name",
	 * 	"market_price":"商品的本店价,取自表市场价",
	 * 	"goods_price":"商品的本店价,取自表goods的shop_price",
	 * 	"goods_number":"商品的购买数量,在购物车时,实际库存不减少",
	 * 	"goods_attribute":"商品的扩展属性,取自ecs_goods的extension_code",
	 * 	"is_real":"取自ecs_goods的is_real",
	 * 	"extension_code":"商品的扩展属性,取自ecs_goods的extension_code",
	 * 	"Cart_type":"购物车商品类型;0普通;1团够;2拍卖;3夺宝奇兵",
	 * 	"can_handsel":"能否处理",
	 * 	"goods_attribute_id":"该商品的属性的id,取自goods_attr的goods_attr_id,如果有多个,只记录了最后一个,可能是bug",
	 * 	"is_shipping":"商品是否需要运输",
	 * 	"goods_thumb":"商品缩略图",
	 * 	"warehouse_id":"Erp仓库id"
	 * }
	 * </pre>
	 * @return <pre>
	 * 添加 成功 or 未知错误
	 * </pre>
	 * 
	 *         <h6>例子</h6>
	 * 
	 *         <pre>
	 * 请求 URL：http://api.llhome.com/order.do?m=add&amp;obj=cart
	 * JSON 参数：
	 * {
	 * 	"cart_id":"00-000-20151110223515820-7943497",
	 * 	"user_id":"00-000-20151102063348000004-0004",
	 * 	"session_id":"b24e6e53-7b66-455a-b52e-0fb162bb6bc3",
	 * 	"goods_id":"00-000-20151102125614000002-0002",
	 * 	"goods_serial_number":"",
	 * 	"goods_name":"",
	 * 	"market_price":"0",
	 * 	"goods_price":"0",
	 * 	"goods_number":"0",
	 * 	"goods_attribute":"",
	 * 	"is_real":"0",
	 * 	"extension_code":"",
	 * 	"Cart_type":"0",
	 * 	"can_handsel":"0",
	 * 	"goods_attribute_id":"",
	 * 	"is_shipping":"0",
	 * 	"goods_thumb":"0",
	 * 	"warehouse_id":"0"
	 * }
	 * Headers 参数：ticket = b4289dcb-4825-4bd5-bd10-39d1128e30ee
	 * 返回结果：
	 * {
	 * 	"message": "success",
	 * 	"messageTxt": "成功",
	 * 	"code": 0
	 * }
	 * 
	 * </pre>
	 */
	@RequestMapping(params = { MethodsXml.add, MethodsXml.objCart })
	public @ResponseBody
	Map<String, Object> addCart(HttpServletRequest request,
			@RequestBody Map<String, Object> cart) {
		Map<String, Object> repMap = Message.SUCCESS.getObjMess();
		try {
			String ticket = request.getHeader(MessageXml.ticket_key);
			cart.put("session_id", ticket);// 添加到对应的信息
			int no = orderService.addCart(cart);
			if (no <= 0) {
				repMap = Message.UNTREATED.getObjMess();
			}
		} catch (Exception e) {
			e.printStackTrace();
			repMap = Message.UN_KNOW.getObjMess(e);
		}
		return repMap;
	}

	/**
	 * 得到购物车订单详细信息
	 * 
	 * @param request
	 * @param accMap
	 *            <pre>
	 * {
	 * 	"user_id":用户ID,
	 * 	["page_size": 每页显示数据条数,]
	 * 	["page": 当前页码]
	 * }
	 * </pre>
	 * @return 当前用户购物车里的订单详细信息
	 * 
	 *         <h6>例句</h6>
	 * 
	 *         <pre>
	 * 请求 URL：http://api.llhome.com/order.do?m=get&amp;date=page&amp;obj=cart
	 * JSON 参数：{"user_id":"00-000-20151102063348000004-0004","page_size": 1,"page": 1}
	 * Headers 参数：ticket = b4289dcb-4825-4bd5-bd10-39d1128e30ee
	 * 返回结果：
	 * {
	 *   "message": "success",
	 *   "messageTxt": "成功",
	 *   "pageInfo": {
	 *     "page_size": 20,
	 *     "page": 1,
	 *     "list": [
	 *       {
	 *         "cart_id": "00-000-20151110223515820-7943497",
	 *         "user_id": "00-000-20151102063348000004-0004",
	 *         "session_id": "b4289dcb-4825-4bd5-bd10-39d1128e30ee",
	 *         "goods_id": "00-000-20151102125614000002-0002",
	 *         "goods_serial_number": null,
	 *         "goods_name": null,
	 *         "market_price": 0,
	 *         "goods_price": 0,
	 *         "goods_number": 0,
	 *         "goods_attribute": null,
	 *         "is_real": false,
	 *         "extension_code": null,
	 *         "Cart_type": false,
	 *         "can_handsel": 0,
	 *         "goods_attribute_id": null,
	 *         "is_shipping": false,
	 *         "goods_thumb": 0,
	 *         "warehouse_id": "0",
	 *         "dataInfo": {
	 *           "sku_id": "00-000-20151105051450000011-0001",
	 *           "sku_name": "{'00-000-20151102125614000002-0002':'KIKI 瓷片 配套瓷片1 SA9530H1 300x600','00-000-20151102080348000027-0001':'KIKI','00-000-20151102080348000028-0001':'长方形','00-000-20151102080348000029-0001':'300x600','00-000-20151102080348000030-0001':'其它','00-000-20151102080348000031-0001':'亮面'}",
	 *           "goods_id": "00-000-20151102125614000002-0002",
	 *           "goods_name": "KIKI 瓷片 配套瓷片1 SA9530H1 300x600",
	 *           "brand_id": "00-000-20151102010801000004-0001",
	 *           "brand_name": "KIKI",
	 *           "attribute_name1": null,
	 *           "attribute_name2": null,
	 *           "attribute_name3": null,
	 *           "attribute_name4": null,
	 *           "attribute_name5": null,
	 *           "attribute_name6": null,
	 *           "imgList": [
	 *             {
	 *               "img_id": "00-000-20151102062828008870-0001",
	 *               "old_img_id": 22363,
	 *               "goods_id": "00-000-20151102125614000002-0002",
	 *               "img_url": "images/201508/goods_img/1937_P_1440005664221.jpg",
	 *               "img_desc": "",
	 *               "thumb_url": "images/201508/thumb_img/1937_thumb_P_1440005664230.jpg",
	 *               "img_original": "images/201508/source_img/1937_P_1440005664460.jpg"
	 *             },
	 *             {
	 *               "img_id": "00-000-20151102062828008871-0001",
	 *               "old_img_id": 22362,
	 *               "goods_id": "00-000-20151102125614000002-0002",
	 *               "img_url": "images/201508/goods_img/1937_P_1440005664383.jpg",
	 *               "img_desc": "",
	 *               "thumb_url": "images/201508/thumb_img/1937_thumb_P_1440005664542.jpg",
	 *               "img_original": "images/201508/source_img/1937_P_1440005664852.jpg"
	 *             }
	 *           ]
	 *         }
	 *       }
	 *     ],
	 *     "rowCount": 2,
	 *     "page_count": 2
	 *   },
	 *   "code": 0
	 * }
	 * 
	 * </pre>
	 * @throws IOException
	 */
	@RequestMapping(params = { MethodsXml.find, MethodsXml.datePage,
			MethodsXml.objCart })
	public @ResponseBody
	Map<String, Object> getCart(HttpServletRequest request,
			@RequestBody Map<String, Object> accMap) throws IOException {
		Map<String, Object> mapRe = Message.SUCCESS.getObjMess();
		try {
			String ticket = request.getHeader(MessageXml.ticket_key);
			accMap.put("session_id", ticket);
			Map<String, Object> list = orderService.getCart(accMap);
			mapRe.put(MessageXml.pageInfo_key, list);
		} catch (Exception e) {
			e.printStackTrace();
			mapRe = Message.UN_KNOW.getObjMess(e);
		}
		return mapRe;
	}

	/**
	 * 得到当前用户的地址信息
	 * 
	 * @param address
	 *            <pre>
	 * {
	 * 	"user_id":用户ID,
	 * 	["page_size": 每页显示数据条数,]
	 * 	["page": 当前页码]
	 * }
	 * </pre>
	 * @return 当前用户的地址信息，可以是多条地址 <h6>例句</h6>
	 * 
	 *         <pre>
	 * 请求 URL：http://api.llhome.com/order.do?m=get&amp;date=page&amp;obj=address
	 * JSON 参数：{"user_id":"00-000-20151102063348000062-0061","page_size": 1,"page": 1}
	 * Headers 参数：ticket = b4289dcb-4825-4bd5-bd10-39d1128e30ee
	 * 返回结果：
	 * {
	 *   "message": "success",
	 *   "messageTxt": "成功",
	 *   "dataInfo": {
	 *     "page_size": 1,
	 *     "page": 1,
	 *     "list": [
	 *       {
	 *         "address_id": "00-000-20151102115524000003-0003",
	 *         "address_name": "",
	 *         "user_id": "00-000-20151102063348000062-0061",
	 *         "consignee": "肖瑜",
	 *         "email": "1264350253@qq.com",
	 *         "country": 1,
	 *         "province": 3,
	 *         "city": 38,
	 *         "district": 418,
	 *         "address": "季华六路",
	 *         "zipcode": "528000",
	 *         "tel": "0521-56524599",
	 *         "mobile": "13147088464",
	 *         "sign_building": "",
	 *         "best_time": "",
	 *         "old_id": "15"
	 *       }
	 *     ],
	 *     "rowCount": 2,
	 *     "page_count": 2
	 *   },
	 *   "code": 0
	 * }
	 * </pre>
	 * @throws IOException
	 */
	@RequestMapping(params = { MethodsXml.find, MethodsXml.datePage,
			MethodsXml.objAddr })
	public @ResponseBody
	Map<String, Object> getAddress(@RequestBody Map<String, Object> address)
			throws IOException {
		Map<String, Object> mapRe = Message.SUCCESS.getObjMess();
		try {
			mapRe.put(MessageXml.dataInfo_key,
					orderService.selectAddress(address));
		} catch (Exception e) {
			mapRe = Message.UN_KNOW.getObjMess(e);
		}
		return mapRe;
	}

	/**
	 * 添加一条收货地址数据
	 * 
	 * @param address
	 *            <pre>
	 * {
	 *     "address_name":名称,
	 *     "user_id":用户表中的user_id,
	 *     "consignee":收货人的名字,
	 *     "email":收货人的email,
	 *     "country":收货人的国家,
	 *     "province":收货人的省份,
	 *     "city":收货人城市,
	 *     "district":收货人的地区,
	 *     "address":收货人的详细地址,
	 *     "zipcode":收货人的邮编,
	 *     "tel":收货人的电话,
	 *     "mobile":收货人的手机号,
	 *     "sign_building":收货地址的标志性建筑名,
	 *     "best_time":收货人的最佳收货时间,
	 *     "old_id:""  //不能为空
	 * }
	 * </pre>
	 * @return <pre>
	 * 添加 成功 or 未知错误
	 * </pre>
	 * 
	 *         <h6>例句</h6>
	 * 
	 *         <pre>
	 *  请求 URL：http://api.llhome.com/order.do?m=add&amp;obj=address
	 *  JSON 参数：
	 *  	{
	 *      "address_name":"",
	 *      "user_id":"00-000-20151102063348000062-0061",
	 *      "consignee":"Miss",
	 *      "email":"1264350253@qq.com",
	 *      "country":"1",
	 *      "province":"3",
	 *      "city":"38",
	 *      "district":"418",
	 *      "address":"季华六路",
	 *      "zipcode":"528000",
	 *      "tel":"0521-56524599",
	 *      "mobile":"13145616513",
	 *      "sign_building":"",
	 *      "best_time":"",
	 *      "old_id":"15"
	 *  }
	 *  Headers 参数：ticket = d8896a9c-21ce-4cf9-9d5e-98ab65a03c29
	 *  返回结果：
	 *  {
	 * 		"message": "success",
	 * 		"messageTxt": "成功",
	 * 		"code": 0
	 *  }
	 * </pre>
	 * @throws IOException
	 */
	@RequestMapping(params = { MethodsXml.add, MethodsXml.objAddr })
	public @ResponseBody
	Map<String, Object> addAddress(@RequestBody Map<String, Object> address)
			throws IOException {
		Map<String, Object> mapRe = Message.SUCCESS.getObjMess();
		try {
			int num = orderService.insertAddress(address);
			if (num < 0) {
				mapRe = Message.ERROR.getObjMess();
			}
		} catch (Exception e) {
			mapRe = Message.UN_KNOW.getObjMess(e);
		}
		return mapRe;
	}

	/**
	 * 添加 订单对应的商品信息
	 * 
	 * @param address
	 *            <pre>
	 * 参数说明：
	 * {
	 * 	"order_id":"订单商品信息对应的详细信息id，取值e_OrderInfo的order_id",
	 * 	"sku_id":"按规定格式生成的唯一编码",
	 * 	"goods_id":"商品的的id，取值表goods的goods_id",
	 * 	"goods_name":"商品的名称，取值表goods",
	 * 	"goods_sn":"商品的唯一货号，取值goods",
	 * 	"goods_digit":"商品的购买数量",
	 * 	"goods_price":"商品的购买单价",
	 * 	"goods_money":"商品的购买金额",
	 *  "old_id:""  //不能为空
	 * }
	 * </pre>
	 * @return <pre>
	 * 添加成功 或 错误信息
	 * <h6>例如：</h6>
	 * <strong>请求 URL：</strong>http://api.llhome.com/order.do?m=add&amp;obj=osl
	 * <strong>JSON 参数： </strong>
	 * {
	 *     "order_id":"00-000-20151102033730000001-0001",
	 *     "sku_id":"600*600",
	 *     "goods_id":"00-000-20151102125614000003-0003",
	 *     "goods_name":"KIKI 瓷片 配套瓷片1 SA9530Y1 70x600",
	 *     "goods_sn":"SA9530Y1",
	 *     "goods_digit":"1",
	 *     "goods_price":"30.39",
	 *     "goods_money":"30.39",
	 *     "old_id":1
	 * }
	 * <strong>Headers 参数：</strong>ticket = d8896a9c-21ce-4cf9-9d5e-98ab65a03c29
	 * <strong>返回结果：</strong>
	 * {
	 *   "message": "success",
	 *   "messageTxt": "成功",
	 *   "code": 0
	 * }
	 * </pre>
	 * @throws IOException
	 */
	@RequestMapping(params = { MethodsXml.add, MethodsXml.objOrdersSKUList })
	public @ResponseBody
	Map<String, Object> addOrdersSKUList(@RequestBody Map<String, Object> or)
			throws IOException {
		Map<String, Object> mapRe = Message.SUCCESS.getObjMess();
		try {
			String pk = OSKULSer.getPrimaryKey();
			String pk_value = ItemHelper.createPrimaryKey(); 
			or.put(pk, pk_value);
			int num = OSKULSer.addRow(or);
			if (num < 0) {
				mapRe = Message.ERROR.getObjMess();
			} else {
				mapRe.put(pk, pk_value);
			}
		} catch (Exception e) {
			mapRe = Message.UN_KNOW.getObjMess(e);
		}
		return mapRe;
	}
	
	/**
	 * 购物车提交订单
	 * @param orderMap
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(params = { MethodsXml.add,MethodsXml.dateList })
	public @ResponseBody
	Map<String, Object> addOrdersSKUList1(@RequestBody Map<String, Object> orderMap)
			throws IOException {
		Map<String, Object> mapRe = Message.SUCCESS.getObjMess();
		try {
			//TODO 2016-03-05 先添加一条数据到e_orderinfo 再添加List 到orderskulist
			if (!orderMap.containsKey("order_sku_list")){
				mapRe = Message.ERROR.getObjMess();
				mapRe.put("order_sku_list", "缺少order_sku_list商品列表!");
				return mapRe;
			}
			if (!orderMap.containsKey("order_info")){
				mapRe = Message.ERROR.getObjMess();
				mapRe.put("order_info", "缺少order_info订单数据!");
				return mapRe;
			}
			List<Map<String, Object>> list = (List<Map<String, Object>>)orderMap.get("order_sku_list");
			if (list.size() <= 0) {
				mapRe = Message.ERROR.getObjMess();
				mapRe.put("order_sku_list", "缺少order_sku_list商品列表!");
				return mapRe;
			}
			//订单的总金额 goods_amount
			double goods_amount = 0.00;
			for (int i = 0, length = list.size(); i < length; i++) {
				int goods_digit = Integer.parseInt(list.get(i).get("goods_digit").toString());
				if (goods_digit<=0){
					mapRe = Message.ERROR.getObjMess();
					mapRe.put(list.get(i).get("goods_name").toString(), "商品必须要有数量");
					return mapRe;
				}
				Double goods_price = Double.parseDouble(list.get(i).get("goods_price").toString());				
				goods_amount = goods_digit*goods_price;
				list.get(i).put("goods_money", goods_amount);
				
			}
			
			Map<String, Object> order_info = (Map<String, Object>)orderMap.get("order_info");
			// 创建 e_OrderInfo 订单主健 
			String pk_OrderInfo = OrderInfoSer.getPrimaryKey();
			String pk_value_OrderInfo = ItemHelper.createPrimaryKey();
			order_info.put(pk_OrderInfo, pk_value_OrderInfo);
			//生成订单号
			String order_sn = ItemHelper.createOrderSN();
			order_info.put("order_sn", order_sn);			
			
			//TODO 将订单添加到erp 方法1
			String erp_mssage = add_erp(orderMap, order_sn);
			if (erp_mssage.indexOf("成功更新数据") <= -1){
				mapRe = Message.UN_KNOW.getObjMess();
				mapRe.put(MessageXml.messageTxt_key, "程序等待");
				mapRe.put("erp_mssage", erp_mssage);
			}else{
			
			//添加到 e_OrdersSKUList 表
			for (int i = 0,length = list.size(); i < length; i++) {
				list.get(i).put("order_id", pk_value_OrderInfo);
			}
			int order_sku_list_rows[] = OSKULSer.addRows(list);
			for (int i = 0, length = order_sku_list_rows.length; i < length; i++) {
				if (order_sku_list_rows[i]<=0){
					mapRe = Message.ERROR.getObjMess();
					mapRe.put(list.get(i).get("goods_name").toString(), "为订单添加商品失败");
					return mapRe;
				}
			}
			//添加到 e_OrderInfo 表
			int order_info_row = OrderInfoSer.addRow(order_info);
			if (order_info_row<=0){
				mapRe = Message.ERROR.getObjMess();
				mapRe.put("order_info_row", "添加订单失败");
				return mapRe;
			}
			
//			//TODO 将订单添加到erp 方法2
//			String erp_mssage = add_erp2(order_sn);
			mapRe.put("order_sn", order_sn);
			mapRe.put("order_id", pk_value_OrderInfo);
			mapRe.put("erp", erp_mssage);
			}
		} catch (Exception e) {
			e.printStackTrace();
			mapRe = Message.UN_KNOW.getObjMess(e);
		}
		return mapRe;
	}
	
	/**
	 * 订单添加到erp方法。参数需要传入order_sku_list
	 * @param order_sku_list
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public String add_erp(Map<String, Object> orderMap,String order_sn) throws Exception{
		//将订单添加到erp  table:t8200044  function:insert_erp
		//docdate,stcode,cltcode,cltname,matcode,matname,special,cv6,UOM,Digit,Price,totalmoney,agreeCode,stname,mattype,clientPhone,HDtext,xinjiu,ReceivePerson,ReceivePhone,ReceiveAddr
		//时间
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = dateFormat.format(new Date());
		
		List<Map<String, Object>> list_order = new ArrayList<Map<String, Object>>();
		Map<String, Object> mt = null;
		//TODO 不需要去取值了
		List<Map<String, Object>> order_sku_list = (List<Map<String, Object>>)orderMap.get("order_sku_list");
		Map<String, Object> order_info = (Map<String, Object>)orderMap.get("order_info");
		Map<String, Object> attr = null;
		String user_id = order_info.get("user_id").toString();
		System.out.println(user_id);
		Map<String, Object> clt = orderService.getClt(user_id);
		System.out.println(clt.get("cltcode").toString());
		Map<String, Object> erpprice = new HashMap<String, Object>();
		for (int i = 0,length = order_sku_list.size(); i < length; i++) {
			attr = new HashMap<String, Object>();
			String sku_id = order_sku_list.get(i).get("sku_id").toString();
			String goods_id = order_sku_list.get(i).get("goods_id").toString();
			List<String> attribute_names = new ArrayList<String>();
			attribute_names.add("等级");
			attribute_names.add("产品编号");
			attr = orderService.getAttribute(sku_id, goods_id, attribute_names);
			String matcode = attr.get("matcode").toString();
			String cv6 = attr.get("cv6").toString();
			String dictvalue = order_sku_list.get(i).get("dictvalue").toString();
			String companyid = orderService.getCompanyid(dictvalue);

			if (!"".equals(matcode) && !"".equals(cv6)){
				if("优等品".equals(cv6)) {
					cv6="AAA";
				}
				if("一级品".equals(cv6)) {
					cv6="一级";
				}
				if("四级品".equals(cv6)) {
					cv6="四级";
				}
			} else {
				throw new Exception("产品编号 和 等级 条件不能为空：matcode=".concat(matcode).concat(", cv6=").concat(cv6));
			}
			erpprice = orderService.getERPOne(matcode, cv6, companyid);

			//for 所有商品
			mt = new HashMap<String, Object>();
			//erppriceService
			mt.put("doccode", order_sn);
			mt.put("docdate", date);
			mt.put("stcode", companyid);
			mt.put("cltcode", clt.get("cltcode"));
			mt.put("cltname", clt.get("cltname"));
			mt.put("matcode", erpprice.get("matcode"));
			mt.put("matname", erpprice.get("matname"));
			mt.put("special", erpprice.get("special"));
			mt.put("cv6", erpprice.get("cv6"));
			mt.put("UOM", erpprice.get("uom"));
			int goods_digit = Integer.parseInt(order_sku_list.get(i).get("goods_digit").toString());
			mt.put("Digit", goods_digit);
			Double goods_price = Double.parseDouble(erpprice.get("Price").toString());
			mt.put("Price", goods_price);
			mt.put("totalmoney", goods_digit*goods_price);
			mt.put("agreeCode", "");
			mt.put("stname", dictvalue);
			mt.put("mattype", "");
			
			list_order.add(mt);
		}
		StringBuffer stb = ItemHelper.insert_erp(list_order, "t8000050", "doccode");
		System.out.println("t8000050:"+stb.toString());
		return stb.toString();
	}
	
	
	/**
	 * 查询订单的相关数据和属性    
	 * @param orderMap
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(params = { MethodsXml.find, "show=erpone" })
	public @ResponseBody
	Map<String, Object> showERPOne(@RequestBody Map<String, Object> whereMap)
			throws Exception {
		Map<String, Object> mapRe = Message.SUCCESS.getObjMess();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			if (!whereMap.containsKey("matcode")){
				throw new Exception("matcode 不存在");
			}
			if (!whereMap.containsKey("cv6")){
				throw new Exception("cv6 不存在");
			}
			if (!whereMap.containsKey("companyid")){
				throw new Exception("companyid 不存在");
			}
			String matcode = whereMap.get("matcode").toString();
			String cv6 = whereMap.get("cv6").toString();
			String companyid = whereMap.get("companyid").toString();
			map = orderService.getERPOne(matcode, cv6, companyid);
			mapRe.put(MessageXml.dataInfo_key, map);
		} catch (Exception e) {
			e.printStackTrace();
			mapRe = Message.UN_KNOW.getObjMess(e);
		}
		return mapRe;
	}
	
	/**
	 * 查询cltcode和cltname
	 * @param orderMap
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(params = { MethodsXml.find, "show=clt" })
	public @ResponseBody
	Map<String, Object> showClt(@RequestBody Map<String, Object> whereMap)
			throws Exception {
		Map<String, Object> mapRe = Message.SUCCESS.getObjMess();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			if (!whereMap.containsKey("user_id")){
				throw new Exception("user_id 不存在");
			}
			String user_id = whereMap.get("user_id").toString();
			map = orderService.getClt(user_id);
			mapRe.put(MessageXml.dataInfo_key, map);
		} catch (Exception e) {
			e.printStackTrace();
			mapRe = Message.UN_KNOW.getObjMess(e);
		}
		return mapRe;
	}
	
	/**
	 * 查询Companyid
	 * @param orderMap
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(params = { MethodsXml.find, "show=companyid" })
	public @ResponseBody
	Map<String, Object> showCompanyid(@RequestBody Map<String, Object> whereMap)
			throws Exception {
		Map<String, Object> mapRe = Message.SUCCESS.getObjMess();
		try {
			if (!whereMap.containsKey("dictvalue")){
				throw new Exception("dictvalue 不存在");
			}
			String dictvalue = whereMap.get("dictvalue").toString();
			String companyid = orderService.getCompanyid(dictvalue);
			mapRe.put("companyid", companyid);
		} catch (Exception e) {
			e.printStackTrace();
			mapRe = Message.UN_KNOW.getObjMess(e);
		}
		return mapRe;
	}
	

	/**
	 * 查询 matcode 和 cv6
	 * @param orderMap
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(params = { MethodsXml.find, "show=attr" })
	public @ResponseBody
	Map<String, Object> showAttribute3(@RequestBody Map<String, Object> whereMap)
			throws Exception {
		Map<String, Object> mapRe = Message.SUCCESS.getObjMess();
		Map<String, Object> list = new HashMap<String, Object>(); 
		try {
			if (!whereMap.containsKey("sku_id")){
				throw new Exception("sku_id 不存在");
			}
			if (!whereMap.containsKey("goods_id")){
				throw new Exception("goods_id 不存在");
			}
			if (!whereMap.containsKey("attribute_name")){
				throw new Exception("attribute_name 不存在");
			}
			String sku_id = whereMap.get("sku_id").toString();
			String goods_id = whereMap.get("goods_id").toString();
			List<String> attribute_name = new ArrayList<String>();
			attribute_name = (List<String>)whereMap.get("attribute_name");
			list = orderService.getAttribute2(sku_id, goods_id, attribute_name);
			mapRe.put(MessageXml.dataInfo_key, list);
		} catch (Exception e) {
			e.printStackTrace();
			mapRe = Message.UN_KNOW.getObjMess(e);
		}
		return mapRe;
	}
}
