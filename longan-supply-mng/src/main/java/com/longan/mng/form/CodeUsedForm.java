package com.longan.mng.form;

import java.util.Date;

import org.springmodules.validation.bean.conf.loader.annotation.handler.NotBlank;
import org.springmodules.validation.bean.conf.loader.annotation.handler.NotNull;

import com.longan.biz.utils.DateTool;

public class CodeUsedForm {
    @NotNull(message = "订单编号不能为空")
    private Long bizOrderId;

    @NotBlank(message = "消费时间不能为空")
    private String date;

    @NotBlank(message = "消费内容不能为空")
    private String memo;

    public Long getBizOrderId() {
	return bizOrderId;
    }

    public void setBizOrderId(Long bizOrderId) {
	this.bizOrderId = bizOrderId;
    }

    public String getDate() {
	return date;
    }

    public void setDate(String date) {
	this.date = date;
    }

    public String getMemo() {
	return memo;
    }

    public void setMemo(String memo) {
	this.memo = memo;
    }

    public Date getDateTime() {
	try {
	    return DateTool.strintToDatetime(date);
	} catch (Exception ex) {
	}
	return new Date();
    }
}
