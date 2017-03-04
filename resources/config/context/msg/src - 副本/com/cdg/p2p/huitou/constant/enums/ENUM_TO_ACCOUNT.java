package com.cddgg.p2p.huitou.constant.enums;

import com.cddgg.base.annotation.FieldConfig;

/**
 * 到款情况
 * @author ldd
 *
 */
public enum ENUM_TO_ACCOUNT{
	
    /**
     * 未付款
     */
	@FieldConfig("未付款")
	NONPAYMENT,
	/**
	 * 付款成功
	 */
	@FieldConfig("付款成功")
	SUCCESS,
	/**
	 * 付款失败
	 */
	@FieldConfig("付款失败")
	FAIL;
}
