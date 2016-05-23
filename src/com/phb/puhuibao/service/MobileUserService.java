package com.phb.puhuibao.service;

import java.util.Map;

import com.phb.puhuibao.entity.MobileUser;

public interface MobileUserService {

	Map<String, Object> processFortune(String muid);

	Map<String, Object> processmyAsset(String muid);

	void adminCreate(MobileUser entity);

}
