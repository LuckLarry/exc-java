package com.ekc.service;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
*
* 定时器
* 每天定时定点执行程序
* @author ZhengXiajun
* 
*/
public class TimerTest {
    static int count = 0;
    
    public static void showTimer() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                ++count;
                System.out.println("时间=" + new Date() + " 执行了" + count + "次"); // 1次
            }
        };

        //设置执行时间
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);    //每天
        //每天定制每天的00:00:00执行，
        calendar.set(year, month, day, 00, 00, 00);  
        Date date = calendar.getTime();
        Timer timer = new Timer();
        System.out.println(date);
        
        int period = 2 * 1000;
        //每天的date时刻执行task，每隔2秒重复执行
        timer.schedule(task, date, period);
        //每天的date时刻执行task, 仅执行一次
        //timer.schedule(task, date);
    }

    public static void main(String[] args) {
        showTimer();
    }
}

