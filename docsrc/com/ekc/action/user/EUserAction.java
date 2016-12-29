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
import com.ekc.xml.MethodsXml;
@Controller
@RequestMapping("/user/user.do")
public class EUserAction extends BaseAction {
	@Autowired
	TableUseIfc  eUsers_ser;
	@Override
	public TableUseIfc getTabelServer() {
		return eUsers_ser;
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
			String pk = eUsers_ser.getPrimaryKey();
			String pk_value=ItemHelper.createPrimaryKey();
			map.put(pk, pk_value);
			int num = getTabelServer().addRow(map);
			if (num == 0) {
				mapRe = Message.UNTREATED.getObjMess();
			} else {
				mapRe.put(pk, pk_value);
			}
		} catch (Exception e) {
			e.printStackTrace();
			mapRe = Message.UN_KNOW.getObjMess(e);
		}
		return mapRe;
	}
}
