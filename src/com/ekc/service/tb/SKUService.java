package com.ekc.service.tb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.ekc.ifc.TableUseIfc;
import com.ekc.service.TableUseAbs;
import com.ekc.util.ItemHelper;
import com.ekc.util.JoinTable;
import com.ekc.util.WhereTable;
import com.ekc.xml.MessageXml;
import com.ekc.xml.TName;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * 商品统一编号信息表
 * 
 * @author hui
 */
@Service("eSKU_Ser")
public class SKUService extends TableUseAbs {
	@Autowired
	TableUseIfc ePicturePlaceObject_Ser;
	@Autowired
	TableUseIfc eGoodsAttributeValues_Ser;
	@Autowired
	AllService allService;
	String att_key = "attributes_commons";
	String ppo = "ppo";

	@Override
	public String getTable() {
		return TName.eSKU;
	}

	@Override
	public String getPrimaryKey() {
		return "sku_id";
	}

	public Map<String, Object> findRow(Map<String, Object> map)
			throws Exception {
		List<Map<String, Object>> list = findList(map);
		return (list != null && list.size() > 0) ? list.get(0) : null;
	}

	/**
	 * @throws Exception
	 * @see 删除SKU，如果包含图片位置，则先删除图片位置 如果不包含图片位置，则直接删除SKU
	 */
	public int delete(Map<String, Object> map) throws Exception {
		List<Map<String, Object>> skus = null;
		if (map.containsKey("sku_id")) {
			skus = ePicturePlaceObject_Ser.findList("sku_id", map.get("sku_id")
					.toString());
		}

		int ci = 0;
		if ((skus != null && skus.size() > 0)) {
			int i = ePicturePlaceObject_Ser.delete("sku_id", map.get("sku_id")
					.toString());
			if (i > 0) {
				ci = delete(getTable(), map);
			}
		} else {
			ci = delete(getTable(), map);
		}
		return ci;
	}

	@Override
	public Map<String, Object> findPage(Map<String, Object> map)
			throws Exception {
		Map<String, Object> mapList = selectPage(getTable(), "*", map);
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> list = (List<Map<String, Object>>) mapList
				.get(MessageXml.list_key);
		getList(list);
		mapList.put(MessageXml.list_key, list);
		return mapList;
	}

	@Override
	public List<Map<String, Object>> findList(Map<String, Object> map)
			throws Exception {
		boolean refSku = false;
		if (map.containsKey("refSku")) {
			refSku = true;
			map.remove("refSku");
		}
		List<Map<String, Object>> list = select(getTable(), "*", map);
		if (refSku) {
			for (Map<String, Object> skuMap : list) {
				updateSKU(skuMap.get("goods_id").toString(),
						skuMap.get("sku_id").toString());
			}
			return null;
		} else {
			getList(list);
		}
		return list;
	}

	/**
	 * 添加查看list的处理
	 * 
	 * @param list
	 * @throws Exception
	 */
	private void getList(List<Map<String, Object>> list) throws Exception {
		Map<String, Object> goodsKeyMap = ItemHelper.findValueToFiled(list);
		String sku_id = getPrimaryKey();
		WhereTable whereObj = new WhereTable("sku_id", goodsKeyMap.get(sku_id));
		List<Map<String, Object>> ppoList = ePicturePlaceObject_Ser
				.findList(whereObj.getMap());
		Map<String, Object> ppoMap = ItemHelper.findFiledToKey(ppoList, sku_id);
		whereObj.put("is_sku", "1");
		whereObj.put("ifnull(attribute_value_id,'') != ? ", "");
		List<Map<String, Object>> attList = eGoodsAttributeValues_Ser
				.findList(whereObj.getMap());
		Map<String, Object> attMap = ItemHelper.findFiledToKey(attList, sku_id);
		Map<String, Object> skuMap = null;
		GsonBuilder gbu = new GsonBuilder();
		Gson gson = gbu.create();
		Object skuObj = null;
		for (int i = 0, len = list.size(); i < len; i++) {
			skuMap = list.get(i);
			skuObj = skuMap.get("sku_name");
			if (!ItemHelper.isEmpty(skuObj)) {// 让页面调用不至于使字符串形式
				list.get(i).put("sku_name",
						gson.fromJson(skuObj.toString(), Map.class));
			}
			skuObj = skuMap.get("side_sku_name");
			if (!ItemHelper.isEmpty(skuObj)) {// 让页面调用不至于使字符串形式
				list.get(i).put("side_sku_name",
						gson.fromJson(skuObj.toString(), Map.class));
			}
			list.get(i).put(ppo, ppoMap.get(skuMap.get(sku_id)));
			list.get(i).put(att_key, attMap.get(skuMap.get(sku_id)));
		}
	}

	/**
	 * @see 添加,商品属性值表(e_goodsattributevalues) 将 json attributes_commons[] 里
	 *      属性值ID(attribute_value_id) 为空的数据删除
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	public int addRowOnlyId(Map<String, Object> map) throws Exception {
		if(map.containsKey("side_sku_name")){
			map.remove("side_sku_name");
		}
		if(map.containsKey("erp_price_id")){
		    map.remove("erp_price_id");
		}
		String sku_id = "";
		if (map.containsKey("sku_id")) {
			sku_id = map.get("sku_id").toString();
		}
		int sku_code = 1, ac_code[] = null;// 插入返回结果
		List<Map<String, Object>> ppoList = null;
		if (map.containsKey(ppo)) {
			// 获得图片位置信息
			ppoList = (List<Map<String, Object>>) map.get(ppo);
			map.remove(ppo);
		}
		List<Map<String, Object>> attributes_commons = null;
		if (map.containsKey(att_key)) {
			// 接收商品属性值数据列表
			attributes_commons = (List<Map<String, Object>>) map.get(att_key);
			map.remove(att_key);
		}
		String sku_key = this.getPrimaryKey();// 添加or修改goods表
		String primaryKey_value = null;// goods_value
		if (map.containsKey(sku_key) && !ItemHelper.isEmpty(map.get(sku_key))) {
			primaryKey_value = map.get(sku_key).toString();
		} else {
			primaryKey_value = ItemHelper.createPrimaryKey();
		}
		if (map.containsKey("sku_name") && map.get("sku_name") != null) {
			Map<String, Object> theMap = null;
			try {
				theMap = (Map<String, Object>) map.get("sku_name");
			} catch (Exception e) {
				GsonBuilder gbu = new GsonBuilder();
				Gson gson = gbu.create();
				theMap = gson.fromJson(map.get("sku_name").toString(),
						Map.class);
			}
			map.put("sku_name", getSkuName(theMap));
		}
		if (map.containsKey(sku_key) && !ItemHelper.isEmpty(map.get(sku_key))) {
			sku_code = update(sku_key, map.get(sku_key), map);
		} else {
			map.put(sku_key, primaryKey_value);// 添加主键值
			try {
				sku_code = addRow(map);// 将map数据插入goods表
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		String goods_id = map.get("goods_id").toString();
		if (!ItemHelper.isEmpty(primaryKey_value)) {
			Map<String, Object> addMap = new HashMap<String, Object>();
			addMap.put(sku_key, primaryKey_value);// 引用同一个sku_id的值
			addMap.put("goods_id", goods_id);// 引用同一个goods_id的值
			ac_code = allService.doGav(addMap, attributes_commons);// 添加or修改
																	// 商品属性值表
			allService.addPicturePlaceObject(addMap, ppoList);
		}

		// 同步ERP里的价格信息 goods_id,sku_id
		if (attributes_commons.size() > 0) {
			// Download(goods_id, primaryKey_value);
			updateSKU(goods_id, primaryKey_value);
		}
		return sku_code;
	}

	private String getSkuName(Map<String, Object> map) {
		GsonBuilder gbu = new GsonBuilder();
		Gson gson = gbu.create();
		return gson.toJson(map);
	}

	/**
	 * 通过sku_id 获得组成的sku_name
	 * 
	 * @param sku_id
	 * @return
	 */
	@SuppressWarnings("unused")
	@Deprecated
	private String getSkuName(String sku_id) {
		JoinTable skuTable = new JoinTable(TName.eGoodsAttributesValue, "a");
		skuTable.leftJoin(TName.eAttributes, "b",
				"a.attribute_id=b.attribute_id");
		skuTable.leftJoin(TName.eAttributeValues, "c",
				"a.attribute_value_id like CONCAT('%',c.attribute_value_id,'%')");
		WhereTable where = new WhereTable("a.sku_id", sku_id);
		where.put("add", "order by a.attribute_id");
		List<Map<String, Object>> list = select(
				skuTable.toString(),
				"b.attribute_name as p_name,ifnull(c.attribute_value,'') as p_value",
				where.getMap());
		Map<String, Object> skuMap = new HashMap<String, Object>();
		String key = null;
		String v = "";
		List<String> keyList = new ArrayList<String>();

		for (Map<String, Object> map : list) {
			key = map.get("p_name").toString();
			if (skuMap.containsKey(key)) {
				v = skuMap.get(key).toString() + ",";
			} else {
				v = "";
				keyList.add(key);
			}
			skuMap.put(key, v + map.get("p_value"));
		}
		String sku_nameJson = "{";
		for (String key_the : keyList) {
			sku_nameJson += "\"" + key_the + "\":\"" + skuMap.get(key_the)
					+ "\",";
		}
		if (keyList.size() > 0) {
			sku_nameJson += sku_nameJson
					.substring(0, sku_nameJson.length() - 1);
		}
		sku_nameJson += "}";
		return sku_nameJson;
	}

	/**
	 * 同步ERP里的价格信息 goods_id
	 * 
	 * @param goods_id
	 * @return
	 */
	public void Download(String goods_id, String sku_id) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("select c.attribute_value ");
		sql.append("from e_GoodsAttributeValues a ");
		sql.append("  left join e_Attributes b on a.attribute_id = b.attribute_id ");
		sql.append("  left join e_AttributeValues c on a.attribute_value_id = c.attribute_value_id ");
		sql.append("where b.attribute_name='产品编号' ");
		sql.append("  and a.goods_id = ? ");
		sql.append("  and a.sku_id = ? ");

		String attribute_value = "";
		List<Map<String, Object>> list_value = jdbcTemplate.queryForList(
				sql.toString(), goods_id, sku_id);
		for (int i = 0, length = list_value.size(); i < length; i++) {
			attribute_value = list_value.get(i).get("attribute_value")
					.toString();
		}
		getMatcodeInErp(attribute_value);
	}
    /**
     * 从erp中读取matcode设置信息
     * @param matcode
     * @throws Exception
     */
	public void getMatcodeInErp(String matcode) throws Exception {
		Map<String, Object> where = new HashMap<String, Object>();
		where.put("matcode", matcode);
		List<Map<String, Object>> list_ma = select(TName.eERPPrice, "1", where);
		if (list_ma.size() <= 0) {
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			if (!"".equals(matcode)) {
				list = ItemHelper.getERPList(2,	matcode.concat("|所有|所有"));
			}
			if (list.size() > 0) {
				insert(TName.eERPPrice, list);
			}
		}
	}

	public void updateSKU(String goods_id, String sku_id) throws Exception {
		List<Map<String, Object>> list = getAttrList(goods_id, sku_id);
		String matcode = "";
//		String special = "";
		String level = "";
		String side_sku_name = "";
		Map<String, Object> map = new HashMap<String, Object>();
	
		Map<String, Object> where = new HashMap<String, Object>();
		int length = list.size();
		if (length > 0) {
			for (Map<String, Object> attMap : list) {
				// level = list.get(i).get("等级").toString();
				if (attMap.get("属性").equals("产品编号")) {
					matcode = attMap.get("属性值").toString();
				}
//				if (attMap.get("属性").equals("规格")) {
//					special = attMap.get("属性值").toString();
//				}
				if (attMap.get("属性").equals("等级")) {
					level = attMap.get("属性值").toString();
				}
				map.put(attMap.get("属性").toString(), attMap.get("属性值"));
			}
			GsonBuilder gbuilder = new GsonBuilder();
			Gson gson = gbuilder.create();
			side_sku_name = gson.toJson(map);
		}
		WhereTable setMap=new WhereTable("side_sku_name", side_sku_name);
		WhereTable wTable=new WhereTable("goods_id", goods_id);
		wTable.put("sku_id", sku_id);
		if (matcode != null) {
			where.put("matcode", matcode);
			//where.put("special", special);
			where.put("cv6", ("优等品".equals(level)||"".equals(level) ? "AAA" : level));
			List<Map<String, Object>> list_price = select(TName.eERPPrice,
					"erp_price_id", where);
			int n = list_price.size();
			if (n > 0) {
				String price_id = list_price.get(0).get("erp_price_id")
						.toString();
				setMap.put("erp_price_id", price_id);
			}else{//erp添加了 而没有更新过来的情况
				getMatcodeInErp(matcode);
			}
		} 
		update(setMap.getMap(), wTable.getMap());
	}
/**
 * 获得对应sku的属性
 * @param goods_id
 * @param sku_id
 * @return
 * @throws DataAccessException
 */
	private List<Map<String, Object>> getAttrList(String goods_id, String sku_id)
			throws DataAccessException {
		StringBuffer sql = new StringBuffer();
		sql.append(" select gv.* , av.attribute_value as '属性值',a.attribute_name as '属性'");
		sql.append(" from e_GoodsAttributeValues gv ");
		sql.append(" LEFT JOIN e_AttributeValues av on(gv.attribute_id=av.attribute_id and gv.attribute_value_id=av.attribute_value_id)");
		sql.append(" LEFT JOIN e_Attributes a on(a.attribute_id=gv.attribute_id) ");
		sql.append(" where (gv.goods_id=? and IFNULL(gv.sku_id,'')='') or gv.sku_id=? ");
	    List<Map<String, Object>> list = jdbcTemplate.queryForList(
				sql.toString(), goods_id, sku_id);
		return list;
	}
}
