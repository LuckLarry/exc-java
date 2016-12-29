package com.ekc.action;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.http.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import pb.pub.MapCom;
import pb.pwd.EcshopPwd;

import com.ekc.enumall.Message;
import com.ekc.service.AccountService;
import com.ekc.service.OrderService;
import com.ekc.util.ItemHelper;
import com.ekc.xml.MessageXml;
import com.ekc.xml.MethodsXml;


/**
 * 操作用户请求
 * 
 * @author pengbei_qq1179009513
 * 
 */
@Controller
@RequestMapping("/account.do")
public class AccountAction {
	@Autowired
	AccountService aService;
	@Autowired
	OrderService orderS;
	
    @Autowired
    JdbcTemplate jdbcTemplate;
	/**
	 * 用户登录
	 * 
	 * @param session
	 * @param accMap
	 *            <pre>
	 * {
	 * [user_name:"用户名",]
	 * [password："明文登陆密码"，]
	 * [md5password："md5加密登陆密码"，]
	 * [logintype："登陆类型"，]
	 * [wx_openid:微信用户id]
	 * [qq_openid:qq用户id]
	 * }
	 * 
	 * </pre>
	 * @return <pre>
	 * {user:{用户信息集合},ticket:权限值,.....}
	 * </pre>
	 * 
	 * 
	 *         <h6>例子：</h6>
	 * 
	 *         <pre>
	 *  	      请求url：http://api.llhome.com/account.do?m=login
	 *        json参数：{"user_name":"13600163791","md5password":"e10adc3949ba59abbe56e057f20f883e"}
	 *        方法结果：{
	 *   "message": "success",
	 *   "messageTxt": "成功",
	 *   "ticket": "6528b389-6b70-4165-8255-424bd6dfb8ad",
	 *   "code": 0,
	 *   "user_code":"",
	 *   "user": {
	 *     "old_user_id": "20136",
	 *     "user_id": "00-000-20151102063348000004-0004",
	 *     "email": "",
	 *     "user_name": "13600163791",
	 *     "password": "e10adc3949ba59abbe56e057f20f883e",
	 *     "question": "",
	 *     "answer": "",
	 *     "sex": false,
	 *     "birthday": null,
	 *     "user_money": 0,
	 *     "frozen_money": 0,
	 *     "pay_points": 0,
	 *     "address_id": "0",
	 *     "reg_time": 0,
	 *     "last_login": 1431997467,
	 *     "last_time": null,
	 *     "last_ip": "183.13.202.214",
	 *     "visit_count": 4,
	 *     "flag": 0,
	 *     "alias": "刘云",
	 *     "msn": "",
	 *     "qq": "",
	 *     "office_phone": "",
	 *     "home_phone": "",
	 *     "mobile_phone": "13600163791",
	 *     "is_validated": 0,
	 *     "credit_line": 0,
	 *     "passwd_question": null,
	 *     "passwd_answer": null,
	 *     "ec_salt": "",
	 *     "salt": "0"
	 *   }
	 * }
	 * </pre>
	 */
    @RequestMapping(params="m=setting")
    public ModelAndView log(HttpServletRequest request,HttpServletResponse response){
    	Map<String, String> map = new HashMap<String, String>();
    	map.put("aa","111");
    	map.put("bb","222");
    	map.put("cc","333");
    	request.setAttribute("nums", map);
    	int[] ages = { 1, 2, 3, 4, 5 };
    	request.setAttribute("ages", ages);
    	System.out.println(map);
    	try {
			PrintWriter out = response.getWriter();
			out.print(map);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	request.setAttribute("userName", "list");
    	return new ModelAndView("index");
    }
    @RequestMapping(params = MethodsXml.login)
	@ResponseBody
	public Map<String, Object> login(HttpSession session,
			@RequestBody Map<String, Object> accMap) {
		Map<String, Object> mapRe = Message.SUCCESS.getObjMess();
		Map<String, Object> userMap = new HashMap<String, Object>();
		String usercode = "";
		try {
			userMap = aService.getAccountInfo(accMap);
			if (userMap != null) {
				// if (true) {//生成tname
				// aService.buildTable();
				// }
				String userStr = null;
				// TODO 添加其他第三方登陆
				if (accMap.containsKey("wx_openid")) {
					userStr = accMap.get("wx_openid").toString();
				} else if (accMap.containsKey("qq_openid")) {
					userStr = accMap.get("qq_openid").toString();
				} else {
					EcshopPwd ePwd = new EcshopPwd();
					Map<String, String> map = new HashMap<String, String>();
					MapCom comMap = new MapCom();
					comMap.put(map, userMap, ePwd.ec_salt_key);
					comMap.put(map, userMap, ePwd.salt_key);
					comMap.put(map, accMap);
					String thePass = ePwd.compile_password(map);// 模拟ecshop加密
					if (userMap.get("password").equals(thePass)) {
						userStr = accMap.get("user_name").toString();
						usercode=userMap.get("usercode").toString();
					} else {
						mapRe = Message.PASSWORD_ERR.getObjMess();
					}
				}
				String[] theTicket = creatTicket(session, userStr);
				if (userStr != null) {
					mapRe.put(MessageXml.user_key, userMap);
					//TODO 确定 user_code 是否有值
					if("".equals(usercode)){
						try {
							//从erp里查到erpuser
							String user_id = aService.getOld_id(userMap.get("user_id").toString());
							if (ItemHelper.isEmpty(user_id)){
								user_id = userMap.get("user_id").toString();
							}
							if (userMap.containsKey("user_id")&&user_id!=null) {
								List<Map<String, Object>> map_Value = ItemHelper.getERPList("1", user_id, "p51exc");
								for (int i = 0, length = map_Value.size(); i < length; i++) {
									usercode = map_Value.get(i).get("erpuser").toString();
								}
							}
							//更新到e_Users表里
							if(!"".equals(usercode)){
								aService.updateUsercode(userMap.get("user_id").toString(), usercode);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						userMap.put("usercode", usercode);
						mapRe.put(MessageXml.user_key, userMap);
					}
					mapRe.put(MessageXml.ticket_key, theTicket[0]);
					// 登陆成功修改购物车信息 成功与否暂不需要考虑 这个session_id修改没意义还不如数据中不需要
					orderS.refCart(theTicket[0], userMap.get("user_id")
							.toString());
					aService.updateLastlogin(userMap.get("user_id").toString());
				}
			} else {
				mapRe = Message.NO_ACCOUNT.getObjMess();
			}
		} catch (Exception e) {
			mapRe = Message.UN_KNOW.getObjMess(e);
		}
		return mapRe;
	}

	
	/**
	 * 创建ticket的
	 * 
	 * @param session
	 * @param user_name
	 * @return String[] 索引0：新创建的ticket 索引1 为原来的ticket
	 */
	@SuppressWarnings("unchecked")
	private String[] creatTicket(HttpSession session, String user_name) {
		ServletContext context = session.getServletContext();
		Object hasObj = context.getAttribute(MessageXml.ticket_key);
		Map<String, String> hasMap = null;
		if (hasObj == null) {
			hasMap = new HashMap<String, String>();
		} else {
			hasMap = (Map<String, String>) hasObj;
		}
		String[] theTick = new String[2];
		theTick[0] = createTicket();
		if (hasMap.containsKey(user_name)) {
			theTick[1] = hasMap.get(user_name);
		}
		hasMap.put(user_name, theTick[0]);
		context.setAttribute(MessageXml.ticket_key, hasMap);
		return theTick;
	}

	/**
	 * 入驻申请
	 * 
	 * <pre>
	 * 操作入驻申请表
	 * 
	 * </pre>
	 * 
	 * @param apply
	 *            <pre>
	 * {company_name："值",
	 * contact_name："值",
	 * province："值",
	 * city："值",
	 * district："值",
	 * address："值",
	 * telephone："值",
	 * home_phone："值",
	 * qq："值",
	 * mail："值",
	 * company_license："值",
	 * company_thumb："值",
	 * add_time："值",
	 * state："值",
	 * user_id："值",
	 * review_user："值"
	 *    }
	 * </pre>
	 * @return map 处理结果 <h6>例子1</h6>
	 * 
	 *         <pre>
	 * 		请求url：http://api.llhome.com/account.do?m=register
	 *      json参数：{
	 * "company_name":"测试客户",
	 * "province":"123456",
	 * "city":"广东佛山",
	 * "district":"123",
	 * "address":"广东佛山",
	 * "home_phone":"13450827751",
	 * "mail":"1111111@qq.com",
	 * "qq":"123456789",
	 * "contact_name":"123",
	 * "telephone":"8569852",
	 * "company_license":"客户"
	 * }
	 *     返回结果：{
	 *   "message": "success",
	 *   "messageTxt": "成功",
	 *   "code": 0
	 * }
	 * </pre>
	 */
	@RequestMapping(params = MethodsXml.register)
	@ResponseBody
	public Map<String, Object> register(@RequestBody Map<String, Object> apply) {
		Map<String, Object> mapRe = Message.SUCCESS.getObjMess();
		try {
			int count = aService.register(apply);
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
	 * 通过用户id得到一条用户数据
	 * 
	 * @param userid
	 *            用户id
	 * @return map <h6>例子1</h6>
	 * 
	 *         <pre>
	 *    请求url：http://api.llhome.com/account.do?m=get&amp;date=one&amp;userid=00-000-20151102063348000004-0004
	 *    字符串参数:userid值为00-000-20151102063348000004-0004
	 *    headers中追加：ticket
	 *    返回结果：
	 *    {
	 *   "message": "success",
	 *   "messageTxt": "成功",
	 *   "dataInfo": {
	 *     "user_id": "00-000-20151102063348000004-0004",
	 *     "user_name": "13600163791",
	 *     "alias": "刘云"
	 *   },
	 *   "code": 0
	 * }
	 * </pre>
	 */
	@RequestMapping(params = { MethodsXml.find, MethodsXml.dateOne })
	@ResponseBody
	public Map<String, Object> OneUser(@RequestParam String userid) {
		Map<String, Object> mapRe = Message.SUCCESS.getObjMess();
		try {
			mapRe.put(MessageXml.dataInfo_key, aService.one(userid));
		} catch (Exception e) {
			mapRe = Message.UN_KNOW.getObjMess(e);
		}
		return mapRe;
	}

	/**
	 * 分页查得多条用户信息
	 * 
	 * @param accMap
	 *            <pre>
	 *   {}json格式 参数 对应Users表字段
	 * </pre>
	 * @return <h6>例子1</h6>
	 * 
	 *         <pre>
	 *    请求url:http://api.llhome.com/account.do?m=get&amp;date=page
	 *    headers：添加ticket
	 *    json参数：{"user_name like ?":"13%","page":1,"page_size":2}
	 *    解释：user_name like ? 为sql 语句 user_name like '13%'
	 *        没有？号user_name:13即为 user_name = '13'
	 *        同理可以 age > ? 等sql语句
	 *        [page：页码
	 *        page_size：页面条数]这个分页使用的关键字
	 *    返回结果：
	 *    {
	 *   "message": "success",
	 *   "messageTxt": "成功",
	 *   "pageInfo": {
	 *     "page_size": 2,
	 *     "page": 1,
	 *     "list": [
	 *       {
	 *         "user_id": "00-000-20151102063348000001-0001",
	 *         "user_name": "13925421629",
	 *         "alias": "jcy"
	 *       },
	 *       {
	 *         "user_id": "00-000-20151102063348000002-0002",
	 *         "user_name": "13728660025",
	 *         "alias": "黄伟"
	 *       }
	 *     ],
	 *     "rowCount": 76,
	 *     "page_count": 38
	 *   },
	 *   "code": 0
	 * }
	 * </pre>
	 */
	@ResponseBody
	@RequestMapping(params = { MethodsXml.find, MethodsXml.datePage })
	public Map<String, Object> list(@RequestBody Map<String, Object> accMap) {
		Map<String, Object> mapRe = Message.SUCCESS.getObjMess();
		try {
			mapRe.put(MessageXml.pageInfo_key, aService.list(accMap));
		} catch (Exception e) {
			mapRe = Message.UN_KNOW.getObjMess(e);
		}
		return mapRe;
	}
	
	/**
	 * 主页数据
	 * 
	 * @param accMap map 参数 未使用到
	 * @return <h6>例子</h6>
	 * 
	 *         <pre>
	 * <strong>请求url</strong>: 	http://api.llhome.com/account.do?m=get&amp;date=page&amp;obj=home
	 * <strong>json参数：</strong>	{}
	 * <strong>返回结果：</strong>{
	 *   "message": "success",
	 *   "messageTxt": "成功",
	 *   "list": [
	 *     {
	 *       "position_id": "00-000-20151105101957000001-0001",
	 *       "position_name": "首页Banner广告位",
	 *       "position_desc": "首页轮播",
	 *       "sort": "01",
	 *       "nodeChild": [
	 *         {
	 *           "goods_id": "",
	 *           "ad_id": "00-000-20151105102005000001-0002",
	 *           "ad_name": "装修公司招募640x300",
	 *           "ad_link": "",
	 *           "ad_code": "data/attached/images/70474e7836049fce229577d7e1b6663b.jpg",
	 *           "position_id": "00-000-20151105101957000001-0001",
	 *           "shop_price": null,
	 *           "goods_name": null
	 *         },
	 *         ......
	 * 		]
	 * 	},
	 *    .......
	 * 	]
	 * }
	 * </pre>
	 */
	@ResponseBody
	@RequestMapping(params = { MethodsXml.find, MethodsXml.datePage,
			MethodsXml.objHome })
	public Map<String, Object> homepage(@RequestBody Map<String, Object> accMap) {
		Map<String, Object> mapRe = Message.SUCCESS.getObjMess();
		try {
			mapRe.put(MessageXml.list_key, aService.homepage(accMap));
			// mapRe.put(MessageXml.list_key,aService.getTouchAd());
		} catch (Exception e) {
			e.printStackTrace();
			mapRe = Message.UN_KNOW.getObjMess(e);
		}
		return mapRe;
	}

	private String createTicket() {
		return UUID.randomUUID().toString();
	}
}
