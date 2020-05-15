package com.longan.biz.core.impl;

import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;

import com.ibatis.sqlmap.engine.mapping.sql.Sql;
import com.longan.biz.core.BaseService;
import com.longan.biz.core.BlackListService;
import com.longan.biz.dao.BlackListDAO;
import com.longan.biz.dataobject.BlackList;
import com.longan.biz.domain.Result;

public class BlackListServiceImpl extends BaseService implements BlackListService {
    @Resource
    private BlackListDAO blackListDAO;

    @Override
    public Result<Integer> getCountByUid(String itemUid) {
	Result<Integer> result = new Result<Integer>();
	try {
	    Integer count = blackListDAO.countByItemUid(itemUid);
	    result.setSuccess(true);
	    result.setModule(count);
	} catch (SQLException e) {
	    result.setResultMsg("黑名单查询失败 msg: " + e.getMessage());
	    logger.error("getCountByUid error ", e);
	}
	return result;
    }

	@Override
	public Result<List<BlackList>> queryBlackList(BlackList blackList) {
    	Result<List<BlackList>> result = new Result<List<BlackList>>();
		try {
			List<BlackList> blackLists= blackListDAO.queryBlackList(blackList);
			result.setSuccess(true);
			result.setModule(blackLists);
		} catch (SQLException e) {
			result.setResultMsg("黑名单查询失败 msg: " + e.getMessage());
			logger.error("queryBlackList error ", e);
		}
		return result;
	}

	@Override
	public void deleteBlack(String id) throws SQLException {
			blackListDAO.deleteBlack(id);
	}

	@Override
	public void insertBlack(BlackList blackList) {
		try {
			blackListDAO.insertBlack(blackList);
		} catch (SQLException e) {
			logger.error("insertBlack error",e);
		}
	}
}
