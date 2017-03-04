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
 * 银行信息
 */
@Entity
@Table(name = "userbank")
public class UserBank implements java.io.Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 会员基本信息
     */
    private Userbasicsinfo userbasicsinfo;

    /**
     * 银行卡账号
     */
    private String bankAccount;

    /**
     * 开户名称
     */
    private String bankname;

    /**
     * 银行卡编号
     */
    private String bankNumber;

    /**
     * 银行类型
     */
    private Banktype banktype;
    
    /** default constructor */
    public UserBank() {
    }

    /**
     * 构造方法
     * 
     * @param userbasicsinfo
     *            会员基本信息
     * @param bankAccount
     *            银行卡号
     * @param bankname
     *            开户名称
     * @param bankNumber
     *            银行卡编号
     *            @param banktype 银行类型
     */
    public UserBank(Userbasicsinfo userbasicsinfo, String bankAccount,
            String bankname, String bankNumber,Banktype banktype) {
        this.userbasicsinfo = userbasicsinfo;
        this.bankAccount = bankAccount;
        this.bankname = bankname;
        this.bankNumber = bankNumber;
        this.banktype=banktype;
    }

    /**
     * 主键
     * 
     * @return 主键
     */
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public Long getId() {
        return this.id;
    }

    /**
     * 主键
     * 
     * @param id
     *            主键
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 会员基本信息
     * 
     * @return 会员基本信息
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    public Userbasicsinfo getUserbasicsinfo() {
        return this.userbasicsinfo;
    }

    /**
     * 会员基本信息
     * 
     * @param userbasicsinfo
     *            会员基本信息
     */
    public void setUserbasicsinfo(Userbasicsinfo userbasicsinfo) {
        this.userbasicsinfo = userbasicsinfo;
    }

    /**
     * 银行账号
     * 
     * @return 银行账号
     */
    @Column(name = "bankAccount", length = 128)
    public String getBankAccount() {
        return this.bankAccount;
    }

    /**
     * 银行账号
     * 
     * @param bankAccount
     *            银行账号
     */
    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    /**
     * 开户名称
     * 
     * @return 开户名称
     */
    @Column(name = "bankname", length = 120)
    public String getBankname() {
        return this.bankname;
    }

    /**
     * 开户名称
     * 
     * @param bankname
     *            开户名称
     */
    public void setBankname(String bankname) {
        this.bankname = bankname;
    }

    /**
     * 银行卡编号
     * 
     * @return 银行卡编号
     */
    @Column(name = "bankNumber", length = 30)
    public String getBankNumber() {
        return bankNumber;
    }

    /**
     * 银行卡编号
     * 
     * @param bankNumber
     *            银行卡编号
     */
    public void setBankNumber(String bankNumber) {
        this.bankNumber = bankNumber;
    }

    /**
     * 银行卡类型
     * @return 银行卡类型
     */
    @ManyToOne
    @JoinColumn(name="banktype_id")
    public Banktype getBanktype() {
        return banktype;
    }

    /**
     * 银行卡类型
     * @param banktype 银行卡类型
     */
    public void setBanktype(Banktype banktype) {
        this.banktype = banktype;
    }

    
}