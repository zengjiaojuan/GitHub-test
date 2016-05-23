package com.phb.puhuibao.service;

import java.util.List;

import com.phb.puhuibao.entity.Resource;
import com.phb.puhuibao.entity.ResourceOrder;

public interface ResourceOrderService {

	ResourceOrder processSave(ResourceOrder order);

	void processConfirm(ResourceOrder order);

	void processSelected(Resource resource, List<String> ids);
	void processSelectedOrders(Resource resource, List<String> ids);//3.0版本
	

	void pay(Resource resource, ResourceOrder order);

	void processSelectOrders(Resource resource, String[] ids, boolean isFull);

}
