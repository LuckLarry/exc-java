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
@RequestMapping("/tb/rolepower.do")
public class RolePower extends BaseAction{
	@Autowired
	TableUseIfc rolePower_Ser;

	@Override
	public TableUseIfc getTabelServer() {
		return rolePower_Ser;
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
			String pk_value = "";
			if(map.containsKey(rolePower_Ser.getPrimaryKey())){
				pk_value = map.get(rolePower_Ser.getPrimaryKey()).toString();
			}
			if(ItemHelper.isEmpty(pk_value)){
				//新增
				map.put(rolePower_Ser.getPrimaryKey(), ItemHelper.createPrimaryKey());
				num = rolePower_Ser.addRowOnlyId(map);
			} else {
				//修改
				num = rolePower_Ser.update(pk_value, map);
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
