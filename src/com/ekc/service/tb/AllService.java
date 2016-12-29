package com.ekc.service.tb;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekc.ifc.TableUseIfc;
import com.ekc.service.BaseService;
import com.ekc.util.ItemHelper;

@Service("allService")
public class AllService extends BaseService {
	@Autowired
	TableUseIfc eGoodsAttributeValues_Ser;// 商品属性值
	@Autowired
	AttributesBase AttributesBase;// 属性
	@Autowired
	TableUseIfc eAttributeValues_Ser;// 属性值
	@Autowired
	TableUseIfc ePicturePlaceObject_Ser;// 图片位置

	/**
	 * 添加or修改 商品属性值表(e_goodsattributevalues)
	 * 
	 * @param addMap
	 *            添加的已知值
	 * @param attributes_commons
	 *            传人的值
	 * @return
	 * @throws Exception
	 */
	public int[] doGav(Map<String, Object> addMap,
			List<Map<String, Object>> attributes_commons) throws Exception {
		int ac_code[] = null;
		if (attributes_commons != null) {
			// 属性值ID
			String attribute_value_id = "attribute_value_id";// 传过来的属性id
			String attribute_value = "attribute_value";// 对应的属性值
			// 单个商品属性值
			Map<String, Object> acMap = null;
			// 要插入处理后的商品属性值数据列表 (已删除attribute_value_id 为空的数据)
			List<Map<String, Object>> ac_list = new ArrayList<Map<String, Object>>();
			// 商品属性值 表ID
			String gav_id = eGoodsAttributeValues_Ser.getPrimaryKey();
			for (int i = 0, length = attributes_commons.size(); i < length; i++) {
				acMap = attributes_commons.get(i);
				if (acMap.containsKey(attribute_value)) {// 存在则修改属性值id
					// WhereTable attvWhere = new WhereTable("attribute_id",
					// acMap.get("attribute_id"));
					// attvWhere
					// .put("attribute_value", acMap.get(attribute_value));
					// acMap.put(attribute_value_id, AttributesBase
					// .findAttributeValueId(attvWhere.getMap()));
					acMap.put(attribute_value_id, AttributesBase
							.findAttributeValueId(acMap.get("attribute_id")
									.toString(), acMap.get("attribute_value")
									.toString(), acMap
									.get("attribute_value_id").toString()));
				}

				// 判断属性值ID 的值 是否为空
				if (!ItemHelper.isEmpty(acMap.get(attribute_value_id))) {
					acMap.putAll(addMap);
					// 判断商品属性值 表ID 值 是否为空
					if (!acMap.containsKey(gav_id)
							|| ItemHelper.isEmpty(acMap.get(gav_id))) {
						// 1.不存在，则添加
						acMap.put(gav_id, ItemHelper.createPrimaryKey());
						ac_list.add(acMap);
					} else {
						// 2.存在，则修改
						acMap.remove(attribute_value);
						eGoodsAttributeValues_Ser.update(acMap.get(gav_id)
								.toString(), acMap);
					}
				} else {
					// eGoodsAttributeValues_Ser.delete(acMap.get(gav_id)
					// .toString());
				}
			}
			// 将attributes_commons[]数据插入商品属性值表
			if (ac_list.size() > 0) {
				ac_code = eGoodsAttributeValues_Ser.addRows(ac_list);
			}
		}
		return ac_code;
	}

	/**
	 * 添加图片位置信息
	 * 
	 * @param addMap
	 *            已经知道的参数
	 * @param ppoList
	 *            图片位置集合
	 * @return
	 * @throws Exception
	 */
	public void addPicturePlaceObject(Map<String, Object> addMap,
			List<Map<String, Object>> ppoList) throws Exception {
		if (ppoList != null) {
			String ppo_id = ePicturePlaceObject_Ser.getPrimaryKey();
			for (Map<String, Object> theMap : ppoList) {
				theMap.putAll(addMap);
				if (!noHasMap(theMap, "picture_id")
						|| !noHasMap(theMap, "html_desc")) {//两个字段存在一个处理
					ItemHelper.mapToMustNull(theMap,"picture_id");
					if (noHasMap(theMap, ppo_id)) {// 不存在添加
						ePicturePlaceObject_Ser.addRowOnlyId(theMap);
					} else {// 修改
						ePicturePlaceObject_Ser.update(theMap.get(ppo_id),
								theMap);
					}
				}
			}
		}
	}

	private boolean noHasMap(Map<String, Object> theMap, String key) {
		boolean has = false;
		if (!theMap.containsKey(key) || ItemHelper.isEmpty(theMap.get(key))) {
			has = true;
		}
		return has;
	}	
}
