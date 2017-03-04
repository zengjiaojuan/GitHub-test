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
 * <p>
 * Title:Attachment
 * </p>
 * <p>
 * Description: Attachment附件表
 * </p>
 *         <p>
 *         date 2014年2月14日
 *         </p>
 */
@Entity
@Table(name = "attachment")
public class Attachment implements java.io.Serializable {

    /** 主键 */
    private Long id;
    /** 上传人 */
    private Adminuser adminuser;
    /** 标的编号 */
    private Loansign loansign;
    /** 附件原始名称 */
    private String originalName;
    /** 附件名称 */
    private String attachmentName;
    /** 附件类型 */
    private Integer attachmentType;
    /** 上传时间 */
    private String uploadTime;

    // Constructors

    /** default constructor */
    public Attachment() {
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
     *            上传人
     * @param loansign
     *            标的编号
     * @param originalName
     *            附件原始名称
     * @param attachmentName
     *            附件名称
     * @param attachmentType
     *            附件类型
     * @param uploadTime
     *            上传时间
     */
    public Attachment(Adminuser adminuser, Loansign loansign,
            String originalName, String attachmentName, Integer attachmentType,
            String uploadTime) {
        this.adminuser = adminuser;
        this.loansign = loansign;
        this.originalName = originalName;
        this.attachmentName = attachmentName;
        this.attachmentType = attachmentType;
        this.uploadTime = uploadTime;
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
     * Title: getAdminuser
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @return adminuser
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adminuser_id")
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
     * Title: getLoansign
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @return loansign
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loansign_id")
    public Loansign getLoansign() {
        return this.loansign;
    }

    /**
     * <p>
     * Title: setLoansign
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param loansign
     *            loansign
     */
    public void setLoansign(Loansign loansign) {
        this.loansign = loansign;
    }

    /**
     * <p>
     * Title: getOriginalName
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @return originalName
     */
    @Column(name = "originalName")
    public String getOriginalName() {
        return this.originalName;
    }

    /**
     * <p>
     * Title: setOriginalName
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param originalName
     *            originalName
     */
    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    /**
     * <p>
     * Title: getAttachmentName
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @return attachmentName
     */
    @Column(name = "attachmentName")
    public String getAttachmentName() {
        return this.attachmentName;
    }

    /**
     * <p>
     * Title: setAttachmentName
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param attachmentName
     *            attachmentName
     */
    public void setAttachmentName(String attachmentName) {
        this.attachmentName = attachmentName;
    }

    /**
     * <p>
     * Title: getAttachmentType
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @return attachmentType
     */
    @Column(name = "attachmentType")
    public Integer getAttachmentType() {
        return this.attachmentType;
    }

    /**
     * <p>
     * Title: setAttachmentType
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param attachmentType
     *            attachmentType
     */
    public void setAttachmentType(Integer attachmentType) {
        this.attachmentType = attachmentType;
    }

    /**
     * <p>
     * Title: getUploadTime
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @return uploadTime
     */
    @Column(name = "uploadTime", length = 30)
    public String getUploadTime() {
        return this.uploadTime;
    }

    /**
     * <p>
     * Title: setUploadTime
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param uploadTime
     *            uploadTime
     */
    public void setUploadTime(String uploadTime) {
        this.uploadTime = uploadTime;
    }

}