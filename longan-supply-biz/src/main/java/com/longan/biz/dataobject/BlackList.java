package com.longan.biz.dataobject;

import com.longan.biz.domain.QueryBase;

/**
 * ClassName: BlackList
 * Description:
 * date: 2020/5/2 0:27
 *
 * @author 刘腾
 * @since JDK 1.8
 */
public class BlackList  extends QueryBase {
    private static final long serialVersionUID = 1L;
    private Long id ;
    private String itemUId;
    private String cityName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getItemUId() {
        return itemUId;
    }

    public void setItemUId(String itemUId) {
        this.itemUId = itemUId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
