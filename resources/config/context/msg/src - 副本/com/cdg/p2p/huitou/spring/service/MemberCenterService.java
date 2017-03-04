package com.cddgg.p2p.huitou.spring.service;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cddgg.base.model.PageModel;
import com.cddgg.base.spring.orm.hibernate.impl.HibernateSupport;
import com.cddgg.p2p.huitou.entity.Userbasicsinfo;
import com.cddgg.p2p.huitou.entity.Usermessage;

/**
 * 会员中心首页
 * 
 * @author ransheng
 * 
 */
@Service
@SuppressWarnings("rawtypes")
public class MemberCenterService {

    /**
     * 通用dao
     */
    @Resource
    private HibernateSupport commonDao;

    /**
     * 根据id查询会员基本信息
     * 
     * @param id
     *            会员编号
     * @return 会员基本信息
     */
    public Userbasicsinfo queryById(Long id) {
        Userbasicsinfo user = commonDao.get(Userbasicsinfo.class, id);
        return user;
    }

    /**
     * 查询用户是否设置安全问题
     * 
     * @param id
     *            用户编号
     * @return 是否设置安全问题
     */
    public boolean isSecurityproblem(Long id) {
        // 查询用户设置安全问题的个数
        String sql = "SELECT COUNT(*) FROM securityproblem b WHERE b.user_id="
                + id;
        Object obj = commonDao.findObjectBySql(sql);
        // 如果条数小于1，代表用户还未设置安全问题。
        if (Integer.parseInt(obj + "") < 1) {
            return false;
        }
        return true;
    }

    /**
     * 根据用户查询系统消息条数
     * 
     * @param id
     *            用户编号
     * @return 消息条数
     */
    public Object queryUserMessageCount(Long id) {
        StringBuffer sql = new StringBuffer(
                "SELECT count(1) FROM usermessage a where a.user_id=")
                .append(id);
        return commonDao.findObjectBySql(sql.toString());
    }

    /**
     * 查询已读或者未读消息
     * 
     * @param id
     *            用户编号
     * @param isRead
     *            是否读取
     * @return 消息条数
     */
    public Object queryIsReadCount(Long id, Integer isRead) {
        StringBuffer sql = new StringBuffer(
                "SELECT count(1) FROM usermessage a where a.user_id=")
                .append(id).append(" AND a.isread=").append(isRead);
        return commonDao.findObjectBySql(sql.toString());
    }

    /**
     * 更用用户编号查询用户系统消息
     * 
     * @param id
     *            用户编号
     *            @param page 分页对象
     * @return 系统消息结果集
     */
    public List queryUserMessage(Long id, PageModel page) {
        String sql = "SELECT id,context,isread,receivetime,title,user_id FROM usermessage WHERE user_id=? ORDER BY isread,usermessage.receivetime DESC ";
        return commonDao.pageListBySql(page, sql ,null, id);
    }

    /**
     * 根据编号查询系统消息
     * 
     * @param id
     *            消息编号
     * @param unRead
     *            是否已读
     * @return 系统消息
     */
    public Usermessage queryById(Long id, Integer unRead) {
        // 查询单条系统消息
        Usermessage message = commonDao.get(Usermessage.class, id);
        // 如果该条信息未读，则修改为已读
        if (unRead == 0) {
            message.setIsread(1);
            commonDao.update(message);
        }
        return message;
    }

    /**
     * 查询用户登录日志
     * 
     * @param id
     *            用户编号
     * @return 登录日志
     */
    public List queryLog(Long id) {
        List list = null;
        String sql = "SELECT id,logintime,ip,address FROM userloginlog "
                + "WHERE user_id=? ORDER BY id DESC LIMIT 10";
        list = commonDao.findBySql(sql, id);
        return list;
    }
    
    /**
     * 根据编号删除系统消息
     * 
     * @param ids
     *            系统消息编号，以逗号分开
     * @return 删除成功true、失败false
     */
    public boolean deletes(String ids) {
        try {
            String[] id = ids.split(",");
            // 删除消息
            for (int i = 0; i < id.length; i++) {
                commonDao.delete(Long.valueOf(id[i]), Usermessage.class);
            }
        } catch (Throwable e) {
            e.getMessage();
            return false;
        }
        return true;
    }
    
    /**
     * 累计利息成本
     * 
     * @param id
     *            会员编号
     * @return 累计利息成本
     */
    public Object interest(Long id) {
        String sql = "SELECT id FROM loansign a  WHERE a.userbasicinfo_id="
                + id;
        List list = commonDao.findBySql(sql);
        BigDecimal sum = new BigDecimal(0);
        for (int i = 0; i < list.size(); i++) {
            Object loanSignId = list.get(i);
            sql = "SELECT repayState,realMoney,preRepayMoney FROM "
                    + "repaymentrecord  WHERE loanSign_id=" + loanSignId;
            List repay = commonDao.findBySql(sql);
            for (int j = 0; j < repay.size(); j++) {
                Object[] obj = (Object[]) repay.get(j);
                if (Integer.parseInt(obj[0].toString()) == 1
                        || Integer.parseInt(obj[0].toString()) == 3) {
                    sum = sum.add((BigDecimal) obj[2]);
                } else {
                    sum = sum.add((BigDecimal) obj[1]);
                }
            }

        }

        return sum;
    }
    
    /**
     * 累计支付
     * @param id
     * @return
     */
    public Object payment(Long id) {
        String sql = "SELECT IFNULL(SUM(b.money)+SUM(b.preRepayMoney),0) FROM "
                + "loansign a INNER JOIN repaymentrecord b ON a.id=b.loanSign_id "
                + "WHERE b.repayState=3 AND a.userbasicinfo_id=?";
        Object obj = commonDao.findObjectBySql(sql, id);
        return obj;
    }
    
    /**
     * 待确认提现
     * @param id
     * @return
     */
    public Object noTransfer(Long id){
    	String sql = "SELECT IFNULL(SUM(withdrawAmount),0)+IFNULL(SUM(deposit),0) FROM Withdraw "
                + "WHERE user_id=? AND withdrawstate=0";
        Object obj = commonDao.findObjectBySql(sql, id);
        return obj;
    }
    
    /**
     * 待确认充值
     * @param id
     * @return
     */
    public Object rechargeTobe(Long id){
    	String sql="SELECT IFNULL(SUM(r.rechargeAmount),0)"
    			+ " FROM Recharge r"
    			+ " WHERE r.status=0 AND r.user_id=?";
    	Object obj=commonDao.findObjectBySql(sql, id);
    	return obj;
    }
    
    /**
     * 投资记录
     * @param id
     * @param loanstate
     * @return
     */
    public Object investmentRecords(Long id, Integer loanstate) {
        String sql = "SELECT IFNULL(SUM(b.tenderMoney),0) FROM "
                + "Loansign a INNER JOIN loanrecord b ON a.id=b.loanSign_id "
                + "WHERE a.loanstate=? AND b.userbasicinfo_id=?";
        Object obj = commonDao.findObjectBySql(sql, loanstate, id);
        return obj;
    }
    
    /**
     * 借款记录
     * @param id
     * @param loanstate
     * @return
     */
    public Object borrowing(Long id,Integer loanstate){
    	String sql="SELECT IFNULL(SUM(a.issueLoan),0) FROM Loansign a "
                + "WHERE a.userbasicinfo_id=? AND a.loanstate=?";
    	Object obj = commonDao.findObjectBySql(sql, id, loanstate);
    	return obj;
    }
    
    /**
     * 已用额度
     * 
     * @param id
     *            会员编号
     * @return 已用额度
     */
    public Object usedAmount(Long id) {
        String sql = "SELECT IFNULL(SUM(a.issueLoan), 0) FROM "
                + "Loansign a WHERE a.userbasicinfo_id=? "
                + "AND a.loanstate!=4 AND a.loanstate!=1";
        Object obj = commonDao.findObjectBySql(sql, id);
        return obj;
    }
    
    
    public Object score(Long id){
    	String sql="SELECT IFNULL(b.suminte,0) FROM borrowersbase b WHERE b.userbasicinfo_id=?";
    	Object obj=commonDao.findObjectBySql(sql, id);
    	return obj;
    }
}
