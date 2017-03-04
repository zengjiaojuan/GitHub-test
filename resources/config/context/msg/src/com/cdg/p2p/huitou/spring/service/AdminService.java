package com.cddgg.p2p.huitou.spring.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.cddgg.base.model.PageModel;
import com.cddgg.base.spring.orm.hibernate.impl.HibernateSupport;
import com.cddgg.base.util.StringUtil;
import com.cddgg.p2p.huitou.admin.spring.service.MenuService;
import com.cddgg.p2p.huitou.constant.Constant;
import com.cddgg.p2p.huitou.entity.Adminuser;
import com.cddgg.p2p.huitou.entity.Menu;
import com.cddgg.p2p.huitou.entity.Role;

/**
 * <p>
 * Title:AdminService</p>
 * <p>
 * Description: 后台管理员控制层</p>
 */
@Service
public class AdminService {

    /** 注入数据库操作层 */
    @Resource
    private HibernateSupport dao;

    /** 注入登录日志服务层 */
    @Resource
    private AdminLoginLogService logservice;

    /** 注入后台菜单服务层 */
    @Resource
    private MenuService menuservice;

    /**
     * <p>Title: getMember</p>
     * <p>Description: 查询所有的服务人员</p>
     * @return 返回查询结果集list
     */
    public List getMember(){
        StringBuffer sb=new StringBuffer("SELECT au.id,au.realname from adminuser au INNER JOIN role r on au.role_id=r.id where r.roleName='服务人员'");
        return dao.findBySql(sb.toString(),null);
    }
    /**
    * <p>Title: login</p>
    * <p>Description: 查询当前登录人拥有的菜单</p>
    * @param adminuser 当前登录人
    * @param request HttpServletRequest
    * @return 返回查询结果集list
    */
    public List<Menu> login(Adminuser adminuser, HttpServletRequest request) {

        List<Menu> onemenulist = new ArrayList<Menu>();
        List<Menu> sendmenulist = new ArrayList<Menu>();
        List<Menu> allmenulist = new ArrayList<Menu>();

        // 根据输入的用户名和密码查询用户
        @SuppressWarnings("unchecked")
        List<Adminuser> adminlist = dao.findByExample(adminuser);

        // 如果结果不为空则
        if (null != adminlist && !adminlist.isEmpty()) {

            // 查询当前登录人所能有的菜单
            allmenulist = menuservice.queryByUser(adminlist.get(0));

            StringBuffer parentid = new StringBuffer();

            for (int i = 0; i < allmenulist.size(); i++) {
                if (1 == allmenulist.get(i).getMlevel()) {
                    onemenulist.add(allmenulist.get(i));
                } else if (2 == allmenulist.get(i).getMlevel()) {
                    sendmenulist.add(allmenulist.get(i));

                    parentid.append(allmenulist.get(i).getMenu().getId() + ",");
                }
            }

            // 如果一级菜单为空，或数量不够，则重新查询
            if (StringUtil.isNotBlank(parentid.toString())
                    && onemenulist.isEmpty()
                    || parentid.length() / 2 > onemenulist.size()) {
                onemenulist = menuservice.queryById(parentid.toString()
                        .substring(0, parentid.length() - 1));
            }
            // 记录登录日志
            logservice.addlog_TRAN(adminlist.get(0), request);

            // 保存当前登录人菜单
            request.getSession().setAttribute("onemenulist", onemenulist);
            request.getSession().setAttribute("sendmenulist", sendmenulist);
            request.getSession().setAttribute(Constant.ADMINLOGIN_SUCCESS,
                    adminlist.get(0));
        }

        return onemenulist;
    }

   
    /**
    * <p>Title: queryByRole</p>
    * <p>Description: 根据角色查询用户</p>
    * @param roleinfo 角色编号
    * @return 返回查询结果集list
    */
    @SuppressWarnings("unchecked")
    public List<Adminuser> queryByRole(Role roleinfo) {

        if (StringUtil.isNotBlank(roleinfo.getId() + "")) {
            String hql = "from Adminuser where role.id=" + roleinfo.getId();

            return dao.find(hql);
        }

        return null;
    }

    /**
    * <p>Title: queryPage</p>
    * <p>Description: 分页查询用户信息</p>
    * @param page 分页参数
    * @param user 查询条件
    * @return 返回查询结果集
    */
    @SuppressWarnings("unchecked")
    public List<Object> queryPage(PageModel page, Adminuser user) {

        List<Object> adminlist = new ArrayList<Object>();

        String sql = "SELECT adminuser.id,adminuser.username,adminuser.realname,adminuser.phone,adminuser.sex,adminuser.age,adminuser.email,adminuser.`status` FROM adminuser";

        if (StringUtil.isNotBlank(user.getUsername())) {
            sql += "WHERE adminuser.username LIKE '%" + user.getUsername()
                    + "%' OR adminuser.realname LIKE '%" + user.getUsername()
                    + "%'";
        }

        adminlist = (List<Object>) dao.pageListBySql(page, sql, null);

        return adminlist;

    }

    /**
    * <p>Title: saveOrUpdate</p>
    * <p>Description: 保存或修改用户信息</p>
    * @param adminuser 要保存或修改的用户
    */
    public void saveOrUpdate(Adminuser adminuser) {

        dao.saveOrUpdate(adminuser);
    }

    /**
    * <p>Title: delete</p>
    * <p>Description: 删除用户</p>
    * @param adminuser 要删除的用户
    */
    public void delete(Adminuser adminuser) {

        dao.delete(adminuser);

    }

    /**
    * <p>Title: deletes</p>
    * <p>Description: 批量删除用户</p>
    * @param ids 要删除用户的编号
    */
    public void deletes(String ids) {

        if (StringUtil.isNotBlank(ids)) {
            // 根据“，”拆分字符串
            String[] newids = ids.split(",");

            // 确认删除的编号
            String delstr = "";

            for (String idstr : newids) {

                // 将不是空格和非数字的字符拼接
                if (StringUtil.isNotBlank(idstr)
                        && StringUtil.isNumberString(idstr)) {
                    delstr += idstr + ",";
                }
            }

            // 如果确认删除的字符串不为空
            if (delstr.length() > 0) {

                // 批量删除
                String hql = "from Adminuser where id in ("
                        + delstr.substring(0, delstr.length() - 1) + ")";
                dao.deleteAll(dao.find(hql));

            }

        }

    }
    /**
    * <p>Title: queryById</p>
    * <p>Description: 根据编号查询用户</p>
    * @param adminuser 要查询用户的编号（对象）
    * @return 查询结果，存在就返回Adminuser，不存在返回null
    */
    public Adminuser queryById(Adminuser adminuser) {

        return dao.get(Adminuser.class, adminuser.getId());
    }

}
