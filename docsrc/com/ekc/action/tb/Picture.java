package com.ekc.action.tb;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekc.action.BaseAction;
import com.ekc.enumall.Message;
import com.ekc.ifc.TableUseIfc;
/**
 * 图片
 * @author Gaohui
 *
 */

@Controller
@RequestMapping("/tb/picture.do")
public class Picture extends BaseAction {
	@Autowired
	TableUseIfc  ePicture_Ser;
	@Override
	public TableUseIfc getTabelServer() {
		return  ePicture_Ser;
	}
	
	/**
	 * 压缩图片
	 * @param map
	 * @return
	 */
	public @ResponseBody Map<String, Object> Image(@RequestBody Map<String, Object> map){
		Map<String, Object> mapRe = Message.SUCCESS.getObjMess();
		
		return mapRe;
	}
}
