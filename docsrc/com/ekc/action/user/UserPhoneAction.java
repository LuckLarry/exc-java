package com.ekc.action.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ekc.action.BaseAction;
import com.ekc.ifc.TableUseIfc;
@Controller
@RequestMapping("/user/userphone.do")
public class UserPhoneAction extends BaseAction {
	@Autowired
	TableUseIfc userphoneSer;
	@Override
	public TableUseIfc getTabelServer() {
		return userphoneSer;
	}

}
