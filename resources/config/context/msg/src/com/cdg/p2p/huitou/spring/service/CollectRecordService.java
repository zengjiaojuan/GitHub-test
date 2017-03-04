package com.cddgg.p2p.huitou.spring.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cddgg.base.model.PageModel;
import com.cddgg.base.spring.orm.hibernate.impl.HibernateSupport;
import com.cddgg.commons.date.DateUtils;
import com.cddgg.p2p.huitou.entity.CollectRecord;

/**
 * 收藏记录
 * @author Administrator
 *
 */
@Service
public class CollectRecordService {
	
	/**
	 * 调用通用Dao接口
	 */
	@Resource
	private HibernateSupport commonDao;
	
	/**
	 * 根据用户id查询收藏记录
	 * @param id
	 * @param page
	 * @return
	 */
	public List queryUserCollect(Long id, PageModel page){
		StringBuffer sql=new StringBuffer(
				"select c.id,b.loanNumber,b.loanTitle,u.userName,"
				+ "a.issueLoan,a.rate*100,a.month,a.refundWay,"
				+ "IFNULL(ROUND((select IFNULL(sum(tenderMoney),0) from loanrecord where loansign_id=a.id)*100/a.issueLoan,0),0) as dd,"
				+ "c.collecttime,c.loanSign_id,a.useDay,a.loanType"
				+ " from loansign a,loansignbasics b,collectrecord c,userbasicsinfo u"
				+ " where c.loanSign_id=a.id and a.id=b.id and a.userbasicinfo_id=u.id and c.userbasicinfo_id=")
		.append(id)
		.append(" LIMIT ")
		.append(page.firstResult()+",")
		.append(page.getNumPerPage());
		return commonDao.findBySql(sql.toString());
	}
	
	/**
	 * 查询记录条数
	 * @param id
	 * @return
	 */
	public Object queryCollectCount(Long id){
		StringBuffer sql=new StringBuffer(
				"SELECT count(c.id) FROM collectrecord c where c.userbasicinfo_id=")
		.append(id);
		return commonDao.findObjectBySql(sql.toString());
	}
	
	/**
	 *根据id删除收藏记录 
	 * @param id
	 * @return
	 */
	public boolean deleteCollect(Long id){
		try {
			commonDao.delete(id, CollectRecord.class);
		} catch(Throwable e) {
			e.getMessage();
			return false;
		}
		return true;
	}
	
	/**
	 * 保存收藏记录
	 * @param collectRecord
	 */
	public boolean addCollect(CollectRecord collectRecord) {
		if (checkCollect(collectRecord)) {
			commonDao.save(collectRecord);
			return true;
		}
		return false;
	}
	
	/**
	 * 判断记录是否收藏过
	 * @param collectRecord
	 * @return
	 */
	public boolean checkCollect(CollectRecord collectRecord) {
		String sql = "from CollectRecord c where c.loansign="
				+ collectRecord.getLoansign().getId()
				+ " and c.userbasicsinfo="
				+ collectRecord.getUserbasicsinfo().getId();
		List list = commonDao.find(sql);
		// 判断该条是否收藏过
		if (list.size() != 0) {
			return false;
		}
		return true;
	}
}
