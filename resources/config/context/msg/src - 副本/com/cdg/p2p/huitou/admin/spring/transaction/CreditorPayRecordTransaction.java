package com.cddgg.p2p.huitou.admin.spring.transaction;

import java.text.ParseException;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.springframework.stereotype.Service;

import com.cddgg.base.spring.orm.hibernate.impl.HibernateSupport;
import com.cddgg.commons.date.DateUtils;
import com.cddgg.p2p.huitou.entity.Creditor;
import com.cddgg.p2p.huitou.entity.CreditorPayRecord;
import com.cddgg.p2p.huitou.entity.ProductPayRecord;
import com.cddgg.p2p.huitou.entity.Userbasicsinfo;

/**
 * 债权认购长事务
 * 
 * @author ldd
 * 
 */
@Service
public class CreditorPayRecordTransaction {

    /**
     * HibernateSupport
     */
    @Resource
    HibernateSupport dao;

    /**
     * 添加债权认购记录
     * 
     * @param productPayRecordId
     *            产品认购记录
     * @param userbasicsinfo
     *            认购用户
     * @param creditorids
     *            债权们
     * @param moneys
     *            金额们
     * @param timeStarts
     *            开始时间们
     * @param timeEnds
     *            结束时间们
     * @throws ParseException
     *             时间格式转化异常
     */
    public void addRecord(long productPayRecordId,
            Userbasicsinfo userbasicsinfo, Long[] creditorids, Double[] moneys,
            String[] timeStarts, String[] timeEnds) throws ParseException {

        ProductPayRecord productPayRecord = dao.get(ProductPayRecord.class,
                productPayRecordId);

        Session session = dao.getSession();

        for (int i = 0; i < creditorids.length; i++) {

            CreditorPayRecord record = new CreditorPayRecord();

            record.setTimeStart(timeStarts[i]);
            record.setTimeEnd(timeEnds[i]);
            record.setCreditor(new Creditor(creditorids[i]));
            record.setDayDuring(DateUtils.differenceDateSimple(timeStarts[i],
                    timeEnds[i]));
            record.setMoney(moneys[i]);
            record.setProductPayRecord(productPayRecord);
            record.setUserbasicsinfo(userbasicsinfo);

            session.save(record);

//            if (i % 20 == 0) {
//                session.flush();
//                session.clear();
//            }

        }

    }

}
