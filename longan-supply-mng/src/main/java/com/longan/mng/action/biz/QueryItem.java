package com.longan.mng.action.biz;

import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import com.longan.biz.core.BizOrderService;
import com.longan.biz.dataobject.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.longan.biz.core.ItemService;
import com.longan.biz.domain.Result;
import com.longan.biz.pojo.PddItemStatus;
import com.longan.biz.utils.Constants;
import com.longan.biz.utils.Utils;
import com.longan.mng.action.common.BaseController;

@Controller
public class QueryItem extends BaseController {
    private static final Long pddUserId = Utils.getLong("pdd.id");

    @Resource
    private ItemService itemService;

    @RequestMapping("biz/queryItem")
    public String doQuery(@ModelAttribute("itemQuery") ItemQuery itemQuery, Model model, HttpSession session) {

	List<Item> countList = new ArrayList();
	List<Item> resultList = new ArrayList();
	Result<List<Item>> result = null;
		// 业务 权限判断
		UserInfo userInfo = super.getUserInfo(session);
		if (!checkBizAuth(itemQuery.getBizId(), userInfo)) {
			return "error/autherror";
		}

		model.addAttribute("bizName", Constants.BIZ_MAP.get(itemQuery.getBizId()));
		model.addAttribute("userInfo", userInfo);
		model.addAttribute("hasCombine", hasCombine(itemQuery.getBizId()));
		if(StringUtils.hasText(itemQuery.getIds())) {
			itemQuery.setIdsList(Arrays.asList(itemQuery.getIds().split(",")));
		}
		if (itemQuery.getSalesAreas() == null) {
			result = itemService.queryItemList(itemQuery);
			if (result.isSuccess()) {
				List<Item> itemList = result.getModule();
				// 代理商显示价格特殊处理//下游代理商处理
				if (userInfo.isDownStreamUser()) {
					//
					AcctInfo acctInfo = localCachedService.getAcctInfoNot4Trade(userInfo.getAcctId());
					if (itemList != null) {
						for (Item item : itemList) {
							//获取商品的状态（）
							Result<Integer> priceResult = itemService.getSalesPrice(item, acctInfo);
							if (priceResult.isSuccess()) {
								//设置商品状态
								item.setItemSalesPrice(priceResult.getModule());
							}
						}
					}
				}

				if (itemList != null) {
					for (Item item : itemList) {
						//拼接操作
						setItemArea(item);
						if (item.getUserId() != null && item.getUserId() == pddUserId.longValue()) {
							//添加一个有过期时间的map
							item.setPddStatus(PddItemStatus.map.get(item.getId()));
						}
					}
				}
				model.addAttribute("itemList", result.getModule());
//		} else {
//			super.setError(model, result.getResultMsg());
		}

			} else {
				for (int i = 0; i < itemQuery.getSalesAreas().size(); i++) {
					itemQuery.setSalesArea(itemQuery.getSalesAreas().get(i));
				 	result = itemService.queryItemList(itemQuery);
					if (result.isSuccess()) {
						List<Item> itemList = result.getModule();
						// 代理商显示价格特殊处理//下游代理商处理
						if (userInfo.isDownStreamUser()) {
							//
							AcctInfo acctInfo = localCachedService.getAcctInfoNot4Trade(userInfo.getAcctId());
							if (itemList != null) {
								for (Item item : itemList) {
									//获取商品的状态（）
									Result<Integer> priceResult = itemService.getSalesPrice(item, acctInfo);
									if (priceResult.isSuccess()) {
										//设置商品状态
										item.setItemSalesPrice(priceResult.getModule());
									}
								}
							}
						}

						if (itemList != null) {
							for (Item item : itemList) {
								//拼接操作
								setItemArea(item);
								if (item.getUserId() != null && item.getUserId() == pddUserId.longValue()) {
									//添加一个有过期时间的map
									item.setPddStatus(PddItemStatus.map.get(item.getId()));
								}
							}
						}
					}
					for (Item item : result.getModule()) {
						countList.add(item);
					}
				}
				itemQuery.setTotalItem(countList.size());
				itemQuery.setPageSize(countList.size());
				//itemQuery.setPageSize(countList.size());
				result.setModule(countList);
			model.addAttribute("itemList", result.getModule());
			}
			return "biz/queryItem";
		}
	@RequestMapping("biz/queryItemUp")
	public String doUpQuery(@ModelAttribute("itemQuery") ItemQuery itemQuery, Model model, HttpSession session) {
		// 业务 权限判断
		UserInfo userInfo = super.getUserInfo(session);
		if (!checkBizAuth(itemQuery.getBizId(), userInfo)) {
			return "error/autherror";
		}
		model.addAttribute("bizName", Constants.BIZ_MAP.get(itemQuery.getBizId()));
		model.addAttribute("userInfo", userInfo);
		model.addAttribute("hasCombine", hasCombine(itemQuery.getBizId()));
		String[] split = itemQuery.getIds().split(",");
		itemQuery.setIdsList(Arrays.asList(split));
		Result<List<Item>> result = itemService.queryItemUpList(itemQuery);
			if (result.isSuccess()) {
				List<Item> itemList = result.getModule();
				// 代理商显示价格特殊处理//下游代理商处理
				if (userInfo.isDownStreamUser()) {
					//
					AcctInfo acctInfo = localCachedService.getAcctInfoNot4Trade(userInfo.getAcctId());
					if (itemList != null) {
						for (Item item : itemList) {
							//获取商品的状态（）
							Result<Integer> priceResult = itemService.getSalesPrice(item, acctInfo);
							if (priceResult.isSuccess()) {
								//设置商品状态
								item.setItemSalesPrice(priceResult.getModule());
							}
						}
					}
				}
				if (itemList != null) {
					for (Item item : itemList) {
						//拼接操作
						setItemArea(item);
						if (item.getUserId() != null && item.getUserId() == pddUserId.longValue()) {
							//添加一个有过期时间的map
							item.setPddStatus(PddItemStatus.map.get(item.getId()));
						}
					}
				}

				model.addAttribute("itemList", result.getModule());
			}else {
				super.setError(model, result.getResultMsg());
			}
		return "biz/queryItem";
	}


	private Boolean hasCombine(int bizId) {
    	//200 201 202
	return bizId == Constants.BizInfo.CODE_PHONE_UNICOM || bizId == Constants.BizInfo.CODE_PHONE_MOBILE
		|| bizId == Constants.BizInfo.CODE_PHONE_TELECOM;
    }
	//请求前操作的添加
    @ModelAttribute("statusList")
    public Map<Integer, String> statusList() {
	Map<Integer, String> map = new HashMap<Integer, String>();
	map.put(Constants.Item.STATUS_DEL, Constants.Item.STATUS_DEL_DESC);
	map.put(Constants.Item.STATUS_DOWN, Constants.Item.STATUS_DOWN_DESC);
	map.put(Constants.Item.STATUS_INIT, Constants.Item.STATUS_INIT_DESC);
	map.put(Constants.Item.STATUS_UP, Constants.Item.STATUS_UP_DESC);
	return map;
    }

    @ModelAttribute("provinceList")
	//省份集合
    public Map<String, AreaInfo> provinceList() {
	return localCachedService.getProvinceMap();
    }
}
