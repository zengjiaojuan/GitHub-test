package com.cddgg.p2p.huitou.spring.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cddgg.base.spring.orm.hibernate.impl.HibernateSupport;
import com.cddgg.commons.date.DateUtils;
import com.cddgg.commons.log.LOG;
import com.cddgg.p2p.huitou.constant.Constant;
import com.cddgg.p2p.huitou.entity.Messagesetting;
import com.cddgg.p2p.huitou.entity.Messagetype;
import com.cddgg.p2p.huitou.entity.Userbasicsinfo;
import com.cddgg.p2p.huitou.entity.Usermessage;
import com.cddgg.p2p.huitou.entity.Validcodeinfo;

/**
 * 用户基本信息修改
 * 
 * @author RanSheng
 * 
 */
@Service
@SuppressWarnings(value = { "unchecked", "rawtypes" })
public class UserBaseInfoService {

    /**
     * 数据库操作通用接口
     */
    @Resource
    private HibernateSupport commonDao;

    /**
     * 发送短信接口
     */
    @Resource
    private SmsService smsService;

    /**
     * 邮件接口
     */
    @Resource
    private EmailService emailService;

    /**
     * 根据用户编号查询用户信息
     * 
     * @param id
     *            用户编号
     * @return 用户基本信息
     */
    public Userbasicsinfo queryUserById(Long id) {
        return commonDao.get(Userbasicsinfo.class, id);
    }

    /**
     * 根据用户名查询用户信息
     * 
     * @param username
     *            用户名
     * @return 用户基本信息
     */
    public List<Userbasicsinfo> queryUserByusername(String username) {
        StringBuffer sb = new StringBuffer(
                "SELECT * from userbasicsinfo where username='").append(
                username).append("'");
        List<Userbasicsinfo> list = commonDao.findBySql(sb.toString(),
                Userbasicsinfo.class);
        return list;
    }

    /**
     * <p>
     * Title: getEmailByUid
     * </p>
     * <p>
     * Description: 通过编号得到用户的邮箱
     * </p>
     * 
     * @param id
     *            编号
     * @return 邮箱
     */
    public String getEmailByUid(long id) {
        StringBuffer sb = new StringBuffer(
                "SELECT email from userrelationinfo where user_id=").append(id);
        Object object = commonDao.findObjectBySql(sb.toString());
        return object != null ? object.toString() : "";
    }

    /**
     * 修改昵称
     * 
     * @param user
     *            用户基本信息
     * @param nickName
     *            昵称
     */
    public void updateNickName(Userbasicsinfo user, String nickName) {
        user.setNickname(nickName);
        commonDao.update(user);
    }

    /**
     * 修改会员基本信息
     * 
     * @param user
     *            会员基本信息
     */
    public void update(Userbasicsinfo user) {
        commonDao.update(user);
    }

    /**
     * 修改登录密码
     * 
     * @param user
     *            用户基本信息
     * @param pwd
     *            登录密码
     */
    public void updatePwd(Userbasicsinfo user, String pwd) {
        // 发送站内消息
        sendMessagetting(user, 3L, "尊敬的" + user.getUserName()
                + "用户您好：您的登录密码已经修改，请核实。如有疑问，请致电p2p网贷平台客服。【p2p网贷平台】", "修改密码");
        // 修改登录密码
        user.setPassword(pwd);
        commonDao.update(user);
    }

    /**
     * 判断手机验证码是否失失效
     * 
     * @param id
     *            用户 id
     * @return 短信对象
     */
    public Validcodeinfo phoneValidcodeinfo(Long id) {
        List<Validcodeinfo> list = commonDao
                .find("from Validcodeinfo validate where validate.userbasicsinfo.id="
                        + id);
        if (list != null && list.size() > 0) {
            Long time = System.currentTimeMillis();
            Validcodeinfo val = list.get(0);
            // 如果验证码时间已经过期
            if (val.getSmsoverTime() == null || time > val.getSmsoverTime()) {
                return null;
            }
            return val;
        }
        return null;
    }

    /**
     * 修改头像
     * 
     * @param user
     *            会员基本信息
     * @param imgUrl
     *            图片地址
     */
    public void updateHead(Userbasicsinfo user, String imgUrl) {
        user.getUserrelationinfo().setImgUrl(imgUrl);
        commonDao.update(user);
    }

    /**
     * 发送站内消息
     * 
     * @param u
     *            用户
     * @param typeId
     *            消息类型
     * @param content
     *            消息类容
     * @param title
     *            站内消息标题
     */
    public void sendMessagetting(Userbasicsinfo u, Long typeId, String content,
            String title) {
        try {
            String sql = "FROM Messagesetting mes WHERE mes.messagetype.id="
                    + typeId + " AND mes.userbasicsinfo.id=" + u.getId();
            List<Messagesetting> list = commonDao.find(sql);
            if (list != null && list.size() > 0) {
                Messagesetting mes = list.get(0);
                // 判断是否需要发送邮件
                if (mes.getEmailIsEnable()) {
                    // 发送邮件
                    emailService.sendEmail(title, content, u
                            .getUserrelationinfo().getEmail());
                }
                if (mes.getSmsIsEnable()) {
                    // 发送短信
                    smsService.sendSMS(content, u.getUserrelationinfo()
                            .getPhone());
                }
                if (mes.getSysIsEnable()) {
                    // 添加站内消息
                    Usermessage umes = new Usermessage();
                    umes.setContext(content);
                    umes.setIsread(0);
                    umes.setUserbasicsinfo(u);
                    umes.setTitle(title);
                    umes.setReceivetime(DateUtils
                            .format(Constant.DEFAULT_TIME_FORMAT));
                    commonDao.save(umes);
                }
            }
        } catch (Throwable e) {
            LOG.error("发送站内消息出错：" + e);
        }

    }

    /**
     * 查询用户消息设置
     * 
     * @param id
     * @return list
     */
    public List queryMessge(Long id) {
        String sql = "select t1.c1,t2.c2,t1.c3,t1.c4,t1.c5,t1.c6,t2.c7 "
                + "from  "
                + "(SELECT "
                + "m.id as c1, "
                + "m.messagetype_id as c2, "
                + "m.user_id as c3, "
                + "m.sysIsEnable as c4, "
                + "m.emailIsEnable as c5, "
                + "m.smsIsEnable as c6, "
                + "'' as c7  "
                + "FROM "
                + "messagesetting m "
                + "where m.user_id= "
                + id
                + ") t1 "
                + "RIGHT JOIN "
                + "(select  "
                + "'' as c1,mt.id as c2,'' as c3,'' as c4,'' as c5,'' as c6,mt.`name` as c7 "
                + " from messagetype mt) t2 on t1.c2=t2.c2 ORDER BY t2.c1";
        List list = commonDao.findBySql(sql);
        return list;
    }

    /**
     * 查询消息设置
     * 
     * @param id
     *            消息类型编号
     * @param userId
     *            用户编号
     * @return 消息类型
     */
    public List<Messagesetting> queryMessageByType(String id, Long userId) {
        String hql = "FROM Messagesetting a where a.messagetype.id='" + id
                + "' and a.userbasicsinfo.id='" + userId + "'";
        List<Messagesetting> li = commonDao.find(hql);
        return li;
    }

    /**
     * 根据消息编号查询消息
     * 
     * @param id
     *            消息编号
     * @return 消息
     */
    public List<Messagetype> queryByMesaageId(String id) {
        String sql = "FROM Messagetype a where a.id='" + id + "'";
        List<Messagetype> lt = commonDao.find(sql);
        return lt;
    }

    /**
     * 修改消息
     * 
     * @param message
     *            消息
     */
    public void updateMessagesetting(Messagesetting message) {
        commonDao.update(message);
    }

    /**
     * 新增消息
     * 
     * @param message
     *            消息
     */
    public void saveMessagesetting(Messagesetting message) {
        commonDao.save(message);
    }
}
