package com.ekc.action.tb;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekc.action.BaseAction;
import com.ekc.enumall.Message;
import com.ekc.service.tb.AttributesBase;
import com.ekc.ifc.TableUseIfc;
import com.ekc.xml.MessageXml;
import com.ekc.xml.MethodsXml;

/**
 * 属性
 * 
 * @author Gaohui
 * 
 */

@Controller
@RequestMapping("/tb/attributes.do")
public class Attributes extends BaseAction {
	@Autowired
	TableUseIfc eAttributes_Ser;
	@Autowired
	AttributesBase AttributesBase;

	@Override
	public TableUseIfc getTabelServer() {
		return eAttributes_Ser;
	}

	/**
	 * 新增一条数据
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping(params = { MethodsXml.add, MethodsXml.dateOne, "g=v" })
	public @ResponseBody
	Map<String, Object> add(@RequestBody Map<String, Object> map) {
		return insertOrUpdate(map);
	}

	/**
	 * 更新一条数据
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping(params = { MethodsXml.update, MethodsXml.dateOne, "g=v" })
	public @ResponseBody
	Map<String, Object> updateAttributes(@RequestBody Map<String, Object> map) {
		return insertOrUpdate(map);
	}

	/**
	 * 查询属性及属性值表
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping(params = { MethodsXml.find, "g=v" })
	public @ResponseBody
	Map<String, Object> findAttributes(@RequestBody Map<String, Object> map) {
		Map<String, Object> mapRe = Message.SUCCESS.getObjMess();
		try {
			List<Map<String, Object>> attrList = AttributesBase.findList(map);
			if (attrList != null & attrList.size() > 0) {
				if (attrList.size() == 1) {
					Map<String, Object> attrMap = attrList.get(0);
					if (attrMap != null) {
						mapRe.put(MessageXml.data_key, attrMap);
					}
				} else {
					mapRe.put(MessageXml.data_key, attrList);
				}
			} else {
				mapRe = Message.NO_DATA.getObjMess();
			}
		} catch (Exception e) {
			e.printStackTrace();
			mapRe = Message.UN_KNOW.getObjMess(e);
		}
		return mapRe;
	}

	private Map<String, Object> insertOrUpdate(Map<String, Object> map) {
		Map<String, Object> mapRe = Message.SUCCESS.getObjMess();
		try {
			int num = AttributesBase.addRowOnlyId(map);
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
