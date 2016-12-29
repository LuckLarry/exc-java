package com.ekc.action.erp;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekc.action.BaseAction;
import com.ekc.enumall.Message;
import com.ekc.factory.FactoryBean;
import com.ekc.ifc.TableUseIfc;
import com.ekc.service.erp.ErpPriceBase;
import com.ekc.xml.MessageXml;
import com.ekc.xml.MethodsXml;
import com.ekc.xml.TName;

@Controller
@RequestMapping("/erp/erpprice.do")
public class ERPPriceAction extends BaseAction {
	@Autowired
	TableUseIfc erppriceService;
	@Autowired
	ErpPriceBase erppriceBase;
	@Autowired
	SimpleJdbcTemplate simpleJdbcTemplate;

	@Override
	public TableUseIfc getTabelServer() {
		return erppriceService;
	}

	/**
	 * 查询价格 Gaohui 2016-03-02
	 * 
	 * @param map
	 * @return <pre>
	 * 	<strong>请求URL：</strong>...?m=get&data=erpprice
	 * 	<strong>ticket:</strong>ticket=?
	 *  <strong>map:</strong>{[json格式]}
	 * </pre>
	 */
	@RequestMapping(params = { MethodsXml.find, "data=erpprice" })
	public @ResponseBody
	Map<String, Object> findRow(@RequestBody Map<String, Object> map) {
		Map<String, Object> mapRe = Message.SUCCESS.getObjMess();
		Map<String, Object> mapData = null;
		try {
			if (!map.containsKey("matcode")
					|| "".equals(map.get("matcode").toString())) {
				mapRe = Message.ERROR.getObjMess();
				mapRe.put("matcode", "matcode 产品编号不能为空");
				return mapRe;
			}
			if (!map.containsKey("cv6") || "".equals(map.get("cv6").toString())) {
				mapRe = Message.ERROR.getObjMess();
				mapRe.put("cv6", "cv6 等级不能为空");
				return mapRe;
			}
			mapData = getTabelServer().findPage(map);
			if (mapData == null
					|| Integer.parseInt(mapData.get(MessageXml.rowCount_key)
							.toString()) == 0) {
				mapRe = Message.NO_DATA.getObjMess();
			} else {
				mapRe.put(MessageXml.data_key, mapData);
			}
		} catch (Exception e) {
			e.printStackTrace();
			mapRe = Message.UN_KNOW.getObjMess(e);
		}
		return mapRe;
	}

	/**
	 * 通过集合参数查询所有信息
	 * 
	 * @param map
	 *            <pre>
	 * 	<strong>请求URL：</strong>...?m=get&amp;date=list
	 * 	<strong>ticket:</strong>ticket=?
	 *  <strong>map:</strong>{[json格式]}
	 * </pre>
	 * @return
	 */
	@RequestMapping(params = { MethodsXml.find, MethodsXml.dateList, "c=erp" })
	public @ResponseBody
	Map<String, Object> findListC(@RequestBody Map<String, Object> map) {
		Map<String, Object> mapRe = Message.SUCCESS.getObjMess();
		try {
			simpleJdbcTemplate = (SimpleJdbcTemplate) FactoryBean
					.getBean("simpleJdbcTemplate");
			List<Map<String, Object>> list = simpleJdbcTemplate
					.queryForList(
							"SELECT e.dictvalue,e.price,e.uom,o.`order` FROM "+TName.eOrderCang+" o LEFT JOIN "+TName.eERPPrice+" e on (e.dictvalue=o.cang_name) WHERE o.moren=? and  e.dictvalue like '%仓' and e.matcode=?  and e.price>0 and e.cv6=? ORDER BY o.`order`",
							new Object[] { map.get("moren"), map.get("matcode"),map.get("cv6")});
			
			if (list != null && list.size() > 0) {
				mapRe.put(MessageXml.data_key, list);
			} else {
				mapRe = Message.NO_DATA.getObjMess();
			}
		} catch (Exception e) {
			e.printStackTrace();
			mapRe = Message.UN_KNOW.getObjMess(e);
		}
		return mapRe;
	}
}
