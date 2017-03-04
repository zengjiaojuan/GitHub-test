package com.cddgg.p2p.huitou.spring.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cddgg.base.spring.orm.hibernate.impl.HibernateSupport;
import com.cddgg.commons.normal.Validate;
import com.cddgg.p2p.huitou.entity.Securityproblem;
import com.cddgg.p2p.huitou.entity.Userbasicsinfo;
import com.cddgg.p2p.huitou.entity.Verifyproblem;

/**
 * 安全中心验证
 * 
 * @author ransheng
 * 
 */
@Service
public class VerificationService {

    /**
     * 数据库接口
     */
    @Resource
    private HibernateSupport commonDao;

    /**
     * 会员基本信息
     */
    @Resource
    private MemberCenterService memberCenterService;

    /**
     * 1、邮箱验证, 2、实名认证, 3、安全问题 ,4、手机验证
     * 
     * @param id
     *            用户编号
     * @return 1、邮箱验证, 2、实名认证, 3、安全问题 ,4、手机验证，其余通过验证
     */
    public Integer queryVerification(Userbasicsinfo user) {
        // 判断手机是否通过验证
        String phone = user.getUserrelationinfo().getPhone() == null ? ""
                : user.getUserrelationinfo().getPhone().trim();
        if (phone.equals("")) {
            return 1;
        }
        Integer email = user.getUserrelationinfo().getEmailisPass() == null ? 0
                : user.getUserrelationinfo().getEmailisPass();
        // 如果没有通过邮箱验证
        if (email == 0) {
            return 2;
        }
        String name = user.getName() == null ? "" : user.getName().trim();
        String cardId = user.getUserrelationinfo().getCardId() == null ? ""
                : user.getUserrelationinfo().getCardId().trim();
        // 实名验证没有通过
        if (name.equals("") || cardId.equals("")) {
            return 3;
        }

        // 判断用户是否通过安全验证
        boolean bool = memberCenterService.isSecurityproblem(user.getId());
        if (!bool) {
            return 4;
        }
        String ipsNo = user.getUserfundinfo().getpIdentNo();
        if (!Validate.emptyStringValidate(ipsNo)) {
            return 5;
        }
        return 6;
    }

    /**
     * 邮箱通过验证
     * 
     * @param user
     *            用户基本信息
     */
    public void emailSafe(Userbasicsinfo user) {
        user.getUserrelationinfo().setEmailisPass(1);
        commonDao.update(user);
    }

    /**
     * 实名验证
     * 
     * @param user
     *            用户基本信息
     * @param cardId
     *            身份证号码
     * @param name
     *            真实姓名
     * @param nickName
     *            昵称
     */
    public void realNameSafe(Userbasicsinfo user, String cardId, String name,
            String nickName) {
        user.getUserrelationinfo().setCardId(cardId);
        user.setName(name);
        user.setNickname(nickName);
        commonDao.update(user);
    }

    /**
     * 安全问题验证
     * 
     * @param user
     *            用户信息
     * @param pwd
     *            交易密码
     * @param id1
     *            安全问题编号1
     * @param id2
     *            安全问题编号2
     * @param answer1
     *            安全问题答案1
     * @param answer2
     *            安全问题答案2
     */
    public void updateSecuritySafe(Userbasicsinfo user, String pwd, String id1,
            String id2, String answer1, String answer2) {

        // 修改交易密码
        user.setTransPassword(pwd);
        commonDao.update(user);

        // 添加安全问题1
        Securityproblem problem = new Securityproblem();
        problem.setAnswer(answer1);
        problem.setUserbasicsinfo(user);
        problem.setVerifyproblem(new Verifyproblem(Long.valueOf(id1)));
        commonDao.save(problem);

        // 添加安全问题2
        problem = new Securityproblem();
        problem.setAnswer(answer2);
        problem.setUserbasicsinfo(user);
        problem.setVerifyproblem(new Verifyproblem(Long.valueOf(id2)));
        commonDao.save(problem);
    }
}
