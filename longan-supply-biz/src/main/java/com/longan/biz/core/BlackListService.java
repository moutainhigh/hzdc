package com.longan.biz.core;

import com.longan.biz.dataobject.BlackList;
import com.longan.biz.domain.Result;

import java.sql.SQLException;
import java.util.List;

public interface BlackListService {
    public Result<Integer> getCountByUid(String itemUid);
    public Result<List<BlackList>> queryBlackList(BlackList blackList);
    public void deleteBlack(String id) throws SQLException;
    public void insertBlack(BlackList blackList);
}
