package com.longan.biz.dataobject;

import java.io.Serializable;

/**
 * ClassName: RefundList
 * Description:
 * date: 2020/5/12 11:00
 *
 * @author 刘腾
 * @since JDK 1.8
 */
public class RefundList implements Serializable {
    private String goodsName;
    private String createdTime;
    private String updatedTime;
    private String afterSaleReason;//售后的反馈信息
    private String orderSn; //订单号
    private String id; //售后单id
    private Integer afterSalesStatus;//售后状态
    private String discountAmount;
    private String speedRefundFlag;
    private String orderAmount;
    private String outerGoodsId;
    private String goodsPrice;
    private String speedRefundStatus;
    private String afterSalesType;
    private String goodsId;
    private String trackingNumber;
    private String skuId;
    private String refundAmount;
    private String confirmTime;
    private String goodImage;

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(String updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getAfterSaleReason() {
        return afterSaleReason;
    }

    public void setAfterSaleReason(String afterSaleReason) {
        this.afterSaleReason = afterSaleReason;
    }

    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getAfterSalesStatus() {
        return afterSalesStatus;
    }

    public void setAfterSalesStatus(Integer afterSalesStatus) {
        this.afterSalesStatus = afterSalesStatus;
    }

    public String getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(String discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getSpeedRefundFlag() {
        return speedRefundFlag;
    }

    public void setSpeedRefundFlag(String speedRefundFlag) {
        this.speedRefundFlag = speedRefundFlag;
    }

    public String getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(String orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getOuterGoodsId() {
        return outerGoodsId;
    }

    public void setOuterGoodsId(String outerGoodsId) {
        this.outerGoodsId = outerGoodsId;
    }

    public String getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(String goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public String getSpeedRefundStatus() {
        return speedRefundStatus;
    }

    public void setSpeedRefundStatus(String speedRefundStatus) {
        this.speedRefundStatus = speedRefundStatus;
    }

    public String getAfterSalesType() {
        return afterSalesType;
    }

    public void setAfterSalesType(String afterSalesType) {
        this.afterSalesType = afterSalesType;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public String getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(String refundAmount) {
        this.refundAmount = refundAmount;
    }

    public String getConfirmTime() {
        return confirmTime;
    }

    public void setConfirmTime(String confirmTime) {
        this.confirmTime = confirmTime;
    }

    public String getGoodImage() {
        return goodImage;
    }

    public void setGoodImage(String goodImage) {
        this.goodImage = goodImage;
    }
}
