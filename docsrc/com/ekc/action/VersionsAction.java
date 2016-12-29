package com.ekc.action;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekc.service.VersionsService;
import com.ekc.xml.MethodsXml;

/**
 * 版本
 * 
 * @author hui
 * 
 */
@Controller
@RequestMapping("/versions.do")
public class VersionsAction {
	@Autowired
	VersionsService vService;

	/**
	 * 查询一条版本数据
	 * 
	 * @param accMap
	 *            <pre>
	 * {}
	 * </pre>
	 * @return <pre>
	 * 返回map
	 * </pre>
	 * 
	 *         <h6>例子</h6>
	 * 
	 *         <pre>
	 * <strong>请求 URL：</strong>http://api.llhome.com/versions.do?m=get&amp;date=one 
	 * <strong>JSON 参数：</strong> 
	 * <strong>Headers 参数：</strong>ticket = 45feb928-3ff2-4f98-a4a2-b9ebb3899992
	 * <strong>返回结果：</strong>
	 * {
	 *   "version_id": "00-000-20151112141816610-1382358",
	 *   "version_name": "homepage",
	 *   "version_num": "1",
	 *   "version_des": "首页广告版本"
	 * }
	 * </pre>
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping(params = { MethodsXml.find, MethodsXml.dateOne })
	public Map<String, Object> get(@RequestBody Map<String, Object> accMap) {
		return vService.getVersions(accMap);
	}
}
