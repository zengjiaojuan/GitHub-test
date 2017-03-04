package com.cddgg.p2p.huitou.constant.enums;

import com.cddgg.base.annotation.FieldConfig;

/**
 * 审核结果
 * @author Mr.Po
 *
 */
public enum ENUM_AUDITRESULT_TYPE{
	
    
	/**
	 * 未通过
	 */
	@FieldConfig("未通过")
	NOPASS,
	/**
     * 通过
     */
    @FieldConfig("通过")
    PASS;
}
