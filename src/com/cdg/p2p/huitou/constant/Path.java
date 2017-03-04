package com.cddgg.p2p.huitou.constant;

/**
 * 路径
 * 
 * @author ldd
 * 
 */
public class Path {

    /**
     * 路径
     */
    private static String path;

    /**
     * 上传文件路径
     */
    private static String upload;

    /**
     * 构造函数
     */
    private Path() {
    };

    /**
     * 初始化
     * 
     * @param localPath    本地路径
     * @param localUpload  上传路径
     */
    public static void init(String localPath, String localUpload) {
        if (Path.path == null && Path.upload == null) {
            Path.path = localPath;
            Path.upload = localUpload;
        }
    }

    /**
     * 得到Web项目运行绝对路径
     * 
     * @return 路径
     */
    public static String getPath() {
        return path;
    }

    /**
     * 得到上传文件绝对路径
     * 
     * @return 路径
     */
    public static String getUpload() {
        return upload;
    }

}
