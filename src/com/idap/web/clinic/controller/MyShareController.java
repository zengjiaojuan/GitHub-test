package com.idap.web.clinic.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.idap.clinic.entity.Share;
import com.idp.pub.entity.Pager;
import com.idp.pub.service.IBaseService;
import com.idp.pub.web.controller.BaseController;

@Controller
@RequestMapping(value = "/myShare")
public class MyShareController extends BaseController<Share, String> {
	@Resource(name = "shareService")
	public void setBaseService(IBaseService<Share, String> baseService) {
		super.setBaseService(baseService);
	}

	@RequestMapping(value="queryShare")
	@ResponseBody
	public Map<String, Object> queryShare(@RequestParam int pageno, @RequestParam String muid) {
		Pager<Share> pshare = new Pager<Share>();
		pshare.setReload(true);
		pshare.setCurrent(pageno);
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("muid", muid);
		map.put("orderBy", "share_datetime");
		map.put("order", "desc");
		Pager<Share> p=this.getBaseService().findByPager(pshare, map);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("result", p.getData());
		data.put("count", p.getTotal());
		data.put("message", "");
		data.put("status", 1);
		return data;
	}

	/**
	 * 保存数据
	 * @param title 标题
	 * @param content 内容
	 * @return 结果
	 */
	@RequestMapping(value="saveShare")
	@ResponseBody
	public Map<String, Object> saveShare(@RequestParam String muid, @RequestParam String title, @RequestParam String content, @RequestParam String type) {
		Share entity = new Share();
		entity.setTitle(title);
		entity.setContent(content);
		entity.setType(type);
		entity.setMuid(muid);

		Map<String, Object> data = new HashMap<String, Object>();
		try {
			entity = this.getBaseService().save(entity);
		} catch (Exception e) {
			data.put("message", "网络异常！");
			data.put("status", 0);			
			return data;
		}
		data.put("message", "保存成功！");
		data.put("status", 1);
		return data;
	}
}
