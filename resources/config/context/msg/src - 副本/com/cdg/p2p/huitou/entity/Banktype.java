package com.cddgg.p2p.huitou.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * <p>
 * Title:Banktype
 * </p>
 * <p>
 * Description: Banktype 银行卡类型
 * </p>
 *         <p>
 *         date 2014年2月14日
 *         </p>
 */
@Entity
@Table(name = "banktype")
public class Banktype implements java.io.Serializable {

    // Fields

    /** 主键 */
    private Long id;
    /** 银行名称 */
    private String name;

    /** userbanks */
    private Set<UserBank> userbanks = new HashSet<UserBank>(0);

    // Constructors

    /** default constructor */
    public Banktype() {
    }

    /**
     * <p>
     * Title:
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param name
     *            银行名称
     * @param userbanks
     *            用户银行卡信息
     */
    public Banktype(String name, Set<UserBank> userbanks) {
        this.name = name;
        this.userbanks = userbanks;
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
     * Title: getName
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @return name
     */
    @Column(name = "name", length = 256)
    public String getName() {
        return this.name;
    }

    /**
     * <p>
     * Title: setName
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param name
     *            name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * <p>
     * Title: getUserbanks
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @return userbanks
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "banktype")
    public Set<UserBank> getUserbanks() {
        return this.userbanks;
    }

    /**
     * <p>
     * Title: setUserbanks
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param userbanks
     *            userbanks
     */
    public void setUserbanks(Set<UserBank> userbanks) {
        this.userbanks = userbanks;
    }

}