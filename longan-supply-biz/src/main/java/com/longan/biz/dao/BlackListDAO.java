package com.longan.biz.dao;

import com.longan.biz.dataobject.BlackList;

import java.sql.SQLException;
import java.util.List;

public interface BlackListDAO {
    int countByItemUid(String itemUid) throws SQLException;

    List<BlackList> queryBlackList(BlackList blackList) throws SQLException;
    void deleteBlack(String id) throws SQLException;
    public void insertBlack(BlackList blackList) throws SQLException;
}
