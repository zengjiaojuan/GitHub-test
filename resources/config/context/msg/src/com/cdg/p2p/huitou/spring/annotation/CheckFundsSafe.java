package com.cddgg.p2p.huitou.spring.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 对资金操作前的安全检查，用户信息如邮箱未激活等都不能对资金操作
 * 
 * @author 冉升
 * @version 2014-3-13
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckFundsSafe {

    /**
     * 默认全部字段都检查
     * 
     */
    String value() default "ALL";
}
