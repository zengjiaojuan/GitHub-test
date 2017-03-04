package com.cddgg.p2p.core.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.cddgg.base.spring.orm.hibernate.HibernateSupportTemplate;
import com.cddgg.p2p.core.loanquery.LoanSignQuery;
import com.cddgg.p2p.huitou.entity.Repaymentrecord;
import com.cddgg.p2p.huitou.util.Arith;

/**
 * 还款记录service
 * 
 * @author Administrator
 * 
 */
@Service
public class RepaymentrecordService {
    /** dao */
    @Resource
    private HibernateSupportTemplate dao;

    /** loanSignQuery */
    @Resource
    private LoanSignQuery loanSignQuery;

    /**
     * <p>
     * Title: getrepaymentRecordCount
     * </p>
     * <p>
     * Description: 借款标为id的还款记录的条数
     * </p>
     * 
     * @param loansignId
     *            借款标号
     * @return 结果
     */
    public int getrepaymentRecordCount(int loansignId) {
        StringBuffer sb = new StringBuffer(
                "SELECT COUNT(1) from repaymentrecord where loanSign_id=");
        return loanSignQuery.queryCount(sb.append(loansignId).toString());
    }

    /**
     * 更改还款记录对象
     * 
     * @param repaymentrecord
     *            还款记录对象
     */
    public void update(Repaymentrecord repaymentrecord) {
        dao.update(repaymentrecord);
    }

    /**
     * 通过借款标标号查询到该借款标的还款记录（适用于所有的借款标）
     * 
     * @param loanSignId
     *            借款标id
     * @param start
     *            start
     * @param limit
     *            limit
     * @return 集合
     */
    public List<Repaymentrecord> queryRepaymentrecordList(int start, int limit,
            int loanSignId) {
        List list = new ArrayList();
        StringBuffer sb = new StringBuffer(
                "SELECT id, periods, preRepayDate, (money + preRepayMoney ), CASE WHEN repayState = 2 THEN '按时还款' ");
        sb.append("WHEN repayState = 3 THEN '逾期未还款' WHEN repayState = 4 THEN '逾期已还款' WHEN repayState = 5 THEN '提前还款' ELSE '' END,repayTime, ");
        sb.append(
                " repayState, money + realMoney +IFNULL(overdueInterest,0) FROM repaymentrecord WHERE loanSign_id = ")
                .append(loanSignId);
        sb.append(" LIMIT ").append(start).append(" , ").append(limit);
        list = dao.findBySql(sb.toString());
        return list;
    }

    /**
     * <p>
     * Title: getJSONArrayByList
     * </p>
     * <p>
     * Description: 还款记录 转JSONArray
     * </p>
     * 
     * @param list
     *            集合
     * @return JSONArray
     */
    public JSONArray getJSONArrayByList(List list) {
        JSONObject json = null;
        JSONArray jsonlist = new JSONArray();
        // 给每条数据添加标题
        for (Object obj : list) {
            json = new JSONObject();
            Object[] str = (Object[]) obj;
            json.element("id", str[0]);
            json.element("periods", str[1]);
            json.element("preRepayDate", str[2]);
            json.element("money",
                    Arith.round(new BigDecimal(str[3].toString()), 2) + "元");
            json.element("repayState", str[4]);
            json.element("repayTime", str[5]);
            if ("1".equals(str[6].toString()) || "3".equals(str[6].toString())) {
                json.element("realmoney", "");
            } else {
                json.element("realmoney",
                        Arith.round(new BigDecimal(str[7].toString()), 2) + "元");
            }
            jsonlist.add(json);
        }
        return jsonlist;
    }
}
