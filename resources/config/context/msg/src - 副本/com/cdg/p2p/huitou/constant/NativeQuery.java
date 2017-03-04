package com.cddgg.p2p.huitou.constant;

/**
 * 本地sql
 * @author ldd
 *
 */
public interface NativeQuery {

    /**
     * 历史产品信息
     */
    String PRODUCT_INFO_HISTORY = "product_info_history";
    
    /**
     * 初判断是否拥有最大限度的匹配
     */
    String INIT_MATCH_COUNT = "init_match_count";
    
    /**
     * 挑选债权
     */
    String PICK_CREDITOR = "pick_creditor";
    
    /**
     * 挑选大额度债权
     */
    String PICK_BIG_CREDITOR = "pick_big_creditor";
    
    /**
     * 匹配未完整债权
     */
    String MATCH_INCOMPLETE_CREDITOR = "match_incomplete_creditor";
    
    /**
     * 挑选未完整债权
     */
    String PICK_INCOMPLETE_CREDITOR = "pick_incomplete_creditor";
    
    /**
     * 得到未匹配的时间
     */
    String OTHER_CUR_DATE = "other_cur_date";
}
