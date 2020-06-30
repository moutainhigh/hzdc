package com.longan.mng.action.statistic;

import com.longan.biz.core.SupplyOrderService;
import com.longan.biz.dataobject.AreaInfo;
import com.longan.biz.dataobject.OrderReport;
import com.longan.biz.dataobject.SuppOrderReport;
import com.longan.biz.dataobject.UserInfo;
import com.longan.biz.domain.Result;
import com.longan.biz.sumobject.SupplyOrderAmount;
import com.longan.biz.utils.Constants;
import com.longan.biz.utils.DateTool;
import com.longan.mng.action.common.BaseController;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.util.*;

/**
 * ClassName: QueryAllSupplyOrderReport
 * Description:
 * date: 2020/5/29 10:10
 *
 * @author 刘腾
 * @since JDK 1.8
 */
@Controller
public class QueryAllSupplyOrderReport extends BaseController {
    @Resource
    private SupplyOrderService supplyOrderService;
    //面值
    Map<Integer, String> itemFacePrice = Constants.ITEM_Face_Price;
//    //运营商
//    Map<String,String> mapCarrierType =  Constants.mapCarrierType;
    //地区 localCachedService.getProvinceMap();
    Map<String, AreaInfo> provinceMap = new HashMap<String, AreaInfo>();
    @RequestMapping(value = "statistic/queryAllSupplyOrderReport",method = RequestMethod.GET)
    public String queryAllOrderReportIndex(@ModelAttribute("suppOrderReport") SuppOrderReport suppOrderReport, Model model, HttpSession session) {
        UserInfo userInfo = super.getUserInfo(session);
        model.addAttribute("userInfo", userInfo);
        suppOrderReport.setStartGmtCreate(new Date());
        suppOrderReport.setEndGmtCreate(new Date());
        SuppOrderReport report = DateTool.dateFilterSuppOrderReport(suppOrderReport);
        return "statistic/queryAllSupplyOrderReport";
    }
    @RequestMapping(value = "statistic/queryAllSupplyOrderReport",method = RequestMethod.POST)
    public String queryAllOrderReport(@ModelAttribute("suppOrderReport")  SuppOrderReport suppOrderReport,Model model, HttpSession session) {
        UserInfo userInfo = super.getUserInfo(session);
        provinceMap = localCachedService.getProvinceMap();
        List<SuppOrderReport> suppOrderReportList = new ArrayList<SuppOrderReport>();
        //上游添加
        model.addAttribute("upStreamUser", localCachedService.getUpStreamUser());
        //调用前的判断
        if (suppOrderReport.getStartGmtCreate()!=null || suppOrderReport.getEndGmtCreate()!=null) {
            Date date = null;
            try {
                date = DateTool.strintToDate(DateTool.parseDate(new Date()));
            } catch (ParseException e) {
            }
            if (suppOrderReport.getEndGmtCreate() == null) {
                suppOrderReport.setEndGmtCreate(date);
            }
            if (suppOrderReport.getStartGmtCreate() == null) {
                suppOrderReport.setStartGmtCreate(date);
            }
            if (!check2Date(suppOrderReport.getStartGmtCreate(), suppOrderReport.getEndGmtCreate())) {
                alertErrorNoneBack(model, "订单时间区间不能超过2个月");
                return "statistic/queryAllSupplyOrderReport";
            }
        }
        suppOrderReport = DateTool.dateFilterSuppOrderReport(suppOrderReport);
        suppOrderReportList = permutationOrder(suppOrderReport);
        model.addAttribute("suppOrderReportList",suppOrderReportList);
        return  "statistic/queryAllSupplyOrderReport";
    }

    public List<SuppOrderReport> permutationOrder (SuppOrderReport suppOrderReport) {
        List<SuppOrderReport> suppOrderReportList = new ArrayList<SuppOrderReport>();
        //省份不为空时 下游还未判断进来
        suppOrderReport.setStatus("2");
        //下游设置名称
        if (StringUtils.hasText(suppOrderReport.getUserId())) {
            Map<Long, String> downStreamUser = localCachedService.getDownStreamUser();
            Long key = Long.valueOf(suppOrderReport.getUserId());
            String value = downStreamUser.get(key);
            suppOrderReport.setUserName(value);
        }
        //上游设置名称
        if (StringUtils.hasText(suppOrderReport.getSupplyTraderId())) {
            Map<Long, String> upStreamUser = localCachedService.getUpStreamUser();
            String key = suppOrderReport.getSupplyTraderId();
            String value = upStreamUser.get(Long.valueOf(key));
            suppOrderReport.setSupplyName(value);
        }
        //运营商设置
        if (suppOrderReport.getBizId()!=null) {
            Map<Integer, String> mapCarrierTypeSupp = Constants.mapCarrierTypeSupp;
            String value = mapCarrierTypeSupp.get(suppOrderReport.getBizId());
            suppOrderReport.setBizIdName(value);
        }
        //省份不为空
        if (suppOrderReport.getSalesAreas() != null && suppOrderReport.getItemFaceprice() == null ) {
            suppOrderReportList = one(suppOrderReport, suppOrderReportList);
        }
        //面值不为空，其他为空
        if (suppOrderReport.getItemFaceprice() != null && suppOrderReport.getSalesAreas() == null ) {
            suppOrderReportList = two(suppOrderReport, suppOrderReportList);
        }
        //省份与面值
        if (suppOrderReport.getSalesAreas() != null && suppOrderReport.getItemFaceprice() != null) {
            suppOrderReportList = six(suppOrderReport, suppOrderReportList);
        }
        //省份，面值，都没有选择
        if(suppOrderReport.getSalesAreas()==null && suppOrderReport.getItemFaceprice()==null) {
            suppOrderReportList = eight(suppOrderReport, suppOrderReportList);
        }
        return suppOrderReportList;
    }
    //单独省份
    public List<SuppOrderReport> one(SuppOrderReport suppOrderReport,List<SuppOrderReport> suppOrderReportList){
        //可以不new的
        SuppOrderReport suppOrderReportAdd = null;
        try {
        suppOrderReportAdd = suppOrderReport.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        for (String itme : suppOrderReport.getSalesAreas()) {
            //省份添加
            suppOrderReportAdd.setSalesAreasId(itme);
                //面值
                for (Integer facePrice : itemFacePrice.keySet()) {
                    suppOrderReportAdd.setItemFaceprice(facePrice);
                    //统计
                    SuppOrderReport suppOrderReport1 = supplyOrderService.querySupplyOrderReportSum(suppOrderReportAdd);
                    suppOrderReport1.setUserName(suppOrderReportAdd.getUserName());
                    suppOrderReport1.setSupplyName(suppOrderReportAdd.getSupplyName());
                    suppOrderReport1.setBizIdName(suppOrderReportAdd.getBizIdName());
                    //名称设置
                    suppOrderReport1.setSalesAreasName(provinceMap.get(itme).toString());
                    //面值
                    suppOrderReport1.setItemFacePriceName(itemFacePrice.get(facePrice));

                    suppOrderReportList.add(suppOrderReport1);
                }
        }
        return suppOrderReportList;
    }
    //单独面值
    public List<SuppOrderReport>  two(SuppOrderReport suppOrderReport,List<SuppOrderReport> suppOrderReportList) {
        SuppOrderReport suppOrderReportAdd = null;
        try {
            suppOrderReportAdd = suppOrderReport.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        //设置面值
        suppOrderReportAdd.setItemFaceprice(suppOrderReport.getItemFaceprice());
        //地区参数
        for (String areaInfo : provinceMap.keySet()) {
            //id
            suppOrderReportAdd.setSalesAreasId(areaInfo);
            //统计
            SuppOrderReport suppOrderReport1 = supplyOrderService.querySupplyOrderReportSum(suppOrderReportAdd);
            //名称设置
            suppOrderReport1.setSalesAreasName(provinceMap.get(areaInfo).toString());
            suppOrderReport1.setUserName(suppOrderReportAdd.getUserName());
            suppOrderReport1.setSupplyName(suppOrderReportAdd.getSupplyName());
            suppOrderReport1.setBizIdName(suppOrderReportAdd.getBizIdName());
            //面值名称
            suppOrderReport1.setItemFacePriceName(itemFacePrice.get(suppOrderReport.getItemFaceprice()));
            suppOrderReportList.add(suppOrderReport1);
        }
        //}
        return suppOrderReportList;
    }
    //省份，面值
    public List<SuppOrderReport> six(SuppOrderReport suppOrderReport,List<SuppOrderReport> suppOrderReportList) {
        SuppOrderReport suppOrderReportAdd = null;
        try {
            suppOrderReportAdd = suppOrderReport.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        //面值
        suppOrderReportAdd.setItemFaceprice(suppOrderReport.getItemFaceprice());
        //地区遍历
        for (String itme : suppOrderReport.getSalesAreas()) {
            //省份添加
            suppOrderReportAdd.setSalesAreasId(itme);
            SuppOrderReport suppOrderReport1 = supplyOrderService.querySupplyOrderReportSum(suppOrderReportAdd);
            //名称设置
            suppOrderReport1.setSalesAreasName(provinceMap.get(itme).toString());
            suppOrderReport1.setUserName(suppOrderReportAdd.getUserName());
            suppOrderReport1.setSupplyName(suppOrderReportAdd.getSupplyName());
            suppOrderReport1.setBizIdName(suppOrderReportAdd.getBizIdName());
            //面值名称设置
            suppOrderReport1.setItemFacePriceName(itemFacePrice.get(suppOrderReport.getItemFaceprice()));
            suppOrderReportList.add(suppOrderReport1);
        }
        return suppOrderReportList;
    }

    //面值，运营商，省份，都没有选
    public List<SuppOrderReport> eight(SuppOrderReport suppOrderReport,List<SuppOrderReport> suppOrderReportList) {
        SuppOrderReport suppOrderReportAdd = null;
        try {
            suppOrderReportAdd = suppOrderReport.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        for (String itme : provinceMap.keySet()) {
            //省份添加
            suppOrderReportAdd.setSalesAreasId(itme);
                //面值
                for (Integer facePrice : itemFacePrice.keySet()) {
                    suppOrderReportAdd.setItemFaceprice(facePrice);
                    //面值名称
                    SuppOrderReport suppOrderReport1 = supplyOrderService.querySupplyOrderReportSum(suppOrderReportAdd);
                    //名称设置
                    suppOrderReportAdd.setSalesAreasName(provinceMap.get(itme).toString());
                    suppOrderReport1.setUserName(suppOrderReportAdd.getUserName());
                    suppOrderReport1.setSupplyName(suppOrderReportAdd.getSupplyName());
                    suppOrderReport1.setBizIdName(suppOrderReportAdd.getBizIdName());
                    //面值名称设置
                    suppOrderReport1.setItemFacePriceName(itemFacePrice.get(facePrice));
                    suppOrderReportList.add(suppOrderReport1);
                }
        }
        //}
        return suppOrderReportList;
    }



    @ModelAttribute("provinceList")
    //省份集合
    public Map<String, AreaInfo> provinceList() {
        return localCachedService.getProvinceMap();
    }
    //供货单面值
    @ModelAttribute("itemFacePriceList")
    public Map<Integer,String> itemFacePriceList() {
        Map<Integer, String> item_face_price = Constants.ITEM_Face_Price;
        return item_face_price;
    }
    //运营商集合
    @ModelAttribute("bizList")
    public Map<Integer, String> carrierTypeList() {
        Map<Integer, String> mapCarrierTypeSupp = Constants.mapCarrierTypeSupp;
        return mapCarrierTypeSupp;
    }
    //    下游集合
    @ModelAttribute("downStreamUser")
    public Map<Long,String> getDownStreamUser() {
        Map<Long, String> downStreamUser = localCachedService.getDownStreamUser();
        return downStreamUser;
    }
    //    上游游集合
    @ModelAttribute("upStreamUser")
    public Map<Long,String> getUpStreamUser() {
        Map<Long, String> upStreamUser = localCachedService.getUpStreamUser();
        return upStreamUser;
    }
}
