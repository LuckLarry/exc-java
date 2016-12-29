<%@page import="com.ekc.factory.FactoryBean"%>
<%@ page import="org.springframework.jdbc.core.JdbcTemplate"%>
<%@ page import="java.util.*"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>生成所有表字段和描述</title>
</head>
<body>
<h4>接口组内部人员辅助需要</h4>
<%!
	public void getField(String table_name){
		if(table_name.length()>0){
			JdbcTemplate jdbcTemplate=(JdbcTemplate)FactoryBean.getBean("jdbcTemplate");
			List<Map<String, Object>> list = jdbcTemplate.queryForList("show full fields from ".concat(table_name));

			StringBuffer sb = new StringBuffer();
			for (Map<String, Object> map : list) {
				String Field = map.get("Field").toString();
				String Default = map.get("Default")+"";
				String Type = map.get("Type").toString();
				if (Default.equalsIgnoreCase("null")){
					Default="\"\"";
				}
				if ("date".equals(Type) || "datetime".equals(Type)){
					Default="\"".concat(Default).concat("\"");
				}
				StringBuffer stb = new StringBuffer();
				String Comment = map.get("Comment").toString();
				String Null = ((map.get("null").toString().equals("NO"))?"不能为空":"");
				stb.append("\"");
				stb.append(Field);
				stb.append("\":");
				stb.append(Default);
				stb.append(",");
				stb.append("//");
				stb.append(Comment);
				stb.append(";");
				stb.append(Null);
				System.out.println(stb.toString());
			}
		}
	}
%>

<%
	
	getField("e_User_rank");

%>
</body>
</html>

