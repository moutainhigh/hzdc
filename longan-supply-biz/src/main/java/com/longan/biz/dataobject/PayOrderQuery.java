package com.longan.biz.dataobject;

import java.util.Date;

import com.longan.biz.domain.QueryBase;
import com.longan.biz.utils.DateTool;

public class PayOrderQuery extends QueryBase {
    private static final long serialVersionUID = 1L;

    private Date startGmtCreate;
    private Date endGmtCreate;
    private Integer bizId;
    private Long acctId;
    private Long userId;
    private Integer status;
    private Long bizOrderId;
    private Long acctLogId;
    private Integer itemId;
    private Long id;

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public Integer getItemId() {
	return itemId;
    }

    public void setItemId(Integer itemId) {
	this.itemId = itemId;
    }

    public Date getStartGmtCreate() {
	return startGmtCreate;
    }

    public void setStartGmtCreate(Date startGmtCreate) {
	this.startGmtCreate = DateTool.formatStartDate(startGmtCreate);
    }

    public Date getEndGmtCreate() {
	return endGmtCreate;
    }

    public void setEndGmtCreate(Date endGmtCreate) {
	this.endGmtCreate = DateTool.formateEndDate(endGmtCreate);
    }

    public Integer getBizId() {
	return bizId;
    }

    public void setBizId(Integer bizId) {
	this.bizId = bizId;
    }

    public Long getAcctId() {
	return acctId;
    }

    public void setAcctId(Long acctId) {
	this.acctId = acctId;
    }

    public Long getUserId() {
	return userId;
    }

    public void setUserId(Long userId) {
	this.userId = userId;
    }

    public Integer getStatus() {
	return status;
    }

    public void setStatus(Integer status) {
	this.status = status;
    }

    public Long getBizOrderId() {
	return bizOrderId;
    }

    public void setBizOrderId(Long bizOrderId) {
	this.bizOrderId = bizOrderId;
    }

    public Long getAcctLogId() {
	return acctLogId;
    }

    public void setAcctLogId(Long acctLogId) {
	this.acctLogId = acctLogId;
    }

}
