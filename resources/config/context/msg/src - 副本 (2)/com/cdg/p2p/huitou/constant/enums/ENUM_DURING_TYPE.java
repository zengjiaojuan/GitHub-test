package com.cddgg.p2p.huitou.constant.enums;

import com.cddgg.base.annotation.FieldConfig;
/**
 * 期限类型
 * @author ldd
 *
 */
public enum ENUM_DURING_TYPE{
	/**
	 * 天
	 */
	@FieldConfig("天")
	DAY,
	
	/**
	 * 月
	 */
	@FieldConfig("月")
	MONTH,
	/**
	 * 年
	 */
	@FieldConfig("年")
	YEAR;
}
