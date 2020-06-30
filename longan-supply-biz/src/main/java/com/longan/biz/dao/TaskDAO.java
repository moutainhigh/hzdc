package com.longan.biz.dao;

import java.sql.SQLException;
import java.util.List;

import com.longan.biz.dataobject.Task;
import com.longan.biz.dataobject.TaskQuery;

public interface TaskDAO {
    Long insert(Task task) throws SQLException;

    List<Task> queryListUnDone(Long inTime) throws SQLException;

    Integer updateStatusByid(Long id, Integer status) throws SQLException;

    Integer cancelTask(Long id) throws SQLException;

    List<Task> queryByPage(TaskQuery taskQuery) throws SQLException;

    //定时调价
    List<Task> queryByPageJob(TaskQuery taskQuery) throws SQLException;

    Integer updateTaskById(Task task) throws SQLException;
    //定时调价完修改状态
    public Integer updateTaskByIdStatus(Integer id) throws SQLException;

    Task getTaskById(Long id) throws SQLException;
}
