package com.longan.biz.dataobject;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * ClassName: OrderReport
 * Description:
 * date: 2020/5/18 11:17
 *
 * @author 小狐
 * @since JDK 1.8
 */
public class OrderReport implements Cloneable{
    private Integer nowOrder;//当天总订单
    private Integer nowSuccessOrder;//成功的订单
    private Integer countThreeOrder;//三分钟回调的订单
    private Integer threeSuccessOrder;//三分钟成功回调的
    private String successRate;//成功率
    private String threeSuccessRate;//三分种内回调的成功率
    private Date startGmtCreate;//开始时间
    private Date endGmtCreate;//结束时间
    private Integer itemFaceprice;//面值
    private String itemFacePriceName;//具体面值
    private List<String> salesAreas;//地区
    private String salesAreasId;//地区id
    private String salesAreasName;//地区名称
    private String moreCostTime;//大于
    private String lessCostTime;//小于
    private String statistic;//是否统计隔天订单
    private Date statisticDate;
    private String supplyTraderId;//供货商
    private String supplyName;
    private String userId;//代理商
    private String userName;
    private String carrierType;//运营商
    private String carrierTypeName;//运营商名称
    private String status;//状态

    //时，分，秒的封装
    private String startHour;
    private String startMinute;
    private String startSecond;
    private String endHour;
    private String endMinute;
    private String endSecond;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getNowOrder() {
        return nowOrder;
    }

    public void setNowOrder(Integer nowOrder) {
        this.nowOrder = nowOrder;
    }

    public Integer getNowSuccessOrder() {
        return nowSuccessOrder;
    }

    public void setNowSuccessOrder(Integer nowSuccessOrder) {
        this.nowSuccessOrder = nowSuccessOrder;
    }

    public Integer getCountThreeOrder() {
        return countThreeOrder;
    }

    public void setCountThreeOrder(Integer countThreeOrder) {
        this.countThreeOrder = countThreeOrder;
    }

    public String getSuccessRate() {
        return successRate;
    }

    public void setSuccessRate(String successRate) {
        this.successRate = successRate;
    }

    public String getThreeSuccessRate() {
        return threeSuccessRate;
    }

    public void setThreeSuccessRate(String threeSuccessRate) {
        this.threeSuccessRate = threeSuccessRate;
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

    public List<String> getSalesAreas() {
        return salesAreas;
    }

    public void setSalesAreas(List<String> salesAreas) {
        this.salesAreas = salesAreas;
    }

    public String getMoreCostTime() {
        return moreCostTime;
    }

    public void setMoreCostTime(String moreCostTime) {
        this.moreCostTime = moreCostTime;
    }

    public String getLessCostTime() {
        return lessCostTime;
    }

    public void setLessCostTime(String lessCostTime) {
        this.lessCostTime = lessCostTime;
    }

    public String getStatistic() {
        return statistic;
    }

    public void setStatistic(String statistic) {
        this.statistic = statistic;
    }

    public String getSupplyTraderId() {
        return supplyTraderId;
    }

    public void setSupplyTraderId(String supplyTraderId) {
        this.supplyTraderId = supplyTraderId;
    }

    public String getCarrierType() {
        return carrierType;
    }

    public void setCarrierType(String carrierType) {
        this.carrierType = carrierType;
    }

    public String getCarrierTypeName() {
        return carrierTypeName;
    }

    public void setCarrierTypeName(String carrierTypeName) {
        this.carrierTypeName = carrierTypeName;
    }

    public Integer getThreeSuccessOrder() {
        return threeSuccessOrder;
    }

    public void setThreeSuccessOrder(Integer threeSuccessOrder) {
        this.threeSuccessOrder = threeSuccessOrder;
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

    public String getItemFacePriceName() {
        return itemFacePriceName;
    }

    public void setItemFacePriceName(String itemFacePriceName) {
        this.itemFacePriceName = itemFacePriceName;
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

    public Date getStatisticDate() {
        return statisticDate;
    }

    public void setStatisticDate(Date statisticDate) {
        this.statisticDate = statisticDate;
    }

    @Override
    public OrderReport clone() throws CloneNotSupportedException {
        return (OrderReport) super.clone();
    }
}
