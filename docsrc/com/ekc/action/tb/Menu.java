package com.ekc.action.tb;

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
@RequestMapping("/tb/menu.do")
public class Menu extends BaseAction{
	@Autowired
	TableUseIfc menu_Ser;

	@Override
	public TableUseIfc getTabelServer() {
		return menu_Ser;
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
			int num = 0;
			String pk = menu_Ser.getPrimaryKey();
			String pk_value = "";
			if (map.containsKey(pk)){
				pk_value = map.get(pk).toString();
			}
			if(ItemHelper.isEmpty(pk_value)){
				//新增
				map.put(pk, ItemHelper.createPrimaryKey());
				num = menu_Ser.addRowOnlyId(map);
			} else {
				//修改
				num = menu_Ser.update(pk_value, map);
			}
			if (num == 0) {
				mapRe = Message.UNTREATED.getObjMess();
			}
		} catch (Exception e) {
			e.printStackTrace();
			mapRe = Message.UN_KNOW.getObjMess(e);
		}
		return mapRe;
	}
}
