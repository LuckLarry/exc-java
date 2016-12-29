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
import com.ekc.util.ItemHelper;
import com.ekc.xml.MessageXml;
import com.ekc.xml.MethodsXml;

/**
 * 操作收货信息表
 * 
 * @author ZhengXiajun
 * 
 */
@Controller
@RequestMapping("/user/useraddress.do")
public class UserAddressAction extends BaseAction {
	@Autowired
	TableUseIfc userAddService;

	@Override
	public TableUseIfc getTabelServer() {
		return userAddService;
	}

	/**
	 * @see 如果地址不存在就添加
	 *      URL:http://api.llhome.com/user/useraddress.do?m=get&date=one&n=a
	 */
	@RequestMapping(params = { MethodsXml.find, MethodsXml.dateOne, "n=a" })
	public @ResponseBody
	Map<String, Object> findAddRow(@RequestBody Map<String, Object> map) {
		Map<String, Object> mapRe = Message.SUCCESS.getObjMess();
		Map<String, Object> mapData = null;
		try {
			mapData = getTabelServer().findRow(map);
			if (mapData == null || mapData.size() == 0) {
				String pk = getTabelServer().getPrimaryKey();
				map.put(pk, ItemHelper.createPrimaryKey());
				int code = getTabelServer().addRow(map);
				if (code > 0) {
					mapRe.put(MessageXml.data_key, map);
				} else {
					mapRe = Message.UNTREATED.getObjMess();
				}
			} else {
				mapRe.put(MessageXml.data_key, mapData);
			}
		} catch (Exception e) {
			e.printStackTrace();
			mapRe = Message.UN_KNOW.getObjMess(e);
		}
		return mapRe;
	}

}
