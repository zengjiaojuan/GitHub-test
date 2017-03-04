package com.cddgg.p2p.huitou.constant.enums;

import com.cddgg.base.annotation.FieldConfig;
/**
 * 用户付款状态
 * @author Administrator
 *
 */
/**
 * 产品状态
 * @author ldd
 *
 */
public enum ENUM_PRODUCT{
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
