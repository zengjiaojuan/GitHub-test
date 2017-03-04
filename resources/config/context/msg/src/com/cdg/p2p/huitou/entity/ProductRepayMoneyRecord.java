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
 * 产品还款记录
 */
@Entity
@Table(name = "product_repay_money_record")
public class ProductRepayMoneyRecord implements java.io.Serializable {

    // Fields

    /**
     * id
     */
    private Long id;
    /**
     * 产品认购记录
     */
    private Creditor creditor;

    /**
     * 操作时间
     */
    private String timeExecute;

    /**
     * 还款内容
     */
    private String repayContent;

    /**
     * 后台操作员
     */
    private Long adminId;

    /**
     * 还款金额
     */
    private Double repayMeney;

    /**
     * 还款时间[本期]
     */
    private String timeRepay;
    
    /**
     * 还款订单号
     */
    private String pMerBillNo;
    
    /**
     * 还款ips订单号
     */
    private String pIpsBillNo;
    // Constructors

    /** default constructor */
    public ProductRepayMoneyRecord() {
    }

    /**
     * minimal constructor
     * @param creditor  creditor
     * @param repayContent  repayContent
     * @param adminId       adminId
     * @param repayMeney    repayMeney
     * @param timeRepay timeRepay
     */
    public ProductRepayMoneyRecord(Creditor creditor,
            String repayContent, Long adminId, Double repayMeney,
            String timeRepay) {
        this.creditor = creditor;
        this.repayContent = repayContent;
        this.adminId = adminId;
        this.repayMeney = repayMeney;
        this.timeRepay = timeRepay;
    }

    /**
     * full constructor
     * @param creditor  creditor
     * @param timeExecute   timeExecute
     * @param repayContent  repayContent
     * @param adminId   adminId
     * @param repayMeney    repayMeney
     * @param timeRepay timeRepay
     */
    public ProductRepayMoneyRecord(Creditor creditor,
            String timeExecute, String repayContent, Long adminId,
            Double repayMeney, String timeRepay) {
        this.creditor = creditor;
        this.timeExecute = timeExecute;
        this.repayContent = repayContent;
        this.adminId = adminId;
        this.repayMeney = repayMeney;
        this.timeRepay = timeRepay;
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
     * @param id id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * creditor
     * @return  creditor
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_pay_record", nullable = false)
    public Creditor getCreditor() {
        return creditor;
    }
    /**
     * creditor
     * @param creditor creditor
     */
    public void setCreditor(Creditor creditor) {
        this.creditor = creditor;
    }

    /**
     * timeExecute
     * @return  timeExecute
     */
    @Column(name = "time_execute", length = 20)
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
     * repayContent
     * @return  repayContent
     */
    @Column(name = "repay_content", nullable = false, length = 20)
    public String getRepayContent() {
        return this.repayContent;
    }

    /**
     * repayContent
     * @param repayContent  repayContent
     */
    public void setRepayContent(String repayContent) {
        this.repayContent = repayContent;
    }

    /**
     * adminId
     * @return  adminId
     */
    @Column(name = "admin_id", nullable = false)
    public Long getAdminId() {
        return this.adminId;
    }

    /**
     * adminId
     * @param adminId   adminId
     */
    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    /**
     * repayMeney
     * @return  repayMeney
     */
    @Column(name = "repay_meney", nullable = false, precision = 20, scale = 4)
    public Double getRepayMeney() {
        return this.repayMeney;
    }

    /**
     * repayMeney
     * @param repayMeney    repayMeney
     */
    public void setRepayMeney(Double repayMeney) {
        this.repayMeney = repayMeney;
    }
    /**
     * timeRepay
     * @return  timeRepay
     */
    @Column(name = "time_repay", nullable = false, length = 20)
    public String getTimeRepay() {
        return this.timeRepay;
    }

    /**
     * timeRepay
     * @param timeRepay timeRepay
     */
    public void setTimeRepay(String timeRepay) {
        this.timeRepay = timeRepay;
    }
    /**
     * @return 还款订单号
     */
    @Column(name="pMerBillNo",length=30)
    public String getpMerBillNo() {
        return pMerBillNo;
    }
    /**
     * @param pMerBillNo 还款订单号
     */
    public void setpMerBillNo(String pMerBillNo) {
        this.pMerBillNo = pMerBillNo;
    }
    /**
     * @return ips还款订单号
     */
    @Column(name = "pIpsBillNo",length=30)
    public String getpIpsBillNo() {
        return pIpsBillNo;
    }
    /**
     * @param pIpsBillNo ips还款订单号
     */
    public void setpIpsBillNo(String pIpsBillNo) {
        this.pIpsBillNo = pIpsBillNo;
    }

    
}