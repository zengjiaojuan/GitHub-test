package com.cddgg.p2p.huitou.entity;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * CodeMain 代码表
 */
@Entity
@Table(name = "code_main")
public class CodeMain implements java.io.Serializable {

    // Fields

    /**
     * id
     */
    private String id;

    /**
     * 代码编号
     */
    private String codeNo;

    /**
     * 代码名称
     */
    private String codeName;

    /**
     * 排序
     */
    private Integer codeOrder;

    /**
     * 是否可以删除
     */
    private Integer isDelete;

    /**
     * codeValues 值
     */
    private Set<CodeValue> codeValues = new HashSet<CodeValue>(0);

    // Constructors

    /** default constructor */
    public CodeMain() {
    }

    /**
     * full constructor
     * @param codeNo    codeNo
     * @param codeName  codeName
     * @param codeOrder codeOrder
     * @param isDelete  isDelete
     * @param codeValues    codeValues
     */
    public CodeMain(String codeNo, String codeName, Integer codeOrder,
            Integer isDelete, Set<CodeValue> codeValues) {
        this.codeNo = codeNo;
        this.codeName = codeName;
        this.codeOrder = codeOrder;
        this.isDelete = isDelete;
        this.codeValues = codeValues;
    }

    /**
     * id
     * @return  id
     */
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false, length = 32)
    public String getId() {
        return this.id;
    }

    /**
     * id
     * @param id    id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * codeNo
     * @return  codeNo
     */
    @Column(name = "code_no", length = 50)
    public String getCodeNo() {
        return this.codeNo;
    }

    /**
     * codeNo
     * @param codeNo    codeNo
     */
    public void setCodeNo(String codeNo) {
        this.codeNo = codeNo;
    }

    /**
     * codeNo
     * @return  codeNo
     */
    @Column(name = "code_name", length = 50)
    public String getCodeName() {
        return this.codeName;
    }

    /**
     * codeName
     * @param codeName  codeName
     */
    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

    /**
     * codeOrder
     * @return  codeOrder
     */
    @Column(name = "code_order")
    public Integer getCodeOrder() {
        return this.codeOrder;
    }

    /**
     * codeOrder
     * @param codeOrder codeOrder
     */
    public void setCodeOrder(Integer codeOrder) {
        this.codeOrder = codeOrder;
    }

    /**
     * isDelete
     * @return  isDelete
     */
    @Column(name = "isDelete")
    public Integer getIsDelete() {
        return this.isDelete;
    }

    /**
     * isDelete
     * @param isDelete  isDelete
     */
    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    /**
     * codeValues
     * @return  codeValues
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "codeMain")
    public Set<CodeValue> getCodeValues() {
        return this.codeValues;
    }

    /**
     * codeValues
     * @param codeValues    codeValues
     */
    public void setCodeValues(Set<CodeValue> codeValues) {
        this.codeValues = codeValues;
    }

}