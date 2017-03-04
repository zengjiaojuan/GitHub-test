package com.cddgg.p2p.huitou.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 客服QQ管理
 * 
 * @author ransheng
 * 
 */
@Entity
@Table(name = "customer")
public class Customer implements java.io.Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    private Long id;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * qq
     */
    private String qqNum;

    /**
     * phone
     */
    private String phone;

    /**
     * 构造方法
     */
    public Customer() {
        super();
    }

    /**
     * 构造方法
     * 
     * @param id
     *            编号
     * @param nickName
     *            昵称
     * @param qqNum
     *            qq
     */
    public Customer(Long id, String nickName, String qqNum, String phone) {
        super();
        this.id = id;
        this.nickName = nickName;
        this.qqNum = qqNum;
        this.phone = phone;
    }

    /**
     * 编号
     * 
     * @return 编号
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public Long getId() {
        return id;
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
     * 昵称
     * 
     * @return 昵称
     */
    @Column(name = "nick_name", length = 20)
    public String getNickName() {
        return nickName;
    }

    /**
     * 昵称
     * 
     * @param nickName
     *            昵称
     */
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    /**
     * qq
     * 
     * @return qq
     */
    @Column(name = "qq_num", length = 20)
    public String getQqNum() {
        return qqNum;
    }

    /**
     * qq
     * 
     * @param qqNum
     *            qq
     */
    public void setQqNum(String qqNum) {
        this.qqNum = qqNum;
    }

    /**
     * phone
     * 
     * @return phone
     */
    @Column(name = "phone", length = 18)
    public String getPhone() {
        return phone;
    }

    /**
     * phone
     * 
     * @param phone
     *            phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

}
