package com.ekc.action.user;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekc.action.BaseAction;
import com.ekc.enumall.Message;
import com.ekc.ifc.TableUseIfc;
import com.ekc.xml.MethodsXml;

/**
 * 操作收藏夹表
 * 
 * @author ZhengXiajun
 * 
 */
@Controller
@RequestMapping("/user/favorite.do")
public class FavoriteAction extends BaseAction {
	@Autowired
	TableUseIfc favoriteService;

	@Override
	public TableUseIfc getTabelServer() {
		return favoriteService;
	}

	/**
	 * 增加一条记录
	 * 
	 * @param map
	 * @return <pre>
	 * 	<strong>请求URL：</strong>...?m=add&amp;date=one
	 * 	<strong>ticket:</strong>ticket=?
	 *  <strong>map:</strong>{[json格式]}
	 * </pre>
	 */
	@RequestMapping(params = { MethodsXml.add, MethodsXml.dateOne })
	public @ResponseBody
	Map<String, Object> addRow(@RequestBody Map<String, Object> map) {
		Map<String, Object> mapRe = Message.SUCCESS.getObjMess();
		try {
			int num = getTabelServer().addRowOnlyId(map);
			if (num == 0) {
				mapRe = Message.UNTREATED.getObjMess();
			}
			if (num == -999) {
				mapRe = Message.COLLECTION.getObjMess();
			}
		} catch (Exception e) {
			e.printStackTrace();
			mapRe = Message.UN_KNOW.getObjMess(e);
		}
		return mapRe;
	}
}
