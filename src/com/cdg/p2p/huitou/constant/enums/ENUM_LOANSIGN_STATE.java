package com.cddgg.p2p.huitou.constant.enums;

import com.cddgg.base.annotation.FieldConfig;
/**
 * 借款标状态enum
 * 1未发布、2进行中、3回款中、4已完成
 * @author Administrator
 *
 */
public enum ENUM_LOANSIGN_STATE{
	/**
	 * 默认值
	 */
	DEFAULT,
	
	/**
     * 未发布
     */
	@FieldConfig("未发布")
	UNPUBLISHED,
	
	/**
	 * 进行中
	 */
	@FieldConfig("进行中")
	INTHE,
	
	/**
	 * 回款中
	 */
	@FieldConfig("回款中")
	BACKIN,
	
	/**
	 * 已完成
	 */
	@FieldConfig("已完成")
	COMPLETED;
}
