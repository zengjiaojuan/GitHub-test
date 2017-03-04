package com.cddgg.p2p.huitou.constant.enums;

import com.cddgg.base.annotation.FieldConfig;
/**
 * 启用、禁用状态enum
 * 0显示、1不显示
 * @author Administrator
 *
 */
public enum ENUM_ISLOCK_STATE{
	/**
	 * 已启用
	 */
	@FieldConfig("已启用")
	ENABLE,
	
	/**
	 * 未启用
	 */
	@FieldConfig("已禁用")
	DISABLE,
}
