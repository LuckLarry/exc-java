package com.ekc.action.tb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ekc.action.BaseAction;
import com.ekc.ifc.TableUseIfc;

@Controller
@RequestMapping("/tb/userrank.do")
public class UserRank extends BaseAction{
	@Autowired
	TableUseIfc euserrank_Ser;

	@Override
	public TableUseIfc getTabelServer() {
		return euserrank_Ser;
	}
}
