<%@page import="com.ekc.xml.WftXml"%>
<%@page contentType="text/html;charset=gb2312" language="java"%>
<%@page session="true"
	import="com.gnete.paymentgateway.PayUtils,java.util.Date"
	import="java.net.URLEncoder"%>
<html>
<table border="0">

	<%
		String strUrl, MerId, UserId, Pwd, PaySuc, ShoppingTime, BeginTime, EndTime, OrderNo, strBody;
		boolean ret;
		String resultMsg;
		strUrl = WftXml.action;
		MerId = WftXml.MerId;
		UserId = WftXml.UserId;
		Pwd = WftXml.Pwd;
		PaySuc = "1";
		ShoppingTime = "";
		BeginTime = "2012-10-11 00:00:00";
		EndTime = "2012-10-12 00:00:00";
		OrderNo = "";

		BeginTime = URLEncoder.encode(BeginTime, "gb2312");
		EndTime = URLEncoder.encode(EndTime, "gb2312");

		strBody = "Merid=" + MerId + "&userid=" + UserId + "&pwd=" + Pwd
				+ "&paysuc=" + PaySuc + "&shoppingtime=" + ShoppingTime
				+ "&BeginTime=" + BeginTime + "&endtime=" + EndTime
				+ "&orderno=" + OrderNo;

		PayUtils payUtils = new PayUtils();
		//从好易联支付网关下载符合条件的交易结果数据
		try {
			resultMsg = payUtils.doPostQueryCmd(strUrl, strBody);
			System.out.println(resultMsg);
			if (resultMsg != null) {
				//下载成功，取交易结果数据，具体格式见《开放商户支付接口V34.doc》中“对帐结果数据”一节
				out.println(resultMsg);
			} else {
				//下载失败，显示错误信息
			}
		} catch (Exception e) {
			e.printStackTrace();
			out.println("<br>" + "查询订单数据失败: " + e.getMessage() + "<br>");
		}
	%>
</table>
</html>




