package com.cddgg.p2p.huitou.spring.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cddgg.base.model.PageModel;
import com.cddgg.base.spring.orm.hibernate.impl.HibernateSupport;
import com.cddgg.p2p.huitou.entity.Vipinfo;
import com.cddgg.p2p.huitou.entity.Viptype;

/**
 * 会员升级业务处理
 * @author RanQiBing 
 * 2014-04-17
 *
 */
@Service
public class UserInfoVipService {
	
	@Resource
	private HibernateSupport dao;
	
	/**
	 * 添加数据
	 * @param vipinfo 
	 */
	public void save(Vipinfo vipinfo){
		dao.save(vipinfo);
	}
	/**
	 * 根据订单编号查询订单信息
	 * @param number
	 * @return 返回会员升级信息
	 */
	public Vipinfo getVipNumber(String number){
		return (Vipinfo) dao.findObject("from Vipinfo v where v.number=?", number);
	}
	/**
	 * 修改会员升级信息
	 * @param vipinfo
	 */
	public void update(Vipinfo vipinfo){
		dao.save(vipinfo);
	}
	/**
	 * 获取用户近的一条升级记录
	 * @param id 用户编号
	 * @return 
	 * @return 返回最新的一条记录
	 */
	public void delete(Vipinfo vipinfo){
	   dao.delete(vipinfo);
	}
	/**
	 * 获取用户近的一条升级记录
	 * @param id 用户编号
	 * @return 返回最新的一条记录
	 */
	public Vipinfo getuserId(Long id,Long vipId){
		String hql = "from Vipinfo v where v.userbasicsinfo.id=? and v.id!=?";
		List<Vipinfo> list = dao.find(hql,id,vipId);
		if(null!=list&&list.size()>0){
			return list.get(list.size()-1);
		}
		return null;
	}
	/**
	 * 获取用户近的一条升级记录
	 * @param id 用户编号
	 * @return 返回最新的一条记录
	 */
	public Vipinfo get(Long id){
		return (Vipinfo) dao.findObject("from Vipinfo v where v.id=?", id);
	}
	/**
	 * 获取会员资费信息
	 * @return 返回类型信息
	 */
	public Viptype getType(){
		List<Viptype> type = dao.find("from Viptype");
		if(null!=type&&type.size()>0){
			return type.get(0);
		}
		return null;
	}
	/**
	 * 分页查询当前用户会员升级情况
	 * @param userid 会员编号
	 * @return 返回分页对象
	 */
	public PageModel getVipInfo(Long userid,PageModel page){
		String sqlCount = "SELECT COUNT(v.id) from vipinfo v,viptype t where v.viptype_id=t.id and v.user_id=?";
		String sql = "SELECT v.id,v.number,t.money,v.begintime,v.endtime,v.status from vipinfo v,viptype t where v.viptype_id=t.id and v.user_id=? ORDER BY v.id DESC LIMIT "+(page.getPageNum()-1)+","+page.getNumPerPage()+"";
		page.setTotalCount(dao.queryNumberSql(sqlCount, userid).intValue());
		List<Object[]> list = dao.findBySql(sql,userid);
		page.setList(list);
		return page;
	}
	/**
	 * 查询用户会员升级的最后一条记录
	 * @param userId 用户编号
	 * @return 返回会员记录
	 */
	public Vipinfo getuserId(Long userId){
		String hql = "from Vipinfo v where v.userbasicsinfo.id=?";
		List<Vipinfo> list = dao.find(hql,userId);
		if(null!=list&&list.size()>0){
			return list.get(list.size()-1);
		}
		return null;
	}
}
