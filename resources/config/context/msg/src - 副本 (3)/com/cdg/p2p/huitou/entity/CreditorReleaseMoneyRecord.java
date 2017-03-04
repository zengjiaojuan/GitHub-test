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
 * CreditorReleaseMoneyRecord entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "creditor_release_money_record")
public class CreditorReleaseMoneyRecord implements java.io.Serializable {

    // Fields

    /**
     * id
     */
    private Long id;

    /**
     * 后台用户
     */
    private Adminuser adminuser;

    /**
     * 债权
     */
    private Creditor creditor;

    /**
     * 操作时间
     */
    private String timeExecute;

    /**
     * 结束时间
     */
    private String timeEnd;

    /**
     * 金额
     */
    private Double money;
    
    /**
     * 放款订单号
     */
    private String pMerBillNo;
    
    /**
     * ips放款订单号
     */
    private String pIpsBillNo;

    // Constructors

    /** default constructor */
    public CreditorReleaseMoneyRecord() {
    }

    /**
     * full constructor
     * @param adminuser adminuser
     * @param creditor  creditor
     * @param timeExecute   timeExecute
     * @param timeEnd   timeEnd
     * @param money money
     */
    public CreditorReleaseMoneyRecord(Adminuser adminuser, Creditor creditor,
            String timeExecute, String timeEnd, Double money) {
        this.adminuser = adminuser;
        this.creditor = creditor;
        this.timeExecute = timeExecute;
        this.timeEnd = timeEnd;
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
     * adminuser
     * @return  adminuser
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "admin_id", nullable = false)
    public Adminuser getAdminuser() {
        return this.adminuser;
    }

    /**
     * adminuser
     * @param adminuser adminuser
     */
    public void setAdminuser(Adminuser adminuser) {
        this.adminuser = adminuser;
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
     * timeExecute
     * @return  timeExecute
     */
    @Column(name = "time_execute", nullable = false, length = 20)
    public String getTimeExecute() {
        return this.timeExecute;
    }

    /**
     * timeExecute
     * @param timeExecute   timeExecute
     */
    public void setTimeExecute(String timeExecute) {
        this.timeExecute = timeExecute;
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
    /**
     * @return pMerBillNo 放款订单号
     */
    @Column(name="pMerBillNo",length=30)
    public String getpMerBillNo() {
        return pMerBillNo;
    }
    /**
     * @param pMerBillNo 放款订单号
     */
    public void setpMerBillNo(String pMerBillNo) {
        this.pMerBillNo = pMerBillNo;
    }
    /**
     * @return pIpsBillNo ips放款订单号
     */
    @Column(name="pIpsBillNo",length=30)
    public String getpIpsBillNo() {
        return pIpsBillNo;
    }
    /**
     * @param pIpsBillNo ips放款订单号
     */
    public void setpIpsBillNo(String pIpsBillNo) {
        this.pIpsBillNo = pIpsBillNo;
    }

}