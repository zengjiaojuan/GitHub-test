package com.cddgg.base.util;

import java.util.List;

import com.cddgg.p2p.huitou.admin.spring.service.MessagesettingService;
import com.cddgg.p2p.huitou.entity.Userrelationinfo;

/**
* <p>Title:SendMsgThead</p>
* <p>Description: 新标上线提醒发送消息</p>
* <p>date 2014年2月14日</p>
*/
public class SendMsgThead extends Thread {

    /** list 集合 */
    private List<Userrelationinfo> list;
    /** contant 内容 */
    private String contant;
    /** messagesettingService 服务层 */
    private MessagesettingService messagesettingService;

    /**
     * <p>
     * Title:
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param list  要发送的会员列表
     * @param contant 内容
     * @param messagesettingService 服务层
     */
    public SendMsgThead(List<Userrelationinfo> list, String contant,
            MessagesettingService messagesettingService) {
        this.list = list;
        this.contant = contant;
        this.messagesettingService = messagesettingService;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Thread#run()
     */
    @Override
    public void run() {
        try {
            for (Userrelationinfo userrelationinfo : list) {
                messagesettingService.sendMessagetting(userrelationinfo, 12L,
                        contant, "新标上线提醒");
            }
        } catch (Exception e) {
        } finally {
            this.stop();
        }
    }
}
