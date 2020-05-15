package com.longan.biz.dao.impl;

import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.longan.biz.dao.BlackListDAO;
import com.longan.biz.dataobject.BlackList;

public class BlackListDAOImpl implements BlackListDAO {
    @Resource
    private SqlMapClient sqlMapClient;

    @Override
    public int countByItemUid(String itemUid) throws SQLException {
	Integer count = (Integer) sqlMapClient.queryForObject("black_list.abatorgenerated_countByItemUid", itemUid);
	return count.intValue();
    }

    @Override
    public List<BlackList> queryBlackList(BlackList blackList) throws SQLException{

        if (blackList.getStartRow()== -1) {
            blackList.setStartRow(1);
        }
        List<BlackList> blackLists = sqlMapClient.queryForList("black_list.abatorgenerated_queryBlackList",blackList);
        Integer count  = (Integer) sqlMapClient.queryForObject("black_list.abatorgenerated_queryBlackListCount", blackList);
        blackList.setTotalItem(count);
        return blackLists;
    }

    @Override
    public void deleteBlack(String id) throws SQLException {
        Integer result=(Integer)sqlMapClient.delete("black_list.abatorgenerated_deleteBlack",id);
    }

    @Override
    public void insertBlack(BlackList blackList) throws SQLException {
        sqlMapClient.insert("black_list.abatorgenerated_insert",blackList);
    }
}
