package com.ekc.service;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.TimerTask;
/**
*
* 定时器
* @author ZhengXiajun
* 
*/
public class TimeTast extends TimerTask {
	private SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	@Override
	public void run(){
	System.out.println("现在北京时间："+sf.format(new Date())); 
	}
	
}

	