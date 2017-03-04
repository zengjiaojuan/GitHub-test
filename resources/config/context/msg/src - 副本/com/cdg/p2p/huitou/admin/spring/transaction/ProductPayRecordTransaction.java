package com.cddgg.p2p.huitou.admin.spring.transaction;

import java.text.ParseException;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cddgg.base.spring.orm.hibernate.impl.HibernateSupport;
import com.cddgg.commons.date.DateUtils;
import com.cddgg.p2p.pay.entity.BidInfo;
import com.cddgg.p2p.huitou.constant.enums.ENUM_MONEY_EXCHANGE_STATE;
import com.cddgg.p2p.huitou.constant.enums.ENUM_PRODUCT_PAY_STATE;
import com.cddgg.p2p.huitou.entity.Product;
import com.cddgg.p2p.huitou.entity.ProductPayRecord;
import com.cddgg.p2p.huitou.entity.Userbasicsinfo;

/**
 * 产品认购记录长事务
 * 
 * @author ldd
 * 
 */
@Service
public class ProductPayRecordTransaction {

    /**
     * 注入HibernateSupport
     */
    @Resource
    HibernateSupport dao;

    /**
     * 添加产品认购记录
     * 
     * @param product
     *            产品
     * @param statePay
     *            购买状态[回购中，待分配，已完成]
     * @param alreadyDateIds
     *            已认购日期
     * @param stateExChange
     *            资金交流方式[线上，线下]
     * @param timeStart
     *            开始时间
     * @param timeEnd
     *            结束时间
     * @param money
     *            购买金额
     * @param user1
     *            投资人
     * @param user2
     *            债权人
     * @param bid
     *            环讯信息
     * @return 产品认购记录
     * @throws ParseException
     *             时间格式化异常
     */
    public ProductPayRecord addProductRecord(Product product,
            ENUM_PRODUCT_PAY_STATE statePay, List<Long> alreadyDateIds,
            ENUM_MONEY_EXCHANGE_STATE stateExChange, String timeStart,
            String timeEnd, Double money, Userbasicsinfo user1,
            Userbasicsinfo user2, BidInfo bid) throws ParseException {

        ProductPayRecord record = new ProductPayRecord();

        record.setUserbasicsinfo(user1);
        record.setUserbasicsinfoCreditor(user2);
        record.setMoney(money);
        record.setProduct(product);

        if (bid != null) {
            record.setBidInfo(bid);
        }

        record.setPayType(stateExChange.ordinal());

        record.setTimeStart(DateUtils.formatSimple());// 购买时间[当前时间]
        record.setTimePayStart(timeStart);// 认购开始时间
        record.setEndTime(timeEnd); // 认购结束时间

        if (statePay == ENUM_PRODUCT_PAY_STATE.WAITING) {// 如果为待分配状态需要追加待分配日期

            StringBuilder sb = new StringBuilder();

            for (Iterator<Long> iterator = alreadyDateIds.iterator(); iterator
                    .hasNext();) {
                sb.append(iterator.next()).append(",");
            }

            record.setAlreadyDate(sb.deleteCharAt(sb.length() - 1).toString());

        }

        record.setStatus(statePay.ordinal());

        // 综合计算
        record.execute();

        dao.save(record);// 添加产品认购记录

        return record;

    }

}
