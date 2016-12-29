package com.ekc.action;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekc.enumall.Message;
import com.ekc.service.ProjectOfferService;
import com.ekc.xml.MessageXml;
import com.ekc.xml.MethodsXml;

/**
 * 项目
 * 
 * @author hui
 * 
 */
@Controller
@RequestMapping("/project.do")
public class ProjectOfferAction {
	@Autowired
	ProjectOfferService pService;

	/**
	 * 添加一条项目工程
	 * 
	 * @param pro
	 * 
	 *            <pre>
	 * {
	 * 	"name":"联系人",
	 * 	"telephone":"电话",
	 * 	"qq":"qq",
	 * 	"province":"省",
	 * 	"city":"市",
	 * 	"district":"市",
	 * 	"necessarity":"需求",
	 * 	"area":"工程所属地区",
	 * 	"introduction":"需求说明",
	 * 	"description":"需求描述",
	 * 	"title":"需求标题",
	 * 	"type":"需求类型1.工程报价2.特殊加工3.瓷砖大小样4.电子相册5.瓷砖贴图6.家装效果图7.价格表",
	 * 	"state":"1.申请中0.已领取",
	 * 	"sample_type":"来样类型(来货定制,来货找样,特殊加工)",
	 * 	"add_time":"添加时间",
	 * 	"is_false":"是否过期"
	 * }
	 * </pre>
	 * @return <pre>
	 * 添加成功 or 未处理
	 * </pre>
	 * 
	 *         <h6>例子</h6>
	 * 
	 *         <pre>
	 * <strong>请求 URL：</strong>http://api.llhome.com/project.do?m=add&amp;date=one
	 * <strong>JSON 参数：</strong>
	 *  {
	 *  	"name":"Miss 小姐",
	 *  	"telephone":"13112261556",
	 *  	"qq":"698745621",
	 *  	"province":"广东省",
	 *  	"city":"佛山市",
	 *  	"district":"禅城区",
	 *  	"necessarity":"保期,保质",
	 *  	"area":"江门,,",
	 *  	"introduction":"完成日期",
	 *  	"description":"合同签定日期内,,",
	 *  	"title":"按期完成,,",
	 *  	"type":"3",
	 *  	"state":"1",
	 *  	"sample_type":"来货找样",
	 *  	"add_time":"2015-12-01 10:01:00",
	 *  	"is_false":"0"
	 *  }
	 *  <strong>Headers 参数：</strong>ticket = 45feb928-3ff2-4f98-a4a2-b9ebb3899992
	 *  <strong>返回结果：</strong>
	 * {
	 *   "message": "success",
	 *   "messageTxt": "成功",
	 *   "code": 0
	 * }
	 * </pre>
	 * @throws IOException
	 */
	@RequestMapping(params = { MethodsXml.add, MethodsXml.dateOne })
	public @ResponseBody
	Map<String, Object> register(@RequestBody Map<String, Object> pro)
			throws IOException {
		Map<String, Object> mapRe = Message.SUCCESS.getObjMess();
		try {
			int count = pService.setProjectOffer(pro);
			if (count <= 0) {
				mapRe = Message.UNTREATED.getObjMess();
			}
		} catch (Exception e) {
			e.printStackTrace();
			mapRe = Message.UN_KNOW.getObjMess(e);
		}
		return mapRe;
	}

	/**
	 * 查询分页项目工程
	 * 
	 * @param ProMap
	 * 
	 *            <pre>
	 * {
	 *     "page_size": 20,
	 *     "page": 1
	 * }
	 * </pre>
	 * @return <pre>
	 * 分页项目工程数据
	 * </pre>
	 * 
	 *         <h6>例子</h6>
	 * 
	 *         <pre>
	 * <strong>请求 URL：</strong>http://api.llhome.com/project.do?m=add&amp;date=one
	 * <strong>JSON 参数：</strong>
	 *  <strong>Headers 参数：</strong>ticket = 45feb928-3ff2-4f98-a4a2-b9ebb3899992
	 *  <strong>返回结果：</strong>
	 * {
	 *   "message": "success",
	 *   "messageTxt": "成功",
	 *   "pageInfo": {
	 *     "page_size": 20,
	 *     "page": 1,
	 *     "list": [
	 *       {
	 *         "name": "Miss",
	 *         "telephone": "13112261555",
	 *         "qq": "987456321",
	 *         "province": "广东省",
	 *         "city": "佛山市",
	 *         "district": "禅城区"
	 *       }
	 *     ],
	 *     "rowCount": 1,
	 *     "page_count": 1
	 *   },
	 *   "code": 0
	 * }
	 * </pre>
	 * @throws IOException
	 */
	@RequestMapping(params = { MethodsXml.find, MethodsXml.datePage })
	public @ResponseBody
	Map<String, Object> getProjectInfo(Map<String, Object> ProMap)
			throws IOException {
		Map<String, Object> mapRe = Message.SUCCESS.getObjMess();
		try {
			mapRe.put(MessageXml.pageInfo_key,
					pService.getProjectOfferInfo(ProMap));
		} catch (Exception e) {
			e.printStackTrace();
			mapRe = Message.UN_KNOW.getObjMess(e);
		}
		return mapRe;
	}
}
