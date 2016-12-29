package com.ekc.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ekc.util.CreatePrimaryKey;
import com.ekc.util.ItemHelper;
import com.ekc.util.JoinTable;
import com.ekc.util.WhereTable;
import com.ekc.xml.MessageXml;
import com.ekc.xml.TName;

@Service("accountService")
public class AccountService extends BaseService {

	public Map<String, Object> getAccountInfo(Map<String, Object> accMap)
			throws RuntimeException {
		WhereTable wh_ = null;
		Map<String, Object> newUserMap = new HashMap<String, Object>();
		// TODO 处理其他第三方登陆
		if (accMap.containsKey("wx_openid")) {
			wh_ = new WhereTable("wx_openid", accMap.get("wx_openid"));
			newUserMap.put("wx_openid", accMap.get("wx_openid"));
		} else if (accMap.containsKey("qq_openid")) {
			wh_ = new WhereTable("qq_openid", accMap.get("qq_openid"));
			newUserMap.put("qq_openid", accMap.get("qq_openid"));
		} else {
			newUserMap.put("newpan", "1");
			wh_ = new WhereTable("user_name", accMap.get("user_name"));
		}
		Map<String, Object> map = selectRow(TName.eUsers, "*", wh_.getMap());
		if (map == null && !newUserMap.containsKey("newpan")) {// 不是普通用户创建新用户
			// TODO newUserMap 新用户的默认值处理
			// newUserMap.put("user_name", "sfdf");
			// .....
			createUser(newUserMap);
			map = selectRow(TName.eUsers, "*", wh_.getMap());// 创建完成后重新查询
		}
		// map.put("fav", getFav(map.get("user_id").toString()));
		return map;
	}

	/**
	 * 查询单个用户的收藏夹
	 * 
	 * @param user_id
	 * @return
	 */
	public List<Map<String, Object>> getFav(String user_id) {
		Map<String, Object> mapRe = new HashMap<String, Object>();
		mapRe.put("user_id", user_id);
		JoinTable gg = new JoinTable(TName.eFavorite, "c");// 收藏夹表
		gg.leftJoin(TName.eGoodsCategory, "a", "c.goods_id = a.goods_id");// 商品分类对照表
		gg.leftJoin(TName.eGoods, "b", "a.goods_id = b.goods_id");// 产品信息表
		List<Map<String, Object>> list = select(
				gg.toString(),
				"a.goods_category_id,a.category_id,a.goods_id,b.goods_name,b.goods_weight,b.market_price,b.shop_price,b.limited,b.lower,b.goods_thumb,b.goods_img,b.original_img",
				mapRe);
		return list;
	}

	/**
	 * 添加一个新用户
	 * 
	 * @param userMap
	 *            map
	 * 
	 *            <pre>
	 * 用户信息集合
	 * </pre>
	 * @return int 添加成功条数
	 */
	public int createUser(Map<String, Object> userMap) {
		userMap.put("user_id", CreatePrimaryKey.createKey("00", "000"));
		return insert(TName.eUsers, userMap);
	}

	/**
	 * 入驻申请
	 * 
	 * @param accMap
	 *            注册数据
	 * 
	 *            <pre>
	 *    数据为EnterApply入驻申请表
	 *    格式：{
	 * company_name："值",
	 * contact_name："值",
	 * province："值",
	 * city："值",
	 * district："值",
	 * address："值",
	 * telephone："值",
	 * home_phone："值",
	 * qq："值",
	 * mail："值",
	 * company_license："值",
	 * company_thumb："值",
	 * add_time："值",
	 * state："值",
	 * user_id："值",
	 * review_user："值",
	 *    }
	 * </pre>
	 * @return
	 */
	public int register(Map<String, Object> accMap) {
		accMap.put("enter_apply_id", CreatePrimaryKey.createKey("00", "000"));
		return insert(TName.eEnterApply, accMap);
	}

	/**
	 * 一条用户数据
	 * 
	 * @param userid
	 *            用户id
	 * @return
	 */
	public Map<String, Object> one(String userid) {
		Map<String, Object> accMap = new HashMap<String, Object>();
		accMap.put(MessageXml.userId_key, userid);
		return selectRow(TName.eUsers, "user_id,user_name,alias", accMap);
	}

	/**
	 * 用户集合数据
	 * 
	 * @param accMap
	 * @return
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public Map<String, Object> list(Map<String, Object> accMap)
			throws IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {
		return selectPage(TName.eUsers, "user_id,user_name,alias", accMap);
	}

	/**
	 * 主页分类数据
	 * 
	 * @param accMap
	 *            <pre>
	 * 参数未使用
	 * </pre>
	 * @return
	 */
	public List<Map<String, Object>> homepage(Map<String, Object> accMap) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> li = select(TName.eTouchAdPosition,
				"position_id,position_name,position_desc,sort");
		Map<String, Object> adMap = ItemHelper.findFiledToKey(getTouchAd(),
				"position_id");
		String vString = null;
		for (Map<String, Object> rowMap : li) {
			vString = rowMap.get("position_id").toString();
			// rowMap.put(vString,getTouchAdByPosition_id(rowMap)); //就要用下面两个方法
			if (adMap.containsKey(vString)) {
				rowMap.put(MessageXml.nodeChild_key, adMap.get(vString));
			} else {
				rowMap.put(MessageXml.nodeChild_key, null);
			}
			list.add(rowMap);
		}
		return list;
	}

	public List<Map<String, Object>> getTouchAdByPosition_id(String position_id) {
		Map<String, Object> accMap = new HashMap<String, Object>();
		accMap.put("position_id", position_id);
		return getTouchAdByPosition_id(accMap);
	}

	public List<Map<String, Object>> getTouchAdByPosition_id(
			Map<String, Object> accMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		accMap.put("position_id", accMap.get("position_id"));
		return select(TName.eTouchAd,
				"ad_id,ad_name,ad_link,ad_code,position_id", map);
	}

	public List<Map<String, Object>> getTouchAd() {
		// return select(
		// TName.eTouchAd + " a left join " + TName.eGoods
		// + " b on (a.goods_id = b.goods_id)",
		// "a.goods_id,a.ad_id,a.ad_name,a.ad_link,a.ad_code,a.position_id,b.shop_price,b.goods_name");

		JoinTable gg = new JoinTable(TName.eTouchAd, "a");
		gg.leftJoin(TName.eGoods, "b", "a.goods_id = b.goods_id");
		gg.leftJoin(TName.eGoodsCategory, "c", "b.goods_id = c.goods_id");
		return select(
				gg.toString(),
				"a.sku_id,a.goods_id,a.ad_id,a.ad_name,a.ad_link,a.ad_code,a.position_id,'' as shop_price,b.goods_name,c.category_id");
	}

	/**
	 * 更新用户最近一次登录时间
	 * 
	 * @param user_id
	 * @return
	 */
	public int updateLastlogin(String user_id) {
		StringBuffer sql = new StringBuffer();
		sql.append(" update " + TName.eUsers);
		sql.append(" set last_login= " + (new Date()).getTime());
		sql.append(" WHERE user_id=?");
		int i = jdbcTemplate.update(sql.toString(), user_id);
		return i;
	}

	/**
	 * 得到e_EnterApply里的user_id 并更新到e_Users表里
	 * 
	 * @param user_id
	 * @return
	 */
	public String getOld_id(String user_id) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("user_id", user_id);
		Object obj = selectOne("e_EnterApply", "old_id", map);
		String old_id = null;
		if (obj != null) {
			old_id = obj.toString();
		}
		return old_id;
	}

	/**
	 * 更新usercode
	 * 
	 * @param user_id
	 * @param usercode
	 * @return
	 */
	public int updateUsercode(String user_id, String usercode) {
		StringBuffer sql = new StringBuffer();
		sql.append(" update " + TName.eUsers);
		sql.append(" set usercode = ? ");
		sql.append(" where user_id = ? ");
		int i = jdbcTemplate.update(sql.toString(), usercode, user_id);
		return i;
	}
}
