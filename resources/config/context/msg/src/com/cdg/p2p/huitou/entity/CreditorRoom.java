package com.cddgg.p2p.huitou.entity;

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
 * 债券库
 */
@Entity
@Table(name = "creditor_room")
public class CreditorRoom implements java.io.Serializable {

    // Fields

    /**
     * id
     */
    private Long id;

    /**
     * 当前时间
     */
    private String curTime;

    /**
     * 债权link
     */
    private Set<CreditorLink> creditorLinks = new HashSet<CreditorLink>(0);

    // Constructors

    /** default constructor */
    public CreditorRoom() {
    }

    /**
     * minimal constructor
     * 
     * @param curTime
     *            curTime
     */
    public CreditorRoom(String curTime) {
        this.curTime = curTime;
    }

    /**
     * full constructor
     * 
     * @param curTime
     *            curTime
     * @param creditorLinks
     *            creditorLinks
     */
    public CreditorRoom(String curTime, Set<CreditorLink> creditorLinks) {
        this.curTime = curTime;
        this.creditorLinks = creditorLinks;
    }

    /**
     * Property accessors
     * 
     * @return id
     */
    @Id
    @GeneratedValue
    @Column(name = "id", unique = true, nullable = false)
    public Long getId() {
        return this.id;
    }

    /**
     * id
     * 
     * @param id
     *            id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * curTime
     * 
     * @return curTime
     */
    @Column(name = "cur_time", nullable = false, length = 25)
    public String getCurTime() {
        return this.curTime;
    }

    /**
     * curTime
     * 
     * @param curTime
     *            curTime
     */
    public void setCurTime(String curTime) {
        this.curTime = curTime;
    }

    /**
     * creditorLinks
     * 
     * @return creditorLinks
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "creditorRoom")
    public Set<CreditorLink> getCreditorLinks() {
        return this.creditorLinks;
    }

    /**
     * creditorLinks
     * 
     * @param creditorLinks
     *            creditorLinks
     */
    public void setCreditorLinks(Set<CreditorLink> creditorLinks) {
        this.creditorLinks = creditorLinks;
    }

}