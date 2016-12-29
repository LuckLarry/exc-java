package com.ekc.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ekc.util.ItemHelper;
import com.ekc.util.JoinTable;
import com.ekc.xml.MessageXml;
import com.ekc.xml.TName;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Service("gService")
public class GoodsService extends BaseService {

	public Map<String, Object> selectGoods(Map<String, Object> goodsinfo)
			throws RuntimeException {
		Map<String, Object> whereMap = new HashMap<String, Object>();
		JoinTable gg = new JoinTable(TName.eGoods, "b");// 产品信息表
		gg.leftJoin(TName.eGoodsCategory, "a", "a.goods_id = b.goods_id");// 分类产品对照表
		gg.leftJoin(TName.eBrand, "c", "b.brand_id = c.brand_id");// 商品品牌表
		gg.leftJoin(TName.eWarehouses, "d", "b.goods_id = d.goods_id");// 仓库库存信息表
		for (String key : goodsinfo.keySet()) {
			if (key.equals(MessageXml.page_key)
					|| key.equals(MessageXml.pageSize_key)) {
				whereMap.put(key, goodsinfo.get(key));
			} else if (key.equals("category_id")) {
				whereMap.put("a." + key, goodsinfo.get(key));
			} else {
				whereMap.put(key, goodsinfo.get(key));
			}
		}
		Map<String, Object> reMap = selectPage(gg.toString(),
				"a.*,b.*,c.*,d.*", whereMap, TName.eGoods);

		@SuppressWarnings("unchecked")
		List<Map<String, Object>> list = (List<Map<String, Object>>) reMap
				.get(MessageXml.list_key);
		Map<String, Object> goods = null;
		for (int i = 0; i < list.size(); i++) {
			goods = list.get(i);
			if (goods.containsKey("goods_id")) {
				goods.put("xinghao", getXingHao(""));// 添加商品型号
				goods.put("spec", getSpec(""));// 添加商品规格
				goods.put("xingzhuang", getXingZhuang(""));// 添加商品形状
				goods.put("yangse", getYangSe(""));// 添加商品颜色
				goods.put("zhimian", getZhiMian(""));// 添加商品质面
			}
		}

		return reMap;
	}

	/**
	 * 查询商品 型号
	 * 
	 * @param goods_id
	 * @return
	 */
	public Map<String, Object> getXingHao(String goods_id) {
		Map<String, Object> mapRe = new HashMap<String, Object>();
		mapRe.put("xinghao1", "600X720");
		return mapRe;
	}

	/**
	 * 查询商品 规格
	 * 
	 * @param goods_id
	 * @return
	 */
	public Map<String, Object> getSpec(String goods_id) {
		Map<String, Object> mapRe = new HashMap<String, Object>();
		mapRe.put("spec", "1200X1690");
		return mapRe;
	}

	/**
	 * 查询商品 形状
	 * 
	 * @param goods_id
	 * @return
	 */
	public Map<String, Object> getXingZhuang(String goods_id) {
		Map<String, Object> mapRe = new HashMap<String, Object>();
		mapRe.put("xingzhuang", "多边形");
		return mapRe;
	}

	/**
	 * 查询商品 颜色
	 * 
	 * @param goods_id
	 * @return
	 */
	public Map<String, Object> getYangSe(String goods_id) {
		Map<String, Object> mapRe = new HashMap<String, Object>();
		mapRe.put("yangse", "red");
		return mapRe;
	}

	/**
	 * 查询商品 质面
	 * 
	 * @param goods_id
	 * @return
	 */
	public Map<String, Object> getZhiMian(String goods_id) {
		Map<String, Object> mapRe = new HashMap<String, Object>();
		mapRe.put("zhimian", "光面");
		return mapRe;
	}

	public List<Map<String, Object>> getCategoryList(
			Map<String, Object> whereMap) {
		return select(TName.eCategory, "*", whereMap);// category_id,category_name,category_image,row_id,parent_row_id,tree_row_id,show_in_nav
	}

	/**
	 * node 0查询所有分类，1查询一级，2查询二级 没有传进来默认1级
	 * 
	 * @param goodsinfo
	 * @return 查出分类集合
	 */
	public List<Map<String, Object>> selectAtegory(Map<String, Object> goodsinfo) {
		Map<String, Object> whereMap = new HashMap<String, Object>();
		whereMap.putAll(goodsinfo);
		whereMap.put("add", "order by tree_row_id");
		List<Map<String, Object>> li = getCategoryList(whereMap);// 获得查找数据

		Map<String, Object> mapStr = ItemHelper.findValueToFiled(li);
		@SuppressWarnings("unchecked")
		List<String> listStr = (List<String>) mapStr.get("row_id");
		Map<String, Object> whereRowMap = new HashMap<String, Object>();
		whereRowMap.put("parent_row_id", listStr);
		List<Map<String, Object>> liAll = getCategoryList(whereRowMap);
		Map<String, Object> adMap = ItemHelper.findFiledToKey(liAll,
				"parent_row_id");// 获得子类对应数据

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String vString = null;
		for (Map<String, Object> rowMap : li) {
			vString = rowMap.get("row_id").toString();
			if (adMap.containsKey(vString)) {// 判断子类是否存在数据
				rowMap.put(MessageXml.nodeChild_key, adMap.get(vString));
			} else {
				rowMap.put(MessageXml.nodeChild_key, null);
			}
			list.add(rowMap);
		}
		return list;
	}

	public List<Map<String, Object>> selectRandom(Map<String, Object> goodsinfo)
			throws RuntimeException {
		if (!goodsinfo.containsKey("page_size")) {
			goodsinfo.put("page_size", 10);
		}
		goodsinfo.put("add", "order by rand() ");
		return select(TName.eGoods, "*", goodsinfo);// goods_name,goods_number,goods_weight,market_price,shop_price
	}

	public Map<String, Object> selectSingle(Map<String, Object> goodsinfo)
			throws RuntimeException {
		return selectPage(TName.eGoods, "*", goodsinfo);// goods_name,goods_number,goods_weight,market_price,shop_price
	}

	public Map<String, Object> getSKU(String goodsId) {
		Map<String, Object> whereMap = new HashMap<String, Object>();
		whereMap.put("goods_id", goodsId);
		return getSKU(whereMap);
	}

	public Map<String, Object> getSKU(Map<String, Object> whereMap) {
		Map<String, Object> rowMap = selectRow(TName.eSKU, "*", whereMap);
		whereMap.clear();
		// rowMap.put("xinghao",
		// getXingHao(rowMap.get("goods_id").toString()));// 添加商品型号
		// rowMap.put("spec", getSpec(rowMap.get("goods_id").toString()));//
		// 添加商品规格
		// rowMap.put("xingzhuang", getXingZhuang(rowMap.get("goods_id")
		// .toString()));// 添加商品形状
		// rowMap.put("yangse", getYangSe(rowMap.get("goods_id").toString()));//
		// 添加商品颜色
		// rowMap.put("zhimian",
		// getZhiMian(rowMap.get("goods_id").toString()));// 添加商品质面
		// rowMap.put("goods", getGoods(rowMap.get("goods_id").toString()));//
		// 添加Goods表里的某些数据
		List<Map<String, Object>> list = null;
		if (rowMap != null){
			rowMap.put("xinghao", "");// 添加商品型号
			rowMap.put("spec", "");// 添加商品规格
			rowMap.put("xingzhuang", "");// 添加商品形状
			rowMap.put("yangse", "");// 添加商品颜色
			rowMap.put("zhimian", "");// 添加商品质面
			rowMap.put("goods", getGoods(rowMap.get("goods_id").toString()));// 添加Goods表里的某些数据
			whereMap.put("goods_id", rowMap.get("goods_id"));
			list = getGImg(whereMap);
		}
		rowMap.put(MessageXml.imgList_key, list);
		return rowMap;
	}

	/**
	 * 查询Goods表里的某些数据
	 * 
	 * @param goods_id
	 * @return
	 */
	public Map<String, Object> getGoods(String goods_id) {
		Map<String, Object> mapRe = new HashMap<String, Object>();
		mapRe.put("goods_id", goods_id);
		return selectRow(TName.eGoods,
				"goods_weight,market_price,shop_price,goods_desc", mapRe);
	}

	public List<Map<String, Object>> getGImg(Map<String, Object> whereMap) {
		return select(TName.eGoodsImage, "*", whereMap);// img_id,old_img_id,goods_id,img_url,img_desc,thumb_url,img_original
	}
	
	//查SKU
	/**
	 * author:Gaohui
	 * mdate:2016-01-27
	 * from:ljy
	 * @param mapWhere
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked"})
	public Map<String, Object> getSKUList(Map<String, Object> mapWhere) throws Exception {
//		Map<String, Object> map = getListInSku(mapWhere);//第一种
		Map<String, Object> map = findSkuPage(mapWhere);	
		//TODO 前面的筛选功能  后面为@高辉
		List<Map<String, Object>> list = (List<Map<String, Object>>)map.get("list");
		GsonBuilder gbu = new GsonBuilder();
		Gson gson = gbu.create();
		Object skuObj=null;
		for (int i = 0, length = list.size(); i < length; i++) {
			skuObj= list.get(i).get("sku_name");
			if (!ItemHelper.isEmpty(skuObj)) {//让页面调用不至于使字符串形式
				list.get(i).put("sku_name", gson.fromJson(skuObj.toString(), Map.class));
			}
			skuObj=list.get(i).get("side_sku_name");
			if (!ItemHelper.isEmpty(skuObj)) {//让页面调用不至于使字符串形式
				list.get(i).put("side_sku_name", gson.fromJson(skuObj.toString(), Map.class));
			}
			//measure_unit 单位
			String goods_id = list.get(i).get("goods_id").toString();
//			Map<String, Object> goods = getGoodsField(goods_id);
//			if(ItemHelper.isEmpty(list.get(i).get("goods_unit"))){
//				list.get(i).put("measure_unit",getCategoryField(goods_id));
//			}else{
				list.get(i).put("measure_unit",list.get(i).get("goods_unit"));
//			}
			//brand_id 品牌
			String brand_id =list.get(i).get("brand_id").toString();// goods.get("brand_id").toString();
			Map<String, Object> brand = getBrandField(brand_id);
			list.get(i).put("brand_id",brand_id);
			list.get(i).put("brand_name",brand.get("brand_name").toString());
			//销量
			list.get(i).put("销量", "0");
			list.get(i).put("goods_name",list.get(i).get("goods_name").toString());//goods.get("goods_name").toString()
			list.get(i).put("picture", getPicture(goods_id));
		}
		return map;
	}
	String temTable="tmp_GoodsAttributeValues";
	StringBuffer temBuffer=null;
	private Map<String,Object> findSkuPage(Map<String, Object> mapWhere) throws Exception {
		List<Map<String, Object>> attrList=simpleJdbcTemplate.queryForList("SELECT DISTINCT attribute_name,is_sku,attribute_id from "+TName.eAttributes);
		String attrStr=null;
		Map<String, Object> orMap=null;
		JoinTable joinTable=new JoinTable(TName.eSKU, "s");
		joinTable.leftJoin(TName.eGoods, "g", "s.goods_id=g.goods_id");
		joinTable.leftJoin(TName.eERPPrice, "ep", "s.erp_price_id=ep.erp_price_id");
		String col="DISTINCT s.*,ep.matname,trim(ep.special) special,ep.uom,ep.cv6,g.brand_id,g.goods_name,g.category_id";
		if(mapWhere.containsKey("sousuo")){//搜索
			String souStr=mapWhere.get("sousuo").toString();
			orMap=new HashMap<String,Object>();
			orMap.put("g.goods_name like ?","%"+souStr+"%");
			mapWhere.remove("sousuo");
			orMap.put("s.side_sku_name like ?", "%"+souStr+"%");			
			orMap.put("ep.matname like ?", "%"+souStr+"%");
		}else{
			if(mapWhere.containsKey("category_id")){//分类
				List<Map<String, Object>> caList=simpleJdbcTemplate.queryForList("select a.category_id from e_Category a LEFT JOIN e_Category b on (b.category_id=?) where a.tree_row_id like CONCAT(b.tree_row_id,'%') ", mapWhere.get("category_id"));
				Map<String, Object> caMap= ItemHelper.findValueToFiled(caList);
				mapWhere.put("g.category_id",caMap.get("category_id"));
				mapWhere.remove("category_id");
			}
			if(mapWhere.containsKey("brand_id")){//品牌 从goods表查询
				mapWhere.put("g.brand_id", mapWhere.get("brand_id"));
				mapWhere.remove("brand_id");
			}
			//添加价格区间处理
			if(mapWhere.containsKey("maxPrice")){//最大价格
				mapWhere.put("ep.price <= ?", mapWhere.get("maxPrice"));
				mapWhere.remove("maxPrice");
			}
			if(mapWhere.containsKey("minPrice")){//最小价格
				mapWhere.put("ep.price > ?", mapWhere.get("minPrice"));
				mapWhere.remove("minPrice");
			}
			if(mapWhere.containsKey("dictvalue")){//判断仓库，因为与价格放在同一地方  即与价格在同一条语句筛选
				mapWhere.put("ep.dictvalue like ?", "%"+mapWhere.get("dictvalue")+"%");
				mapWhere.remove("dictvalue");
			}
			for (int i = 0,len=attrList.size(); i < len ; i++) {
				attrStr=attrList.get(i).get("attribute_name").toString();
				if(mapWhere.containsKey(attrStr)){//最后一步 清除特殊搜索信息
					mapWhere.put("s.side_sku_name like ?", "%\""+attrStr+"\":\""+mapWhere.get(attrStr).toString()+"\"%");
					mapWhere.remove(attrStr);//去除属性
				}
			}
		}
		if(mapWhere.containsKey("goods_id")){
		    mapWhere.put("s.goods_id",mapWhere.get("goods_id"));
		    mapWhere.remove("goods_id");
		}
		if(orMap!=null){
			mapWhere.put("or", orMap);
		}
		return this.selectPage(joinTable.toString(), col, mapWhere);//goods_id 与sku_id 同时存在为需要搜索的结果
	}
    /**
     * 直接通过条件获取数据
     * @param mapWhere
     * @return
     */
	@SuppressWarnings("unused")
	private Map<String, Object> getListInSku(Map<String, Object> mapWhere) throws Exception {
		List<Map<String, Object>> attrList=simpleJdbcTemplate.queryForList("SELECT DISTINCT attribute_name,is_sku,attribute_id from "+TName.eAttributes);
		String attrStr=null;
//		String andOr=" and ";//不是搜索 为并列关系
		Map<String, Object> orMap=null;
		if(mapWhere.containsKey("sousuo")){//搜索
			String souStr=mapWhere.get("sousuo").toString();
			orMap=new HashMap<String,Object>();
			orMap.put("g.goods_name like ?","%"+souStr+"%");
			mapWhere.remove("sousuo");
//			andOr=" or ";//存在为 或者关系，只需一种存在  都查出来
//			mapWhere.put("and", andOr);
			orMap.put("产品编号", souStr);
		}else{
			if(mapWhere.containsKey("category_id")){//分类
				List<Map<String, Object>> caList=simpleJdbcTemplate.queryForList("select a.category_id from e_Category a LEFT JOIN e_Category b on (b.category_id=?) where a.tree_row_id like CONCAT(b.tree_row_id,'%') ", mapWhere.get("category_id"));
				Map<String, Object> caMap= ItemHelper.findValueToFiled(caList);
				mapWhere.put("g.category_id",caMap.get("category_id"));
				mapWhere.remove("category_id");
			}
			if(mapWhere.containsKey("brand_id")){//品牌 从goods表查询
				mapWhere.put("g.brand_id", mapWhere.get("brand_id"));
				mapWhere.remove("brand_id");
			}
			String sql="select DISTINCT matcode from "+TName.eERPPrice+" where ";//price <= 13
			boolean has=false;
			//添加价格区间处理
			if(mapWhere.containsKey("maxPrice")){//最大价格
				sql+=" price <= "+mapWhere.get("maxPrice");
				has=true;
				mapWhere.remove("maxPrice");
			}
			if(mapWhere.containsKey("minPrice")){//最小价格
				if(has){
					sql+=" and ";
				}
				sql+=" price > "+mapWhere.get("minPrice");
				has=true;
				mapWhere.remove("minPrice");
			}
			if(mapWhere.containsKey("dictvalue")){//判断仓库，因为与价格放在同一地方  即与价格在同一条语句筛选
				if(has){
					sql+=" and ";
				}
				sql+=" dictvalue like '%"+mapWhere.get("dictvalue")+"%'";
				has=true;
				mapWhere.remove("dictvalue");
			}
			if(has){
				List<Map<String, Object>> matList=simpleJdbcTemplate.queryForList(sql);
//				mapWhere.put("价格区间",sql);
				mapWhere.put("价格区间",ItemHelper.getSqlIn(matList, "matcode"));
			}
		}
		JoinTable joinTable=new JoinTable(TName.eSKU, "s");
		joinTable.leftJoin(TName.eGoods, "g", "s.goods_id=g.goods_id");
		String col="DISTINCT s.*";
		String alias=null;
		Map<String, Object> attMap=null;
		String inSql=null;
		for (int i = 0,len=attrList.size(); i < len ; i++) {
			attMap=attrList.get(i);
			attrStr=attMap.get("attribute_name").toString();
			if((Boolean)attMap.get("is_sku")){//是is_sku属性 
				if("产品编号".equals(attrStr)){
					if(mapWhere.containsKey("价格区间")){
						alias="kj"+i;
					    joinTable.leftJoin(TName.eGoodsAttributesValue, alias, "s.sku_id="+alias+".sku_id and "+alias+".attribute_id='"+attMap.get("attribute_id")+"' and IFNULL("+alias+".sku_id,'')!=''");
					    inSql="SELECT attribute_value_id FROM "+TName.eAttributeValues+" WHERE attribute_value in ("+mapWhere.get("价格区间")+")";
					    ItemHelper.setIntToMapKey(mapWhere, i, alias+".attribute_value_id IN ("+ItemHelper.getSqlIn(simpleJdbcTemplate.queryForList(inSql), "attribute_value_id").toString()+")");
					    mapWhere.remove("价格区间");//去除属性
					    col+=","+alias+".attribute_value_id as '价格区间'";
					}
					if(orMap!=null&&orMap.containsKey("产品编号")){
					    //最后一步 清除特殊搜索信息
					    alias="sk"+i;
					    joinTable.leftJoin(TName.eGoodsAttributesValue, alias, "(s.sku_id="+alias+".sku_id  and IFNULL("+alias+".sku_id,'')!='') or (s.goods_id="+alias+".goods_id and IFNULL("+alias+".sku_id,'')='')");
					    inSql="SELECT attribute_value_id FROM "+TName.eAttributeValues+" WHERE attribute_value LIKE '%"+orMap.get(attrStr)+"%'";
					    ItemHelper.setIntToMapKey(orMap, i, alias+".attribute_value_id IN ("+ItemHelper.getSqlIn(simpleJdbcTemplate.queryForList(inSql), "attribute_value_id").toString()+")");
					    orMap.remove(attrStr);//去除属性
					    col+=","+alias+".attribute_value_id as '"+attrStr+"'";
					}
				}
				if(mapWhere.containsKey(attrStr)){
					    //最后一步 清除特殊搜索信息
					    alias="k"+i;
					    joinTable.leftJoin(TName.eGoodsAttributesValue, alias, "s.sku_id="+alias+".sku_id and "+alias+".attribute_id='"+attMap.get("attribute_id")+"' and IFNULL("+alias+".sku_id,'')!=''");
					    inSql="SELECT attribute_value_id FROM "+TName.eAttributeValues+" WHERE attribute_value LIKE '%"+mapWhere.get(attrStr)+"%'";
					    ItemHelper.setIntToMapKey(mapWhere, i, alias+".attribute_value_id IN ("+ItemHelper.getSqlIn(simpleJdbcTemplate.queryForList(inSql), "attribute_value_id").toString()+")");
					    mapWhere.remove(attrStr);//去除属性
					    col+=","+alias+".attribute_value_id as '"+attrStr+"'";
				}
			}else{//不是is_sku属性筛选goods_id
				if("产品编号".equals(attrStr)){
					if(mapWhere.containsKey("价格区间")){
						alias="j"+i;
					    joinTable.leftJoin(TName.eGoodsAttributesValue, alias, "s.goods_id="+alias+".goods_id and "+alias+".attribute_id='"+attMap.get("attribute_id")+"' and IFNULL("+alias+".sku_id,'')='' ");
					    inSql="SELECT attribute_value_id FROM "+TName.eAttributeValues+" WHERE attribute_value in ("+mapWhere.get("价格区间")+")";
					    ItemHelper.setIntToMapKey(mapWhere, i, alias+".attribute_value_id IN ("+ItemHelper.getSqlIn(simpleJdbcTemplate.queryForList(inSql), "attribute_value_id").toString()+")");
					    mapWhere.remove("价格区间");//去除属性
					    col+=","+alias+".attribute_value_id as '价格区间'";
					}
					if(orMap!=null&&orMap.containsKey("产品编号")){
					    //最后一步 清除特殊搜索信息
					    alias="sa"+i;
					    joinTable.leftJoin(TName.eGoodsAttributesValue, alias, "(s.sku_id="+alias+".sku_id  and IFNULL("+alias+".sku_id,'')!='') or (s.goods_id="+alias+".goods_id and IFNULL("+alias+".sku_id,'')='')");
					    inSql="SELECT attribute_value_id FROM "+TName.eAttributeValues+" WHERE attribute_value LIKE '%"+orMap.get(attrStr)+"%'";
					    ItemHelper.setIntToMapKey(orMap, i, alias+".attribute_value_id IN ("+ItemHelper.getSqlIn(simpleJdbcTemplate.queryForList(inSql), "attribute_value_id").toString()+")");
					    orMap.remove(attrStr);//去除属性
					    col+=","+alias+".attribute_value_id as '"+attrStr+"'";
					}
				}
				if(mapWhere.containsKey(attrStr)){
				    alias="a"+i;
				    joinTable.leftJoin(TName.eGoodsAttributesValue, alias, "s.goods_id="+alias+".goods_id and "+alias+".attribute_id='"+attMap.get("attribute_id")+"' and IFNULL("+alias+".sku_id,'')=''");
				    inSql="SELECT attribute_value_id FROM "+TName.eAttributeValues+" WHERE attribute_value LIKE '%"+mapWhere.get(attrStr)+"%'";
				    mapWhere.put(String.valueOf(i), alias+".attribute_value_id IN ("+ItemHelper.getSqlIn(simpleJdbcTemplate.queryForList(inSql), "attribute_value_id").toString()+")");
				    mapWhere.remove(attrStr);//去除属性
				    col+=","+alias+".attribute_value_id as '"+attrStr+"'";
				}
			}
		}
		if(mapWhere.containsKey("goods_id")){
		    mapWhere.put("s.goods_id",mapWhere.get("goods_id"));
		    mapWhere.remove("goods_id");
		}
		if(orMap!=null){
			mapWhere.put("or", orMap);
		}
		return this.selectPage(joinTable.toString(), col, mapWhere);//goods_id 与sku_id 同时存在为需要搜索的结果
	}

	//得到e_category相关信息
	private String getCategoryField(String goods_id) {
		String s = "select category_id from e_Goods where goods_id = ? ";
		Map<String, Object> mapG = jdbcTemplate.queryForMap(s.toString(), goods_id);
		String category_id = mapG.get("category_id").toString();
		StringBuffer sql = new StringBuffer();
		sql.append("select IFNULL(measure_unit,'') as measure_unit,parent_row_id from e_Category where category_id = ? ");
		Map<String, Object> map = jdbcTemplate.queryForMap(sql.toString(), category_id);
		String measure_unit = map.get("measure_unit").toString();
		//如果没有找到，就找父级的单位
		if (ItemHelper.isEmpty(measure_unit)){
			String sql2 = "select IFNULL(measure_unit,'') as measure_unit from e_Category where row_id = ? ";
			map = jdbcTemplate.queryForMap(sql2.toString(), map.get("parent_row_id").toString());
			measure_unit = map.get("measure_unit").toString();
		}
		return measure_unit;
	}
	
	//得到goods相关信息
	private Map<String, Object> getGoodsField(String goods_id) {
		StringBuffer sql = new StringBuffer();
		//sql.append("select category_id,goods_id,goods_name,brand_id from e_Goods where goods_id = ? ");
		sql.append("select * from e_Goods where goods_id = ? ");
		Map<String, Object> map = jdbcTemplate.queryForMap(sql.toString(), goods_id);
		Object sku_name = map.get("sku_name");
		if (!ItemHelper.isEmpty(sku_name)) {//让页面调用不至于使字符串形式
			GsonBuilder gbu = new GsonBuilder();
			Gson gson = gbu.create();
			map.put("sku_name",gson.fromJson(sku_name.toString(), Map.class));
		}
		return map;
	}
	
	//得到brand相关信息
	private Map<String, Object> getBrandField(String brand_id) {
		StringBuffer sql = new StringBuffer();
		//sql.append("select brand_id,brand_name from e_Brand where brand_id = ? ");
		sql.append("select * from e_Brand where brand_id = ? ");
		Map<String, Object> map = jdbcTemplate.queryForMap(sql.toString(), brand_id);
		return map;
	}
	
	// 得到单个商品的图片位置信息
	/**
	 * author:Gaohui
	 * mdate:2016-01-27
	 * from:ljy
	 * @param goods_id
	 * @return
	 */
	public List<Map<String, Object>> getPicture(String goods_id) {
		StringBuffer sql = new StringBuffer();
		sql.append("select distinct ");
		sql.append("a.place_id,a.place_name,b.sku_id,b.goods_id,b.picture_id,b.html_desc,c.picture_name,c.thumbnail_path,c.picture_path ");
		sql.append("from e_PicturePlace a ");
		sql.append("left join e_PicturePlaceObject b on a.place_id = b.place_id ");
		sql.append("left join e_Picture c on b.picture_id = c.picture_id ");
		if (goods_id!=null && goods_id!=""){
			sql.append("where b.goods_id = ? ");
		}
		sql.append("order by right(a.row_id,2) asc ");
		List<Map<String, Object>> list = jdbcTemplate.queryForList(
				sql.toString(), goods_id);
		return list;
	}
	
	//只查询正面图
	public List<Map<String, Object>> getPictureZT(String goods_id) {
		StringBuffer sql = new StringBuffer();
		sql.append("select distinct ");
		sql.append("a.place_id,a.place_name,b.goods_id,b.picture_id,b.html_desc,c.picture_name,c.thumbnail_path,c.picture_path ");
		sql.append("from e_PicturePlace a ");
		sql.append("left join e_PicturePlaceObject b on a.place_id = b.place_id ");
		sql.append("left join e_Picture c on b.picture_id = c.picture_id ");
		if (goods_id!=null && goods_id!=""){
			sql.append("where a.place_id=11 and b.goods_id = ? ");
		}
		sql.append("order by right(a.row_id,2) asc ");
		List<Map<String, Object>> list = jdbcTemplate.queryForList(
				sql.toString(), goods_id);
		return list;
	}
	
	
	// 传入category_id 查询goods
	/**
	 * author:Gaohui
	 * mdate:2016-01-28
	 * from:hjx
	 * @param mapWhere
	 * @return
	 */
	public List<Map<String, Object>> getGoodsList(Map<String, Object> mapWhere) {
		List<Map<String, Object>> list = select("e_Goods","*",mapWhere);
		for (int i = 0, length = list.size(); i < length; i++) {
			String goods_id = list.get(i).get("goods_id").toString();
			StringBuffer sql2 = new StringBuffer();
			sql2.append("select * from e_SKU  ");
			sql2.append("where goods_id = ? ");
			List<Map<String, Object>> listSKU = jdbcTemplate.queryForList(sql2.toString(), goods_id);
			for (int j = 0, len = listSKU.size(); j < len; j++) {
				String sku_name = listSKU.get(j).get("sku_name").toString();
				GsonBuilder gbu = new GsonBuilder();
				Gson gson = gbu.create();
				if (!ItemHelper.isEmpty(sku_name)) {//让页面调用不至于使字符串形式
					listSKU.get(j).put("sku_name", gson.fromJson(sku_name, Map.class));
				}
				//添加goods_name2
				String sku_id = listSKU.get(j).get("sku_id").toString();
				Map<String, Object> map2 = this.getGoodsName2(goods_id, sku_id); 
				listSKU.get(j).put("goods_name2", map2);
				//销量为0
				listSKU.get(j).put("销量", 0);
				//添加图片信息
				listSKU.get(j).put("picture", getPicture(goods_id));
				String goods_id2 = listSKU.get(j).get("goods_id").toString();
				Map<String, Object> goods = getGoodsField(goods_id2);
				String brand_id = goods.get("brand_id").toString();
				Map<String, Object> brand = getBrandField(brand_id);
				listSKU.get(j).put("goods_name",goods.get("goods_name").toString());
				listSKU.get(j).put("brand_id",brand_id);
				listSKU.get(j).put("brand_name",brand.get("brand_name").toString());
			}
			list.get(i).put("sku", listSKU);
		}
		return list;
	}
	
	// 传入goods_id 查询goods
	/**
	 * author:Gaohui
	 * mdate:2016-01-28
	 * from:ljy
	 * @param mapWhere
	 * @return
	 */
	public Map<String, Object> getGoodsSKU(Map<String, Object> mapWhere) {
		StringBuffer sql = new StringBuffer();
		sql.append("select * from e_Goods ");
		sql.append("where goods_id = ?");
		String where_goods_id = mapWhere.get("goods_id").toString();
		Map<String, Object> map = jdbcTemplate.queryForMap(sql.toString(), where_goods_id);
		String goods_id = map.get("goods_id").toString();
		StringBuffer sql2 = new StringBuffer();
		sql2.append("select * from e_SKU  ");
		sql2.append("where goods_id = ? ");
		List<Map<String, Object>> listSKU = jdbcTemplate.queryForList(sql2.toString(), goods_id);
		Object skuObj=null;
		for (int j = 0, len = listSKU.size(); j < len; j++) {
			skuObj = listSKU.get(j).get("sku_name");
			GsonBuilder gbu = new GsonBuilder();
			Gson gson = gbu.create();
			if (!ItemHelper.isEmpty(skuObj)) {//让页面调用不至于使字符串形式
				listSKU.get(j).put("sku_name", gson.fromJson(skuObj.toString(), Map.class));
			}
			skuObj = listSKU.get(j).get("side_sku_name");
			if (!ItemHelper.isEmpty(skuObj)) {//让页面调用不至于使字符串形式
				listSKU.get(j).put("side_sku_name", gson.fromJson(skuObj.toString(), Map.class));
			}
			listSKU.get(j).put("销量", 0);
			String goods_id2 = listSKU.get(j).get("goods_id").toString();
			Map<String, Object> goods = getGoodsField(goods_id2);
			String brand_id = goods.get("brand_id").toString();
			Map<String, Object> brand = getBrandField(brand_id);
			listSKU.get(j).put("goods_name",goods.get("goods_name").toString());
			listSKU.get(j).put("brand_id",brand_id);
			listSKU.get(j).put("brand_name",brand.get("brand_name").toString());
		}
		map.put("sku", listSKU);
		map.put("measure_unit", getCategoryField(goods_id));
		map.put("picture", getPicture(goods_id));
		
		//添加商品品牌表  e_Brand
		String brand_id = map.get("brand_id").toString();
		String sql_brand = "select * from e_Brand where brand_id = ? ";
		Map<String, Object> map_brand = jdbcTemplate.queryForMap(sql_brand.toString(), brand_id);
		map.put("brand", map_brand);
		
		//添加商品分类表  e_Category
		String category_id = map.get("category_id").toString();
		String sql_category = "select * from e_Category where category_id = ? ";
		Map<String, Object> category = jdbcTemplate.queryForMap(sql_category.toString(), category_id);
		map.put("category", category);
		
		//select * from e_GoodsAttributeValues where goods_id = '00-000-20160126003519931-3713850'
		//添加商品属性值表  e_GoodsAttributeValues
		String sql_gav = "select * from e_GoodsAttributeValues where goods_id = ? ";
		List<Map<String, Object>> gav = jdbcTemplate.queryForList(sql_gav.toString(), where_goods_id);
		for (int i = 0, length = gav.size(); i < length; i++) {
			String attribute_id = gav.get(i).get("attribute_id").toString();
			String attribute_value_id = gav.get(i).get("attribute_value_id").toString();
			
			//添加属性表  e_Attributes
			String sql_attr = "select * from e_Attributes where attribute_id = ? ";
			List<Map<String, Object>> attributes = jdbcTemplate.queryForList(sql_attr.toString(), attribute_id);
			gav.get(i).put("attributes", attributes);
			
			//添加属性表值  e_AttributeValues
			String sql_attr_value = "select * from e_AttributeValues where attribute_value_id = ? ";
			List<Map<String, Object>> attributes_value = jdbcTemplate.queryForList(sql_attr_value.toString(), attribute_value_id);
			gav.get(i).put("attributes_value", attributes_value);
		}
		map.put("goodsAttributeValues", gav);
		return map;
	}
	
	
	/**
	 * 
	 * 返回如下格式数据
	 * author:gaohui
	 * date:2016-02-16
	 * from:HJX

	 "goods_name2": {
      "形状": "长方形",
      "规格": "150x600",
      "等级": "优等品",
      "质面": "哑面"
     },
	 */
	public Map<String, Object> getGoodsName2(String goods_id, String sku_id){
		StringBuffer sql = new StringBuffer();
		sql.append("select b.attribute_name,c.attribute_value ");
		sql.append("from e_GoodsAttributeValues a ");
		sql.append(" left join e_Attributes b on a.attribute_id = b.attribute_id ");
		sql.append(" left join e_AttributeValues c on a.attribute_value_id = c.attribute_value_id ");
		sql.append("where a.goods_id = ?");
		sql.append(" and a.sku_id = ? ");
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql.toString(), goods_id, sku_id);
		Map<String, Object> map = new HashMap<String, Object>();
		for (Map<String, Object> mapList : list) {
			String attribute_name = mapList.get("attribute_name").toString();
			String attribute_value = mapList.get("attribute_value").toString();
			map.put(attribute_name, attribute_value);
		}
		return map;
	}
}
