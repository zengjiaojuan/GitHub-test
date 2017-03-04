package com.cddgg.p2p.huitou.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * Creditor entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "creditor")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "creditorLinks",
        "creditorReleaseMoneyRecords", "creditorPayRecords" })
public class Creditor implements java.io.Serializable {

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
     * 债权详细信息
     */
    private CreditorInfo creditorInfo;

    /**
     * 债权编号
     */
    private String serial;

    /**
     * 债权金额
     */
    private Double money;

    /**
     * 最小债权单位
     */
    private Double moneyMin;

    /**
     * 债权开始日期
     */
    private String timeStart;

    /**
     * 债权结束日期
     */
    private String timeEnd;

    /**
     * 债权期限
     */
    private Integer timeDuring;

    /**
     * 状态[0:未发布，1:进行中，2:已完成，3:已过期]
     */
    private Integer status;

    /**
     * 放款截止时间
     */
    private String lendEndTime;

    /**
     * 合计放款金额
     */
    private Double lendSumMoney;

    /**
     * 债权关联表
     */
    private Set<CreditorLink> creditorLinks = new HashSet<CreditorLink>(0);

    /**
     * 债权放款记录
     */
    private Set<CreditorReleaseMoneyRecord> creditorReleaseMoneyRecords = new HashSet<CreditorReleaseMoneyRecord>(
            0);

    /**
     * 债权还款记录
     */
    private Set<CreditorPayRecord> creditorPayRecords = new HashSet<CreditorPayRecord>(
            0);

    // Constructors

    /** default constructor */
    public Creditor() {
    }

    /**
     * id
     * 
     * @param id
     *            id
     */
    public Creditor(Long id) {
        super();
        this.id = id;
    }

    /**
     * minimal constructor
     * @param userbasicsinfo    userbasicsinfo
     * @param serial    serial
     * @param money money
     * @param moneyMin  moneyMin
     * @param timeStart timeStart
     * @param timeEnd   timeEnd
     * @param timeDuring    timeDuring
     * @param status    status
     */
    public Creditor(Userbasicsinfo userbasicsinfo, String serial, Double money,
            Double moneyMin, String timeStart, String timeEnd,
            Integer timeDuring, Integer status) {
        this.userbasicsinfo = userbasicsinfo;
        this.serial = serial;
        this.money = money;
        this.moneyMin = moneyMin;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.timeDuring = timeDuring;
        this.status = status;
    }

    /**
     * Property accessors
     * @return id
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
     * serial
     * @return  serial
     */
    @Column(name = "serial", nullable = false, length = 20)
    public String getSerial() {
        return this.serial;
    }

    /**
     * serial
     * @param serial    serial
     */
    public void setSerial(String serial) {
        this.serial = serial;
    }

    /**
     * money
     * @return  money
     */
    @Column(name = "money", nullable = false, precision = 18, scale = 4)
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

    /**
     * moneyMin
     * @return  moneyMin
     */
    @Column(name = "money_min", nullable = false, precision = 5, scale = 4)
    public Double getMoneyMin() {
        return this.moneyMin;
    }

    /**
     * moneyMin
     * @param moneyMin  moneyMin
     */
    public void setMoneyMin(Double moneyMin) {
        this.moneyMin = moneyMin;
    }

    /**
     * timeStart
     * @return  timeStart
     */
    @Column(name = "time_start", nullable = false, length = 25)
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
    @Column(name = "time_end", nullable = false, length = 25)
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
     * timeDuring
     * @return  timeDuring
     */
    @Column(name = "time_during", nullable = false)
    public Integer getTimeDuring() {
        return this.timeDuring;
    }

    /**
     * timeDuring
     * @param timeDuring    timeDuring
     */
    public void setTimeDuring(Integer timeDuring) {
        this.timeDuring = timeDuring;
    }

    /**
     * status
     * @return  status
     */
    @Column(name = "status", nullable = false)
    public Integer getStatus() {
        return this.status;
    }

    /**
     * status
     * @param status    status
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * lendEndTime
     * @return  lendEndTime
     */
    @Column(name = "lend_end_time", nullable = true)
    public String getLendEndTime() {
        return this.lendEndTime;
    }

    /**
     * lendEndTime
     * @param lendEndTime   lendEndTime
     */
    public void setLendEndTime(String lendEndTime) {
        this.lendEndTime = lendEndTime;
    }

    /**
     * lendSumMoney
     * @return  lendSumMoney
     */
    @Column(name = "lend_sum_money", nullable = true)
    public Double getLendSumMoney() {
        return this.lendSumMoney;
    }

    /**
     * lendSumMoney
     * @param lendSumMoney  lendSumMoney
     */
    public void setLendSumMoney(Double lendSumMoney) {
        this.lendSumMoney = lendSumMoney;
    }

    /**
     * creditorInfo
     * @return  creditorInfo
     */
    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    public CreditorInfo getCreditorInfo() {
        return creditorInfo;
    }

    /**
     * creditorInfo
     * @param creditorInfo  creditorInfo
     */
    public void setCreditorInfo(CreditorInfo creditorInfo) {
        this.creditorInfo = creditorInfo;
    }

    /**
     * creditorLinks
     * @return  creditorLinks
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "creditor")
    public Set<CreditorLink> getCreditorLinks() {
        return this.creditorLinks;
    }

    /**
     * creditorLinks
     * @param creditorLinks creditorLinks
     */
    public void setCreditorLinks(Set<CreditorLink> creditorLinks) {
        this.creditorLinks = creditorLinks;
    }

    /**
     * creditorReleaseMoneyRecords
     * @return  creditorReleaseMoneyRecords
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "creditor")
    public Set<CreditorReleaseMoneyRecord> getCreditorReleaseMoneyRecords() {
        return this.creditorReleaseMoneyRecords;
    }

    /**
     * creditorReleaseMoneyRecords
     * @param creditorReleaseMoneyRecords   creditorReleaseMoneyRecords
     */
    public void setCreditorReleaseMoneyRecords(
            Set<CreditorReleaseMoneyRecord> creditorReleaseMoneyRecords) {
        this.creditorReleaseMoneyRecords = creditorReleaseMoneyRecords;
    }

    /**
     * creditorPayRecords
     * @return  creditorPayRecords
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "creditor")
    public Set<CreditorPayRecord> getCreditorPayRecords() {
        return this.creditorPayRecords;
    }

    /**
     * creditorPayRecords   
     * @param creditorPayRecords    creditorPayRecords
     */
    public void setCreditorPayRecords(Set<CreditorPayRecord> creditorPayRecords) {
        this.creditorPayRecords = creditorPayRecords;
    }

}