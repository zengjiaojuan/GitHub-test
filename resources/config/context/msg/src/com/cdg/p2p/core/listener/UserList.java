package com.cddgg.p2p.core.listener;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.cddgg.base.util.StringUtil;
import com.cddgg.commons.log.LOG;
import com.cddgg.p2p.huitou.constant.Constant;

/**   
 * Filename:    UserList.java   
 * @version:    1.0   
 * @since:  JDK 1.7.0_25  
 * Create at:   2014年4月8日 上午10:43:34   
 * Description:  统计当前登录会员
 *   
 */

public class UserList {
    private static final UserList userList = new UserList();
    private Map<String, HttpSession> usermap=new HashMap<String, HttpSession>();
    private int maxUser;
   
    private UserList()
    {
        //v = new Vector();
    }
    
    public static UserList getInstance()
    {
        return userList;
    }
    //将用户登陆身份证保存到Vector中
    public void addUser(String sfz,HttpSession session) throws Exception
    {
        
        try{
           if (StringUtil.isNotBlank(sfz))
           {
               HttpSession newsession=usermap.get(sfz);
               if(null==newsession){
                   usermap.put(sfz, session);
               }else{
                   //从session中移除登录信息
                   newsession.removeAttribute(Constant.SESSION_USER);
                   newsession.setAttribute("logout", "您的账号在别处登录，您已被迫下线。如果不是您本人在登录，请您尽快修改密码！");
                   //从在线会员列表中移除当前会员
                   usermap.remove(sfz);
                   LOG.error("让第一个登录的用户登录信息失效！！！");
                   //在线会议列表中添加当前会员
                   usermap.put(sfz, session);
               }
               if(getUserCount()>maxUser){
                maxUser=getUserCount();
               }
           }
        }
         catch(Exception ex)
        {
            LOG.error("统计登录会员失败！"+ex);
        }
        finally{
        }
    }
   
    //删除用户登录ID
    public void RemoveUser(String sfz)throws Exception
    {
        try{
           if (StringUtil.isNotBlank(sfz))
           { 
              //修改数据库
              //移除用户登录ID
               usermap.remove(sfz);
           }
        }
        catch(Exception ex)
        {    
            LOG.error("移除登录会员失败！"+ex);
        }
        finally{
        }
    }
    //返回在线人数
    public int getUserCount()
    {
        return usermap.size();
    }
    //返回在线人数峰值
    public int getMaxUser(){
     return maxUser;
    }
}