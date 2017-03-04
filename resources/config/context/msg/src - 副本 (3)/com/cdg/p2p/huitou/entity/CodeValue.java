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
 * CodeValue 代码值表
 */
@Entity
@Table(name = "code_value")
public class CodeValue implements java.io.Serializable {

    // Fields

    /**
     * id
     */
    private String id;

    /**
     * 代码表
     */
    private CodeMain codeMain;

    /**
     * 代码值编号
     */
    private String valueNo;

    /**
     * 主表ID
     */
    private String valueMainId;

    /**
     * 名称
     */
    private String valueName;

    /**
     * 排序
     */
    private Integer valueOrder;

    /**
     * 是否可用
     */
    private Integer isDelete;

    // Constructors

    /** default constructor */
    public CodeValue() {
    }

    /**
     * full constructor
     * @param codeMain  codeMain
     * @param valueNo   valueNo
     * @param valueMainId   valueMainId
     * @param valueName valueName
     * @param valueOrder    valueOrder
     * @param isDelete  isDelete
     */
    public CodeValue(CodeMain codeMain, String valueNo, String valueMainId,
            String valueName, Integer valueOrder, Integer isDelete) {
        this.codeMain = codeMain;
        this.valueNo = valueNo;
        this.valueMainId = valueMainId;
        this.valueName = valueName;
        this.valueOrder = valueOrder;
        this.isDelete = isDelete;
    }

    /**
     * Property accessors
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
     * codeMain
     * @return  codeMain
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codeMain_id")
    public CodeMain getCodeMain() {
        return this.codeMain;
    }

    /**
     * codeMain
     * @param codeMain  codeMain
     */
    public void setCodeMain(CodeMain codeMain) {
        this.codeMain = codeMain;
    }

    /**
     * valueNo
     * @return  valueNo
     */
    @Column(name = "value_no", length = 50)
    public String getValueNo() {
        return this.valueNo;
    }

    /**
     * valueNo
     * @param valueNo   valueNo
     */
    public void setValueNo(String valueNo) {
        this.valueNo = valueNo;
    }

    /**
     * valueMainId
     * @return  valueMainId
     */
    @Column(name = "value_main_id", length = 32)
    public String getValueMainId() {
        return this.valueMainId;
    }

    /**
     * valueMainId
     * @param valueMainId   valueMainId
     */
    public void setValueMainId(String valueMainId) {
        this.valueMainId = valueMainId;
    }

    /**
     * valueName
     * @return  valueName
     */
    @Column(name = "value_name", length = 50)
    public String getValueName() {
        return this.valueName;
    }

    /**
     * valueName
     * @param valueName     valueName
     */
    public void setValueName(String valueName) {
        this.valueName = valueName;
    }

    /**
     * value_order
     * @return  value_order
     */
    @Column(name = "value_order")
    public Integer getValueOrder() {
        return this.valueOrder;
    }

    /**
     * valueOrder
     * @param valueOrder    valueOrder
     */
    public void setValueOrder(Integer valueOrder) {
        this.valueOrder = valueOrder;
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

}