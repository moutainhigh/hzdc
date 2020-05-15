package com.longan.mng.action.biz;

import com.longan.biz.core.BizOrderService;
import com.longan.biz.core.BlackListService;
import com.longan.biz.dataobject.AreaInfo;
import com.longan.biz.dataobject.BizOrder;
import com.longan.biz.dataobject.BlackList;
import com.longan.biz.dataobject.UserInfo;
import com.longan.biz.domain.Result;
import com.longan.mng.action.common.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
//import org.springframework.util.StringUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * ClassName: BlackList
 * Description:
 * date: 2020/5/2 0:17
 *
 * @author 刘腾
 * @since JDK 1.8//环境变量的关系
 */
@Controller
public class BlackListDeal extends BaseController {
    @Resource
    private BlackListService blackListService;

    @Resource
    private BizOrderService bizOrderService;

    @RequestMapping(value = "biz/queryBlackList")
    public String queryBlackList(@ModelAttribute("blackListQuery") BlackList blackList, Model model, HttpSession session){

        Result<List<BizOrder>> demoResult = bizOrderService.queryByPageDayThree();
        //System.out.println(demoResult.getModule().get(0).getId());
        //拼接订单id
        StringBuilder builder = new StringBuilder();
        int length = demoResult.getModule().size();
        //订单id拼接
        for (int i = 0 ; i < length; i++) {
            builder.append(demoResult.getModule().get(i).getId());
            if (i !=  length-1) {
                builder.append(",");
            }
        }





        //下面的不能动
        String cityNameKeep = blackList.getCityName();
        UserInfo userInfo = getUserInfo(session);//获取到用户信息
        if (StringUtils.hasText(blackList.getCityName())) {
            Map<String, AreaInfo> provinceMap = localCachedService.getProvinceMap();
            AreaInfo areaInfo = provinceMap.get(blackList.getCityName());
            blackList.setCityName(areaInfo.toString());
        }
        Result<List<BlackList>> listResult = blackListService.queryBlackList(blackList);
        if (listResult.isSuccess()) {
            blackList.setCityName(cityNameKeep);
            model.addAttribute("blackLists",listResult.getModule());
        }

        return "biz/BlackListDeal";
    }
    @RequestMapping(value = "biz/deleteBlack")
    public void deleteBlack(String id,Model model) {
        try {
            blackListService.deleteBlack(id);
//            System.out.println("删除了");
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("deleteBlack error ", e);
        }
    }
    @RequestMapping(value = "biz/blackListAdd",method = RequestMethod.GET)
    public String getBlackListAdd (Model model) {

        return "biz/blackListAdd";
    }
    @RequestMapping(value = "biz/blackListAdd",method = RequestMethod.POST)
    public String possBlackListAdd (BlackList blackList,Model model,HttpSession session) {

        UserInfo userInfo = getUserInfo(session);
        // 业务权限判断
        //判断是否重复号码
        Result<Integer> countByUid = blackListService.getCountByUid(blackList.getItemUId());
        if (countByUid.getModule()==1) {
           super.alertError(model,"此添加号码已在黑名单列表中");
           return "biz/blackListAdd";
       }
        if (!StringUtils.hasText(blackList.getCityName())) {
            super.alertError(model,"黑名单地区不能为空");
            return "biz/blackListAdd";
        }
        Map<String, AreaInfo> provinceMap = localCachedService.getProvinceMap();
        AreaInfo areaInfo = provinceMap.get(blackList.getCityName());
        blackList.setCityName(areaInfo.toString());
        logger.warn("黑名单手机号添加"+blackList.getItemUId()+"添加黑名单的地区："+blackList.getCityName()+"操作人"+userInfo.getUserName());
        blackListService.insertBlack(blackList);
        return "redirect:/biz/queryBlackList.do";
    }


    //省份集合
    @ModelAttribute("provinceList")
    public Map<String, AreaInfo> provinceList() {
        return localCachedService.getProvinceMap();
    }
}
