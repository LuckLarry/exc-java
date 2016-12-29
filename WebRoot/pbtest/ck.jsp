<%@page import="com.google.gson.Gson"%>
<%@page import="com.google.gson.GsonBuilder"%>
<%@page import="com.ekc.factory.FactoryBean"%>
<%@page import="com.ekc.ifc.TableUseIfc"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
TableUseIfc ddd=(TableUseIfc) FactoryBean.getBean("WarehousesSer");
	String jjj="{\"warehouse_id\": \"00-000-20160118210943113-9140581\",   \"storage_name\": \"佛山\",   \"goods_id\": \"00-000-20160105233302013-6954666\",   \"goods_name\": null,    \"category_id\": null,   \"category_name\": null,   \"brand_id\": null,  \"brand_name\": null, \"stock\": 120,  \"stock_price\": 12.03, \"stock_value\": 0,  \"普通会员\": 12.03,  \"企业会员\": null,  \"审核员\": 11.12,  \"售前客服\": null,  \"售后客服\": null }";
      	GsonBuilder gsonbuild=new GsonBuilder();
      	Gson gson=gsonbuild.create();
      	Map<String, Object> map=gson.fromJson(jjj, Map.class);
Map<String, Object> wMap=new HashMap<String,Object>();
wMap.put("storage_id", "00-000-20160118171322597-7629520");
wMap.put("sku_id", "00-000-20160106222022719-7750696");
wMap.put("user_rank", 1);
ddd.update(map, wMap);
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'ck.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  
  <body>
    This is my JSP page. <br>
  </body>
</html>
