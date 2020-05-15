package com.longan.mng.action.biz;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.longan.biz.core.BizOrderService;
import com.longan.biz.dataobject.*;
import com.longan.mng.quartz.TestJobTaskDemo;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.longan.biz.core.ItemService;
import com.longan.biz.domain.Result;
import com.longan.biz.func.PddGoodsService;
import com.longan.biz.utils.BigDecimalUtils;
import com.longan.biz.utils.Constants;
import com.longan.biz.utils.OperLogUtils;
import com.longan.mng.action.common.BaseController;
import com.longan.mng.form.ItemForm;
import com.longan.mng.form.UpDownForm;

@Controller
public class ItemDeal extends BaseController {
    @Resource
    private ItemService itemService;

    @Resource
    private Validator validator;
    @Resource
    private PddGoodsService pddGoodsService;
	//上架
    @RequestMapping(value = "biz/itemDeal", params = "requestType=up")
    public String onRequestUp(@RequestParam("itemId") Integer itemId, @RequestParam("bizId") Integer bizId, HttpSession session,
	    Model model) {
	UserInfo userInfo = getUserInfo(session);
	// 业务权限判断
	if (!checkBizAuth(bizId, userInfo)) {
	    return "error/autherror";
	}

	Result<Item> itemResult = itemService.getItem(itemId);
	Item older;
	if (!itemResult.isSuccess()) {
	    alertError(model, itemResult.getResultMsg());
	    return "biz/queryItem";
	} else {
	    if (itemResult.getModule() == null) {
		alertError(model, "商品为空");
		return "biz/queryItem";
	    }
	    older = itemResult.getModule();
	}

	if (older.isCombine() && !older.canUpCombine()) {
	    alertError(model, "拆分商品未关联");
	    return "biz/queryItem";
	}

	logger.warn(userInfo.getUserName() + "执行上架操作 商品 id : " + itemId);
	Result<Boolean> result = itemService.upItem(itemId);
	if (result.isSuccess() && result.getModule()) {
	    setOperationLog(itemId, bizId, "(上架)", older, userInfo, session);
	    alertSuccess(model, "queryItem.do?bizId=" + bizId + "&id=" + itemId);
	} else {
	    alertError(model, result.getResultMsg());
	}
	return "biz/queryItem";
    }
	//下架操作
    @RequestMapping(value = "biz/itemDeal", params = "requestType=down")
    public String onRequestDown(@RequestParam("itemId") Integer itemId, @RequestParam("bizId") Integer bizId,
	    HttpSession session, Model model) {
	UserInfo userInfo = getUserInfo(session);
	// 业务权限判断
	if (!checkBizAuth(bizId, userInfo)) {
	    return "error/autherror";
	}

	Result<Item> itemResult = itemService.getItem(itemId);
	Item older;
	if (!itemResult.isSuccess()) {
	    alertError(model, itemResult.getResultMsg());
	    return "biz/queryItem";
	} else {
	    if (itemResult.getModule() == null) {
		alertError(model, "商品为空");
		return "biz/queryItem";
	    }
	    older = itemResult.getModule();
	}

	logger.warn(userInfo.getUserName() + "执行下架操作 商品 id : " + itemId);
	Result<Boolean> result = itemService.downItem(itemId);
	if (result.isSuccess() && result.getModule()) {
	    setOperationLog(itemId, bizId, "(下架)", older, userInfo, session);
	    alertSuccess(model, "queryItem.do?bizId=" + bizId + "&id=" + itemId);
	} else {
	    alertError(model, result.getResultMsg());
	}
	return "biz/queryItem";
    }

    @RequestMapping(value = "biz/itemDeal", params = "requestType=combine")
    public String onRequestCombine(@RequestParam("itemId") Integer itemId, @RequestParam("bizId") Integer bizId,
	    HttpSession session, Model model) {
	UserInfo userInfo = getUserInfo(session);
	// 业务权限判断
	if (!checkBizAuth(bizId, userInfo)) {
	    return "error/autherror";
	}

	Result<Boolean> checkResult = itemService.hasCombinedItem(itemId);
	if (!checkResult.isSuccess() || checkResult.getModule()) {
	    alertError(model, checkResult.getResultMsg());
	    return "biz/queryItem";
	}

	Result<Item> itemResult = itemService.getItem(itemId);
	Item older;
	if (!itemResult.isSuccess()) {
	    alertError(model, itemResult.getResultMsg());
	    return "biz/queryItem";
	} else {
	    if (itemResult.getModule() == null) {
		alertError(model, "商品为空");
		return "biz/queryItem";
	    }
	    older = itemResult.getModule();
	}

	logger.warn(userInfo.getUserName() + "执行转拆分操作 商品 id : " + itemId);
	Result<Boolean> result = itemService.toCombineItem(itemId);
	if (result.isSuccess() && result.getModule()) {
	    setOperationLog(itemId, bizId, "(转拆分)", older, userInfo, session);
	    alertSuccess(model, "queryItem.do?bizId=" + bizId + "&id=" + itemId);
	} else {
	    alertError(model, result.getResultMsg());
	}
	return "biz/queryItem";
    }

    @RequestMapping(value = "biz/itemDeal", params = "requestType=charge")
    public String onRequestCharge(@RequestParam("itemId") Integer itemId, @RequestParam("bizId") Integer bizId,
	    HttpSession session, Model model) {
	UserInfo userInfo = getUserInfo(session);
	// 业务权限判断
	if (!checkBizAuth(bizId, userInfo)) {
	    return "error/autherror";
	}

	Result<Item> itemResult = itemService.getItem(itemId);
	Item older;
	if (!itemResult.isSuccess()) {
	    alertError(model, itemResult.getResultMsg());
	    return "biz/queryItem";
	} else {
	    if (itemResult.getModule() == null) {
		alertError(model, "商品为空");
		return "biz/queryItem";
	    }
	    older = itemResult.getModule();
	}

	logger.warn(userInfo.getUserName() + "执行转直充操作 商品 id : " + itemId);
	Result<Boolean> result = itemService.toChargeItem(itemId);
	if (result.isSuccess() && result.getModule()) {
	    setOperationLog(itemId, bizId, "(转直充)", older, userInfo, session);
	    alertSuccess(model, "queryItem.do?bizId=" + bizId + "&id=" + itemId);
	} else {
	    alertError(model, result.getResultMsg());
	}
	return "biz/queryItem";
    }

    @RequestMapping(value = "biz/itemDeal", params = "requestType=card")
    public String onRequestCard(@RequestParam("itemId") Integer itemId, @RequestParam("bizId") Integer bizId,
	    HttpSession session, Model model) {
	UserInfo userInfo = getUserInfo(session);
	// 业务权限判断
	if (!checkBizAuth(bizId, userInfo)) {
	    return "error/autherror";
	}

	Result<Item> itemResult = itemService.getItem(itemId);
	Item older;
	if (!itemResult.isSuccess()) {
	    alertError(model, itemResult.getResultMsg());
	    return "biz/queryItem";
	} else {
	    if (itemResult.getModule() == null) {
		alertError(model, "商品为空");
		return "biz/queryItem";
	    }
	    older = itemResult.getModule();
	}

	logger.warn(userInfo.getUserName() + "执行转卡密操作 商品 id : " + itemId);
	Result<Boolean> result = itemService.toCardItem(itemId);
	if (result.isSuccess() && result.getModule()) {
	    setOperationLog(itemId, bizId, "(转卡密)", older, userInfo, session);
	    alertSuccess(model, "queryItem.do?bizId=" + bizId + "&id=" + itemId);
	} else {
	    alertError(model, result.getResultMsg());
	}
	return "biz/queryItem";
    }
	//下架跳转
    @RequestMapping(value = "biz/itemDeal", params = "requestType=batchDownIndex")
    public String onRequestBatchDownIndex(@RequestParam("ids") String ids, @RequestParam("bizId") Integer bizId,
	    HttpSession session, Model model) {
	UserInfo userInfo = getUserInfo(session);
	// 业务权限判断
	if (!checkBizAuth(bizId, userInfo)) {
	    return "error/autherror";
	}

	model.addAttribute("bizName", Constants.BIZ_MAP.get(bizId));
	model.addAttribute("bizId", bizId);

	UpDownForm upDownForm = new UpDownForm();
	upDownForm.setIds(ids);
	upDownForm.setUrl("itemDeal.do");
	upDownForm.setRequestType("batchDown");
	upDownForm.setTypeDown();
	upDownForm.setModuleName("商品");
	upDownForm.setBizId(bizId + "");

	model.addAttribute("upDownForm", upDownForm);
	return "biz/batchUpDown";
    }

    @RequestMapping(value = "biz/itemDeal", params = "requestType=conditionDownIndex", method = RequestMethod.POST)
    public String onRequestConditionDownIndex(@ModelAttribute("itemQuery") ItemQuery itemQuery,
	    @RequestParam("bizId") Integer bizId, @RequestParam("requestType") String requestType, HttpSession session,
	    Model model) {
	UserInfo userInfo = getUserInfo(session);
	model.addAttribute("bizName", Constants.BIZ_MAP.get(bizId));
	model.addAttribute("bizId", bizId);
	Result<List<Item>> result = null;
	// 业务权限判断
	if (!checkBizAuth(bizId, userInfo)) {
	    return "error/autherror";
	}

	StringBuffer ids = new StringBuffer("");
	StringBuffer names = new StringBuffer("");
	//
	if (itemQuery.getSalesAreas()==null) {
		itemQuery.setPageSize(10000);
		itemQuery.setCurrentPage(1);
		result = itemService.queryItemList(itemQuery);
		if (!result.isSuccess()) {
			alertError(model, result.getResultMsg());
			return "biz/queryItem";
		}
		List<Item> list = result.getModule();
		if (list == null || list.isEmpty()) {
			alertError(model, "商品列表不能为空");
			return "biz/queryItem";
		}

		for (int i = 0; i < list.size(); i++) {
			ids.append(list.get(i).getId());
			names.append(list.get(i).getItemName());
			if (i != (list.size() - 1)) {
				ids.append(",");
				names.append(" ，");
			}
		}
	}else{
		for (int i = 0; i<itemQuery.getSalesAreas().size(); i++) {
			itemQuery.setSalesArea(itemQuery.getSalesAreas().get(i));
			itemQuery.setPageSize(10000);
			itemQuery.setCurrentPage(1);
			result = itemService.queryItemList(itemQuery);
			List<Item> list = result.getModule();
			if (list == null || list.isEmpty()) {
				alertError(model, "商品列表不能为空");
				return "biz/queryItem";
			}
			for (int j = 0; j < list.size(); j++) {
				ids.append(list.get(j).getId());
				names.append(list.get(j).getItemName()).append(" ，");
				if (i!=itemQuery.getSalesAreas().size()-1) {
					ids.append(",");

				}else{
					if (j != (list.size() - 1)) {
						ids.append(",");
					}
				}
			}
		}
	}

	UpDownForm upDownForm = new UpDownForm();
	upDownForm.setIds(ids.toString().trim());
	upDownForm.setNames(names.toString().trim());
	upDownForm.setUrl("itemDeal.do");
	upDownForm.setRequestType("batchDown");
	upDownForm.setTypeDown();
	upDownForm.setBizId(bizId + "");
	upDownForm.setModuleName("商品");

	model.addAttribute("upDownForm", upDownForm);
	return "biz/batchUpDown";
    }
	//批量上架的跳转
    @RequestMapping(value = "biz/itemDeal", params = "requestType=batchUpIndex")
    public String onRequestBatchUpIndex(@RequestParam("ids") String ids, @RequestParam("bizId") Integer bizId,
	    HttpSession session, Model model) {
	UserInfo userInfo = getUserInfo(session);
	System.out.println(ids);
	model.addAttribute("bizName", Constants.BIZ_MAP.get(bizId));//根据商家id获取具体的名称
	model.addAttribute("bizId", bizId);

	// 业务权限判断
	if (!checkBizAuth(bizId, userInfo)) {
	    return "error/autherror";
	}

	UpDownForm upDownForm = new UpDownForm();
	upDownForm.setIds(ids);
	upDownForm.setUrl("itemDeal.do");
	upDownForm.setRequestType("batchUp");
	upDownForm.setTypeOn();
	upDownForm.setBizId(bizId + "");
	upDownForm.setModuleName("商品");

	model.addAttribute("upDownForm", upDownForm);
	return "biz/batchUpDown";
    }
	@RequestMapping(value = "biz/itemDeal", params = "requestType=conditionUpIndex", method = RequestMethod.POST)
    public String onRequestConditionUpIndex(@ModelAttribute("itemQuery") ItemQuery itemQuery,
	    @RequestParam("bizId") Integer bizId, @RequestParam("requestType") String requestType, HttpSession session,
	    Model model) {
	UserInfo userInfo = getUserInfo(session);
	model.addAttribute("bizName", Constants.BIZ_MAP.get(bizId));
	model.addAttribute("bizId", bizId);
	Result<List<Item>> result = null;
	// 业务权限判断
	if (!checkBizAuth(bizId, userInfo)) {
	    return "error/autherror";
	}
	StringBuffer ids = new StringBuffer("");
	StringBuffer names = new StringBuffer("");
	if (itemQuery.getSalesAreas()==null) {
		itemQuery.setPageSize(10000);
		itemQuery.setCurrentPage(1);
		result = itemService.queryItemList(itemQuery);
		List<Item> list = result.getModule();
		if (list == null || list.isEmpty()) {
			alertError(model, "商品列表不能为空");
			return "biz/queryItem";
		}
		for (int i = 0; i < list.size(); i++) {
			ids.append(list.get(i).getId());
			names.append(list.get(i).getItemName()).append(" ，");
			if (i != (list.size() - 1)) {
				ids.append(",");
			}
		}
	}else{
		for (int i = 0 ; i<itemQuery.getSalesAreas().size(); i++) {
			itemQuery.setSalesArea(itemQuery.getSalesAreas().get(i));
			itemQuery.setPageSize(10000);
			itemQuery.setCurrentPage(1);
			result = itemService.queryItemList(itemQuery);
			List<Item> list = result.getModule();
			if (list == null || list.isEmpty()) {
				alertError(model, "商品列表不能为空");
				return "biz/queryItem";
			}
			for (int j = 0; j < list.size(); j++) {
				ids.append(list.get(j).getId());
				names.append(list.get(j).getItemName()).append(" ，");
				if (i!=itemQuery.getSalesAreas().size()-1) {
						ids.append(",");

				}else{
					if (j != (list.size() - 1)) {
						ids.append(",");
				}
				}
			}
		}
	}
	if (!result.isSuccess()) {
	    alertError(model, result.getResultMsg());
	    return "biz/queryItem";
	}
	UpDownForm upDownForm = new UpDownForm();
	upDownForm.setIds(ids.toString().trim());
	upDownForm.setNames(names.toString().trim());
	upDownForm.setUrl("itemDeal.do");
	upDownForm.setRequestType("batchUp");
	upDownForm.setTypeOn();
	upDownForm.setBizId(bizId + "");
	upDownForm.setModuleName("商品");

	model.addAttribute("upDownForm", upDownForm);
	return "biz/batchUpDown";
    }

	//新增商品跳转
    @RequestMapping(value = "biz/itemAdd", method = RequestMethod.GET)
    public String onAddIndex(@RequestParam("bizId") Integer bizId, HttpSession session, Model model) {
	UserInfo userInfo = getUserInfo(session);
	// 业务权限判断
	if (!checkBizAuth(bizId, userInfo)) {
	    return "error/autherror";
	}

	model.addAttribute("bizName", Constants.BIZ_MAP.get(bizId));
	model.addAttribute("bizId", bizId);
	model.addAttribute("hasReverse", hasReverse(bizId));
	model.addAttribute("hasCombine", hasCombine(bizId));
	setSelectValue(bizId, model);
	return "biz/itemAdd";
    }
	//新增商品操作
    @RequestMapping(value = "biz/itemAdd", method = RequestMethod.POST)
    public String onPostAdd(@ModelAttribute("itemForm") ItemForm itemForm, BindingResult bindingResult, HttpSession session,
	    Model model) {
	UserInfo userInfo = getUserInfo(session);
	String returnUrl = "biz/itemAdd";
	// 业务权限判断
	if (!checkBizAuth(Integer.parseInt(itemForm.getBizId()), userInfo)) {
	    return "error/autherror";
	}

	model.addAttribute("bizName", Constants.BIZ_MAP.get(Integer.parseInt(itemForm.getBizId())));
	model.addAttribute("bizId", itemForm.getBizId());
	model.addAttribute("hasReverse", hasReverse(Integer.parseInt(itemForm.getBizId())));
	model.addAttribute("hasCombine", hasCombine(Integer.parseInt(itemForm.getBizId())));
	setSelectValue(Integer.parseInt(itemForm.getBizId()), model);
	validator.validate(itemForm, bindingResult);
	if (bindingResult.hasErrors()) {
	    model.addAttribute("errorList", bindingResult.getAllErrors());
	    return returnUrl;
	}

	// 对salesAreaList做特殊验证
	if (String.valueOf(Constants.Item.SALE_TYPE_AREA).equals(itemForm.getSalesAreaType())
		&& ((itemForm.getSalesAreaList() == null || itemForm.getSalesAreaList().isEmpty()))) {
	    FieldError error = new FieldError("itemForm", "salesAreaList", "至少选择一个销售省域");
	    setErrorList(bindingResult, error, model);
	    return returnUrl;
	}

	if (StringUtils.isNotBlank(itemForm.getItemSize()) && StringUtils.isBlank(itemForm.getItemRange())) {
	    FieldError error = new FieldError("itemForm", "itemRange", "商品大小和流量类型必需同时选择");
	    setErrorList(bindingResult, error, model);
	    return returnUrl;
	}

	if (Integer.parseInt(itemForm.getItemType()) == Constants.Item.TYPE_ITEM_COMBINE) {
	    if (StringUtils.isBlank(itemForm.getItemCategoryId())) {
		FieldError error = new FieldError("itemForm", "itemCategoryId", "拆分商品必需选择拆分关联商品");
		setErrorList(bindingResult, error, model);
		return returnUrl;
	    }
	    Item combineItem = localCachedService.getItem(Integer.parseInt(itemForm.getItemCategoryId()));
	    if (combineItem == null) {
		FieldError error = new FieldError("itemForm", "itemCategoryId", "拆分关联商品不存在");
		setErrorList(bindingResult, error, model);
		return returnUrl;
	    }
	    if (BigDecimalUtils.multInteger(itemForm.getItemFacePrice()) <= combineItem.getItemFacePrice().intValue()) {
		FieldError error = new FieldError("itemForm", "itemCategoryId", "主商品价格必需大于拆分关联商品价格");
		setErrorList(bindingResult, error, model);
		return returnUrl;
	    }
	    if (BigDecimalUtils.multInteger(itemForm.getItemFacePrice()) % combineItem.getItemFacePrice() != 0) {
		FieldError error = new FieldError("itemForm", "itemCategoryId", "主商品价格必需是拆分关联商品价格的整数倍");
		setErrorList(bindingResult, error, model);
		return returnUrl;
	    }
	}

	Item item = formToItem(itemForm);
	Result<Boolean> result = itemService.createItem(item);
	if (result != null && result.isSuccess()) {
	    setOperationLog(null, item, userInfo, session);
	    alertSuccess(model, "queryItem.do?bizId=" + itemForm.getBizId() + "&id=" + item.getId());
	} else {
	    alertError(model, result == null ? "操作失败" : result.getResultMsg());// ???
	}
	return returnUrl;
    }

    @RequestMapping(value = "biz/itemEdit", method = RequestMethod.GET)
    public String onEditIndex(@RequestParam("id") Integer id, @RequestParam("bizId") Integer bizId, HttpSession session,
	    Model model) {
	UserInfo userInfo = getUserInfo(session);
	// 业务权限判断
	if (!checkBizAuth(bizId, userInfo)) {
	    return "error/autherror";
	}
	model.addAttribute("bizName", Constants.BIZ_MAP.get(bizId));
	model.addAttribute("bizId", bizId);
	model.addAttribute("hasReverse", hasReverse(bizId));
	setSelectValue(bizId, model);

	Result<Item> result = itemService.getItem(id);
	if (result.isSuccess()) {
	    Item item = result.getModule();
	    if (item != null) {
		if (item.isCombine()) {
		    model.addAttribute("hasCombine", true);
		}
		model.addAttribute("item", item);
	    } else {
		alertError(model, "没有该商品");
	    }
	} else {
	    alertError(model, result.getResultMsg());
	}
	return "biz/itemEdit";
    }

    @RequestMapping(value = "biz/itemEdit", method = RequestMethod.POST)
    public String onPostEdit(@ModelAttribute("itemForm") ItemForm itemForm, BindingResult bindingResult, HttpSession session,
	    Model model) {
	UserInfo userInfo = getUserInfo(session);
	String returnUrl = "biz/itemEdit";
	// 业务权限判断
	if (!checkBizAuth(Integer.parseInt(itemForm.getBizId()), userInfo)) {
	    return "error/autherror";
	}

	model.addAttribute("bizName", Constants.BIZ_MAP.get(Integer.parseInt(itemForm.getBizId())));
	model.addAttribute("hasReverse", hasReverse(Integer.parseInt(itemForm.getBizId())));
	setSelectValue(Integer.parseInt(itemForm.getBizId()), model);
	if (itemForm.getId() == null) {
	    alertError(model, "商品编号不能为空");
	    return returnUrl;
	}

	Result<Item> itemResult = itemService.getItem(Integer.parseInt(itemForm.getId()));
	if (!itemResult.isSuccess()) {
	    alertError(model, itemResult.getResultMsg());
	    return returnUrl;
	}
	if (itemResult.getModule() == null) {
	    alertError(model, "没有该商品");
	    return returnUrl;
	}
	Item older = itemResult.getModule();
	model.addAttribute("item", itemResult.getModule());
	if (older.isCombine()) {
	    model.addAttribute("hasCombine", true);
	}

	validator.validate(itemForm, bindingResult);
	if (bindingResult.hasErrors()) {
	    model.addAttribute("errorList", bindingResult.getAllErrors());
	    return returnUrl;
	}

	// 对salesAreaList做特殊验证
	if (String.valueOf(Constants.Item.SALE_TYPE_AREA).equals(itemForm.getSalesAreaType())
		&& ((itemForm.getSalesAreaList() == null || itemForm.getSalesAreaList().isEmpty()))) {
	    FieldError error = new FieldError("itemForm", "salesAreaList", "至少选择一个销售省域");
	    setErrorList(bindingResult, error, model);
	    return returnUrl;
	}

	if (StringUtils.isNotBlank(itemForm.getItemSize()) && StringUtils.isBlank(itemForm.getItemRange())) {
	    FieldError error = new FieldError("itemForm", "itemRange", "商品大小和流量类型必需同时选择");
	    setErrorList(bindingResult, error, model);
	    return returnUrl;
	}

	if (Integer.parseInt(itemForm.getItemType()) == Constants.Item.TYPE_ITEM_COMBINE) {
	    if (StringUtils.isBlank(itemForm.getItemCategoryId())) {
		FieldError error = new FieldError("itemForm", "itemCategoryId", "拆分商品必需选择拆分关联商品");
		setErrorList(bindingResult, error, model);
		return returnUrl;
	    }
	    Item combineItem = localCachedService.getItem(Integer.parseInt(itemForm.getItemCategoryId()));
	    if (combineItem == null) {
		FieldError error = new FieldError("itemForm", "itemCategoryId", "拆分关联商品不存在");
		setErrorList(bindingResult, error, model);
		return returnUrl;
	    }
	    if (BigDecimalUtils.multInteger(itemForm.getItemFacePrice()) <= combineItem.getItemFacePrice().intValue()) {
		FieldError error = new FieldError("itemForm", "itemCategoryId", "主商品价格必需大于拆分关联商品价格");
		setErrorList(bindingResult, error, model);
		return returnUrl;
	    }
	    if (BigDecimalUtils.multInteger(itemForm.getItemFacePrice()) % combineItem.getItemFacePrice() != 0) {
		FieldError error = new FieldError("itemForm", "itemCategoryId", "主商品价格必需是拆分关联商品价格的整数倍");
		setErrorList(bindingResult, error, model);
		return returnUrl;
	    }
	}

	Item itemUpdate = formToItem(itemForm);
	Result<Boolean> result = itemService.updateItem(itemUpdate);
	if (result.isSuccess()) {
	    setOperationLog(older, itemUpdate, userInfo, session);
	    alertSuccess(model, "queryItem.do?bizId=" + itemForm.getBizId() + "&id=" + itemUpdate.getId());
	} else {
	    alertError(model, result.getResultMsg());
	}
	return returnUrl;
    }
	//拼多多上架跳转
    @RequestMapping(value = "biz/itemPdd", params = "requestType=batchUp", method = RequestMethod.GET)
    public String pddBatchUpG(@RequestParam("bizId") Integer bizId, HttpSession session, Model model) {
	UserInfo userInfo = getUserInfo(session);
	// 业务权限判断
	if (!checkBizAuth(bizId, userInfo)) {
	    return "error/autherror";
	}
	model.addAttribute("bizName", Constants.BIZ_MAP.get(bizId));
	model.addAttribute("bizId", bizId);
	model.addAttribute("requestType", "batchUp");
	model.addAttribute("typeDesc", "上架");
	return "biz/itemPdd";
    }
	//PDD上架操作
    @RequestMapping(value = "biz/itemPdd", params = "requestType=batchUp", method = RequestMethod.POST)
    public String pddBatchUpP(@RequestParam("bizId") Integer bizId, @RequestParam("provinceCode") String provinceCode,
	    HttpSession session, Model model) {
	UserInfo userInfo = getUserInfo(session);
	logger.warn(userInfo.getUserName() + "执行拼多多上架操作 省域:" + provinceCode);
	model.addAttribute("bizName", Constants.BIZ_MAP.get(bizId));
	model.addAttribute("bizId", bizId);
	model.addAttribute("requestType", "batchUp");
	model.addAttribute("typeDesc", "上架");
	// 业务权限判断
	if (!checkBizAuth(bizId, userInfo)) {
	    return "error/autherror";
	}

	logger.warn(userInfo.getUserName() + "执行拼多多上架操作 省域provinceCode:" + provinceCode);
	Result<Boolean> result = pddGoodsService.batchUp(bizId, provinceCode);
	if (result.isSuccess()) {
	    alertSuccess(model, "queryItem.do?bizId=" + bizId);
	} else {
	    alertError(model, result.getResultMsg());
	}
	return "biz/itemPdd";
    }
	//下架操作跳转
    @RequestMapping(value = "biz/itemPdd", params = "requestType=batchDown", method = RequestMethod.GET)
    public String pddBatchDownG(@RequestParam("bizId") Integer bizId, HttpSession session, Model model) {
	UserInfo userInfo = getUserInfo(session);
	// 业务权限判断
	if (!checkBizAuth(bizId, userInfo)) {
	    return "error/autherror";
	}
	model.addAttribute("bizName", Constants.BIZ_MAP.get(bizId));
	model.addAttribute("bizId", bizId);
	model.addAttribute("requestType", "batchDown");
	model.addAttribute("typeDesc", "下架");
	return "biz/itemPdd";
    }
	//拼多多下架操作
    @RequestMapping(value = "biz/itemPdd", params = "requestType=batchDown", method = RequestMethod.POST)
    public String pddBatchDownP(@RequestParam("bizId") Integer bizId, @RequestParam("provinceCode") String provinceCode,
	    HttpSession session, Model model) {
	UserInfo userInfo = getUserInfo(session);
	logger.warn(userInfo.getUserName() + "执行拼多多下架操作 省域:" + provinceCode);
	model.addAttribute("bizName", Constants.BIZ_MAP.get(bizId));
	model.addAttribute("bizId", bizId);
	model.addAttribute("requestType", "batchDown");
	model.addAttribute("typeDesc", "下架");
	// 业务权限判断
	if (!checkBizAuth(bizId, userInfo)) {
	    return "error/autherror";
	}

	logger.warn(userInfo.getUserName() + "执行拼多多下架操作 省域provinceCode:" + provinceCode);
	Result<Boolean> result = pddGoodsService.batchDown(bizId, provinceCode);
	if (result.isSuccess()) {
	    alertSuccess(model, "queryItem.do?bizId=" + bizId);
	} else {
	    alertError(model, result.getResultMsg());
	}
	return "biz/itemPdd";
    }
	//拼多多上架
    @RequestMapping(value = "biz/itemPdd", params = "requestType=up")
    public String pddRequestUp(@RequestParam("itemId") Integer itemId, @RequestParam("bizId") Integer bizId, HttpSession session,
	    Model model) {
	UserInfo userInfo = getUserInfo(session);
	// 业务权限判断
	if (!checkBizAuth(bizId, userInfo)) {
	    return "error/autherror";
	}

	logger.warn(userInfo.getUserName() + "执行拼多多上架操作 商品id:" + itemId);
	Result<Boolean> result = pddGoodsService.itemUp(itemId);
	if (result.isSuccess()) {
	    alertSuccess(model, "queryItem.do?bizId=" + bizId + "&id=" + itemId);
	} else {
	    alertError(model, result.getResultMsg());
	}
	return "biz/queryItem";
    }
    /*
    对的
     */
	//拼多多批量上架
	@RequestMapping(value = "biz/itemPdd", params = "requestType=batchPddUp")
	public synchronized String pddRequestBatchUp(@RequestParam("itemId") String itemIds, @RequestParam("bizId") Integer bizId, HttpSession session,
									Model model) {
		UserInfo userInfo = getUserInfo(session);
		// 业务权限判断
		if (!checkBizAuth(bizId, userInfo)) {
			return "error/autherror";
		}
		boolean flag = false;//标识上架结果成功还是失败
		List<Future<Result<Boolean>>> list = new ArrayList<Future<Result<Boolean>>>();//线程结果封装
		List<Result<Boolean>> dataList = new ArrayList<Result<Boolean>>();//结果封装
		List<Integer> itemList = new ArrayList<Integer>();//获取到所有的订单数据
		List<Integer> itemFaildList = new ArrayList<Integer>();//失败时的数据封装
		String[] split = itemIds.split(",");

		for (int i = 0; i <split.length;i++) {
			final Integer itemId = Integer.valueOf(split[i]);
			logger.warn(userInfo.getUserName() + "执行拼多多上架操作 商品id:" + itemId);
			Future<Result<Boolean>> submit = Constants.threadPoolExecutor.submit(new Callable<Result<Boolean>>() {
				@Override
				public Result<Boolean> call() throws Exception {
					return pddGoodsService.itemUp(itemId);
				}
			});
			list.add(submit);//线程添加
			itemList.add(itemId);//商品id添加
		}
		//获取pdd返回的结果
		for (int i = 0 ; i<list.size();i++) {
			try {
				dataList.add(list.get(i).get());
			} catch (Exception e) {
				//pdd超时
				list.get(i).cancel(true);
				logger.warn(userInfo.getUserName() + "执行拼多多上架操作超时 商品id"+itemList.get(i));
			}
		}
		//判断是否有超时的
		for (int i = 0 ; i<dataList.size();i++) {
			Result<Boolean> booleanResult = dataList.get(i);
			//上架失败
			if (!booleanResult.isSuccess()) {
				flag=true;
				//itemList.get(i);
				itemFaildList.add(itemList.get(i));
			}
		}
		String join = StringUtils.join(itemFaildList, ",");
		//是否批量上架成功
		if (!flag) {
			alertSuccess(model, "queryItem.do?bizId=" + bizId + "&ids=" + itemIds);
		}else{
//			失败跳转
			alertMsgRedirect(model,"商品上架失败((商品非拼多多商品))","queryItemUp.do?bizId="+bizId+"&ids=" + join);
//			return "redirect:/biz/queryItemUp.do?bizId="+bizId+"&ids="+ join;
		}
		return "biz/queryItem";
	}

    @RequestMapping(value = "biz/itemPdd", params = "requestType=down")
    public  String pddRequestDown(@RequestParam("itemId") Integer itemId, @RequestParam("bizId") Integer bizId,
	    HttpSession session, Model model) {
	UserInfo userInfo = getUserInfo(session);
	// 业务权限判断
	if (!checkBizAuth(bizId, userInfo)) {
	    return "error/autherror";
	}

	logger.warn(userInfo.getUserName() + "执行拼多多下架操作 商品id:" + itemId);
	Result<Boolean> result = pddGoodsService.itemDown(itemId);
	if (result.isSuccess()) {
	    alertSuccess(model, "queryItem.do?bizId=" + bizId + "&idsList=" + itemId);
	} else {
	    alertError(model, result.getResultMsg());
	}
	return "biz/queryItem";
    }

	/**
	 * PDD批量下架操作
	 * @param itemIds
	 * @param bizId
	 * @param session
	 * @param model
	 * @return
	 */
    //pdd批量下架操作
	@RequestMapping(value = "biz/itemPdd", params = "requestType=batchPddDown")
	public synchronized String pddRequestBatchDown(@RequestParam("itemId") String itemIds, @RequestParam("bizId") Integer bizId,
									  HttpSession session, Model model) {
		UserInfo userInfo = getUserInfo(session);
		// 业务权限判断
		if (!checkBizAuth(bizId, userInfo)) {
			return "error/autherror";
		}
		boolean flag = false;//标识下架结果成功还是失败
		List<Future<Result<Boolean>>> list = new ArrayList<Future<Result<Boolean>>>();//线程结果封装
		List<Result<Boolean>> dataList = new ArrayList<Result<Boolean>>();//结果封装
		List<Integer> itemList = new ArrayList<Integer>();//获取到所有的订单数据
		List<Integer> itemFaildList = new ArrayList<Integer>();//失败时的数据封装

		//logger.warn(userInfo.getUserName() + "执行拼多多下架操作 商品id:" + itemIds);
		String[] split = itemIds.split(",");
		for (int i = 0; i <split.length;i++) {
			final Integer itemId = Integer.valueOf(split[i]);
			logger.warn(userInfo.getUserName() + "执行拼多多下架操作 商品id:" + itemId);
			Future<Result<Boolean>> submit = Constants.threadPoolExecutor.submit(new Callable<Result<Boolean>>() {
				@Override
				public Result<Boolean> call() throws Exception {
					return pddGoodsService.itemDown(itemId);//执行pdd下架操作
				}
			});
			list.add(submit);//线程添加
			itemList.add(itemId);//商品id添加
		}
		//获取pdd返回的结果
		for (int i = 0 ; i<list.size();i++) {
			try {
				dataList.add(list.get(i).get());
			} catch (Exception e) {
				//pdd超时
				list.get(i).cancel(true);
				logger.warn(userInfo.getUserName() + "执行拼多多下架操作超时 商品id"+itemList.get(i));
			}
		}
		//获取pdd的结果判断
		for (int i = 0 ; i<dataList.size();i++) {
			Result<Boolean> booleanResult = dataList.get(i);
			//下架失败
			if (!booleanResult.isSuccess()) {
				flag=true;
				//itemList.get(i);
				itemFaildList.add(itemList.get(i));
			}
		}
		String join = StringUtils.join(itemFaildList, ",");
		//是否批量下架成功
		if (!flag) {
			alertSuccess(model, "queryItem.do?bizId=" + bizId + "&ids=" + itemIds);
		}else{
//			失败跳转
			alertMsgRedirect(model,"商品下架失败(商品非拼多多商品)","queryItemUp.do?bizId="+bizId+"&ids=" + join);
//			return "redirect:/biz/queryItemUp.do?bizId="+bizId+"&ids="+ join;
		}
		return "biz/queryItem";
	}
	@RequestMapping(
			value = {"biz/itemDeal"},
			params = {"requestType=batchUp"},
			method = {RequestMethod.POST}
	)
	public String onRequestBatchUp(@ModelAttribute("upDownForm") UpDownForm upDownForm, BindingResult bindingResult, HttpSession session, Model model) {
		UserInfo userInfo = this.getUserInfo(session);
		String returnUrl = "biz/batchUpDown";
		this.validator.validate(upDownForm, bindingResult);
		if (bindingResult.hasErrors()) {
			model.addAttribute("errorList", bindingResult.getAllErrors());
			model.addAttribute("bizName", Constants.BIZ_MAP.get(upDownForm.getBizId()));
			model.addAttribute("bizId", upDownForm.getBizId());
			return returnUrl;
		} else {
			this.logger.warn(userInfo.getUserName() + "执行批量上架操作 商品 id : " + upDownForm.getIds());
			model.addAttribute("bizName", Constants.BIZ_MAP.get(upDownForm.getBizId()));
			model.addAttribute("bizId", upDownForm.getBizId());
			if (!this.checkBizAuth(Integer.parseInt(upDownForm.getBizId()), userInfo)) {
				return "error/autherror";
			} else {
				String[] strs = upDownForm.getIds().split(",");
				if (strs != null && strs.length != 0) {
					List<Integer> list = new ArrayList();
					String[] var12 = strs;
					int var11 = strs.length;

					String description;
					for(int var10 = 0; var10 < var11; ++var10) {
						description = var12[var10];
						if (StringUtils.isNumeric(description)) {
							list.add(Integer.parseInt(description));
						}
					}

					if (list.isEmpty()) {
						this.alertError(model, "商品列表不能为空");
						return returnUrl;
					} else {
						description = upDownForm.toString();
						OperationLog operationLog = OperLogUtils.operationLogDeal(description, userInfo, this.getModuleNameFromSession(session), (Integer)null, this.getIpFromSession(session), 2);
						Result<Long> result = this.submitTaskForm(upDownForm, "itemService", "upItem", (Serializable)list, "java.util.List", operationLog, userInfo, Integer.parseInt(upDownForm.getBizId()));
						if (!result.isSuccess()) {
							this.alertError(model, result.getResultMsg());
							return returnUrl;
						} else {
							if (upDownForm.isRealTimeCommit()) {
								this.alertSuccess(model, "queryItem.do?bizId=" + upDownForm.getBizId());
							} else if (result.getModule() == null) {
								this.alertSuccess(model, "../system/queryTask.do");
							} else {
								this.alertSuccess(model, "../system/queryTask.do?id=" + result.getModule());
							}

							return returnUrl;
						}
					}
				} else {
					this.alertError(model, "商品列表不能为空");
					return returnUrl;
				}
			}
		}
	}
	@RequestMapping(value = "biz/itemDeal", params = "requestType=batchDown", method = RequestMethod.POST)
	public String onRequestBatchDown(@ModelAttribute("upDownForm") UpDownForm upDownForm, BindingResult bindingResult,
									 HttpSession session, Model model) {
		UserInfo userInfo = getUserInfo(session);
		String returnUrl = "biz/batchUpDown";

		validator.validate(upDownForm, bindingResult);
		if (bindingResult.hasErrors()) {
			model.addAttribute("errorList", bindingResult.getAllErrors());
			model.addAttribute("bizName", Constants.BIZ_MAP.get(upDownForm.getBizId()));
			model.addAttribute("bizId", upDownForm.getBizId());
			return returnUrl;
		}

		logger.warn(userInfo.getUserName() + "执行批量下架操作 商品 id : " + upDownForm.getIds());
		model.addAttribute("bizName", Constants.BIZ_MAP.get(upDownForm.getBizId()));
		model.addAttribute("bizId", upDownForm.getBizId());

		// 业务权限判断
		if (!checkBizAuth(Integer.parseInt(upDownForm.getBizId()), userInfo)) {
			return "error/autherror";
		}

		String[] strs = upDownForm.getIds().split(",");
		if (strs == null || strs.length == 0) {
			alertError(model, "商品列表不能为空");
			return returnUrl;
		}

		List<Integer> list = new ArrayList<Integer>();
		for (String str : strs) {
			if (StringUtils.isNumeric(str)) {
				list.add(Integer.parseInt(str));
			}
		}
		if (list.isEmpty()) {
			alertError(model, "商品列表不能为空");
			return returnUrl;
		}

		String description = upDownForm.toString();
		OperationLog operationLog = OperLogUtils.operationLogDeal(description, userInfo, getModuleNameFromSession(session), null,
				getIpFromSession(session), Constants.OperationLog.TYPE_UPDATE);

		Result<Long> result = submitTaskForm(upDownForm, "itemService", "downItem", (Serializable) list, "java.util.List",
				operationLog, userInfo, Integer.parseInt(upDownForm.getBizId()));
		if (!result.isSuccess()) {
			alertError(model, result.getResultMsg());
			return returnUrl;
		}

		if (upDownForm.isRealTimeCommit()) {
			alertSuccess(model, "queryItem.do?bizId=" + upDownForm.getBizId());
		} else {
			if (result.getModule() == null) {
				alertSuccess(model, "../system/queryTask.do");
			} else {
				alertSuccess(model, "../system/queryTask.do?id=" + result.getModule());
			}
		}
		return returnUrl;
	}
    private Item formToItem(ItemForm itemForm) {
	Item result = new Item();
	if (StringUtils.isNotEmpty(itemForm.getId())) {
	    result.setId(Integer.parseInt(itemForm.getId()));
	}
	result.setItemName(itemForm.getItemName());
	result.setItemType(Integer.parseInt(itemForm.getItemType()));
	result.setBizId(Integer.parseInt(itemForm.getBizId()));
	result.setCarrierType(Integer.parseInt(itemForm.getCarrierType()));
	if (String.valueOf(Constants.Item.SALE_TYPE_AREA).equals(itemForm.getSalesAreaType())) {
	    StringBuffer sb = new StringBuffer("");
	    for (String areaCode : itemForm.getSalesAreaList()) {
		sb.append(areaCode).append(Constants.Item.SALES_AREA_SPLIT);
	    }
	    result.setSalesArea(sb.toString().substring(0, sb.toString().length() - 1));
	} else {
	    result.setSalesArea(""); // 空表示全国
	}

	if (StringUtils.isNotEmpty(itemForm.getCanSync()) && "1".equals(itemForm.getCanSync())) {
	    result.setCanSync(true);
	} else {
	    result.setCanSync(false);
	}

	// 判断价格是否为空,不为空则*1000
	if (StringUtils.isNotBlank(itemForm.getItemFacePrice())) {
	    Integer price = BigDecimalUtils.multInteger(itemForm.getItemFacePrice());
	    if (price > 0) {
		result.setItemFacePrice(price);
	    }
	}
	if (StringUtils.isNotBlank(itemForm.getItemSalesPrice())) {
	    Integer price = BigDecimalUtils.multInteger(itemForm.getItemSalesPrice());
	    if (price > 0) {
		result.setItemSalesPrice(price);
	    }
	}
	if (StringUtils.isNotBlank(itemForm.getItemSalesPrice2())) {
	    Integer price = BigDecimalUtils.multInteger(itemForm.getItemSalesPrice2());
	    if (price > 0) {
		result.setItemSalesPrice2(price);
	    }
	}
	if (StringUtils.isNotBlank(itemForm.getItemSalesPrice3())) {
	    Integer price = BigDecimalUtils.multInteger(itemForm.getItemSalesPrice3());
	    if (price > 0) {
		result.setItemSalesPrice3(price);
	    }
	}
	if (StringUtils.isNotBlank(itemForm.getItemSalesPrice4())) {
	    Integer price = BigDecimalUtils.multInteger(itemForm.getItemSalesPrice4());
	    if (price > 0) {
		result.setItemSalesPrice4(price);
	    }
	}
	if (StringUtils.isNotBlank(itemForm.getItemDummyPrice())) {
	    Integer price = BigDecimalUtils.multInteger(itemForm.getItemDummyPrice());
	    if (price > 0) {
		result.setItemDummyPrice(price);
	    }
	}
	if (StringUtils.isBlank(itemForm.getItemSize())) {
	    result.setItemSize(0);
	} else if (StringUtils.isNumeric(itemForm.getItemSize())) {
	    result.setItemSize(Integer.parseInt(itemForm.getItemSize()));
	}
	if (StringUtils.isBlank(itemForm.getItemRange())) {
	    result.setItemRange(99);
	} else if (StringUtils.isNumeric(itemForm.getItemRange())) {
	    result.setItemRange(Integer.parseInt(itemForm.getItemRange()));
	}

	if (Integer.parseInt(itemForm.getItemType()) == Constants.Item.TYPE_ITEM_COMBINE) {
	    result.setCombineType(Constants.Item.COMBINE_TYPE_YES);
	} else {
	    result.setCombineType(Constants.Item.COMBINE_TYPE_NO);
	}
	if (StringUtils.isBlank(itemForm.getItemCategoryId())) {
	    result.setItemCategoryId(0);
	} else {
	    result.setItemCategoryId(Integer.parseInt(itemForm.getItemCategoryId()));
	}

	if (StringUtils.isBlank(itemForm.getUserId())) {
	    result.setUserId(0l);
	} else {
	    result.setUserId(Long.parseLong(itemForm.getUserId()));
	}
	result.setItemUnit(itemForm.getItemUnit());// 临时使用itemUnit
	return result;
    }

    private void setOperationLog(Integer itemId, Integer bizId, String opName, Item older, UserInfo userInfo, HttpSession session) {
	@SuppressWarnings("unchecked")
	Map<String, String> map = (HashMap<String, String>) session.getAttribute("requestInfoMap");
	OperationLog operationLog = OperLogUtils.operationLogDeal(older, itemService.getItem(itemId).getModule(), userInfo,
		map.get("moduleName") + opName, bizId, map.get("loginIp"));
	operationLogService.createOperationLog(operationLog);
    }

    private void setOperationLog(Item older, Item item, UserInfo userInfo, HttpSession session) {
	@SuppressWarnings("unchecked")
	Map<String, String> map = (HashMap<String, String>) session.getAttribute("requestInfoMap");
	OperationLog operationLog = OperLogUtils.operationLogDeal(older, item, userInfo, map.get("moduleName"), item.getBizId(),
		map.get("loginIp"));
	operationLogService.createOperationLog(operationLog);
    }

    private void setSelectValue(Integer bizId, Model model) {
	model.addAttribute("itemCategoryList", localCachedService.getItemByBizId(bizId));
    }

    private Boolean hasReverse(int bizId) {
	return bizId == Constants.BizInfo.CODE_FLOW_UNICOM || bizId == Constants.BizInfo.CODE_FLOW_MOBILE
		|| bizId == Constants.BizInfo.CODE_FLOW_TELECOM;
    }

    private Boolean hasCombine(int bizId) {
	return bizId == Constants.BizInfo.CODE_PHONE_UNICOM || bizId == Constants.BizInfo.CODE_PHONE_MOBILE
		|| bizId == Constants.BizInfo.CODE_PHONE_TELECOM;
    }

    @ModelAttribute("salesAreaTypeList")
    public Map<Integer, String> salesAreaTypeList() {
	Map<Integer, String> salesAreaTypeList = new HashMap<Integer, String>();
	salesAreaTypeList.put(Constants.Item.SALE_TYPE_NATIONWIDE, Constants.Item.SALE_TYPE_NATIONWIDE_DESC);
	salesAreaTypeList.put(Constants.Item.SALE_TYPE_AREA, Constants.Item.SALE_TYPE_AREA_DESC);
	return salesAreaTypeList;
    }

    @ModelAttribute("itemTypeList")
    public Map<String, String> itemTypeList() {
	Map<String, String> mapType = new HashMap<String, String>();
	mapType.put(Constants.Item.TYPE_ITEM_CARD + "", Constants.Item.TYPE_ITEM_CARD_DESC);
	mapType.put(Constants.Item.TYPE_ITEM_CHARGE + "", Constants.Item.TYPE_ITEM_CHARGE_DESC);
	mapType.put(Constants.Item.TYPE_ITEM_COMBINE + "", Constants.Item.TYPE_ITEM_COMBINE_DESC);
	return mapType;
    }

    @ModelAttribute("areaInfoList")
    public Map<String, String> areaInfoList() {
	Map<String, String> result = new HashMap<String, String>();
	Map<String, AreaInfo> map = localCachedService.getProvinceMap();
	if (map == null) {
	    return result;
	}
	for (Entry<String, AreaInfo> e : map.entrySet()) {
	    result.put(e.getKey(), e.getValue().getProvinceName());
	}
	// 市域临时
	// result.put("442000", "中山");
	// result.put("640300", "吴忠");
	return result;
    }

    @ModelAttribute("carrierTypeList")
    public Map<String, String> carrierTypeList() {
	Map<String, String> map = new HashMap<String, String>();
	map.put(Constants.Item.CARRIER_TYPE_UNICOM + "", Constants.Item.CARRIER_TYPE_UNICOM_DESC);
	map.put(Constants.Item.CARRIER_TYPE_MOBILE + "", Constants.Item.CARRIER_TYPE_MOBILE_DESC);
	map.put(Constants.Item.CARRIER_TYPE_TELECOM + "", Constants.Item.CARRIER_TYPE_TELECOM_DESC);
	map.put(Constants.Item.CARRIER_TYPE_OTHER + "", Constants.Item.CARRIER_TYPE_OTHER_DESC);
	return map;
    }

    @ModelAttribute("commitTypeList")
    public Map<String, String> commitTypeList() {
	Map<String, String> map = new HashMap<String, String>(2);
	map.put(Constants.Task.COMMIT_TYPE_REAL_TIME + "", Constants.Task.COMMIT_TYPE_REAL_TIME_DESC);
	map.put(Constants.Task.COMMIT_TYPE_TASK + "", Constants.Task.COMMIT_TYPE_TASK_DESC);
	return map;
    }

    @Override
	@InitBinder//初始化表达数据
    public void formInitBinder(WebDataBinder binder) {
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	dateFormat.setLenient(false);
	binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    @ModelAttribute("itemSizeList")//商品基数
    public Map<String, String> itemSizeList() {
	return Constants.ITEM_SIZE_MAP;
    }

    @ModelAttribute("itemRangeList")//商品基数
    public Map<String, String> itemRangeList() {
	return Constants.ITEM_RANGE_MAP;
    }

    @ModelAttribute("downStreamUser")
    public Map<Long, String> downStreamUser() {
	return localCachedService.getDownStreamUser();
    }
}
