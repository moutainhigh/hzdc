package com.longan.mng.action.statistic.export;

import com.longan.biz.core.SupplyOrderService;
import com.longan.biz.dataobject.AreaInfo;
import com.longan.biz.dataobject.OrderReport;
import com.longan.biz.dataobject.SuppOrderReport;
import com.longan.biz.dataobject.UserInfo;
import com.longan.biz.utils.Constants;
import com.longan.biz.utils.DateTool;
import com.longan.biz.utils.ExcelUtil;
import com.longan.mng.action.common.BaseController;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ClassName: SupplyOrderReport
 * Description:
 * date: 2020/6/8 13:40
 *
 * @author 刘腾
 * @since JDK 1.8
 */
@Controller
@RequestMapping("/statistic/supplyOrderReportExport")
public class SupplyOrderReportExport extends BaseController {

    @Resource
    private SupplyOrderService supplyOrderService;
    //面值
    Map<Integer, String> itemFacePrice = Constants.ITEM_Face_Price;
    //地区设置
    Map<String, AreaInfo> provinceMap = new HashMap<String, AreaInfo>();

    @RequestMapping(method = RequestMethod.POST)
    public void onRequest(@ModelAttribute("suppOrderReport") SuppOrderReport suppOrderReport, HttpSession session, HttpServletResponse response){

        UserInfo userInfo = super.getUserInfo(session);

        suppOrderReport = DateTool.dateFilterSuppOrderReport(suppOrderReport);
        provinceMap = localCachedService.getProvinceMap();
        List<SuppOrderReport> suppOrderReportList = permutationOrder(suppOrderReport);

        String [][] content = new String[suppOrderReportList.size()][];

        //excel标题
        String[] title = {"下游代理商","上游供货商","省份","运营商","面值","销售","成本","附加费","利润"};

        //excel文件名
        String fileName = "供货单统计报表"+System.currentTimeMillis()+".xls";

        //sheet名
        String sheetName = "供货单统计报表";
        for (int i = 0; i < suppOrderReportList.size(); i++) {
            content[i] = new String[title.length];
            SuppOrderReport obj = suppOrderReportList.get(i);
            content[i][0] = obj.getUserName();
            content[i][1] = obj.getSupplyName();
            content[i][2] = obj.getSalesAreasName();
            content[i][3] = obj.getBizIdName();
            content[i][4] = obj.getFaceAmount();
            content[i][5] = obj.getSaleAmount();
            content[i][6] = obj.getCostAmount();
            content[i][7] = obj.getFeeAmount();
            content[i][8] = obj.getProfitAmount();
        }
        logger.warn("操作员:" + userInfo.getUserName() + "执行供货单报表导出操作。");
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

}
