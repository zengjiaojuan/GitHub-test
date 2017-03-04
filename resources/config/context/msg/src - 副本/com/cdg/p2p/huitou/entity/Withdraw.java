package com.cddgg.p2p.huitou.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Withdraw
 */
@Entity
@Table(name = "withdraw")
public class Withdraw implements java.io.Serializable {

    // Fields
    /**
     * 主键id
     */
    private Long id;
    /**
     * 用户基本信息
     */
    private Userbasicsinfo userbasicsinfo;
    // private Userbank userbank;
    /**
     * 提现金额
     */
    private Double withdrawAmount;
    /**
     * 手续费
     */
    private Double deposit;
    /**
     * applytime
     */
    private String applytime;
    /**
     * 是否提现成功
     */
    private Integer withdrawstate;
    /**
     * 商户提现订单号
     */
    private String strNum;
    /**
     * 备注
     */
    private String remark;
    /**
     * 提现时间
     */
    private String time;
    /**
     * IPS提现订单号
     */
    private String pIpsBillNo;

    // Constructors

    /** default constructor */
    public Withdraw() {
    }

    /**
     * full constructor
     * 
     * @param userbasicsinfo
     *            用户基础信息
     * @param withdrawAmount
     *            提现金额
     * @param deposit
     *            手续费
     * @param applytime
     *            applytime
     * @param withdrawstate
     *            是否提现成功
     * @param strNum
     *            商户提现订单号
     * @param remark
     *            备注
     * @param time
     *            提现时间
     * @param pIpsBillNo
     *            IPS提现订单号
     */
    public Withdraw(Userbasicsinfo userbasicsinfo, Double withdrawAmount,
            Double deposit, String applytime, Integer withdrawstate,
            String strNum, String remark, String time, String pIpsBillNo) {
        this.userbasicsinfo = userbasicsinfo;
        // this.userbank = userbank;
        this.withdrawAmount = withdrawAmount;
        this.deposit = deposit;
        this.applytime = applytime;
        this.withdrawstate = withdrawstate;
        this.strNum = strNum;
        // this.zztime = zztime;
        this.remark = remark;
        this.time = time;
        this.pIpsBillNo = pIpsBillNo;
    }

    // Property accessors
    /**
     * @return Id
     */
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public Long getId() {
        return this.id;
    }

    /**
     * 
     * @param id 主键id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     *
     * @return  Userbasicsinfo
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    public Userbasicsinfo getUserbasicsinfo() {
        return this.userbasicsinfo;
    }

    /**
     * 
     * @param userbasicsinfo 用户基本信息
     */
    public void setUserbasicsinfo(Userbasicsinfo userbasicsinfo) {
        this.userbasicsinfo = userbasicsinfo;
    }

    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "bank_id")
    // public Userbank getUserbank() {
    // return this.userbank;
    // }
    //
    // public void setUserbank(Userbank userbank) {
    // this.userbank = userbank;
    // }

    /**
     * 
     * @return Double
     */
    @Column(name = "withdrawAmount", precision = 18, scale = 4)
    public Double getWithdrawAmount() {
        return this.withdrawAmount;
    }

    /**
     * 
     * @param withdrawAmount 提现金额
     */
    public void setWithdrawAmount(Double withdrawAmount) {
        this.withdrawAmount = withdrawAmount;
    }

    /**
     * 
     * @return Double
     */
    @Column(name = "deposit", precision = 18, scale = 4)
    public Double getDeposit() {
        return this.deposit;
    }

    /**
     * 
     * @param deposit 提现费用
     */
    public void setDeposit(Double deposit) {
        this.deposit = deposit;
    }

    /**
     * 
     * @return String
     */
    @Column(name = "applytime", length = 30)
    public String getApplytime() {
        return this.applytime;
    }

    /**
     * 
     * @param applytime applytime
     */
    public void setApplytime(String applytime) {
        this.applytime = applytime;
    }

    /**
     * 
     * @return Integer
     */
    @Column(name = "withdrawstate")
    public Integer getWithdrawstate() {
        return this.withdrawstate;
    }

    /**
     * 
     * @param withdrawstate 提现状态
     */
    public void setWithdrawstate(Integer withdrawstate) {
        this.withdrawstate = withdrawstate;
    }

    /**
     * 
     * @return String
     */
    @Column(name = "strNum", length = 50)
    public String getStrNum() {
        return this.strNum;
    }

    /**
     * 
     * @param strNum 打款流水号
     */
    public void setStrNum(String strNum) {
        this.strNum = strNum;
    }

    // @Column(name = "zztime", length = 30)
    // public String getZztime() {
    // return this.zztime;
    // }
    //
    // public void setZztime(String zztime) {
    // this.zztime = zztime;
    // }
    /**
     * 
     * @return String
     */
    @Column(name = "remark", length = 200)
    public String getRemark() {
        return this.remark;
    }

    /**
     * 
     * @param remark 备注
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * 
     * @return String
     */
    @Column(name = "time", length = 30)
    public String getTime() {
        return this.time;
    }

    /**
     * 
     * @param time 提现审核时间
     */
    public void setTime(String time) {
        this.time = time;
    }

    /**
     * 
     * @return String
     */
    @Column(name = "pIpsBillNo", length = 30)
    public String getpIpsBillNo() {
        return pIpsBillNo;
    }

    /**
     * 
     * @param pIpsBillNo ips提现编号
     */
    public void setpIpsBillNo(String pIpsBillNo) {
        this.pIpsBillNo = pIpsBillNo;
    }

}