package com.hzdc.biz.core;

import com.longan.biz.domain.Result;

public interface DailyReportService {
    public Result<Boolean> unicom(Long userId, String day, String fileName);
}
