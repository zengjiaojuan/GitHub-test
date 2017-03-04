package com.cddgg.p2p.huitou.constant.enums;

import com.cddgg.base.annotation.FieldConfig;
/**
 * 借款标类型enum
 * 1普通标、2天标、3秒标、4流转标
 * @author Administrator
 *
 */
public enum ENUM_LOANSIGN_REFUNDWAY{
	/**
	 * 默认值
	 */
	DEFAULT,
	
	/**
	 * 按月等额本息
	 */
	@FieldConfig("按月等额本息")
	REFUNDWAY01,
	
	/**
	 * 按月付息到期还本
	 */
	@FieldConfig("按月付息到期还本")
	REFUNDWAY02,
	
	/**
	 * 到期一次性还本息
	 */
	@FieldConfig("到期一次性还本息")
	REFUNDWAY03,
}
