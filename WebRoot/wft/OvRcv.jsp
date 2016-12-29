<%@page import="com.ekc.xml.WftXml"%>
<%@page contentType="text/html;charset=gb2312" language="java"%>
<%@page session="true" import="com.gnete.paymentgateway.PayUtils"
	import="java.util.Date" import="java.util.StringTokenizer"%>

<%
	//接收页面变量
	String strPKey, SignMsg,SourceMsg;
	String OrderNo,PayNo,PayAmount,CurrCode,SystemSSN,RespCode,SettDate,Reserved01,Reserved02;
	boolean ret;
	
	strPKey	= WftXml.PKey;

	//接受银联在线返回数据
	//----------------------------------------------------------------------------------------
	OrderNo 	= request.getParameter("OrderNo");		//商户订单号
	PayNo 	    = request.getParameter("PayNo");		//支付单号
	PayAmount 	= request.getParameter("PayAmount");		//支付金额，格式：元.角分
	CurrCode 	= request.getParameter("CurrCode");		//货币代码
	SystemSSN 	= request.getParameter("SystemSSN");		//系统参考号
	RespCode 	= request.getParameter("RespCode");		//响应码
	SettDate 	= request.getParameter("SettDate");		//清算日期，格式：月月日日
	Reserved01 	= request.getParameter("Reserved01");		//保留域1
	Reserved02 	= request.getParameter("Reserved02");		//保留域2
	SignMsg     = request.getParameter("SignMsg");                    //时间戳签名

	SourceMsg = OrderNo + PayNo + PayAmount + CurrCode + SystemSSN + RespCode + SettDate + Reserved01 + Reserved02;

	out.println(SourceMsg);
	//解密数据
	PayUtils payUtils = new PayUtils();
	
	ret = payUtils.checkSign(SourceMsg,SignMsg,strPKey);
	
	if (ret == false) {
		//解密失败
		out.println("验签失败");
	} else {
		//将银联返回的支付结果更新到商户本地数据库
		//----------------------------------------------------------------------------------------
		//此部分操作由商户自行开发，建议将银联返回的原文、SignMsg同时写入数据库

		//输出支付结果给顾客
		
		//响应码为"00"表示交易成功，具体的响应码对照表请查阅《开放商户支付接口V34.doc》
		if (RespCode.equals("00")){
			//支付成功
			out.println("支付成功，响应码: " + RespCode + "<br>");
		}
		else{
			//支付不成功
			out.println("支付失败，响应码: " + RespCode + "<br>");
		}

	}
%>
