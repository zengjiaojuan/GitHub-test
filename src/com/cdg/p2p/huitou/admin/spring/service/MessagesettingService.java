package com.cddgg.p2p.huitou.admin.spring.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cddgg.base.spring.orm.hibernate.impl.HibernateSupport;
import com.cddgg.commons.date.DateUtils;
import com.cddgg.p2p.huitou.constant.Constant;
import com.cddgg.p2p.huitou.entity.Loansign;
import com.cddgg.p2p.huitou.entity.Messagesetting;
import com.cddgg.p2p.huitou.entity.Usermessage;
import com.cddgg.p2p.huitou.entity.Userrelationinfo;
import com.cddgg.p2p.huitou.spring.service.EmailService;

/**
 * 消息设置Service
 * 
 * @author longyang
 * 
 */
@Service
public class MessagesettingService {

    /** dao */
    @Resource
    private HibernateSupport dao;

    /** emailService email服务层 */
    @Resource
    private EmailService emailService;

    /**
     * <p>
     * Title: sendMessagetting
     * </p>
     * <p>
     * Description: 发送消息
     * </p>
     * 
     * @param userrelationinfo
     *            用户
     * @param typeId
     *            消息类型
     * @param content
     *            消息类容
     * @param title
     *            站内消息标题
     * @return 是否成功
     */
    public boolean sendMessagetting(Userrelationinfo userrelationinfo,
            Long typeId, String content, String title) {
        // SendSMSUtil sendSMSUtil=new SendSMSUtil();
        StringBuffer sb = new StringBuffer(
                "SELECT * from  messagesetting where messagetype_id=")
                .append(typeId).append(" and user_id=")
                .append(userrelationinfo.getUserbasicsinfo().getId());
        List<Messagesetting> list = dao.findBySql(sb.toString(),
                Messagesetting.class, null);
        if (list != null && list.size() > 0) {
            Messagesetting mes = list.get(0);
            // 判断是否需要发送邮件
            if (mes.getEmailIsEnable()) {
                // 发送邮件
                emailService.sendEmail(title, content,
                        userrelationinfo.getEmail());
            }
            if (mes.getSmsIsEnable()) {
                // 发送短信
                // sendSMSUtil.sendSMS(client, ext, content, telNos)
            }
            if (mes.getSysIsEnable()) {
                // 添加站内消息
                Usermessage umes = new Usermessage();
                umes.setContext(content);
                umes.setIsread(0);
                umes.setUserbasicsinfo(userrelationinfo.getUserbasicsinfo());
                umes.setTitle(title);
                umes.setReceivetime(DateUtils
                        .format(Constant.DEFAULT_TIME_FORMAT));
                dao.save(umes);
            }
        }
        return true;
    }

    /**
     * <p>
     * Title: sendBackMessaget
     * </p>
     * <p>
     * Description: 回款时给投资者发送消息
     * </p>
     * 
     * @param loansign
     *            借款标
     * @param content
     *            内容
     * @param title
     *            主题
     * @return 是否成功
     */
    public boolean sendBackMessaget(Loansign loansign, String content,
            String title) {

        StringBuffer sb = new StringBuffer(
                "SELECT * from userrelationinfo where user_id in(SELECT DISTINCT userbasicinfo_id from loanrecord where loanSign_id=")
                .append(loansign.getId()).append(" )");
        List<Userrelationinfo> list = dao.findBySql(sb.toString(),
                Userrelationinfo.class, null);
        if (list != null && list.size() > 0) {
            Userrelationinfo userrelationinfo = list.get(0);
            // 发送邮件
            emailService.sendEmail(title, content, userrelationinfo.getEmail());
            // 发送短信
            // sendSMSUtil.sendSMS(client, ext, content, telNos)
            // 添加站内消息
            Usermessage umes = new Usermessage();
            umes.setContext(content);
            umes.setIsread(0);
            umes.setUserbasicsinfo(userrelationinfo.getUserbasicsinfo());
            umes.setTitle(title);
            umes.setReceivetime(DateUtils.format(Constant.DEFAULT_TIME_FORMAT));
            dao.save(umes);
        }
        return true;
    }

}
