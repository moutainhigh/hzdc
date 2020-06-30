package com.longan.biz.pojo;

import java.io.Serializable;
import java.util.Date;

public class CmccItemCode implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long stockId;
    private String cardId;
    private String cardPwd;
    private Date date;
    private Integer itemId;
    private Integer amount;// 单位元
    private Long bizOrderId;
    private String serialno;
    private String mobile;
    private String memo;

    public Long getStockId() {
	return stockId;
    }

    public void setStockId(Long stockId) {
	this.stockId = stockId;
    }

    public String getCardId() {
	return cardId;
    }

    public void setCardId(String cardId) {
	this.cardId = cardId;
    }

    public String getCardPwd() {
	return cardPwd;
    }

    public void setCardPwd(String cardPwd) {
	this.cardPwd = cardPwd;
    }

    public Date getDate() {
	return date;
    }

    public void setDate(Date date) {
	this.date = date;
    }

    public Integer getItemId() {
	return itemId;
    }

    public void setItemId(Integer itemId) {
	this.itemId = itemId;
    }

    public Integer getAmount() {
	return amount;
    }

    public void setAmount(Integer amount) {
	this.amount = amount;
    }

    public Long getBizOrderId() {
	return bizOrderId;
    }

    public void setBizOrderId(Long bizOrderId) {
	this.bizOrderId = bizOrderId;
    }

    public String getSerialno() {
	return serialno;
    }

    public void setSerialno(String serialno) {
	this.serialno = serialno;
    }

    public String getMobile() {
	return mobile;
    }

    public void setMobile(String mobile) {
	this.mobile = mobile;
    }

    public String getMemo() {
	return memo;
    }

    public void setMemo(String memo) {
	this.memo = memo;
    }
}
