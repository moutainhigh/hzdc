package com.longan.biz.domain;

import com.longan.biz.dataobject.OrderReport;

import java.util.Comparator;

/**
 * ClassName: ComparatorNowOrder
 * Description:
 * date: 2020/5/21 8:50
 *
 * @author 刘腾
 * @since JDK 1.8
 */
public class ComparatorNowOrder implements Comparator<OrderReport> {
    @Override
    public int compare(OrderReport o1, OrderReport o2) {
        return o2.getNowOrder()-o1.getNowOrder();
    }
}
