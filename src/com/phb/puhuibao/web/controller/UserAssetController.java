package com.phb.puhuibao.web.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.idp.pub.entity.Pager;
import com.idp.pub.service.IBaseService;
import com.idp.pub.utils.JsonUtils;
import com.idp.pub.web.controller.BaseController;
import com.phb.puhuibao.entity.UserAsset;

@Controller
@RequestMapping(value = "/userAsset")
public class UserAssetController extends BaseController<UserAsset, String> {
	@Resource(name = "userAssetService")
	public void setBaseService(IBaseService<UserAsset, String> baseService) {
		super.setBaseService(baseService);
	}

	@RequestMapping(params = "paging=true", method = RequestMethod.GET)
	@ResponseBody
	public Pager<UserAsset> findByPager(Pager<UserAsset> pager, @RequestParam("params") String values) {
		pager = this.getBaseService().findByPager(pager, JsonUtils.toMap(values));
		return pager;
	}
}
