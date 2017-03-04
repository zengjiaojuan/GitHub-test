package com.cddgg.p2p.huitou.constant;

import com.cddgg.base.annotation.FieldConfig;
/**
 * 错误代码
 * 
 * @author ldd
 * 
 */
public enum ErrorNumber {
    /**
     * 默认无（仅作占位不做使用）
     */
    DEFAULT,
    /**
     * 常见同态异常
     */
    NO_1,
    /**
     * 您所购买的产品不存在！
     */
    @FieldConfig("您所购买的产品不存在！")
    NO_2,
    /**
     * 无法判断用户是否有充足的的余额，因为无法获取到用户！
     */
    @FieldConfig("无法判断用户是否有充足的的余额，因为无法获取到用户！")
    NO_3,
    /**
     * 匹配债权错误，匹配金额超过了购买金额超过，请稍后重试！
     */
    @FieldConfig("匹配债权错误，匹配金额超过了购买金额超过，请稍后重试！")
    NO_4,
    /**
     * 您所购买的金额必须为1000的整数倍！
     */
    @FieldConfig("您所购买的金额必须为1000的整数倍！")
    NO_5,
    /**
     * 您输入的参数不正确，请启用浏览器的JavaScript脚本验证！
     */
    @FieldConfig("您输入的参数不正确，请启用浏览器的JavaScript脚本验证！")
    NO_6,
    /**
     * 已经找到与之匹配的债权，但锁定这些债权已被其他用户锁定，请稍后重试！
     */
    @FieldConfig("已经找到与之匹配的债权，但锁定这些债权已被其他用户锁定，请稍后重试！")
    NO_7,
    /**
     * 您的购买的产品匹配的债权已超时，请重新匹配后购买！
     */
    @FieldConfig("您的购买的产品匹配的债权已超时，请重新匹配后购买！")
    NO_8,
    /**
     * 更新债权信息失败，购买失败！
     */
    @FieldConfig("更新债权信息失败，购买失败！")
    NO_9,
}
