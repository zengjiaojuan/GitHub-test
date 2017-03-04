package com.cddgg.p2p.huitou.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * CreditorPayRecord entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "creditor_pay_record")
public class CreditorPayRecord implements java.io.Serializable {

    // Fields

    /**
     * id
     */
    private Long id;

    /**
     * 用户基本信息
     */
    private Userbasicsinfo userbasicsinfo;

    /**
     * 产品认购记录
     */
    private ProductPayRecord productPayRecord;

    /**
     * 债权
     */
    private Creditor creditor;

    /**
     * 开始时间
     */
    private String timeStart;

    /**
     * 结束时间
     */
    private String timeEnd;

    /**
     * 期间
     */
    private Integer dayDuring;

    /**
     * 金额 
     */
    private Double money;

    /**
     * 状态 0:未提交,1:已提交
     */
    private Integer status=0;
    
    // Constructors

    /** default constructor */
    public CreditorPayRecord() {
    }

    /**
     * full constructor
     * @param userbasicsinfo    userbasicsinfo
     * @param productPayRecord  productPayRecord
     * @param creditor  creditor
     * @param timeStart timeStart
     * @param timeEnd   timeEnd
     * @param dayDuring dayDuring
     * @param money money
     */
    public CreditorPayRecord(Userbasicsinfo userbasicsinfo,
            ProductPayRecord productPayRecord, Creditor creditor,
            String timeStart, String timeEnd, Integer dayDuring, Double money) {
        this.userbasicsinfo = userbasicsinfo;
        this.productPayRecord = productPayRecord;
        this.creditor = creditor;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.dayDuring = dayDuring;
        this.money = money;
    }

    /**
     * Property accessors
     * @return  id
     */
    @Id
    @GeneratedValue
    @Column(name = "id", unique = true, nullable = false)
    public Long getId() {
        return this.id;
    }

    /**
     * id
     * @param id    id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * userbasicsinfo
     * @return  userbasicsinfo
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userbasic_id", nullable = false)
    public Userbasicsinfo getUserbasicsinfo() {
        return this.userbasicsinfo;
    }

    /**
     * userbasicsinfo
     * @param userbasicsinfo    userbasicsinfo
     */
    public void setUserbasicsinfo(Userbasicsinfo userbasicsinfo) {
        this.userbasicsinfo = userbasicsinfo;
    }

    /**
     * productPayRecord
     * @return  productPayRecord
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_pay_record_id", nullable = false)
    public ProductPayRecord getProductPayRecord() {
        return this.productPayRecord;
    }

    /**
     * status
     * @return  status
     */
    @Column(name = "status")
    public int getStatus() {
        return status;
    }

    /**
     * status
     * @param status    status
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * productPayRecord
     * @param productPayRecord  productPayRecord
     */
    public void setProductPayRecord(ProductPayRecord productPayRecord) {
        this.productPayRecord = productPayRecord;
    }

    /**
     * creditor
     * @return  creditor
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creditor_id", nullable = false)
    public Creditor getCreditor() {
        return this.creditor;
    }

    /**
     * creditor
     * @param creditor  creditor
     */
    public void setCreditor(Creditor creditor) {
        this.creditor = creditor;
    }

    /**
     * timeStart
     * @return  timeStart
     */
    @Column(name = "time_start", nullable = false, length = 20)
    public String getTimeStart() {
        return this.timeStart;
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
    @Column(name = "time_end", nullable = false, length = 20)
    public String getTimeEnd() {
        return this.timeEnd;
    }

    /**
     * timeEnd
     * @param timeEnd   timeEnd
     */
    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    /**
     * day_during
     * @return  day_during
     */
    @Column(name = "day_during", nullable = false)
    public Integer getDayDuring() {
        return this.dayDuring;
    }

    /**
     * dayDuring
     * @param dayDuring dayDuring
     */
    public void setDayDuring(Integer dayDuring) {
        this.dayDuring = dayDuring;
    }

    /**
     * money
     * @return  money
     */
    @Column(name = "money", nullable = false, precision = 20, scale = 4)
    public Double getMoney() {
        return this.money;
    }

    /**
     * money
     * @param money money
     */
    public void setMoney(Double money) {
        this.money = money;
    }

}