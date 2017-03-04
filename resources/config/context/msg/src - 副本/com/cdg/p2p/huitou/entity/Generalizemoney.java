package com.cddgg.p2p.huitou.entity;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * <p>
 * Title:Generalizemoney
 * </p>
 * <p>
 * Description: 推广奖金信息表
 * </p>
 *         date 2014年2月14日
 *         </p>
 */
@Entity
@Table(name = "generalizemoney")
public class Generalizemoney implements java.io.Serializable {

    // Fields

    /** 主键 */
    private Long id;
    /** 添加时间 */
    private String addtime;
    /** 奖金 */
    private String bonuses;
    /** 被推广人收入 */
    private String umoney;
    /** 被推广人用户名 */
    private String uname;
    /** 推广人编号 */
    private Long genuid;

    /** default constructor */
    public Generalizemoney() {
    }

    /**
     * <p>
     * Title:
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param addtime
     *            添加时间
     * @param bonuses
     *            奖金
     * @param umoney
     *            被推广人收入
     * @param uname
     *            被推广人用户名
     * @param genuid
     *            推广人编号
     */
    public Generalizemoney(String addtime, String bonuses, String umoney,
            String uname, Long genuid) {
        this.addtime = addtime;
        this.bonuses = bonuses;
        this.umoney = umoney;
        this.uname = uname;
        this.genuid = genuid;
    }

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
     * Title: getAddtime
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @return addtime
     */
    @Column(name = "addtime", length = 32)
    public String getAddtime() {
        return this.addtime;
    }

    /**
     * <p>
     * Title: setAddtime
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param addtime
     *            addtime
     */
    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    /**
     * <p>
     * Title: getBonuses
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @return bonuses
     */
    @Column(name = "bonuses", length = 32)
    public String getBonuses() {
        return this.bonuses;
    }

    /**
     * <p>
     * Title: setBonuses
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param bonuses
     *            bonuses
     */
    public void setBonuses(String bonuses) {
        this.bonuses = bonuses;
    }

    /**
     * <p>
     * Title: getUmoney
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @return umoney
     */
    @Column(name = "umoney", length = 32)
    public String getUmoney() {
        return this.umoney;
    }

    /**
     * <p>
     * Title: setUmoney
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param umoney
     *            umoney
     */
    public void setUmoney(String umoney) {
        this.umoney = umoney;
    }

    /**
     * <p>
     * Title: getUname
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @return uname
     */
    @Column(name = "uname", length = 32)
    public String getUname() {
        return this.uname;
    }

    /**
     * <p>
     * Title: setUname
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param uname
     *            uname
     */
    public void setUname(String uname) {
        this.uname = uname;
    }

    /**
     * <p>
     * Title: getGenuid
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @return genuid
     */
    @Column(name = "genuid")
    public Long getGenuid() {
        return this.genuid;
    }

    /**
     * <p>
     * Title: setGenuid
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param genuid
     *            genuid
     */
    public void setGenuid(Long genuid) {
        this.genuid = genuid;
    }

}