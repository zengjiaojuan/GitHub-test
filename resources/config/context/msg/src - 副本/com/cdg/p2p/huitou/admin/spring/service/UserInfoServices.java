package com.cddgg.p2p.huitou.admin.spring.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cddgg.base.model.PageModel;
import com.cddgg.base.spring.orm.hibernate.impl.HibernateSupport;
import com.cddgg.base.util.StringUtil;
import com.cddgg.commons.date.DateUtils;
import com.cddgg.p2p.core.listener.UserList;
import com.cddgg.p2p.huitou.entity.Adminuser;
import com.cddgg.p2p.huitou.entity.Borrowersbase;
import com.cddgg.p2p.huitou.entity.Borrowerscompany;
import com.cddgg.p2p.huitou.entity.Borrowerscontact;
import com.cddgg.p2p.huitou.entity.Borrowersfiles;
import com.cddgg.p2p.huitou.entity.Borrowersfinanes;
import com.cddgg.p2p.huitou.entity.Borrowersothercontact;
import com.cddgg.p2p.huitou.entity.Userbasicsinfo;
import com.cddgg.p2p.huitou.entity.Userfundinfo;
import com.cddgg.p2p.huitou.entity.Usermessage;
import com.cddgg.p2p.huitou.entity.Userrelationinfo;
import com.cddgg.p2p.huitou.entity.Vipinfo;
import com.cddgg.p2p.huitou.spring.service.MemberCenterService;
import com.cddgg.p2p.huitou.spring.service.MyMoneyService;
import com.cddgg.p2p.huitou.util.SQLUtils;

/**
 * <p>
 * Title:UserInfoServices * </p>
 * <p>
 * Description: 前台会员服务层
 * </p>
 */
@Service
@Transactional
public class UserInfoServices {

    /** 引入log4j日志打印类 */
    private static final Logger LOGGER = Logger
            .getLogger(UserInfoServices.class);

    /** 注入数据库操作层 */
    @Resource
    private HibernateSupport dao;
    
    
    /** ul当前登录会员数量、历史最高数量*/
    private UserList ul=UserList.getInstance();

    @Resource
    private MyMoneyService myMoneyService;

    @Resource
    private MemberCenterService memberCenterService;
    
    /**
     * <p>
     * Title: queryUserPage
     * </p>
     * <p>
     * Description: 分页查询会员信息
     * </p>
     * 
     * @param page
     *            分页参数
     * @param userinfo
     *            查询条件
     * @return 查询结果集list
     */
    @SuppressWarnings("rawtypes")
    public List queryUserPage(PageModel page, Userbasicsinfo userinfo) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("queryUserPage(PageModel page=" + page + ", Userbasicsinfo userinfo=" + userinfo + ")方法开始"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        }

        List datalist = new ArrayList();

        // 统计数据条数sql拼接
        StringBuffer countsql = new StringBuffer(
                "SELECT count(1)FROM userbasicsinfo ");
        countsql.append(" LEFT JOIN userfundinfo ON userbasicsinfo.id = userfundinfo.id ");
        countsql.append(" LEFT JOIN userrelationinfo ON userrelationinfo.user_id = userbasicsinfo.id  ");
        countsql.append(" WHERE userbasicsinfo.userName is not null ");

        // 查询数据sql拼接
        StringBuffer sqlbuffer = new StringBuffer(
                "SELECT userbasicsinfo.id, userbasicsinfo.userName, userbasicsinfo.`name`, ");
        sqlbuffer
                .append("( SELECT suminte FROM borrowersbase WHERE userbasicinfo_id = userbasicsinfo.id ),");
        sqlbuffer
                .append(" ROUND(userfundinfo.credit,2), userbasicsinfo.createTime, ( SELECT max(userloginlog.logintime) ");
        sqlbuffer
                .append(" FROM userloginlog WHERE userloginlog.user_id = userbasicsinfo.id),");
        sqlbuffer
                .append(" ( SELECT userloginlog.address FROM userloginlog WHERE userloginlog.user_id = userbasicsinfo.id ORDER BY userloginlog.id DESC LIMIT 0, 1 ), ");
        sqlbuffer
                .append(" ( SELECT count(1) FROM userloginlog WHERE userloginlog.user_id = userbasicsinfo.id ), ");
        sqlbuffer
                .append(" (SELECT  max(endtime) FROM vipinfo WHERE vipinfo.endtime > NOW() AND vipinfo.user_id = userbasicsinfo.id)  AS vipendtime,");
        sqlbuffer
                .append(" ( SELECT count(1) FROM borrowersbase WHERE auditResult = 1 AND userbasicinfo_id = userbasicsinfo.id ),");
        sqlbuffer
                .append("  CASE WHEN userbasicsinfo.isLock = 0 THEN '正常' ELSE '禁用' END, ");
        sqlbuffer
                .append(" (SELECT realname FROM adminuser WHERE id = userrelationinfo.adminuser_id ) ");
        sqlbuffer
                .append(" FROM userbasicsinfo LEFT JOIN userfundinfo ON userbasicsinfo.id = userfundinfo.id ");
        sqlbuffer
                .append(" LEFT JOIN userrelationinfo ON userrelationinfo.user_id = userbasicsinfo.id WHERE userbasicsinfo.userName is not null ");

        if (null != userinfo) {

            if (StringUtil.isNotBlank(userinfo.getUserName())) {
                sqlbuffer.append(" and userbasicsinfo.userName like '%")
                        .append(StringUtil.replaceAll(userinfo.getUserName()))
                        .append("%'");
                countsql.append(" and userbasicsinfo.userName like '%")
                        .append(StringUtil.replaceAll(userinfo.getUserName()))
                        .append("%'");
            }
            // 身份证号模糊查询
            if (StringUtil.isNotBlank(userinfo.getName())) {
                sqlbuffer.append(" AND userrelationinfo.cardId LIKE '%")
                        .append(StringUtil.replaceAll(userinfo.getName()))
                        .append("%'");
                countsql.append(" AND userrelationinfo.cardId LIKE '%")
                        .append(StringUtil.replaceAll(userinfo.getName()))
                        .append("%'");
            }
            // 是否是特权会员，0是普通会员，1是特权会员
            if (null != userinfo.getErrorNum()) {
                if ("0".equals(userinfo.getErrorNum().intValue() + "")) {
                    sqlbuffer
                            .append(" and userbasicsinfo.id NOT IN ( SELECT vipinfo.user_id FROM vipinfo WHERE vipinfo.endtime > NOW())");
                    countsql.append(" and  userbasicsinfo.id NOT IN ( SELECT vipinfo.user_id FROM vipinfo WHERE vipinfo.endtime > NOW())");
                } else {
                    sqlbuffer
                            .append(" and userbasicsinfo.id IN ( SELECT vipinfo.user_id FROM vipinfo WHERE vipinfo.endtime > NOW())");
                    countsql.append(" and userbasicsinfo.id IN ( SELECT vipinfo.user_id FROM vipinfo WHERE vipinfo.endtime > NOW())");
                }
            }
            // 是否是借款人
            if (null != userinfo.getLockTime()
                    && !"".equals(userinfo.getLockTime())) {
                if ("0".equals(userinfo.getLockTime())) {
                    sqlbuffer
                            .append("AND userbasicsinfo.id NOT  IN ( SELECT userbasicinfo_id FROM borrowersbase WHERE auditResult = 1 )");
                    countsql.append("AND userbasicsinfo.id NOT  IN ( SELECT userbasicinfo_id FROM borrowersbase WHERE auditResult = 1 )");
                } else {
                    sqlbuffer
                            .append("AND userbasicsinfo.id IN ( SELECT userbasicinfo_id FROM borrowersbase WHERE auditResult = 1 )");
                    countsql.append("AND userbasicsinfo.id IN ( SELECT userbasicinfo_id FROM borrowersbase WHERE auditResult = 1 )");
                }

            }

            // 注册时间查询
            if (StringUtil.isNotBlank(userinfo.getCreateTime())) {
                sqlbuffer.append(" and userbasicsinfo.createTime >= '")
                        .append(userinfo.getCreateTime()).append(" 00:00:00'");
                countsql.append(" and userbasicsinfo.createTime >= '")
                        .append(userinfo.getCreateTime()).append(" 00:00:00'");
            }

            if (StringUtil.isNotBlank(userinfo.getFailTime())) {
                sqlbuffer.append(" and userbasicsinfo.createTime <= '")
                        .append(userinfo.getFailTime()).append(" 23:59:59'");
                countsql.append(" and userbasicsinfo.createTime <= '")
                        .append(userinfo.getFailTime()).append(" 23:59:59'");
            }

            // 登录次数查询
            if (null != userinfo.getIsLock()) {
                sqlbuffer
                        .append(" and  (SELECT count(0) FROM userloginlog WHERE userloginlog.user_id=userbasicsinfo.id)>"
                                + userinfo.getIsLock().intValue());
                countsql.append(" and  (SELECT count(0) FROM userloginlog WHERE userloginlog.user_id=userbasicsinfo.id)>"
                        + userinfo.getIsLock().intValue());
            }

            // 根据客服
            if (StringUtil.isNotBlank(userinfo.getNickname())) {
                sqlbuffer.append("AND userrelationinfo.adminuser_id = ").append(
                        userinfo.getNickname());
                countsql.append("AND userrelationinfo.adminuser_id = ").append(
                        userinfo.getNickname());
            }

        }
        sqlbuffer.append(" ORDER By userbasicsinfo.id DESC");
        datalist = dao.pageListBySql(page, countsql.toString(),
                sqlbuffer.toString(), null);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("queryUserPage(PageModel, Userbasicsinfo)方法结束OUTPARAM="
                    + datalist);
        }
        return datalist;

    }

    /**
     * <p>
     * Title: queryAll
     * </p>
     * <p>
     * Description: 查询所有会员信息或根据编号查询编号为null则查询全部
     * </p>
     * 
     * @param ids
     *            根据编号查询，多个编号用逗号隔开
     * @return 返回查询结果集list
     */
    @SuppressWarnings("rawtypes")
    public List queryAll(String ids) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("queryAll()方法开始"); //$NON-NLS-1$
        }

        // 查询数据sql拼接
        StringBuffer sqlbuffer = new StringBuffer(
                "SELECT userbasicsinfo.id,userbasicsinfo.userName,");
        sqlbuffer
                .append("userbasicsinfo.`name`,manualintegral.amountPoints,userfundinfo.credit,");
        sqlbuffer.append(" userbasicsinfo.createTime,");
        sqlbuffer
                .append("(SELECT userloginlog.logintime FROM userloginlog WHERE userloginlog.user_id=userbasicsinfo.id ORDER BY userloginlog.id DESC LIMIT 0,1),");
        sqlbuffer
                .append("(SELECT userloginlog.address FROM userloginlog WHERE userloginlog.user_id=userbasicsinfo.id ORDER BY userloginlog.id DESC LIMIT 0,1),");
        sqlbuffer
                .append("(SELECT count(userloginlog.id) FROM userloginlog WHERE userloginlog.user_id=userbasicsinfo.id),");
        sqlbuffer
                .append(" CASE WHEN (SELECT vipinfo.endtime FROM vipinfo WHERE vipinfo.user_id=userbasicsinfo.id ORDER BY id DESC LIMIT 1) > (SELECT NOW()) THEN '特权会员' ELSE '普通会员' END,");
        sqlbuffer
                .append(" CASE WHEN (SELECT vipinfo.endtime FROM vipinfo WHERE vipinfo.user_id=userbasicsinfo.id ORDER BY id DESC LIMIT 1) > (SELECT NOW()) THEN (SELECT vipinfo.endtime FROM vipinfo WHERE vipinfo.user_id=userbasicsinfo.id ORDER BY id DESC LIMIT 1) ELSE '永久' END,");
        sqlbuffer
                .append(" CASE WHEN (SELECT COUNT(borrowersbase.id) FROM borrowersbase WHERE borrowersbase.auditResult=1 AND borrowersbase.userbasicinfo_id=userbasicsinfo.id) > 0 THEN '是' ELSE '否' END,");
        sqlbuffer
                .append(" CASE WHEN userbasicsinfo.isLock = 0 THEN '正常' ELSE '禁用' END,");
        sqlbuffer.append("userbasicsinfo.id,");
        sqlbuffer
                .append(" (SELECT SUM(generalizemoney.bonuses) FROM generalizemoney WHERE genuid = userbasicsinfo.id),");
        sqlbuffer
                .append(" (SELECT userbasicsinfo.userName FROM userbasicsinfo WHERE userbasicsinfo.id = generalize.genuid)");
        sqlbuffer
                .append(" FROM userbasicsinfo LEFT JOIN userfundinfo ON userbasicsinfo.id = userfundinfo.userbasic_id");
        sqlbuffer
                .append(" LEFT JOIN manualintegral ON manualintegral.user_id = userbasicsinfo.id");
        sqlbuffer
                .append(" LEFT JOIN generalize ON generalize.uid = userbasicsinfo.id");
        sqlbuffer
                .append(" LEFT JOIN generalizemoney ON generalizemoney.genuid = userbasicsinfo.id");
        sqlbuffer
                .append(" LEFT JOIN userrelationinfo ON userrelationinfo.user_id=userbasicsinfo.id");

        if (StringUtil.isNotBlank(ids)) {
            sqlbuffer.append(" where  userbasicsinfo.id in (" + ids + ")");
        }

        List returnList = dao.findBySql(sqlbuffer.toString());

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("queryAll()方法结束OUTPARAM=" + returnList); //$NON-NLS-1$
        }
        return returnList;

    }

    /**
     * <p>
     * Title: queryBrrowPage
     * </p>
     * <p>
     * Description: 借款人查询
     * </p>
     * 
     * @param page
     *            分页参数
     * @param userinfo
     *            查询条件
     * @return 返回查询结果集list
     */
    @SuppressWarnings("rawtypes")
    public List queryBrrowPage(PageModel page, Userbasicsinfo userinfo) {
         if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("queryBrrowPage(PageModel page=" + page + ", Userbasicsinfo userinfo=" + userinfo + ")方法开始"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
         }

        // 查询数据sql
        StringBuffer sqlbuffer = new StringBuffer(
                "SELECT borrowersbase.id, userbasicsinfo.userName, IFNULL(borrowersbase.suminte, 0),");
        sqlbuffer
                .append("  borrowersbase.addTime, IFNULL(borrowersbase.credit, 0), userbasicsinfo.createTime, ");
        sqlbuffer
                .append(" ( SELECT max(userloginlog.logintime) FROM userloginlog WHERE userloginlog.user_id = userbasicsinfo.id ),");
        sqlbuffer
                .append(" ( SELECT count(userloginlog.id) FROM userloginlog WHERE userloginlog.user_id = userbasicsinfo.id ),");
        sqlbuffer
                .append(" CASE WHEN ( SELECT vipinfo.endtime FROM vipinfo WHERE vipinfo.user_id = userbasicsinfo.id ORDER BY id DESC LIMIT 1 ) > (SELECT NOW()) THEN '特权会员' ELSE '普通会员' END, ");
        sqlbuffer
                .append(" CASE WHEN ( SELECT vipinfo.endtime FROM vipinfo WHERE vipinfo.user_id = userbasicsinfo.id ORDER BY id DESC LIMIT 1 ) > (SELECT NOW()) THEN ( SELECT vipinfo.endtime FROM vipinfo WHERE vipinfo.user_id = userbasicsinfo.id ORDER BY id DESC LIMIT 1 ) ELSE '永久' END,");
        sqlbuffer
                .append(" CASE WHEN borrowersbase.auditStatus = 2 THEN '1' WHEN borrowersbase.auditStatus = 4 AND borrowersbase.auditResult = 1 THEN '2' WHEN borrowersbase.auditStatus = 4 AND borrowersbase.auditResult = 0 THEN '3' ELSE '' END  ");
        sqlbuffer
                .append(" FROM borrowersbase LEFT JOIN userbasicsinfo ON borrowersbase.userbasicinfo_id = userbasicsinfo.id WHERE borrowersbase.auditStatus > 1 ");

        // 统计数据条数
        StringBuffer countsql = new StringBuffer(
                "SELECT COUNT(1) FROM borrowersbase ");
        countsql.append("INNER JOIN userbasicsinfo ON borrowersbase.userbasicinfo_id=userbasicsinfo.id where borrowersbase.auditStatus>1 ");
        String chaxun = getBorrowQueryLi(userinfo);

        sqlbuffer.append("ORDER BY borrowersbase.id DESC");
        List returnList = dao.pageListBySql(page, countsql.toString() + chaxun,
                sqlbuffer.toString() + chaxun, null);

         if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("queryBrrowPage(PageModel, Userbasicsinfo)方法结束OUTPARAM=" + returnList); //$NON-NLS-1$
         }
        return returnList;
    }
    
    /**
     * <p>
     * Title: getBorrowQueryLi
     * </p>
     * <p>
     * Description:查询条件
     * </p>
     * 
     * @param userinfo
     * @return
     */
    public String getBorrowQueryLi(Userbasicsinfo userinfo) {
        StringBuffer sbsql = new StringBuffer();
        if (StringUtil.isNotBlank(userinfo.getUserName())) {
            sbsql.append(" and userbasicsinfo.userName like '%")
                    .append(StringUtil.replaceAll(userinfo.getUserName()))
                    .append("%'");
        }
        // 身份证号模糊查询
        if (StringUtil.isNotBlank(userinfo.getName())) {
            sbsql.append(
                    "AND userbasicsinfo.id in (SELECT id FROM userrelationinfo where cardId like '%")
                    .append(StringUtil.replaceAll(userinfo.getName()))
                    .append("%')");
        }
        // 注册时间查询
        if (StringUtil.isNotBlank(userinfo.getCreateTime())) {
            sbsql.append(" and userbasicsinfo.createTime >= '")
                    .append(userinfo.getCreateTime()).append(" 00:00:00'");
        }

        if (StringUtil.isNotBlank(userinfo.getFailTime())) {
            sbsql.append(" and userbasicsinfo.createTime <= '")
                    .append(userinfo.getFailTime()).append(" 23:59:59'");
        }
        // 是否通过
        if (null != userinfo.getIsLock()) {
            if (userinfo.getIsLock() == 1) {
                sbsql.append(" and borrowersbase.auditStatus=2");
            }
            if (userinfo.getIsLock() == 2) {
                sbsql.append(" and borrowersbase.auditStatus=4 and borrowersbase.auditStatus=1");
            }
            if (userinfo.getIsLock() == 3) {
                sbsql.append(" and borrowersbase.auditStatus=4 and borrowersbase.auditStatus=0");
            }
        }
        // 积分
        if (null != userinfo.getErrorNum()) {
            sbsql.append(" and borrowersbase.suminte>=").append(
                    userinfo.getErrorNum());
        }
        // 申请时间
        if (StringUtil.isNotBlank(userinfo.getpIpsAcctDate())) {
            sbsql.append(" and borrowersbase.addTime >= '")
                    .append(userinfo.getpIpsAcctDate()).append(" 00:00:00'");
        }
        if (StringUtil.isNotBlank(userinfo.getpMerBillNo())) {
            sbsql.append(" and borrowersbase.addTime <= '")
                    .append(userinfo.getpMerBillNo()).append(" 23:59:59'");
        }
        // 授信额度
        if (null != userinfo.getPassword()
                && !StringUtil.isBlank(userinfo.getPassword())) {
            sbsql.append(" and borrowersbase.credit>=").append(
                    userinfo.getPassword());
        }
        // 登陆次数
        if (null != userinfo.getId()
                && StringUtil.isNotBlank(userinfo.getId().toString())) {
            sbsql.append(
                    " and  (SELECT count(0) FROM userloginlog WHERE userloginlog.user_id=userbasicsinfo.id)>")
                    .append(userinfo.getId());
        }
        return sbsql.toString();
    }

    /**
     * <p>
     * Title: queryBorrowBasinfo
     * </p>
     * <p>
     * Description: 根据编号查询借款人基本信息
     * </p>
     * 
     * @param ids
     *            借款人主键编号
     * @return 返回查询结果集list
     */
    @SuppressWarnings("rawtypes")
    public List queryBorrowBasinfo(String ids) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("queryBorrowBasinfo(String ids=" + ids + ")方法开始"); //$NON-NLS-1$ //$NON-NLS-2$
        }

        StringBuffer sqlbuffer = new StringBuffer(
                "SELECT userbasicsinfo.userName,borrowersbase.suminte,borrowersbase.credit,borrowersbase.realName");
        sqlbuffer
                .append(", CASE WHEN (SELECT vipinfo.endtime FROM vipinfo WHERE vipinfo.user_id=userbasicsinfo.id ORDER BY id DESC LIMIT 1) > (SELECT NOW()) THEN '特权会员' ELSE '普通会员' END, ");
        sqlbuffer.append("userrelationinfo.phone,userbasicsinfo.createTime,");
        sqlbuffer
                .append("CASE WHEN (SELECT vipinfo.endtime FROM vipinfo WHERE vipinfo.user_id=userbasicsinfo.id ORDER BY id DESC LIMIT 1) > (SELECT NOW()) THEN (SELECT vipinfo.endtime FROM vipinfo WHERE vipinfo.user_id=userbasicsinfo.id ORDER BY id DESC LIMIT 1) ELSE '永久' END,");
        sqlbuffer
                .append("userrelationinfo.cardId,userrelationinfo.email FROM borrowersbase ");
        sqlbuffer
                .append(" LEFT JOIN userbasicsinfo ON userbasicsinfo.id=borrowersbase.userbasicinfo_id");
        sqlbuffer
                .append(" LEFT JOIN userrelationinfo ON userbasicsinfo.id=userrelationinfo.user_id");
        sqlbuffer.append(" where borrowersbase.id=" + Long.parseLong(ids));

        List returnList = dao.findBySql(sqlbuffer.toString());

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("queryBorrowBasinfo(String)方法结束OUTPARAM=" + returnList); //$NON-NLS-1$
        }
        return returnList;

    }

    /**
     * <p>
     * Title: queryPersonal
     * </p>
     * <p>
     * Description: 查询借款人个人资料
     * </p>
     * 
     * @param ids
     *            会员主键编号
     * @return 返回查询的结果集list
     */
    @SuppressWarnings("rawtypes")
    public List queryPersonal(String ids) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("queryPersonal(String ids=" + ids + ")方法开始"); //$NON-NLS-1$ //$NON-NLS-2$
        }

        StringBuffer sqlbuffer = new StringBuffer(
                "SELECT borrowersbase.realName,borrowersbase.addTime,borrowersbase.money, ");
        sqlbuffer
                .append("borrowersbase.qualifications,borrowersbase.isCard,borrowersbase.income,borrowersbase.age,borrowersbase.phone, ");
        sqlbuffer
                .append("CASE WHEN borrowersbase.marryStatus =1 THEN '已婚' ELSE '未婚' END,");
        sqlbuffer
                .append("CASE WHEN borrowersbase.sex =1 THEN '男' ELSE '女' END,");
        sqlbuffer
                .append("borrowersbase.remark FROM borrowersbase WHERE borrowersbase.id="
                        + Long.parseLong(ids));

        List returnList = dao.findBySql(sqlbuffer.toString());

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("queryPersonal(String)方法结束OUTPARAM=" + returnList); //$NON-NLS-1$
        }
        return returnList;

    }

    /**
     * <p>
     * Title: queryContact
     * </p>
     * <p>
     * Description: 查询借款人联系方式
     * </p>
     * 
     * @param ids
     *            借款人主键编号
     * @return 返回Borrowerscontact对象
     */
    public Borrowerscontact queryContact(String ids) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("queryContact(String ids=" + ids + ")方法开始"); //$NON-NLS-1$ //$NON-NLS-2$
        }

        String hql = "from Borrowerscontact where borrowersbase.id=" + ids;

        Borrowerscontact returnBorrowerscontact = (Borrowerscontact) dao
                .findObject(hql);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("queryContact(String)方法结束OUTPARAM=" + returnBorrowerscontact); //$NON-NLS-1$
        }
        return returnBorrowerscontact;
    }

    /**
     * <p>
     * Title: queryCompany
     * </p>
     * <p>
     * Description: 查询借款人单位资料
     * </p>
     * 
     * @param ids
     *            借款人主键
     * @return 返回 Borrowerscompany对象
     */
    public Borrowerscompany queryCompany(String ids) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("queryCompany(String ids=" + ids + ")方法开始"); //$NON-NLS-1$ //$NON-NLS-2$
        }

        String hql = "from Borrowerscompany where borrowersbase.id=" + ids;

        Borrowerscompany returnBorrowerscompany = (Borrowerscompany) dao
                .findObject(hql);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("queryCompany(String)方法结束OUTPARAM=" + returnBorrowerscompany); //$NON-NLS-1$
        }
        return returnBorrowerscompany;
    }

    /**
     * <p>
     * Title: queryFinanes
     * </p>
     * <p>
     * Description: 查询借款人财务状况
     * </p>
     * 
     * @param ids
     *            借款人主键
     * @return 返回Borrowersfinanes对象
     */
    public Borrowersfinanes queryFinanes(String ids) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("queryFinanes(String ids=" + ids + ")方法开始"); //$NON-NLS-1$ //$NON-NLS-2$
        }

        String hql = "from Borrowersfinanes where borrowersbase.id=" + ids;

        Borrowersfinanes returnBorrowersfinanes = (Borrowersfinanes) dao
                .findObject(hql);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("queryFinanes(String)方法结束OUTPARAM=" + returnBorrowersfinanes); //$NON-NLS-1$
        }
        return returnBorrowersfinanes;

    }

    /**
     * <p>
     * Title: queryOtherContact
     * </p>
     * <p>
     * Description: 查询借款人联保信息
     * </p>
     * 
     * @param ids
     *            借款人主键
     * @return 返回Borrowersothercontact对象
     */
    public Borrowersothercontact queryOtherContact(String ids) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("queryOtherContact(String ids=" + ids + ")方法开始"); //$NON-NLS-1$ //$NON-NLS-2$
        }

        String hql = "from Borrowersothercontact where borrowersbase.id=" + ids;

        Borrowersothercontact returnBorrowersothercontact = (Borrowersothercontact) dao
                .findObject(hql);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("queryOtherContact(String)方法结束OUTPARAM=" + returnBorrowersothercontact); //$NON-NLS-1$
        }
        return returnBorrowersothercontact;
    }

    /**
     * <p>
     * Title: queryFiles
     * </p>
     * <p>
     * Description: 查询借款人上传的文件
     * </p>
     * 
     * @param ids
     *            借款人主键
     * @param typeid
     *            查询文件类型 1是图片，2是其它文件
     * @return 返回查询结果集list
     */
    @SuppressWarnings("unchecked")
    public List<Borrowersfiles> queryFiles(String ids, String typeid) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("queryFiles(String ids=" + ids + ", String typeid=" + typeid + ")方法开始"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        }

        String hql = "from Borrowersfiles where borrowersbase.id=" + ids
                + " and fileType=" + typeid;

        List<Borrowersfiles> returnList = (List<Borrowersfiles>) dao.query(hql,
                true);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("queryFiles(String, String)方法结束OUTPARAM=" + returnList); //$NON-NLS-1$
        }
        return returnList;

    }

    /**
     * <p>
     * Title: disUser
     * </p>
     * <p>
     * Description: 根据传入的会员编号及状态修改会员状态
     * </p>
     * 
     * @param ids
     *            要修改状态的会员
     * @param status
     *            要修改的状态
     * @return 返回修改的结果，boolean
     */
    public boolean updateUserStatus(String ids, String status) {
        boolean flag = true;
        if (StringUtil.isNotBlank(ids)) {
            // 根据“，”拆分字符串
            String[] newids = ids.split(",");
            // 要修改状态的编号
            String delstr = "";
            for (String idstr : newids) {
                // 将不是空格和非数字的字符拼接
                if (StringUtil.isNotBlank(idstr)
                        && StringUtil.isNumberString(idstr)) {
                    delstr += idstr + ",";
                }
            }
            // 如果确认修改的字符串不为空
            if (delstr.length() > 0) {
                StringBuffer sql = new StringBuffer(
                        "UPDATE userbasicsinfo SET userbasicsinfo.isLock=");
                if ("1".equals(status)) {
                    sql.append(status + ",userbasicsinfo.lockTime='"
                            + DateUtils.format("yyyy-MM-dd HH:mm:ss") + "'");
                } else {
                    sql.append(status + ",userbasicsinfo.lockTime= NULL ");
                }
                sql.append(" WHERE userbasicsinfo.id IN("
                        + delstr.substring(0, delstr.length() - 1) + ")");
                // 批量修改
                if (dao.executeSql(sql.toString()) <= 0) {
                    flag = false;
                }
            }
        }
        return flag;
    }

    /**
     * <p>
     * Title: updateMemberDate
     * </p>
     * <p>
     * Description: 修改会员特权期限
     * </p>
     * 
     * @param endtime
     *            特权结束时间
     * @param ids
     *            要修改会员编号
     * @return 修改结果 ，返回boolean
     */
    public boolean updateMemberDate(String endtime, String ids) {

        boolean flag = false;

        // 判断传入编号是否是纯数字
        if (StringUtil.isNotBlank(ids) && StringUtil.isNumberString(ids)) {
            // 根据传入编号查询会员基本信息
            Userbasicsinfo userbaseinfo = dao.get(Userbasicsinfo.class,
                    Long.parseLong(ids));
            // 判断会员是否存在
            if (null != userbaseinfo) {
                Vipinfo newvip = new Vipinfo();

                newvip.setBegintime(DateUtils.format(null));
                newvip.setEndtime(endtime + " 23:59:59");
                newvip.setUserbasicsinfo(userbaseinfo);
                newvip.setTime(DateUtils.format(null));
                // 保存特权会员信息
                dao.save(newvip);
                flag = true;
            }

        }
        return flag;
    }

    /**
     * <p>
     * Title: queryRelationById
     * </p>
     * <p>
     * Description: 根据会员基本信息编号查询会员联系信息
     * </p>
     * 
     * @param ids
     *            会员基本信息编号
     * @return 返回会员联系信息
     */
    @SuppressWarnings("unchecked")
    public Userrelationinfo queryRelationById(String ids) {

        List<Userrelationinfo> relationList = new ArrayList<Userrelationinfo>();
        // 判断传入ids是否为纯数字
        if (StringUtil.isNotBlank(ids) && StringUtil.isNumberString(ids)) {

            String hql = "from Userrelationinfo where userbasicsinfo.id=" + ids;

            relationList = dao.find(hql);

        }
        // 判断是否查询到数据并返回
        if (null != relationList && !relationList.isEmpty()) {

            return relationList.get(0);
        } else {
            return null;
        }
    }

    /**
     * <p>
     * Title: queryBasicsInfoById
     * </p>
     * <p>
     * Description: 根据编号查询会员基本信息
     * </p>
     * 
     * @param ids
     *            会员编号
     * @return 返回查询会员基本信息
     */
    public Userbasicsinfo queryBasicsInfoById(String ids) {

        Userbasicsinfo basicsInfo = null;

        // 判断传入编号是否是纯数字
        if (StringUtil.isNotBlank(ids) && StringUtil.isNumberString(ids)) {

            basicsInfo = dao.get(Userbasicsinfo.class, Long.parseLong(ids));
        }

        return basicsInfo;
    }

    /**
     * <p>
     * Title: updateRelation
     * </p>
     * <p>
     * Description: 修改会员联系信息
     * </p>
     * 
     * @param ids
     *            会员基本信息编号
     * @param relation
     *            要修改的联系信息
     */
    public void updateRelation(String ids, Userrelationinfo relation) {

        Userrelationinfo loadRelation = this.queryRelationById(ids);

        // 判断会员联系信息是否存在
        if (null != loadRelation) {
            loadRelation.setCardId(relation.getCardId());
            loadRelation.setEmail(relation.getEmail());
            loadRelation.setPhone(relation.getPhone());

            // 修改会员的联系信息
            dao.update(loadRelation);
        }

    }

    /**
     * <p>
     * Title: udapteBasic
     * </p>
     * <p>
     * Description: 修改会员基本信息
     * </p>
     * 
     * @param ids
     *            基本信息的主键
     * @param basicinfo
     *            要修改的基本信息
     */
    public void udapteBasic(String ids, Userbasicsinfo basicinfo) {

        Userbasicsinfo loadBasic = this.queryBasicsInfoById(ids);

        // 判断会员信息是否存在
        if (null != loadBasic) {
            loadBasic.setNickname(basicinfo.getNickname());
            loadBasic.setName(basicinfo.getName());

            dao.update(loadBasic);
        }
    }

    /**
     * <p>
     * Title: updateCredit
     * </p>
     * <p>
     * Description: 修改借款人授信额度
     * </p>
     * 
     * @param ids
     *            借款人主键
     * @param credit
     *            要修改成的授信额度
     * @return 数据库受影响的行数
     */
    public int updateCredit(String ids, String credit) {

        int result = 0;

        // 授信额度是否为空
        if (StringUtil.isNotBlank(credit)) {
            // 判断授信额度中是否有小数点
            if (credit.indexOf(".") != -1) {
                credit = credit.substring(0, credit.indexOf("."));
            }
        }

        // 修改授信额度
        if (StringUtil.isNotBlank(ids)
                && StringUtil.isNumberString(ids + credit)) {
            String sql = "UPDATE borrowersbase SET credit=" + credit
                    + " WHERE auditStatus=4 AND auditResult=1 AND borrowersbase.id=" + ids;

            result = dao.executeSql(sql);
            
            if(result>0){
                Borrowersbase borrowersbase=dao.get(Borrowersbase.class, Long.parseLong(ids));
                
                sql="UPDATE userfundinfo SET credit="+credit+" WHERE id="+borrowersbase.getUserbasicsinfo().getId();
                
                result=dao.executeSql(sql);
            }
        }

        // 返回受影响的行数
        return result;
    }

    /**
     * 
     * <p>
     * Title: addUser
     * </p>
     * <p>
     * Description: 添加会员调用的方法，保存手机号码和身份证号码
     * </p>
     * 
     * @param basic
     *            会员基本信息
     * @param cardId
     *            身份证号码
     * @param phone
     *            手机号码
     * @return 数据库受影响的行数
     */
    public int addUser(Userbasicsinfo basic, String cardId, String phone) {

        String sql = "UPDATE userrelationinfo SET userrelationinfo.cardId="
                + cardId + ",userrelationinfo.phone=" + phone
                + " WHERE userrelationinfo.user_id=" + basic.getId();

        int result = 0;

        result = dao.executeSql(sql);

        return result;

    }

    /**
     * <p>
     * Title: getUserMoney
     * </p>
     * <p>
     * Description: 后台会员资金统计
     * </p>
     * 
     * @param ids
     *            会员编号
     * @param request
     *            HttpServletRequest
     */
    public void getUserMoney(String ids, HttpServletRequest request) {

        Userbasicsinfo user = dao.get(Userbasicsinfo.class, Long.valueOf(ids));
        Userfundinfo userfund = dao.get(Userfundinfo.class, user.getId());
        request.setAttribute("sumMoney", userfund.getCashBalance());
        // 待收本金金额
        Object toBeClosed = myMoneyService.toBeClosed(user.getId());
        request.setAttribute("toBeClosed", toBeClosed);
        // 待付本息金额
        Object colltionPrinInterest = myMoneyService.colltionPrinInterest(user
                .getId());
        request.setAttribute("colltionPrinInterest", colltionPrinInterest);

        // 待确认投标
        Object lentBid = memberCenterService.investmentRecords(user.getId(), 2);
        request.setAttribute("lentBid", lentBid);
        // 待确认提现
        Object withdrawTobe = myMoneyService.withdrawTobe(user.getId());
        request.setAttribute("withdrawTobe", withdrawTobe);
        // 待确认充值
        Object rechargeTobe = myMoneyService.rechargeTobe(user.getId());
        request.setAttribute("rechargeTobe", rechargeTobe);
        // 累计奖励
        Object accumulative = myMoneyService.accumulative(user.getId());
        request.setAttribute("accumulative", accumulative);
        // 平台累计支付
        Object adminAccumulative = myMoneyService.adminAccumulative(user
                .getId());
        request.setAttribute("adminAccumulative", adminAccumulative);
        // 账户资产总额=可用现金金额 + 待确认投标+待确认提现 – 平台累计支付

        // 净赚利息
        Object netInterest = myMoneyService.netInterest(user.getId());
        request.setAttribute("netInterest", netInterest);
        // 累计支付会员费
        Object vipSum = myMoneyService.vipSum(user.getId());
        request.setAttribute("vipSum", vipSum);
        // 累计提现手续费
        Object witharwDeposit = myMoneyService.witharwDeposit(user.getId());
        request.setAttribute("witharwDeposit", witharwDeposit);
        // 累计盈亏总额= 净赚利息 - 累计支付会员费 - 累计提现手续费

        // 累计投资金额
        Object investmentRecords = myMoneyService.investmentRecords(user
                .getId());
        request.setAttribute("investmentRecords", investmentRecords);
        // 累计借入金额
        Object borrowing = myMoneyService.borrowing(user.getId());
        request.setAttribute("borrowing", borrowing);
        // 累计充值金额
        Object rechargeSuccess = myMoneyService.rechargeSuccess(user.getId());
        request.setAttribute("rechargeSuccess", rechargeSuccess);
        // 累计提现金额
        Object withdrawSucess = myMoneyService.withdrawSucess(user.getId());
        request.setAttribute("withdrawSucess", withdrawSucess);
        // 累计支付佣金
        Object commission = myMoneyService.commission(user.getId());
        request.setAttribute("commission", commission);
        // 待收利息总额
        Object interestToBe = myMoneyService.interestToBe(user.getId());
        request.setAttribute("interestToBe", interestToBe);

        // 待付利息总额
        Object colltionInterest = myMoneyService.colltionInterest(user.getId());
        request.setAttribute("colltionInterest", colltionInterest);
    }

    /**
     * <p>
     * Title: updateCustomer
     * </p>
     * <p>
     * Description: 分配客服
     * </p>
     * 
     * @param uid
     *            会员编号
     * @param adminid
     *            客服人员
     */
    public void updateCustomer(String uid, String adminid) {

        Userrelationinfo user=dao.get(Userrelationinfo.class, Long.parseLong(uid));
        Adminuser admin=dao.get(Adminuser.class, Long.parseLong(adminid));
        
        if(null!=user&&null!=admin){
            user.setAdminuser(admin);
            
            dao.update(user);
        }
    }
    
    /**
    * <p>Title: getUsercount</p>
    * <p>Description: 统计会员总数、特权会员数量、普通会员数量</p>
    * @return 会员总数,特权会员数量,普通会员数量、在线会员数量
    */
    public String getUsercount(){
        
        Object allcount=dao.findObjectBySql(SQLUtils.USER_COUNT);
        
        int sum_count=0;
        
        if(null!=allcount){
            sum_count=Integer.parseInt(allcount.toString());
        }
        
        Object vipcount=dao.findObjectBySql(SQLUtils.VIP_COUNT);
        
        int vip_count=0;
        
        if(null!=vipcount){
            vip_count=Integer.parseInt(vipcount.toString());
        }
        
        return sum_count+","+vip_count+","+(sum_count-vip_count)+","+ul.getUserCount();
        
    }
    
    /**
     * <p>Title: queryuserrelistjxByuId</p>
     * <p>Description:竞标中的投资列表 </p>
     * @param page
     * @param uid
     * @return 查询结果集
     */
     public List queryuserrelistjxByuId(PageModel page,String uid,int state){
         
         StringBuffer sqlbuffer=new StringBuffer("SELECT dlr.id, ls.id, lsb.loanNumber, (SELECT name from userbasicsinfo where id=ls.userbasicinfo_id), dlr.tenderTime, dlr.tenderMoney, ");
         sqlbuffer.append(" ROUND(ls.rate*100,2), ls.`month`, ls.useDay, ls.loanType, ls.issueLoan FROM loanrecord dlr INNER JOIN");
         sqlbuffer.append(" loansign ls ON dlr.loanSign_id = ls.id INNER JOIN loansignbasics lsb ON ls.id = lsb.id");
       
         StringBuffer sqlcount=new StringBuffer("SELECT count(0) FROM loanrecord dlr INNER JOIN");
         sqlcount.append(" loansign ls ON dlr.loanSign_id = ls.id INNER JOIN loansignbasics lsb ON ls.id = lsb.id");
         
         if(state==2){
             sqlbuffer.append(" WHERE ( ls.loanstate = 2 OR ( ls.loanType = 4 AND ls.loanstate IN (2, 3) ");
             sqlbuffer.append(" AND lsb.creditTime IS NULL )) AND dlr.userbasicinfo_id =").append(uid);
             
             sqlcount.append(" WHERE ( ls.loanstate = 2 OR ( ls.loanType = 4 AND ls.loanstate IN (2, 3) ");
             sqlcount.append(" AND lsb.creditTime IS NULL )) AND dlr.userbasicinfo_id =").append(uid);
         }else if(state==3){
             sqlbuffer.append(" WHERE ls.loanstate = 3 AND ( ls.loanType < 3 OR ( ls.loanType = 4 AND lsb.creditTime IS NOT NULL )) AND dlr.userbasicinfo_id =").append(uid);
             sqlcount.append(" WHERE ls.loanstate = 3 AND ( ls.loanType < 3 OR ( ls.loanType = 4 AND lsb.creditTime IS NOT NULL )) AND dlr.userbasicinfo_id =").append(uid);
         }else if(state==4){
             sqlbuffer.append(" WHERE ls.loanstate = 4 AND dlr.userbasicinfo_id =").append(uid);
             sqlcount.append(" WHERE ls.loanstate = 4 AND dlr.userbasicinfo_id =").append(uid);
         }else{//查询所有
             sqlbuffer.append(" WHERE dlr.userbasicinfo_id =").append(uid);
             sqlcount.append(" WHERE dlr.userbasicinfo_id =").append(uid);
         }
        return  dao.pageListBySql(page, sqlcount.toString(), sqlbuffer.toString(),null);
     }
     
     /**
      * 资料上传
      * 
      * @param ids
      *            编号
      * @return 资料上传
      */
     @SuppressWarnings("rawtypes")
     public List queryDataUpload(String ids) {
         String sql = "SELECT b.fileName,b.fileRemark,b.addTime,b.fileType,b.id FROM "
                 + "borrowersfiles b WHERE b.fileType!='商业图片' "
                 + "AND b.base_id=" + ids;
         List list = dao.findBySql(sql);
         return list;
     }
     
     /**
      * 借款人商业图片
      * 
      * @param ids
      *            编号
      * @return 借款人商业图片
      */
     @SuppressWarnings("rawtypes")
     public List queryStockPhoto(String ids) {
         String sql = "SELECT b.filePath,b.fileName,b.addTime FROM "
                 + "borrowersfiles b WHERE b.fileType='商业图片' "
                 + "AND b.base_id=" + ids;
         List list = dao.findBySql(sql);
         return list;
     }

     /**
      * <p>Title: querybljxByuserId</p>
      * <p>Description: 借入记录</p>
      * @param page
      * @param uid
      * @return 数据
      */
      public List querybljxByuserId(PageModel page,String uid,int state){
          
          StringBuffer sqlbuffer=new StringBuffer("SELECT ls.id,lsb.loanNumber,ROUND(ls.issueLoan,0),ls.publishTime,ROUND(ls.rate*100,2),lsb.creditTime from loansign ls");
          sqlbuffer.append(" INNER JOIN loansignbasics lsb on lsb.id=ls.id  where ls.loanstate=").append(state).append(" and ls.userbasicinfo_id=").append(uid);

          StringBuffer sqlcount=new StringBuffer("SELECT count(0) from loansign ls");
          sqlcount.append(" INNER JOIN loansignbasics lsb on lsb.id=ls.id  where ls.loanstate=").append(state).append(" and ls.userbasicinfo_id=").append(uid);
          
         return  dao.pageListBySql(page, sqlcount.toString(), sqlbuffer.toString(),null);
      }
      
      /**
       * <p>
       * Title: updateborrowState
       * </p>
       * <p>
       * Description:审核借款人
       * </p>
       * 
       * @param ids
       *            编号
       * @param state
       *            通过和不通过
       * @return 是否成功
       */
      public boolean updateborrowState(String ids, int state) {

          // 如果确认修改的字符串不为空
          if (ids.length() > 0) {
              // 先判断是否全部都能审核
              StringBuffer updatesql = new StringBuffer(
                      "update borrowersbase set auditStatus=4,auditResult=")
                      .append(state).append(" where auditStatus=2 and id in (")
                      .append(ids.substring(0, ids.length() - 1)).append(")");
              // 批量修改
              if (dao.executeSql(updatesql.toString()) <= 0) {
                  return false;
              }

          }
          return true;
      }
      
      /**
    * <p>Title: ispassture</p>
    * <p>Description: 查询会员申请是否已经审核</p>
    * @param ids 会员编号
    * @return 查询结果
    */
    public boolean ispassture(String ids) {
          StringBuffer sqlcount = new StringBuffer(
                  "SELECT count(1) from borrowersbase where auditStatus!=2 and id in (")
                  .append(ids.substring(0, ids.length() - 1)).append(")");
          Object obj = dao.findObjectBySql(sqlcount.toString());
          if (Integer.parseInt(obj.toString()) > 0) {
              return false;
          }
          return true;
      }
    
    /**
     * <p>
     * Title: updatevip
     * </p>
     * <p>
     * Description: 修改会员等级
     * </p>
     * 
     * @param user
     *            会员信息
     * @param endtime
     *            结束时间
     * @return 是否成功
     */
    public void updatevip(Userbasicsinfo user, String endtime) {
        Vipinfo vipinfo = new Vipinfo();
        vipinfo.setBegintime(DateUtils.format("yyyy-MM-dd HH:mm:ss"));
        vipinfo.setEndtime(endtime + DateUtils.format(" HH:mm:ss"));
        vipinfo.setUserbasicsinfo(user);
        Object obj=dao.save(vipinfo);
        
        if(null!=obj){
            Usermessage message=new Usermessage();
            message.setContext("尊敬的会员:"+user.getUserName()+"后台管理员已将你设置为特权会员，特权会员到期时间为:"+vipinfo.getEndtime()+";会员到期时间已最大的时间为准！若"+vipinfo.getEndtime()+"小于以前设置的时间，则以上次到期时间为准！如有疑问，请联系客服！");
            message.setTitle("特权会员升级通知");
            message.setIsread(0);
            message.setReceivetime(DateUtils.format("yyyy-MM-dd HH:mm:ss"));
            message.setUserbasicsinfo(user);
            dao.save(message);
        }
    }
}
