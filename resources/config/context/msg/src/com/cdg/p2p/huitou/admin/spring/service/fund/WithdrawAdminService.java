package com.cddgg.p2p.huitou.admin.spring.service.fund;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.cddgg.base.model.PageModel;
import com.cddgg.base.spring.orm.hibernate.impl.HibernateSupport;
import com.cddgg.p2p.huitou.spring.service.UserBaseInfoService;

/**
 * 提现记录
 * 
 * @author Administrator
 * 
 */
@Service
@SuppressWarnings("rawtypes")
public class WithdrawAdminService {

    /**
     * 数据库接口
     */
    @Resource
    private HibernateSupport commonDao;

    @Resource
    private UserBaseInfoService userBaseInfoService;
    
    /**
     * sql拼接
     * @param beginDate
     * @param endDate
     * @param userName
     * @return
     */
    public String connectionSql(String beginDate, String endDate,String userName) {
        String sql = "";
        if (beginDate != null && !"".equals(beginDate.trim())) {
            sql = sql
                    + " AND DATE_FORMAT(a.applytime, '%Y-%m-%d %H:%i:%s')>=DATE_FORMAT('"
                    + beginDate + "', '%Y-%m-%d %H:%i:%s') ";
        }
        if (endDate != null && !"".equals(endDate.trim())) {
            sql = sql
                    + " AND DATE_FORMAT(a.applytime, '%Y-%m-%d %H:%i:%s')<=DATE_FORMAT('"
                    + endDate + "', '%Y-%m-%d %H:%i:%s') ";
        }
        if (userName != null && !userName.trim().equals("")) {
            sql = sql + " AND b.userName LIKE '%" + userName + "%'";
        }
        return sql;
    }

    /**
     * 查询提现条数
     * 
     * @param beginDate
     *            开始时间
     * @param endDate
     *            结束时间
     * @param userName
     *            用户名
     * @param strNum
     *            打款流水号
     * @param withdrawstate
     *            状态
     * @return 提现条数
     */
    public Integer queryCount(String beginDate, String endDate, String userName) {
        String sql = "SELECT count(*)  FROM withdraw a,userbasicsinfo b "
                + "WHERE a.user_id=b.id"
                + connectionSql(beginDate, endDate, userName);
        Object obj = commonDao.findObjectBySql(sql);
        return Integer.parseInt(obj.toString());
    }

    /**
     * 查询提现记录
     * 
     * @param page
     *            分页对象
     * @param beginDate
     *            开始时间
     * @param endDate
     *            结束时间
     * @param userName
     *            用户名
     * @return 提现记录
     */
    public List queryPage(PageModel page, String beginDate, String endDate, String userName) {
        String sql = "SELECT a.id,b.name,b.userName,a.withdrawAmount,"
                + "a.deposit,a.strNum,a.pIpsBillNo,a.time,a.remark "
                + "FROM withdraw a,userbasicsinfo b WHERE "
                + "a.user_id=b.id"
                + connectionSql(beginDate, endDate, userName) + " ORDER BY a.applytime DESC LIMIT "
                + page.firstResult() + "," + page.getNumPerPage();
        List list = commonDao.findBySql(sql);
        return list;
    }


    /**
     * 根据编号查询提现记录
     * 
     * @param ids
     *            编号
     * @return 提现记录
     */
    public List queryById(String ids) {
        StringBuffer sql = new StringBuffer("SELECT a.id,b.name,b.userName,ROUND(a.withdrawAmount,2),"
                + "ROUND(a.deposit,2),a.strNum,a.pIpsBillNo,a.time,a.remark "
                + "FROM withdraw a,userbasicsinfo b WHERE "
                + "a.user_id=b.id");
        if (ids != null && !ids.trim().equals("")) {
            ids = ids.substring(0, ids.lastIndexOf(","));
            sql.append(" AND a.id in (" + ids + ")");
        }
        List list = commonDao.findBySql(sql.toString());
        return list;
    }
}
