package com.longan.client.remote.service;

import java.util.List;

import com.longan.biz.domain.Result;
import com.longan.biz.pojo.CmccItemCode;

public interface CallJifenService {
    public Result<Boolean> resend(Long bizOrderId);

    public Result<Boolean> resend(Long userId, String serialno);

    public Result<Boolean> invalid(Long userId, String serialno);

    public Result<Boolean> record(CmccItemCode code, Long opId);

    public Result<Boolean> records(List<CmccItemCode> codes, Long opId);

    public Result<Boolean> refund(Long bizOrderId, Long opId);

    public Result<Boolean> callback(Long bizOrderId);
}
