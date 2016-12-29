package com.ekc.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekc.enumall.Message;
import com.ekc.service.AccountService;
import com.ekc.service.GoodsService;
import com.ekc.xml.MessageXml;
import com.ekc.xml.MethodsXml;
import com.ekc.service.tb.GoodsObjService;

/**
 * 商品类
 * 
 * @author hui
 * 
 */
@Controller
@RequestMapping("/goods.do")
public class GoodsinfoAction {
	@Autowired
	GoodsService gService;
	@Autowired
	AccountService accountService;
	@Autowired
	GoodsObjService GoodsObjService;
	/**
	 * 产品信息 查询某类产品信息 第三目录
	*/
	@Deprecated
	@RequestMapping(params = MethodsXml.find)
	public @ResponseBody
	Map<String, Object> getgoodsinfo(@RequestBody Map<String, Object> goodsinfo) {
		Map<String, Object> mapRe = Message.SUCCESS.getObjMess();
		try {
			Map<String, Object> pageMap = gService.selectGoods(goodsinfo);
			if (Integer.parseInt(pageMap.get(MessageXml.rowCount_key)
					.toString()) < 0) {
				mapRe = Message.NO_DATA.getObjMess();
			} else {
				mapRe.put(MessageXml.dataInfo_key, pageMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
			mapRe = Message.UN_KNOW.getObjMess(e);
		}
		return mapRe;
	}

	/**
	 * 列出产品分类 over 第一分类目录和二级分类
	 * 
	 * <pre>
	 * {
	 * 	"category_id":"00-000-20151104122000000001-0001"
	 * }
	 * </pre>
	 * 
	 * @return <pre>
	 * 列出产品分类
	 * </pre>
	 * 
	 *         <h6>例子</h6>
	 * 
	 *         <pre>
	 * <strong>请求 URL：</strong>http://api.llhome.com/goods.do?m=get&amp;obj=class
	 * <strong>JSON 参数：{"category_id":"00-000-20151104122000000001-0001"}</strong> 
	 * <strong>Headers 参数：</strong>ticket=45feb928-3ff2-4f98-a4a2-b9ebb3899992 
	 * <strong>返回结果：</strong>
	 * {
	 *   "message": "success",
	 *   "messageTxt": "成功",
	 *   "list": [
	 *     {
	 *       "category_id": "00-000-20151104122000000001-0001",
	 *       "old_category_id": 1,
	 *       "old_goods_type": null,
	 *       "category_name": "瓷砖",
	 *       "category_image": "data/attached/cat_image/b6f1a6b69bbab0d7930fdd0e69422748.jpg",
	 *       "keywords": null,
	 *       "category_desc": null,
	 *       "sort_order": true,
	 *       "template_file": null,
	 *       "filter_attr": "211,212,213,214,215,216",
	 *       "measure_unit": null,
	 *       "show_in_nav": true,
	 *       "style": null,
	 *       "is_show": true,
	 *       "row_id": "000001",
	 *       "parent_row_id": "",
	 *       "tree_row_id": "000001",
	 *       "nodeChild": [
	 *         {
	 *           "category_id": "00-000-20151104122000000020-0020",
	 *           "old_category_id": 37,
	 *           "old_goods_type": 43,
	 *           "category_name": "仿大理石",
	 *           "category_image": "data/attached/cat_image/1e8f483f9e6cc5ee175c771c1e34c3c7.jpg",
	 *           "keywords": null,
	 *           "category_desc": null,
	 *           "sort_order": true,
	 *           "template_file": null,
	 *           "filter_attr": "",
	 *           "measure_unit": null,
	 *           "show_in_nav": false,
	 *           "style": null,
	 *           "is_show": true,
	 *           "row_id": "00000K",
	 *           "parent_row_id": "000001",
	 *           "tree_row_id": "000001-00000K"
	 *         }
	 *       ]
	 *     }
	 *   ],
	 *   "code": 0
	 * }
	 * </pre>
	 */
	@RequestMapping(params = { MethodsXml.find, MethodsXml.objClass })
	public @ResponseBody
	Map<String, Object> getategoryinfo(
			@RequestBody Map<String, Object> goodsinfo) {
		Map<String, Object> mapRe = Message.SUCCESS.getObjMess();
		try {
			List<Map<String, Object>> list = gService.selectAtegory(goodsinfo);
			if (list.size() > 0) {
				mapRe.put(MessageXml.list_key, list);
			} else {
				mapRe = Message.NO_DATA.getObjMess();
			}
		} catch (Exception e) {
			e.printStackTrace();
			mapRe = Message.UN_KNOW.getObjMess(e);
		}
		return mapRe;
	}

	/**
	 * 随机产品产品列表
	 * 
	 * <pre>
	 * {
	 * 	"page_size": 1,
	 * 	"page": 1
	 * }
	 * </pre>
	 * 
	 * @return <pre>
	 * 随机产品产品列表
	 * </pre>
	 * 
	 *         <h6>例子</h6>
	 * 
	 *         <pre>
	 * <strong>请求 URL：</strong>http://api.llhome.com/goods.do?m=get&amp;obj=rows
	 * <strong>JSON 参数：</strong>
	 * {
	 * 	"page_size": 1,
	 * 	"page": 1
	 * } 
	 * <strong>Headers 参数：</strong>ticket=45feb928-3ff2-4f98-a4a2-b9ebb3899992 
	 * <strong>返回结果：</strong>
	 * {
	 *   "message": "success",
	 *   "messageTxt": "成功",
	 *   "list": [
	 *     {
	 *       "category_id": "00-000-20151104122000000001-0001",
	 *       "old_category_id": 1,
	 *       "old_goods_type": null,
	 *       "category_name": "瓷砖",
	 *       "category_image": "data/attached/cat_image/b6f1a6b69bbab0d7930fdd0e69422748.jpg",
	 *       "keywords": null,
	 *       "category_desc": null,
	 *       "sort_order": true,
	 *       "template_file": null,
	 *       "filter_attr": "211,212,213,214,215,216",
	 *       "measure_unit": null,
	 *       "show_in_nav": true,
	 *       "style": null,
	 *       "is_show": true,
	 *       "row_id": "000001",
	 *       "parent_row_id": "",
	 *       "tree_row_id": "000001",
	 *       "nodeChild": [
	 *         {
	 *           "category_id": "00-000-20151104122000000020-0020",
	 *           "old_category_id": 37,
	 *           "old_goods_type": 43,
	 *           "category_name": "仿大理石",
	 *           "category_image": "data/attached/cat_image/1e8f483f9e6cc5ee175c771c1e34c3c7.jpg",
	 *           "keywords": null,
	 *           "category_desc": null,
	 *           "sort_order": true,
	 *           "template_file": null,
	 *           "filter_attr": "",
	 *           "measure_unit": null,
	 *           "show_in_nav": false,
	 *           "style": null,
	 *           "is_show": true,
	 *           "row_id": "00000K",
	 *           "parent_row_id": "000001",
	 *           "tree_row_id": "000001-00000K"
	 *         }
	 *       ]
	 *     }
	 *   ],
	 *   "code": 0
	 * }
	 * </pre>
	 */
	@RequestMapping(params = { MethodsXml.find, MethodsXml.dateRows,
			MethodsXml.objRandom })
	public @ResponseBody
	Map<String, Object> randomgoodsinfo(
			@RequestBody Map<String, Object> goodsinfo) {
		Map<String, Object> mapRe = new HashMap<String, Object>();
		List<Map<String, Object>> list = null;
		try {
			list = gService.selectRandom(goodsinfo);
			mapRe.put(MessageXml.dataInfo_key, list);
			if (list.size() < 0) {
				mapRe = Message.NO_DATA.getObjMess();
			}
		} catch (Exception e) {
			e.printStackTrace();
			mapRe = Message.UN_KNOW.getObjMess(e);
		}
		return mapRe;
	}

	/**
	 * 查询分页商品数据
	 * 
	 * @param goodsinfo
	 *            <pre>
	 * {
	 * 	"page_size": 1,
	 * 	"page": 1
	 * }
	 * </pre>
	 * @return <pre>
	 * 分页商品数据
	 * </pre>
	 * 
	 *         <h6>例子</h6>
	 * 
	 *         <pre>
	 * <strong>请求 URL：</strong>http://api.llhome.com/goods.do?m=get&amp;date=page
	 * <strong>JSON 参数：</strong>
	 * {
	 * 	"page_size": 1,
	 * 	"page": 1
	 * } 
	 * <strong>Headers 参数：</strong>ticket=45feb928-3ff2-4f98-a4a2-b9ebb3899992 
	 * <strong>返回结果：</strong>
	 * {
	 *   "message": "success",
	 *   "messageTxt": "成功",
	 *   "dataInfo": {
	 *     "page_size": 1,
	 *     "page": 1,
	 *     "list": [
	 *       {
	 *         "goods_id": "00-000-20151102125614000001-0001",
	 *         "old_goods_id": 1936,
	 *         "goods_sn": "SA9530B",
	 *         "goods_name": "KIKI 瓷片 配套瓷片1 SA9530B 300x600",
	 *         "goods_name_style": "+",
	 *         "click_count": 0,
	 *         "brand_id": "0",
	 *         "provider_name": "",
	 *         "goods_number": 1000,
	 *         "goods_weight": 2.75,
	 *         "market_price": 16.4,
	 *         "shop_price": 13.67,
	 *         "promote_price": 0,
	 *         "promote_start_date": 0,
	 *         "promote_end_date": 0,
	 *         "warn_number": 1,
	 *         "keywords": "",
	 *         "goods_brief": "",
	 *         "goods_desc": "",
	 *         "goods_thumb": "images/201508/thumb_img/1936_thumb_G_1440005516617.jpg",
	 *         "goods_img": "images/201508/goods_img/1936_G_1440005516231.jpg",
	 *         "original_img": "images/201508/source_img/1936_G_1440005516836.jpg",
	 *         "is_real": 1,
	 *         "extension_code": "",
	 *         "is_on_sale": true,
	 *         "is_alone_sale": true,
	 *         "integral": 0,
	 *         "add_time": 1440005516000,
	 *         "sort_order": 100,
	 *         "is_delete": false,
	 *         "is_best": false,
	 *         "is_new": false,
	 *         "is_hot": false,
	 *         "is_promote": false,
	 *         "last_update": 1445126622000,
	 *         "seller_note": "",
	 *         "give_integral": -1
	 *       }
	 *     ],
	 *     "rowCount": 2464,
	 *     "page_count": 2464
	 *   },
	 *   "code": 0
	 * }
	 * </pre>
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(params = { MethodsXml.find, MethodsXml.datePage })
	public @ResponseBody
	Map<String, Object> singlegoodsinfo(
			@RequestBody Map<String, Object> goodsinfo) {
		Map<String, Object> mapRe = Message.SUCCESS.getObjMess();
		try {
			mapRe.put(MessageXml.dataInfo_key, gService.selectSingle(goodsinfo));
			if (Integer.parseInt(((Map<String, Object>) mapRe
					.get(MessageXml.dataInfo_key)).get(MessageXml.rowCount_key)
					.toString()) < 0) {
				mapRe = Message.NO_DATA.getObjMess();
			}
		} catch (Exception e) {
			e.printStackTrace();
			mapRe = Message.UN_KNOW.getObjMess(e);
		}
		return mapRe;
	}

	/**
	 * 产品详情
	 * 
	 * @param map
	 *            <pre>
	 * {"goods_id":"商品ID"}
	 * </pre>
	 * @return <pre>
	 * 产品详情
	 * </pre>
	 * 
	 *         <h6>例子</h6>
	 * 
	 *         <pre>
	 * <strong>请求 URL：</strong>http://api.llhome.com/goods.do?m=get&amp;obj=doa
	 * <strong>JSON 参数：</strong>
	 * {"goods_id":"00-000-20151102125614000032-0032"}
	 * <strong>Headers 参数：</strong>ticket=45feb928-3ff2-4f98-a4a2-b9ebb3899992 
	 * <strong>返回结果：</strong>
	 * {
	 *   "message": "success",
	 *   "messageTxt": "成功",
	 *   "dataInfo": {
	 *     "sku_id": "00-000-20151105051450000125-0001",
	 *     "sku_name": "{'00-000-20151102125614000032-0032':'楼兰 玻璃马赛克 KTP-S005(#15)','00-000-20151102080348000066-0001':'楼兰','00-000-20151102080348000113-0001':'优等品','00-000-20151102080348000157-0001':'300x300'}",
	 *     "goods_id": "00-000-20151102125614000032-0032",
	 *     "goods_name": "楼兰 玻璃马赛克 KTP-S005(#15)",
	 *     "brand_id": "00-000-20151102010801000002-0001",
	 *     "brand_name": "楼兰",
	 *     "attribute_name1": null,
	 *     "attribute_name2": null,
	 *     "attribute_name3": null,
	 *     "attribute_name4": null,
	 *     "attribute_name5": null,
	 *     "attribute_name6": null,
	 *     "xinghao": {
	 *       "xinghao1": "600X720"
	 *     },
	 *     "spec": {
	 *       "spec": "1200X1690"
	 *     },
	 *     "xingzhuang": {
	 *       "xingzhuang": "多边形"
	 *     },
	 *     "yangse": {
	 *       "yangse": "red"
	 *     },
	 *     "zhimian": {
	 *       "zhimian": "光面"
	 *     },
	 *     "imgList": [
	 *       {
	 *         "img_id": "00-000-20151102062828009009-0001",
	 *         "old_img_id": 22501,
	 *         "goods_id": "00-000-20151102125614000032-0032",
	 *         "img_url": "images/201508/goods_img/1965_P_1440094421628.jpg",
	 *         "img_desc": "",
	 *         "thumb_url": "images/201508/thumb_img/1965_thumb_P_1440094421888.jpg",
	 *         "img_original": "images/201508/source_img/1965_P_1440094421287.jpg"
	 *       }
	 *     ]
	 *   },
	 *   "code": 0
	 * }
	 * </pre>
	 */
	@RequestMapping(params = { MethodsXml.find, MethodsXml.objDoa })
	public @ResponseBody
	Map<String, Object> details(@RequestBody Map<String, Object> map) {
		Map<String, Object> mapRe = Message.SUCCESS.getObjMess();
		try {
			Map<String, Object> rowMap = gService.getSKU(map);
			if (rowMap == null) {
				mapRe = Message.NO_DATA.getObjMess();
			} else {
				mapRe.put(MessageXml.dataInfo_key, rowMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
			mapRe = Message.UN_KNOW.getObjMess(e);
		}
		return mapRe;
	}

	/**
	 * 产品详情 只根据ID来查
	 * 
	 * @param goods_id
	 *            <pre>
	 * <strong>在地址后面添加商品ID：</strong>http://api.llhome.com/goods.do?m=get&obj=dob&goods_id=商品的ID
	 * </pre>
	 * @return <pre>
	 * 产品详情
	 * </pre>
	 * 
	 *         <h6>例子</h6>
	 * 
	 *         <pre>
	 * 
	 * <strong>请求 URL：</strong>http://api.llhome.com/goods.do?m=get&obj=dob&goods_id=00-000-20151102125614000032-0032
	 * <strong>JSON 参数：</strong>
	 * <strong>Headers 参数：</strong>ticket=45feb928-3ff2-4f98-a4a2-b9ebb3899992 
	 * <strong>返回结果：</strong>
	 * {
	 *   "message": "success",
	 *   "messageTxt": "成功",
	 *   "dataInfo": {
	 *     "sku_id": "00-000-20151105051450000125-0001",
	 *     "sku_name": "{'00-000-20151102125614000032-0032':'楼兰 玻璃马赛克 KTP-S005(#15)','00-000-20151102080348000066-0001':'楼兰','00-000-20151102080348000113-0001':'优等品','00-000-20151102080348000157-0001':'300x300'}",
	 *     "goods_id": "00-000-20151102125614000032-0032",
	 *     "goods_name": "楼兰 玻璃马赛克 KTP-S005(#15)",
	 *     "brand_id": "00-000-20151102010801000002-0001",
	 *     "brand_name": "楼兰",
	 *     "attribute_name1": null,
	 *     "attribute_name2": null,
	 *     "attribute_name3": null,
	 *     "attribute_name4": null,
	 *     "attribute_name5": null,
	 *     "attribute_name6": null,
	 *     "xinghao": {
	 *       "xinghao1": "600X720"
	 *     },
	 *     "spec": {
	 *       "spec": "1200X1690"
	 *     },
	 *     "xingzhuang": {
	 *       "xingzhuang": "多边形"
	 *     },
	 *     "yangse": {
	 *       "yangse": "red"
	 *     },
	 *     "zhimian": {
	 *       "zhimian": "光面"
	 *     },
	 *     "imgList": [
	 *       {
	 *         "img_id": "00-000-20151102062828009009-0001",
	 *         "old_img_id": 22501,
	 *         "goods_id": "00-000-20151102125614000032-0032",
	 *         "img_url": "images/201508/goods_img/1965_P_1440094421628.jpg",
	 *         "img_desc": "",
	 *         "thumb_url": "images/201508/thumb_img/1965_thumb_P_1440094421888.jpg",
	 *         "img_original": "images/201508/source_img/1965_P_1440094421287.jpg"
	 *       }
	 *     ]
	 *   },
	 *   "code": 0
	 * }
	 * </pre>
	 */
	@RequestMapping(params = { MethodsXml.find, MethodsXml.objDob })
	public @ResponseBody
	Map<String, Object> details(String goods_id) {
		Map<String, Object> mapRe = Message.SUCCESS.getObjMess();
		try {
			Map<String, Object> rowMap = gService.getSKU(goods_id);
			if (rowMap == null) {
				mapRe = Message.NO_DATA.getObjMess();
			} else {
				mapRe.put(MessageXml.dataInfo_key, rowMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
			mapRe = Message.UN_KNOW.getObjMess(e);
		}
		return mapRe;
	}
	
	/**
	 * 查询SKU集合
	 * author:Gaohui
	 * date:2016-01-23
	 * url:.../goods.do?m=get&data=sku
	 * 用于http://www.51exc.com/category.php
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping(params = { MethodsXml.find,"data=sku"})
	public @ResponseBody Map<String, Object> SKUList(@RequestBody Map<String, Object> map){
		Map<String, Object> mapRe = Message.SUCCESS.getObjMess();
		try {
			mapRe.put(MessageXml.data_key, gService.getSKUList(map));
		} catch (Exception e) {
			e.printStackTrace();
			mapRe = Message.UN_KNOW.getObjMess(e);
		}
		return mapRe;
	}
	
	/**
	 * 查询category以下列表
	 * author:Gaohui
	 * date:2016-01-23
	 * url:.../goods.do?m=get&data=ph
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping(params = { MethodsXml.find,"data=ph"})
	public @ResponseBody Map<String, Object> categoryList(@RequestBody Map<String, Object> map){
		Map<String, Object> mapRe = Message.SUCCESS.getObjMess();
		try {
			List<Map<String, Object>> list = gService.getGoodsList(map);
			mapRe.put(MessageXml.data_key, list);
		} catch (Exception e) {
			e.printStackTrace();
			mapRe = Message.UN_KNOW.getObjMess(e);
		}
		return mapRe;
	}
	
	/**
	 * 查询Goods SKU 单个集合
	 * author:Gaohui
	 * date:2016-01-28
	 * url:.../goods.do?m=get&data=goods&data=sku
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping(params = { MethodsXml.find,"data=goodssku"})
	public @ResponseBody Map<String, Object> SKUGoodsList(@RequestBody Map<String, Object> map){
		Map<String, Object> mapRe = Message.SUCCESS.getObjMess();
		try {
			mapRe.put(MessageXml.data_key, gService.getGoodsSKU(map));
		} catch (Exception e) {
			e.printStackTrace();
			mapRe = Message.UN_KNOW.getObjMess(e);
		}
		return mapRe;
	}
}
