<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'MyJsp.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<script type='text/javascript' src='http://union.tenpay.com/bankList/jquery.js'></script>
	<link rel="stylesheet" type="text/css" href="http://union.tenpay.com/bankList/css_col4.css"/>

  </head>
  
  <body>
   <div id="tenpayBankList"></div>
   <input type="hidden" name="bank_type_value" id="bank_type_value" value="0">
   <script>$.getScript("http://union.tenpay.com/bankList/bank.js");</script>
  </body>
</html>
