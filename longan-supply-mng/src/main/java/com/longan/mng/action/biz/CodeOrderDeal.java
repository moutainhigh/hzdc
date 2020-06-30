package com.longan.mng.action.biz;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.longan.biz.core.StockService;
import com.longan.biz.dataobject.Stock;
import com.longan.biz.dataobject.UserInfo;
import com.longan.biz.domain.Result;
import com.longan.biz.pojo.CmccItemCode;
import com.longan.biz.utils.BigDecimalUtils;
import com.longan.biz.utils.Constants;
import com.longan.biz.utils.DateTool;
import com.longan.client.remote.service.CallJifenService;
import com.longan.mng.action.common.BaseController;
import com.longan.mng.form.CodeUsedForm;

@Controller
@RequestMapping("biz/codeOrderDeal")
public class CodeOrderDeal extends BaseController {
    @Resource
    private StockService stockService;

    @Resource
    private CallJifenService callJifenService;

    @Resource
    private Validator validator;

    @Resource
    private CommonsMultipartResolver multipartResolver;

    @RequestMapping(params = "type=inUsed", method = RequestMethod.GET)
    public String indexInUsed(Long userId, Model model, HttpSession session) {
	UserInfo userInfo = getUserInfo(session);
	if (!checkBizAuth(Constants.BizInfo.CODE_JFDH_VOUCHER, userInfo)) {
	    return "error/autherror";
	}
	model.addAttribute("userId", userId);
	model.addAttribute("traderList", localCachedService.getUpStreamUser());
	return "/code/inCodeUsed";
    }

    @RequestMapping(params = "type=inUsed", method = RequestMethod.POST)
    public String onRequestInUsed(Long userId, Model model, HttpServletRequest request, HttpSession session) {
	UserInfo userInfo = getUserInfo(session);
	if (!checkBizAuth(Constants.BizInfo.CODE_JFDH_VOUCHER, userInfo)) {
	    return "error/autherror";
	}
	String url = "/code/inCodeUsed";
	model.addAttribute("userId", userId);
	model.addAttribute("traderList", localCachedService.getUpStreamUser());
	if (userId == null) {
	    setError(model, "供货商不能是空");
	    return url;
	}
	File file = uploadFile(request, userId);
	if (file == null) {
	    setError(model, "上传文件不能是空");
	    return url;
	}

	List<CmccItemCode> codeList = null;
	try {
	    Result<List<CmccItemCode>> parseResult = parseFile(userId, file);
	    if (!parseResult.isSuccess()) {
		setError(model, parseResult.getResultMsg());
		return url;
	    }
	    codeList = parseResult.getModule();
	} catch (Exception ex) {
	    setError(model, "文件格式错误、解析失败");
	    return url;
	}

	Result<Boolean> callResult = callJifenService.records(codeList, userInfo.getId());
	if (!callResult.isSuccess()) {
	    setError(model, callResult.getResultMsg());
	    return url;
	}
	alertSuccess(model, "queryBizOrder.do?bizId=" + Constants.BizInfo.CODE_JFDH_VOUCHER);
	return url;
    }

    @RequestMapping(params = "type=used", method = RequestMethod.GET)
    public String indexUsed(Long bizOrderId, Model model, HttpSession session) {
	UserInfo userInfo = getUserInfo(session);
	if (!checkBizAuth(Constants.BizInfo.CODE_JFDH_VOUCHER, userInfo)) {
	    return "error/autherror";
	}
	model.addAttribute("bizOrderId", bizOrderId);
	return "/code/bizOrderUsed";
    }

    @RequestMapping(params = "type=used", method = RequestMethod.POST)
    public String onRequestUsed(CodeUsedForm codeUsedForm, BindingResult bindingResult, HttpSession session, Model model) {
	UserInfo userInfo = getUserInfo(session);
	if (!checkBizAuth(Constants.BizInfo.CODE_JFDH_VOUCHER, userInfo)) {
	    return "error/autherror";
	}
	String url = "/code/bizOrderUsed";
	model.addAttribute("bizOrderId", codeUsedForm.getBizOrderId());
	validator.validate(codeUsedForm, bindingResult);
	if (bindingResult.hasErrors()) {
	    model.addAttribute("errorList", bindingResult.getAllErrors());
	    return url;
	}

	CmccItemCode code = new CmccItemCode();
	code.setBizOrderId(codeUsedForm.getBizOrderId());
	code.setDate(codeUsedForm.getDateTime());
	code.setMemo(codeUsedForm.getMemo());
	Result<Boolean> callResult = callJifenService.record(code, userInfo.getId());
	if (!callResult.isSuccess()) {
	    setError(model, callResult.getResultMsg());
	    return url;
	}
	alertSuccess(model,
		"queryBizOrder.do?bizId=" + Constants.BizInfo.CODE_JFDH_VOUCHER + "&id=" + codeUsedForm.getBizOrderId());
	return url;
    }

    @RequestMapping(params = "type=refund")
    public String onRequestRefund(Long bizOrderId, HttpSession session, Model model) {
	UserInfo userInfo = getUserInfo(session);
	if (!checkBizAuth(Constants.BizInfo.CODE_JFDH_VOUCHER, userInfo)) {
	    return "error/autherror";
	}

	Result<Boolean> callResult = callJifenService.refund(bizOrderId, userInfo.getId());
	if (callResult.isSuccess()) {
	    alertSuccess(model, "queryBizOrder.do?bizId=" + Constants.BizInfo.CODE_JFDH_VOUCHER + "&id=" + bizOrderId);
	} else {
	    alertError(model, callResult.getResultMsg());
	}
	return "/biz/queryBizOrder";
    }

    @RequestMapping(params = "type=resend")
    public String onRequestResend(Long bizOrderId, HttpSession session, Model model) {
	UserInfo userInfo = getUserInfo(session);
	if (!checkBizAuth(Constants.BizInfo.CODE_JFDH_VOUCHER, userInfo)) {
	    return "error/autherror";
	}

	Result<Boolean> callResult = callJifenService.resend(bizOrderId);
	if (callResult.isSuccess()) {
	    alertSuccess(model, "queryBizOrder.do?bizId=" + Constants.BizInfo.CODE_JFDH_VOUCHER + "&id=" + bizOrderId);
	} else {
	    alertError(model, callResult.getResultMsg());
	}
	return "/biz/queryBizOrder";
    }

    @RequestMapping(params = "type=callback")
    public String onRequestCallback(Long bizOrderId, Model model) {
	Result<Boolean> callResult = callJifenService.callback(bizOrderId);
	if (callResult.isSuccess()) {
	    alertSuccess(model, "queryBizOrder.do?bizId=" + Constants.BizInfo.CODE_JFDH_VOUCHER + "&id=" + bizOrderId);
	} else {
	    alertError(model, callResult.getResultMsg());
	}
	return "/biz/queryBizOrder";
    }

    private File uploadFile(HttpServletRequest request, Long userId) {
	MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
	if (!multipartResolver.isMultipart(multipartRequest)) {
	    return null;
	}
	File file = null;
	MultiValueMap<String, MultipartFile> multfiles = multipartRequest.getMultiFileMap();
	for (String srcfname : multfiles.keySet()) {
	    MultipartFile mfile = multfiles.getFirst(srcfname);
	    try {
		file = new File(com.longan.mng.utils.Constants.UPLOAD_PATH + userId + "." + System.currentTimeMillis() + ".txt");
		if (mfile.isEmpty()) {
		    return null;
		}
		mfile.transferTo(file);
	    } catch (Exception ex) {
		return null;
	    }
	}
	return file;
    }

    private Result<List<CmccItemCode>> parseFile(Long userId, File file) throws Exception {
	Result<List<CmccItemCode>> result = new Result<List<CmccItemCode>>();
	BufferedReader br = null;
	try {
	    br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
	    String line = null;
	    String error = "";
	    List<CmccItemCode> codeList = new ArrayList<CmccItemCode>();
	    while ((line = br.readLine()) != null && StringUtils.isNotBlank(line)) {
		String[] items = line.split(Constants.BUY_REQUEST_SPLIT);
		if (items.length < 2) {
		    error = error + "该行格式错误 line" + line + "\r\n";
		    continue;
		}
		String cardId = items[0];
		Result<Stock> stockResult = stockService.getStockByCardId(userId, cardId);
		if (!stockResult.isSuccess()) {
		    error = error + stockResult.getResultMsg() + "\r\n";
		    continue;
		}
		Stock stock = stockResult.getModule();
		if (stock.getBizOrderId() == null) {
		    error = error + "兑换码不存在对应订单 cardId：" + cardId + "\r\n";
		    continue;
		}

		CmccItemCode code = new CmccItemCode();
		code.setStockId(stock.getId());
		code.setCardId(cardId);
		code.setCardPwd(stock.getCardPwd());
		code.setDate(DateTool.strintToDatetime(items[1]));
		code.setItemId(stock.getItemId());
		if (items.length >= 3) {
		    code.setAmount(Integer.parseInt(items[2]));
		} else {
		    Double amount = BigDecimalUtils.doubleDiveid(stock.getItemFacePrice() + "");
		    code.setAmount(amount.intValue());
		}
		code.setBizOrderId(stock.getBizOrderId());
		if (items.length == 4) {
		    code.setMemo(items[3]);
		} else {
		    code.setMemo("");
		}
		codeList.add(code);
	    }

	    if (StringUtils.isBlank(error)) {
		result.setModule(codeList);
		result.setSuccess(true);
	    } else {
		result.setResultMsg(error);
		codeList.clear();
	    }
	} finally {
	    try {
		if (br != null) {
		    br.close();
		}
	    } catch (Exception ex) {
	    }
	}
	return result;
    }
}
