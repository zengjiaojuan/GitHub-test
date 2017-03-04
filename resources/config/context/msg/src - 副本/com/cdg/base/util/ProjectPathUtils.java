package com.cddgg.base.util;

/**
 * 获取项目运行路径
 * 
 * @author ldd
 * 
 */
public class ProjectPathUtils {

    /**
     * 得到Classes路径
     * 
     * @return 路径
     */
    public static String getClassesPath() {
        return Thread.currentThread().getContextClassLoader().getResource("")
                .getPath().substring(1);
    }

}
