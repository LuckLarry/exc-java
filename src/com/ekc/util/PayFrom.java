package com.ekc.util;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.ekc.factory.FactoryBean;
import com.ekc.service.OrderService;
import com.ekc.service.RegionService;
import com.ekc.xml.CftXml;
import com.ekc.xml.WftXml;
import com.gnete.merchant.api.Crypt;
import com.tenpay.RequestHandler;
import com.tenpay.util.TenpayUtil;

/**
 * 付款表单 生成
 * 
 * @author pengbei_qq1179009513
 * 
 */
public class PayFrom {
	public PayFrom() {
		orderService = (OrderService) FactoryBean.getBean("orderService");
		regionService = (RegionService) FactoryBean.getBean("rService");
	}
	OrderService orderService;
	RegionService regionService;
    
//	/**
//	 * 
//	 * @param order_no
//	 *            订单号
//	 * @return html 表单
//	 */
//	public StringBuffer zfbFrom(String order_no) {
//		return zfbFrom(order_no, "alipayapi.jsp");
//	}

	/**
	 * 支付宝pc提交表单
	 * 
	 * @param order_no
	 *            订单号
	 * @param action
	 *            提交路径
	 * @return html 表单
	 */
	public StringBuffer zfbFrom(String order_no) {
		if (ItemHelper.isEmpty(order_no)) {
			return new StringBuffer("没有订单号！");
		}
		return zfbFrom(orderService.getOrderInfOrderSn(order_no));
	}

	/**
	 * 支付宝pc提交表单
	 * 
	 * @param map
	 *            订单信息
	 * @param action
	 *            提交路径
	 * @return html 表单
	 */
	public StringBuffer zfbFrom(Map<String, Object> map) {
		String action=ItemHelper.getItemUrl()+"zfb/alipayapi.jsp";
		if (ItemHelper.isEmpty(map.get("consignee"))) {
			return new StringBuffer("没有收货人信息！");
		}
		if (ItemHelper.isEmpty(map.get("order_name"))) {
			return new StringBuffer("没有包含订单名称！");
		}
		if (ItemHelper.isEmpty(map.get("order_amount"))) {
			return new StringBuffer("没有包含付款金额！");
		}
		try {
			if (Double.valueOf(map.get("order_amount").toString()) < 0.01) {
				return new StringBuffer("付款金额不能小于0.01元！");
			}
		} catch (Exception e) {
			return new StringBuffer("付款金额需要为数字！");
		}
		String address = regionService.getAddress(Integer.parseInt(map.get(
				"district").toString()))
				+ map.get("address");
		StringBuffer formBuffer = new StringBuffer();
		formBuffer.append("<form name=\"alipayment\" action=\"").append(action)
				.append("\" method=\"post\" target=\"_blank\">");
		formBuffer
				.append("<input type='hidden' name='WIDout_trade_no' value='")
				.append(map.get("order_sn")).append("'>");// 商户订单号
		formBuffer.append("<input type='hidden' name='WIDsubject' value='")
				.append(map.get("order_name")).append("'>");// 订单名称
		formBuffer.append("<input type='hidden' name='WIDprice' value='")
				.append(map.get("order_amount")).append("'>");// 付款金额
		formBuffer.append("<input type='hidden' name='WIDbody' value='")
				.append(map.get("order_des")).append("'>");// 订单描述
		formBuffer.append("<input type='hidden' name='WIDshow_url' value='")
				.append("").append("'>");// 商品展示地址
		formBuffer
				.append("<input type='hidden' name='WIDreceive_name' value='")
				.append(map.get("consignee")).append("'>");// 收货人姓名
		formBuffer
				.append("<input type='hidden' name='WIDreceive_address' value='")
				.append(address).append("'>");// 收货人地址
		formBuffer.append("<input type='hidden' name='WIDreceive_zip' value='")
				.append(map.get("zipcode")).append("'>");// 收货人邮编
		formBuffer
				.append("<input type='hidden' name='WIDreceive_phone' value='")
				.append(map.get("tel")).append("'>");// 收货人电话号码
		formBuffer
				.append("<input type='hidden' name='WIDreceive_mobile' value='")
				.append(map.get("mobile")).append("'>");// 收货人手机号码
		formBuffer
				.append("</form>");//<button class=\"new-btn-login\" type=\"submit\" style=\"text-align:center;\">确 认</button>
		return formBuffer;
	}

//	/**
//	 * 网付通支付接口
//	 * 
//	 * @param order_no
//	 *            订单号
//	 * @return
//	 */
//	public StringBuffer wftFrom(String order_no) {
//		return wftFrom(order_no, WftXml.action);
//	}

	/**
	 * 网付通提交支付表单
	 * 
	 * @param order_no
	 *            订单号
	 * @param action
	 *            提交action
	 * @return
	 */
	public StringBuffer wftFrom(String order_no) {
		return wftFrom(orderService.getOrderInfOrderSn(order_no));
	}

	public StringBuffer wftFrom(Map<String, Object> map) {
		String action=WftXml.action;
		StringBuffer formBuffer = new StringBuffer();
		String MerId = WftXml.MerId; // 商户ID参数,非空,String(3)
		String OrderNo =new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());// map.get("order_sn").toString(); // 商户订单号(每次上送必须唯一),非空,
															// String(20)
		String OrderAmount = map.get("order_amount").toString(); // 订单金额，格式：元/角/分,
																	// 非空,
																	// Number(10,2)
		String CurrCode = "CNY"; // 货币代码，默认值为：CNY, 非空,
									// String(3),CNY:人民币,HKD:港币,TWD:台币
		String CallBackUrl = ItemHelper.getItemUrl()
				+ "/pay/wft/Prod/ReceiveBackResultProcess.jsp"; // 支付结果接收URL,String(400),非空
		String ResultMode = "0"; // 支付结果返回方式(0-成功和失败支付结果均返回；1-仅返回成功支付结果)
		String Reserved01 = OrderNo; // 保留域1, 可空, String(300)
		String Reserved02 = ""; // 保留域2, 可空 , String(300)
		String OrderType = "B2C"; // 订单类型,可空, String(3)
		String IsDirect = "1"; // 是否选择直通车 1:表示选择直通车
		String BankCode = "00010000"; // 支付渠道, 可空, String(16)
		String EncryptedMsg = ""; // 加密数据
		String SignedMsg = ""; // 验签数据

		// 接受方证书路径(银联证书-公钥)
		String RcvCertPath = WftXml.getGNETE(ItemHelper.getContextPath());// ContextPath+"pay\\wft\\Prod\\Resource\\GNETE.cer";
		// 发送方证书路径(商户证书-私钥)
		String SendCertPath = WftXml.getDnz_LouLan(ItemHelper.getContextPath());// ContextPath+"pay\\wft\\Prod\\Resource\\DNZ_LOULAN.pfx";
		// 发送方证书密码
		String SendCertPWD = WftXml.SendCertPWD;

		// 生产环境支付结果接收URL
		if (StringUtils.isEmpty(CallBackUrl)) {
			CallBackUrl = ItemHelper.getItemUrl() + "wft/OvRcv.jsp";
		}

		// 如果当选择了直通车，则支付渠道不能为空，否则可以为空
		if (!"1".equals(IsDirect)) {
			BankCode = "";
		}

		// 组合成订单原始数据
		StringBuffer SourceText = new StringBuffer();
		SourceText.append("MerId=").append(MerId).append("&");
		SourceText.append("OrderNo=").append(OrderNo).append("&");
		SourceText.append("OrderAmount=").append(OrderAmount).append("&");
		SourceText.append("CurrCode=").append(CurrCode).append("&");
		SourceText.append("CallBackUrl=").append(CallBackUrl).append("&");
		SourceText.append("ResultMode=").append(ResultMode).append("&");
		SourceText.append("OrderType=").append(OrderType).append("&");
		SourceText.append("BankCode=").append(BankCode).append("&");
		SourceText.append("Reserved01=").append(Reserved01).append("&");
		SourceText.append("Reserved02=").append(Reserved02);


		/********** 2、商户使用银联证书的公钥对订单数据进行加密 *****************/
		// 创建商户加密对象
		Crypt Obj = new Crypt();

		// 使用接收方证书对订单原始数据进行加密
		boolean EncrypFlag = Obj.EncryptMsg(SourceText.toString(), RcvCertPath,
				"UTF-8");
		if (EncrypFlag) {
			EncryptedMsg = Obj.getLastResult();
			/** ----加密数据----- */
			// System.out.println("【加密数据】："+EncryptedMsg);
		} else {
			EncryptedMsg = Obj.getLastErrMsg();
			return new StringBuffer("【加密数据出错】：" + EncryptedMsg);
		}

		/****************** 3、商户使用商户证书的私钥对订单数据进行数字签名，数字签名中包括商户证书的公钥及商户系统当前时间 **************/
		// 使用发送方证书对订单原始数据进行签名
		boolean SignFlag = Obj.SignMsg(SourceText.toString(), SendCertPath,
				SendCertPWD, "UTF-8");
		if (SignFlag) {
			SignedMsg = Obj.getLastResult();
			/** --签名数据---- */
			// System.out.println("【签名数据】:"+SignedMsg);
		} else {
			SignedMsg = Obj.getLastErrMsg();
			// System.out.println("【签名数据出错】:"+SignedMsg);
			return new StringBuffer("【签名数据出错】:" + SignedMsg);
		}

		formBuffer.append("<form method='post' name='SendOrderForm' action='")
				.append(action).append("'>");
		formBuffer.append("<input type='hidden' name='EncodeMsg' value='")
				.append(EncryptedMsg).append("' />");
		formBuffer.append("<input type='hidden' name='SignMsg' value='")
				.append(SignedMsg).append("'>");
		formBuffer.append("</form>");
		return formBuffer;
	}
	
	
	
	/************************财付通提交表单********************************************/
	
	
	/**
	 * 财付通pc提交表单
	 * 
	 * @param map
	 *            订单信息
	 * @param action
	 *            提交路径
	 * @return html 表单
	 * @throws UnsupportedEncodingException 
	 */
	public StringBuffer cftFrom(String order_no) throws UnsupportedEncodingException {
		return cftFrom(orderService.getOrderInfOrderSn(order_no));
	}
	//public StringBuffer cftFrom(Map<String, Object> map,HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException {	
	public StringBuffer cftFrom(Map<String, Object> map) throws UnsupportedEncodingException {	
	//财付通网关支付请求示例，商户按照此文档进行开发即可
		//---------------------------------------------------------
		//request.setCharacterEncoding("utf-8");

		String action=ItemHelper.getItemUrl()+"pay/cft/payRequest.jsp";
		
		//获取提交的商品价格
		Map<String,Object> theMap=new HashMap<String,Object>();
		String order_price=theMap.get("order_price").toString();
		
		
		
		/* 商品价格（包含运费），以分为单位 */
		
		double total_fee = (Double.valueOf(order_price) * 100);
		int fee = (int)total_fee;
		  
		//获取提交的商品名称
		String product_name=theMap.get("product_name").toString();   

		//获取提交的备注信息
		String remarkexplain=theMap.get("remarkexplain").toString();  

		String desc = "商品：" + product_name+",备注："+remarkexplain;

		//获取提交的订单号
		String out_trade_no=theMap.get("order_no").toString();  

		//支付方式
		String trade_mode=theMap.get("trade_mode").toString(); 

		//---------------生成订单号 开始------------------------
		//当前时间 yyyyMMddHHmmss
		//String currTime = TenpayUtil.getCurrTime();
		//8位日期
		//String strTime = currTime.substring(8, currTime.length());
		//四位随机数
		//String strRandom = TenpayUtil.buildRandom(4) + "";
		//10位序列号,可以自行调整。
		//String strReq = strTime + strRandom;
		//订单号，此处用时间加随机数生成，商户根据自己情况调整，只要保持全局唯一就行
		//String out_trade_no = strReq;
		//---------------生成订单号 结束------------------------


		String currTime = TenpayUtil.getCurrTime();
		//创建支付请求对象
		
		RequestHandler reqHandler = new RequestHandler(null,null);//TODO 待处理
    	reqHandler.init();
		//设置密钥
		String key=CftXml.key;
		reqHandler.setKey(key);
		//设置支付网关
		reqHandler.setGateUrl("https://gw.tenpay.com/gateway/pay.htm");

		//-----------------------------
		//设置支付参数
		//-----------------------------
		reqHandler.setParameter("partner", CftXml.partner);		        //商户号
		reqHandler.setParameter("out_trade_no", out_trade_no);		//商家订单号
		reqHandler.setParameter("total_fee", String.valueOf(fee));			        //商品金额,以分为单位
		reqHandler.setParameter("return_url", CftXml.return_url);		    //交易完成后跳转的URL
		reqHandler.setParameter("notify_url",  CftXml.notify_url);		    //接收财付通通知的URL
		reqHandler.setParameter("body", desc);	                    //商品描述
		reqHandler.setParameter("bank_type", "DEFAULT");		    //银行类型(中介担保时此参数无效)
		reqHandler.setParameter("fee_type", "1");                    //币种，1人民币
		reqHandler.setParameter("subject", desc);              //商品名称(中介交易时必填)

		//系统可选参数
		reqHandler.setParameter("sign_type", "MD5");                //签名类型,默认：MD5
		reqHandler.setParameter("service_version", "1.0");			//版本号，默认为1.0
		reqHandler.setParameter("input_charset", "UTF-8");            //字符编码
		reqHandler.setParameter("sign_key_index", "1");             //密钥序号


		//业务可选参数
		reqHandler.setParameter("attach", "");                      //附加数据，原样返回
		reqHandler.setParameter("product_fee", "");                 //商品费用，必须保证transport_fee + product_fee=total_fee
		reqHandler.setParameter("transport_fee", "0");               //物流费用，必须保证transport_fee + product_fee=total_fee
		reqHandler.setParameter("time_start", currTime);            //订单生成时间，格式为yyyymmddhhmmss
		reqHandler.setParameter("time_expire", "");                 //订单失效时间，格式为yyyymmddhhmmss
		reqHandler.setParameter("buyer_id", "");                    //买方财付通账号
		reqHandler.setParameter("goods_tag", "");                   //商品标记
		reqHandler.setParameter("trade_mode", trade_mode);                 //交易模式，1即时到账(默认)，2中介担保，3后台选择（买家进支付中心列表选择）
		reqHandler.setParameter("transport_desc", "");              //物流说明
		reqHandler.setParameter("trans_type", "1");                  //交易类型，1实物交易，2虚拟交易
		reqHandler.setParameter("agentid", "");                     //平台ID
		reqHandler.setParameter("agent_type", "");                  //代理模式，0无代理(默认)，1表示卡易售模式，2表示网店模式
		reqHandler.setParameter("seller_id", "");                   //卖家商户号，为空则等同于partner

		//请求的url
		String requestUrl = reqHandler.getRequestURL();

		//获取debug信息,建议把请求和debug信息写入日志，方便定位问题
		String debuginfo = reqHandler.getDebugInfo();
		//System.out.println("requestUrl:  " + requestUrl);
		//System.out.println("sign_String:  " + debuginfo);

		System.out.print("requestUrl:  " + requestUrl + "<br><br>");
		System.out.print("sign_String:  " + debuginfo + "<br><br>");
		
		StringBuffer formBuffer = new StringBuffer();
		formBuffer.append("<form name=\"alipayment\" action=\"").append(action)
				.append("\" method=\"post\" target=\"_blank\">");
		formBuffer
				.append("<input type='hidden' name='WIDout_trade_no' value='")
				.append(theMap.get("order_no")).append("'>");// 提交的订单号
		formBuffer.append("<input type='hidden' name='WIDsubject' value='")
				.append(theMap.get("product_name")).append("'>");// 商品名称
		formBuffer.append("<input type='hidden' name='WIDprice' value='")
				.append(theMap.get("order_price")).append("'>");// 商品价格
		formBuffer.append("<input type='hidden' name='WIDnote' value='")
				.append(theMap.get("remarkexplain")).append("'>");//备注信息
		formBuffer
				.append("<input type='hidden' name='WIDpay_mode' value='")
				.append(theMap.get("trade_mode")).append("'>");// 支付方式
		formBuffer
				.append("</form>");//<button class=\"new-btn-login\" type=\"submit\" style=\"text-align:center;\">确 认</button>
		return formBuffer;				
	}
}







