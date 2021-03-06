package com.longan.biz.dataobject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OperationInfoExample {
    /**
     * This field was generated by Abator for iBATIS.
     * This field corresponds to the database table operation_info
     *
     * @abatorgenerated Thu Mar 06 15:12:58 CST 2014
     */
    protected String orderByClause;

    /**
     * This field was generated by Abator for iBATIS.
     * This field corresponds to the database table operation_info
     *
     * @abatorgenerated Thu Mar 06 15:12:58 CST 2014
     */
    protected List oredCriteria;

    /**
     * This method was generated by Abator for iBATIS.
     * This method corresponds to the database table operation_info
     *
     * @abatorgenerated Thu Mar 06 15:12:58 CST 2014
     */
    public OperationInfoExample() {
        oredCriteria = new ArrayList();
    }

    /**
     * This method was generated by Abator for iBATIS.
     * This method corresponds to the database table operation_info
     *
     * @abatorgenerated Thu Mar 06 15:12:58 CST 2014
     */
    protected OperationInfoExample(OperationInfoExample example) {
        this.orderByClause = example.orderByClause;
        this.oredCriteria = example.oredCriteria;
    }

    /**
     * This method was generated by Abator for iBATIS.
     * This method corresponds to the database table operation_info
     *
     * @abatorgenerated Thu Mar 06 15:12:58 CST 2014
     */
    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    /**
     * This method was generated by Abator for iBATIS.
     * This method corresponds to the database table operation_info
     *
     * @abatorgenerated Thu Mar 06 15:12:58 CST 2014
     */
    public String getOrderByClause() {
        return orderByClause;
    }

    /**
     * This method was generated by Abator for iBATIS.
     * This method corresponds to the database table operation_info
     *
     * @abatorgenerated Thu Mar 06 15:12:58 CST 2014
     */
    public List getOredCriteria() {
        return oredCriteria;
    }

    /**
     * This method was generated by Abator for iBATIS.
     * This method corresponds to the database table operation_info
     *
     * @abatorgenerated Thu Mar 06 15:12:58 CST 2014
     */
    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    /**
     * This method was generated by Abator for iBATIS.
     * This method corresponds to the database table operation_info
     *
     * @abatorgenerated Thu Mar 06 15:12:58 CST 2014
     */
    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    /**
     * This method was generated by Abator for iBATIS.
     * This method corresponds to the database table operation_info
     *
     * @abatorgenerated Thu Mar 06 15:12:58 CST 2014
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     * This method was generated by Abator for iBATIS.
     * This method corresponds to the database table operation_info
     *
     * @abatorgenerated Thu Mar 06 15:12:58 CST 2014
     */
    public void clear() {
        oredCriteria.clear();
    }

    /**
     * This class was generated by Abator for iBATIS.
     * This class corresponds to the database table operation_info
     *
     * @abatorgenerated Thu Mar 06 15:12:58 CST 2014
     */
    public static class Criteria {
        protected List criteriaWithoutValue;

        protected List criteriaWithSingleValue;

        protected List criteriaWithListValue;

        protected List criteriaWithBetweenValue;

        protected Criteria() {
            super();
            criteriaWithoutValue = new ArrayList();
            criteriaWithSingleValue = new ArrayList();
            criteriaWithListValue = new ArrayList();
            criteriaWithBetweenValue = new ArrayList();
        }

        public boolean isValid() {
            return criteriaWithoutValue.size() > 0
                || criteriaWithSingleValue.size() > 0
                || criteriaWithListValue.size() > 0
                || criteriaWithBetweenValue.size() > 0;
        }

        public List getCriteriaWithoutValue() {
            return criteriaWithoutValue;
        }

        public List getCriteriaWithSingleValue() {
            return criteriaWithSingleValue;
        }

        public List getCriteriaWithListValue() {
            return criteriaWithListValue;
        }

        public List getCriteriaWithBetweenValue() {
            return criteriaWithBetweenValue;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteriaWithoutValue.add(condition);
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            Map map = new HashMap();
            map.put("condition", condition);
            map.put("value", value);
            criteriaWithSingleValue.add(map);
        }

        protected void addCriterion(String condition, List values, String property) {
            if (values == null || values.size() == 0) {
                throw new RuntimeException("Value list for " + property + " cannot be null or empty");
            }
            Map map = new HashMap();
            map.put("condition", condition);
            map.put("values", values);
            criteriaWithListValue.add(map);
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            List list = new ArrayList();
            list.add(value1);
            list.add(value2);
            Map map = new HashMap();
            map.put("condition", condition);
            map.put("values", list);
            criteriaWithBetweenValue.add(map);
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return this;
        }

        public Criteria andIdEqualTo(Integer value) {
            addCriterion("id =", value, "id");
            return this;
        }

        public Criteria andIdNotEqualTo(Integer value) {
            addCriterion("id <>", value, "id");
            return this;
        }

        public Criteria andIdGreaterThan(Integer value) {
            addCriterion("id >", value, "id");
            return this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("id >=", value, "id");
            return this;
        }

        public Criteria andIdLessThan(Integer value) {
            addCriterion("id <", value, "id");
            return this;
        }

        public Criteria andIdLessThanOrEqualTo(Integer value) {
            addCriterion("id <=", value, "id");
            return this;
        }

        public Criteria andIdIn(List values) {
            addCriterion("id in", values, "id");
            return this;
        }

        public Criteria andIdNotIn(List values) {
            addCriterion("id not in", values, "id");
            return this;
        }

        public Criteria andIdBetween(Integer value1, Integer value2) {
            addCriterion("id between", value1, value2, "id");
            return this;
        }

        public Criteria andIdNotBetween(Integer value1, Integer value2) {
            addCriterion("id not between", value1, value2, "id");
            return this;
        }

        public Criteria andOperationCodeIsNull() {
            addCriterion("operation_code is null");
            return this;
        }

        public Criteria andOperationCodeIsNotNull() {
            addCriterion("operation_code is not null");
            return this;
        }

        public Criteria andOperationCodeEqualTo(String value) {
            addCriterion("operation_code =", value, "operationCode");
            return this;
        }

        public Criteria andOperationCodeNotEqualTo(String value) {
            addCriterion("operation_code <>", value, "operationCode");
            return this;
        }

        public Criteria andOperationCodeGreaterThan(String value) {
            addCriterion("operation_code >", value, "operationCode");
            return this;
        }

        public Criteria andOperationCodeGreaterThanOrEqualTo(String value) {
            addCriterion("operation_code >=", value, "operationCode");
            return this;
        }

        public Criteria andOperationCodeLessThan(String value) {
            addCriterion("operation_code <", value, "operationCode");
            return this;
        }

        public Criteria andOperationCodeLessThanOrEqualTo(String value) {
            addCriterion("operation_code <=", value, "operationCode");
            return this;
        }

        public Criteria andOperationCodeLike(String value) {
            addCriterion("operation_code like", value, "operationCode");
            return this;
        }

        public Criteria andOperationCodeNotLike(String value) {
            addCriterion("operation_code not like", value, "operationCode");
            return this;
        }

        public Criteria andOperationCodeIn(List values) {
            addCriterion("operation_code in", values, "operationCode");
            return this;
        }

        public Criteria andOperationCodeNotIn(List values) {
            addCriterion("operation_code not in", values, "operationCode");
            return this;
        }

        public Criteria andOperationCodeBetween(String value1, String value2) {
            addCriterion("operation_code between", value1, value2, "operationCode");
            return this;
        }

        public Criteria andOperationCodeNotBetween(String value1, String value2) {
            addCriterion("operation_code not between", value1, value2, "operationCode");
            return this;
        }

        public Criteria andOperationNameIsNull() {
            addCriterion("operation_name is null");
            return this;
        }

        public Criteria andOperationNameIsNotNull() {
            addCriterion("operation_name is not null");
            return this;
        }

        public Criteria andOperationNameEqualTo(String value) {
            addCriterion("operation_name =", value, "operationName");
            return this;
        }

        public Criteria andOperationNameNotEqualTo(String value) {
            addCriterion("operation_name <>", value, "operationName");
            return this;
        }

        public Criteria andOperationNameGreaterThan(String value) {
            addCriterion("operation_name >", value, "operationName");
            return this;
        }

        public Criteria andOperationNameGreaterThanOrEqualTo(String value) {
            addCriterion("operation_name >=", value, "operationName");
            return this;
        }

        public Criteria andOperationNameLessThan(String value) {
            addCriterion("operation_name <", value, "operationName");
            return this;
        }

        public Criteria andOperationNameLessThanOrEqualTo(String value) {
            addCriterion("operation_name <=", value, "operationName");
            return this;
        }

        public Criteria andOperationNameLike(String value) {
            addCriterion("operation_name like", value, "operationName");
            return this;
        }

        public Criteria andOperationNameNotLike(String value) {
            addCriterion("operation_name not like", value, "operationName");
            return this;
        }

        public Criteria andOperationNameIn(List values) {
            addCriterion("operation_name in", values, "operationName");
            return this;
        }

        public Criteria andOperationNameNotIn(List values) {
            addCriterion("operation_name not in", values, "operationName");
            return this;
        }

        public Criteria andOperationNameBetween(String value1, String value2) {
            addCriterion("operation_name between", value1, value2, "operationName");
            return this;
        }

        public Criteria andOperationNameNotBetween(String value1, String value2) {
            addCriterion("operation_name not between", value1, value2, "operationName");
            return this;
        }

        public Criteria andOperationUrlIsNull() {
            addCriterion("operation_url is null");
            return this;
        }

        public Criteria andOperationUrlIsNotNull() {
            addCriterion("operation_url is not null");
            return this;
        }

        public Criteria andOperationUrlEqualTo(String value) {
            addCriterion("operation_url =", value, "operationUrl");
            return this;
        }

        public Criteria andOperationUrlNotEqualTo(String value) {
            addCriterion("operation_url <>", value, "operationUrl");
            return this;
        }

        public Criteria andOperationUrlGreaterThan(String value) {
            addCriterion("operation_url >", value, "operationUrl");
            return this;
        }

        public Criteria andOperationUrlGreaterThanOrEqualTo(String value) {
            addCriterion("operation_url >=", value, "operationUrl");
            return this;
        }

        public Criteria andOperationUrlLessThan(String value) {
            addCriterion("operation_url <", value, "operationUrl");
            return this;
        }

        public Criteria andOperationUrlLessThanOrEqualTo(String value) {
            addCriterion("operation_url <=", value, "operationUrl");
            return this;
        }

        public Criteria andOperationUrlLike(String value) {
            addCriterion("operation_url like", value, "operationUrl");
            return this;
        }

        public Criteria andOperationUrlNotLike(String value) {
            addCriterion("operation_url not like", value, "operationUrl");
            return this;
        }

        public Criteria andOperationUrlIn(List values) {
            addCriterion("operation_url in", values, "operationUrl");
            return this;
        }

        public Criteria andOperationUrlNotIn(List values) {
            addCriterion("operation_url not in", values, "operationUrl");
            return this;
        }

        public Criteria andOperationUrlBetween(String value1, String value2) {
            addCriterion("operation_url between", value1, value2, "operationUrl");
            return this;
        }

        public Criteria andOperationUrlNotBetween(String value1, String value2) {
            addCriterion("operation_url not between", value1, value2, "operationUrl");
            return this;
        }

        public Criteria andBizIdIsNull() {
            addCriterion("biz_id is null");
            return this;
        }

        public Criteria andBizIdIsNotNull() {
            addCriterion("biz_id is not null");
            return this;
        }

        public Criteria andBizIdEqualTo(Integer value) {
            addCriterion("biz_id =", value, "bizId");
            return this;
        }

        public Criteria andBizIdNotEqualTo(Integer value) {
            addCriterion("biz_id <>", value, "bizId");
            return this;
        }

        public Criteria andBizIdGreaterThan(Integer value) {
            addCriterion("biz_id >", value, "bizId");
            return this;
        }

        public Criteria andBizIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("biz_id >=", value, "bizId");
            return this;
        }

        public Criteria andBizIdLessThan(Integer value) {
            addCriterion("biz_id <", value, "bizId");
            return this;
        }

        public Criteria andBizIdLessThanOrEqualTo(Integer value) {
            addCriterion("biz_id <=", value, "bizId");
            return this;
        }

        public Criteria andBizIdIn(List values) {
            addCriterion("biz_id in", values, "bizId");
            return this;
        }

        public Criteria andBizIdNotIn(List values) {
            addCriterion("biz_id not in", values, "bizId");
            return this;
        }

        public Criteria andBizIdBetween(Integer value1, Integer value2) {
            addCriterion("biz_id between", value1, value2, "bizId");
            return this;
        }

        public Criteria andBizIdNotBetween(Integer value1, Integer value2) {
            addCriterion("biz_id not between", value1, value2, "bizId");
            return this;
        }

        public Criteria andTypeIsNull() {
            addCriterion("type is null");
            return this;
        }

        public Criteria andTypeIsNotNull() {
            addCriterion("type is not null");
            return this;
        }

        public Criteria andTypeEqualTo(Integer value) {
            addCriterion("type =", value, "type");
            return this;
        }

        public Criteria andTypeNotEqualTo(Integer value) {
            addCriterion("type <>", value, "type");
            return this;
        }

        public Criteria andTypeGreaterThan(Integer value) {
            addCriterion("type >", value, "type");
            return this;
        }

        public Criteria andTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("type >=", value, "type");
            return this;
        }

        public Criteria andTypeLessThan(Integer value) {
            addCriterion("type <", value, "type");
            return this;
        }

        public Criteria andTypeLessThanOrEqualTo(Integer value) {
            addCriterion("type <=", value, "type");
            return this;
        }

        public Criteria andTypeIn(List values) {
            addCriterion("type in", values, "type");
            return this;
        }

        public Criteria andTypeNotIn(List values) {
            addCriterion("type not in", values, "type");
            return this;
        }

        public Criteria andTypeBetween(Integer value1, Integer value2) {
            addCriterion("type between", value1, value2, "type");
            return this;
        }

        public Criteria andTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("type not between", value1, value2, "type");
            return this;
        }

        public Criteria andStatusIsNull() {
            addCriterion("status is null");
            return this;
        }

        public Criteria andStatusIsNotNull() {
            addCriterion("status is not null");
            return this;
        }

        public Criteria andStatusEqualTo(Integer value) {
            addCriterion("status =", value, "status");
            return this;
        }

        public Criteria andStatusNotEqualTo(Integer value) {
            addCriterion("status <>", value, "status");
            return this;
        }

        public Criteria andStatusGreaterThan(Integer value) {
            addCriterion("status >", value, "status");
            return this;
        }

        public Criteria andStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("status >=", value, "status");
            return this;
        }

        public Criteria andStatusLessThan(Integer value) {
            addCriterion("status <", value, "status");
            return this;
        }

        public Criteria andStatusLessThanOrEqualTo(Integer value) {
            addCriterion("status <=", value, "status");
            return this;
        }

        public Criteria andStatusIn(List values) {
            addCriterion("status in", values, "status");
            return this;
        }

        public Criteria andStatusNotIn(List values) {
            addCriterion("status not in", values, "status");
            return this;
        }

        public Criteria andStatusBetween(Integer value1, Integer value2) {
            addCriterion("status between", value1, value2, "status");
            return this;
        }

        public Criteria andStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("status not between", value1, value2, "status");
            return this;
        }
    }
}