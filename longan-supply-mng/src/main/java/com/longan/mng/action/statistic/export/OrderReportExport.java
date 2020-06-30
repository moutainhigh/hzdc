package com.longan.mng.action.statistic.export;

import com.longan.biz.core.BizOrderService;
import com.longan.biz.dataobject.AreaInfo;
import com.longan.biz.dataobject.OrderReport;
import com.longan.biz.dataobject.UserInfo;
import com.longan.biz.utils.Constants;
import com.longan.biz.utils.DateTool;
import com.longan.biz.utils.ExcelUtil;
import com.longan.mng.action.common.BaseController;
import com.longan.mng.action.statistic.QueryAllOrderReport;
//import org.apache.http.HttpResponse;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * ClassName: OrderReportExport
 * Description:
 * date: 2020/6/8 8:57
 *
 * @author 刘腾
 * @since JDK 1.8
 */
@Controller
@RequestMapping("/statistic/orderReportExport")
public class OrderReportExport extends BaseController {
    @Resource
    BizOrderService bizOrderService;
    //面值
    Map<Integer, String> itemFacePrice = Constants.ITEM_Face_Price;
    //运营商
    Map<String,String> mapCarrierType =  Constants.mapCarrierType;
    //地区 localCachedService.getProvinceMap();
    Map<String, AreaInfo> provinceMap = new HashMap<String, AreaInfo>();

    @RequestMapping(method = RequestMethod.POST)
    public void onRequest(@ModelAttribute("orderReport")OrderReport orderReport, HttpSession session, HttpServletResponse  response) {
        UserInfo userInfo = super.getUserInfo(session);

        //获取数据
        orderReport = DateTool.dateFilterOrderReport(orderReport);
        provinceMap = localCachedService.getProvinceMap();
//        QueryAllOrderReport queryAllOrderReport = new QueryAllOrderReport();
        List<OrderReport> orderReportList = permutationOrder(orderReport);
        String [][] content = new String[orderReportList.size()][];

        //excel标题
        String[] title = {"下游代理商","运营商","省份","面值","上游供货商","总订单数","成功订单数","成功率","3分钟成功回调数","3分钟到账率"};

        //excel文件名
        String fileName = "订单统计报表"+System.currentTimeMillis()+".xls";

        //sheet名
        String sheetName = "订单统计报表";
        for (int i = 0; i < orderReportList.size(); i++) {
            content[i] = new String[title.length];
            OrderReport obj = orderReportList.get(i);
            content[i][0] = obj.getUserName();
            content[i][1] = obj.getCarrierTypeName();
            content[i][2] = obj.getSalesAreasName();
            content[i][3] = obj.getItemFacePriceName();
            content[i][4] = obj.getSupplyName();
            content[i][5] = obj.getNowOrder().toString();
            content[i][6] = obj.getNowSuccessOrder().toString();
            content[i][7] = obj.getSuccessRate();
            content[i][8] = obj.getThreeSuccessOrder().toString();
            content[i][9] = obj.getThreeSuccessRate();
        }
        logger.warn("操作员:" + userInfo.getUserName() + "执行订单报表导出操作。");
        //创建HSSFWorkbook
        HSSFWorkbook wb = ExcelUtil.getHSSFWorkbook(sheetName, title, content, null);
        //响应到客户端
        try {
            this.setResponseHeader(response, fileName);
            OutputStream os = response.getOutputStream();
            wb.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        return "/statistic/queryAllOrderReport";
    }
    //发送响应流方法
    public void setResponseHeader(HttpServletResponse response, String fileName) {
        try {
            try {
                fileName = new String(fileName.getBytes(),"ISO8859-1");
            } catch (UnsupportedEncodingException e) {

                e.printStackTrace();
            }
            response.setContentType("application/octet-stream;charset=ISO8859-1");
            response.setHeader("Content-Disposition", "attachment;filename="+ fileName);
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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


}
