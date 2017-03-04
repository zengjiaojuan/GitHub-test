package com.cddgg.base.sms.impl;

import java.util.ArrayList;
import java.util.UUID;

import com.cddgg.base.sms.SmsProxy;
import com.cddgg.base.sms.SmsResult;
import com.cddgg.commons.log.LOG;
import com.esms.MessageData;
import com.esms.PostMsg;
import com.esms.common.entity.*;

/**
 * PostMsg短信接口
 * @author ldd
 *
 */
public class PostMsgSms implements SmsProxy{
    /**
     * 账户
     */
    Account ac;
    
    /**
     * 发送器
     */
    PostMsg pm;
    
    
    @Override
    public void init(String username, String password, String etc){
        
        String[] strs = etc.split(",");
        
        ac = new Account(username,password);
        
        pm = new PostMsg();
        
        pm.getCmHost().setHost(strs[0],Integer.parseInt(strs[1]));  //设置网关的IP和port
        pm.getWsHost().setHost(strs[2],Integer.parseInt(strs[3]));  //设置WebService的 IP和port
    }

    @Override
    public SmsResult sendSMS(String content, String... telNos) throws Exception{
        
        MTPack pack = new MTPack();
        pack.setBatchID(UUID.randomUUID());
        pack.setMsgType(MTPack.MsgType.SMS);
        pack.setSendType(MTPack.SendType.MASS);
        pack.setBizType(0);
        pack.setDistinctFlag(false);
        
        ArrayList<MessageData> msgs = new ArrayList<MessageData>();
        
        for(String tel : telNos){
            msgs.add(new MessageData(tel, content));
        }
        
        pack.setMsgs(msgs);
        
        GsmsResponse resp = pm.post(ac, pack);
        
        boolean status = false;
        
        if(resp.getResult()==0){
            status = true;
            LOG.info("发送成功，返回值为："+resp.getResult());
        }else{
            LOG.error("发送失败！返回值为："+resp.getResult()+"请查看webservice返回值对照表");
        }
        
        
        return new SmsResult(status, resp, resp.getMessage());
    }
  
}

