<% 
/**
 * 功能说明
 * <ul>
 * <li>	创建文件描述：生产环境,V34后台异步通知交易结果返回调试页面入口</li>
 * <li>	主要功能：商户录入订单数据加密信息、时间戳签名,提交表单到逻辑处理页面ReceiveBackResultProcess.jsp</li>
 * <li>	访问链接:http://localhost:8080/Gnetpg_Pay_Gateway_By_V34_Vendor_Java_UTF-8/Prod/ReceiveBackResult.jsp</li>
 * <li></li>
 * </ul>
 * 
 * @author <ul>
 *         <li>创建者：广州银联网络支付有限公司（技术管理部）</li>
 *         </ul>
 * @version <ul>
 *          <li>创建版本：v1.0.0 日期：2015-02-05</li>
 *          </ul>
 */
%>

<%@page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>广州银联网络-后台异步通知交易结果返回示例V3.4</title>
	<link rel="stylesheet" type="text/css" href="../Themes/Style/Gnetpg.css" />
</head>
<body>

<form method="post" name="OrderForm" action="ReceiveBackResultProcess.jsp">
	<div class = "BoxBody">
		<div class = "PayHead" >
			<div class = "HeadTitle">商户联调生产环境-后台异步通知交易结果返回示例V3.4</div>
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
				<div class="FormLabel">支付结果加密信息：</div>
				<div class="FormInput"><textarea rows="9" cols="30" id="EncodeMsg" name="EncodeMsg" ></textarea></div>
				<div class="FormRemark">*非空</div>
			</div>
			
			<div class="FormItem">  
				<div class="FormLabel">时间戳签名信息：</div>   
				<div class="FormInput"><textarea rows="9" cols="30" id="SignMsg" name="SignMsg" ></textarea></div>
				<div class="FormRemark">*非空</div>
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