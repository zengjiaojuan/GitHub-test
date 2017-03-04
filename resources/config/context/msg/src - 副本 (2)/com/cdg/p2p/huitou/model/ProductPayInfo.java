package com.cddgg.p2p.huitou.model;

import java.text.ParseException;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import com.cddgg.base.spring.exception.ResponseException;
import com.cddgg.base.spring.exception.ResponseExceptionFactory;
import com.cddgg.commons.date.DateUtils;
import com.cddgg.p2p.huitou.constant.ErrorNumber;

/**
 * 产品认购信息
 * 
 * @author ldd
 * 
 */
public class ProductPayInfo {

    /**
     * 产品
     */
    private Long id;

    /**
     * 投资人
     */
    private Long userId1;

    /**
     * 债权人id
     */
    private Long userId2;

    /**
     * 购买金额
     */
    private double money;

    /**
     * 认购开始时间
     */
    private String timeStart;

    /**
     * 认购结束时间
     */
    private String timeEnd;

    /**
     * 债权集
     */
    private CreditorModel[] models;

    /**
     * 过期时间
     */
    private String timeOut;

    /**
     * 已匹配的日期
     */
    private List<Long> alreadyDateIds;

    /**
     * 债权集
     */
    private Long[] creditors;

    /**
     * 债权关联集
     */
    private Long[] creditorLinkIds;

    /**
     * 资金集
     */
    private Double[] moneys;

    /**
     * 开始时间集
     */
    private String[] timeStarts;

    /**
     * 结束时间集
     */
    private String[] timeEnds;

    /**
     * 分配
     * 
     * @param factory
     *            异常工厂
     * @throws ParseException
     *             时间格式化异常
     * @throws ResponseException
     *             您的购买的产品匹配的债权已超时，请重新匹配后购买！
     */
    public void allot(ResponseExceptionFactory factory) throws ParseException,
            ResponseException {

        if (DateUtils.isAfter(DateUtils.DEFAULT_TIME_FORMAT, timeOut)) {

            throw factory.bornUrl(ErrorNumber.NO_8, null, null);
        }

        List<Long> creditors = new LinkedList<>();
        List<Long> creditorLinkIds = new LinkedList<>();
        List<Double> moneys = new LinkedList<>();
        List<String> timeStarts = new LinkedList<>();
        List<String> timeEnds = new LinkedList<>();

        for (CreditorModel model : models) {

            creditors.add(model.getCreditor().getId());
            creditorLinkIds.addAll(model.getCreditorLinkIds());
            moneys.addAll(model.getMoneys());
            timeStarts.add(model.getTimePayStart());
            timeEnds.add(model.getTimePayEnd());

        }

        setCreditors(creditors.toArray(new Long[0]));
        setCreditorLinkIds(creditorLinkIds.toArray(new Long[0]));
        setMoneys(moneys.toArray(new Double[0]));
        setTimeStarts(timeStarts.toArray(new String[0]));
        setTimeEnds(timeEnds.toArray(new String[0]));

    }

    /**
     * id
     * 
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * id
     * 
     * @param id
     *            id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * userId1
     * @return  userId1
     */
    public Long getUserId1() {
        return userId1;
    }

    /**
     * userId1
     * @param userId1   userId1
     */
    public void setUserId1(Long userId1) {
        this.userId1 = userId1;
    }

    /**
     * userId2
     * @return  userId2
     */
    public Long getUserId2() {
        return userId2;
    }

    /**
     * userId2  
     * @param userId2   userId2
     */
    public void setUserId2(Long userId2) {
        this.userId2 = userId2;
    }

    /**
     * money
     * @return  money
     */
    public double getMoney() {
        return money;
    }

    /**
     * money
     * @param money money
     */
    public void setMoney(double money) {
        this.money = money;
    }

    /**
     * timeStart
     * @return  timeStart
     */
    public String getTimeStart() {
        return timeStart;
    }

    /**
     * timeStart
     * @param timeStart timeStart
     */
    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    /**
     * timeEnd
     * @return  timeEnd
     */
    public String getTimeEnd() {
        return timeEnd;
    }

    /**
     * timeEnd
     * @param timeEnd   timeEnd
     */
    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    /**
     * creditors
     * @return  creditors
     */
    public Long[] getCreditors() {
        return creditors;
    }

    /**
     * creditors
     * @param creditors creditors
     */
    public void setCreditors(Long[] creditors) {
        this.creditors = creditors;
    }

    /**
     * creditorLinkIds
     * @return  creditorLinkIds
     */
    public Long[] getCreditorLinkIds() {
        return creditorLinkIds;
    }

    /**
     * creditorLinkIds
     * @param creditorLinkIds   creditorLinkIds
     */
    public void setCreditorLinkIds(Long[] creditorLinkIds) {
        this.creditorLinkIds = creditorLinkIds;
    }

    /**
     * moneys
     * @return  moneys
     */
    public Double[] getMoneys() {
        return moneys;
    }

    /**
     * moneys
     * @param moneys    moneys
     */
    public void setMoneys(Double[] moneys) {
        this.moneys = moneys;
    }

    /**
     * timeStarts
     * @return  timeStarts
     */
    public String[] getTimeStarts() {
        return timeStarts;
    }

    /**
     * timeStarts
     * @param timeStarts    timeStarts
     */
    public void setTimeStarts(String[] timeStarts) {
        this.timeStarts = timeStarts;
    }

    /**
     * timeEnds
     * @return  timeEnds
     */
    public String[] getTimeEnds() {
        return timeEnds;
    }

    /**
     * timeEnds
     * @param timeEnds  timeEnds
     */
    public void setTimeEnds(String[] timeEnds) {
        this.timeEnds = timeEnds;
    }

    /**
     * models
     * @return  models
     */
    public CreditorModel[] getModels() {
        return models;
    }

    /**
     * models
     * @param models    models
     */
    public void setModels(CreditorModel[] models) {
        this.models = models;
    }

    /**
     * alreadyDateIds
     * @return  alreadyDateIds
     */
    public List<Long> getAlreadyDateIds() {
        return alreadyDateIds;
    }

    /**
     * alreadyDateIds
     * @param alreadyDateIds    alreadyDateIds
     */
    public void setAlreadyDateIds(List<Long> alreadyDateIds) {
        this.alreadyDateIds = alreadyDateIds;
    }

    /**
     * timeOut
     * @return  timeOut
     */
    public String getTimeOut() {
        return timeOut;
    }

    /**
     * time
     * @param time  time
     * @throws ParseException   时间格式化异常
     */
    public void setTimeOut(int time) throws ParseException {
        this.timeOut = DateUtils.add(DateUtils.DEFAULT_TIME_FORMAT,
                Calendar.MINUTE, time);
    }

}
