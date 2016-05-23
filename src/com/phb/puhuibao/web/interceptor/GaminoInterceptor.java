package com.phb.puhuibao.web.interceptor;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.idp.pub.entity.Pager;
import com.idp.pub.service.IBaseService;
import com.phb.puhuibao.common.Constants;
import com.phb.puhuibao.entity.CalculateFormula;
import com.phb.puhuibao.entity.ExperienceValue;
import com.phb.puhuibao.entity.HexagramAbility;
import com.phb.puhuibao.entity.HexagramCharm;
import com.phb.puhuibao.entity.HexagramCredit;
import com.phb.puhuibao.entity.HexagramFame;
import com.phb.puhuibao.entity.HexagramFortune;
import com.phb.puhuibao.entity.MobileUser;
import com.phb.puhuibao.entity.MobileUserHexagram;
import com.phb.puhuibao.entity.MuserFollow;
import com.phb.puhuibao.entity.Resource;
import com.phb.puhuibao.entity.ResourceOrder;
import com.phb.puhuibao.entity.UserCard;
import com.phb.puhuibao.web.controller.EvaluateController;
import com.phb.puhuibao.web.controller.MobileUserController;
import com.phb.puhuibao.web.controller.MuserFollowController;
import com.phb.puhuibao.web.controller.ResourceController;
import com.phb.puhuibao.web.controller.ResourceOrderController;
import com.phb.puhuibao.web.controller.UserAccountController;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class GaminoInterceptor implements HandlerInterceptor {
	private static final Log LOG = LogFactory.getLog(UserAccountController.class);

	@javax.annotation.Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	@javax.annotation.Resource(name = "mobileUserService")
	private IBaseService<MobileUser, String> mobileUserService;
	@javax.annotation.Resource(name = "mobileUserHexagramService")
	private IBaseService<MobileUserHexagram, String> mobileUserHexagramService;
	@javax.annotation.Resource(name = "hexagramCharmService")
	private IBaseService<HexagramCharm, String> hexagramCharmService;
	@javax.annotation.Resource(name = "muserFollowService")
	private IBaseService<MuserFollow, String> muserFollowService;
	@javax.annotation.Resource(name = "hexagramFameService")
	private IBaseService<HexagramFame, String> hexagramFameService;
	@javax.annotation.Resource(name = "hexagramAbilityService")
	private IBaseService<HexagramAbility, String> hexagramAbilityService;
	@javax.annotation.Resource(name = "hexagramCreditService")
	private IBaseService<HexagramCredit, String> hexagramCreditService;
	@javax.annotation.Resource(name = "hexagramFortuneService")
	private IBaseService<HexagramFortune, String> hexagramFortuneService;
	@javax.annotation.Resource(name = "userCardService")
	private IBaseService<UserCard, String> userCardService;
	@javax.annotation.Resource(name = "resourceService")
	private IBaseService<Resource, String> resourceService;
	@javax.annotation.Resource(name = "resourceOrderService")
	private IBaseService<ResourceOrder, String> resourceOrderService;
	
	@javax.annotation.Resource(name = "calculateFormulaService")
	private IBaseService<CalculateFormula, String> calculateFormulaService;
	
	@javax.annotation.Resource(name = "experienceValueService")
	private IBaseService<ExperienceValue, String> experienceValueService;

	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception exception)
			throws Exception {
		String muid = request.getParameter("muid");
		if (muid == null) {
			String resourceId = request.getParameter("resourceId");
			if (resourceId == null) {
				String orderId = request.getParameter("orderId");
				if (orderId != null) {
					ResourceOrder order = resourceOrderService.getById(orderId);
					if (order != null) {
						muid = order.getmUserId() + "";
					}
				}
			} else {
				Resource resource = resourceService.getById(resourceId);
				if (resource != null) {
					muid = resource.getmUserId() + "";
				}
			}
		}
		if (muid == null) {
			LOG.error("muid is null!");
			return;
		}

		// 魅力 照片
		if (((HandlerMethod) handler).getBean() instanceof MobileUserController && ((HandlerMethod) handler).getMethod().getName().equals("saveUserPictureWall")) {
			MobileUser user = mobileUserService.getById(muid);
			String pictures = user.getPicturewall();
			if (pictures != null) {
				double photoNumber;
				int count = pictures.split(",").length;
				if (count >= Constants.hexagramCharm_maxphotonumber) {
					photoNumber = 1;
				} else {
					photoNumber = count / Constants.hexagramCharm_maxphotonumber;
				}
				HexagramCharm entity = hexagramCharmService.getById(muid);
				if (entity == null) {
					entity = new HexagramCharm();
					entity.setmUserId(Integer.valueOf(muid));
					entity.setPhotoNumber(photoNumber);
					entity.setFansNumber(0.0);
					entity.setFriendsNumber(0.0);
					entity.setUserLevel(0.0);
					hexagramCharmService.save(entity);
					updateCharmScore(entity);
				} else if (new BigDecimal(entity.getPhotoNumber()).compareTo(new BigDecimal(photoNumber)) != 0) {
					entity.setPhotoNumber(photoNumber);
					updateCharmScore(entity);
					entity = new HexagramCharm();
					entity.setmUserId(Integer.valueOf(muid));
					entity.setPhotoNumber(photoNumber);
					hexagramCharmService.update(entity);
				}
			}
		}
		// 魅力 粉丝
		if (((HandlerMethod) handler).getBean() instanceof MuserFollowController && ((HandlerMethod) handler).getMethod().getName().equals("savefollow")) {
			String sql="SELECT count(1) FROM phb_muser_follow f LEFT JOIN phb_mobile_user u ON f.m_user_id = u.m_user_id WHERE f.follow_user = "+muid+" and f.m_user_id not in (select follow_user from phb_muser_follow where m_user_id="+muid+" and follow_isblocked ='1')";
			List<Map<String, Object>> result = this.jdbcTemplate.queryForList(sql);
			double fansNumber;
			long count = (Long) result.get(0).get("count(1)");
			if (count >= Constants.hexagramCharm_maxfannumber) {
				fansNumber = 1;
			} else {
				fansNumber = count / Constants.hexagramCharm_maxfannumber;
			}
			HexagramCharm entity = hexagramCharmService.getById(muid);
			if (entity == null) {
				entity = new HexagramCharm();
				entity.setmUserId(Integer.valueOf(muid));
				entity.setPhotoNumber(0.0);
				entity.setFansNumber(fansNumber);
				entity.setFriendsNumber(0.0);
				entity.setUserLevel(0.0);
				hexagramCharmService.save(entity);
				updateCharmScore(entity);
			} else if (new BigDecimal(entity.getFansNumber()).compareTo(new BigDecimal(fansNumber)) != 0) {
				entity.setFansNumber(fansNumber);
				updateCharmScore(entity);
				entity = new HexagramCharm();
				entity.setmUserId(Integer.valueOf(muid));
				entity.setFansNumber(fansNumber);
				hexagramCharmService.update(entity);
			}
		}		
		// 魅力 朋友
		if (((HandlerMethod) handler).getBean() instanceof MuserFollowController && ((HandlerMethod) handler).getMethod().getName().equals("savefriendship")) {
			Pager<MuserFollow> pager = new Pager<MuserFollow>();
			pager.setReload(true);
			pager.setCurrent(0);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("findfriends", true);
			params.put("currentuserId", muid);
			Pager<MuserFollow> p = muserFollowService.findByPager(pager, params);
			double friendsNumber;
			int count = p.getTotal();
			if (count >= Constants.hexagramCharm_maxfriendnumber) {
				friendsNumber = 1;
			} else {
				friendsNumber = count / Constants.hexagramCharm_maxfriendnumber;
			}
			HexagramCharm entity = hexagramCharmService.getById(muid);
			if (entity == null) {
				entity = new HexagramCharm();
				entity.setmUserId(Integer.valueOf(muid));
				entity.setPhotoNumber(0.0);
				entity.setFansNumber(0.0);
				entity.setFriendsNumber(friendsNumber);
				entity.setUserLevel(0.0);
				hexagramCharmService.save(entity);
				updateCharmScore(entity);
			} else if (new BigDecimal(entity.getFriendsNumber()).compareTo(new BigDecimal(friendsNumber)) != 0) {
				entity.setFriendsNumber(friendsNumber);
				updateCharmScore(entity);
				entity = new HexagramCharm();
				entity.setmUserId(Integer.valueOf(muid));
				entity.setFriendsNumber(friendsNumber);
				hexagramCharmService.update(entity);
			}
		}
		// 名望 多人任务
		if (((HandlerMethod) handler).getBean() instanceof ResourceController && ((HandlerMethod) handler).getMethod().getName().equals("save")) {
			String sql = "select count(1) from phb_resource where status>0 and number>1 and m_user_id=" + muid;
			List<Map<String, Object>> result = this.jdbcTemplate.queryForList(sql);
			double multipersonMession;
			long count = (Long) result.get(0).get("count(1)");
			if (count >= Constants.hexagramFame_multipersonmession) {
				multipersonMession = 1;
			} else {
				multipersonMession = count / Constants.hexagramFame_multipersonmession;
			}
			HexagramFame entity = hexagramFameService.getById(muid);
			if (entity == null) {
				entity = new HexagramFame();
				entity.setmUserId(Integer.valueOf(muid));
				entity.setValidMessions(0.0);
				entity.setEvaluateScore(0.0);
				entity.setMultipersonMession(multipersonMession);
				entity.setShareTimes(0.0);
				entity.setUserLevel(0.0);
				hexagramFameService.save(entity);
				updateReputationScore(entity);
			} else if (new BigDecimal(entity.getMultipersonMession()).compareTo(new BigDecimal(multipersonMession)) != 0) {
				entity.setMultipersonMession(multipersonMession);
				updateReputationScore(entity);
				entity = new HexagramFame();
				entity.setmUserId(Integer.valueOf(muid));
				entity.setMultipersonMession(multipersonMession);
				hexagramFameService.update(entity);
			}
		}
		// 名望 完成任务
		if (((HandlerMethod) handler).getBean() instanceof ResourceOrderController && ((HandlerMethod) handler).getMethod().getName().equals("finished")) {
			String sql = "select count(1) from phb_resource where status=3 and m_user_id=" + muid;
			List<Map<String, Object>> result = this.jdbcTemplate.queryForList(sql);
			double validMessions;
			long count = (Long) result.get(0).get("count(1)");
			sql = "select count(1) from phb_resource_order where status=2 and m_user_id=" + muid;
			result = this.jdbcTemplate.queryForList(sql);
			count += (Long) result.get(0).get("count(1)");
			if (count >= Constants.hexagramFame_validmessions) {
				validMessions = 1;
			} else {
				validMessions = count / Constants.hexagramFame_validmessions;
			}
			HexagramFame entity = hexagramFameService.getById(muid);
			if (entity == null) {
				entity = new HexagramFame();
				entity.setmUserId(Integer.valueOf(muid));
				entity.setValidMessions(validMessions);
				entity.setEvaluateScore(0.0);
				entity.setMultipersonMession(0.0);
				entity.setShareTimes(0.0);
				entity.setUserLevel(0.0);
				hexagramFameService.save(entity);
				updateReputationScore(entity);
			} else if (new BigDecimal(entity.getValidMessions()).compareTo(new BigDecimal(validMessions)) != 0) {
				entity.setValidMessions(validMessions);
				updateReputationScore(entity);
				entity = new HexagramFame();
				entity.setmUserId(Integer.valueOf(muid));
				entity.setValidMessions(validMessions);
				hexagramFameService.update(entity);
			}
		}
		// 名望 评价
		if (((HandlerMethod) handler).getBean() instanceof EvaluateController && ((HandlerMethod) handler).getMethod().getName().equals("save")) {
			String sql = "select sum(score) from phb_evaluate where m_user_id=" + muid;
			List<Map<String, Object>> result = this.jdbcTemplate.queryForList(sql);
			double evaluateScore;
			BigDecimal total = null;
			if (result.get(0).get("sum(score)") != null) {
				total = (BigDecimal) result.get(0).get("sum(score)");
			}
			if (total.intValue() >= Constants.hexagramFame_evaluatescore) {
				evaluateScore = 1;
			} else {
				evaluateScore = total.intValue() / Constants.hexagramFame_evaluatescore;
			}
			HexagramFame entity = hexagramFameService.getById(muid);
			if (entity == null) {
				entity = new HexagramFame();
				entity.setmUserId(Integer.valueOf(muid));
				entity.setValidMessions(0.0);
				entity.setEvaluateScore(evaluateScore);
				entity.setMultipersonMession(0.0);
				entity.setShareTimes(0.0);
				entity.setUserLevel(0.0);
				hexagramFameService.save(entity);
				updateReputationScore(entity);
			} else if (new BigDecimal(entity.getEvaluateScore()).compareTo(new BigDecimal(evaluateScore)) != 0) {
				entity.setEvaluateScore(evaluateScore);
				updateReputationScore(entity);
				entity = new HexagramFame();
				entity.setmUserId(Integer.valueOf(muid));
				entity.setEvaluateScore(evaluateScore);
				hexagramFameService.update(entity);
			}
		}
		// 能力 技能数
		if (((HandlerMethod) handler).getBean() instanceof ResourceOrderController && ((HandlerMethod) handler).getMethod().getName().equals("finished")) {
			String sql = "select count(1) from phb_resource where status=3 and category=0 and m_user_id=" + muid;
			List<Map<String, Object>> result = this.jdbcTemplate.queryForList(sql);
			double abilityMumber;
			long count = 0;
			if (result.get(0).get("sum(score)") != null) {
				count = (Long) result.get(0).get("sum(score)");
			}
			if (count >= Constants.hexagramAbility_abilitynumber) {
				abilityMumber = 1;
			} else {
				abilityMumber = count / Constants.hexagramAbility_abilitynumber;
			}
			HexagramAbility entity = hexagramAbilityService.getById(muid);
			if (entity == null) {
				entity = new HexagramAbility();
				entity.setmUserId(Integer.valueOf(muid));
				entity.setAbilityMumber(abilityMumber);
				entity.setFriendsNumber(0.0);
				entity.setMessioncompliteNumber(0.0);
				entity.setSpecialabilityNumber(0.0);
				entity.setUserLevel(0.0);
				hexagramAbilityService.save(entity);
				updateAbilityScore(entity);
			} else if (new BigDecimal(entity.getAbilityMumber()).compareTo(new BigDecimal(abilityMumber)) != 0) {
				entity.setAbilityMumber(abilityMumber);
				updateAbilityScore(entity);
				entity = new HexagramAbility();
				entity.setmUserId(Integer.valueOf(muid));
				entity.setAbilityMumber(abilityMumber);
				hexagramAbilityService.update(entity);
			}
		}
		// 能力 朋友
		if (((HandlerMethod) handler).getBean() instanceof MuserFollowController && ((HandlerMethod) handler).getMethod().getName().equals("savefriendship")) {
			Pager<MuserFollow> pager = new Pager<MuserFollow>();
			pager.setReload(true);
			pager.setCurrent(0);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("findfriends", true);
			params.put("currentuserId", muid);
			Pager<MuserFollow> p = muserFollowService.findByPager(pager, params);
			double friendsNumber;
			int count = p.getTotal();
			if (count >= Constants.hexagramAbility_friendsnumber) {
				friendsNumber = 1;
			} else {
				friendsNumber = count / Constants.hexagramAbility_friendsnumber;
			}
			HexagramAbility entity = hexagramAbilityService.getById(muid);
			if (entity == null) {
				entity = new HexagramAbility();
				entity.setmUserId(Integer.valueOf(muid));
				entity.setAbilityMumber(0.0);
				entity.setFriendsNumber(friendsNumber);
				entity.setMessioncompliteNumber(0.0);
				entity.setSpecialabilityNumber(0.0);
				entity.setUserLevel(0.0);
				hexagramAbilityService.save(entity);
				updateAbilityScore(entity);
			} else if (new BigDecimal(entity.getFriendsNumber()).compareTo(new BigDecimal(friendsNumber)) != 0) {
				entity.setFriendsNumber(friendsNumber);
				updateAbilityScore(entity);
				entity = new HexagramAbility();
				entity.setmUserId(Integer.valueOf(muid));
				entity.setFriendsNumber(friendsNumber);
				hexagramAbilityService.update(entity);
			}
		}
		// 能力 特殊技能数
		if (((HandlerMethod) handler).getBean() instanceof ResourceOrderController && ((HandlerMethod) handler).getMethod().getName().equals("finished")) {
			String sql = "select count(1) from (select sum(score) sum_score from (SELECT a.score,a.resource_id FROM phb_evaluate a,phb_resource b where a.to_user_id="  + muid + " and a.resource_id=b.resource_id and b.category=1) c group by resource_id)d where sum_score>=50";
			List<Map<String, Object>> result = this.jdbcTemplate.queryForList(sql);
			double specialabilityNumber;
			long count = (Long) result.get(0).get("count(1)");
			if (count >= Constants.hexagramAbility_specialabilitynumber) {
				specialabilityNumber = 1;
			} else {
				specialabilityNumber = count / Constants.hexagramAbility_specialabilitynumber;
			}
			HexagramAbility entity = hexagramAbilityService.getById(muid);
			if (entity == null) {
				entity = new HexagramAbility();
				entity.setmUserId(Integer.valueOf(muid));
				entity.setAbilityMumber(0.0);
				entity.setFriendsNumber(0.0);
				entity.setMessioncompliteNumber(0.0);
				entity.setSpecialabilityNumber(specialabilityNumber);
				entity.setUserLevel(0.0);
				hexagramAbilityService.save(entity);
				updateAbilityScore(entity);
			} else if (new BigDecimal(entity.getSpecialabilityNumber()).compareTo(new BigDecimal(specialabilityNumber)) != 0) {
				entity.setSpecialabilityNumber(specialabilityNumber);
				updateAbilityScore(entity);
				entity = new HexagramAbility();
				entity.setmUserId(Integer.valueOf(muid));
				entity.setSpecialabilityNumber(specialabilityNumber);
				hexagramAbilityService.update(entity);
			}
		}
		// 能力 发布任务领取任务数
		if ((((HandlerMethod) handler).getBean() instanceof ResourceController || ((HandlerMethod) handler).getBean() instanceof ResourceOrderController) && ((HandlerMethod) handler).getMethod().getName().equals("save")) {
			String sql = "select count(1) from phb_resource where status>0 and m_user_id=" + muid;
			List<Map<String, Object>> result = this.jdbcTemplate.queryForList(sql);
			double messioncompliteNumber;
			long count = (Long) result.get(0).get("count(1)");
			sql = "select count(1) from phb_resource_order where status>0 and m_user_id=" + muid;
			result = this.jdbcTemplate.queryForList(sql);
			count += (Long) result.get(0).get("count(1)");
			if (count >= Constants.hexagramAbility_messioncomplitenumber) {
				messioncompliteNumber = 1;
			} else {
				messioncompliteNumber = count / Constants.hexagramAbility_messioncomplitenumber;
			}
			HexagramAbility entity = hexagramAbilityService.getById(muid);
			if (entity == null) {
				entity = new HexagramAbility();
				entity.setmUserId(Integer.valueOf(muid));
				entity.setAbilityMumber(0.0);
				entity.setFriendsNumber(0.0);
				entity.setMessioncompliteNumber(messioncompliteNumber);
				entity.setSpecialabilityNumber(0.0);
				entity.setUserLevel(0.0);
				hexagramAbilityService.save(entity);
				updateAbilityScore(entity);
			} else if (new BigDecimal(entity.getMessioncompliteNumber()).compareTo(new BigDecimal(messioncompliteNumber)) != 0) {
				entity.setMessioncompliteNumber(messioncompliteNumber);
				updateAbilityScore(entity);
				entity = new HexagramAbility();
				entity.setmUserId(Integer.valueOf(muid));
				entity.setMessioncompliteNumber(messioncompliteNumber);
				hexagramAbilityService.update(entity);
			}
		}
		// 信用 评价
		if (((HandlerMethod) handler).getBean() instanceof EvaluateController && ((HandlerMethod) handler).getMethod().getName().equals("save")) {
			String sql = "select sum(score-2)/count(1)*4/3 average from phb_evaluate where m_user_id=" + muid;
			List<Map<String, Object>> result = this.jdbcTemplate.queryForList(sql);
			double resourceComment;
			double average = 0;
			if (result.get(0).get("average") != null) {
				average = ((BigDecimal) result.get(0).get("average")).doubleValue();
			}
			if (average >= Constants.hexagramCredit_usercomment) {
				resourceComment = 1;
			} else {
				resourceComment = average / Constants.hexagramCredit_usercomment;
			}
			HexagramCredit entity = hexagramCreditService.getById(muid);
			if (entity == null) {
				entity = new HexagramCredit();
				entity.setmUserId(Integer.valueOf(muid));
				entity.setIdAuthentication(0.0);
				entity.setResourceComment(resourceComment);
				entity.setUserLevel(0.0);
				hexagramCreditService.save(entity);
				updateCreditScore(entity);
			} else if (new BigDecimal(entity.getResourceComment()).compareTo(new BigDecimal(resourceComment)) != 0) {
				entity.setResourceComment(resourceComment);
				updateCreditScore(entity);
				entity = new HexagramCredit();
				entity.setmUserId(Integer.valueOf(muid));
				entity.setResourceComment(resourceComment);
				hexagramCreditService.update(entity);
			}
		}
		// 信用 实名认证
		if (((HandlerMethod) handler).getBean() instanceof MobileUserController && (((HandlerMethod) handler).getMethod().getName().equals("certificationForAndroid") || ((HandlerMethod) handler).getMethod().getName().equals("certificationForIOS"))) {
			MobileUser user = mobileUserService.getById(muid);
			HexagramCredit entity = hexagramCreditService.getById(muid);
			if (entity == null) {
				entity = new HexagramCredit();
				entity.setmUserId(Integer.valueOf(muid));
				entity.setIdAuthentication((double) user.getIsAudit());
				entity.setResourceComment(0.0);
				entity.setUserLevel(0.0);
				hexagramCreditService.save(entity);
				updateCreditScore(entity);
			} else if (new BigDecimal(entity.getIdAuthentication()).compareTo(new BigDecimal(user.getIsAudit())) != 0) {
				entity.setIdAuthentication((double) user.getIsAudit());
				updateCreditScore(entity);
				entity = new HexagramCredit();
				entity.setmUserId(Integer.valueOf(muid));
				entity.setIdAuthentication((double) user.getIsAudit());
				hexagramCreditService.update(entity);
			}
		}
		// 财富 银行卡绑定
		if (((HandlerMethod) handler).getBean() instanceof UserAccountController && ((HandlerMethod) handler).getMethod().getName().equals("notify")) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("mUserId", muid);
			UserCard card = userCardService.unique(params);
			double isBind = 0;
			if (card != null) {
				isBind = 1;
			}
			HexagramFortune entity = hexagramFortuneService.getById(muid);
			if (entity == null) {
				entity = new HexagramFortune();
				entity.setmUserId(Integer.valueOf(muid));
				entity.setDepositValue(0.0);
				entity.setInvestValue(0.0);
				entity.setIsBind(isBind);
				entity.setMessionPublish(0.0);
				hexagramFortuneService.save(entity);
				updateFortuneScore(entity);
			} else if (new BigDecimal(entity.getIsBind()).compareTo(new BigDecimal(isBind)) != 0) {
				entity.setIsBind(isBind);
				updateFortuneScore(entity);
				entity = new HexagramFortune();
				entity.setmUserId(Integer.valueOf(muid));
				entity.setIsBind(isBind);
				hexagramFortuneService.update(entity);
			}
		}
		// 财富 任务金额
		if (((HandlerMethod) handler).getBean() instanceof ResourceController && ((HandlerMethod) handler).getMethod().getName().equals("save")) {
			String sql = "select sum(price*number) total from phb_resource where status>0 and m_user_id=" + muid;
			List<Map<String, Object>> result = this.jdbcTemplate.queryForList(sql);
			double messionPublish = 0;
			BigDecimal total = new BigDecimal(0);
			if (result.get(0).get("total") != null) {
				total = (BigDecimal) result.get(0).get("total");
			}
			if (total.compareTo(new BigDecimal(10000)) >= 0) {
				messionPublish = 1;
			} else if (total.compareTo(new BigDecimal(5000)) >= 0) {
				messionPublish = 1.5 / Constants.hexagramFortune_messionpublish;
			} else if (total.compareTo(new BigDecimal(2000)) >= 0) {
				messionPublish = 1 / Constants.hexagramFortune_messionpublish;
			} else if (total.compareTo(new BigDecimal(1000)) >= 0) {
				messionPublish = 0.5 / Constants.hexagramFortune_messionpublish;
			}
			HexagramFortune entity = hexagramFortuneService.getById(muid);
			if (entity == null) {
				entity = new HexagramFortune();
				entity.setmUserId(Integer.valueOf(muid));
				entity.setDepositValue(0.0);
				entity.setInvestValue(0.0);
				entity.setIsBind(0.0);
				entity.setMessionPublish(messionPublish);
				hexagramFortuneService.save(entity);
				updateFortuneScore(entity);
			} else if (new BigDecimal(entity.getMessionPublish()).compareTo(new BigDecimal(messionPublish)) != 0) {
				entity.setMessionPublish(messionPublish);
				updateFortuneScore(entity);
				entity = new HexagramFortune();
				entity.setmUserId(Integer.valueOf(muid));
				entity.setMessionPublish(messionPublish);
				hexagramFortuneService.update(entity);
			}
		}
		// 财富 余额
		if ((((HandlerMethod) handler).getBean() instanceof UserAccountController
				&& (((HandlerMethod) handler).getMethod().getName().equals("notify")
						|| ((HandlerMethod) handler).getMethod().getName().equals("withdraw")
//						|| ((HandlerMethod) handler).getMethod().getName().equals("webChatNotifyUrl")
//						|| ((HandlerMethod) handler).getMethod().getName().equals("alipayNotifyUrl")
						))
				|| (((HandlerMethod) handler).getBean() instanceof ResourceOrderController && ((HandlerMethod) handler).getMethod().getName().equals("pay"))
				|| (((HandlerMethod) handler).getBean() instanceof ResourceOrderController && ((HandlerMethod) handler).getMethod().getName().equals("enrolandpay"))
				|| (((HandlerMethod) handler).getBean() instanceof ResourceController && ((HandlerMethod) handler).getMethod().getName().equals("pay"))
				) {
			MobileUser user = mobileUserService.getById(muid);
			BigDecimal balance = new BigDecimal(user.getmUserMoney() - user.getFrozenMoney());
			double depositValue = 0;
			if (balance.compareTo(new BigDecimal(50000)) >= 0) {
				depositValue = 1;
			} else if (balance.compareTo(new BigDecimal(20000)) >= 0) {
				depositValue = 4 / Constants.hexagramFortune_depositvalue;
			} else if (balance.compareTo(new BigDecimal(10000)) >= 0) {
				depositValue = 3.5 / Constants.hexagramFortune_depositvalue;
			} else if (balance.compareTo(new BigDecimal(5000)) >= 0) {
				depositValue = 3 / Constants.hexagramFortune_depositvalue;
			} else if (balance.compareTo(new BigDecimal(3000)) >= 0) {
				depositValue = 2.5 / Constants.hexagramFortune_depositvalue;
			} else if (balance.compareTo(new BigDecimal(2000)) >= 0) {
				depositValue = 2 / Constants.hexagramFortune_depositvalue;
			} else if (balance.compareTo(new BigDecimal(1000)) >= 0) {
				depositValue = 1.5 / Constants.hexagramFortune_depositvalue;
			} else if (balance.compareTo(new BigDecimal(500)) >= 0) {
				depositValue = 1 / Constants.hexagramFortune_depositvalue;
			}
			HexagramFortune entity = hexagramFortuneService.getById(muid);
			if (entity == null) {
				entity = new HexagramFortune();
				entity.setmUserId(Integer.valueOf(muid));
				entity.setDepositValue(depositValue);
				entity.setInvestValue(0.0);
				entity.setIsBind(0.0);
				entity.setMessionPublish(0.0);
				hexagramFortuneService.save(entity);
				updateFortuneScore(entity);
			} else if (new BigDecimal(entity.getDepositValue()).compareTo(new BigDecimal(depositValue)) != 0) {
				entity.setDepositValue(depositValue);
				updateFortuneScore(entity);
				entity = new HexagramFortune();
				entity.setmUserId(Integer.valueOf(muid));
				entity.setDepositValue(depositValue);
				hexagramFortuneService.update(entity);
			}
		}
		// 财富 理财金
		if (((HandlerMethod) handler).getBean() instanceof ResourceController && ((HandlerMethod) handler).getMethod().getName().equals("save")) {
			String sql = "SELECT sum(investment_amount) FROM phb_muser_investment where m_user_id=" + muid;
			List<Map<String, Object>> result = this.jdbcTemplate.queryForList(sql);
			double investValue = 0;
			BigDecimal total = new BigDecimal(0);
			if (result.get(0).get("total") != null) {
				total = (BigDecimal) result.get(0).get("total");
			}
			sql = "SELECT sum(investment_amount) FROM phb_item_investment where m_user_id=" + muid;
			result = this.jdbcTemplate.queryForList(sql);
			if (result.get(0).get("total") != null) {
				total = total.add((BigDecimal) result.get(0).get("total"));
			}
			if (total.compareTo(new BigDecimal(100000)) >= 0) {
				investValue = 1;
			} else if (total.compareTo(new BigDecimal(50000)) >= 0) {
				investValue = 2.5 / Constants.hexagramFortune_investvalue;
			} else if (total.compareTo(new BigDecimal(20000)) >= 0) {
				investValue = 2 / Constants.hexagramFortune_investvalue;
			} else if (total.compareTo(new BigDecimal(10000)) >= 0) {
				investValue = 1.5 / Constants.hexagramFortune_investvalue;
			} else if (total.compareTo(new BigDecimal(5000)) >= 0) {
				investValue = 1 / Constants.hexagramFortune_investvalue;
			} else if (total.compareTo(new BigDecimal(500)) >= 0) {
				investValue = 0.5 / Constants.hexagramFortune_investvalue;
			}
			HexagramFortune entity = hexagramFortuneService.getById(muid);
			if (entity == null) {
				entity = new HexagramFortune();
				entity.setmUserId(Integer.valueOf(muid));
				entity.setDepositValue(0.0);
				entity.setInvestValue(investValue);
				entity.setIsBind(0.0);
				entity.setMessionPublish(0.0);
				hexagramFortuneService.save(entity);
				updateFortuneScore(entity);
			} else if (new BigDecimal(entity.getInvestValue()).compareTo(new BigDecimal(investValue)) != 0) {
				entity.setInvestValue(investValue);
				updateFortuneScore(entity);
				entity = new HexagramFortune();
				entity.setmUserId(Integer.valueOf(muid));
				entity.setInvestValue(investValue);
				hexagramFortuneService.update(entity);
			}
		}
		
		
		
		
		
		//等级维护--第一次上传主页头像
		if (((HandlerMethod) handler).getBean() instanceof MobileUserController && ((HandlerMethod) handler).getMethod().getName().equals("saveUserProfilePic")) {
			double userexperiencevaule   =  getTotalValueByUserId(muid);
			String sql = "select photo from phb_mobile_user where m_user_id=" + muid ;
			Map<String, Object> usercheck   = this.jdbcTemplate.queryForMap(sql);
			if( StringUtils.isEmpty((String)usercheck.get("photo"))){//第一次上传头像
				updateByFormula(muid,userexperiencevaule,"uploadphoto");
			}
		}
		
		//等级维护--绑定银行卡     注释掉是因为暂时无法测试   无法绑卡
//		if (((HandlerMethod) handler).getBean() instanceof UserAccountController && ((HandlerMethod) handler).getMethod().getName().equals("notify")) {
//			
//			String reqStr = LLPayUtil.readReqStr(request);
//			PayDataBean payDataBean = JSON.parseObject(reqStr, PayDataBean.class);
//	        if (!"SUCCESS".equals(payDataBean.getResult_pay())) {
//	        	return;
//	        }
//			Map<String,Object> params = new HashMap<String,Object>();
//			String identityid = payDataBean.getInfo_order();
//			if (identityid.indexOf(",") > 0) {
//				String[] info_order = identityid.split(",");
//				identityid = info_order[0];
//			}
//			
//			params = new HashMap<String,Object>();
//			params.put("mUserTel", identityid);
//			MobileUser user = mobileUserService.unique(params);
//			double userexperiencevaule   =  getTotalValueByUserId(user.getmUserId());
//			String sql = "select bank_account from phb_muser_card where m_user_id=" + user.getmUserId();
//			Map<String, Object> usercheck = this.jdbcTemplate.queryForMap(sql);
//			if(usercheck == null){//第一次绑定银行卡
//				updateByFormula(user.getmUserId(),userexperiencevaule,"bindbankcard");
//			}
//		}
		
		//等级维护--发布任务
		if (((HandlerMethod) handler).getBean() instanceof ResourceController && ((HandlerMethod) handler).getMethod().getName().equals("save")) {
			double userexperiencevaule   =  getTotalValueByUserId(muid);
			String sql = "select m_user_id from phb_resource where m_user_id=" + muid;
			List <Map<String, Object>> list = this.jdbcTemplate.queryForList(sql);
			if(list.size()==1){//第一次发任务的时候,此处的list只是1,大于1说明有以前创建的任务
				updateByFormula(muid,userexperiencevaule,"releasetask");
			}
		}
		
		//等级维护--接受任务
		if (((HandlerMethod) handler).getBean() instanceof ResourceOrderController && ((HandlerMethod) handler).getMethod().getName().equals("save")) {
			double userexperiencevaule   =  getTotalValueByUserId(muid);
			String sql = "select m_user_id from phb_resource_order where m_user_id=" + muid;
			List <Map<String, Object>> list = this.jdbcTemplate.queryForList(sql);
			if(list.size()==1){//第一次接任务
				updateByFormula(muid,userexperiencevaule,"accepttask");
			}
		}
		//等级维护--评价任务
		if (((HandlerMethod) handler).getBean() instanceof EvaluateController && ((HandlerMethod) handler).getMethod().getName().equals("save")) {
			double userexperiencevaule   =  getTotalValueByUserId(muid);
			String sql =  "select m_user_id from phb_evaluate where m_user_id=" + muid;
			List <Map<String, Object>> list = this.jdbcTemplate.queryForList(sql);
			if(list.size()==1){//第一次对任务发表评论
				updateByFormula(muid,userexperiencevaule,"commenttask");
			}

		}
		//等级维护--实名认证
		if ((((HandlerMethod) handler).getBean() instanceof MobileUserController && ((HandlerMethod) handler).getMethod().getName().equals("certificationForAndroid"))
			|| ((HandlerMethod) handler).getBean() instanceof MobileUserController && ((HandlerMethod) handler).getMethod().getName().equals("certificationForIOS") ) {
			double userexperiencevaule   =  getTotalValueByUserId(muid);
            updateByFormula(muid,userexperiencevaule,"attestname");
		}
	}

	private void updateFortuneScore(HexagramFortune entity) {
		double fortuneScore = entity.getDepositValue() * 4.5;
		fortuneScore += entity.getInvestValue() * 3;
		fortuneScore += entity.getMessionPublish() * 2;
		fortuneScore += entity.getIsBind() * 0.5;
		fortuneScore = fortuneScore / Constants.GAMINO_FORTUNE_SCORE;
		MobileUserHexagram gamino = mobileUserHexagramService.getById(entity.getmUserId() + "");
		MobileUserHexagram updateGamino = new MobileUserHexagram();
		updateGamino.setmUserId(gamino.getmUserId());
		updateGamino.setFortuneScore(fortuneScore);
		mobileUserHexagramService.update(updateGamino);
	}

	private void updateCreditScore(HexagramCredit entity) {
		double creditScore = entity.getIdAuthentication();
		creditScore += entity.getResourceComment() * 4;
		creditScore += entity.getUserLevel() * 5;
		creditScore = creditScore / Constants.GAMINO_CREDIT_SCORE;
		MobileUserHexagram gamino = mobileUserHexagramService.getById(entity.getmUserId() + "");
		MobileUserHexagram updateGamino = new MobileUserHexagram();
		updateGamino.setmUserId(gamino.getmUserId());
		updateGamino.setCreditScore(creditScore);
		mobileUserHexagramService.update(updateGamino);
	}

	private void updateAbilityScore(HexagramAbility entity) {
		double abilityScore = entity.getAbilityMumber();
		abilityScore += entity.getFriendsNumber();
		abilityScore += entity.getMessioncompliteNumber() * 2;
		abilityScore += entity.getSpecialabilityNumber() * 2;
		abilityScore += entity.getUserLevel() * 4;
		abilityScore = abilityScore / Constants.GAMINO_ABILITY_SCORE;
		MobileUserHexagram gamino = mobileUserHexagramService.getById(entity.getmUserId() + "");
		MobileUserHexagram updateGamino = new MobileUserHexagram();
		updateGamino.setmUserId(gamino.getmUserId());
		updateGamino.setAbilityScore(abilityScore);
		mobileUserHexagramService.update(updateGamino);
	}

	private void updateCharmScore(HexagramCharm entity) {
		double charmScore = entity.getFansNumber() * 4;
		charmScore += entity.getFriendsNumber() *2;
		charmScore += entity.getPhotoNumber();
		charmScore += entity.getUserLevel() * 3;
		charmScore = charmScore / Constants.GAMINO_CHARM_SCORE;
		MobileUserHexagram gamino = mobileUserHexagramService.getById(entity.getmUserId() + "");
		MobileUserHexagram updateGamino = new MobileUserHexagram();
		updateGamino.setmUserId(gamino.getmUserId());
		updateGamino.setCharmScore(charmScore);
		mobileUserHexagramService.update(updateGamino);
	}

	private void updateReputationScore(HexagramFame entity) {
		double reputationScore = entity.getEvaluateScore() * 2;
		reputationScore += entity.getMultipersonMession() * 2;
		reputationScore += entity.getShareTimes() * 2;
		reputationScore += entity.getUserLevel() * 3;
		reputationScore += entity.getValidMessions();
		reputationScore = reputationScore / Constants.GAMINO_REPUTATION_SCORE;
		MobileUserHexagram gamino = mobileUserHexagramService.getById(entity.getmUserId() + "");
		MobileUserHexagram updateGamino = new MobileUserHexagram();
		updateGamino.setmUserId(gamino.getmUserId());
		updateGamino.setReputationScore(reputationScore);
		mobileUserHexagramService.update(updateGamino);
	}
	
	
	
 
	
	
	/*
	 *  获得用户经验值
	*/
	public double getTotalValueByUserId(String  muid) {
		String sql = "select sum(experience_value) experienceValue from phb_muser_experiencevalue where m_user_id=" + muid;
		Map<String, Object> experience = this.jdbcTemplate.queryForMap(sql);
		double sumvalue=0;
		if(experience.get("experienceValue")==null){
			
		}else{
			sumvalue = Double.parseDouble(experience.get("experienceValue").toString());
		}
		return sumvalue;
	}
	
	/*
	 *  获得业务公式
	*/
	
	public String getFormula(String formulaId) {
		try {
			CalculateFormula formula = calculateFormulaService.getById(formulaId);
			if(formula==null){
				return   "";
			}else{
				return   formula.getFormulaText();
			}
		} catch (Exception e) {
			return "";
		} 
	}
	
	public void updateByFormula(String id,double oldvalue,String type) {
		int muid = Integer.valueOf(id);
		double addexperience=0;
		double newvaule=0;
		String formula = getFormula(type);//一个值
		if(!StringUtils.isEmpty(formula)){
			addexperience = Double.parseDouble(formula);
		}
		newvaule = oldvalue +  addexperience;
		ExperienceValue ev = new ExperienceValue();
		ev.setExperienceType(type);
		ev.setExperienceValue(addexperience);
		ev.setmUserId(muid);
		experienceValueService.save(ev);//新插入一条经验记录
		
		int newlevel = ifLevelUp(oldvalue,newvaule);
		if(newlevel>0){// 此次升级导致用户级别变动
			MobileUser muser = new MobileUser();
			muser.setmUserId(muid);
			muser.setLevel(newlevel);
			mobileUserService.update(muser);
			
			//更新六芒星
			HexagramCharm charm = new HexagramCharm();
			charm.setmUserId(muid);
			charm.setUserLevel(newlevel/1.0);
			hexagramCharmService.update(charm);
			
			HexagramAbility ha = new HexagramAbility();
			ha.setmUserId(muid);
			ha.setUserLevel(newlevel/1.0);
			hexagramAbilityService.update(ha);
			
			HexagramCredit hc = new HexagramCredit();
			hc.setmUserId(muid);
			hc.setUserLevel(newlevel/1.0);
			hexagramCreditService.update(hc);
			
			HexagramFame hf = new HexagramFame();
			hf.setmUserId(muid);
			hf.setUserLevel(newlevel/1.0);
			hexagramFameService.update(hf);
 
			// lyb
			double charmScore = charm.getFansNumber() * Constants.hexagramCharm_maxfannumber;
			charmScore += charm.getFriendsNumber() * Constants.hexagramCharm_maxfriendnumber;
			charmScore += charm.getPhotoNumber() * Constants.hexagramCharm_maxphotonumber;
			charmScore += charm.getUserLevel();
			charmScore = charmScore / Constants.GAMINO_CHARM_SCORE;
			double abilityScore = ha.getAbilityMumber() * Constants.hexagramAbility_abilitynumber;
			abilityScore += ha.getFriendsNumber() * Constants.hexagramAbility_friendsnumber;
			abilityScore += ha.getMessioncompliteNumber() * Constants.hexagramAbility_messioncomplitenumber;
			abilityScore += ha.getSpecialabilityNumber() * Constants.hexagramAbility_specialabilitynumber;
			abilityScore += ha.getUserLevel();
			abilityScore = abilityScore / Constants.GAMINO_ABILITY_SCORE;
			double creditScore = hc.getIdAuthentication();
			creditScore += hc.getResourceComment() * Constants.hexagramCredit_usercomment;
			creditScore += hc.getUserLevel();
			creditScore = creditScore / Constants.GAMINO_CREDIT_SCORE;
			double reputationScore = hf.getEvaluateScore() * Constants.hexagramFame_evaluatescore;
			reputationScore += hf.getMultipersonMession() * Constants.hexagramFame_multipersonmession;
			reputationScore += hf.getShareTimes() * Constants.hexagramFame_sharetimes;
			reputationScore += hf.getUserLevel();
			reputationScore += hf.getValidMessions() * Constants.hexagramFame_validmessions;
			reputationScore = reputationScore / Constants.GAMINO_REPUTATION_SCORE;

			MobileUserHexagram gamino = mobileUserHexagramService.getById(muid + "");
			gamino.setCharmScore(charmScore);
			gamino.setAbilityScore(abilityScore);
			gamino.setCreditScore(creditScore);
			gamino.setReputationScore(reputationScore);
			mobileUserHexagramService.update(gamino);
		}
	}
	
 
	
	/*
	 *  检查此次新增经验是否导致升级,如果升级了,则需要修改用户级别,用户的六芒星信息
	*/
	
	public int ifLevelUp(double oldvalue,double newvalue) {
		
	    int oldValueLevel=0;
	    int newValueLevel=0;
	    
		String formula = getFormula("level");//一个值
		//formula = "0,20,0|20,50,1|50,10,2|100,200,3|200,500,4|500,800,5|800,1200,6|1200,1750,7|1750,2500,8|2500,4000,9|4000,9999999999,10";
		String [] formulaarr = formula.split("\\|");
		for(int i =0;i<formulaarr.length;i++){
			String[] splited = formulaarr[i].split(",");
			if(oldvalue>= Double.parseDouble(splited[0]) && oldvalue< Double.parseDouble(splited[1])){
				oldValueLevel = Integer.parseInt(splited[2]);
			}
			if(newvalue>= Double.parseDouble(splited[0]) && newvalue< Double.parseDouble(splited[1])){
				newValueLevel = Integer.parseInt(splited[2]);
			}
		}
		if(oldValueLevel==newValueLevel){//级别没有变化
			return 0;
		}else{//新的经验值所处的等级和旧的不一样,说明此次经验升级导致了用户等级的提升 返回新的等级
			return newValueLevel;
		}
		
	 
	}	
	
	
	
	
	
	
	

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response,
			Object handler, ModelAndView modelAndView) throws Exception {
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
			Object handler) throws Exception {
		// TODO Auto-generated method stub
		return true;
	}

}
