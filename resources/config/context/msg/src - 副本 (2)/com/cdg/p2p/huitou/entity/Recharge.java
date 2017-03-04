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
 * Recharge
 */
@Entity
@Table(name = "recharge")
public class Recharge implements java.io.Serializable {

    // Fields

    /**
     * 主键id
     */
    private Long id;
    /**
     * 用户基础信息
     */
    private Userbasicsinfo userbasicsinfo;
    /**
     * 充值时间
     */
    private String time;
    /**
     * 充值金额
     */
    private Double rechargeAmount; 
    /**
     *  实际到账金额
     */
    private Double reAccount; 
    /**
     * 是否充值成功(0 未充值 1充值成功 2充值失败)
     */
    private Integer status; 
    // private Integer tpstatus;
    /**
     * 充值流水号
     */
    private String orderNum;
    /**
     * ips充值流水号
     */
    private String pIpsBillNo;

    // Constructors

    /** default constructor */
    public Recharge() {
    }

    /** full constructor */
    /**
     * 
     * @param userbasicsinfo 用户信息表
     * @param time 充值时间
     * @param rechargeAmount 充值金额
     * @param reAccount 到账金额
     * @param status 状态
     * @param pIpsBillNo ips充值编号
     * @param orderNum 订单号
     */
    public Recharge(Userbasicsinfo userbasicsinfo, String time,
            Double rechargeAmount, Double reAccount, Integer status,
            String pIpsBillNo, String orderNum) {
        this.userbasicsinfo = userbasicsinfo;
        this.time = time;
        this.rechargeAmount = rechargeAmount;
        this.reAccount = reAccount;
        this.status = status;
        this.pIpsBillNo = pIpsBillNo;
        this.orderNum = orderNum;
    }

    // Property accessors
    /**
     * 
     * @return Long
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
     * @return Userbasicsinfo
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

    /**
     * 
     * @return String
     */
    @Column(name = "time", length = 32)
    public String getTime() {
        return this.time;
    }

    /**
     * 
     * @param time 充值时间
     */
    public void setTime(String time) {
        this.time = time;
    }

    /**
     * 
     * @return Double
     */
    @Column(name = "rechargeAmount", precision = 18, scale = 4)
    public Double getRechargeAmount() {
        return this.rechargeAmount;
    }

    /**
     * 
     * @param rechargeAmount 充值金额
     */
    public void setRechargeAmount(Double rechargeAmount) {
        this.rechargeAmount = rechargeAmount;
    }

    /**
     * 
     * @return Double
     */
    @Column(name = "reAccount", precision = 18, scale = 4)
    public Double getReAccount() {
        return this.reAccount;
    }

    /**
     * 
     * @param reAccount 到账金额
     */
    public void setReAccount(Double reAccount) {
        this.reAccount = reAccount;
    }

    /**
     * 
     * @return  Integer
     */
    @Column(name = "status")
    public Integer getStatus() {
        return this.status;
    }

    /**
     * 
     * @param status 状态
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 
     * @return String
     */
    @Column(name = "pIpsBillNo")
    public String getpIpsBillNo() {
        return pIpsBillNo;
    }

    /**
     * 
     * @param pIpsBillNo ips充值编号
     */
    public void setpIpsBillNo(String pIpsBillNo) {
        this.pIpsBillNo = pIpsBillNo;
    }

    // @Column(name = "tpstatus")
    // public Integer getTpstatus() {
    // return this.tpstatus;
    // }
    //
    // public void setTpstatus(Integer tpstatus) {
    // this.tpstatus = tpstatus;
    // }

    /**
     * 
     * @return String
     */
    @Column(name = "orderNum", length = 15)
    public String getOrderNum() {
        return this.orderNum;
    }

    /**
     * 
     * @param orderNum 订单号
     */
    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

}