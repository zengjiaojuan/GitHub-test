package com.phb.puhuibao.web.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Test {
	public static void main(String args[]) throws ParseException{
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat df = new SimpleDateFormat ("EEE MMM dd HH:mm:ss Z yyyy", Locale.UK);
		 Date dt= df.parse("Wed Apr 27 00:00:00 CST 2016");//转换成功的Date对象
	     Long time  = dt.getTime();//这就是距离1970年1月1日0点0分0秒的毫秒数
	     Long time2 = System.currentTimeMillis();//与上面的相同,获取系统当前时间毫秒数
	     System.out.println((time2-time)/(1000*60*60*24));//输出输入时间与当前时间的时间差，结果是毫秒数
	     System.out.println (sdf.format(dt));
	     int year=0; 
	     int month=0; 
	     int day=0; 
	     Calendar c=Calendar.getInstance();//获得系统当前日期 
	     year=c.get(Calendar.YEAR); 
	     month=c.get(Calendar.MONTH)+1;//系统日期从0开始算起 
	     day=c.get(Calendar.DAY_OF_MONTH);  
	     System.out.println("当前系统时间："+year+"年"+month+"月"+day+"日");//
	     
	}

}
