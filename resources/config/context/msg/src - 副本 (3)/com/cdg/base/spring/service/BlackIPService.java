package com.cddgg.base.spring.service;

import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cddgg.base.entity.BlackIP;
import com.cddgg.base.filter.BlackIPFilter;
import com.cddgg.base.spring.orm.hibernate.HibernateSupportTemplate;
import com.cddgg.commons.date.DateUtils;
import com.cddgg.commons.log.LOG;

/**
 * 黑名单IP处理服务（测试）
 * 
 * @author 刘道冬
 * 
 */
@Service("blackService")
@SuppressWarnings("unchecked")
public class BlackIPService {

    /**
     * HibernateSupportTemplate
     */
    @Resource
    HibernateSupportTemplate dao;

    /**
     * 初始化
     */
    public void init() {

        LOG.info("[" + DateUtils.formatSimple() + "] 开始初始化黑名单IP");
        BlackIPFilter.MAP_BLACK_IPS.clear();
        for (BlackIP blackIP : (List<BlackIP>) dao.findCache(
                "FROM BlackIP WHERE lockTime>?",
                DateUtils.format(DateUtils.DEFAULT_DATE_FORMAT))) {
            BlackIPFilter.MAP_BLACK_IPS.put(blackIP.getIp(), null);
        }
        LOG.info("现存有效黑名单IP[" + BlackIPFilter.MAP_BLACK_IPS.size() + "]");

    }

    /**
     * 拉黑传入IP
     * 
     * @param ip    ip
     */
    public void autoBlackedIP(String ip){

        BlackIP blackIP = (BlackIP) dao.findObject(
                "FROM BlackIP a WHERE a.ip=?", ip);
        if (blackIP == null){
            blackIP = new BlackIP(ip);
        }
        try {
            blackIP.setLockTime(DateUtils.add(DateUtils.DEFAULT_DATE_FORMAT,
                    DateUtils.format(DateUtils.DEFAULT_DATE_FORMAT),
                    Calendar.DATE, 1));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        blackIP.addCount();

        dao.saveOrUpdate(blackIP);

        LOG.info("IP[" + ip + "]被拉入黑名单！");

    }

}
