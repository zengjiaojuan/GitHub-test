package com.phb.puhuibao.dao;

import org.springframework.stereotype.Repository;

import com.idp.pub.dao.impl.DefaultBaseDao;
import com.phb.puhuibao.entity.Holiday;

@Repository("holidayDao")
public class HolidayDao extends DefaultBaseDao<Holiday,String> {
	public String getNamespace() {
		return Holiday.class.getName();
	}
}
