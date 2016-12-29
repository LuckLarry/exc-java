package com.ekc.action.tb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ekc.action.BaseAction;
import com.ekc.ifc.TableUseIfc;
/**
 * 位置
 * @author Gaohui
 *
 */

@Controller
@RequestMapping("/tb/pictureplace.do")
public class Pictureplace extends BaseAction {
	@Autowired   
	TableUseIfc  ePicturePlace_Ser;
	@Override
	public TableUseIfc getTabelServer() {
		return  ePicturePlace_Ser;
	}	
}
