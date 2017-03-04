package com.cddgg.p2p.huitou.constant.enums;

import com.cddgg.base.annotation.FieldConfig;
/**
 * 是否显示状态enum
 * 0线上购买、1线下购买
 * @author Administrator
 *
 */
public enum ENUM_PAY_TYPE{
	/**
	 * 线上购买
	 */
	@FieldConfig("线上购买")
	ONLINE,
	
	/**
	 * 线下购买
	 */
	@FieldConfig("线下购买")
	OFFLINE,
}
