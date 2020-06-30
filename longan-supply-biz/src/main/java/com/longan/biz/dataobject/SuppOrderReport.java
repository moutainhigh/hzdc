package com.longan.biz.dataobject;

import com.longan.biz.utils.BigDecimalUtils;

import java.util.Date;
import java.util.List;

/**
 * ClassName: SuppOrderReport
 * Description:
 * date: 2020/5/28 13:52
 *
 * @author 刘腾
 * @since JDK 1.8
 */
public class SuppOrderReport implements Cloneable {
    private String faceAmount; //faceAmount面值总额
    private Long face;
    private String saleAmount; //销售总额
    private Long sale;
    private String dummyAmount;
    private Long dummy;
    private String costAmount;//成本总额
    private Long cost;
    private String profitAmount;//利润
    private Long fee;
    private String feeAmount; //附加费
    private Date startGmtCreate;//开始时间
    private Date endGmtCreate;//结束时间
    private Integer itemFaceprice;//面值
    private String itemFacePriceName;//具体面值
    private List<String> salesAreas;//地区
    private String salesAreasId;//地区id
    private String salesAreasName;//地区名称
//    private String statistic;//是否统计隔天订单
    private String supplyTraderId;//供货商
    private String supplyName;
    private String userId;//代理商
    private String userName;
    private Integer bizId;//运营商
    private String bizIdName;//运营商名称
    private String status;//状态

    //时，分，秒的封装
    private String startHour;
    private String startMinute;
    private String startSecond;
    private String endHour;
    private String endMinute;
    private String endSecond;

    public String getFaceAmount() {
        if (face == null)
            return "0";
        else {
            faceAmount = BigDecimalUtils.getAmountDesc(BigDecimalUtils.doubleDiveid(face + ""));
            return faceAmount;
        }
    }

    public void setFaceAmount(String faceAmount) {
        this.faceAmount = faceAmount;
    }

    public Long getFace() {
        return face;
    }

    public void setFace(Long face) {
        this.face = face;
    }

    public String getSaleAmount() {
        if (sale == null)
            return "0";
        else {
            saleAmount = BigDecimalUtils.getAmountDesc(BigDecimalUtils.doubleDiveid(sale + ""));
            return saleAmount;
        }
    }

    public void setSaleAmount(String saleAmount) {
        this.saleAmount = saleAmount;
    }

    public String getDummyAmount() {
        if (dummy == null)
            return "0";
        else {
            dummyAmount = BigDecimalUtils.getAmountDesc(BigDecimalUtils.doubleDiveid(dummy + ""));
            return dummyAmount;
        }
    }

    public void setDummyAmount(String dummyAmount) {
        this.dummyAmount = dummyAmount;
    }

    public Long getDummy() {
        return dummy;
    }

    public void setDummy(Long dummy) {
        this.dummy = dummy;
    }

    public Long getSale() {
        return sale;
    }

    public void setSale(Long sale) {
        this.sale = sale;
    }

    public String getCostAmount() {
        if (cost == null)
            return "0";
        else {
            costAmount = BigDecimalUtils.getAmountDesc(BigDecimalUtils.doubleDiveid(cost + ""));
            return costAmount;
        }
    }

    public void setCostAmount(String costAmount) {
        this.costAmount = costAmount;
    }

    public Long getCost() {
        return cost;
    }

    public void setCost(Long cost) {
        this.cost = cost;
    }

    public String getProfitAmount() {
        if (sale == null || cost == null)
            return "0";
        else {
            if (fee == null) {
                profitAmount = BigDecimalUtils.getAmountDesc(BigDecimalUtils.doubleDiveid((sale - cost) + ""));
            } else {
                profitAmount = BigDecimalUtils.getAmountDesc(BigDecimalUtils.doubleDiveid((sale - cost - fee) + ""));
            }
            return profitAmount;
        }
    }

    public void setProfitAmount(String profitAmount) {
        this.profitAmount = profitAmount;
    }

    public String getFeeAmount() {
        return feeAmount;
    }

    public void setFeeAmount(Integer serviceFee, Integer cashFee) {
        Double amount = 0d;
        if (serviceFee == null || serviceFee == 0) {
            serviceFee = 0;
        } else {
            if (sale != null) {
                amount = BigDecimalUtils.doubleDiveid(sale + "") * (BigDecimalUtils.doubleDiveid(serviceFee + "") / 100);
            }
        }
        if (cashFee != null && cashFee != 0) {
            amount += BigDecimalUtils.doubleDiveid(sale + "") * (1 - (BigDecimalUtils.doubleDiveid(serviceFee + "") / 100))
                    * (BigDecimalUtils.doubleDiveid(cashFee + "") / 100);
        }
        this.fee = BigDecimalUtils.multLong(amount + "");
        this.feeAmount = BigDecimalUtils.getAmountDesc(amount);
    }

    public Long getFee() {
        return fee;
    }

    public void setFee(Long fee) {
        this.fee = fee;
    }

    public Date getStartGmtCreate() {
        return startGmtCreate;
    }

    public void setStartGmtCreate(Date startGmtCreate) {
        this.startGmtCreate = startGmtCreate;
    }

    public Date getEndGmtCreate() {
        return endGmtCreate;
    }

    public void setEndGmtCreate(Date endGmtCreate) {
        this.endGmtCreate = endGmtCreate;
    }

    public Integer getItemFaceprice() {
        return itemFaceprice;
    }

    public void setItemFaceprice(Integer itemFaceprice) {
        this.itemFaceprice = itemFaceprice;
    }

    public String getItemFacePriceName() {
        return itemFacePriceName;
    }

    public void setItemFacePriceName(String itemFacePriceName) {
        this.itemFacePriceName = itemFacePriceName;
    }

    public List<String> getSalesAreas() {
        return salesAreas;
    }

    public void setSalesAreas(List<String> salesAreas) {
        this.salesAreas = salesAreas;
    }

    public String getSalesAreasId() {
        return salesAreasId;
    }

    public void setSalesAreasId(String salesAreasId) {
        this.salesAreasId = salesAreasId;
    }

    public String getSalesAreasName() {
        return salesAreasName;
    }

    public void setSalesAreasName(String salesAreasName) {
        this.salesAreasName = salesAreasName;
    }

    public String getSupplyTraderId() {
        return supplyTraderId;
    }

    public void setSupplyTraderId(String supplyTraderId) {
        this.supplyTraderId = supplyTraderId;
    }

    public String getSupplyName() {
        return supplyName;
    }

    public void setSupplyName(String supplyName) {
        this.supplyName = supplyName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStartHour() {
        return startHour;
    }

    public void setStartHour(String startHour) {
        this.startHour = startHour;
    }

    public String getStartMinute() {
        return startMinute;
    }

    public void setStartMinute(String startMinute) {
        this.startMinute = startMinute;
    }

    public String getStartSecond() {
        return startSecond;
    }

    public void setStartSecond(String startSecond) {
        this.startSecond = startSecond;
    }

    public String getEndHour() {
        return endHour;
    }

    public void setEndHour(String endHour) {
        this.endHour = endHour;
    }

    public String getEndMinute() {
        return endMinute;
    }

    public void setEndMinute(String endMinute) {
        this.endMinute = endMinute;
    }

    public String getEndSecond() {
        return endSecond;
    }

    public void setEndSecond(String endSecond) {
        this.endSecond = endSecond;
    }

    public Integer getBizId() {
        return bizId;
    }

    public void setBizId(Integer bizId) {
        this.bizId = bizId;
    }

    public String getBizIdName() {
        return bizIdName;
    }

    public void setBizIdName(String bizIdName) {
        this.bizIdName = bizIdName;
    }

    public void setFeeAmount(String feeAmount) {
        this.feeAmount = feeAmount;
    }

    @Override
    public SuppOrderReport clone() throws CloneNotSupportedException {
        return (SuppOrderReport) super.clone();
    }
}
