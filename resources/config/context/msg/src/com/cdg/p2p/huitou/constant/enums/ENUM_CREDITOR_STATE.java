package com.cddgg.p2p.huitou.constant.enums;

import com.cddgg.base.annotation.FieldConfig;

/**
 * 债权
 * @author My_Ascii
 *
 */
public enum ENUM_CREDITOR_STATE{
	/**
	 * 未发布
	 */
	@FieldConfig("未发布")
	UNPUBLISH,
	
	/**
	 * 进行中
	 */
	@FieldConfig("进行中")
	WORKING,
	
	/**
	 * 已完成
	 */
	@FieldConfig("已完成")
	FINISH,
	
	/**
	 * 已过期
	 */
	@FieldConfig("已过期")
	PAST,
}
