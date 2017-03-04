package com.cddgg.p2p.huitou.spring.util;

import com.cddgg.p2p.huitou.constant.Constant;
import com.cddgg.p2p.huitou.entity.Userbasicsinfo;

/**
 * 判断用户是否注册有ips账号;
 * @author RanQiBing 2014-02-26
 *
 */
public class IpsJudge {
    
    /**
     * 判断用户是否有ips账号
     * @param userbasics 用户信息
     * @return 返回路径
     */
    public static String judge(Userbasicsinfo userbasics){
        if (null == userbasics.getUserrelationinfo().getEmailisPass()
                || Constant.STATUES_ZERO == userbasics
                        .getUserrelationinfo().getEmailisPass()) {
            return "WEB-INF/views/member/safetycenter/verify_email";
        }else if(null == userbasics.getUserrelationinfo().getCardId()){
            return "WEB-INF/views/member/safetycenter/verify_identity";
        }else if(null == userbasics.getUserrelationinfo().getPhone()){
            return "WEB-INF/views/member/safetycenter/verify_phone";
        }else if(null == userbasics.getUserfundinfo().getpIdentNo()){
            return "WEB-INF/views/member/safetycenter/ipsregistration";
        }else{
            return null;
        }
    }
}
