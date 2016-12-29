<%@page import="com.ekc.service.ItemImpl"%>
<%@page import="com.ekc.ifc.ItemDao"%>
<%@page import="com.ekc.util.ItemHelper"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	import="com.ekc.util.ActionEnter"
    pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%

    request.setCharacterEncoding( "utf-8" );
	response.setHeader("Content-Type" , "text/html");
	response.setHeader("Access-Control-Allow-Origin", "*");
	response.setHeader("Access-Control-Allow-Headers", "Accept, Origin, X-Requested-With, Content-Type, Last-Modified, sid, ticket,X_Requested_With");
	String rootPath = application.getRealPath( "/" );
	//处理项目移动时需要根据项目填写的配置
	String itemtPath=ItemHelper.getItemUrl(request);
	ItemDao itemdao=new ItemImpl();
	if(!itemdao.hasFolder(rootPath+"fwb/jsp/config.json")){
		String json=itemdao.readFile(rootPath+"fwb/jsp/config1.json","GBK");
		json=json.replaceAll("@www@", itemtPath.substring(0,itemtPath.length()-1));
		itemdao.createFile(rootPath+"fwb/jsp/config.json", json);
	}
	//完成
	String str=new ActionEnter( request, rootPath ).exec() ;
	out.write(str);
	
%>