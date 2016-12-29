<%@page import="com.ekc.xml.WftXml"%>
<% 
/**
 * 功能说明
 * <ul>
 * <li>	创建文件描述：生产环境,查询交易结果(实时对账)逻辑处理页面</li>
 * <li>	主要功能：</li>
 * <li>	1、最长可查询28天内的交易;</li>
 * <li> 2、由于本接口属于后台交易,开发时,一定要加入Http的超时时间，超时时间建议不要超时3分钟; </li>
 * <li> 3、交易结果实时返回;</li>
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
<%@page import="org.apache.commons.lang.StringUtils" %>
<%@page import="com.gnete.merchant.api.*" %>
<%@page import="com.gnete.merchant.utils.GnetePayUtils" %>
<%@page import="com.gnete.merchant.utils.HttpUtils" %>
<%@page import="java.util.*" %>

<% 
	
	//设置编码，解决乱码问题
	request.setCharacterEncoding("UTF-8");
    //获取资源文件绝对路径目录
	String ContextPath = getServletContext().getRealPath("/");
    
    /*************1.获取商户查询条件****************/
	String TranType       = request.getParameter("TranType");           //交易类型
	String JavaCharset		= request.getParameter("JavaCharset");	    //编码
	String Version	= request.getParameter("Version");	                //版本号
	String UserId	= request.getParameter("UserId");		  	        //用户ID
	String Pwd	= request.getParameter("Pwd");	                        //用户密码
	String MerId  	= request.getParameter("MerId");				    //商户ID	
	String PayStatus 	= request.getParameter("PayStatus");            //支付状态
	String ShoppingTime 	= request.getParameter("ShoppingTime");	    //交易时间
	String BeginTime	= request.getParameter("BeginTime");            //交易开始时间
	String EndTime = request.getParameter("EndTime");                   //交易结束时间
	String OrderNo = request.getParameter("OrderNo");                   //商户订单号
	String ExtFields = request.getParameter("ExtFields"); 			    //返回扩展字段类型
	
	
	//银联网络服务器证书JKS
	String JKSPath = WftXml.getJks();   
	//JKS密码
	String JKSPwd = WftXml.JKSPwd;  
	
	//对用户名密码进行MD5加密
	String PwdMD5 = Md5.md5(Pwd);
	
	//校验交易时间,如果交易时间不为空，则交易开始时间与交易结束时间作废，反之以交易开始时间与交易结束时间做查询条件
	if(StringUtils.isNotEmpty(ShoppingTime))
	{
		BeginTime = "";
		EndTime = "";
	}
	
	
	/*************2.HTTP请求后台***************************/
	//组合成订单原始数据
	String MerDomain = "https://www.xxx.com"; //商户域名
	//生产环境URL
	String Url = "https://www.gnetpg.com/GneteMerchantAPI/Trans.action";
	StringBuffer SourceParam = new StringBuffer();
	SourceParam.append("TranType=").append(TranType).append("&");
	SourceParam.append("JavaCharset=").append(JavaCharset).append("&");
	SourceParam.append("Version=").append(Version).append("&");
	SourceParam.append("UserId=").append(UserId).append("&");
	SourceParam.append("Pwd=").append(PwdMD5).append("&");
	SourceParam.append("MerId=").append(MerId).append("&");
	SourceParam.append("PayStatus=").append(PayStatus).append("&");
	SourceParam.append("ShoppingTime=").append(ShoppingTime).append("&");
	SourceParam.append("BeginTime=").append(BeginTime).append("&");
	SourceParam.append("EndTime=").append(EndTime).append("&");
	SourceParam.append("OrderNo=").append(OrderNo).append("&");
	SourceParam.append("ExtFields=").append(ExtFields);
    
    //打印信息
    System.out.println("【银联网络服务器证书JKS路径】:"+JKSPath);
    System.out.println("【POST请求参数】:"+SourceParam.toString());
    
    //HTTP请求
    HttpUtils http = new HttpUtils();
    http.setJKSPath(JKSPath);
    http.setJKSPwd(JKSPwd);
    String Resp = http.doPost(Url, SourceParam.toString(), "UTF-8", MerDomain);
    //out.print(Resp);
    
    //处理查询结果
	String Code = GnetePayUtils.GetValue(Resp, "Code");
	if(Code != null && !"".equals(Code.trim()))
	{
		String Message = GnetePayUtils.GetValue(Resp, "Message");
		out.write("<div class='InfoPage'><div class='InfoContext'>" + Message + "</div></div>");
		System.out.println("【对账结果】" + Message);
		return;
	}
     
    //1、对账响应结果数据是指构造成对账结果的数据，每条记录用行分隔，每列用\n分隔
    //2、订单的格式：订单日期\n支付金额\n商户订单号\n商户终端号\n系统参考号\n响应码\n清算日期\n保留域1\n保留域2\n
    //注意用&替换\n空格符,不然\n传入到后台会自动转换为空格
    List<Map<String,String>> SplitValueList = new ArrayList<Map<String,String>>();
    if(Resp.contains("\\n"))
    {
    	Resp = Resp.replace("\\n","&");
    	SplitValueList =  GnetePayUtils.SplitStringToList(Resp, "&", 9);
    }
    else 
    {
		out.write("<div class='InfoPage'><div class='InfoContext'>" + Resp + "</div></div>");
		System.out.println("【对账结果】" + Resp);
		return;
    }
	
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>广州银联网络-查询交易结果(实时对账)示例V3.4</title>
	<link rel="stylesheet" type="text/css" href="../Themes/Style/Gnetpg.css" />
</head>
<body>
	<div class = "BoxBody">
		<div class = "PayHead" >
			<div class = "HeadTitle">商户联调生产环境--查询交易结果(实时对账)示例V3.4</div>
		</div>
		
		<div class="PayTop">
			<div class="PayTopContext"> 
				<span>技术支持人员: 陆苇 </span>
				<span>联系电话：020-85545314</span> 
				<span>邮箱：luwei@chinaums.com</span>
			</div>
		</div>
		
		<div class="PayForm" >
			<table class="ResultList">
				<thead>
					<tr>
						<td>交易日期</td>
						<td>订单金额</td>
						<td>商户支付流水号</td>
						<td>商户终端号</td>
						<td>系统参考号</td>
						<td>响应码</td>
						<td>清算日期</td>
						<td>保留域1</td>
						<td>保留域2</td>
					</tr>
				</thead>
				<tbody>
				<%
					if(SplitValueList != null && SplitValueList.size()>0)
					{
						for(Map<String,String> MapValue : SplitValueList)
						{
				%>
					<tr>
						<td><%=MapValue.get("Key0") %></td>
						<td><%=MapValue.get("Key1") %></td>
						<td><%=MapValue.get("Key2") %></td>
						<td><%=MapValue.get("Key3") %></td>
						<td><%=MapValue.get("Key4") %></td>
						<td><%=MapValue.get("Key5") %></td>
						<td><%=MapValue.get("Key6") %></td>
						<td><%=MapValue.get("Key7") %></td>
						<td><%=MapValue.get("Key8") %></td>
					</tr>
				<%
						}
					}
				%>
				</tbody>
			</table>
			
		</div>
	
		<div class="PayFoot" > 
			版权所有©广州银联网络支付有限公司(中国银联体系公司) 客服热线：4008-333-222
		</div>
	</div>
</body>
</html>