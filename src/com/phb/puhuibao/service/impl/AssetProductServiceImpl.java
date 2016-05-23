package com.phb.puhuibao.service.impl;

import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.idp.pub.dao.IBaseDao;
import com.idp.pub.dao.IPagerDao;
import com.idp.pub.service.impl.DefaultBaseService;
import com.phb.puhuibao.entity.AssetProduct;

/**
 * @###################################################
 * @创建日期：2015-4-28 
 * @开发人员：LiuYongbo
 * @功能描述：
 * @修改日志：
 * @###################################################
 */

@Transactional
@Service("assetProductService")
public class AssetProductServiceImpl extends DefaultBaseService<AssetProduct, String> {
	@Resource(name = "assetProductDao")
	public void setBaseDao(IBaseDao<AssetProduct, String> baseDao) {
		super.setBaseDao(baseDao);
	}

	@Resource(name = "assetProductDao")
	public void setPagerDao(IPagerDao<AssetProduct> pagerDao) {
		super.setPagerDao(pagerDao);
	}

	public AssetProduct save(AssetProduct entity) {
		entity.setCreateTime(new Date());
		return  this.getBaseDao().save(entity);
	}
		 
    public AssetProduct update(AssetProduct entity) {
		return  this.getBaseDao().update(entity);
    }		 
		 
    public Integer delete(Map<String, Object> params) {
        return this.getBaseDao().delete(params); 
    }
}
