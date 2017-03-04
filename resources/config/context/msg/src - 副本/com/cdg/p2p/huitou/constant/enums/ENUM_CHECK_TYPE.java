package com.cddgg.p2p.huitou.constant.enums;

import com.cddgg.base.annotation.FieldConfig;


public enum ENUM_CHECK_TYPE{
	
    /**
     * 审核中
     */
	@FieldConfig("审核中")
	CHECKING,
	/**
	 * 已通过
	 */
	@FieldConfig("已通过")
	SUCCESS,
	/**
	 * 未通过
	 */
	@FieldConfig("未通过")
	FAIL,
}
