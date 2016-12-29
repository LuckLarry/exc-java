package com.ekc.action;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekc.enumall.Message;
import com.ekc.ifc.TableUseIfc;
import com.ekc.service.RegionService;
import com.ekc.xml.MessageXml;
import com.ekc.xml.MethodsXml;

/**
 * 地区信息处理 请求
 * 
 * @author pengbei_qq1179009513
 * 
 */
@Controller
@RequestMapping("/region.do")
public class RegionAction extends BaseAction {
	@Autowired
	RegionService rService;
    @Autowired
    TableUseIfc regionservice;
	/**
	 * 通过地区列表的参数信息获得对应的树节点信息
	 * 
	 * @param param
	 *            地区表对应的字段值
	 * 
	 *            <pre>
	 * 如： {'parent_id':1}
	 * </pre>
	 * @return <pre>
	 * <h6>例子：</h6>
	 *   请求url http://api.llhome.com/region.do?m=get&date=tree
	 *   请求headers : ticket :参数
	 *   请求json 参数: {} 或者 {'parent_id':1}
	 *   返回结果：
	 *   {
	 *   "message": "success",
	 *   "messageTxt": "成功",
	 *   "dataInfo": [
	 *     {
	 *       "region_id": 2,
	 *       "parent_id": 1,
	 *       "region_name": "北京",
	 *       "region_type": true,
	 *       "agency_id": 0,
	 *       "nodeChild": [
	 *         {
	 *         ....
	 *         }
	 *         ...]
	 *      }
	 *      .....
	 *      ]
	 *  }
	 * </pre>
	 */
	@RequestMapping(params = { MethodsXml.find, MethodsXml.dateTree })
	@ResponseBody
	public Map<String, Object> treeByParentId(
			@RequestBody Map<String, Object> param) {
		Map<String, Object> reMap = Message.SUCCESS.getObjMess();
		try {
			if (param.size() <= 0) {
				param.put("parent_id", 0);
			}
			reMap.put(MessageXml.dataInfo_key,
					rService.treeByParentId(param));
		} catch (Exception e) {
			e.printStackTrace();
			reMap = Message.UN_KNOW.getObjMess(e);
		}
		return reMap;
	}
	@RequestMapping(params = { MethodsXml.find, MethodsXml.dateList,"c=s" })
	@ResponseBody
	public Map<String, Object> cangSet(
			@RequestBody Map<String, Object> param) {
		Map<String, Object> reMap = Message.SUCCESS.getObjMess();
		try {
			reMap.put(MessageXml.data_key,rService.cangSet(param));
		} catch (Exception e) {
			e.printStackTrace();
			reMap = Message.UN_KNOW.getObjMess(e);
		}
		return reMap;
	}
	@Override
	public TableUseIfc getTabelServer() {
		return regionservice;
	}
}
