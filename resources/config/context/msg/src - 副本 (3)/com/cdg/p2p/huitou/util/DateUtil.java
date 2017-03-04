package com.cddgg.p2p.huitou.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.cddgg.commons.date.DateUtils;

/**   
 * Filename:    DateUtil.java   
 * @version:    1.0   
 * @since:  JDK 1.7.0_25  
 * Create at:   2014年3月27日 下午1:41:24   
 * Description:  
 *   
 */

/**
* <p>Title:DateUtil</p>
* <p>Description: 时间计算工具，继承自com.cddgg.commons.date.DateUtils</p>
* <p>date 2014年3月27日</p>
*/
public class DateUtil extends DateUtils {
    
    
    /**
    * <p>Title: getSpecifiedMonthAfter</p>
    * <p>Description: 计算传入时间加上传入月份后的年月日</p>
    * @param specifiedDay 年月日
    * @param monthNum 要增加的月份数（传入正数相加，负数相减）
    * @return 传入时间加上传入月份后的时间如传入（2014-03-27,3） 返回的则是2014-06-27
    */
    public static String getSpecifiedMonthAfter(String specifiedDay, int monthNum) {
        
        Calendar c = Calendar.getInstance();
        Date date = null;
        try {
            date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTime(date);
        int month = c.get(Calendar.MONTH);
        c.set(Calendar.MONTH, month + monthNum);

        String dayAfter = new SimpleDateFormat("yyyy-MM-dd")
                .format(c.getTime());
        return dayAfter;
    }
    
    public static void main(String[] args) {
        
        System.err.println(DateUtil.getSpecifiedMonthAfter(DateUtil.format("yyyy-MM-dd"), -3));
        
    }
}
