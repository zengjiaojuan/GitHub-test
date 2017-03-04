package com.cddgg.base.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 黑名单IP（测试）
 * 
 * @author 刘道冬
 * 
 */
@Entity
@Table(name = "black_ip")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class BlackIP implements Serializable {

    /**
     * 版本号
     */
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Integer id;

    /**
     * ip
     */
    private String ip;

    /**
     * 锁定时间
     */
    private String lockTime;

    /**
     * 恶意次数
     */
    private Integer count = 0;

    /**
     * 备注
     */
    private String remark;

    /**
     * 构造函数
     * 
     * @param ip
     *            ip
     */
    public BlackIP(String ip) {
        super();
        this.ip = ip;
    }

    /**
     * 构造函数
     * 
     * @param id
     *            id
     * @param ip
     *            ip
     * @param lockTime
     *            锁定时间
     * @param count
     *            恶意次数
     * @param remark
     *            备注
     */
    public BlackIP(Integer id, String ip, String lockTime, Integer count,
            String remark) {
        super();
        this.id = id;
        this.ip = ip;
        this.lockTime = lockTime;
        this.count = count;
        this.remark = remark;
    }

    /**
     * 构造函数
     */
    public BlackIP() {
        super();
    }

    /**
     * id
     * 
     * @return id
     */
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public Integer getId() {
        return id;
    }

    /**
     * id
     * 
     * @param id id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * ip
     * 
     * @return ip
     */
    @Column(name = "ip", length = 15)
    public String getIp() {
        return ip;
    }

    /**
     * ip
     * 
     * @param ip    ip
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * lockTime
     * 
     * @return lockTime
     */
    @Column(name = "lockTime", length = 18)
    public String getLockTime() {
        return lockTime;
    }

    /**
     * lockTime
     * 
     * @param lockTime
     *            lockTime
     */
    public void setLockTime(String lockTime) {
        this.lockTime = lockTime;
    }

    /**
     * remark
     * 
     * @return  remark
     */
    @Column(name = "remark")
    public String getRemark() {
        return remark;
    }

    /**
     * remark
     * 
     * @param remark    remark
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * 判断是否相等
     * 
     * @param obj
     *            对象
     * @return boolean
     */
    public boolean equals(Object obj) {
        return ip.equals(obj);
    }

    /**
     * count
     * 
     * @return count
     */
    @Column(name = "count")
    public Integer getCount() {
        return count;
    }

    /**
     * count
     * 
     * @param count
     *            count
     */
    public void setCount(Integer count) {
        this.count = count;
    }

    /**
     * 增加次数
     */
    public void addCount() {
        this.count++;
    }

}
