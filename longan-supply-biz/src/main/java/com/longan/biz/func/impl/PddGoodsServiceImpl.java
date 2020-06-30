package com.longan.biz.func.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.annotation.Resource;

import com.longan.biz.dataobject.PddAfterApi;
import com.longan.biz.dataobject.PddGoodsApi;
import com.longan.biz.utils.FuncUtils;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import com.longan.biz.core.ItemService;
import com.longan.biz.dataobject.Item;
import com.longan.biz.domain.Result;
import com.longan.biz.func.BaseTokenService;
import com.longan.biz.func.PddGoodsService;
import com.longan.biz.pojo.PddCacheToken;
import com.longan.biz.pojo.PddItemApi;
import com.longan.biz.pojo.PddItemStatus;
import com.longan.biz.utils.Constants;
import com.longan.biz.utils.Utils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class PddGoodsServiceImpl extends BaseTokenService implements PddGoodsService {
    private static final Long pddUserId = Utils.getLong("pdd.id");
    private static final String pddApiUrl = Utils.getProperty("pdd.api");

    private static ExecutorService exceutor = new ThreadPoolExecutor(2, 2, 0L, TimeUnit.MILLISECONDS,
	    new LinkedBlockingQueue<Runnable>(4));

    @Resource
    private ItemService itemService;

    @Override
    public Result<Boolean> itemUp(Integer itemId) {
	Result<Boolean> result = new Result<Boolean>();
	Result<Item> itemResult = checkItem(itemId);
	if (!itemResult.isSuccess()) {
	    result.setResultMsg(itemResult.getResultMsg());
	    return result;
	}
	Item item = itemResult.getModule();
	String accessToken = getAccessToken();
	if (StringUtils.isBlank(accessToken)) {
	    result.setResultMsg("获取accessToken失败");
	    return result;
	}
	PddItemApi pddItem = new PddItemApi();
	pddItem.setIsOnsale(1);
	pddItem.setAccessToken(accessToken);
	String response = callPddApi(item, pddItem);
	Result<Boolean> responseResult = checkPddResponse(response);
	if (responseResult.isSuccess()) {
	    PddItemStatus.map.put(item.getId(), 1);
	    result.setModule(true);
	    result.setSuccess(true);
	} else {
	    PddItemStatus.map.put(item.getId(), 3);
	    result.setResultMsg(responseResult.getResultMsg());
	}
	return result;
    }
	@Override
	public Result<Boolean> pddUp(Integer itemId) {
		Result<Boolean> result = new Result<Boolean>();
		Result<Item> itemResult = checkItem(itemId);
		if (!itemResult.isSuccess()) {
			result.setResultMsg(itemResult.getResultMsg());
			return result;
		}
		Item item = itemResult.getModule();
		String accessToken = getAccessToken();
		if (StringUtils.isBlank(accessToken)) {
			result.setResultMsg("获取accessToken失败");
			return result;
		}
		PddGoodsApi pddItem = new PddGoodsApi();
		//上架状态设置
		pddItem.setIsOnsale(1);
		pddItem.setAccessToken(accessToken);
		String response = callPddGoodsApi(item, pddItem);
		Result<Boolean> responseResult = checkPddGoodsResponse(response);
		if (responseResult.isSuccess()) {
			PddItemStatus.map.put(item.getId(), 1);
			result.setModule(true);
			result.setSuccess(true);
		} else {
			PddItemStatus.map.put(item.getId(), 3);
			result.setResultMsg(responseResult.getResultMsg());
		}
		return result;
	}
	//第二次下架测试
//	@Override
	public Result<Boolean> pddDown(Integer itemId) {
		Result<Boolean> result = new Result<Boolean>();
		Result<Item> itemResult = checkItem(itemId);
		if (!itemResult.isSuccess()) {
			result.setResultMsg(itemResult.getResultMsg());
			return result;
		}
		Item item = itemResult.getModule();
		String accessToken = getAccessToken();
		if (StringUtils.isBlank(accessToken)) {
			result.setResultMsg("获取accessToken失败");
			return result;
		}
		PddGoodsApi pddItem = new PddGoodsApi();
		pddItem.setIsOnsale(0);
		pddItem.setAccessToken(accessToken);
		String response = callPddGoodsApi(item, pddItem);
		Result<Boolean> responseResult = checkPddGoodsResponse(response);
		if (responseResult.isSuccess()) {
			PddItemStatus.map.put(item.getId(), 0);
			result.setModule(true);
			result.setSuccess(true);
		} else {
			PddItemStatus.map.put(item.getId(), 2);
			result.setResultMsg(responseResult.getResultMsg());
		}
		return result;
	}

    @Override
    public Result<Boolean> itemDown(Integer itemId) {
	Result<Boolean> result = new Result<Boolean>();
	Result<Item> itemResult = checkItem(itemId);
	if (!itemResult.isSuccess()) {
	    result.setResultMsg(itemResult.getResultMsg());
	    return result;
	}
	Item item = itemResult.getModule();
	String accessToken = getAccessToken();
	if (StringUtils.isBlank(accessToken)) {
	    result.setResultMsg("获取accessToken失败");
	    return result;
	}

	PddItemApi pddItem = new PddItemApi();
	pddItem.setIsOnsale(0);
	pddItem.setAccessToken(accessToken);
	String response = callPddApi(item, pddItem);
	Result<Boolean> responseResult = checkPddResponse(response);
	if (responseResult.isSuccess()) {
	    PddItemStatus.map.put(item.getId(), 0);
	    result.setModule(true);
	    result.setSuccess(true);
	} else {
	    PddItemStatus.map.put(item.getId(), 2);
	    result.setResultMsg(responseResult.getResultMsg());
	}
	return result;
    }


    @Override
    public Result<Boolean> batchUp(Integer bizId, String provinceCode) {
	Result<Boolean> result = new Result<Boolean>();
	Result<List<Item>> itemResult = checkItemList(bizId, provinceCode);
	if (!itemResult.isSuccess()) {
	    result.setResultMsg(itemResult.getResultMsg());
	    return result;
	}
	List<Item> items = itemResult.getModule();
	String accessToken = getAccessToken();
	if (StringUtils.isBlank(accessToken)) {
	    result.setResultMsg("获取accessToken失败");
	    return result;
	}
	Result<Boolean> batchResult = batchUp(accessToken, items);
	if (batchResult.isSuccess()) {
	    result.setModule(true);
	    result.setSuccess(true);
	} else {
	    result.setResultMsg(batchResult.getResultMsg());
	}
	return result;
    }

    @Override
    public Result<Boolean> batchDown(Integer bizId, String provinceCode) {
	Result<Boolean> result = new Result<Boolean>();
	Result<List<Item>> itemResult = checkItemList(bizId, provinceCode);
	if (!itemResult.isSuccess()) {
	    result.setResultMsg(itemResult.getResultMsg());
	    return result;
	}
	List<Item> items = itemResult.getModule();
	String accessToken = getAccessToken();
	if (StringUtils.isBlank(accessToken)) {
	    result.setResultMsg("获取accessToken失败");
	    return result;
	}
	Result<Boolean> batchResult = batchDown(accessToken, items);
	if (batchResult.isSuccess()) {
	    result.setModule(true);
	    result.setSuccess(true);
	} else {
	    result.setResultMsg(batchResult.getResultMsg());
	}
	return result;
    }

    private Result<Item> checkItem(Integer itemId) {
	Result<Item> result = new Result<Item>();
	if (itemId == null) {
	    result.setResultMsg("商品编号是空");
	    return result;
	}
	Result<Item> itemResult = itemService.getItem(itemId);
	if (!itemResult.isSuccess()) {
	    result.setResultMsg(itemResult.getResultMsg());
	    return result;
	}
	Item item = itemResult.getModule();
	if (itemResult.getModule() == null) {
	    result.setResultMsg("商品不存在");
	    return result;
	}
	if (item.getUserId() == null || item.getUserId() != pddUserId.longValue()) {
	    result.setResultMsg("非拼多多商品");
	    return result;
	}
	if (item.getItemSalesPrice() == null || item.getItemDummyPrice() == null) {
	    result.setResultMsg("商品价格未配置");
	    return result;
	}
	if (item.getItemDummyPrice().intValue() <= item.getItemSalesPrice().intValue()) {
	    result.setResultMsg("单买价必需大于拼团价");
	    return result;
	}
	// 临时使用itemUnit
	if (StringUtils.isBlank(item.getItemUnit()) || item.getItemUnit().split(":").length != 2) {
	    result.setResultMsg("拼多多商品编号或skuid未配置");
	    return result;
	}
	String[] codes = item.getItemUnit().trim().split(":");
	if (!StringUtils.isNumeric(codes[0]) || !StringUtils.isNumeric(codes[1])) {
	    result.setResultMsg("拼多多商品编号或skuid格式错误itemId=" + item.getId());
	    return result;
	}
	result.setSuccess(true);
	result.setModule(item);
	return result;
    }

    private Result<List<Item>> checkItemList(Integer bizId, String provinceCode) {
	Result<List<Item>> result = new Result<List<Item>>();
	if (bizId == null || StringUtils.isBlank(provinceCode)) {
	    result.setResultMsg("业务编号或省域是空");
	    return result;
	}
	if (provinceCode.equals("all")) {
	    provinceCode = null;
	} else if (provinceCode.equals("000000")) {
	    provinceCode = "";
	}
	Result<List<Item>> itemResult = itemService.queryItemList(pddUserId, bizId, provinceCode);
	if (!itemResult.isSuccess()) {
	    result.setResultMsg(itemResult.getResultMsg());
	    return result;
	}
	List<Item> items = itemResult.getModule();
	if (items == null || items.size() == 0) {
	    result.setResultMsg("商品不存在");
	    return result;
	}
	for (Item item : items) {
	    if (item.getItemSalesPrice() == null || item.getItemDummyPrice() == null) {
		result.setResultMsg("商品价格未配置itemId=" + item.getId());
		return result;
	    }
	    if (item.getItemDummyPrice().intValue() <= item.getItemSalesPrice().intValue()) {
		result.setResultMsg("单买价必需大于拼团价itemId=" + item.getId());
		return result;
	    }
	    // 临时使用itemUnit
	    if (StringUtils.isBlank(item.getItemUnit()) || item.getItemUnit().split(":").length != 2) {
		result.setResultMsg("拼多多商品编号或skuid未配置itemId=" + item.getId());
		return result;
	    }
	    String[] codes = item.getItemUnit().trim().split(":");
	    if (!StringUtils.isNumeric(codes[0]) || !StringUtils.isNumeric(codes[1])) {
		result.setResultMsg("拼多多商品编号或skuid格式错误itemId=" + item.getId());
		return result;
	    }
	}
	result.setSuccess(true);
	result.setModule(items);
	return result;
    }

    private Result<Boolean> batchUp(final String accessToken, final List<Item> items) {
	Callable<Result<Boolean>> callable = new Callable<Result<Boolean>>() {
	    @Override
	    public Result<Boolean> call() throws Exception {
		Result<Boolean> callResult = new Result<Boolean>();
		for (Item item : items) {
		    PddItemApi pddItem = new PddItemApi();
		    pddItem.setIsOnsale(1);
		    pddItem.setAccessToken(accessToken);
		    String response = callPddApi(item, pddItem);
		    Result<Boolean> responseResult = checkPddResponse(response);
		    if (responseResult.isSuccess()) {
			PddItemStatus.map.put(item.getId(), 1);
		    } else {
			PddItemStatus.map.put(item.getId(), 3);
			callResult.setResultMsg(responseResult.getResultMsg() + "itemId=" + item.getId());
			return callResult;
		    }
		}
		callResult.setModule(true);
		callResult.setSuccess(true);
		return callResult;
	    }
	};

	Result<Boolean> result = new Result<Boolean>();
	try {
	    Future<Result<Boolean>> future = exceutor.submit(callable);
	    result = future.get(1, TimeUnit.MINUTES);
	} catch (TimeoutException ex) {
	    result.setResultMsg("上架超时、请检查结果");
	} catch (InterruptedException ex) {
	    result.setResultMsg("batchApi error msg : " + ex.getMessage());
	} catch (ExecutionException ex) {
	    result.setResultMsg("batchApi error msg : " + ex.getMessage());
	}
	return result;
    }

    private Result<Boolean> batchDown(final String accessToken, final List<Item> items) {
	Callable<Result<Boolean>> callable = new Callable<Result<Boolean>>() {
	    @Override
	    public Result<Boolean> call() throws Exception {
		Result<Boolean> callResult = new Result<Boolean>();
		for (Item item : items) {
		    PddItemApi pddItem = new PddItemApi();
		    pddItem.setIsOnsale(0);
		    pddItem.setAccessToken(accessToken);
		    String response = callPddApi(item, pddItem);
		    Result<Boolean> responseResult = checkPddResponse(response);
		    if (responseResult.isSuccess()) {
			PddItemStatus.map.put(item.getId(), 0);
		    } else {
			PddItemStatus.map.put(item.getId(), 2);
			callResult.setResultMsg(responseResult.getResultMsg() + "itemId=" + item.getId());
			return callResult;
		    }
		}
		callResult.setModule(true);
		callResult.setSuccess(true);
		return callResult;
	    }
	};

	Result<Boolean> result = new Result<Boolean>();
	try {
	    Future<Result<Boolean>> future = exceutor.submit(callable);
	    result = future.get(1, TimeUnit.MINUTES);
	} catch (TimeoutException ex) {
	    result.setResultMsg("下架超时、请检查结果");
	} catch (InterruptedException ex) {
	    result.setResultMsg("batchApi error msg : " + ex.getMessage());
	} catch (ExecutionException ex) {
	    result.setResultMsg("batchApi error msg : " + ex.getMessage());
	}
	return result;
    }

    private String callPddApi(Item item, PddItemApi pddItem) {
	String[] codes = item.getItemUnit().trim().split(":");
	pddItem.setGoodsId(Long.parseLong(codes[0]));
	pddItem.setSinglePrice(Long.parseLong(item.getItemDummyPrice() / 10 + ""));// 上游单位是分
	pddItem.setGroupPrice(Long.parseLong(item.getItemSalesPrice() / 10 + ""));// 上游单位是分
	pddItem.setSkuId(Long.parseLong(codes[1]));
	pddItem.createSign();
	pddItem.createMap();
	return sendData(pddApiUrl, pddItem.getMap());
    }
    //pdd第二次修改的pdd上下架
	private String callPddGoodsApi(Item item, PddGoodsApi pddItem) {
		String[] codes = item.getItemUnit().trim().split(":");
		pddItem.setGoodsId(Long.parseLong(codes[0]));
		pddItem.createSign();
		pddItem.createMap();
		return sendData(pddApiUrl, pddItem.getMap());
	}
	// //pdd第二次修改的pdd上下架状态设置
	private Result<Boolean> checkPddGoodsResponse(String response) {
		Result<Boolean> result = new Result<Boolean>();
		if (StringUtils.isBlank(response)) {
			result.setResultMsg("获取上下架结果失败");
			return result;
		}
		JSONObject json = JSONObject.fromObject(response);
		if (json.containsKey("error_response")) {
			JSONObject error = json.getJSONObject("error_response");
			result.setResultMsg(error.get("error_code") + ":" + error.get("error_msg"));
			return result;
		}
		if (json.getJSONObject("pdd.goods.sale.status.set").getBoolean("is_success")) {
			result.setModule(true);
			result.setSuccess(true);
		} else {
			result.setResultMsg("上下架失败");
		}
		return result;
	}

    private Result<Boolean> checkPddResponse(String response) {
	Result<Boolean> result = new Result<Boolean>();
	if (StringUtils.isBlank(response)) {
	    result.setResultMsg("获取上下架结果失败");
	    return result;
	}
	JSONObject json = JSONObject.fromObject(response);
	if (json.containsKey("error_response")) {
	    JSONObject error = json.getJSONObject("error_response");
	    result.setResultMsg(error.get("error_code") + ":" + error.get("error_msg"));
	    return result;
	}
	if (json.getJSONObject("goods_update_sku_price_response").getBoolean("is_success")) {
	    result.setModule(true);
	    result.setSuccess(true);
	} else {
	    result.setResultMsg("上下架失败");
	}
	return result;
    }

    private String getAccessToken() {
	Object object = memcachedService.get(Constants.CacheKey.PDD_TOKEN_KEY);
	if (object != null) {
	    return ((PddCacheToken) object).getAccessToken();
	}
	return null;
    }
	public String RefundcallPddApi(Integer page,Integer pageSize) {
		PddAfterApi pddAfterApi =new PddAfterApi();
		//获取系统当前的毫秒数
		Long nowTime = System.currentTimeMillis()/1000;
		//30分钟的时间
		Long stateTiem = 30L*60L;
		Long beforTime = nowTime - stateTiem;
		pddAfterApi.setStartUpdatedAt(beforTime);
		pddAfterApi.setEndUpdatedAt(nowTime);
		//设置页码
		pddAfterApi.setPage(page);
		pddAfterApi.setPageSize(pageSize);
		//pddAfterApi.setAccessToken("ef1ffd328d984998bc1a8e782f217ac5956d4517");//tocken
		//获取tocken
		String accessToken = getAccessToken();
		if (StringUtils.isBlank(accessToken)) {
			return null;
		}
		pddAfterApi.setAccessToken(accessToken);
		//创建签名
		pddAfterApi.createSign();
		//uri
		String data = pddAfterApi.createData();
		//
		return sendData(pddApiUrl, data);
	}
}
