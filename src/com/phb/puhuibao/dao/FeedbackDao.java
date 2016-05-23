package com.phb.puhuibao.dao;

import org.springframework.stereotype.Repository;

import com.phb.puhuibao.entity.Feedback;
import com.idp.pub.dao.impl.DefaultBaseDao;

@Repository("feedbackDao")
public class FeedbackDao extends DefaultBaseDao<Feedback,String> {
	public String getNamespace() {
		return Feedback.class.getName();
	}
}
