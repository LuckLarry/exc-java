package com.ekc.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;

import com.baidu.ueditor.ConfigManager;
import com.baidu.ueditor.define.ActionMap;
import com.baidu.ueditor.define.AppInfo;
import com.baidu.ueditor.define.BaseState;
import com.baidu.ueditor.define.State;
import com.baidu.ueditor.hunter.FileManager;
import com.baidu.ueditor.hunter.ImageHunter;
import com.baidu.ueditor.upload.Uploader;
import com.demo.CompressPicDemo;
import com.ekc.factory.FactoryBean;
import com.ekc.ifc.TableUseIfc;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ActionEnter {
	
	TableUseIfc ePicture_Ser = (TableUseIfc) FactoryBean
			.getBean("ePicture_Ser");

	private HttpServletRequest request = null;

	private String rootPath = null;
	private String contextPath = null;

	private String actionType = null;

	private ConfigManager configManager = null;
	private String outputWidth_key = "outputWidth"; 
	private String outputHeight_key = "outputHeight"; 

	public ActionEnter(HttpServletRequest request, String rootPath) {
		this.request = request;
		this.rootPath = rootPath;
		this.actionType = request.getParameter("action");
		this.contextPath = request.getContextPath();
		this.configManager = ConfigManager.getInstance(this.rootPath,
				this.contextPath, request.getRequestURI());
		}

	public String exec() {
		return exec(0, 0);
	}

	public String exec(int outputWidth, int outputHeight) {

		String callbackName = this.request.getParameter("callback");

		if (callbackName != null) {

			if (!validCallbackName(callbackName)) {
				return new BaseState(false, AppInfo.ILLEGAL).toJSONString();
			}

			return callbackName + "(" + this.invoke(outputWidth, outputHeight)
					+ ");";

		} else {
			return this.invoke(outputWidth, outputHeight);
		}

	}

	public String invoke() {
		return invoke(0, 0);
	}

	@SuppressWarnings("unchecked")
	public String invoke(int outputWidth, int outputHeight) {

		if (actionType == null || !ActionMap.mapping.containsKey(actionType)) {
			return new BaseState(false, AppInfo.INVALID_ACTION).toJSONString();
		}

		if (this.configManager == null || !this.configManager.valid()) {
			return new BaseState(false, AppInfo.CONFIG_ERROR).toJSONString();
		}

		State state = null;

		int actionCode = ActionMap.getType(this.actionType);

		Map<String, Object> conf = null;
		
		switch (actionCode) {

		case ActionMap.CONFIG:
			return this.configManager.getAllConfig().toString();

		case ActionMap.UPLOAD_IMAGE:
		case ActionMap.UPLOAD_SCRAWL:
		case ActionMap.UPLOAD_VIDEO:
		case ActionMap.UPLOAD_FILE:
			conf = this.configManager.getConfig(actionCode);
			state = new Uploader(request, conf).doExec();
			// 添加picture_id
			String str = state.toJSONString();
			GsonBuilder gsonBuil = new GsonBuilder();
			Gson gson = gsonBuil.create();
			//str 转为 map
			Map<String, Object> map = gson.fromJson(str, Map.class);
			if (outputWidth != 0 && outputHeight != 0) {
				map.put(outputWidth_key, outputWidth);
				map.put(outputHeight_key, outputHeight);
			}
			return doPicture(map);

		case ActionMap.CATCH_IMAGE:
			conf = configManager.getConfig(actionCode);
			String[] list = this.request.getParameterValues((String) conf
					.get("fieldName"));
			state = new ImageHunter(conf).capture(list);
			break;

		case ActionMap.LIST_IMAGE:
		case ActionMap.LIST_FILE:
			conf = configManager.getConfig(actionCode);
			int start = this.getStartIndex();
			state = new FileManager(conf).listFile(start);
			// TODO 这里是在线管理图片 添加picture_id
			String s = state.toJSONString();
			rootPath=rootPath.replace("\\", "/");
			s=s.replace("\"url\": \""+rootPath, "\"url\": \"/");
			GsonBuilder gb = new GsonBuilder();
			Gson g = gb.create();
			//s 转为 Map<String, Object>
			Map<String, Object> img = g.fromJson(s, Map.class);
			List<Map<String, Object>> img_list = (List<Map<String, Object>>)img.get("list");
			Map<String, Object> mapRe = null;
			for (int i = 0, length = img_list.size(); i < length; i++) {
				mapRe = img_list.get(i);
				String url_all = mapRe.get("url").toString();
				String picture_name =  url_all.substring(url_all.lastIndexOf("/")+1, url_all.length());
				String pk_value = getPkValue(picture_name);
				//没有ID的图片将不显示出来
				if(!ItemHelper.isEmpty(pk_value)){
					img_list.get(i).put("picture_id", pk_value);
				}
			}
			img.put("list", img_list);
			JSONObject jsonObject = new JSONObject(img);
			return jsonObject.toString();

		}

		return state.toJSONString();

	}

	public int getStartIndex() {

		String start = this.request.getParameter("start");

		try {
			return Integer.parseInt(start);
		} catch (Exception e) {
			return 0;
		}

	}

	/**
	 * callback参数验证
	 */
	public boolean validCallbackName(String name) {

		if (name.matches("^[a-zA-Z_]+[\\w0-9_]*$")) {
			return true;
		}

		return false;

	}

	/**
	 * @see 需要压缩并添加数据库
	 * @param state
	 * @return
	 */
	private String doPicture(Map<String, Object> jo) {
		try {
			// 图片在项目里的路径[并非全路径]
			String url = "url";
			// 插入到数据库里后图片的name
			String title = "title";
			// 用于保存数据库map
			Map<String, Object> map = new HashMap<String, Object>();
			if(jo.get(title)==null){
				map.put("picture_name","无名称");
			}else{
				// 图片名称
				map.put("picture_name", jo.get(title).toString());
			}
			String getPath = "";
			//如果存在宽和高 那么就需要压缩图片
			if (jo.containsKey(outputWidth_key) && jo.containsKey(outputHeight_key)){
				// 缩略图地址[需要通过压缩图片得到]
				CompressPicDemo cd = new CompressPicDemo();
				String path = ItemHelper.getContextPath().replace("\\", "/");
				path = path.substring(0, path.length() - 1);
				String all = path + jo.get(url).toString();
				getPath = cd.doPicture(all,
						Integer.parseInt(jo.get(outputWidth_key).toString()),
						Integer.parseInt(jo.get(outputHeight_key).toString())).replace(
						path, "");
			}
			map.put("thumbnail_path", getPath);
			if(jo.get(url)==null){
				map.put("picture_path","");
			}else{
				// 原图地址
				map.put("picture_path", jo.get(url).toString());
			}
			// 添加时间戳
			map.put("add_time", System.currentTimeMillis());
			// 插入图片
			String picture_id = this.addRow(map);
			// 如果picture_id不等于空,那么返回picture_id
			if (!ItemHelper.isEmpty(picture_id)) {
				jo.put(ePicture_Ser.getPrimaryKey(), picture_id);
				JSONObject jsonObject = new JSONObject(jo);
				return jsonObject.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * @see 添加图片数据并返回picture_id
	 * @param map
	 * @return
	 * @throws Exception
	 */
	private String addRow(Map<String, Object> map) throws Exception {
		String pk = ePicture_Ser.getPrimaryKey();
		String pk_value = ItemHelper.createPrimaryKey();
		map.put(pk, pk_value);
		int code = ePicture_Ser.addRow(map);
		if (code > 0) {
			return pk_value;
		}
		return "";
	}
	
	/**
	 * @see 获得picture_id
	 * @param map
	 * @return
	 * @throws Exception
	 */
	private String getPkValue(String picture_name) {
		String pk = ePicture_Ser.getPrimaryKey();
		String pk_value = "";
		try {
			Map<String, Object> pv = ePicture_Ser.findRow("picture_name",picture_name);
			if(pv!=null){
			    pk_value=pv.get(pk).toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pk_value;
	} 
}