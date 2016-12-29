<%@page import="com.ekc.xml.WftXml"%>
<% 
/**
 * 功能说明
 * <ul>
 * <li>	创建文件描述：生产环境,查询交易结果(实时对账)调试页面入口</li>
 * <li>	主要功能：录入查询交易结果条件,提交表单到逻辑处理页面GnetpgGetResultProcess.jsp</li>
 * <li>	访问链接:http://localhost:8080/Gnetpg_Pay_Gateway_By_V34_Vendor_Java_UTF-8/Prod/GnetpgGetResult.jsp</li>
 * <li></li>
 * </ul>
 * 
 * @author <ul>
 *         <li>创建者：广州银联网络支付有限公司（技术管理部）</li>
 *         </ul>
 * @version <ul>
 *          <li>创建版本：v1.0.0 日期：2015-02-06</li>
 *          </ul>
 */
%>
<%@page contentType="text/html;charset=UTF-8" language="java" %>
<%@page import="com.gnete.merchant.utils.GnetePayUtils" %>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>广州银联网络-查询交易结果(实时对账)示例V3.4</title>
	<link rel="stylesheet" type="text/css" href="../Themes/Style/Gnetpg.css" />
</head>
<body>
<form method="post" name="OrderForm" action="GnetpgGetResultProcess.jsp">
	<div class = "BoxBody">
		<div class = "PayHead" >
			<div class = "HeadTitle">商户联调生产环境-查询交易结果(实时对账)示例V3.4</div>
		</div>
		
		<div class="PayTop">
			<div class="PayTopContext"> 
				<span>技术支持人员: 陆苇 </span>
				<span>联系电话：020-85545314</span> 
				<span>邮箱：luwei@chinaums.com</span>
			</div>
		</div>
		
		<div class="PayForm" >	
			<div class="FormItem">  
				<div class="FormLabel">交易类型：</div>
				<div class="FormInput"><input type="text" name="TranType" value="100" ></div>
				<div class="FormRemark">*非空,长度为3,不同交易有不同定义,100:实时对账
				</div>
			</div>
			
			<div class="FormItem">  
				<div class="FormLabel">编码：</div>
				<div class="FormInput"><input type="text" name="JavaCharset" value="UTF-8" ></div>
				<div class="FormRemark">*非空,编码(固定):UTF-8</div>
			</div>
			
			<div class="FormItem">  
				<div class="FormLabel">版本号：</div>
				<div class="FormInput"><input type="text" name="Version" value="V60" ></div>
				<div class="FormRemark">*非空,版本号(固定):V60</div>
			</div>
			
			<div class="FormItem">  
				<div class="FormLabel">用户ID：</div>
				<div class="FormInput"><input type="text" name="UserId" value="<%=WftXml.UserId %>" ></div>
				<div class="FormRemark">*非空,银联业务人员分配,测试环境参数文档4.1</div>
			</div>
			
			<div class="FormItem">  
				<div class="FormLabel">用户密码：</div>
				<div class="FormInput"><input type="password" name="Pwd" value="<%=WftXml.Pwd %>" ></div>
				<div class="FormRemark">*非空,银联业务人员分配,需要进行MD5后再上送</div>
			</div>
			
			<div class="FormItem">  
				<div class="FormLabel">商户ID：</div>
				<div class="FormInput"><input type="text" name="MerId" value="<%=WftXml.MerId %>" ></div>
				<div class="FormRemark">*非空,长度为3</div>
			</div>
			
			<div class="FormItem">  
				<div class="FormLabel">支付状态：</div>
				<div class="FormInput">
					<select name="PayStatus" >
						<option value="2" >全部订单</option>
						<option value="0" >失败订单</option>
						<option value="1" >成功订单</option>
					</select>
				</div>
				<div class="FormRemark">可为空,默认:全部订单</div>
			</div>
			
			<div class="FormItem">  
				<div class="FormLabel">交易时间：</div>
				<div class="FormInput"><input type="text" name="ShoppingTime" value="<%=GnetePayUtils.GetCurrentDate("yyyy-MM-dd HH:mm:ss") %>" ></div>
				<div class="FormRemark">可为空,格式:yyyy-MM-dd HH:mm:ss,最长可查询28天内的交易,如果录入值,交易开始时间与结束时间作废</div>
			</div>
			
			<div class="FormItem">  
				<div class="FormLabel">交易开始时间：</div>
				<div class="FormInput"><input type="text" name="BeginTime" value="" ></div>
				<div class="FormRemark">可为空,格式:yyyy-MM-dd HH:mm:ss,最长可查询28天内的交易</div>
			</div>
			
			<div class="FormItem">  
				<div class="FormLabel">交易结束时间：</div>
				<div class="FormInput"><input type="text" name="EndTime" value="" ></div>
				<div class="FormRemark">可为空,格式:yyyy-MM-dd HH:mm:ss,最长可查询28天内的交易</div>
			</div>
			
			<div class="FormItem">  
				<div class="FormLabel">商户订单号：</div>
				<div class="FormInput"><input type="text" name="OrderNo" value="" ></div>
				<div class="FormRemark">可为空</div>
			</div>
			
			<div class="FormItem">  
				<div class="FormLabel">返回扩展字段类型：</div>
				<div class="FormInput"><input type="text" name="ExtFields" value="" ></div>
				<div class="FormRemark">可为空,返回扩展字段类型(备用)可不上送</div>
			</div>
			
			<div class="FormItem"> 
				<div class="FormButton"><input type="submit" value=" 提交订单 " > </div>
			</div>
		</div>
	
		<div class="PayFoot" > 
			版权所有©广州银联网络支付有限公司(中国银联体系公司) 客服热线：4008-333-222
		</div>
	</div>
</form>	
</body>
</html>