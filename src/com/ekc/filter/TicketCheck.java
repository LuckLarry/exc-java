package com.ekc.filter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.ekc.enumall.Message;
import com.ekc.factory.FactoryBean;
import com.ekc.util.ItemHelper;
import com.ekc.xml.MessageXml;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tencent.WXPay;

public class TicketCheck implements Filter {

	public void destroy() {

	}

	/**
	 * 可以直接访问配置
	 */
	private Map<String, Object> toMap = null;
	/**
	 * 退出ticket
	 */
	private String exit = "/account/logout.do";
	private String itemPath = null;// 项目
	private Log log = LogFactory.getLog(getClass());

	public void doFilter(ServletRequest req, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		try {
			HttpServletRequest request = (HttpServletRequest) req;
			ItemHelper.setItemUrl(request);
			String url = request.getRequestURI();// .substring(1);
			if (itemPath == null) {
				itemPath = request.getContextPath();
				exit = itemPath + exit;
			}
			if (itemPath != "" && url.indexOf(itemPath) == 0) {
				url = url.substring(itemPath.length());
			}
			Map<String, String[]> paramMap=req.getParameterMap();
			int i=0;
			for (String key:paramMap.keySet()) {
				url += (i==0)?"?":"@";
				if (req.getParameter(key) != null){
					url += key+"=" + req.getParameter(key);
					i=1;
				}
			}
			// 排除api请求
			if (hasUrl(url)) {
				chain.doFilter(request, response);
			} else if (url.contains(".do")) {// TODO 判断缓存
				checkTicket(response, chain, request);
			} else {// 排除网页
				chain.doFilter(request, response);
			}
		} catch (Exception e) {
			printJson(response, Message.UN_KNOW.getObjMess(e));
		}
	}

	/**
	 * 是否可以直接访问
	 * 
	 * @param toDo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private boolean hasUrl(String url) {
		if (toMap.containsKey("not_ticket")) {
			for (String v : (List<String>) toMap.get("not_ticket")) {
				if (v.equals(url) || url.contains(v)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 检查ticket权限
	 * 
	 * @param response
	 * @param chain
	 * @param request
	 * @throws IOException
	 * @throws ServletException
	 */
	private void checkTicket(ServletResponse response, FilterChain chain,
			HttpServletRequest request) throws IOException, ServletException {
		String url = request.getRequestURI();// .substring(1);
		String ticket = request.getHeader(MessageXml.ticket_key);
		log.info(ticket);
		ServletContext context = request.getServletContext();
		// ticket 存在与否
		Object ticketObj = context.getAttribute(MessageXml.ticket_key);
		if (ticketObj != null) {
			@SuppressWarnings("unchecked")
			Map<String, String> ticketMap = (Map<String, String>) ticketObj;
			String uticket = null;
			for (String key : ticketMap.keySet()) {
				uticket = ticketMap.get(key);
				if (uticket.equals(ticket)) {
					if (exit.contains(url)) {
						ticketMap.remove(key);
						context.setAttribute(MessageXml.ticket_key, ticketMap);
						printJson(response, Message.EXIT.getObjMess());
					} else {
						chain.doFilter(request, response);
					}
					return;
				}
			}

		}
		if (!ItemHelper.isEmpty(ticket)&&"e895482e-7662-4aa1-bdc7-a6fb3e806ccd".equals(ticket)) {// TODO 暂时释放权限 但必须传tikect
			chain.doFilter(request, response);
			return;
		}
		// TODO 不存在ticket情况处理 如重启tomcat 使登陆用户继续保持操作权限处理 待实现
		// end
		printJson(response, Message.NO_TICKET.getObjMess());
	}

	/**
	 * 输出消息
	 * 
	 * @param response
	 * @param jsonObj
	 * @throws IOException
	 */
	private void printJson(ServletResponse response, Object jsonObj)
			throws IOException {
		PrintWriter out = response.getWriter();
		response.setContentType("application/json");
		GsonBuilder gsonBuilder = new GsonBuilder();
		Gson gson = gsonBuilder.create();
		String json = gson.toJson(jsonObj);
		out.print(json);
		out.flush();
		out.close();
	}

	@SuppressWarnings("unchecked")
	public void init(FilterConfig filterConfig) throws ServletException {
		String toJson = filterConfig.getInitParameter("unchecked");
		System.out.println(filterConfig.getInitParameter("itempath"));
		GsonBuilder gsonBuilder = new GsonBuilder();
		Gson gson = gsonBuilder.create();
		toMap = gson.fromJson(toJson, Map.class);
		// toMap可以在此后追加也可以在toJson中追加

		log.info("---------ticket start---------");
		// 微信支付配置
		String wx_param = filterConfig.getInitParameter("wx_param");
		Map<String, String> wxMap = gson.fromJson(wx_param, Map.class);
		WXPay.initSDKConfiguration(wxMap.get("key"), wxMap.get("appID"),
				wxMap.get("mchID"), wxMap.get("sdbMchID"),
				wxMap.get("certLocalPath"), wxMap.get("certPassword"));
		// 初始factoryBean
		WebApplicationContext wac = WebApplicationContextUtils
				.getWebApplicationContext(filterConfig.getServletContext());
		FactoryBean.setCtx(wac);
		ItemHelper.setContextPath(filterConfig.getServletContext().getRealPath(
				"/"));
		// 初始结束
	}

	public String getContent(ServletRequest request) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(
				request.getInputStream()));
		StringBuilder sb = new StringBuilder();
		String line = null;
		while ((line = in.readLine()) != null) {
			sb.append(line);
		}
		System.out.println(sb);
		return sb.toString();
	}
}