<%@page import="com.ekc.xml.WftXml"%>
<%@page import="com.ekc.util.PayFrom"%>
<%@page import="com.ekc.service.OrderService"%>
<%@page import="com.ekc.factory.FactoryBean"%>
<%@page import="com.ekc.util.ItemHelper"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String basePath =ItemHelper.getItemUrl();
String order_sn=request.getParameter("order_sn");
if(ItemHelper.isEmpty(order_sn)){
    out.print("没有提交订单号！");
    return;
}
OrderService orderService=(OrderService) FactoryBean.getBean("orderService");
Map<String,Object> ordMap=orderService.getOrderInfOrderSn(order_sn);
PayFrom formObj=new PayFrom();
StringBuffer stBuffer=formObj.wftFrom(ordMap);
StringBuffer zfbBuffer=formObj.zfbFrom(ordMap);
 StringBuffer cftBuffer=null;//formObj.cftFrom(ordMap);
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>家居网支付页面</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

</head>

<body>
	<%=stBuffer.toString()%><%=zfbBuffer.toString()%>
	<input type="button" onclick="wft()" value="提交网付通" /> 
	<input type="button" onclick="zfb()" value="提交支付宝" /> 
    <input type="button" onclick="cft()" value="提交财付通" /> 
	<script language='javascript'>
		function wft() {
			document.all.SendOrderForm.submit();
		}
		function zfb() {
			document.all.alipayment.submit();
		}
		
		function cft() {
			document.all.tecentpayment.submit();
		}
	</script>
</body>
</html>
