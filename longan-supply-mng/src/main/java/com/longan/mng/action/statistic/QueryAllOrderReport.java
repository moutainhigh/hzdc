package com.longan.mng.action.statistic;

import com.longan.biz.core.BizOrderService;
import com.longan.biz.dataobject.AreaInfo;
import com.longan.biz.dataobject.OrderReport;
import com.longan.biz.dataobject.UserInfo;
import com.longan.biz.domain.ComparatorNowOrder;
import com.longan.biz.utils.Constants;
import com.longan.biz.utils.DateTool;
import com.longan.mng.action.common.BaseController;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * ClassName: queryAllOrderReport
 * Description:
 * date: 2020/5/18 10:32
 *
 * @author 刘腾
 * @since JDK 1.8
 */
@Controller
public class QueryAllOrderReport extends BaseController {
    @Resource
    BizOrderService bizOrderService;
    //面值
    Map<Integer, String> itemFacePrice = Constants.ITEM_Face_Price;
    //运营商
    Map<String,String> mapCarrierType =  Constants.mapCarrierType;
    //地区 localCachedService.getProvinceMap();
    Map<String, AreaInfo> provinceMap = new HashMap<String, AreaInfo>();

    @RequestMapping(value = "/statistic/queryAllOrderReport",method = RequestMethod.GET)
    public String queryAllOrderReportIndex(@ModelAttribute("orderReport") OrderReport orderReport,Model model, HttpSession session) {
        UserInfo userInfo = super.getUserInfo(session);
        model.addAttribute("userInfo", userInfo);
        //上游添加
        model.addAttribute("upStreamUser", localCachedService.getUpStreamUser());
        orderReport.setStartGmtCreate(new Date());
        orderReport.setEndGmtCreate(new Date());
        OrderReport report = DateTool.dateFilterOrderReport(orderReport);
        //查询当天总订单数
//        setOrder(model,report);
        return "statistic/queryAllOrderReport";
    }
    @RequestMapping(value = "/statistic/queryAllOrderReport",method = RequestMethod.POST)
    public String queryAllOrderReport(@ModelAttribute("orderReport") OrderReport orderReport,Model model, HttpSession session) {
        UserInfo userInfo = super.getUserInfo(session);
        model.addAttribute("userInfo", userInfo);
        provinceMap = localCachedService.getProvinceMap();
        List<OrderReport> orderReportList = new ArrayList<OrderReport>();
        //上游添加
        model.addAttribute("upStreamUser", localCachedService.getUpStreamUser());
        //调用前的判断
        if (orderReport.getStartGmtCreate()!=null || orderReport.getEndGmtCreate()!=null) {
            Date date = null;
            try {
                date = DateTool.strintToDate(DateTool.parseDate(new Date()));
            } catch (ParseException e) {
            }
            if (orderReport.getEndGmtCreate() == null) {
                orderReport.setEndGmtCreate(date);
            }
            if (orderReport.getStartGmtCreate() == null) {
                orderReport.setStartGmtCreate(date);
            }
            if (!check2Date(orderReport.getStartGmtCreate(), orderReport.getEndGmtCreate())) {
                alertErrorNoneBack(model, "订单时间区间不能超过2个月");
                return "statistic/queryAllOrderReport";
            }
        }
        //时间转换
        orderReport = DateTool.dateFilterOrderReport(orderReport);
        //是否统计隔天订单
        if (StringUtils.hasText(orderReport.getStatistic())) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(orderReport.getStartGmtCreate());
            calendar.add(Calendar.DAY_OF_MONTH, +1);
            orderReport.setStatisticDate(calendar.getTime());
        }
        //查询条件选择总订单数
        List<OrderReport> orderReportList1 = permutationOrder(orderReport);
        OrderReport linkReport = new OrderReport();
        linkReport = setVal(linkReport);
        OrderReport moveReport = new OrderReport();
        moveReport = setVal(moveReport);
        OrderReport telecomReport = new OrderReport();
        telecomReport = setVal(telecomReport);
        //可以抽取出来但是没必要
        for (OrderReport report : orderReportList1) {
            //联通
            if (Constants.Item.CARRIER_TYPE_UNICOM_DESC.equals(report.getCarrierTypeName())) {
                linkReport.setNowOrder(linkReport.getNowOrder()+report.getNowOrder());
                linkReport.setNowSuccessOrder(linkReport.getNowSuccessOrder()+report.getNowSuccessOrder());
                linkReport.setThreeSuccessOrder(linkReport.getThreeSuccessOrder()+report.getThreeSuccessOrder());
            }
            //电信
            if (Constants.Item.CARRIER_TYPE_TELECOM_DESC.equals(report.getCarrierTypeName())) {
                telecomReport.setNowOrder(telecomReport.getNowOrder()+report.getNowOrder());
                telecomReport.setNowSuccessOrder(telecomReport.getNowSuccessOrder()+report.getNowSuccessOrder());
                telecomReport.setThreeSuccessOrder(telecomReport.getThreeSuccessOrder()+report.getThreeSuccessOrder());
            }
            //移动
            if (Constants.Item.CARRIER_TYPE_MOBILE_DESC.equals(report.getCarrierTypeName())) {
                moveReport.setNowOrder(moveReport.getNowOrder()+report.getNowOrder());
                moveReport.setNowSuccessOrder(moveReport.getNowSuccessOrder()+report.getNowSuccessOrder());
                moveReport.setThreeSuccessOrder(moveReport.getThreeSuccessOrder()+report.getThreeSuccessOrder());
            }
        }
        if (linkReport.getNowOrder()!=0) {
            OrderReport report = successReta(linkReport);
            model.addAttribute("linkReport",report);
        }
        if (moveReport.getNowOrder()!=0) {
            OrderReport report = successReta(moveReport);
            model.addAttribute("moveReport", report);
        }
        if (telecomReport.getNowOrder()!=0) {
            OrderReport report = successReta(telecomReport);
            model.addAttribute("telecomReport", report);
        }

        //集合排序
        Collections.sort(orderReportList1,new ComparatorNowOrder());
        model.addAttribute("orderReportList",orderReportList1);

        return "statistic/queryAllOrderReport";
    }
    public List<OrderReport> permutationOrder (OrderReport orderReport) {
        List<OrderReport> orderReportList = new ArrayList<OrderReport>();
        //省份不为空时 下游还未判断进来
        //下游设置名称
        if (StringUtils.hasText(orderReport.getUserId())) {
            Map<Long, String> downStreamUser = localCachedService.getDownStreamUser();
            Long key = Long.valueOf(orderReport.getUserId());
            String value = downStreamUser.get(key);
            orderReport.setUserName(value);
        }
        //上游设置名称
        if (StringUtils.hasText(orderReport.getSupplyTraderId())) {
            Map<Long, String> upStreamUser = localCachedService.getUpStreamUser();
            String key = orderReport.getSupplyTraderId();
            String value = upStreamUser.get(Long.valueOf(key));
            orderReport.setSupplyName(value);
        }
        if (orderReport.getSalesAreas() != null && orderReport.getItemFaceprice() == null && !StringUtils.hasText(orderReport.getCarrierType())) {
            orderReportList = one(orderReport, orderReportList);
        }
        //面值不为空，其他为空
        if (orderReport.getItemFaceprice() != null && orderReport.getSalesAreas() == null && !StringUtils.hasText(orderReport.getCarrierType())) {
            orderReportList = two(orderReport, orderReportList);
        }
        //单独选运营商
        if (StringUtils.hasText(orderReport.getCarrierType()) && orderReport.getItemFaceprice() == null && orderReport.getSalesAreas()==null) {
            orderReportList = three(orderReport, orderReportList);
        }
        //省份与运营商
        if (StringUtils.hasText(orderReport.getCarrierType())&& orderReport.getSalesAreas() != null && orderReport.getItemFaceprice() == null) {
            orderReportList = four(orderReport, orderReportList);
        }
        //省份与运营商与面值
        if (StringUtils.hasText(orderReport.getCarrierType()) && orderReport.getSalesAreas() != null && orderReport.getItemFaceprice() != null) {
            orderReportList = five(orderReport, orderReportList);
        }
        //省份与面值
        if (!StringUtils.hasText(orderReport.getCarrierType()) && orderReport.getSalesAreas() != null && orderReport.getItemFaceprice() != null) {
            orderReportList = six(orderReport, orderReportList);
        }
        //运营商跟面值
        if (StringUtils.hasText(orderReport.getCarrierType()) && orderReport.getSalesAreas() == null && orderReport.getItemFaceprice() != null) {
            orderReportList = seven(orderReport, orderReportList);
        }
        //运营商，省份，面值，都没有选择
        if(!StringUtils.hasText(orderReport.getCarrierType()) && orderReport.getSalesAreas()==null && orderReport.getItemFaceprice()==null) {
            orderReportList = eight(orderReport, orderReportList);
        }
        return orderReportList;
    }

    //单独省份
    public List<OrderReport> one(OrderReport orderReport,List<OrderReport> orderReportList){
        //可以不new的
        OrderReport orderReportAdd = null;
        try {
            orderReportAdd = orderReport.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        orderReportAdd.setStartGmtCreate(orderReport.getStartGmtCreate());
        orderReportAdd.setEndGmtCreate(orderReport.getEndGmtCreate());
        for (String itme : orderReport.getSalesAreas()) {
            //省份添加
            orderReportAdd.setSalesAreasId(itme);
            //名称设置
            orderReportAdd.setSalesAreasName(provinceMap.get(itme).toString());
            //查询每个面值的不同运营商的成功率
            for (String carrtype :  mapCarrierType.keySet()) {
                //运营商编号
                orderReportAdd.setCarrierType(carrtype);
                //运营商名称
                orderReportAdd.setCarrierTypeName(mapCarrierType.get(carrtype));
                //面值
                for (Integer facePrice : itemFacePrice.keySet()) {
                    orderReportAdd.setItemFaceprice(facePrice);
                    orderReportAdd.setItemFacePriceName(itemFacePrice.get(facePrice));
                    //总订单数
                    OrderReport orderReport1 = sunReta(orderReportAdd);
                    OrderReport orderReport2 = successReta(orderReport1);
                    orderReportList.add(orderReport2);
                }
            }
        }
        //}
        return orderReportList;
    }
    //单独面值
    public List<OrderReport> two(OrderReport orderReport,List<OrderReport> orderReportList) {
        OrderReport orderReportAdd = null;
        try {
            orderReportAdd = orderReport.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
//            orderReportAdd.setStartGmtCreate(orderReport.getStartGmtCreate());
//            orderReportAdd.setEndGmtCreate(orderReport.getEndGmtCreate());
        //设置面值
        orderReportAdd.setItemFaceprice(orderReport.getItemFaceprice());
        //面值名称
        orderReportAdd.setItemFacePriceName(itemFacePrice.get(orderReport.getItemFaceprice()));
        //地区参数
        for (String areaInfo : provinceMap.keySet()) {
            //id
            orderReportAdd.setSalesAreasId(areaInfo);
            //名称设置
            orderReportAdd.setSalesAreasName(provinceMap.get(areaInfo).toString());
            //运营商设置
            for (String carrtype :  mapCarrierType.keySet()) {
                orderReportAdd.setCarrierType(carrtype);
                //运营商名称
                orderReportAdd.setCarrierTypeName(mapCarrierType.get(carrtype));
                //总订单数
                OrderReport orderReport1 = sunReta(orderReportAdd);
                OrderReport orderReport2 = successReta(orderReport1);
                orderReportList.add(orderReport2);
            }
        }
        //}
        return orderReportList;
    }
    //单独运营商
    public List<OrderReport> three(OrderReport orderReport,List<OrderReport> orderReportList) {
        OrderReport orderReportAdd = null;
        try {
            orderReportAdd = orderReport.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
//            orderReportAdd.setStartGmtCreate(orderReport.getStartGmtCreate());
//            orderReportAdd.setEndGmtCreate(orderReport.getEndGmtCreate());
        //运营商
        orderReportAdd.setCarrierType(orderReport.getCarrierType());
        //运营商名称
        orderReportAdd.setCarrierTypeName(mapCarrierType.get(orderReport.getCarrierType()));
        //地区参数
        for (String areaInfo : provinceMap.keySet()) {
            //id
            orderReportAdd.setSalesAreasId(areaInfo);
            //名称设置
            orderReportAdd.setSalesAreasName(provinceMap.get(areaInfo).toString());
            for (Integer facePrice : itemFacePrice.keySet()) {
                orderReportAdd.setItemFaceprice(facePrice);
                //具体面值设置
                orderReportAdd.setItemFacePriceName(itemFacePrice.get(facePrice));
                //总订单数
                OrderReport orderReport1 = sunReta(orderReportAdd);
                OrderReport orderReport2 = successReta(orderReport1);
                orderReportList.add(orderReport2);
            }
        }
        // }
        return orderReportList;
    }
    //运营商跟省份
    public List<OrderReport> four(OrderReport orderReport,List<OrderReport> orderReportList) {
        OrderReport orderReportAdd = null;
        try {
            orderReportAdd = orderReport.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
//            orderReportAdd.setStartGmtCreate(orderReport.getStartGmtCreate());
//            orderReportAdd.setEndGmtCreate(orderReport.getEndGmtCreate());
        //运营商
        orderReportAdd.setCarrierType(orderReport.getCarrierType());
        //运营商名称
        orderReportAdd.setCarrierTypeName(mapCarrierType.get(orderReport.getCarrierType()));
        //地区遍历
        for (String itme : orderReport.getSalesAreas()) {
            //省份添加
            orderReportAdd.setSalesAreasId(itme);
            //名称设置
            orderReportAdd.setSalesAreasName(provinceMap.get(itme).toString());
            //面值设置
            for (Integer facePrice : itemFacePrice.keySet()) {
                orderReportAdd.setItemFaceprice(facePrice);
                //具体面值设置
                orderReportAdd.setItemFacePriceName(itemFacePrice.get(facePrice));
                //总订单数
                OrderReport orderReport1 = sunReta(orderReportAdd);
                OrderReport orderReport2 = successReta(orderReport1);
                orderReportList.add(orderReport2);

            }
        }
        //}
        return orderReportList;
    }
    //运营商，省份，面值
    public List<OrderReport> five(OrderReport orderReport,List<OrderReport> orderReportList) {
        OrderReport orderReportAdd = null;
        try {
            orderReportAdd = orderReport.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
//            orderReportAdd.setStartGmtCreate(orderReport.getStartGmtCreate());
//            orderReportAdd.setEndGmtCreate(orderReport.getEndGmtCreate());
        //运营商
        orderReportAdd.setCarrierType(orderReport.getCarrierType());
        orderReportAdd.setCarrierTypeName(mapCarrierType.get(orderReport.getCarrierType()));
        //面值
        orderReportAdd.setItemFaceprice(orderReport.getItemFaceprice());
        orderReportAdd.setItemFacePriceName(itemFacePrice.get(orderReport.getItemFaceprice()));
        //地区遍历
        for (String itme : orderReport.getSalesAreas()) {
            //省份添加
            orderReportAdd.setSalesAreasId(itme);
            //名称设置
            orderReportAdd.setSalesAreasName(provinceMap.get(itme).toString());
            //总订单数
            OrderReport orderReport1 = sunReta(orderReportAdd);
            OrderReport orderReport2 = successReta(orderReport1);
            orderReportList.add(orderReport2);
        }
        //}
        return orderReportList;
    }
    //省份，面值
    public List<OrderReport> six(OrderReport orderReport,List<OrderReport> orderReportList) {
        OrderReport orderReportAdd = null;
        try {
            orderReportAdd = orderReport.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
//            orderReportAdd.setStartGmtCreate(orderReport.getStartGmtCreate());
//            orderReportAdd.setEndGmtCreate(orderReport.getEndGmtCreate());
        //面值
        orderReportAdd.setItemFaceprice(orderReport.getItemFaceprice());
        orderReportAdd.setItemFacePriceName(itemFacePrice.get(orderReport.getItemFaceprice()));
        //地区遍历
        for (String itme : orderReport.getSalesAreas()) {
            //省份添加
            orderReportAdd.setSalesAreasId(itme);
            //名称设置
            orderReportAdd.setSalesAreasName(provinceMap.get(itme).toString());
            //运营商遍历
            for (String carrtype :  mapCarrierType.keySet()) {
                orderReportAdd.setCarrierType(carrtype);
                //运营商名称
                orderReportAdd.setCarrierTypeName(mapCarrierType.get(carrtype));
                OrderReport orderReport1 = sunReta(orderReportAdd);
                OrderReport orderReport2 = successReta(orderReport1);
                orderReportList.add(orderReport2);
            }
        }
        //}
        return orderReportList;
    }
    //运营商，面值
    public List<OrderReport> seven(OrderReport orderReport,List<OrderReport> orderReportList) {

        OrderReport orderReportAdd = null;
        try {
            orderReportAdd = orderReport.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
//            orderReportAdd.setStartGmtCreate(orderReport.getStartGmtCreate());
//            orderReportAdd.setEndGmtCreate(orderReport.getEndGmtCreate());
        //运营商
        orderReportAdd.setCarrierType(orderReport.getCarrierType());
        //运营商名称
        orderReportAdd.setCarrierTypeName(mapCarrierType.get(orderReport.getCarrierType()));
        //面值
        orderReportAdd.setItemFaceprice(orderReport.getItemFaceprice());
        //面值名称
        orderReportAdd.setItemFacePriceName(itemFacePrice.get(orderReport.getItemFaceprice()));
        //地区遍历
        for (String areaInfo : provinceMap.keySet()) {
            //id
            orderReportAdd.setSalesAreasId(areaInfo);
            //名称设置
            orderReportAdd.setSalesAreasName(provinceMap.get(areaInfo).toString());
            //设置数据
            OrderReport orderReport1 = sunReta(orderReportAdd);
            OrderReport orderReport2 = successReta(orderReport1);
            orderReportList.add(orderReport2);
        }
        //}
        return orderReportList;
    }

    //面值，运营商，省份，都没有选
    public List<OrderReport> eight(OrderReport orderReport,List<OrderReport> orderReportList) {
        OrderReport orderReportAdd = null;
        try {
            orderReportAdd = orderReport.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
//        orderReportAdd.setStartGmtCreate(orderReport.getStartGmtCreate());
//        orderReportAdd.setEndGmtCreate(orderReport.getEndGmtCreate());
        for (String itme : provinceMap.keySet()) {
            //省份添加
            orderReportAdd.setSalesAreasId(itme);
            //名称设置
            orderReportAdd.setSalesAreasName(provinceMap.get(itme).toString());
            //查询每个面值的不同运营商的成功率
            for (String carrtype :  mapCarrierType.keySet()) {
                //运营商编号
                orderReportAdd.setCarrierType(carrtype);
                //运营商名称
                orderReportAdd.setCarrierTypeName(mapCarrierType.get(carrtype));
                //面值
                for (Integer facePrice : itemFacePrice.keySet()) {
                    orderReportAdd.setItemFaceprice(facePrice);
                    //面值名称
                    orderReportAdd.setItemFacePriceName(itemFacePrice.get(facePrice));
                    //总订单数
                    OrderReport orderReport1 = sunReta(orderReportAdd);
                    OrderReport orderReport2 = successReta(orderReport1);
                    orderReportList.add(orderReport2);
                }
            }
        }
        //}
        return orderReportList;
    }

    //订单统计
    public OrderReport sunReta(final OrderReport orderReportAdd) {
        final OrderReport orderReport2 = new OrderReport();
        final OrderReport orderReport3 =  new OrderReport();
        BeanUtils.copyProperties(orderReportAdd,orderReport2);
        BeanUtils.copyProperties(orderReportAdd,orderReport3);
        Future<Integer> orderCount = Constants.threadPoolExecutor.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return bizOrderService.selectReportCount(orderReportAdd);
            }
        });
        Future<Integer> successOrder = Constants.threadPoolExecutor.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                orderReport2.setStatus("2");
                return bizOrderService.selectReportCount(orderReport2);
            }
        });
        Future<Integer> threeSuccessOrder = Constants.threadPoolExecutor.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                orderReport3.setStatus("2");
                orderReport3.setMoreCostTime("0");
                orderReport3.setLessCostTime("180");
                return bizOrderService.selectReportCount(orderReport3);
            }
        });
        //三分钟回调的总订单数
        try {
            orderReportAdd.setNowOrder(orderCount.get());
            orderReportAdd.setNowSuccessOrder(successOrder.get());
            orderReportAdd.setThreeSuccessOrder(threeSuccessOrder.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        orderReportAdd.setStatus(null);
        //
        orderReportAdd.setMoreCostTime(null);
        orderReportAdd.setLessCostTime(null);
//        int threeOrderCount = bizOrderService.selectReportCount(orderReportAdd);
//        orderReportAdd.setCountThreeOrder(threeOrderCount);
        OrderReport clone = null;
        try {
            clone =orderReportAdd.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return clone;
    }
    //成功回调数
    public OrderReport successReta(OrderReport orderReport) {
        //俩位小数
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        //三分钟成功率计算
        if (orderReport.getNowOrder()!=0 && orderReport.getThreeSuccessOrder()!=0){
            if (orderReport.getThreeSuccessOrder().equals(orderReport.getNowOrder())) {
//            double t = countThreeOrder/nowOrder;
                orderReport.setThreeSuccessRate("100%");
            }else {
                Double now = Double.valueOf(Integer.valueOf(orderReport.getThreeSuccessOrder()).toString());
                Double end = Double.valueOf(Integer.valueOf(orderReport.getNowOrder()).toString());
                double t = (now/end)*100;
                orderReport.setThreeSuccessRate(decimalFormat.format(t)+"%");
            }
        }else {
            orderReport.setThreeSuccessRate(0+"%");
        }
        //总成功率
        if (orderReport.getNowOrder()!=0 && orderReport.getNowSuccessOrder()!=0) {
            if (orderReport.getNowSuccessOrder().equals(orderReport.getNowOrder())) {
                orderReport.setSuccessRate("100%");
            }else {
                Double now = Double.valueOf(Integer.valueOf(orderReport.getNowSuccessOrder()).toString());
                Double end = Double.valueOf(Integer.valueOf(orderReport.getNowOrder()).toString());
                double d = (now/end)*100;
                orderReport.setSuccessRate(decimalFormat.format(d)+"%");
            }
        }else{
            orderReport.setSuccessRate(0+"%");
        }

        return orderReport;
    }
    public OrderReport setVal(OrderReport orderReport){
        orderReport.setNowOrder(0);
        orderReport.setNowSuccessOrder(0);
        orderReport.setCountThreeOrder(0);
        orderReport.setThreeSuccessOrder(0);
        return orderReport;
    }
    public OrderReport setValNull(OrderReport orderReport){
        if(orderReport.getNowOrder()==0) {
            return null;
        }
        if (orderReport.getNowSuccessOrder()==0) {
            return null;
        }
        if (orderReport.getCountThreeOrder()==0) {
            return null;
        }
        if (orderReport.getThreeSuccessOrder()==0) {
            return null;
        }
        return orderReport;
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
    @ModelAttribute("carrierTypeList")
    public Map<String, String> carrierTypeList() {
        Map<String, String> mapType = new LinkedHashMap<String, String>();
        mapType.put(Constants.Item.CARRIER_TYPE_MOBILE + "", Constants.Item.CARRIER_TYPE_MOBILE_DESC);
        mapType.put(Constants.Item.CARRIER_TYPE_TELECOM + "", Constants.Item.CARRIER_TYPE_TELECOM_DESC);
        mapType.put(Constants.Item.CARRIER_TYPE_UNICOM + "", Constants.Item.CARRIER_TYPE_UNICOM_DESC);
//        mapType.put(Constants.Item.CARRIER_TYPE_OTHER + "", Constants.Item.CARRIER_TYPE_OTHER_DESC);
        return mapType;
    }
    //隔天回调集合statisticList
    @ModelAttribute("statisticList")
    public Map<String,String> statisticList() {
        Map<String, String> mapType = new LinkedHashMap<String, String>();
        mapType.put("ok","是");
        return mapType;

    }
    //    下游集合
    @ModelAttribute("downStreamUser")
    public Map<Long,String> getDownStreamUser() {
        Map<Long, String> downStreamUser = localCachedService.getDownStreamUser();
        return downStreamUser;
    }
}
