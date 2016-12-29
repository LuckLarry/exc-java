<%@page import="com.ekc.util.ItemHelper"%>
<%@page import="com.ekc.xml.WftXml"%>
<% 
/**
 * 功能说明
 * <ul>
 * <li>	创建文件描述：生产环境,V34前端交易结果返回逻辑处理页面</li>
 * <li>	主要功能：</li>
 * <li>	1、此程序放在商户服务器端，用于接收GNETE.com通过用户浏览器返回的支付结果;</li>
 * <li> 2、对支付结果进行解密校验; </li>
 * <li> 3、返回支付结果给商户;</li>
 * <li>	</li>
 * <li> 订单数据格式: EncodeMsg = 订单数据加密信息; SignMsg = 时间戳签名</li>
 * </ul>
 * @author <ul>
 *         <li>创建者：广州银联网络支付有限公司（技术管理部）</li>
 *         </ul>
 * @version <ul>
 *          <li>创建版本：v1.0.0 日期：2015-02-03</li>
 *          </ul>
 */
%>
<%@page contentType="text/html;charset=UTF-8" language="java"%>
<%@page import="org.apache.commons.lang.StringUtils" %>
<%@page import="com.gnete.merchant.api.Crypt" %>
<%@page import="com.gnete.merchant.utils.* "%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>广州银联网络-前端交易结果返回逻辑处理页面</title>
	<link rel="stylesheet" type="text/css" href="../Themes/Style/Gnetpg.css" />
</head>
<body>
	
</body>
</html>
<%
	/**
	  *	商户收到广州银联网络支付有限公司交易系统页面跳转交易结果通知时,
	  *	若交易状态为成功，则建议立即发起后台交易查询,
	  * 查询该交易是否成功，以此来防范第三方伪造交易成功的信息,同时交易结果以后台通知为准;
	  * 若未收到后台通知，需要重新发起查询请求。
	  */
	 
	  //设置编码，解决乱码问题
	  request.setCharacterEncoding("UTF-8");
	  //获取资源文件绝对路径目录
	  String ContextPath = getServletContext().getRealPath("/");
	  
	  /*************1.获取银联在线返回的数据********************/
	  //支付结果加密信息
	  String EncodeMsg = request.getParameter("EncodeMsg");  
	  //时间戳签名
	  String SignMsg = request.getParameter("SignMsg");
	  
	  //校验返回的数据是否存在
	  if(StringUtils.isEmpty(EncodeMsg) || StringUtils.isEmpty(SignMsg))
	  {
		  out.write("<div class='InfoPage'><div class='InfoContext'>Err No.: 102<br>Err Description: The Payment Result Parameters Is Empty!</div></div>");
		  System.out.println("【银联在线返回的加密信息或签名参数为空!】");
		  return ;
	  }
	  
	  /*******************2.对信息进行解密并校验时间戳签名*********************/
	  //接受方证书路径(银联证书-公钥)
	  String RcvCertPath =WftXml.getGNETE(ItemHelper.getContextPath());  
	  //发送方证书路径(商户证书-私钥)
	  String SendCertPath =WftXml.getDnz_LouLan(ItemHelper.getContextPath());// ContextPath+"Prod\\Resource\\JIAXI.pfx"; 
	  //发送方证书密码
	  String SendCertPWD = WftXml.SendCertPWD; 
	  
	  //创建商户加密对象
	  Crypt Obj = new Crypt();
	  //解密数据信息
	  String DecryptedMsg = ""; 
	  
	  //解密
	  boolean DecryptMsgFlag = Obj.DecryptMsg(EncodeMsg,SendCertPath,SendCertPWD,"UTF-8");
	  if(DecryptMsgFlag)
	  {
		  DecryptedMsg = Obj.getLastResult();
		  System.out.println("【解密数据信息】："+DecryptedMsg);
	  }
	  else 
	  {
		  out.write("<div class='InfoPage'><div class='InfoContext'>Err No.: 103<br>Err Description: The PayGate's Encrypt Information Is Incorrect!</div></div>");
		  System.out.println("【解密交易结果返回数据出错】："+Obj.getLastErrMsg());
		  return ;
	  }
	  
	  //校验签名是否一致
	  boolean VerifyMsgFlag = Obj.VerifyMsg(SignMsg,DecryptedMsg,RcvCertPath,"UTF-8");
	  if(!VerifyMsgFlag)
	  {
		  out.write("<div class='InfoPage'><div class='InfoContext'>Err No.: 104<br>Err Description: The PayGate's Sign Information Is Incorrect!</div></div>");
		  System.out.println("【校验签名是否一致出错】："+Obj.getLastErrMsg());
		  return ;
	  }
	  
	  //根据解密后的内容分解出订单信息
	  String OrderNo 	= GnetePayUtils.GetValue(DecryptedMsg, "OrderNo");		   //商户订单号
	  String PayNo 	    = GnetePayUtils.GetValue(DecryptedMsg, "PayNo");		   //支付单号
      String PayAmount 	= GnetePayUtils.GetValue(DecryptedMsg, "PayAmount");       //支付金额，格式：元.角分
      String CurrCode 	= GnetePayUtils.GetValue(DecryptedMsg, "CurrCode");		   //货币代码
      String SystemSSN 	= GnetePayUtils.GetValue(DecryptedMsg, "SystemSSN");	   //系统参考号
      String RespCode 	= GnetePayUtils.GetValue(DecryptedMsg, "RespCode");		   //响应码
      String SettDate 	= GnetePayUtils.GetValue(DecryptedMsg, "SettDate");		   //清算日期，格式：月月日日
      String Reserved01 	= GnetePayUtils.GetValue(DecryptedMsg, "Reserved01");  //保留域1
      String Reserved02 	= GnetePayUtils.GetValue(DecryptedMsg, "Reserved02");  //保留域2

      //显示支付结果
      if("00".equals(RespCode))
      {
    	  out.write("<div class='InfoPage'><div class='InfoContext'>支付成功!系统参考号为："+SystemSSN+"</div></div>");
    	  System.out.println("【支付成功!系统参考号为："+SystemSSN+"】");
      }
      else 
      {
    	  out.write("<div class='InfoPage'><div class='InfoContext'>支付失败!响应码为："+RespCode+"</div></div>");
    	  System.out.println("【支付失败!响应码为："+RespCode+"】");
      }
%>
