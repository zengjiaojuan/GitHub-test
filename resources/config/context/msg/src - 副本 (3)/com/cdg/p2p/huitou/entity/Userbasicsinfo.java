package com.cddgg.p2p.huitou.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * 会员基本信息
 */
@Entity
@Table(name = "userbasicsinfo")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer","memberNumber"})
public class Userbasicsinfo implements java.io.Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 登陆用户名
     */
    private String userName;

    /**
     * 真实姓名
     */
    private String name;

    /**
     * 登陆密码
     */
    private String password;

    /**
     * 交易密码
     */
    private String transPassword;

    /**
     * 邮箱激活验证码
     */
    private String randomCode;

    /**
     * 注册时间
     */
    private String createTime;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 是否被锁[1-是，0-否]
     */
    private Integer isLock;

    /**
     * 锁定时间
     */
    private String lockTime;

    /**
     * 上次登陆失败时间
     */
    private String failTime;

    /**
     * 登录错误次数
     */
    private Integer errorNum;

    /**
     * 是否发布有净值标[1-是，0-否]
     */
    private Integer isLoanState;

    /**
     * 环讯开户时间
     */
    private String pIpsAcctDate;

    /**
     * 商户开户流水号
     */
    private String pMerBillNo;

    /**
     * 用户资金信息
     */
    private Userfundinfo userfundinfo;
    
    /**
     * 会员关联信息
     */
    private Userrelationinfo userrelationinfo;

    /**
     * 验证码存放表
     */
    private Validcodeinfo validcodeinfo;

    /**
     * 会员编号
     */
    private MemberNumber memberNumber;

    /** default constructor */
    public Userbasicsinfo() {
    }

    /**
     * 构造方法
     * 
     * @param id
     *            会员编号
     */
    public Userbasicsinfo(Long id) {
        this.id = id;
    }

    /**
     * author:xiongxiaoli
     * 
     * @param id
     *            编号
     * @param userName
     *            用户名
     * @param name
     *            真实姓名
     * @param userrelationinfo
     *            用户关联表
     */
    public Userbasicsinfo(Long id, String userName, String name,
            Userrelationinfo userrelationinfo) {
        this.id = id;
        this.userName = userName;
        this.name = name;
        this.userrelationinfo = userrelationinfo;
    }

    /**
     * 构造方法
     * 
     * @param id
     *            编号
     * @param userName
     *            用户名
     * @param name
     *            真实姓名
     * @param password
     *            密码
     * @param transPassword
     *            交易密码
     * @param randomCode
     *            激活码
     * @param createTime
     *            创建时间
     * @param nickname
     *            昵称
     * @param isLock
     *            是否锁定
     * @param lockTime
     *            锁定时间
     * @param failTime
     *            登录错误时间
     * @param errorNum
     *            错误次数
     * @param isCreditor
     *            是否发布有净值标[1-是，0-否]
     * @param pIpsAcctDate
     *            环迅开户时间
     * @param pMerBillNo
     *            商户开户流水号
     * @param userrelationinfo
     *            验证码存放表
     * @param memberNumber
     *            会员编号
     */
    public Userbasicsinfo(Long id, String userName, String name,
            String password, String transPassword, String randomCode,
            String createTime, String nickname, Integer isLock,
            String lockTime, String failTime, Integer errorNum,
            Integer isLoanState, String pIpsAcctDate, String pMerBillNo,
            Userrelationinfo userrelationinfo, MemberNumber memberNumber) {
        this.id = id;
        this.userName = userName;
        this.name = name;
        this.password = password;
        this.transPassword = transPassword;
        this.randomCode = randomCode;
        this.createTime = createTime;
        this.nickname = nickname;
        this.isLock = isLock;
        this.lockTime = lockTime;
        this.failTime = failTime;
        this.errorNum = errorNum;
        this.isLoanState = isLoanState;
        this.pIpsAcctDate = pIpsAcctDate;
        this.pMerBillNo = pMerBillNo;
        this.userrelationinfo = userrelationinfo;
        this.memberNumber = memberNumber;
    }

    /**
     * 编号
     * 
     * @return 编号
     */
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public Long getId() {
        return this.id;
    }

    /**
     * 编号
     * 
     * @param id
     *            编号
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * userfundinfo
     * @return  userfundinfo
     */
    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    public Userfundinfo getUserfundinfo() {
        return userfundinfo;
    }

    /**
     * userfundinfo
     * @param userfundinfo  userfundinfo
     */
    public void setUserfundinfo(Userfundinfo userfundinfo) {
        this.userfundinfo = userfundinfo;
    }

    /**
     * 用户名
     * 
     * @return 用户名
     */
    @Column(name = "userName", length = 50)
    public String getUserName() {
        return this.userName;
    }

    /**
     * 用户名
     * 
     * @param userName
     *            用户名
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * 真实姓名
     * 
     * @return 真实姓名
     */
    @Column(name = "name", length = 50)
    public String getName() {
        return this.name;
    }

    /**
     * 真实姓名
     * 
     * @param name
     *            真实姓名
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 密码
     * 
     * @return 密码
     */
    @Column(name = "password")
    public String getPassword() {
        return this.password;
    }

    /**
     * 密码
     * 
     * @param password
     *            密码
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 交易密码
     * 
     * @return 交易密码
     */
    @Column(name = "transPassword")
    public String getTransPassword() {
        return this.transPassword;
    }

    /**
     * 交易密码
     * 
     * @param transPassword
     *            交易密码
     */
    public void setTransPassword(String transPassword) {
        this.transPassword = transPassword;
    }

    /**
     * 激活码
     * 
     * @return 激活码
     */
    @Column(name = "randomCode")
    public String getRandomCode() {
        return this.randomCode;
    }

    /**
     * 激活码
     * 
     * @param randomCode
     *            激活码
     */
    public void setRandomCode(String randomCode) {
        this.randomCode = randomCode;
    }

    /**
     * 创建时间
     * 
     * @return 创建时间
     */
    @Column(name = "createTime", length = 80)
    public String getCreateTime() {
        return this.createTime;
    }

    /**
     * 创建时间
     * 
     * @param createTime 创建时间
     */
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    /**
     * 昵称
     * 
     * @return 昵称
     */
    @Column(name = "nickname")
    public String getNickname() {
        return this.nickname;
    }

    /**
     * 昵称
     * 
     * @param nickname
     *            昵称
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * 是否锁定
     * 
     * @return 是否锁定
     */
    @Column(name = "isLock")
    public Integer getIsLock() {
        return this.isLock;
    }

    /**
     * 是否锁定
     * 
     * @param isLock
     *            是否锁定
     */
    public void setIsLock(Integer isLock) {
        this.isLock = isLock;
    }

    /**
     * 锁定时间
     * 
     * @return 锁定时间
     */
    @Column(name = "lockTime", length = 80)
    public String getLockTime() {
        return this.lockTime;
    }

    /**
     * 锁定时间
     * 
     * @param lockTime
     *            锁定时间
     */
    public void setLockTime(String lockTime) {
        this.lockTime = lockTime;
    }

    /**
     * 登陆错误时间
     * 
     * @return 登录错误时间
     */
    @Column(name = "failTime", length = 80)
    public String getFailTime() {
        return this.failTime;
    }

    /**
     * 登录错误时间
     * 
     * @param failTime
     *            登录错误时间
     */
    public void setFailTime(String failTime) {
        this.failTime = failTime;
    }

    /**
     * 错误次数
     * 
     * @return 错误次数
     */
    @Column(name = "errorNum")
    public Integer getErrorNum() {
        return this.errorNum;
    }

    /**
     * 错误次数
     * 
     * @param errorNum
     *            错误次数
     */
    public void setErrorNum(Integer errorNum) {
        this.errorNum = errorNum;
    }

    /**
     * 是否发布有净值标
     * 
     * @return isLoanState
     */
    @Column(name = "isLoanState")
    public Integer getIsLoanState() {
		return isLoanState;
	}
    /**
     * @param isLoanState 是否发布有净值标
     */
	public void setIsLoanState(Integer isLoanState) {
		this.isLoanState = isLoanState;
	}
    

    /**
     * 会员关联表
     * 
     * @return 会员关联表
     */
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    public Userrelationinfo getUserrelationinfo() {
        return userrelationinfo;
    }

   

	/**
     * 会员关联表
     * 
     * @param userrelationinfo
     *            会员关联表
     */
    public void setUserrelationinfo(Userrelationinfo userrelationinfo) {
        this.userrelationinfo = userrelationinfo;
    }

    /**
     * 验证码存放表
     * 
     * @return 验证码存放表
     */
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    public Validcodeinfo getValidcodeinfo() {
        return validcodeinfo;
    }

    /**
     * 验证码存放表
     * 
     * @param validcodeinfo
     *            验证码存放表
     */
    public void setValidcodeinfo(Validcodeinfo validcodeinfo) {
        this.validcodeinfo = validcodeinfo;
    }

    /**
     * 环迅开户时间
     * 
     * @return 环迅开户时间
     */
    @Column(name = "pIpsAcctDate", length = 50)
    public String getpIpsAcctDate() {
        return pIpsAcctDate;
    }

    /**
     * 环迅开户时间
     * 
     * @param pIpsAcctDate
     *            环迅开户时间
     */
    public void setpIpsAcctDate(String pIpsAcctDate) {
        this.pIpsAcctDate = pIpsAcctDate;
    }

    /**
     * 商户开户流水号
     * 
     * @return 商户开户流水号
     */
    @Column(name = "pMerBillNo", length = 50)
    public String getpMerBillNo() {
        return pMerBillNo;
    }

    /**
     * 商户开户流水号
     * 
     * @param pMerBillNo
     *            商户开户流水号
     */
    public void setpMerBillNo(String pMerBillNo) {
        this.pMerBillNo = pMerBillNo;
    }

    /**
     * 会员编码表
     * 
     * @return 会员编码表
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_number_id")
    public MemberNumber getMemberNumber() {
        return memberNumber;
    }

    /**
     * 会员编码表
     * 
     * @param memberNumber
     *            会员编码表
     */
    public void setMemberNumber(MemberNumber memberNumber) {
        this.memberNumber = memberNumber;
    }

}