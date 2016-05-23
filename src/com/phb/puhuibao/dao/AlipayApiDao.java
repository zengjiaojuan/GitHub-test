package com.phb.puhuibao.dao;
 
import org.springframework.stereotype.Repository;

import com.idp.pub.dao.impl.DefaultBaseDao;
import com.phb.puhuibao.entity.AlipayApi;

@Repository("alipayApiDao")
public class AlipayApiDao extends DefaultBaseDao<AlipayApi,String> {
	public String getNamespace() {
		return AlipayApi.class.getName();
	}

}