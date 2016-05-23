package com.phb.puhuibao.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.phb.puhuibao.entity.Feedback;
import com.idp.pub.service.IBaseService;
import com.idp.pub.web.controller.BaseController;

@Controller
@RequestMapping(value = "/feedback")
public class FeedbackController extends BaseController<Feedback, String> {
	@Resource(name = "feedbackService")
	private IBaseService<Feedback, String> baseService;
	@Resource(name = "feedbackService") // for BaseController
	public void setBaseService(IBaseService<Feedback, String> baseService) {
		super.setBaseService(baseService);
	}

	/**
	 * 保存意见
	 * @param muid 用户id
	 * @param content 意见
	 * @return
	 */
	@RequestMapping(value="saveFeedback")
	@ResponseBody
	public Map<String, Object> saveFeedback(@RequestParam int muid, @RequestParam String content) {
		Feedback entity = new Feedback();
		entity.setContent(content);
		entity.setmUserId(muid);
		entity = baseService.save(entity);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("message", "保存成功！");
		data.put("status", 1);
		return data;
	}
}
