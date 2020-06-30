package com.longan.biz.func;

import com.longan.biz.domain.Result;

public interface PddGoodsService {
    public Result<Boolean> itemUp(Integer itemId);
    //第二次pdd上架修改
    public Result<Boolean> pddUp(Integer itemId);
    ///第二次pdd下架修改
    public Result<Boolean> pddDown(Integer itemId);

    public Result<Boolean> itemDown(Integer itemId);

    public Result<Boolean> batchUp(Integer bizId, String provinceCode);

    public Result<Boolean> batchDown(Integer bizId, String provinceCode);
    //定时售后功能
    public String RefundcallPddApi(Integer page,Integer pageSize);
}
