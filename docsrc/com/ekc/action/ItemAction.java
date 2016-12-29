package com.ekc.action;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.demo.CompressPicDemo;
import com.ekc.enumall.Message;
import com.ekc.factory.FactoryBean;
import com.ekc.ifc.ItemDao;
import com.ekc.ifc.TableUseIfc;
import com.ekc.service.ItemImpl;
import com.ekc.util.ItemHelper;
import com.jspsmart.upload.File;
import com.jspsmart.upload.Files;
import com.jspsmart.upload.SmartUpload;

/**
 * 操作erp数据供外部调用，能自己访问的
 * 
 * @author pengbei_qq1179009513
 * 
 */
@Controller
@RequestMapping(value = "/item.do")
public class ItemAction {
	@Autowired
	JdbcTemplate jdbcTemplate;
	private ItemDao cParent = new ItemImpl();

	TableUseIfc ePicture_Ser = (TableUseIfc) FactoryBean
			.getBean("ePicture_Ser");

	@RequestMapping(params = { "m=up" })
	public @ResponseBody
	Map<String, Object> upload(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Map<String, Object> remap = Message.SUCCESS.getObjMess();
		try {
			SmartUpload myUpLoad = new SmartUpload();
			int count = 0;
			// Initialization
			myUpLoad.initialize(request, response);
			// Upload
			myUpLoad.upload();
			Files myFiles = myUpLoad.getFiles();
			String rootPath = ItemHelper.getContextPath();
			String path = "pLoad/"
					+ new SimpleDateFormat("yyyyMMddHHmmssSSS")
							.format(new Date());

			cParent.createFolder(rootPath + path);
			String beginName = null;
			String name = null;
			String houzui = null;
			String endName = null;
			File file = null;
			List<Map<String, Object>> upFileList = new ArrayList<Map<String, Object>>();
			Map<String, Object> fmap = null;
			Random random2 = new Random(100);
			int len = myFiles.getCount();
			if (len > 0) {
				for (int i = 0; i < len; i++) {
					file = myFiles.getFile(i);
					beginName = file.getFileName();// 上传前的名称
					if (beginName.equals("")) {
						continue;
					}
					name = String.valueOf(i) + random2.nextInt();// beginName.substring(0,
					// beginName.indexOf("."));
					houzui = beginName.substring(beginName.indexOf("."));// 过滤文件
					endName = name + houzui;
					fmap = new HashMap<String, Object>();
					fmap.put("beginName", beginName);
					fmap.put("endName", endName);
					fmap.put("upFile", path + "/" + endName);
					fmap.put("fileName", file.getFieldName());
					upFileList.add(fmap);
					file.setFileName(endName);// 重新命名
				}
			} 
			if(upFileList.size()<1) {
				remap = Message.NO_DATA.getObjMess();
			}

			// mySmartUpload
			count = myUpLoad.save("/" + path);
			if (len > 0) {
				// 图片压缩、插入数据库、返回图片ID
				upFileList = this.doImg(upFileList, 200, 200);
				remap.put("upInfo", upFileList);
			}
			// GsonBuilder gsonBuilder = new GsonBuilder();
			// Gson gson = gsonBuilder.create();
			// @SuppressWarnings("unchecked")
			// Map<String, Object> dd = gson.fromJson(readJSONString(request),
			// Map.class);
			// System.out.println(dd.get("dfd"));
			if (count > 0) {
				remap.put("load_count", count);
			}
		} catch (Exception e) {
			e.printStackTrace();
			remap = Message.UN_KNOW.getObjMess(e);
		}
		return remap;
	}
	
	/**
	 * 1.压缩图片
	 * 2.插入数据库
	 * 3.把接收的集合里添加picture_id并返回
	 * @param upFileList 原图集合
	 * @param outputWidth 压缩后宽度
	 * @param outputHeight 压缩后长度
	 * @return
	 * @throws Exception
	 */
	private List<Map<String, Object>> doImg(List<Map<String, Object>> upFileList, int outputWidth,
			int outputHeight) throws Exception {
		List<Map<String, Object>> list = upFileList;
		// 缩略图地址[需要通过压缩图片得到]
		CompressPicDemo cd = new CompressPicDemo();
		String path = ItemHelper.getContextPath().replace("\\", "/");
		// path = path.substring(0, path.length()-1);
		for (int i=0, length = upFileList.size(); i < length; i++) {
			Map<String, Object> map_old = upFileList.get(i);
			Map<String, Object> map = new HashMap<String, Object>();
			map.putAll(map_old);
		
			String upFile = map.get("upFile").toString();
			String all = path + upFile;
			// 缩略图地址
			String getPath = "";
			if (outputWidth > 0 && outputHeight > 0) {
				getPath = cd.doPicture(all, outputHeight, outputHeight);
				getPath = getPath.replace(path, "/");
			}
			String picture_name = map.get("endName").toString();
			// 清空map
			map.clear();
			// 缩略图名称
			map.put("picture_name", picture_name);
			// 缩略图地址
			map.put("thumbnail_path", getPath);
			// 原图地址
			map.put("picture_path", "/".concat(upFile));
			// 添加时间戳
			map.put("add_time", System.currentTimeMillis());
			//得到picture_id
			String pk_value = this.addRow(map);
			//添加到返回集合里
			list.get(i).put("picture_id", pk_value);
		}
		return list;
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
}
