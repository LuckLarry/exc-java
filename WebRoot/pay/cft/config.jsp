<%@page import="com.ekc.util.ItemHelper"%>
<%@page contentType="text/html;charset=UTF-8" language="java" %>
<% 
String itemUrl=ItemHelper.getItemUrl(request);
//收款方
String spname = "财付通双接口测试";  

//商户号
String partner = "1246508401";

//密钥
String key = "c15c118bcedfec1aa64e00f1384ded4f";

//交易完成后跳转的URL
String return_url =itemUrl+ "pay/cft/payReturnUrl.jsp";

//接收财付通通知的URL
String notify_url =itemUrl+ "pay/cft/payNotifyUrl.jsp";

%>
