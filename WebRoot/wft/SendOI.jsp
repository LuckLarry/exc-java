<%@page import="com.ekc.mj.CurrCodeXml"%>
<%@page import="com.ekc.xml.WftXml"%>
<script language="javascript">
	function DoAction() {
		document.SendOrderForm.submit();
	}
</script>

<%@page contentType="text/html;charset=gb2312" language="java"%>
<!-- import="java.net.URLEncoder" --> 
<%@page session="true"
	import="com.gnete.paymentgateway.PayUtils,java.text.SimpleDateFormat,java.util.Date"
	%>
 
<html>

<table border=1 bordercolor="ccccff" bgcolor="ccddee" align="center">
	<%
	   String path = request.getContextPath();
       String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	
		//定义变量
		String MerId, OrderNo, OrderAmount, CurrCode, BankCode,OrderType, nOrderType, CallBackUrl, ResultMode, Reserved01, Reserved02, SourceText, PKey, SignedMsg, EntryMode;
		boolean ret;

		PKey = WftXml.PKey;
		SignedMsg = "";

		MerId = WftXml.MerId; //商户ID参数		
		out.println("商户ID: " + MerId + "<br>");
		Date myDate = new Date();
		OrderNo = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()); //商户订单号
		
		out.println("商户订单号不超过20位: " + OrderNo + "<br>");
		OrderAmount = "1.01"; //订单金额，格式：元.角分
		out.println("订单金额: " + OrderAmount.toString() + "<br>");
		CurrCode =CurrCodeXml.CNY.toString(); //货币代码，值为：CNY
		out.println("货币代码: " + CurrCode + "<br>");
		CallBackUrl = basePath+"wft/OvRcv.jsp"; //支付结果接收URL
		//CallBackUrl = "http://www.llhome.com:80/wft/OvRcv.jsp"; //支付结果接收URL
		
		EntryMode = request.getParameter("EntryMode") == null ? "": request.getParameter("EntryMode"); //支付方式

		BankCode = request.getParameter("nBankCode") == null ? "" : request.getParameter("nBankCode");

		out.println("支付结果接收URL: " + CallBackUrl + "<br>");
		ResultMode = "0"; //支付结果返回方式(0-成功和失败支付结果均返回；1-仅返回成功支付结果)
		Reserved01 = ""; //保留域1
		Reserved02 = ""; //保留域2
		OrderType = "B2C";
		String EncodeMsg="";
		//组合成订单原始数据
		SourceText = MerId + OrderNo + OrderAmount + CurrCode + OrderType + CallBackUrl+ ResultMode + BankCode + EntryMode + Reserved01+ Reserved02;
		out.println("<br>SourceText=" +SourceText);
		
		PayUtils payUtils = new PayUtils();

		SignedMsg = payUtils.md5(SourceText + payUtils.md5(PKey));

		out.println("<br>" + "加密后的信息: " + SignedMsg + "<br>");
		
		ret = payUtils.checkSign(SourceText,SignedMsg,PKey);
		if(ret){
			out.println("<br>"+"验证签名成功！");
		}else{
			out.println("<br>"+"验证签名失败! ");
		}
		//out.println("<br>"+"验证签名: "+"883ef8a28d9602f8705cafb4d01ca754".equals(SignedMsg)  +"<br>");
	%>
	<form method="post" name="SendOrderForm" accept-charset='GB2312' action="<%=WftXml.action%>">
		<input type='hidden' name='MerId' value='<%=MerId%>'> 
		<input type='hidden' name='OrderNo' value='<%=OrderNo%>'> 
		<input type='hidden' name='OrderAmount' value='<%=OrderAmount%>'> 
		<input type='hidden' name='CurrCode' value='<%=CurrCode%>'> 
		<input type='hidden' name='CallBackUrl' value='<%=CallBackUrl%>'> 
		<input type='hidden' name='ResultMode' value='<%=ResultMode%>'>
		<input type='hidden' name='OrderType' value='<%=OrderType%>'>
		<input type='hidden' name='BankCode' value='<%=BankCode%>'> 
		<input type='hidden' name='EntryMode' value='<%=EntryMode%>'> 
		<input type='hidden' name='Reserved01' value='<%=Reserved01%>'> 
		<input type='hidden' name='Reserved02' value='<%=Reserved02%>'> 
		<input type='hidden' name='SignMsg' value='<%=SignedMsg%>'>
		<input type="hidden" name="EncodeMsg" value="<%=SignedMsg%>">
	<div align="center"><input type="submit" name="Submit" value="提交">
	</div>

	</form>
	<tr>
	</tr>
</table>

</html>




