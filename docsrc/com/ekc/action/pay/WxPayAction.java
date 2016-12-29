package com.ekc.action.pay;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekc.enumall.Message;
import com.ekc.xml.MethodsXml;
import com.tencent.WXPay;
import com.tencent.protocol.pay_protocol.ScanPayReqData;

/**
 * 微信支付请求
 * 
 * @author pengbei_qq1179009513
 * 
 */
@Controller
@RequestMapping("/wxpay.do")
public class WxPayAction {
	/**
	 * 扫码支付
	 * 
	 * @param accMap
	 *            map参数
	 * 
	 *            <pre>
	 * {authCode： 这个是扫码终端设备从用户手机上扫取到的支付授权号，这个号是跟用户用来支付的银行卡绑定的，有效期是1分钟,
	 * 	body 要支付的商品的描述信息，用户会在支付成功页面里看到这个信息
	 * 	attach 支付订单里面可以填的附加数据，API会将提交的这个附加数据原样返回
	 * 	outTradeNo 商户系统内部的订单号,32个字符内可包含字母, 确保在商户系统唯一
	 * 	totalFee 订单总金额，单位为“分”，只能整数
	 * 	deviceInfo 商户自己定义的扫码支付终端设备号，方便追溯这笔交易发生在哪台终端设备上
	 * 	spBillCreateIP 订单生成的机器IP
	 * 	timeStart 订单生成时间， 格式为yyyyMMddHHmmss，如2009年12 月25 日9 点10 分10 秒表示为20091225091010。时区为GMT+8 beijing。该时间取自商户服务器
	 * 	timeExpire 订单失效时间，格式同上
	 * 	goodsTag 商品标记，微信平台配置的商品标记，用于优惠券或者满减使用
	 * 	}
	 * </pre>
	 * @return map 执行结果
	 */
	@ResponseBody
	@RequestMapping(params = { MethodsXml.saoma })
	public Map<String, Object> pay(@RequestBody Map<String, Object> accMap) {
		Map<String, Object> retMap = Message.SUCCESS.getObjMess();
		String authCode = accMap.get("authCode").toString();
		String body = accMap.get("body").toString();
		String attach = accMap.get("attach").toString();
		String outTradeNo = accMap.get("outTradeNo").toString();
		int totalFee = Integer.parseInt(accMap.get("totalFee").toString());
		String deviceInfo = accMap.get("deviceInfo").toString();
		String spBillCreateIP = accMap.get("spBillCreateIP").toString();
		String timeStart = accMap.get("timeStart").toString();
		String timeExpire = accMap.get("timeExpire").toString();
		String goodsTag = accMap.get("goodsTag").toString();
		ScanPayReqData scanPayReqData = new ScanPayReqData(authCode, body,
				attach, outTradeNo, totalFee, deviceInfo, spBillCreateIP,
				timeStart, timeExpire, goodsTag);
		try {
			WXPay.requestScanPayService(scanPayReqData);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
//		try {
//			WXPay.doScanPayBusiness(scanPayReqData, new ResultListener() {
//
//				@Override
//				public void onSuccess(ScanPayResData scanPayResData) {
//					// TODO Auto-generated method stub
//
//				}
//
//				@Override
//				public void onFailBySignInvalid(ScanPayResData scanPayResData) {
//					// TODO Auto-generated method stub
//
//				}
//
//				@Override
//				public void onFailByReturnCodeFail(ScanPayResData scanPayResData) {
//					// TODO Auto-generated method stub
//
//				}
//
//				@Override
//				public void onFailByReturnCodeError(
//						ScanPayResData scanPayResData) {
//					// TODO Auto-generated method stub
//
//				}
//
//				@Override
//				public void onFailByMoneyNotEnough(ScanPayResData scanPayResData) {
//					// TODO Auto-generated method stub
//
//				}
//
//				@Override
//				public void onFailByAuthCodeInvalid(
//						ScanPayResData scanPayResData) {
//					// TODO Auto-generated method stub
//
//				}
//
//				@Override
//				public void onFailByAuthCodeExpire(ScanPayResData scanPayResData) {
//					// TODO Auto-generated method stub
//
//				}
//
//				@Override
//				public void onFail(ScanPayResData scanPayResData) {
//					// TODO Auto-generated method stub
//
//				}
//			});
//		} catch (Exception e) {
//			e.printStackTrace();
//			retMap = Message.UN_KNOW.getObjMess(e);
//		}
		return retMap;
	}
}
