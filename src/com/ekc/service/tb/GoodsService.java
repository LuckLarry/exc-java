package com.ekc.service.tb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Service;

import com.ekc.ifc.TableUseIfc;
import com.ekc.service.TableUseAbs;
import com.ekc.util.ItemHelper;
import com.ekc.util.JoinTable;
import com.ekc.util.WhereTable;
import com.ekc.xml.MessageXml;
import com.ekc.xml.TName;

/**
 * 商品基本信息表
 * 
 * 
 * @author hui 仅存储商品的基础信息
 */
@Service("eGoods_Ser")
public class GoodsService extends TableUseAbs {
	@Autowired
	TableUseIfc eSKU_Ser;
	@Autowired
	TableUseIfc eGoodsAttributeValues_Ser;
	@Autowired
	AllService allService;
	@Autowired
	TableUseIfc ePicturePlaceObject_Ser;// 图片位置
	@Autowired
	TableUseIfc eBrand_Ser;
	@Autowired
	TableUseIfc eCategory_Ser;
	@Autowired
	SimpleJdbcTemplate simpleJdbcTemplate;

	String att_key = "attributes_commons";
	String ppo = "ppo";

	@Override
	public String getTable() {
		return TName.eGoods;
	}

	@Override
	public String getPrimaryKey() {
		return "goods_id";
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	public Map<String, Object> findPage(Map<String, Object> map)
			throws Exception {
		Map<String, Object> pageMap = findGoodPage(map);
		List<Map<String, Object>> list = (List<Map<String, Object>>) pageMap
				.get(MessageXml.list_key);
		getList(list, true);
		pageMap.put(MessageXml.list_key, list);
		return pageMap;
	}
	private Map<String,Object> findGoodPage(Map<String, Object> mapWhere) throws Exception {
		Map<String, Object> orMap=null;
		JoinTable joinTable=new JoinTable(TName.eSKU, "s");
		joinTable.leftJoin(TName.eGoods, "g", "s.goods_id=g.goods_id");
		Map<String,Object> copyMap=new HashMap<String, Object>();
		copyMap.putAll(mapWhere);
		if(mapWhere.containsKey("sousuo")){//搜索
			String souStr=mapWhere.get("sousuo").toString();
			orMap=new HashMap<String,Object>();
			orMap.put("g.goods_name like ?","%"+souStr+"%");
			mapWhere.remove("sousuo");
			orMap.put("s.side_sku_name like ?", "%"+souStr+"%");
			copyMap.remove("sousuo");
		}else{
			return getGoodPage(mapWhere);
//			List<Map<String, Object>> attrList=simpleJdbcTemplate.queryForList("SELECT DISTINCT attribute_name,is_sku,attribute_id from "+TName.eAttributes);
//			String attrStr=null;
//			if(mapWhere.containsKey("category_id")){//分类
//				if( mapWhere.get("category_id") instanceof List ){
//					mapWhere.put("g.category_id",mapWhere.get("category_id"));
//				}else{
//					List<Map<String, Object>> caList=simpleJdbcTemplate.queryForList("select a.category_id from e_Category a LEFT JOIN e_Category b on (b.category_id=?) where a.tree_row_id like CONCAT(b.tree_row_id,'%') ", mapWhere.get("category_id"));
//					Map<String, Object> caMap= ItemHelper.findValueToFiled(caList);
//					mapWhere.put("g.category_id",caMap.get("category_id"));
//				}
//				mapWhere.remove("category_id");
//			}
//			if(mapWhere.containsKey("brand_id")){//品牌 从goods表查询
//				mapWhere.put("g.brand_id", mapWhere.get("brand_id"));
//				mapWhere.remove("brand_id");
//			}
//			boolean has=false;
//			//添加价格区间处理
//			if(mapWhere.containsKey("maxPrice")){//最大价格
//				mapWhere.put("ep.price <= ?", mapWhere.get("maxPrice"));
//				has=true;
//				mapWhere.remove("maxPrice");
//			}
//			if(mapWhere.containsKey("minPrice")){//最小价格
//				mapWhere.put("ep.price > ?", mapWhere.get("minPrice"));
//				has=true;
//				mapWhere.remove("minPrice");
//			}
//			if(mapWhere.containsKey("dictvalue")){//判断仓库，因为与价格放在同一地方  即与价格在同一条语句筛选
//				mapWhere.put("ep.dictvalue like ?", "%"+mapWhere.get("dictvalue")+"%");
//				has=true;
//				mapWhere.remove("dictvalue");
//			}
//			if(has){//存在要查erp设置信息时连接对应表
//				joinTable.leftJoin(TName.eERPPrice, "ep", "s.erp_price_id=ep.erp_price_id");
//			}
//			for (int i = 0,len=attrList.size(); i < len ; i++) {
//				attrStr=attrList.get(i).get("attribute_name").toString();
//				if(mapWhere.containsKey(attrStr)){//最后一步 清除特殊搜索信息
//					mapWhere.put("s.side_sku_name like ?", "%\""+attrStr+"\":\""+mapWhere.get(attrStr).toString()+"\"%");
//					mapWhere.remove(attrStr);//去除属性
//				}
//			}
		}

		if(mapWhere.containsKey("goods_id")){
		    mapWhere.put("s.goods_id",mapWhere.get("goods_id"));
		    mapWhere.remove("goods_id");
		}
		if(orMap!=null){
			mapWhere.put("or", orMap);
		}
		Map<String,Object> goodsList=ItemHelper.findValueToFiled(select(joinTable.toString(), "DISTINCT s.goods_id", mapWhere));//goods_id 与sku_id 同时存在为需要搜索的结果
		copyMap.put("goods_id", goodsList.get("goods_id"));
		return selectPage(getTable(), "*", copyMap);
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Map<String, Object> getGoodPage(Map<String, Object> map) {
		// 形状：正方形
		// 获取特殊属性id
		List<String> attrList = new ArrayList<String>();
		attrList.add("系列");
		attrList.add("品牌");
		attrList.add("形状");
		attrList.add("规格");
		attrList.add("颜色");
		attrList.add("质面");
		attrList.add("等级");
		String attrStr = null;
		String findSql = "SELECT DISTINCT goods_id from  e_GoodsAttributeValues";
		boolean findBoo = false;
		for (int i = 0, len = attrList.size(); i < len; i++) {
			attrStr = attrList.get(i);
			if (map.containsKey(attrStr)) {
				// 最后一步 清除特殊搜索信息
				findSql += ((findBoo) ? " and " : " where ")
						+ "goods_id in (SELECT DISTINCT ga.goods_id FROM e_GoodsAttributeValues ga where ga.attribute_value_id in (select attribute_value_id from e_AttributeValues a where a.attribute_value = '"
						+ map.get(attrStr) + "'))";
				findBoo = true;
				map.remove(attrStr);// 去除属性
			}
		}
		if (findBoo) {
			Map<String, Object> goodMap = ItemHelper
					.findValueToFiled(simpleJdbcTemplate.queryForList(findSql));
			List<String> goodList = (List<String>) goodMap.get("goods_id");
			Object objva = null;
			if (map.containsKey("goods_id")) {// 存在时
				objva = map.get("goods_id");
				if (objva instanceof List) {
					goodList.addAll((List) objva);
				} else {
					goodList.add(objva.toString());
				}
			}
			map.put("goods_id", goodList);
		}
		return selectPage(getTable(), "*", map);
	}

	@Override
	public List<Map<String, Object>> findList(Map<String, Object> map)
			throws Exception {
		List<Map<String, Object>> list = select(getTable(), "*", map);
		// 2016-01-25 Gaohui LJY memo:goods_id有值的时候(单条数据查询),不要 goods_name2 字段
		boolean bool = true;
		if (map.containsKey("goods_id")) {
			if (map.get("goods_id").toString().length() > 0) {
				bool = false;
			}
		}
		getList(list, bool);
		return list;
	}

	/**
	 * 添加查看list的处理
	 * 
	 * @param list
	 * @throws Exception
	 */
	private void getList(List<Map<String, Object>> list, boolean bool)
			throws Exception {
		Map<String, Object> goodsKeyMap = ItemHelper.findValueToFiled(list);
		String goods_id = getPrimaryKey();
		WhereTable whereObj = new WhereTable("goods_id",
				goodsKeyMap.get(goods_id));
		// 2016-01-26 Gaohui LJY memo:查询sku_id为空的数据
		whereObj.put("sku_id", "");
		List<Map<String, Object>> ppoList = ePicturePlaceObject_Ser
				.findList(whereObj.getMap());
		Map<String, Object> ppoMap = ItemHelper.findFiledToKey(ppoList,
				goods_id);
		whereObj.put("is_sku", "0");
		List<Map<String, Object>> attList = eGoodsAttributeValues_Ser
				.findList(whereObj.getMap());
		Map<String, Object> attMap = ItemHelper.findFiledToKey(attList,
				goods_id);
		Map<String, Object> goodMap = null;
		Object keyV = null;
		for (int i = 0, len = list.size(); i < len; i++) {
			goodMap = list.get(i);
			keyV = goodMap.get(goods_id);
			if (bool) {
				list.get(i).put("goods_name2",
						getAttributesName(keyV.toString()));
			}
			list.get(i).put(ppo, ppoMap.get(keyV));
			list.get(i).put(att_key, attMap.get(keyV));
			// 添加 goods_title2 list.get(i).
			String brand_id = list.get(i).get("brand_id").toString();
			String category_id = list.get(i).get("category_id").toString();
			String goods_name = list.get(i).get("goods_name").toString();
			list.get(i).put("goods_title2",
					this.goods_title2(brand_id, category_id, goods_name));
		}
	}

	private Map<String, Object> getAttributesName(String goods_id) {
		JoinTable theTable = new JoinTable(TName.eGoodsAttributesValue, "a");
		theTable.leftJoin(TName.eAttributes, "b",
				"a.attribute_id=b.attribute_id");
		theTable.leftJoin(TName.eAttributeValues, "c",
				"a.attribute_value_id like CONCAT('%',c.attribute_value_id,'%')");
		WhereTable where = new WhereTable("a.goods_id", goods_id);
		where.put("a.is_sku", 0);
		where.put("add", "order by a.attribute_id");
		List<Map<String, Object>> list = select(
				theTable.toString(),
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
		return skuMap;
	}

	public Map<String, Object> findRow(Map<String, Object> map)
			throws Exception {
		List<Map<String, Object>> list = findList(map);
		Map<String, Object> skuMap = null;
		if (list != null && list.size() > 0) {
			skuMap = list.get(0);
		}
		return skuMap;
	}

	/**
	 * @throws Exception
	 * @see 需要程序处理树节点
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	public int addRowOnlyId(Map<String, Object> map) throws Exception {
		int good_code = 0, ac_code[] = null;// 插入返回结果
		List<Map<String, Object>> attributes_commons = null;
		if (map.containsKey(att_key)) {
			attributes_commons = (List<Map<String, Object>>) map.get(att_key);// 接收商品属性值数据列表
			map.remove(att_key);
		}
		List<Map<String, Object>> ppoList = null;
		if (map.containsKey(ppo)) {
			ppoList = (List<Map<String, Object>>) map.get(ppo);// 获得图片位置信息
			map.remove(ppo);
		}

		String good_key = this.getPrimaryKey();// 添加or修改goods表
		String primaryKey_value = null;// goods_value
		if (map.containsKey(good_key) && !ItemHelper.isEmpty(map.get(good_key))) {
			primaryKey_value = map.get(good_key).toString();
		} else {
			primaryKey_value = ItemHelper.createPrimaryKey();
		}
		if (map.containsKey(good_key) && !ItemHelper.isEmpty(map.get(good_key))) {
			good_code = update(map.get(good_key), map);
		} else {
			map.put(good_key, primaryKey_value);// 添加主键值
			try {
				good_code = addRow(map);// 将map数据插入goods表
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (!ItemHelper.isEmpty(primaryKey_value)) {
			Map<String, Object> addMap = new HashMap<String, Object>();
			addMap.put(good_key, primaryKey_value);// 引用同一个goods_id的值
			ac_code = allService.doGav(addMap, attributes_commons);// 添加or修改
																	// 商品属性值表
			allService.addPicturePlaceObject(addMap, ppoList);
		}
		return good_code;
	}

	public int update(Map<String, Object> map) throws Exception {
		return addRowOnlyId(map);
	}

	/**
	 * 删除商品，如果商品中包含商品属性或者SKU，则先删除商品属性或者SKU，再删除商品 如果商品中不包含商品属性，则删除商品
	 * 
	 * @throws Exception
	 */
	public int delete(Map<String, Object> map) throws Exception {
		List<Map<String, Object>> skus = null;
		List<Map<String, Object>> attributes = null;
		if (map.containsKey("goods_id")) {
			skus = eSKU_Ser
					.findList("goods_id", map.get("goods_id").toString());
			attributes = eGoodsAttributeValues_Ser.findList("goods_id", map
					.get("goods_id").toString());
		}
		int ci = 0;
		if ((skus != null && skus.size() > 0)
				|| (attributes != null && attributes.size() > 0)) {
			int i = eSKU_Ser.delete("goods_id", map.get("goods_id").toString());
			int j = eGoodsAttributeValues_Ser.delete("goods_id",
					map.get("goods_id").toString());
			if (i > 0 || j > 0) {
				ci = delete(getTable(), map);
			}
		} else {
			ci = delete(getTable(), map);
		}
		return ci;
	}

	/**
	 * "goods_title2":"brand_name+category_name+goods_name"
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public String goods_title2(String brand_id, String category_id,
			String goods_name) throws Exception {
		String value = "";
		List<Map<String, Object>> brand = eBrand_Ser.findList(brand_id);
		for (int i = 0, length = brand.size(); i < length; i++) {
			value += brand.get(i).get("brand_name").toString();

		}
		List<Map<String, Object>> category = eCategory_Ser
				.findList(category_id);
		for (int i = 0, length = category.size(); i < length; i++) {
			value += category.get(i).get("category_name").toString();

		}
		value += goods_name;
		return value;
	}
}
