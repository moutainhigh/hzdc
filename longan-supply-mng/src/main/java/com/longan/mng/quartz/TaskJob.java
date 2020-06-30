package com.longan.mng.quartz;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hzdc.biz.core.DailyReportService;
import com.longan.biz.core.TaskService;
import com.longan.biz.dataobject.Task;
import com.longan.biz.domain.Result;
import com.longan.biz.utils.DateTool;
import com.longan.biz.utils.Utils;
import com.longan.client.remote.service.PddRemoteService;

public class TaskJob {
    private final Logger logger = LoggerFactory.getLogger(TaskJob.class);

    private final static boolean goto_task = Utils.getBoolean("goto.task");
    private final static boolean goto_report = Utils.getBoolean("goto.report");
    private final static boolean goto_daily = Utils.getBoolean("goto.daily");

    private final static Long unicom_user = Utils.getLong("unicom.userId");
    private final static String unicom_channel = Utils.getProperty("unicom.channel");

    @Resource
    private TaskService taskService;

    @Resource
    private PddRemoteService pddRemoteService;

    @Resource
    private DailyReportService dailyReportService;

    public void submit() {
	gotoMngTask();
	gotoPddSum();
	gotoUnicomDaily();
    }

    private void gotoMngTask() {
	if (goto_task) {
	    logger.warn("goto mng task is started");
	    Result<List<Task>> result = taskService.queryTaskListUnDone();
	    if (!result.isSuccess()) {
		logger.error("TaskJob queryTaskListUnDone error " + result.getResultMsg());
		return;
	    }

	    List<Task> TaskList = result.getModule();
	    if (TaskList != null) {
		for (Task task : TaskList) {
		    logger.warn("submitTask  id : " + task.getId());
		    taskService.commitTask(task);
		}
	    }
	}
    }

    private void gotoPddSum() {
	if (goto_report) {
	    logger.warn("goto pdd sum is started");
	    Result<Boolean> result = pddRemoteService.runReport();
	    if (result.isSuccess()) {
		logger.warn("goto pdd report is running");
	    } else {
		logger.warn("goto pdd report is failed：" + result.getResultMsg());
	    }
	}
    }

    private void gotoUnicomDaily() {
	if (goto_daily) {
	    logger.warn("goto unicom daily is started");
	    String day = DateTool.parseDate8(DateTool.beforeDay(new Date(), 1));
	    String fileName = day + "." + unicom_channel + ".recharge";
	    Result<Boolean> result = dailyReportService.unicom(unicom_user, day, fileName);
	    if (result.isSuccess()) {
		logger.warn("goto unicom daily is running");
	    } else {
		logger.warn("goto unicom daily is failed：" + result.getResultMsg());
	    }
	}
    }
}
