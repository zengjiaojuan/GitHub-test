package com.cddgg.p2p.huitou.spring.service;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.cddgg.base.spring.orm.hibernate.impl.HibernateSupport;
import com.cddgg.p2p.core.service.CityInfoService;
import com.cddgg.p2p.huitou.entity.City;
import com.cddgg.p2p.huitou.entity.Province;
import com.cddgg.p2p.huitou.entity.Userbasicsinfo;
import com.cddgg.p2p.huitou.entity.Userfundinfo;

/**
 * 前台首页service
 * 
 * @author My_Ascii
 * 
 */
@Service
@SuppressWarnings(value = { "visitorservice" })
public class VisitorService {

    /**
     * 注入HibernateSupport
     */
    @Resource
    HibernateSupport commondao;

    /**
     * CityInfoService
     */
    @Resource
    CityInfoService cityInfoService;
    
    /**
     * 用户注册
     * 
     * @param user 用户
     */
    public void regist(Userbasicsinfo user) {
        commondao.save(user);
    }

    /**
     * 添加省
     * @param request   request
     */
    public void putProductInfoRightCity(HttpServletRequest request){
        
        //查询到所有的省--longyang
        List<Province> provinceList= cityInfoService.queryAllProvince();
        List<City> cityList= cityInfoService.queryCityByProvince(1);
        request.setAttribute("provinceList",provinceList);
        request.setAttribute("cityList",cityList);
        
    }
    
    /**
     * 用户登陆
     * 
     * @param userName 用户名
     * @param password 用户密码
     * @return Userbasicsinfo
     */
    public Userbasicsinfo login(String userName, String password) {
        String hql = "from Userbasicsinfo where userName = '" + userName
                + "' and password = '" + password + "'";
        List<Userbasicsinfo> userlist = commondao.find(hql);
        if (userlist.size() > 0) {
            return userlist.get(0);
        }
        return null;
    }

    /**
     * 验证用户的登陆名是否重复
     * @param userName 用户名
     * @return boolean
     */
    public boolean checkUserName(String userName) {
        String hql = "from Userbasicsinfo where userName = '" + userName + "'";
        int size = commondao.find(hql).size();
        if (size > 0) {
            return false;
        } else {
            return true;
        }
    }

   /**
    * 验证用户注册邮箱是否重复
    * @param email 邮箱
    * @return boolean
    */
    public boolean checkUserEmail(String email) {
        String hql = "from Userrelationinfo where email = '" + email + "'";
        int size = commondao.find(hql).size();
        if (size > 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 根据会员身份证号码查询会员基本信息
     * 
     * @param cardId
     *            身份证号码
     * @return Userbasicsinfo
     */ 
    public Userbasicsinfo getUserbasicsinfo(String cardId) {

        StringBuffer hql = new StringBuffer(
                "from Userbasicsinfo u where u.userrelationinfo=")
                .append(cardId);

        List userbaseicsList = commondao.find(hql.toString());

        if (userbaseicsList.size() > 0) {

            return (Userbasicsinfo) userbaseicsList.get(0);
        }

        return null;
    }

    /**
     * 根据基本信息编号查询用户的资金信息
     * 
     * @param userBasicsId
     *            用户基本信息编号
     * @return Userfundinfo
     */
    public Userfundinfo getuserUserfundinfo(Long userBasicsId) {

        StringBuffer hqlUser = new StringBuffer(
                "from Userfundinfo u where u.userbasicsinfo=")
                .append(userBasicsId);

        List<Userfundinfo> userFundInfo = (List<Userfundinfo>) commondao
                .find(hqlUser.toString());

        if (userFundInfo.size() > 0) {

            return userFundInfo.get(0);
        }
        return null;

    }
}
