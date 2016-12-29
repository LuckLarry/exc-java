package com.ekc.action.tb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ekc.action.BaseAction;
import com.ekc.ifc.TableUseIfc;
/**
 * 图片位置
 * @author Gaohui
 *
 */

@Controller
@RequestMapping("/tb/ppla.do")
public class Pictureplaceobject extends BaseAction {
	@Autowired
	TableUseIfc  ePicturePlaceObject_Ser;
	@Override
	public TableUseIfc getTabelServer() {
		return  ePicturePlaceObject_Ser;
	}	
}
