package com.longan.mng.quartz;

import com.longan.biz.core.ItemService;
import com.longan.biz.core.TaskService;
import com.longan.biz.core.UserService;
import com.longan.biz.dataobject.*;
import com.longan.biz.domain.Result;
import com.longan.biz.utils.BigDecimalUtils;
import com.longan.biz.utils.Constants;
import com.longan.biz.utils.DateTool;
import com.longan.biz.utils.OperLogUtils;
import com.longan.mng.action.common.BaseController;
import com.longan.mng.form.ItemPriceForm;
import com.longan.mng.form.ItemPriceListForm;
import org.apache.commons.lang.StringUtils;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * ClassName: TestJob2
 * Description:
 * date: 2020/6/11 8:49
 *
 * @author 小狐
 * @since JDK 1.8
 */
public class TestJob2 extends BaseController {

    @Resource
    private ItemService itemService;

    @Resource
    private UserService userService;

    @Resource
    private TaskService taskService;

    public void test2() {
            Date date = new Date();
            TaskQuery taskQuery = new TaskQuery();
            //当前时间
        //2020-06-11 14:54:10
            taskQuery.setStartGmtCreate(date);
            taskQuery.setEndGmtCreate(date);

        //查询未执行的
            taskQuery.setStatus(1);
            Result<List<Task>> listResult = taskService.queryTaskByPageJob(taskQuery);
            List<Task> module = listResult.getModule();
            if (module.size()>0 && module!=null) {
                for (Task item :module) {
                    if("itemPriceService".equals(item.getServiceName())) {
                    ItemPriceListForm itemPriceListForm = new ItemPriceListForm();
                    String instance =  item.getInstanceDesc();
                    String[] split = instance.split(";");
                    if (split.length>0 && split!=null) {
                        String[] splitOne = split[0].split(" ");
                        String priceIdList = splitOne[1].substring(1,splitOne[1].length()-1);
                        String costPrice = splitOne[4];
                        String[] splitTwo = split[1].split("->");
                        String[] splitThree = split[2].split("->");
                        String[] splitFour = split[3].split("->");
                        String[] splitFive = split[4].split("->");
                        String[] splitSix = split[5].split("->");
                        //属性设置
                        itemPriceListForm.setItemSupplyIdList(priceIdList.trim());
                        itemPriceListForm.setItemCostPriceDiscount(costPrice.trim());
                        itemPriceListForm.setItemSalesPriceDiscount(splitTwo[1].trim());
                        itemPriceListForm.setItemSalesPrice2Discount(splitThree[1].trim());
                        itemPriceListForm.setItemSalesPrice3Discount(splitFour[1].trim());
                        itemPriceListForm.setItemSalesPrice4Discount(splitFive[1].trim());
                        itemPriceListForm.setItemDummyPriceDiscount(splitSix[1].trim());
                    }
                    //
                    itemPriceListForm.setCommitType("1");
                    onListAddEdit(itemPriceListForm,item.getUserName());
                    Integer result = taskService.updateTaskByIdStatus(item.getId().toString());
                }
            }
            }
    }
    public void onListAddEdit(ItemPriceListForm itemPriceListForm,String  username) {
        UserInfo userInfo = userService.queryByName(username);
        logger.warn(userInfo.getUserName() + "执行商品调价操作 供货商品编号列表 : " + itemPriceListForm.getItemSupplyIdList());
        String itemSupplyIdList = itemPriceListForm.getItemSupplyIdList();
        String[] strs = itemSupplyIdList.split(",");
        if (strs == null || strs.length == 0) {
            logger.warn("供货商品列表不能为空");
        }
        List<Long> list = new ArrayList<Long>();
        for (int i = 0; i < strs.length; i++) {
            list.add(Long.parseLong(strs[i]));
        }
        List<ItemPrice> itemPriceList = new ArrayList<ItemPrice>();
        String itemNameList = "";
        for (Long itemSupplyId : list) {
            Result<ItemSupply> itemSupplyResult = itemService.getItemSupply(itemSupplyId);
            if (!itemSupplyResult.isSuccess() || itemSupplyResult.getModule() == null) {
                break;
            }
            ItemSupply itemSupply = itemSupplyResult.getModule();

            Result<Item> itemResult = itemService.getItem(itemSupply.getItemId());
            if (!itemResult.isSuccess() || itemResult.getModule() == null) {
                break;
            }
            Item item = itemResult.getModule();

            ItemPrice itemPrice = new ItemPrice();
            itemPrice.setId(itemSupply.getId());
            itemPrice.setItemId(item.getId());
            itemPrice.setItemFacePrice(item.getItemFacePrice());
            itemPriceList.add(itemPrice);
            itemNameList = itemNameList + item.getItemName() + "(" + itemSupply.getSupplyTraderName() + ")";
        }

        if (StringUtils.isEmpty(itemPriceListForm.getItemCostPriceDiscount())
                && StringUtils.isEmpty(itemPriceListForm.getItemSalesPriceDiscount())
                && StringUtils.isEmpty(itemPriceListForm.getItemSalesPrice2Discount())
                && StringUtils.isEmpty(itemPriceListForm.getItemSalesPrice3Discount())
                && StringUtils.isEmpty(itemPriceListForm.getItemSalesPrice4Discount())
                && StringUtils.isEmpty(itemPriceListForm.getItemDummyPriceDiscount())) {
        }
        // 设置价格
        for (ItemPrice itemPrice : itemPriceList) {
            setPrice(itemPriceListForm, itemPrice);
        }

        String description = itemPriceListForm.toString();
        OperationLog operationLog = OperLogUtils.operationLogDeal(description, userInfo, null, null,
                userInfo.getLoginId(), Constants.OperationLog.TYPE_UPDATE);
        Result<Long> result = super.submitTaskForm(itemPriceListForm, "itemPriceService", "batchAdjustPrice",
                (Serializable) itemPriceList, "java.util.List", operationLog, userInfo, null);
    }
    private void setPrice(ItemPriceForm itemPriceForm, ItemPrice itemPrice) {
        if (itemPrice == null || itemPriceForm == null || itemPrice.getItemFacePrice() == null) {
            return;
        }
        Integer itemFacePrice = itemPrice.getItemFacePrice();
        if (StringUtils.isNotBlank(itemPriceForm.getItemSalesPriceDiscount())) {
            Integer price = getPrice(itemFacePrice, itemPriceForm.getItemSalesPriceDiscount());
            if (price >= 0) {
                itemPrice.setItemSalesPrice(price);
            }
        }
        if (StringUtils.isNotBlank(itemPriceForm.getItemSalesPrice2Discount())) {
            Integer price = getPrice(itemFacePrice, itemPriceForm.getItemSalesPrice2Discount());
            if (price >= 0) {
                itemPrice.setItemSalesPrice2(price);
            }

        }
        if (StringUtils.isNotBlank(itemPriceForm.getItemSalesPrice3Discount())) {
            Integer price = getPrice(itemFacePrice, itemPriceForm.getItemSalesPrice3Discount());
            if (price >= 0) {
                itemPrice.setItemSalesPrice3(price);
            }
        }
        if (StringUtils.isNotBlank(itemPriceForm.getItemSalesPrice4Discount())) {
            Integer price = getPrice(itemFacePrice, itemPriceForm.getItemSalesPrice4Discount());
            if (price >= 0) {
                itemPrice.setItemSalesPrice4(price);
            }
        }
        if (StringUtils.isNotBlank(itemPriceForm.getItemCostPriceDiscount())) {
            Integer price = getPrice(itemFacePrice, itemPriceForm.getItemCostPriceDiscount());
            if (price >= 0) {
                itemPrice.setItemCostPrice(price);
            }
        }
        if (StringUtils.isNotBlank(itemPriceForm.getItemDummyPriceDiscount())) {
            Integer price = getPrice(itemFacePrice, itemPriceForm.getItemDummyPriceDiscount());
            if (price >= 0) {
                itemPrice.setItemDummyPrice(price);
            }
        }
        System.out.println("执行了"+DateTool.parseDate8(new Date()));
    }

    private void setPrice(ItemPriceListForm itemPriceListForm, ItemPrice itemPrice) {
        if (itemPrice == null || itemPriceListForm == null || itemPrice.getItemFacePrice() == null) {
            return;
        }
        Integer itemFacePrice = itemPrice.getItemFacePrice();
        if (StringUtils.isNotBlank(itemPriceListForm.getItemSalesPriceDiscount())) {
            Integer price = getPrice(itemFacePrice, itemPriceListForm.getItemSalesPriceDiscount());
            if (price >= 0) {
                itemPrice.setItemSalesPrice(price);
            }
        }
        if (StringUtils.isNotBlank(itemPriceListForm.getItemSalesPrice2Discount())) {
            Integer price = getPrice(itemFacePrice, itemPriceListForm.getItemSalesPrice2Discount());
            if (price >= 0) {
                itemPrice.setItemSalesPrice2(price);
            }

        }
        if (StringUtils.isNotBlank(itemPriceListForm.getItemSalesPrice3Discount())) {
            Integer price = getPrice(itemFacePrice, itemPriceListForm.getItemSalesPrice3Discount());
            if (price >= 0) {
                itemPrice.setItemSalesPrice3(price);
            }
        }
        if (StringUtils.isNotBlank(itemPriceListForm.getItemSalesPrice4Discount())) {
            Integer price = getPrice(itemFacePrice, itemPriceListForm.getItemSalesPrice4Discount());
            if (price >= 0) {
                itemPrice.setItemSalesPrice4(price);
            }
        }
        if (StringUtils.isNotBlank(itemPriceListForm.getItemCostPriceDiscount())) {
            Integer price = getPrice(itemFacePrice, itemPriceListForm.getItemCostPriceDiscount());
            if (price >= 0) {
                itemPrice.setItemCostPrice(price);
            }
        }
        if (StringUtils.isNotBlank(itemPriceListForm.getItemDummyPriceDiscount())) {
            Integer price = getPrice(itemFacePrice, itemPriceListForm.getItemDummyPriceDiscount());
            if (price >= 0) {
                itemPrice.setItemDummyPrice(price);
            }
        }
    }

    private static Integer getPrice(Integer itemFacePrice, String discount) {
        if (itemFacePrice == null || StringUtils.isEmpty(discount)) {
            return null;
        }
        return BigDecimalUtils.multDiscount(itemFacePrice.toString(), discount);
    }
}
