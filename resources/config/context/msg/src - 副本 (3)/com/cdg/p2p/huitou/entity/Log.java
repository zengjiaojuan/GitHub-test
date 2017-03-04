package com.cddgg.p2p.huitou.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Log
 */
@Entity
@Table(name = "log")
public class Log implements java.io.Serializable {

    /** id */
    private Long id;
    /** 登陆ip */
    private String ip;
    /** 登陆时间 */
    private String logTime;
    /** 登陆人 */
    private String loginId;
    /** 备注 */
    private String remark;
    /** 真实姓名 */
    private String userName;

    // Constructors

    /** default constructor */
    public Log() {
    }

    /** full constructor */
    /**
     * @param ip
     *            登陆ip
     * @param logTime
     *            登陆时间
     * @param loginId
     *            登陆人
     * @param remark
     *            备注
     * @param userName
     *            真实姓名
     */
    public Log(String ip, String logTime, String loginId, String remark,
            String userName) {
        this.ip = ip;
        this.logTime = logTime;
        this.loginId = loginId;
        this.remark = remark;
        this.userName = userName;
    }

    // Property accessors
    /**
     * @return id
     */
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public Long getId() {
        return this.id;
    }

    /**
     * @param id
     *            id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return ip
     */
    @Column(name = "ip")
    public String getIp() {
        return this.ip;
    }

    /**
     * @param ip
     *            ip
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * @return logTime
     */
    @Column(name = "logTime")
    public String getLogTime() {
        return this.logTime;
    }

    /**
     * @param logTime
     *            logTime
     */
    public void setLogTime(String logTime) {
        this.logTime = logTime;
    }

    /**
     * @return loginId
     */
    @Column(name = "loginId")
    public String getLoginId() {
        return this.loginId;
    }

    /**
     * @param loginId
     *            loginId
     */
    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    /**
     * @return remark
     */
    @Column(name = "remark")
    public String getRemark() {
        return this.remark;
    }

    /**
     * @param remark
     *            remark
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * @return userName
     */
    @Column(name = "userName")
    public String getUserName() {
        return this.userName;
    }

    /**
     * @param userName
     *            userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

}