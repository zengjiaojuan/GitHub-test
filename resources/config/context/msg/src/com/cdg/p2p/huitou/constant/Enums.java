package com.cddgg.p2p.huitou.constant;


/**
 * 
 * 枚举
 * 
 * @author Administrator
 * 
 */
public interface Enums {
    /**
     * 
     * @author Administrator
     * 
     */

    enum PROCEDURES {
        /**
         * 用户注册
         */
    	PROCEDURE_REGISTRATION_UPDATE,
        /**
         * 充值
         */
    	PROCEDURE_RECHARGE_UPDATE,
        /**
         * 提现同步处理
         */
    	PROCEDURE_WITHDRAWAL_RECORD,
        /**
         * 提现异步处理
         */
    	PROCEDURE_WITHDRAWAL_MONEY,
        /**
         * 放款
         */
    	PROCEDURE_LIANS_INSERT,
        /**
         * 还款 同步处理
         */
    	PROCESURE_REPAYMENT_RECORD,
        /**
         * 还款异步处理
         */
        PROCEDURE_REPAYMENT_MONEY,
        /**
         * 修改用户账户余额
         */
        PROCEDURE_USER_MONEY,
        /**
         * 购买债权
         */
        PROCEDURE_COSTRATIO_BUY,
        /**
         * 修改认购产品状态
         */
        PROCEDURE_PRODUCT_STATE,
        /**
         * 流标
         */
        PROCEDURE_STANDAED_INSERT
    }

}
