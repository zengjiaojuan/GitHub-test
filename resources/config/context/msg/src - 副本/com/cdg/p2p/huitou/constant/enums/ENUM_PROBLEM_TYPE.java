package com.cddgg.p2p.huitou.constant.enums;

import com.cddgg.base.annotation.FieldConfig;
/**
 * 是否显示状态enum
 * 0显示、1不显示
 * @author Administrator
 *
 */
public enum ENUM_PROBLEM_TYPE{
    /**
     * 借款问题
     */
	@FieldConfig("借款问题")
	BORROWPROBLEM,
	/**
	 * 投资问题
	 */
	@FieldConfig("投资问题")
	INVESTPROBLEM,
	/**
     * 平台运营问题
     */
    @FieldConfig("平台运营问题")
	PLATFORMPROBLEM,
}
