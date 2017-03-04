package com.cddgg.p2p.huitou.spring.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cddgg.base.spring.orm.hibernate.impl.HibernateSupport;
import com.cddgg.commons.log.LOG;
import com.cddgg.p2p.huitou.entity.Securityproblem;
import com.cddgg.p2p.huitou.entity.Userbasicsinfo;
import com.cddgg.p2p.huitou.entity.Validcodeinfo;
import com.cddgg.p2p.huitou.entity.Verifyproblem;

/**
 * 安全中心
 * 
 * @author ransheng
 * 
 */

@Service
@SuppressWarnings("unchecked")
public class VerifyService {

    /**
     * 数据库接口
     */
    @Resource
    private HibernateSupport commonDao;

    /**
     * 接口
     */
    @Resource
    private UserBaseInfoService userBaseInfoService;

    /**
     * 修改邮箱
     * 
     * @param user
     *            用户
     * @param email
     *            邮箱
     */

    /**
     * 查询安全问题类型
     * 
     * @return 安全问题类型
     */
    public List<Verifyproblem> queryVerify() {
        String hql = "from Verifyproblem";
        List<Verifyproblem> list = commonDao.find(hql);
        return list;
    }

    /**
     * 修改用户安全问题
     * 
     * @param id
     *            用户编号
     * @param id1
     *            安全问题编号1
     * @param id2
     *            安全问题编号2
     * @param answer1
     *            安全问题答案
     * @param answer2
     *            安全问题答案
     * @return 是否设置成功
     */
    public boolean updateSafe(Long id, Long id1, Long id2, String answer1,
            String answer2) {
        try {
            String hql = "from Securityproblem where userbasicsinfo.id=?";
            List<Securityproblem> list = commonDao.find(hql, id);
            if (list.size() > 1) {
                // 修改安全问题
                Securityproblem security = list.get(0);
                security.setVerifyproblem(new Verifyproblem(id1));
                security.setAnswer(answer1);
                commonDao.update(security);
                security = list.get(1);
                security.setVerifyproblem(new Verifyproblem(id2));
                security.setAnswer(answer2);
                commonDao.update(security);

                return true;
            } else {
                return false;
            }
        } catch (Throwable e) {
            LOG.info("修改安全问题出错：" + e);
            return false;
        }
    }

    /**
     * 修改电话号码
     * 
     * @param user
     *            用户基本信息
     * @param phone
     *            手机号码
     * @param vail
     *            验证码表
     */
    public void updatePhone(Userbasicsinfo user, String phone,
            Validcodeinfo vail) {
        if (vail != null) {
            // 清空手机验证码
//            userBaseInfoService.clearPhone(vail);
        }
        user.getUserrelationinfo().setPhone(phone);
        commonDao.update(user);

    }
}
