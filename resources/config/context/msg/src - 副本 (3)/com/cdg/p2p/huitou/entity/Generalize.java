package com.cddgg.p2p.huitou.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Generalize
 */
@Entity
@Table(name = "generalize")
public class Generalize implements java.io.Serializable {

	/**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * 编号
     */
    private Long id;
    
    /**
     * 推广时间
     */
	private String adddate;
	
	/**
	 * 推广人编号
	 */
	private Long genuid;
	
	/**
	 * 被推广人编号
	 */
	private Long uid;
	
	/**
	 * 被推广人用户名
	 */
	private String uanme;

	/** default constructor */
	public Generalize() {
	}

	/**
	 * 构造方法
	 * @param adddate 推广时间
	 * @param genuid 推广人编号
	 * @param uid 被推广人编号
	 * @param uanme 被推广人用户名
	 */
	public Generalize(String adddate, Long genuid, Long uid, String uanme) {
		this.adddate = adddate;
		this.genuid = genuid;
		this.uid = uid;
		this.uanme = uanme;
	}

	/**
	 * 编号
	 * @return 编号
	 */
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Long getId() {
		return this.id;
	}

	/**
	 * 编号
	 * @param id 编号
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * 推广时间
	 * @return 推广时间
	 */
	@Column(name = "adddate", length = 32)
	public String getAdddate() {
		return this.adddate;
	}

	/**
	 * 推广时间
	 * @param adddate 推广时间
	 */
	public void setAdddate(String adddate) {
		this.adddate = adddate;
	}

	/**
	 * 推广人编号
	 * @return 推广人编号
	 */
	@Column(name = "genuid")
	public Long getGenuid() {
		return this.genuid;
	}

	/**
	 * 推广人编号
	 * @param genuid 推广人编号
	 */
	public void setGenuid(Long genuid) {
		this.genuid = genuid;
	}

	/**
	 * 被推广人编号
	 * @return  被推广人编号
	 */
	@Column(name = "uid")
	public Long getUid() {
		return this.uid;
	}

	/**
	 *  被推广人编号
	 * @param uid  被推广人编号
	 */
	public void setUid(Long uid) {
		this.uid = uid;
	}

	/**
	 * 被推广人用户名
	 * @return 被推广人用户名
	 */
	@Column(name = "uanme", length = 32)
	public String getUanme() {
		return this.uanme;
	}

	/**
	 * 被推广人用户名
	 * @param uanme 被推广人用户名
	 */
	public void setUanme(String uanme) {
		this.uanme = uanme;
	}

}