package com.cddgg.p2p.huitou.admin.spring.service;

import org.apache.log4j.Logger;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cddgg.base.spring.orm.hibernate.impl.HibernateSupport;
import com.cddgg.commons.date.DateUtils;
import com.cddgg.p2p.huitou.entity.Menu;
import com.cddgg.p2p.huitou.entity.Menurole;
import com.cddgg.p2p.huitou.entity.Role;

/**
 * <p>
 * Title:MenuroleService</p>
 * <p>
 * Description: 菜单角色服务层</p>
 * <p>
 */
@Service
public class MenuroleService {
    /**
     * 引入log4j日志打印类
     */
    private static final Logger LOGGER = Logger
            .getLogger(MenuroleService.class);

    /** 注入数据库操作层 */
    @Resource
    private HibernateSupport dao;

    /** 注入菜单服务层 */
    @Resource
    private MenuService menuservice;

    /**
     * <p>
     * Title: updateByRole</p>
     * <p>
     * Description:修改角色菜单信息 </p>
     * 
     * @param roleid 角色编号
     *            
     * @param menuids 菜单编号
     *            
     */
    public void updateByRole(long roleid, String menuids) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("updateByRole(long, String)方法开始"); //$NON-NLS-1$
        }

        String rolemenuhql = "from Menurole where role.id=" + roleid;

        List<Menurole> menurolelist = dao.find(rolemenuhql);

        if (null != menurolelist && !menurolelist.isEmpty()) {
            // 删除角色现有菜单
            dao.deleteAll(menurolelist);
        }

        if (menuids.length() > 0) {
            String menuhql = "from Menu where id in ("
                    + menuids.substring(0, menuids.length() - 1) + ")";

            List<Menu> menulist = menuservice.queryByHql(menuhql);

            Menurole menurole = null;

            Role roleinfo = new Role();

            roleinfo.setId(roleid);

            // 角色新增菜单
            for (Menu newmenu : menulist) {

                menurole = new Menurole();

                menurole.setCreateTime(DateUtils.format("yyyy-MM-dd HH:mm:ss"));
                menurole.setMenu(newmenu);
                menurole.setRole(roleinfo);

                dao.save(menurole);

            }
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("updateByRole(long, String)方法结束"); //$NON-NLS-1$
        }
    }

    /**
    * <p>Title: deleteByRole</p>
    * <p>Description: 根据角色编号删除角色</p>
    * @param role 角色
    * @return 删除结果 boolean
    */
    public Boolean deleteByRole(Role role) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("deleteByRole(Role)方法开始"); //$NON-NLS-1$
        }

        boolean flag = false;

        String hql = "FROM Menurole WHERE role.id=?";

        dao.deleteAll(dao.find(hql, role.getId()));

        flag = true;

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("deleteByRole(Role)方法结束OUTPARAM=" + flag); //$NON-NLS-1$
        }
        return flag;

    }
}
