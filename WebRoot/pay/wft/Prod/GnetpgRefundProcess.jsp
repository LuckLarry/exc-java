<% 
/**
 * 功能说明
 * <ul>
 * <li>创建文件描述：生产环境-退款后台处理页面</li>
 *  <li>访问链接:http://localhost:8080/Gnetpg_Pay_Gateway_By_V34_Vendor_Java_UTF-8/Prod/GnetpgRefundProcess.jsp</li>
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
<%@page contentType="text/html;charset=UTF-8" language="java"%>
<%@page import="java.util.*"%>
<%@page import="com.gnete.merchant.api.*"%>
<%@page import="com.gnete.merchant.utils.HttpUtils"%>
<%@page import="com.gnete.merchant.utils.GnetePayUtils"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>广州银联网络-退款逻辑处理页面</title>
	<link rel="stylesheet" type="text/css" href="../Themes/Style/Gnetpg.css" />
</head>
<body>

</body>
</html>
<%
	//设置编码，解决乱码问题
	request.setCharacterEncoding("UTF-8"); 
	//获取资源文件绝对路径目录
	String ContextPath = getServletContext().getRealPath("/");

	/*************1.获取退款数据信息********************/
	String TranType = request.getParameter("TranType");        //交易类型（固定），31：退款
	String JavaCharset = request.getParameter("JavaCharset");  //编码
	String Version = request.getParameter("Version");          //版本号
	String MerId = request.getParameter("MerId");              //商户ID
	String OrderNo = request.getParameter("OrderNo");          //商户订单号
	String ShoppingDate = request.getParameter("ShoppingDate");//订单交易（支付）日期
	String PayAmount = request.getParameter("PayAmount");      //交易（支付）金额
	String RefundAmount = request.getParameter("RefundAmount");//退款金额
	String RefundNo = request.getParameter("RefundNo");        //退款流水号
	String Reserved = request.getParameter("Reserved");        //保留域
	
	String EncryptedMsg = "";  //加密数据
	String SignedMsg = "";  //签名数据
	
	
	//接受方证书路径(银联证书-公钥)
	String RcvCertPath = ContextPath+"Prod\\Resource\\GNETEWEB-TEST.cer";  
	//发送方证书路径(商户证书-私钥)
	String SendCertPath = ContextPath+"Prod\\Resource\\MERCHANT.pfx"; 
	//发送方证书密码
	String SendCertPWD = "12345678";  
	
	//银联网络服务器证书JKS
	String JKSPath = ContextPath+"Prod\\Resource\\www.gnetpg.com.jks";   
	//JKS密码
	String JKSPwd = "111111"; 
	
	
	//组合成订单原始数据
	StringBuffer SourceText = new StringBuffer();
	SourceText.append("TranType=").append(TranType).append("&");
	SourceText.append("JavaCharset=").append(JavaCharset).append("&");
	SourceText.append("Version=").append(Version).append("&");
	SourceText.append("MerId=").append(MerId).append("&");
	SourceText.append("OrderNo=").append(OrderNo).append("&");
	SourceText.append("ShoppingDate=").append(ShoppingDate).append("&");
	SourceText.append("PayAmount=").append(PayAmount).append("&");
	SourceText.append("RefundAmount=").append(RefundAmount).append("&");
	SourceText.append("RefundNo=").append(RefundNo).append("&");
	SourceText.append("Reserved=").append(Reserved);
    
    //打印信息
    System.out.println("【发送方证书路径】:"+SendCertPath);
    System.out.println("【接收方证书路径】:"+RcvCertPath);
    System.out.println("【待加密数据明文】:"+SourceText);
	
    
    /**********2、商户使用银联证书的公钥对订单数据进行加密*****************/
    //创建商户加密对象
    Crypt Obj = new Crypt();
  	
  	//使用接收方证书对订单原始数据进行加密
    boolean EncrypFlag = Obj.EncryptMsg(SourceText.toString(),RcvCertPath, "UTF-8");
  	if(EncrypFlag)
  	{
  		EncryptedMsg = Obj.getLastResult();
  		/**----加密数据-----*/
  		System.out.println("【加密数据】："+EncryptedMsg);
  	}
  	else 
  	{
  		EncryptedMsg = Obj.getLastErrMsg();
  		System.out.println("【加密数据出错】："+EncryptedMsg);
  		out.write(EncryptedMsg);
  		return ;
  	}
  	
  	
  	/******************3、商户使用商户证书的私钥对订单数据进行数字签名，数字签名中包括商户证书的公钥及商户系统当前时间**************/
  	//使用发送方证书对订单原始数据进行签名
  	boolean SignFlag = Obj.SignMsg(SourceText.toString(),SendCertPath,SendCertPWD,"UTF-8");
  	if(SignFlag)
  	{
  		SignedMsg = Obj.getLastResult();
  		/**--签名数据----*/
  		System.out.println("【签名数据】:"+SignedMsg);
  	}
  	else 
  	{
  		SignedMsg = Obj.getLastErrMsg();
  		System.out.println("【签名数据出错】:"+SignedMsg);
  		out.write(SignedMsg);
  		return;
  	}
  	
  	//在页面显示信息
  	out.write("【待加密数据明文】:<br/>"+SourceText+"<br/>");
  	out.write("【加密数据】:<br/>"+EncryptedMsg+"<br/>");
  	out.write("【签名数据】:<br/>"+SignedMsg+"<br/>");
	
  	
  	/*****************4、调用后台HTTP请求*************************/
  	//生产环境退款接口URL
	String URL = "https://www.gnetpg.com/GneteMerchantAPI/Trans.action";
	String Params = SourceText + "&SignMsg=" + SignedMsg + "&EncodeMsg=" + EncryptedMsg;
	String MerDomain = "https://www.xxx.com"; //商户域名
	HttpUtils http = new HttpUtils();
    http.setJKSPath(JKSPath);
    http.setJKSPwd(JKSPwd);
	String Resp = http.doPost(URL, Params, "UTF-8", MerDomain);
	System.out.println("【退款返回结果】" + Resp);
	
	//处理退款结果，响应数据样例：Code=0000&Message=退款成功
	if(Resp == null || Resp.length() == 0)
	{
	 out.write("<div class='InfoPage'><div class='InfoContext'>退款返回的结果为空！</div></div>");
	 System.out.println("【退款返回的结果为空!】");
	 return;
	}
	String Code = GnetePayUtils.GetValue(Resp, "Code");
	String Message = GnetePayUtils.GetValue(Resp, "Message");
	if("0000".equals(Code))
	{
		out.write("<div class='InfoPage'><div class='InfoContext'>" + Message + "</div></div>");
		System.out.println("【退款成功！响应信息】" + Message);
	}
	else
	{
		if(Message != null && !"".equals(Message))
		{
			out.write("<div class='InfoPage'><div class='InfoContext'>" + Message + "</div></div>");
			System.out.println("【退款失败！错误信息】" + Message);
		}
		else 
		{
			out.write("<div class='InfoPage'><div class='InfoContext'>" + Resp + "</div></div>");
			System.out.println("【退款失败！错误信息】" + Resp);
		}
	}

%>
