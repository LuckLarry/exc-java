<% 
/**
 * 功能说明
 * <ul>
 * <li>	创建文件描述：生产环境,V34退款接口调试页面入口</li>
 * <li>	主要功能：录入退款信息,提交表单到逻辑处理页面GnetpgRefundProcess.jsp</li>
 * <li>	访问链接:http://localhost:8080/Gnetpg_Pay_Gateway_By_V34_Vendor_Java_UTF-8/Prod/GnetpgRefund.jsp</li>
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
<%@page import="com.gnete.merchant.utils.* "%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>中国银联广州分公司-网上退款示例V3.4</title>
	<link rel="stylesheet" type="text/css" href="../Themes/Style/Gnetpg.css" />
</head>
<body>
<form method="post" name="OrderForm" action="GnetpgRefundProcess.jsp">
	<div class = "BoxBody">
		<div class = "PayHead" >
			<div class = "HeadTitle">商户联调生产环境--网上退款示例V3.4</div>
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
				<div class="FormInput"><input type="text" name="TranType" value="31" ></div>
				<div class="FormRemark">*非空，固定，31：退款</div>
			</div>
			
			<div class="FormItem">  
				<div class="FormLabel">编码：</div>
				<div class="FormInput"><input type="text" name="JavaCharset" value="UTF-8" ></div>
				<div class="FormRemark">*非空，UTF-8</div>
			</div>
			
			<div class="FormItem">  
				<div class="FormLabel">版本号：</div>
				<div class="FormInput"><input type="text" name="Version" value="V34" ></div>
				<div class="FormRemark">*非空，版本号（固定）：V34</div>
			</div>
			
			<div class="FormItem">  
				<div class="FormLabel">商户ID：</div>
				<div class="FormInput"><input type="text" name="MerId" value="198" ></div>
				<div class="FormRemark">*非空</div>
			</div>
			
			<div class="FormItem">  
				<div class="FormLabel">商户订单号：</div>
				<div class="FormInput"><input type="text" name="OrderNo" value="" ></div>
				<div class="FormRemark">*非空</div>
			</div>
			
			<div class="FormItem">  
				<div class="FormLabel">订单交易（支付）日期：</div>
				<div class="FormInput"><input type="text" name="ShoppingDate" value="" ></div>
				<div class="FormRemark">*非空,格式：yyyyMMdd</div>
			</div>
			
			<div class="FormItem">  
				<div class="FormLabel">交易（支付）金额：</div>
				<div class="FormInput"><input type="text" name="PayAmount" value="0.01" ></div>
				<div class="FormRemark">*非空</div>
			</div>
			
			<div class="FormItem">  
				<div class="FormLabel">退款金额：</div>
				<div class="FormInput"><input type="text" name="RefundAmount" value="1.00" ></div>
				<div class="FormRemark">*非空</div>
			</div>
			
			<div class="FormItem">  
				<div class="FormLabel">退款流水号：</div>
				<div class="FormInput"><input type="text" name="RefundNo" value="<%=GnetePayUtils.GetOrderNo() %>" ></div>
				<div class="FormRemark">*非空，每次唯一</div>
			</div>
			
			<div class="FormItem">  
				<div class="FormLabel">保留域：</div>
				<div class="FormInput"><input type="text" name="Reserved" value="" ></div>
				<div class="FormRemark"></div>
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