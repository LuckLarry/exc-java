package com.ekc.action;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekc.enumall.Message;
import com.ekc.enumall.SM;
import com.ekc.sm.EMaySDKClient;
import com.ekc.xml.MethodsXml;

/**
 * 发送短信
 * 
 * @author hui
 * 
 */
@Controller
@RequestMapping("/sm.do")
public class SMAction {
	/**
	 * 发送短信方法1
	 * 
	 * @param mapSM
	 *            <pre>
	 * {
	 * 		"phone":["手机号1","手机号2","手机号3", ...],
	 * 		"message":"发送内容",
	 * 		"type":[tongzhi]|[yingxiao]
	 * }
	 * </pre>
	 * @return <pre>
	 * 如果返回成功，那么所有的手机号都能接收到信息;
	 * 如果返回失败，那么所有的手机号都不会接到到信息。
	 * </pre>
	 * 
	 *         <pre>
	 * <h6>例子:通知类</h6>
	 * <strong>请求 URL：</strong>http://api.llhome.com/sm.do?m=send&amp;obj=doa
	 * <strong>Headers 参数：</strong>ticket = 45feb928-3ff2-4f98-a4a2-b9ebb3899992
	 * <strong>JSON 参数：</strong> 
	 * {
	 * 		"phone":["13113111164","13112271556"],
	 * 		"message":"楼兰发送内容",
	 * 		"type":"tongzhi"
	 * }
	 * <strong>返回结果：</strong>
	 * {
	 *   "message": "success",
	 *   "messageTxt": "成功",
	 *   "code": 0
	 * }
	 * <br>
	 * <h6>例子:营销类</h6>
	 * <strong>请求 URL：</strong>http://api.llhome.com/sm.do?m=send&amp;obj=doa
	 * <strong>Headers 参数：</strong>ticket = 45feb928-3ff2-4f98-a4a2-b9ebb3899992
	 * <strong>JSON 参数：</strong> 
	 * {
	 * 		"phone":["13113111164","13112271556"],
	 * 		"message":"发送内容",
	 * 		"type":"yingxiao"
	 * }
	 * <strong>返回结果：</strong>
	 * {
	 *   "message": "success",
	 *   "messageTxt": "成功",
	 *   "code": 0
	 * }
	 * </pre>
	 */
	@RequestMapping(params = { MethodsXml.send, MethodsXml.objDoa })
	public @ResponseBody
	Map<String, Object> send(@RequestBody Map<String, Object> mapSM) {
		Map<String, Object> marMap = Message.SUCCESS.getObjMess();
		String msg = SM.IDENTI.getMess_zh()
				+ mapSM.get(SM.MESSAGE.getMess_en()).toString();// 发送短信的所有内容
		@SuppressWarnings("unchecked")
		List<String> phonesList = ((List<String>) mapSM.get(SM.PHONE
				.getMess_en()));
		int size = phonesList.size();// 手机号码的个数
		int number = 4;// 一次发送手机号码的个数
		try {
			int num = 999;
			String[] ph = new String[size];
			phonesList.toArray(ph);
			String[] tempA = null;
			int idxt = 0, j = 0;
			for (int i = 0; i < size; i = i + number) {

				idxt = (number) * (j + 1);
				if (idxt > size) {
					idxt = size;
				}
				tempA = Arrays.copyOfRange(ph, number * j, idxt);
				num = EMaySDKClient.sendSMS(tempA, msg,
						mapSM.get(SM.TYPE.getMess_en()).toString());
				j++;
			}
			if (num != 0) {
				marMap = Message.UN_KNOW.getObjMess();
			}
			marMap.put("sendType", getValue(num));// 发送短信状态
		} catch (Exception e) {
			e.printStackTrace();
			marMap = Message.ERROR.getObjMess(e);
		}
		return marMap;
	}

	/**
	 * 发送短信方法2
	 * 
	 * @param mapSM
	 *            <pre>
	 * {
	 * 		"phone":["手机号1","手机号2","手机号3", ...],
	 * 		"message":"发送内容",
	 * 		"type":[tongzhi]|[yingxiao]
	 * }
	 * </pre>
	 * @return <pre>
	 * 此方法将信息一一发送到手机号里。如果某一手机号发送失败，那么该手机号会被记录并返回。但是，这并不影响发送成功的手机号。
	 * </pre>
	 * 
	 *         <pre>
	 * <h6>例子:通知类</h6>
	 * <strong>请求 URL：</strong>http://api.llhome.com/sm.do?m=send&amp;obj=dob
	 * <strong>Headers 参数：</strong>ticket = 45feb928-3ff2-4f98-a4a2-b9ebb3899992
	 * <strong>JSON 参数：</strong> 
	 * {
	 * 		"phone":["13113111164","13112271556"],
	 * 		"message":"楼兰发送内容",
	 * 		"type":"tongzhi"
	 * }
	 * 
	 * <strong>返回结果：</strong>
	 * {
	 *   "message": "success",
	 *   "messageTxt": "成功",
	 *   "list": {
	 *     "phone": [
	 *       "13112271556",
	 *       "15625418982"
	 *     ],
	 *     "message": "【楼兰家居】发送成功",
	 *     "type": "tongzhi",
	 *     "num": 0,
	 *     "fail": {}
	 *   },
	 *   "code": 0
	 * }
	 * <br>
	 *  <h6>例子:营销类</h6>
	 * <strong>请求 URL：</strong>http://api.llhome.com/sm.do?m=send&amp;obj=dob
	 * <strong>Headers 参数：</strong>ticket = 45feb928-3ff2-4f98-a4a2-b9ebb3899992
	 * <strong>JSON 参数：</strong> 
	 * {
	 * 		"phone":["13113111164","13112271556"],
	 * 		"message":"发送内容",
	 * 		"type":"yingxiao"
	 * }
	 * <strong>返回结果：</strong>
	 * {
	 *   "message": "success",
	 *   "messageTxt": "成功",
	 *   "list": {
	 *     "phone": [
	 *       "13112271556",
	 *       "15625418982"
	 *     ],
	 *     "message": "【楼兰家居】发送成功",
	 *     "type": "yingxiao",
	 *     "num": 0,
	 *     "fail": {}
	 *   },
	 *   "code": 0
	 * }
	 * </pre>
	 */
	@RequestMapping(params = { MethodsXml.send, MethodsXml.objDob })
	public @ResponseBody
	Map<String, Object> sende(@RequestBody Map<String, Object> mapSM) {
		Map<String, Object> marMap = Message.SUCCESS.getObjMess();
		@SuppressWarnings("unchecked")
		List<String> phonesList = ((List<String>) mapSM.get(SM.PHONE
				.getMess_en()));// get phone
		String[] phones = new String[phonesList.size()];
		phonesList.toArray(phones);// phones to String[]
		String msg = SM.IDENTI.getMess_zh()
				+ mapSM.get(SM.MESSAGE.getMess_en()).toString();// combination
																// phone message
		StringBuffer sb = new StringBuffer();// error phone
		int num = 999;
		try {
			for (int i = 0; i < phones.length; i++) {
				num = EMaySDKClient.sendSMS(new String[] { phones[i] }, msg,
						mapSM.get(SM.TYPE.getMess_en()).toString());
				if (num != 0) {
					sb.append(phones[i] + ",");
				}
			}
			if (num != 0) {
				marMap = Message.ERROR.getObjMess();
				marMap.put(SM.FAIL.getMess_en(), sb.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
			marMap = Message.ERROR.getObjMess(e);
		}
		return marMap;
	}

	/**
	 * 查询短信余额
	 * 
	 * <pre>
	 * <strong>请求 URL：</strong>http://api.llhome.com/sm.do?m=send&amp;obj=balance
	 * <strong>Headers 参数：</strong>ticket = 45feb928-3ff2-4f98-a4a2-b9ebb3899992
	 * </pre>
	 * 
	 * @return
	 */
	@RequestMapping(params = { MethodsXml.send, MethodsXml.objBalance })
	public @ResponseBody
	Map<String, Object> getBalance() {
		Map<String, Object> marMap = Message.SUCCESS.getObjMess();
		try {
			double balance = EMaySDKClient.getBalance();
			marMap.put("balance", balance);
		} catch (Exception e) {
			e.printStackTrace();
			marMap = Message.ERROR.getObjMess(e);
		}
		return marMap;
	}

	/**
	 * 发送短信状态
	 * 
	 * @param i
	 * @return
	 */
	private String getValue(int i) {
		switch (i) {
		case 0:
			return "发送成功";
		case -1:
			return "系统异常";
		case -2:
			return "客户端异常";
		case -9001:
			return "序列号格式错误";
		case -9002:
			return "密码格式错误";
		case -9003:
			return "客户端key格式错误";
		case -9016:
			return "发送短信包大小超出范围";
		case -9017:
			return "发送短信内容格式错误";
		case -9019:
			return "发送短信优先级格式错误";
		case -9020:
			return "发送短信手机号格式错误";
		case -9022:
			return "发送短信唯一序列值错误";
		case -9025:
			return "客户端请求sdk5超时";
		case -101:
			return "命令不被支持";
		case -104:
			return "请求超过限制";
		case -117:
			return "发送短信失败";
		case -126:
			return "路由信息失败";
		case -1104:
			return "路由失败，请联系系统管理员";
		case 101:
			return "客户端网络故障";
		case 307:
			return "目标电话不符合规则，电话号码必须是以0，1开头";
		case 303:
			return "由于客户端网络问题导致信息发送超时，该信息是否成功下发无法确定";
		case 305:
			return "服务器端返回错误，错误的返回值（返回值不是数字字符串）";
		case -128:
			return "余额不足";
		default:
			return "短信服务器返回不明参数:" + i;
		}
	}
}
