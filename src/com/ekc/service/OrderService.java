package com.ekc.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ekc.ifc.TableUseIfc;
import com.ekc.util.CreatePrimaryKey;
import com.ekc.util.ItemHelper;
import com.ekc.util.JoinTable;
import com.ekc.util.WhereTable;
import com.ekc.xml.MessageXml;
import com.ekc.xml.TName;

@Service("orderS")
public class OrderService extends BaseService {
	@Autowired
	GoodsService gService;
	@Autowired
	TableUseIfc OrderInfoSer;
	@Autowired
	TableUseIfc OSKULSer;

	public int add(Map<String, Object> order) {
		return insert(TName.eOrderInfo, order);
	}

	public Map<String, Object> get(Map<String, Object> order)
			throws IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {
		JoinTable gg = new JoinTable(TName.eOrderInfo, "a");
		gg.leftJoin(TName.eOrdersSKUList, "b", "a.order_id = b.order_id");

		for (String key : order.keySet()) {
			if (key.equals(MessageXml.page_key)
					|| key.equals(MessageXml.pageSize_key)) {
				order.put(key, order.get(key));
			} else if (key.equals("order_id")) {
				order.put("a." + key, order.get(key));
				order.remove("order_id");
			} else if (key.equals("old_id")) {
				order.put("a." + key, order.get(key));
				order.remove("order_id");
			} else {
				order.put(key, order.get(key));
			}
		}

		return selectPage(
				gg.toString(),
				"a.*,b.sku_id,b.goods_id,b.goods_name,b.goods_sn,b.goods_digit,b.goods_price,b.goods_money,b.old_id as b_old_id",
				order);
	}

	public int del(Map<String, Object> order) {
		return delete(TName.eOrderInfo, order);
	}

	public int addCart(Map<String, Object> cart) {
		cart.put("cart_id", CreatePrimaryKey.createKey("00", "000"));
		return insert(TName.eCart, cart);
	}

	public Map<String, Object> getCart(Map<String, Object> accMap)
			throws IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {
		Map<String, Object> pageList = selectPage(TName.eCart, "*", accMap);
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> list = (List<Map<String, Object>>) pageList
				.get(MessageXml.list_key);
		for (int i = 0, len = list.size(); i < len; i++) {
			list.get(i).put(MessageXml.dataInfo_key,
					gService.getSKU(list.get(i).get("goods_id").toString()));
		}
		return pageList;
	}

	/**
	 * 刷新购物车中的ticket
	 * 
	 * @param refTickt
	 *            原来ticket
	 * @param user_id
	 *            登陆用户id
	 * @return
	 */
	public int refCart(String refTickt, String user_id) {
		Map<String, Object> mapSet = new HashMap<String, Object>();
		mapSet.put("session_id", refTickt);
		Map<String, Object> mapWhere = new HashMap<String, Object>();
		mapWhere.put("user_id", user_id);
		return update(TName.eCart, mapSet, mapWhere);
	}

	/**
	 * 分页获得地址信息
	 * 
	 * @param whereMap
	 * @return
	 */
	public Map<String, Object> selectAddress(Map<String, Object> whereMap) {
		return selectPage(
				TName.eUserAddress,
				"address_id,address_name,user_id,consignee,email,country,province,city,district,address,zipcode,tel,mobile,sign_building,best_time,old_id",
				whereMap);
	}

	/**
	 * 插入地址信息数据
	 * 
	 * @param whereMap
	 * @return
	 */
	public int insertAddress(Map<String, Object> whereMap) {
		whereMap.put("address_id", CreatePrimaryKey.createKey("00", "000"));
		return insert(TName.eUserAddress, whereMap);
	}

	/**
	 * 根据订单号获得订单及地址信息
	 * 
	 * @param order_sn
	 * @return
	 */
	public Map<String, Object> getOrderInfOrderSn(String order_sn) {
		Map<String, Object> orderMap = getOrderByOrderSn(order_sn);
		if (orderMap.get("address_id") != null) {
			Map<String, Object> addressMap = getAddressByAddressId(orderMap
					.get("address_id").toString());
			if (addressMap != null) {
				orderMap.putAll(addressMap);
			}
		}
		return orderMap;
	}

	/**
	 * 根据订单号获得收到地址信息
	 * 
	 * @param order_sn
	 * @return
	 */
	public Map<String, Object> getAddressByOrderSn(String order_sn) {
		Map<String, Object> orderMap = getOrderByOrderSn(order_sn);
		return getAddressByAddressId(orderMap.get("address_id").toString());
	}

	/**
	 * 根据订单号获得订单信息
	 * 
	 * @param order_sn
	 * @return
	 */
	public Map<String, Object> getOrderByOrderSn(String order_sn) {
		WhereTable whereTable = new WhereTable("order_sn", order_sn);
		return selectRow(TName.eOrderInfo, "*", whereTable.getMap());
	}

	/**
	 * 根据地址id获得地址信息
	 * 
	 * @param ddress_id
	 * @return
	 */
	public Map<String, Object> getAddressByAddressId(String ddress_id) {
		WhereTable whereTable = new WhereTable("address_id", ddress_id);
		return selectRow(TName.eUserAddress, "*", whereTable.getMap());
	}

	/**
	 * 添加订单事务
	 * 
	 * @param ddress_id
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes", "finally" })
	@Transactional(rollbackFor=Exception.class) 
	public String addSKUList(Map<String, Object> map) {
		String Id = null;
		try {
			List<Map<String, Object>> skulist = null;
			Map<String, Object> order = null;
			if (map.containsKey("skulist")) {
				Id = CreatePrimaryKey.createKey("00", "000");
				skulist = (List) map.get("skulist");
				order=(Map)map.get("order");
				order.put(OrderInfoSer.getPrimaryKey(), Id);
				int num = OrderInfoSer.addRow(order);
				if (num > 0) {
					for (int i = 0, length = order.size(); i < length; i++) {
						skulist.get(i).put("order_id", Id);
					}
					int[] os = OSKULSer.addRows(skulist);
					for (int i = 0, length = os.length; i < length; i++) {
						if (os[i]!=1){
							throw new RuntimeException("第"+i+"条 订单对应的商品信息录入失败");
						}
					}
				}
			}
		} catch (Exception e) {
			Id = null;
			e.printStackTrace();
			throw new RuntimeException("录入失败");
		} finally {
			return Id;
		}
	}
	/**
	 * 查询订单的相关数据和属性
	 * @param order_sn
	 * @return
	 */
	public List<Map<String, Object>> getOrderSN(String order_sn){
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		StringBuffer stb = new StringBuffer();
		stb.append("select ");
		stb.append(" b.sku_id, ");
		stb.append(" a.order_sn as doccode, ");
		stb.append(" a.user_id as cltcode, ");
		stb.append(" c.user_name as cltname, ");
		stb.append(" a.order_des as HDtext, ");
		stb.append(" b.goods_digit as Digit, ");
		stb.append(" b.goods_price as Price, ");
		stb.append(" b.goods_money as totalmoney, ");
		stb.append(" c.mobile_phone as clientPhone, ");
		stb.append(" e.attribute_name, ");
		stb.append(" f.attribute_value, ");
		stb.append(" b.dictvalue ");
		stb.append("from  "+TName.eOrderInfo+" a ");
		stb.append(" left join "+TName.eOrdersSKUList+" b on a.order_id = b.order_id ");
		stb.append(" left join "+TName.eUsers+" c on a.user_id = c.user_id ");
		stb.append(" left join "+TName.eGoodsAttributesValue + " d on b.sku_id = d.sku_id or (b.goods_id = d.goods_id and IFNULL(d.sku_id,'')='') ");
		stb.append(" left join "+TName.eAttributes + " e on d.attribute_id = e.attribute_id ");
		stb.append(" left join "+TName.eAttributeValues+" f on d.attribute_value_id = f.attribute_value_id and e.attribute_id = f.attribute_id ");
		stb.append("where a.order_sn = ? ");
		stb.append(" and e.attribute_name in ('产品编号','等级') ");
		stb.append("order by b.sku_id,e.attribute_name ");
		list = jdbcTemplate.queryForList(stb.toString(), order_sn);
		return list;
	}
	
	/**
	 * 查询订单的相关数据和属性
	 * @param order_sn
	 * @return
	 * @throws Exception 
	 */
	public Map<String, Object> getERPOne(String matcode, String cv6, String companyid) throws Exception{
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> erpprice = new HashMap<String, Object>();
		StringBuffer stb = new StringBuffer();
		stb.append("select ");
		stb.append(" erp_price_id,matcode,matname,companyid,dictvalue,special,cv6,uom,price,sqmprice,hmemo ");
		stb.append("from e_ERPPrice ");
		stb.append("where matcode = ? ");
		stb.append(" and companyid = ? ");
		stb.append(" and cv6 = ? ");
		list = jdbcTemplate.queryForList(stb.toString(), matcode, companyid, cv6);
		if (list.size() > 0){
			erpprice=list.get(0);
		} else {
			throw new Exception("erpprice 价格表没有对应数据信息条件：matcode=".concat(matcode).concat(", cv6=").concat(cv6).concat(", companyid=").concat(companyid));
		}
		return erpprice;
	}
	
	/**
	 * 查询cltcode和cltname
	 * @param user_id
	 * @return
	 * @throws Exception 
	 */
	public Map<String, Object> getClt(String user_id) throws Exception{
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> user =null;
//		JoinTable joinTable=new JoinTable(TName.eUsers,"a");
//		joinTable.leftJoin(TName.eEnterApply, "b", "a.old_user_id = b.old_user_id");
//		WhereTable whereTable=new WhereTable("a.user_id",user_id);
//		list=select(joinTable.toString(), "CONCAT(IFNULL(b.old_id,a.user_id),' ') as cltcode,user_name as cltname",whereTable.getMap());
		StringBuffer stb = new StringBuffer();
		stb.append("select ");
		stb.append(" b.old_id,a.user_id,a.user_name as cltname ");
		stb.append("from ".concat(TName.eUsers)+" a left join "+TName.eEnterApply+" b on (a.old_user_id = b.old_user_id)");
		stb.append(" where a.user_id = ? ");
		System.out.println(stb.toString());
		list = jdbcTemplate.queryForList(stb.toString(), user_id);
		if (list.size() > 0){
			user=list.get(0);
			if (ItemHelper.isEmpty(user.get("old_id"))){
				user.put("cltcode", user.get("user_id"));
			}else{
				user.put("cltcode", user.get("old_id"));
			}
		} else {
			throw new Exception("clt 客户信息：user_id=".concat(user_id));
		}
		return user;
	}
	
	/**
	 * 查询Companyid
	 * @return
	 * @throws Exception 
	 */
	public String getCompanyid(String dictvalue) throws Exception{
		Object companyid="";
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		StringBuffer stb = new StringBuffer();
		stb.append("select distinct ");
		stb.append(" companyid ");
		stb.append("from e_ERPPrice ");
		stb.append("where dictvalue = ? ");
		list = jdbcTemplate.queryForList(stb.toString(), dictvalue);
		if (list.size() > 0){
			companyid=list.get(0).get("companyid");
		}
		if(ItemHelper.isEmpty(companyid)){
			throw new Exception("companyid 仓库ID为空 ：dictvalue=".concat(dictvalue));
		}
		return companyid.toString();
	}
	
	/**
	 * 查询 matcode 和 cv6
	 * @param sku_id
	 * @param goods_id
	 * @return
	 */
	public Map<String, Object> getAttribute(String sku_id, String goods_id, List<String> attribute_names) throws Exception{
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> attribute = new HashMap<String, Object>();
		StringBuffer stb = new StringBuffer();
		stb.append("select ");
		stb.append(" gv.goods_id,gv.sku_id,a.attribute_name, av.attribute_value ");
		stb.append("from ".concat(TName.eGoodsAttributesValue).concat(" gv "));
		stb.append(" left join ".concat(TName.eAttributeValues).concat(" av on (gv.attribute_value_id = av.attribute_value_id and gv.attribute_id = av.attribute_id) "));
		stb.append(" left join ".concat(TName.eAttributes).concat(" a on a.attribute_id = gv.attribute_id "));
		stb.append("where ((gv.goods_id=? and IFNULL(gv.sku_id,'')='') or gv.sku_id=?) ");
		stb.append(" and a.attribute_name in (");
		int length = attribute_names.size();
		for (int i = 0; i < length; i++) {
			if(i==length-1)
				stb.append("'"+attribute_names.get(i)+"'");
			else
				stb.append("'"+attribute_names.get(i)+"',");
		}
		stb.append(" )");
		list = jdbcTemplate.queryForList(stb.toString(), goods_id, sku_id);
		int size = list.size();
		if (size > 0){
			attribute.put("cv6", "AAA");
			for (int i = 0; i < size; i++) {
				attribute.put("goods_id", goods_id);
				attribute.put("sku_id", sku_id);
				String attribute_name = list.get(i).get("attribute_name").toString();
				String attribute_value = list.get(i).get("attribute_value").toString();
				if ("产品编号".equals(attribute_name)){
					attribute.put("matcode", attribute_value);
				}
				if ("等级".equals(attribute_name) && list.get(i).containsValue(sku_id)){
					attribute.put("cv6", attribute_value);
					continue;
				}
				if ("等级".equals(attribute_name)){
					attribute.put("cv6", attribute_value);
				}
			}
		}
		if (!attribute.containsKey("matcode")){
			throw new Exception("matcode 产品编号不存在");
		}
		if (!attribute.containsKey("cv6")){
			throw new Exception("cv6 等级不存在");
		}
		if (size <= 0){
			throw new Exception("没有查到相关属性值 条件：sku_id=".concat(sku_id).concat(", goods_id=").concat(goods_id));
		}
		return attribute;
	}
	
	/**
	 * 查询 matcode 和 cv6
	 * @param sku_id
	 * @param goods_id
	 * @return
	 */
	public Map<String, Object> getAttribute2(String sku_id, String goods_id, List<String> attribute_names) throws Exception{
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> attribute = new HashMap<String, Object>();
		StringBuffer stb = new StringBuffer();
		stb.append("select ");
		stb.append(" gv.goods_id,gv.sku_id,a.attribute_name, av.attribute_value ");
		stb.append("from ".concat(TName.eGoodsAttributesValue).concat(" gv "));
		stb.append(" left join ".concat(TName.eAttributeValues).concat(" av on (gv.attribute_value_id = av.attribute_value_id and gv.attribute_id = av.attribute_id) "));
		stb.append(" left join ".concat(TName.eAttributes).concat(" a on a.attribute_id = gv.attribute_id "));
		stb.append("where ((gv.goods_id=? and IFNULL(gv.sku_id,'')='') or gv.sku_id=?) ");
		stb.append(" and a.attribute_name in (");
		int length = attribute_names.size();
		for (int i = 0; i < length; i++) {
			if(i==length-1)
				stb.append("'"+attribute_names.get(i)+"'");
			else
				stb.append("'"+attribute_names.get(i)+"',");
		}
		stb.append(" )");
		list = jdbcTemplate.queryForList(stb.toString(), goods_id, sku_id);
		int size = list.size();
		if (size > 0){
			attribute.put("cv6", "AAA");
			for (int i = 0; i < size; i++) {
				attribute.put("goods_id", goods_id);
				attribute.put("sku_id", sku_id);
				String attribute_name = list.get(i).get("attribute_name").toString();
				String attribute_value = list.get(i).get("attribute_value").toString();
				if ("产品编号".equals(attribute_name)){
					attribute.put("matcode", attribute_value);
				}
				if ("等级".equals(attribute_name) && list.get(i).containsValue(sku_id)){
					attribute.put("cv6", attribute_value);
					continue;
				}
				if ("等级".equals(attribute_name)){
					attribute.put("cv6", attribute_value);
				}
			}
		}
		return attribute;
	}
	
}
