package com.cddgg.p2p.huitou.spring.service;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cddgg.base.spring.orm.hibernate.impl.HibernateSupport;
import com.cddgg.base.spring.service.LocalService;
import com.cddgg.base.util.StringUtil;
import com.cddgg.commons.date.DateUtils;
import com.cddgg.commons.normal.Md5Util;
import com.cddgg.p2p.huitou.constant.Constant;
import com.cddgg.p2p.huitou.entity.Generalize;
import com.cddgg.p2p.huitou.entity.MemberNumber;
import com.cddgg.p2p.huitou.entity.Messagesetting;
import com.cddgg.p2p.huitou.entity.Messagetype;
import com.cddgg.p2p.huitou.entity.Userbasicsinfo;
import com.cddgg.p2p.huitou.entity.Userfundinfo;
import com.cddgg.p2p.huitou.entity.Userloginlog;
import com.cddgg.p2p.huitou.entity.Usermessage;
import com.cddgg.p2p.huitou.entity.Userrelationinfo;
import com.cddgg.p2p.huitou.entity.Validcodeinfo;
import com.cddgg.p2p.huitou.util.GenerateLinkUtils;

import freemarker.template.TemplateException;

/**
 * 用户注册
 * 
 * @author ransheng
 * 
 */
@Service
@SuppressWarnings(value = { "unchecked", "rawtypes" })
public class RegistrationService {

    /**
     * 数据库操作接口
     */
    @Resource
    private HibernateSupport commonDao;

    /**
     * 获取本地服务
     */
    @Resource
    private LocalService localService;

    /**
     * 邮件发送工具类
     */
    @Resource
    private MyindexService myindexService;

    /**
     * 用户注册
     * 
     * @param userName
     *            用户名
     * @param email
     *            邮箱
     * @param pwd
     *            登陆了密码
     * @param number
     *            会员编号
     * @param promoter
     *            推广人
     * @param request
     *            request
     * @return 如果存在，返回用户信息，不存在返回null
     * 
     * @throws TemplateException
     *             TemplateException
     * @throws IOException
     *             IOException
     * @throws DataAccessException
     *             DataAccessException
     */
    @Transactional(rollbackFor = Exception.class)
    public Userbasicsinfo registrationSave(String userName, String email,
            String pwd, Userbasicsinfo promoter,
            HttpServletRequest request) throws DataAccessException,
            IOException, TemplateException {

        // 当前时间
        String date = DateUtils.format(null);

        // 用户基本信息
        Userbasicsinfo userInfo = new Userbasicsinfo();
        // 用户关联信息表
        Userrelationinfo user = new Userrelationinfo();
        // 用户资金信息
        Userfundinfo userFund = new Userfundinfo();
        // 用户系统消息
        Usermessage userMessage = new Usermessage();
        //邮箱限制信息
        Validcodeinfo validcodeinfo=new Validcodeinfo();
        

        // 注册时间
        userInfo.setCreateTime(date);
        // 邮箱激活验证码
        userInfo.setRandomCode(StringUtil.getvalidcode());
        // 是否被锁[1-是，0-否]
        userInfo.setIsLock(0);
        // 登录错误次数
        userInfo.setErrorNum(0);
        // 是否为债权人[1-是，0-否]
//        userInfo.setIsCreditor(0);
        // 用户名
        userInfo.setUserName(userName.trim());
        // 初始化登录密码
        userInfo.setPassword(pwd);
        // 初始化交易密码（与登录密码一致）
        userInfo.setTransPassword(pwd);
        //昵称默认为用户名
        userInfo.setNickname(userName.trim());
        // 保存会员基本信息
        commonDao.save(userInfo);

        // 判断是否有推广人，如果有插入推广数据
        if (promoter != null) {
            Generalize generalize = new Generalize();
            generalize.setAdddate(date);
            generalize.setGenuid(generalize.getId());
            generalize.setUid(userInfo.getId());
            generalize.setUanme(userInfo.getUserName());
            // 保存推广信息
            commonDao.save(generalize);
        }

        // 邮箱
        user.setEmail(email);
        // 是否激活邮箱
        user.setEmailisPass(0);
        // 默认头像路径http://www.huitou365.com/resources/images/vipuserImgBG1.jpg
        user.setImgUrl("/resources/images/vipuserImgBG5.png");
        // 用户基本信息
        user.setUserbasicsinfo(userInfo);
        // 保存用户关联信息
        commonDao.save(user);

        //会员邮箱限制信息
        validcodeinfo.setUserbasicsinfo(userInfo);
        validcodeinfo.setEmailagaintime(System.currentTimeMillis()+2*60*1000L);
        validcodeinfo.setEmailovertime(System.currentTimeMillis()+24*60*60*1000L);
        validcodeinfo.setEmailcode(userInfo.getRandomCode());
        commonDao.save(validcodeinfo);
        
        // 用户基本信息
        userFund.setUserbasicsinfo(userInfo);
        // 可用余额
        userFund.setCashBalance(0.00);
        // 奖金余额
        userFund.setBonusBalance(0.00);
        // 授信额度
        userFund.setCredit(0.00);
        // 保存用户资金信息
        commonDao.save(userFund);

        // 用户基本信息
        userMessage.setUserbasicsinfo(userInfo);
        // 消息内容
        userMessage.setContext("恭喜您成功注册成为p2p网贷平台的用户！您的注册帐户是:" + userName + " 邮箱是:"
                + email);
        // 未读
        userMessage.setIsread(0);
        // 发送时间
        userMessage.setReceivetime(date);
        // 发送主题
        userMessage.setTitle("注册成功");
        // 保存系统消息
        commonDao.save(userMessage);

        // 关联用户基本信息
        userInfo.setUserrelationinfo(user);
        commonDao.update(userInfo);
        
        String sql="SELECT id,`name`,explan FROM messagetype WHERE `name`='新上标提醒'";
        //默认新标上线短信提示
        Messagesetting messagesetting=new Messagesetting();
        messagesetting.setEmailIsEnable(false);
        messagesetting.setSmsIsEnable(true);
        messagesetting.setSysIsEnable(false);
        messagesetting.setUserbasicsinfo(userInfo);
        messagesetting.setMessagetype(commonDao.findObjectBySql(sql, Messagetype.class));
        commonDao.save(messagesetting);
        

        // 发送邮件，激活邮箱
        myindexService.sendEmail(userInfo, request);
        // 保存注册用户session
        request.getSession().setAttribute(Constant.SESSION_USER, userInfo);

        return userInfo;
    }

    /**
     * 验证用户名是否唯一
     * 
     * @param userName
     *            用户名
     * @return 唯一true 不唯一false
     */
    public boolean checkUserName(String userName) {
        // 定义返回值变量
        boolean bool = true;
        StringBuffer sql = new StringBuffer(
                "SELECT COUNT(*) FROM userbasicsinfo a WHERE a.userName='")
                .append(userName.trim()).append("'");
        // 配备该用户名下的条数
        Object count = commonDao.findObjectBySql(sql.toString());
        if (Integer.parseInt(count.toString()) > 0) {
            bool = false;
        }
        return bool;
    }

    /**
     * 验证邮箱唯一性
     * 
     * @param email
     *            邮箱
     * @return 唯一true 不唯一false
     */
    public boolean checkEmail(String email) {
        // 定义返回值变量
        boolean bool = true;
        StringBuffer sql = new StringBuffer(
                "SELECT COUNT(*) FROM userrelationinfo a WHERE a.email='"
                        + email.trim() + "'");
        // 配备该用户名下的条数
        Object count = commonDao.findObjectBySql(sql.toString());
        if (Integer.parseInt(count.toString()) > 0) {
            bool = false;
        }
        return bool;
    }

    /**
     * 登录
     * 
     * @param userName
     *            用户名（邮箱）
     * @param pwd
     *            密码
     * @return 登录成功返回Userbasicsinfo对象
     */
    public Userbasicsinfo loginMethod(String userName, String pwd) {
        Userbasicsinfo userInfo = null;
        // 查询用户名（邮箱）、密码是否匹配
        StringBuffer hql = new StringBuffer(
                "from Userbasicsinfo a,Userrelationinfo b where (a.userName=? or b.email=?) and a.password=?");
        pwd = Md5Util.execute(pwd);
        List list = commonDao.find(hql.toString(), userName, userName, pwd);
        // 如果登录成功
        if (list.size() > 0) {
            Object[] obj = (Object[]) list.get(0);
            userInfo = (Userbasicsinfo) obj[0];
        }
        return userInfo;
    }

    /**
     * 判断用户是否被管理员锁定
     * 
     * @param user
     *            用户对象
     * @return true 锁定，false 未锁定
     */
    public boolean isLock(Userbasicsinfo user) {
        StringBuffer sql = new StringBuffer(
                "SELECT COUNT(*) FROM userbasicsinfo a WHERE a.isLock=1 AND id=?");
        Object obj = commonDao.findObjectBySql(sql.toString(), user.getId());
        // 如果大于0，锁定
        if (Integer.parseInt(obj.toString()) > 0) {
            return true;
        }
        return false;
    }

    /**
     * 查询用户登录错误次数
     * 
     * @param userName
     *            用户名
     * @return 错误次数
     */
    public Integer errorCount(String userName) {
        Integer marked = 0;
        // 查询用户是否存在
        StringBuffer hql = new StringBuffer(
                "from Userbasicsinfo a,Userrelationinfo b where (a.userName=? or b.email=?)");
        List list = commonDao.find(hql.toString(), userName, userName);
        String date = DateUtils.format(null);
        // 如果用户存在
        if (list.size() > 0) {
            Object[] obj = (Object[]) list.get(0);
            Userbasicsinfo userInfo = (Userbasicsinfo) obj[0];
            if(userInfo.getErrorNum()==null){
                userInfo.setErrorNum(0);
            }
            Integer errorCount = userInfo.getErrorNum() + 1;
            errorCount = errorCount > 5 ? 5 : errorCount;
            // 错误次数
            userInfo.setErrorNum(errorCount);
            if (errorCount == 5) {
                // 锁定时间
                userInfo.setFailTime(date);
            }
            commonDao.update(userInfo);
            marked = errorCount;
        }
        return marked;
    }

    /**
     * 判断是否已过锁定时间
     * 
     * @param user
     *            用户对象
     * @return 是否已过锁定时间
     * @throws ParseException
     *             传入时间值不可解析为时间
     */
    public boolean comparisonTime(Userbasicsinfo user) throws ParseException {

        String date = DateUtils.format(null);
        if (!StringUtil.isBlank(user.getFailTime())) {
            // 比较锁定时间与当前时间的大小
            int day = DateUtils.differenceDateSimple(user.getFailTime(), date);
            // 如果当前时间小于锁定时间
            if (day < 1) {
                return false;
            }
        }
        // 如果锁定时间已过
        user.setFailTime("");
        user.setErrorNum(0);
        commonDao.update(user);
        return true;
    }

    /**
     * 添加登录日志
     * 
     * @param user
     *            用户信息
     * @param ip
     *            ip地址
     * 
     */
    public void saveUserLog(Userbasicsinfo user, String ip) {
        String date = DateUtils.format(null);
        Userloginlog loginLog = new Userloginlog();
        loginLog.setLogintime(date);
        loginLog.setUserbasicsinfo(user);
        loginLog.setIp(ip);
        loginLog.setAddress(localService.getRequesterAddressByIP(ip));
        // 保存登录日志
        commonDao.save(loginLog);
    }
    
    /**
    * <p>Title: queryUserLog</p>
    * <p>Description: 获取会员上次登录地址</p>
    * @param user 当前登录的会员
    * @param request HttpServletRequest
    */
    public void queryUserLog(Userbasicsinfo user,HttpServletRequest request){
        
        String sql="SELECT id,logintime,ip,address,user_id FROM userloginlog WHERE user_id="+user.getId()+" ORDER BY id DESC LIMIT 1";
        
        List<Userloginlog> loginLog=commonDao.findBySql(sql, Userloginlog.class);
        
        if(null!=loginLog&&!loginLog.isEmpty()){
            request.getSession().setAttribute("loginLog", loginLog.get(0));
        }
    }
    
    /**
     * 判断身份证是否唯一
     * 
     * @param card
     *            身份证号码
     * @return 是否唯一
     */
    public boolean checkCardId(String card) {
        boolean bool = true;
        StringBuffer sql = new StringBuffer(
                "SELECT COUNT(*) FROM userrelationinfo a WHERE a.cardId=?");
        // 配备该条数
        Object count = commonDao.findObjectBySql(sql.toString(), card);
        if (Integer.parseInt(count.toString()) > 0) {
            bool = false;
        }
        return bool;
    }
    
    /**
     * 判断电话是否唯一
     * 
     * @param phone
     *            电话号码
     * @return 是否唯一
     */
    public boolean checkPhone(String phone) {
        boolean bool = true;
        StringBuffer sql = new StringBuffer(
                "SELECT COUNT(*) FROM userrelationinfo a WHERE a.phone=?");
        // 配备该条数
        Object count = commonDao.findObjectBySql(sql.toString(), phone);
        if (Integer.parseInt(count.toString()) > 0) {
            bool = false;
        }
        return bool;
    }
    
    /**
     * 验证激活链接
     * @param id 用户id
     * @param request
     * @return 0表示激活失败 1表示已经激活了邮箱 2表示激活链接时间失效 3表示激活成功
     */
    public Integer activateAccount(Long id,HttpServletRequest request){
    	//取得user并判断是否存在该user
    	Userbasicsinfo user=commonDao.get(Userbasicsinfo.class, id);
    	if(user==null){
    		return 0;
    	} else {
    		//取得邮箱验证码信息，判断是否存在
    		Validcodeinfo validCode=(Validcodeinfo) commonDao.findObject(
    				"FROM Validcodeinfo v WHERE v.userbasicsinfo.id=?", id);
    		if(validCode==null){
    			return 0;
    		} else {
				//判断是否已经激活过邮箱
    			if(user.getUserrelationinfo().getEmailisPass().intValue()==1){
    				return 1;
    			}
    			//取得系统当前时间的毫秒数 与链接失效时间比较
    			Long time=System.currentTimeMillis();
    			if(time>validCode.getEmailovertime()){
    				return 2;
    			} else {
					if(GenerateLinkUtils.verifyCheckcode(user, request)){
						//激活验证通过则将邮箱是否激活的状态改为1通过
						user.getUserrelationinfo().setEmailisPass(1);
						//将邮箱验证的再次发送时间和过期时间设置为null
						validCode.setEmailagaintime(null);
						validCode.setEmailovertime(null);
						commonDao.update(validCode);
						commonDao.update(user);
						user=commonDao.get(Userbasicsinfo.class, user.getId());
						 request.getSession().setAttribute(Constant.SESSION_USER, user);//重置session
						return 3;
					}
				}
			}
    	}
    	return 0;
    }
}
