package com.cddgg.base.spring.controller;

import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cddgg.commons.image.SecurityImageTool;
import com.cddgg.commons.log.LOG;
import com.cddgg.commons.normal.RandomUtils;

/**
 * 验证码生成控制器
 * 
 * @author ldd
 * 
 */
@RequestMapping("cic")
@Controller
public class CaptchaImageController {

    /**
     * 不缓存
     */
    private static final String NO_CACHE = "No-cache";
    
    /**
     * 得到一个校验码
     * @param name      校验码保存键值
     * @param request   请求
     * @param response  响应
     */
    @RequestMapping("/code")
    public void getCode(String name, HttpServletRequest request,
            HttpServletResponse response) {

        if (null==name){
            return;
        }
            

        String code = RandomUtils.getDefaultString(4);

        LOG.info("产生一个验证码[" + code + "],保存于[" + name + "]");

        response.setHeader("Pragma",NO_CACHE);// 禁止缓存
        response.setHeader("Cache-Control",NO_CACHE);
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");// 指定生成的响应是图片

        request.getSession().setAttribute(name, code);
        try {
            ImageIO.write(SecurityImageTool.createImage(code), "JPEG",
                    response.getOutputStream());
        } catch (IOException e) {
            LOG.error("生成验证图片失败！", e);
        }
        
        

    }

}
