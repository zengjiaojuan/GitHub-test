package com.cddgg.base.spring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cddgg.p2p.huitou.spring.annotation.CheckLogin;

/**   
 * Filename:    SystemInfoController.java   
 * @version:    1.0   
 * @since:  JDK 1.7.0_25  
 * Create at:   2014年5月4日 上午9:29:57   
 * Description:  获取服务器资源信息
 *   
 * Modification History:   
 * 时间    			作者   	   	版本     		描述 
 * ----------------------------------------------------------------- 
 * 2014年5月4日 	LiNing      1.0     	1.0Version   
 */
@Controller
@RequestMapping("/run_time")
@CheckLogin(value=CheckLogin.ADMIN)
public class SystemInfoController {

    
    @ResponseBody
    @RequestMapping("/get_info")
    public String runInfo(){
        String result="";
        
        Runtime runtime =Runtime.getRuntime();
        
        //获取处理器个数
        result+=runtime.availableProcessors()+"-";
        //空闲内存
        result+=runtime.freeMemory()/1024/1024+"-";
        //总内存
        result+=runtime.totalMemory()/1024/1024+"-";
        //最大可用内存
        result+=runtime.maxMemory()/1024/1024;
        
        return result;
    }  
    
}
