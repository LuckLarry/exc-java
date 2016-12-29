<%@page import="com.ekc.xml.WftXml"%>
<% 
/**
 * 功能说明
 * <ul>
 * <li>	创建文件描述：生产环境,V34支付调试页面入口</li>
 * <li>	主要功能：商户生成订单数据,提交表单到逻辑处理页面GnetpgPayProcess.jsp</li>
 * <li>	访问链接:http://localhost:8080/Gnetpg_Pay_Gateway_By_V34_Vendor_Java_UTF-8/Prod/GnetpgPay.jsp</li>
 * <li></li>
 * </ul>
 * 
 * @author <ul>
 *         <li>创建者：广州银联网络支付有限公司（技术管理部）</li>
 *         </ul>
 * @version <ul>
 *          <li>创建版本：v1.0.0 日期：2015-02-03</li>
 *          </ul>
 */
%>

<%@page contentType="text/html;charset=UTF-8" language="java" %>
<%@page import="com.gnete.merchant.utils.* "%>

<%
    //生成交易订单号,且每次生成必须唯一(即:商户订单号与交易订单号是一对多关系)
	String GetOrderNo = GnetePayUtils.GetOrderNo(); 
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>广州银联网络-网上支付程序示例V3.4</title>
	<link rel="stylesheet" type="text/css" href="../Themes/Style/Gnetpg.css" />
</head>
<body>
<script type="text/javascript" language="javascript" src="../Themes/Script/jquery-1.11.1.min.js"></script>
<script type="text/javascript">
	$(document).ready(function()
	{  	
		//初始化隐藏支付渠道
		$("#BankCode").hide();
		$('#IsDirect').click(function(){
			var Vaule = $("[name = IsDirect]:checkbox");
			if(Vaule[0].checked == true)
			{
				$("#BankCode").show();
			} else if(Vaule[0].checked == false)
			{
				$("#BankCode").hide();
			}
		});
	});
</script>

<form method="post" name="OrderForm" action="GnetpgPayProcess.jsp">
	<div class = "BoxBody">
		<div class = "PayHead" >
			<div class = "HeadTitle">商户联调生产环境-网上支付程序示例V3.4</div>
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
				<div class="FormLabel">商户ID：</div>
				<div class="FormInput"><input type="text" name="MerId" value="<%=WftXml.MerId %>" ></div>
				<div class="FormRemark">*非空，长度为3</div>
			</div>
			
			<div class="FormItem">  
				<div class="FormLabel">订单号：</div>
				<div class="FormInput"><input type="text" name="OrderNo" value="<%=GetOrderNo %>" ></div>
				<div class="FormRemark">*非空，每次填写必须唯一</div>
			</div>
			
			<div class="FormItem">  
				<div class="FormLabel">货币代码：</div>
				<div class="FormInput"><input type="text" name="CurrCode" value="CNY" ></div>
				<div class="FormRemark">*非空，默认为人民币CNY</div>
			</div>
			
			<div class="FormItem">  
				<div class="FormLabel">支付金额：</div>
				<div class="FormInput"><input type="text" name="OrderAmount" value="0.01" ></div>
				<div class="FormRemark">*非空，元/角/分，保留两位小数</div>
			</div>
			
			<div class="FormItem">  
				<div class="FormLabel">支付结果接收URL：</div>
				<div class="FormInput"><input type="text" name="CallBackUrl" value="http://qiju.zicp.net/api/pay/wft/Prod/ReceiveBackResult.jsp" ></div>
				<div class="FormRemark">*非空，填写生产环境支付结果接收URL</div>
			</div>
			
			<div class="FormItem">  
				<div class="FormLabel">订单类型：</div>
				<div class="FormInput">
					<select name="OrderType">
						<option value="B2C" >B2C订单</option>
						<option value="B2B" >B2B订单</option>
						<option value="PPA" >预授权交易订单</option>
					</select>
				</div>
				<div class="FormRemark">可为空，默认为B2C</div>
			</div>
			
			<div class="FormItem">  
				<div class="FormLabel">是否直通车标签：</div>
				<div class="FormInput">
					<input type="checkbox"  id="IsDirect" name = "IsDirect" />
				</div>
				<div class="FormRemark">选中直通车，需选择支付渠道直接跳转到对应银行的网银支付，而不必通过银联系统跳转</div>
			</div>
			
			<div class="FormItem" id="BankCode">  
				<div class="FormLabel">支付渠道：</div>
				<div class="FormInput">
					<select name="BankCode" >
						<option value="" >请选择</option>
						<option value="00010000" >无卡支付(无指定银行)</option>
						<option value="77031000" >CFCAB2B支付(无指定银行)</option>
					</select>
				</div>
				<div class="FormRemark">勾选了直通车标签，此选项非空</div>
			</div>
			
			<div class="FormItem">  
				<div class="FormLabel">保留域1：</div>
				<div class="FormInput"><input type="text" name="Reserved01" value="<%=GetOrderNo %>" ></div>
				<div class="FormRemark">可为空</div>
			</div>
			
			<div class="FormItem">  
				<div class="FormLabel">保留域2：</div>
				<div class="FormInput"><input type="text" name="Reserved02" value="test" ></div>
				<div class="FormRemark">可为空</div>
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