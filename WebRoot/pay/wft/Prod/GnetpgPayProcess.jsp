<%@page import="com.ekc.util.ItemHelper"%>
<%@page import="com.ekc.xml.WftXml"%>
<% 
/**
 * 功能说明
 * <ul>
 * <li>	创建文件描述：生产环境,V34支付加密及验签处理页面</li>
 * <li>	主要功能：</li>
 * <li>	1、此程序放在商户服务器端，获取商户订单数据;</li>
 * <li> 2、商户使用银联证书的公钥对订单数据进行加密; </li>
 * <li> 3、商户使用商户证书的私钥对订单数据进行数字签名，数字签名中包括商户证书的公钥及商户系统当前时间，加密后的订单数据+订单数据数字签名构成一份订单数字信封;</li>
 * <li>	4、商户通过表单方式将订单数字信封通过浏览器自动提交到银联在线;</li>
 * <li> 订单数据格式: EncodeMsg = 订单数据加密信息; SignMsg = 时间戳签名</li>
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

<%@page contentType="text/html;charset=UTF-8" language="java"%>
<%@page import="java.util.*" %>
<%@page import="org.apache.commons.lang.StringUtils" %>
<%@page import="com.gnete.merchant.api.Crypt" %>

<% 
	
	//设置编码，解决乱码问题
	request.setCharacterEncoding("UTF-8");
    //获取资源文件绝对路径目录
	String ContextPath = getServletContext().getRealPath("/");
	
    /*************1.获取商户订单数据****************/
	String MerId       = WftXml.MerId;           //商户ID参数,非空,String(3)			
	String OrderNo		= request.getParameter("OrderNo");	      //商户订单号(每次上送必须唯一),非空, String(20)
	String OrderAmount	= request.getParameter("OrderAmount");	  //订单金额，格式：元/角/分, 非空, Number(10,2)
	String CurrCode	= request.getParameter("CurrCode");		  	  //货币代码，默认值为：CNY, 非空, String(3),CNY:人民币,HKD:港币,TWD:台币
	String CallBackUrl	= request.getParameter("CallBackUrl");	  //支付结果接收URL,String(400),非空
	String ResultMode  	= "0";				                      //支付结果返回方式(0-成功和失败支付结果均返回；1-仅返回成功支付结果)	
	String Reserved01 	= request.getParameter("Reserved01");     //保留域1, 可空, String(300)
	String Reserved02 	= request.getParameter("Reserved02");	  //保留域2, 可空 , String(300)
	String OrderType	= request.getParameter("OrderType");     //订单类型,可空, String(3)
	String IsDirect = request.getParameter("IsDirect");           //是否选择直通车 1:表示选择直通车
	String BankCode = request.getParameter("BankCode");          //支付渠道, 可空, String(16)
	String EncryptedMsg = "" ;									  //加密数据
	String SignedMsg = "";                                        //验签数据
	
	//接受方证书路径(银联证书-公钥)
	String RcvCertPath =WftXml.getGNETE(ItemHelper.getContextPath());//   ContextPath+"pay\\wft\\Prod\\Resource\\GNETE.cer";  
	//发送方证书路径(商户证书-私钥)
	String SendCertPath =WftXml.getDnz_LouLan(ItemHelper.getContextPath());// ContextPath+"pay\\wft\\Prod\\Resource\\DNZ_LOULAN.pfx"; 
	//发送方证书密码
	String SendCertPWD =WftXml.SendCertPWD;  
	
	//生产环境支付结果接收URL
	if(StringUtils.isEmpty(CallBackUrl))
	{
		CallBackUrl= "http://www.gnete.com/Bin/Scripts/OpenVendor/Gnete/V34/OvRcv.asp";
	}
	
  	//生产环境form跳转链接
  	String FromUrl = "https://www.gnete.com/Bin/Scripts/OpenVendor/Gnete/V34/GetOvOrder.asp";
  	
  //如果当选择了直通车，则支付渠道不能为空，否则可以为空
  	if(!"1".equals(IsDirect))
  	{
  		BankCode = "";
  	}
	
	//组合成订单原始数据
	StringBuffer SourceText = new StringBuffer();
	SourceText.append("MerId=").append(MerId).append("&");
	SourceText.append("OrderNo=").append(OrderNo).append("&");
	SourceText.append("OrderAmount=").append(OrderAmount).append("&");
	SourceText.append("CurrCode=").append(CurrCode).append("&");
	SourceText.append("CallBackUrl=").append(CallBackUrl).append("&");
	SourceText.append("ResultMode=").append(ResultMode).append("&");
	SourceText.append("OrderType=").append(OrderType).append("&");
	SourceText.append("BankCode=").append(BankCode).append("&");
	SourceText.append("Reserved01=").append(Reserved01).append("&");
	SourceText.append("Reserved02=").append(Reserved02);
    
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
%>


<html>
<body>
	<form method='post' name='SendOrderForm' action='<%=FromUrl %>'> 
		<input type='hidden' name='EncodeMsg' value='<%=EncryptedMsg %>' />
		<input type='hidden' name='SignMsg' value='<%=SignedMsg %>'>
	</form>
	<script language='javascript'>
		document.all.SendOrderForm.submit();
	</script>
</body>
</html>