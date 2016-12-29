package com.ekc.action.goods;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekc.action.BaseAction;
import com.ekc.enumall.Message;
import com.ekc.ifc.TableUseIfc;
import com.ekc.util.ItemHelper;
import com.ekc.xml.MessageXml;
import com.ekc.xml.MethodsXml;

/**
 * 操作商品分类表
 * @author ZhengXiajun
 *
 */
@Controller
@RequestMapping("/goods/category.do")
public class CategoryAction extends BaseAction {
	@Autowired
	TableUseIfc  CategorySer;
	@Autowired
	TableUseIfc  AttributesSer;
	@Override
	public TableUseIfc getTabelServer() {
		return CategorySer;
	}
	
	/**
	 * 需求人 ：阿仪
	 * 名　称 ：分类
	 * 请求URL: .../goods/category.do?m=get&date=one&t=map
	 * 传送参数：{"category_id": "00-000-20151104122000000001-0001"}
	 *        或 {}
	 * @param mapWhere
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(params={MethodsXml.find, MethodsXml.dateOne, "t=map"})
	public @ResponseBody Map<String, Object> bb(@RequestBody Map<String, Object> mapWhere){
		Map<String, Object> mapRe = Message.SUCCESS.getObjMess();
		try {
			Map<String, Object> map = null; 
			List<Map<String, Object>> list = CategorySer.findList(mapWhere);
			if (list == null || list.size()==0){
				mapRe = Message.NO_DATA.getObjMess();
			} else {
				map=ItemHelper.findValueToFiled(list);
				List<Map<String, Object>> list_a = AttributesSer.findList("category_id",map.get("old_category_id"));
				Object list_key = null;
				for (int i = 0,length=list.size(); i < length; i++) {
					list_key = list_a.get(i).get("av_r");
					if (!ItemHelper.isEmpty(list_key)) {
						list_a.get(i).put("attribute_values", list_key.toString().split(","));
					} else {
						list_a.get(i).put("attribute_values",new ArrayList());
					}
				}
				Map<String, Object> map_a = ItemHelper.findFiledToKey(list_a,"category_id");
				Object key = null;
				for (int i = 0,length=list.size(); i < length; i++) {
					key = list.get(i).get("old_category_id");
					list_a = new ArrayList<Map<String,Object>>();
					if (!ItemHelper.isEmpty(key)) {
						if (map_a.containsKey(key.toString())){
							list_a=(List)map_a.get(key.toString());
						}
					}
					list.get(i).put("attributes", list_a);
				}
				mapRe.put(MessageXml.dataInfo_key, list);
			}
		} catch (Exception e) {
			e.printStackTrace();
			mapRe = Message.UN_KNOW.getObjMess(e);
		}
		return mapRe;
	}
	
	
	
}
