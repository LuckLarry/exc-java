package com.ekc.action.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ekc.action.BaseAction;
import com.ekc.ifc.TableUseIfc;
/**
* 操作序列化后的session数据表
* @author ZhengXiajun
* 
*/
@Controller
@RequestMapping("/user/sessiondata.do")
public class SessionsDataAction extends BaseAction{
	@Autowired
	TableUseIfc sessdataService;
	@Override
	public TableUseIfc getTabelServer() {
		return sessdataService;
	}	
}
