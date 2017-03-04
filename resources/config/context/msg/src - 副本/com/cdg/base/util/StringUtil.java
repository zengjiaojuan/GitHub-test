package com.cddgg.base.util;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Pattern;

import org.jasypt.util.text.BasicTextEncryptor;

import com.cddgg.commons.date.DateUtils;

/**
 * <p>
 * Title:StringUtil
 * </p>
 * <p>
 * Description: 字符处理类
 * </p>
 *         <p>
 *         date 2014年2月7日`
 *         </p>
 */
public class StringUtil {

    /** textEncryptor */
    private static BasicTextEncryptor textEncryptor;

    static {
        textEncryptor = new BasicTextEncryptor();
        textEncryptor.setPassword("QQmima147");
    }

    /**
     * <p>
     * Title: getTextEncryptor
     * </p>
     * <p>
     * Description: 获取加密对象
     * </p>
     * 
     * @return 加密对象
     */
    public static BasicTextEncryptor getTextEncryptor() {
        return textEncryptor;
    }

    /** 特殊字符 */
    private static String[] str = new String[] { "<", ">", "'", "￠", "£", "¥",
            "€", "§", "©", "®", "™", "×", "÷" };

    /** 特殊字符替代符 */
    private static String[] newstr = new String[] { "&lt;", "&gt;", "&apos;",
            "&cent;", "&pound;", "&yen;", "&euro;", "&sect;", "&copy;",
            "&reg;", "&trade;", "&times;", "&divide;" };

    /**
     * <p>
     * Title: replaceAll
     * </p>
     * <p>
     * Description: 将特殊字符转换成网页可以显示的符号（防止用户写入html标签或javascript代码）
     * </p>
     * 
     * @param content
     *            要替换的字符串
     * @return 替换后的字符串
     */
    public static String replaceAll(String content) {

        if (content == null || "".equals(content)) {

        } else {
            for (int i = 0; i < str.length; i++) {
                content = content.replaceAll(str[i], newstr[i]);
            }
        }

        return content;
    }

    /**
     * <p>
     * Title: formateNumber
     * </p>
     * <p>
     * Description: 格式化数字，如大数值时以1.0000E方式时，转换为完整显示，不以科学计数法显示，并标以
     * </p>
     * 
     * @param val
     *            要格式化的科学数字
     * @return 100,000,000.00
     */
    public static String formateNumber(Object val) {
            return null == val ? "0.00" : formateNumber(val, true);
    }

    /**
     * <p>
     * Title: formateNumber
     * </p>
     * <p>
     * Description: 对数字进行格式化
     * </p>
     * 
     * @param val
     *            要格式化的数字
     * @param groupingUsed
     *            。。
     * @return 格式化后的数字
     */
    private static String formateNumber(Object val, boolean groupingUsed) {
        val = (null == val) ? "0.00" : val;
        NumberFormat f = DecimalFormat.getInstance(Locale.CHINA);
        f.setMaximumFractionDigits(2);
        f.setMinimumFractionDigits(2);
        f.setGroupingUsed(groupingUsed);
        return f.format(val);
    }

    public static String getBirthByIdCard(String idCard){
        
        if (idCard!=null && idCard.length() == 18){
            return idCard.substring(6, 10) + "-" + idCard.substring(10, 12) + "-" + idCard.substring(12, 14);
        }else {
             return null;
        }
        
    }
    
    public static int getAgeByBirth(String birth){
        
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date date_birth = null;
        try {
            date_birth = simpleDateFormat.parse(birth);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
        
        java.util.Date date_cur = new Date();
        int age = (date_cur.getYear()) - date_birth.getYear();
        if(date_cur.getMonth()>date_birth.getMonth())   age++;
        
        return age;
        
    }
    
    public static int getAgeByIdCard(String idCard){
        if(idCard==null)    return 0;
        return getAgeByBirth(getBirthByIdCard(idCard));
    }
    
    /**
     * <p>
     * Title: isBlank
     * </p>
     * <p>
     * Description: 判断是否为空 null、" "、"" 均返回true
     * </p>
     * 
     * @param str
     *            要判断的字符
     * @return 空就为true
     */
    public static boolean isBlank(String str) {
        boolean result = true;

        if (str != null) {
            for (int i = 0; i < str.length(); i++) {
                if (str.charAt(i) != ' ') {
                    result = false;
                    break;
                }
            }
        }

        return result;
    }

    /**
     * <p>
     * Title: isNotBlank
     * </p>
     * <p>
     * Description: 判断是否非空
     * </p>
     * 
     * @param str
     *            要判断的字符
     * @return true表示非空
     */
    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    /**
     * <p>
     * Title: isNumberString
     * </p>
     * <p>
     * Description: 如果str中的每一位都是数字，返回true，否则返回false
     * </p>
     * 
     * @param str
     *            需要判断的字符
     * @return 判断结果
     */
    public static boolean isNumberString(String str) {
        boolean result = true;
        if (str != null) {
            for (int i = 0; i < str.length(); i++) {
                if (!Character.isDigit(str.charAt(i))) {
                    result = false;
                    break;
                }
            }
        }
        return result;
    }

    /**
     * <p>
     * Title: getvalidcode
     * </p>
     * <p>
     * Description: 获取一个6位数字的验证码
     * </p>
     * 
     * @return 返回6个数字
     */
    public static String getvalidcode() {
        Random random = new Random();
        int num = 100000 + random.nextInt(899999) + 1;
        return num + "";
    }

    /**
     * <p>
     * Title: sliptTitle
     * </p>
     * <p>
     * Description: 分割字符串
     * </p>
     * 
     * @param titles
     *            要分割的字符串
     * @param regex
     *            根据什么分割 默认根据","分割，不能根据空格分割
     * @return 分割后的数组
     */
    public static String[] sliptTitle(String titles, String regex) {

        if (StringUtil.isNotBlank(regex)) {
            return titles.split(regex);
        } else {
            return titles.split(",");
        }
    }

    /**
     * <p>
     * Title: generatePassword
     * </p>
     * <p>
     * Description: 将输入的字符串进行base64加密
     * </p>
     * 
     * @param inputString
     *            要加密的字符串
     * @return 加密后的字符串
     */
    public static String generatePassword(String inputString) {
        String result = null;

        if (StringUtil.isNotBlank(inputString)) {
            result = getTextEncryptor().encrypt(inputString);
        }
        return result;
    }

    /**
     * <p>
     * Title: correctPassword
     * </p>
     * <p>
     * Description: 将base64加密的字符串进行解密
     * </p>
     * 
     * @param generatePassword
     *            要解密的字符串
     * @return 解密后的字符串
     */
    public static String correctPassword(String generatePassword) {

        String result = null;
        if (StringUtil.isNotBlank(generatePassword)) {
            result = getTextEncryptor().decrypt(generatePassword);
        }
        return result;
    }
    /**
     * 生成一个长度为12的字符串(当前时间+四位的随机数)
     *
     * @return 返回一个长度为12的字符串
     */
    public static String pMerBillNo(){
        //得到当前时间
        String time = DateUtils.format("yyyyMMddhhmmss");
        Random randome = new Random();
        int num =100000 + randome.nextInt(899999) + 1;
        return time+(num+"");
    }
    /**
     * 用正则表达式判断用户输入的字符串是否为数字
     * @param str 
     * @return
     */
    public static boolean isNumeric(String str){ 
        Pattern pattern = Pattern.compile("[0-9]*"); 
        return pattern.matcher(str).matches();    
     } 
    
    /**
     * 获取连个时间点间的月数
     * @param date1 开始时间
     * @param date2 结束时间
     * @return
     */
    public static Integer getMonth(String date1,String date2){
    	DateFormat dd=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date dateone=null;
		Date datetwo=null;
		try {
			dateone = dd.parse(date1);
			datetwo = dd.parse(date2);
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Calendar cal1 = new GregorianCalendar();
		  cal1.setTime(datetwo);
		  Calendar cal2 = new GregorianCalendar();
		  cal2.setTime(dateone);
		  int c =
		   (cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR)) * 12 + cal1.get(Calendar.MONTH)
		    - cal2.get(Calendar.MONTH);
		  return c;
    }
}
