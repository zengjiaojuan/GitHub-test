package com.cddgg.p2p.huitou.entity;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
* <p>Title:MemberNumber</p>
* <p>Description: MemberNumber 会员编码表</p>
* <p>date 2014年2月14日</p>
*/
@Entity
@Table(name = "member_number")
public class MemberNumber implements java.io.Serializable {

    /**
     * 主键
     */
    private Long id;

    /**
     * 所属服务人员
     */
    private Adminuser adminuser;

    /**
     * 会员编码
     */
    private String number;

    /**
     * 是否使用
     */
    private Integer isuse;

    // Constructors

    /** default constructor */
    public MemberNumber() {
    }

    /**
     * <p>
     * Title:
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param adminuser
     *            所属服务人员
     * @param number
     *            会员编码
     * @param isuse
     *            是否使用
     */
    public MemberNumber(Adminuser adminuser, String number, Integer isuse) {
        this.adminuser = adminuser;
        this.number = number;
        this.isuse = isuse;
    }

    // Property accessors
    /**
     * <p>
     * Title: getId
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @return id
     */
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public Long getId() {
        return this.id;
    }

    /**
     * <p>
     * Title: setId
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param id
     *            id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * <p>
     * Title: getAdminuser
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @return adminuser
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adminuser_id", nullable = false)
    public Adminuser getAdminuser() {
        return this.adminuser;
    }

    /**
     * <p>
     * Title: setAdminuser
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param adminuser
     *            adminuser
     */
    public void setAdminuser(Adminuser adminuser) {
        this.adminuser = adminuser;
    }

    /**
     * <p>
     * Title: getNumber
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @return number
     */
    @Column(name = "number", unique = true, length = 8)
    public String getNumber() {
        return number;
    }

    /**
     * <p>
     * Title: setNumber
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param number
     *            number
     */
    public void setNumber(String number) {
        this.number = number;
    }

    /**
     * <p>
     * Title: getIsuse
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @return isuse
     */
    @Column(name = "isuse")
    public Integer getIsuse() {
        return isuse;
    }

    /**
     * <p>
     * Title: setIsuse
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param isuse
     *            isuse
     */
    public void setIsuse(Integer isuse) {
        this.isuse = isuse;
    }

}