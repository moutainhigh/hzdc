package com.longan.biz.dataobject;

import java.io.Serializable;
import java.util.List;

/**
 * ClassName: RefundIncrementGetResponse
 * Description:
 * date: 2020/5/12 11:07
 *
 * @author 刘腾
 * @since JDK 1.8
 */
public class RefundGetResponse implements Serializable {
    private  String requestId;
    private List<RefundList> refundList;
    private Integer totalCount;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public List<RefundList> getRefundList() {
        return refundList;
    }

    public void setRefundList(List<RefundList> refundList) {
        this.refundList = refundList;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }
}
