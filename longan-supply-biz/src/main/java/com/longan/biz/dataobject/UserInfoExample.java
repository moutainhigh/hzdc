package com.longan.biz.dataobject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserInfoExample {
    /**
     * This field was generated by Abator for iBATIS. This field corresponds to the database table user_info
     * 
     * @abatorgenerated Sat Mar 08 15:15:14 CST 2014
     */
    protected String orderByClause;

    /**
     * This field was generated by Abator for iBATIS. This field corresponds to the database table user_info
     * 
     * @abatorgenerated Sat Mar 08 15:15:14 CST 2014
     */
    protected List oredCriteria;

    /**
     * This method was generated by Abator for iBATIS. This method corresponds to the database table user_info
     * 
     * @abatorgenerated Sat Mar 08 15:15:14 CST 2014
     */
    public UserInfoExample() {
	oredCriteria = new ArrayList();
    }

    /**
     * This method was generated by Abator for iBATIS. This method corresponds to the database table user_info
     * 
     * @abatorgenerated Sat Mar 08 15:15:14 CST 2014
     */
    protected UserInfoExample(UserInfoExample example) {
	this.orderByClause = example.orderByClause;
	this.oredCriteria = example.oredCriteria;
    }

    /**
     * This method was generated by Abator for iBATIS. This method corresponds to the database table user_info
     * 
     * @abatorgenerated Sat Mar 08 15:15:14 CST 2014
     */
    public void setOrderByClause(String orderByClause) {
	this.orderByClause = orderByClause;
    }

    /**
     * This method was generated by Abator for iBATIS. This method corresponds to the database table user_info
     * 
     * @abatorgenerated Sat Mar 08 15:15:14 CST 2014
     */
    public String getOrderByClause() {
	return orderByClause;
    }

    /**
     * This method was generated by Abator for iBATIS. This method corresponds to the database table user_info
     * 
     * @abatorgenerated Sat Mar 08 15:15:14 CST 2014
     */
    public List getOredCriteria() {
	return oredCriteria;
    }

    /**
     * This method was generated by Abator for iBATIS. This method corresponds to the database table user_info
     * 
     * @abatorgenerated Sat Mar 08 15:15:14 CST 2014
     */
    public void or(Criteria criteria) {
	oredCriteria.add(criteria);
    }

    /**
     * This method was generated by Abator for iBATIS. This method corresponds to the database table user_info
     * 
     * @abatorgenerated Sat Mar 08 15:15:14 CST 2014
     */
    public Criteria createCriteria() {
	Criteria criteria = createCriteriaInternal();
	if (oredCriteria.size() == 0) {
	    oredCriteria.add(criteria);
	}
	return criteria;
    }

    /**
     * This method was generated by Abator for iBATIS. This method corresponds to the database table user_info
     * 
     * @abatorgenerated Sat Mar 08 15:15:14 CST 2014
     */
    protected Criteria createCriteriaInternal() {
	Criteria criteria = new Criteria();
	return criteria;
    }

    /**
     * This method was generated by Abator for iBATIS. This method corresponds to the database table user_info
     * 
     * @abatorgenerated Sat Mar 08 15:15:14 CST 2014
     */
    public void clear() {
	oredCriteria.clear();
    }

    /**
     * This class was generated by Abator for iBATIS. This class corresponds to the database table user_info
     * 
     * @abatorgenerated Sat Mar 08 15:15:14 CST 2014
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
	    return criteriaWithoutValue.size() > 0 || criteriaWithSingleValue.size() > 0 || criteriaWithListValue.size() > 0
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

	public Criteria andIdEqualTo(Long value) {
	    addCriterion("id =", value, "id");
	    return this;
	}

	public Criteria andIdNotEqualTo(Long value) {
	    addCriterion("id <>", value, "id");
	    return this;
	}

	public Criteria andIdGreaterThan(Long value) {
	    addCriterion("id >", value, "id");
	    return this;
	}

	public Criteria andIdGreaterThanOrEqualTo(Long value) {
	    addCriterion("id >=", value, "id");
	    return this;
	}

	public Criteria andIdLessThan(Long value) {
	    addCriterion("id <", value, "id");
	    return this;
	}

	public Criteria andIdLessThanOrEqualTo(Long value) {
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

	public Criteria andIdBetween(Long value1, Long value2) {
	    addCriterion("id between", value1, value2, "id");
	    return this;
	}

	public Criteria andIdNotBetween(Long value1, Long value2) {
	    addCriterion("id not between", value1, value2, "id");
	    return this;
	}

	public Criteria andLoginIdIsNull() {
	    addCriterion("login_id is null");
	    return this;
	}

	public Criteria andLoginIdIsNotNull() {
	    addCriterion("login_id is not null");
	    return this;
	}

	public Criteria andLoginIdEqualTo(String value) {
	    addCriterion("login_id =", value, "loginId");
	    return this;
	}

	public Criteria andLoginIdNotEqualTo(String value) {
	    addCriterion("login_id <>", value, "loginId");
	    return this;
	}

	public Criteria andLoginIdGreaterThan(String value) {
	    addCriterion("login_id >", value, "loginId");
	    return this;
	}

	public Criteria andLoginIdGreaterThanOrEqualTo(String value) {
	    addCriterion("login_id >=", value, "loginId");
	    return this;
	}

	public Criteria andLoginIdLessThan(String value) {
	    addCriterion("login_id <", value, "loginId");
	    return this;
	}

	public Criteria andLoginIdLessThanOrEqualTo(String value) {
	    addCriterion("login_id <=", value, "loginId");
	    return this;
	}

	public Criteria andLoginIdLike(String value) {
	    addCriterion("login_id like", value, "loginId");
	    return this;
	}

	public Criteria andLoginIdNotLike(String value) {
	    addCriterion("login_id not like", value, "loginId");
	    return this;
	}

	public Criteria andLoginIdIn(List values) {
	    addCriterion("login_id in", values, "loginId");
	    return this;
	}

	public Criteria andLoginIdNotIn(List values) {
	    addCriterion("login_id not in", values, "loginId");
	    return this;
	}

	public Criteria andLoginIdBetween(String value1, String value2) {
	    addCriterion("login_id between", value1, value2, "loginId");
	    return this;
	}

	public Criteria andLoginIdNotBetween(String value1, String value2) {
	    addCriterion("login_id not between", value1, value2, "loginId");
	    return this;
	}

	public Criteria andAcctIdIsNull() {
	    addCriterion("acct_id is null");
	    return this;
	}

	public Criteria andAcctIdIsNotNull() {
	    addCriterion("acct_id is not null");
	    return this;
	}

	public Criteria andAcctIdEqualTo(Long value) {
	    addCriterion("acct_id =", value, "acctId");
	    return this;
	}

	public Criteria andAcctIdNotEqualTo(Long value) {
	    addCriterion("acct_id <>", value, "acctId");
	    return this;
	}

	public Criteria andAcctIdGreaterThan(Long value) {
	    addCriterion("acct_id >", value, "acctId");
	    return this;
	}

	public Criteria andAcctIdGreaterThanOrEqualTo(Long value) {
	    addCriterion("acct_id >=", value, "acctId");
	    return this;
	}

	public Criteria andAcctIdLessThan(Long value) {
	    addCriterion("acct_id <", value, "acctId");
	    return this;
	}

	public Criteria andAcctIdLessThanOrEqualTo(Long value) {
	    addCriterion("acct_id <=", value, "acctId");
	    return this;
	}

	public Criteria andAcctIdIn(List values) {
	    addCriterion("acct_id in", values, "acctId");
	    return this;
	}

	public Criteria andAcctIdNotIn(List values) {
	    addCriterion("acct_id not in", values, "acctId");
	    return this;
	}

	public Criteria andAcctIdBetween(Long value1, Long value2) {
	    addCriterion("acct_id between", value1, value2, "acctId");
	    return this;
	}

	public Criteria andAcctIdNotBetween(Long value1, Long value2) {
	    addCriterion("acct_id not between", value1, value2, "acctId");
	    return this;
	}

	public Criteria andUserNameIsNull() {
	    addCriterion("user_name is null");
	    return this;
	}

	public Criteria andUserNameIsNotNull() {
	    addCriterion("user_name is not null");
	    return this;
	}

	public Criteria andUserNameEqualTo(String value) {
	    addCriterion("user_name =", value, "userName");
	    return this;
	}

	public Criteria andUserNameNotEqualTo(String value) {
	    addCriterion("user_name <>", value, "userName");
	    return this;
	}

	public Criteria andUserNameGreaterThan(String value) {
	    addCriterion("user_name >", value, "userName");
	    return this;
	}

	public Criteria andUserNameGreaterThanOrEqualTo(String value) {
	    addCriterion("user_name >=", value, "userName");
	    return this;
	}

	public Criteria andUserNameLessThan(String value) {
	    addCriterion("user_name <", value, "userName");
	    return this;
	}

	public Criteria andUserNameLessThanOrEqualTo(String value) {
	    addCriterion("user_name <=", value, "userName");
	    return this;
	}

	public Criteria andUserNameLike(String value) {
	    addCriterion("user_name like", value, "userName");
	    return this;
	}

	public Criteria andUserNameNotLike(String value) {
	    addCriterion("user_name not like", value, "userName");
	    return this;
	}

	public Criteria andUserNameIn(List values) {
	    addCriterion("user_name in", values, "userName");
	    return this;
	}

	public Criteria andUserNameNotIn(List values) {
	    addCriterion("user_name not in", values, "userName");
	    return this;
	}

	public Criteria andUserNameBetween(String value1, String value2) {
	    addCriterion("user_name between", value1, value2, "userName");
	    return this;
	}

	public Criteria andUserNameNotBetween(String value1, String value2) {
	    addCriterion("user_name not between", value1, value2, "userName");
	    return this;
	}

	public Criteria andCompayInfoIsNull() {
	    addCriterion("compay_info is null");
	    return this;
	}

	public Criteria andCompayInfoIsNotNull() {
	    addCriterion("compay_info is not null");
	    return this;
	}

	public Criteria andCompayInfoEqualTo(String value) {
	    addCriterion("compay_info =", value, "compayInfo");
	    return this;
	}

	public Criteria andCompayInfoNotEqualTo(String value) {
	    addCriterion("compay_info <>", value, "compayInfo");
	    return this;
	}

	public Criteria andCompayInfoGreaterThan(String value) {
	    addCriterion("compay_info >", value, "compayInfo");
	    return this;
	}

	public Criteria andCompayInfoGreaterThanOrEqualTo(String value) {
	    addCriterion("compay_info >=", value, "compayInfo");
	    return this;
	}

	public Criteria andCompayInfoLessThan(String value) {
	    addCriterion("compay_info <", value, "compayInfo");
	    return this;
	}

	public Criteria andCompayInfoLessThanOrEqualTo(String value) {
	    addCriterion("compay_info <=", value, "compayInfo");
	    return this;
	}

	public Criteria andCompayInfoLike(String value) {
	    addCriterion("compay_info like", value, "compayInfo");
	    return this;
	}

	public Criteria andCompayInfoNotLike(String value) {
	    addCriterion("compay_info not like", value, "compayInfo");
	    return this;
	}

	public Criteria andCompayInfoIn(List values) {
	    addCriterion("compay_info in", values, "compayInfo");
	    return this;
	}

	public Criteria andCompayInfoNotIn(List values) {
	    addCriterion("compay_info not in", values, "compayInfo");
	    return this;
	}

	public Criteria andCompayInfoBetween(String value1, String value2) {
	    addCriterion("compay_info between", value1, value2, "compayInfo");
	    return this;
	}

	public Criteria andCompayInfoNotBetween(String value1, String value2) {
	    addCriterion("compay_info not between", value1, value2, "compayInfo");
	    return this;
	}

	public Criteria andEmailIsNull() {
	    addCriterion("email is null");
	    return this;
	}

	public Criteria andEmailIsNotNull() {
	    addCriterion("email is not null");
	    return this;
	}

	public Criteria andEmailEqualTo(String value) {
	    addCriterion("email =", value, "email");
	    return this;
	}

	public Criteria andEmailNotEqualTo(String value) {
	    addCriterion("email <>", value, "email");
	    return this;
	}

	public Criteria andEmailGreaterThan(String value) {
	    addCriterion("email >", value, "email");
	    return this;
	}

	public Criteria andEmailGreaterThanOrEqualTo(String value) {
	    addCriterion("email >=", value, "email");
	    return this;
	}

	public Criteria andEmailLessThan(String value) {
	    addCriterion("email <", value, "email");
	    return this;
	}

	public Criteria andEmailLessThanOrEqualTo(String value) {
	    addCriterion("email <=", value, "email");
	    return this;
	}

	public Criteria andEmailLike(String value) {
	    addCriterion("email like", value, "email");
	    return this;
	}

	public Criteria andEmailNotLike(String value) {
	    addCriterion("email not like", value, "email");
	    return this;
	}

	public Criteria andEmailIn(List values) {
	    addCriterion("email in", values, "email");
	    return this;
	}

	public Criteria andEmailNotIn(List values) {
	    addCriterion("email not in", values, "email");
	    return this;
	}

	public Criteria andEmailBetween(String value1, String value2) {
	    addCriterion("email between", value1, value2, "email");
	    return this;
	}

	public Criteria andEmailNotBetween(String value1, String value2) {
	    addCriterion("email not between", value1, value2, "email");
	    return this;
	}

	public Criteria andUserInfoIsNull() {
	    addCriterion("user_info is null");
	    return this;
	}

	public Criteria andUserInfoIsNotNull() {
	    addCriterion("user_info is not null");
	    return this;
	}

	public Criteria andUserInfoEqualTo(String value) {
	    addCriterion("user_info =", value, "userInfo");
	    return this;
	}

	public Criteria andUserInfoNotEqualTo(String value) {
	    addCriterion("user_info <>", value, "userInfo");
	    return this;
	}

	public Criteria andUserInfoGreaterThan(String value) {
	    addCriterion("user_info >", value, "userInfo");
	    return this;
	}

	public Criteria andUserInfoGreaterThanOrEqualTo(String value) {
	    addCriterion("user_info >=", value, "userInfo");
	    return this;
	}

	public Criteria andUserInfoLessThan(String value) {
	    addCriterion("user_info <", value, "userInfo");
	    return this;
	}

	public Criteria andUserInfoLessThanOrEqualTo(String value) {
	    addCriterion("user_info <=", value, "userInfo");
	    return this;
	}

	public Criteria andUserInfoLike(String value) {
	    addCriterion("user_info like", value, "userInfo");
	    return this;
	}

	public Criteria andUserInfoNotLike(String value) {
	    addCriterion("user_info not like", value, "userInfo");
	    return this;
	}

	public Criteria andUserInfoIn(List values) {
	    addCriterion("user_info in", values, "userInfo");
	    return this;
	}

	public Criteria andUserInfoNotIn(List values) {
	    addCriterion("user_info not in", values, "userInfo");
	    return this;
	}

	public Criteria andUserInfoBetween(String value1, String value2) {
	    addCriterion("user_info between", value1, value2, "userInfo");
	    return this;
	}

	public Criteria andUserInfoNotBetween(String value1, String value2) {
	    addCriterion("user_info not between", value1, value2, "userInfo");
	    return this;
	}

	public Criteria andMobileIsNull() {
	    addCriterion("mobile is null");
	    return this;
	}

	public Criteria andMobileIsNotNull() {
	    addCriterion("mobile is not null");
	    return this;
	}

	public Criteria andMobileEqualTo(String value) {
	    addCriterion("mobile =", value, "mobile");
	    return this;
	}

	public Criteria andMobileNotEqualTo(String value) {
	    addCriterion("mobile <>", value, "mobile");
	    return this;
	}

	public Criteria andMobileGreaterThan(String value) {
	    addCriterion("mobile >", value, "mobile");
	    return this;
	}

	public Criteria andMobileGreaterThanOrEqualTo(String value) {
	    addCriterion("mobile >=", value, "mobile");
	    return this;
	}

	public Criteria andMobileLessThan(String value) {
	    addCriterion("mobile <", value, "mobile");
	    return this;
	}

	public Criteria andMobileLessThanOrEqualTo(String value) {
	    addCriterion("mobile <=", value, "mobile");
	    return this;
	}

	public Criteria andMobileLike(String value) {
	    addCriterion("mobile like", value, "mobile");
	    return this;
	}

	public Criteria andMobileNotLike(String value) {
	    addCriterion("mobile not like", value, "mobile");
	    return this;
	}

	public Criteria andMobileIn(List values) {
	    addCriterion("mobile in", values, "mobile");
	    return this;
	}

	public Criteria andMobileNotIn(List values) {
	    addCriterion("mobile not in", values, "mobile");
	    return this;
	}

	public Criteria andMobileBetween(String value1, String value2) {
	    addCriterion("mobile between", value1, value2, "mobile");
	    return this;
	}

	public Criteria andMobileNotBetween(String value1, String value2) {
	    addCriterion("mobile not between", value1, value2, "mobile");
	    return this;
	}

	public Criteria andAddrIsNull() {
	    addCriterion("addr is null");
	    return this;
	}

	public Criteria andAddrIsNotNull() {
	    addCriterion("addr is not null");
	    return this;
	}

	public Criteria andAddrEqualTo(String value) {
	    addCriterion("addr =", value, "addr");
	    return this;
	}

	public Criteria andAddrNotEqualTo(String value) {
	    addCriterion("addr <>", value, "addr");
	    return this;
	}

	public Criteria andAddrGreaterThan(String value) {
	    addCriterion("addr >", value, "addr");
	    return this;
	}

	public Criteria andAddrGreaterThanOrEqualTo(String value) {
	    addCriterion("addr >=", value, "addr");
	    return this;
	}

	public Criteria andAddrLessThan(String value) {
	    addCriterion("addr <", value, "addr");
	    return this;
	}

	public Criteria andAddrLessThanOrEqualTo(String value) {
	    addCriterion("addr <=", value, "addr");
	    return this;
	}

	public Criteria andAddrLike(String value) {
	    addCriterion("addr like", value, "addr");
	    return this;
	}

	public Criteria andAddrNotLike(String value) {
	    addCriterion("addr not like", value, "addr");
	    return this;
	}

	public Criteria andAddrIn(List values) {
	    addCriterion("addr in", values, "addr");
	    return this;
	}

	public Criteria andAddrNotIn(List values) {
	    addCriterion("addr not in", values, "addr");
	    return this;
	}

	public Criteria andAddrBetween(String value1, String value2) {
	    addCriterion("addr between", value1, value2, "addr");
	    return this;
	}

	public Criteria andAddrNotBetween(String value1, String value2) {
	    addCriterion("addr not between", value1, value2, "addr");
	    return this;
	}

	public Criteria andAreaIsNull() {
	    addCriterion("area is null");
	    return this;
	}

	public Criteria andAreaIsNotNull() {
	    addCriterion("area is not null");
	    return this;
	}

	public Criteria andAreaEqualTo(String value) {
	    addCriterion("area =", value, "area");
	    return this;
	}

	public Criteria andAreaNotEqualTo(String value) {
	    addCriterion("area <>", value, "area");
	    return this;
	}

	public Criteria andAreaGreaterThan(String value) {
	    addCriterion("area >", value, "area");
	    return this;
	}

	public Criteria andAreaGreaterThanOrEqualTo(String value) {
	    addCriterion("area >=", value, "area");
	    return this;
	}

	public Criteria andAreaLessThan(String value) {
	    addCriterion("area <", value, "area");
	    return this;
	}

	public Criteria andAreaLessThanOrEqualTo(String value) {
	    addCriterion("area <=", value, "area");
	    return this;
	}

	public Criteria andAreaLike(String value) {
	    addCriterion("area like", value, "area");
	    return this;
	}

	public Criteria andAreaNotLike(String value) {
	    addCriterion("area not like", value, "area");
	    return this;
	}

	public Criteria andAreaIn(List values) {
	    addCriterion("area in", values, "area");
	    return this;
	}

	public Criteria andAreaNotIn(List values) {
	    addCriterion("area not in", values, "area");
	    return this;
	}

	public Criteria andAreaBetween(String value1, String value2) {
	    addCriterion("area between", value1, value2, "area");
	    return this;
	}

	public Criteria andAreaNotBetween(String value1, String value2) {
	    addCriterion("area not between", value1, value2, "area");
	    return this;
	}

	public Criteria andPwdIsNull() {
	    addCriterion("pwd is null");
	    return this;
	}

	public Criteria andPwdIsNotNull() {
	    addCriterion("pwd is not null");
	    return this;
	}

	public Criteria andPwdEqualTo(String value) {
	    addCriterion("pwd =", value, "pwd");
	    return this;
	}

	public Criteria andPwdNotEqualTo(String value) {
	    addCriterion("pwd <>", value, "pwd");
	    return this;
	}

	public Criteria andPwdGreaterThan(String value) {
	    addCriterion("pwd >", value, "pwd");
	    return this;
	}

	public Criteria andPwdGreaterThanOrEqualTo(String value) {
	    addCriterion("pwd >=", value, "pwd");
	    return this;
	}

	public Criteria andPwdLessThan(String value) {
	    addCriterion("pwd <", value, "pwd");
	    return this;
	}

	public Criteria andPwdLessThanOrEqualTo(String value) {
	    addCriterion("pwd <=", value, "pwd");
	    return this;
	}

	public Criteria andPwdLike(String value) {
	    addCriterion("pwd like", value, "pwd");
	    return this;
	}

	public Criteria andPwdNotLike(String value) {
	    addCriterion("pwd not like", value, "pwd");
	    return this;
	}

	public Criteria andPwdIn(List values) {
	    addCriterion("pwd in", values, "pwd");
	    return this;
	}

	public Criteria andPwdNotIn(List values) {
	    addCriterion("pwd not in", values, "pwd");
	    return this;
	}

	public Criteria andPwdBetween(String value1, String value2) {
	    addCriterion("pwd between", value1, value2, "pwd");
	    return this;
	}

	public Criteria andPwdNotBetween(String value1, String value2) {
	    addCriterion("pwd not between", value1, value2, "pwd");
	    return this;
	}

	public Criteria andRefererIsNull() {
	    addCriterion("referer is null");
	    return this;
	}

	public Criteria andRefererIsNotNull() {
	    addCriterion("referer is not null");
	    return this;
	}

	public Criteria andRefererEqualTo(String value) {
	    addCriterion("referer =", value, "referer");
	    return this;
	}

	public Criteria andRefererNotEqualTo(String value) {
	    addCriterion("referer <>", value, "referer");
	    return this;
	}

	public Criteria andRefererGreaterThan(String value) {
	    addCriterion("referer >", value, "referer");
	    return this;
	}

	public Criteria andRefererGreaterThanOrEqualTo(String value) {
	    addCriterion("referer >=", value, "referer");
	    return this;
	}

	public Criteria andRefererLessThan(String value) {
	    addCriterion("referer <", value, "referer");
	    return this;
	}

	public Criteria andRefererLessThanOrEqualTo(String value) {
	    addCriterion("referer <=", value, "referer");
	    return this;
	}

	public Criteria andRefererLike(String value) {
	    addCriterion("referer like", value, "referer");
	    return this;
	}

	public Criteria andRefererNotLike(String value) {
	    addCriterion("referer not like", value, "referer");
	    return this;
	}

	public Criteria andRefererIn(List values) {
	    addCriterion("referer in", values, "referer");
	    return this;
	}

	public Criteria andRefererNotIn(List values) {
	    addCriterion("referer not in", values, "referer");
	    return this;
	}

	public Criteria andRefererBetween(String value1, String value2) {
	    addCriterion("referer between", value1, value2, "referer");
	    return this;
	}

	public Criteria andRefererNotBetween(String value1, String value2) {
	    addCriterion("referer not between", value1, value2, "referer");
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

	public Criteria andLastLoginTimeIsNull() {
	    addCriterion("last_login_time is null");
	    return this;
	}

	public Criteria andLastLoginTimeIsNotNull() {
	    addCriterion("last_login_time is not null");
	    return this;
	}

	public Criteria andLastLoginTimeEqualTo(Date value) {
	    addCriterion("last_login_time =", value, "lastLoginTime");
	    return this;
	}

	public Criteria andLastLoginTimeNotEqualTo(Date value) {
	    addCriterion("last_login_time <>", value, "lastLoginTime");
	    return this;
	}

	public Criteria andLastLoginTimeGreaterThan(Date value) {
	    addCriterion("last_login_time >", value, "lastLoginTime");
	    return this;
	}

	public Criteria andLastLoginTimeGreaterThanOrEqualTo(Date value) {
	    addCriterion("last_login_time >=", value, "lastLoginTime");
	    return this;
	}

	public Criteria andLastLoginTimeLessThan(Date value) {
	    addCriterion("last_login_time <", value, "lastLoginTime");
	    return this;
	}

	public Criteria andLastLoginTimeLessThanOrEqualTo(Date value) {
	    addCriterion("last_login_time <=", value, "lastLoginTime");
	    return this;
	}

	public Criteria andLastLoginTimeIn(List values) {
	    addCriterion("last_login_time in", values, "lastLoginTime");
	    return this;
	}

	public Criteria andLastLoginTimeNotIn(List values) {
	    addCriterion("last_login_time not in", values, "lastLoginTime");
	    return this;
	}

	public Criteria andLastLoginTimeBetween(Date value1, Date value2) {
	    addCriterion("last_login_time between", value1, value2, "lastLoginTime");
	    return this;
	}

	public Criteria andLastLoginTimeNotBetween(Date value1, Date value2) {
	    addCriterion("last_login_time not between", value1, value2, "lastLoginTime");
	    return this;
	}

	public Criteria andLastLoginIpIsNull() {
	    addCriterion("last_login_ip is null");
	    return this;
	}

	public Criteria andLastLoginIpIsNotNull() {
	    addCriterion("last_login_ip is not null");
	    return this;
	}

	public Criteria andLastLoginIpEqualTo(String value) {
	    addCriterion("last_login_ip =", value, "lastLoginIp");
	    return this;
	}

	public Criteria andLastLoginIpNotEqualTo(String value) {
	    addCriterion("last_login_ip <>", value, "lastLoginIp");
	    return this;
	}

	public Criteria andLastLoginIpGreaterThan(String value) {
	    addCriterion("last_login_ip >", value, "lastLoginIp");
	    return this;
	}

	public Criteria andLastLoginIpGreaterThanOrEqualTo(String value) {
	    addCriterion("last_login_ip >=", value, "lastLoginIp");
	    return this;
	}

	public Criteria andLastLoginIpLessThan(String value) {
	    addCriterion("last_login_ip <", value, "lastLoginIp");
	    return this;
	}

	public Criteria andLastLoginIpLessThanOrEqualTo(String value) {
	    addCriterion("last_login_ip <=", value, "lastLoginIp");
	    return this;
	}

	public Criteria andLastLoginIpLike(String value) {
	    addCriterion("last_login_ip like", value, "lastLoginIp");
	    return this;
	}

	public Criteria andLastLoginIpNotLike(String value) {
	    addCriterion("last_login_ip not like", value, "lastLoginIp");
	    return this;
	}

	public Criteria andLastLoginIpIn(List values) {
	    addCriterion("last_login_ip in", values, "lastLoginIp");
	    return this;
	}

	public Criteria andLastLoginIpNotIn(List values) {
	    addCriterion("last_login_ip not in", values, "lastLoginIp");
	    return this;
	}

	public Criteria andLastLoginIpBetween(String value1, String value2) {
	    addCriterion("last_login_ip between", value1, value2, "lastLoginIp");
	    return this;
	}

	public Criteria andLastLoginIpNotBetween(String value1, String value2) {
	    addCriterion("last_login_ip not between", value1, value2, "lastLoginIp");
	    return this;
	}

	public Criteria andGmtCreateIsNull() {
	    addCriterion("gmt_create is null");
	    return this;
	}

	public Criteria andGmtCreateIsNotNull() {
	    addCriterion("gmt_create is not null");
	    return this;
	}

	public Criteria andGmtCreateEqualTo(Date value) {
	    addCriterion("gmt_create =", value, "gmtCreate");
	    return this;
	}

	public Criteria andGmtCreateNotEqualTo(Date value) {
	    addCriterion("gmt_create <>", value, "gmtCreate");
	    return this;
	}

	public Criteria andGmtCreateGreaterThan(Date value) {
	    addCriterion("gmt_create >", value, "gmtCreate");
	    return this;
	}

	public Criteria andGmtCreateGreaterThanOrEqualTo(Date value) {
	    addCriterion("gmt_create >=", value, "gmtCreate");
	    return this;
	}

	public Criteria andGmtCreateLessThan(Date value) {
	    addCriterion("gmt_create <", value, "gmtCreate");
	    return this;
	}

	public Criteria andGmtCreateLessThanOrEqualTo(Date value) {
	    addCriterion("gmt_create <=", value, "gmtCreate");
	    return this;
	}

	public Criteria andGmtCreateIn(List values) {
	    addCriterion("gmt_create in", values, "gmtCreate");
	    return this;
	}

	public Criteria andGmtCreateNotIn(List values) {
	    addCriterion("gmt_create not in", values, "gmtCreate");
	    return this;
	}

	public Criteria andGmtCreateBetween(Date value1, Date value2) {
	    addCriterion("gmt_create between", value1, value2, "gmtCreate");
	    return this;
	}

	public Criteria andGmtCreateNotBetween(Date value1, Date value2) {
	    addCriterion("gmt_create not between", value1, value2, "gmtCreate");
	    return this;
	}

	public Criteria andGmtModifyIsNull() {
	    addCriterion("gmt_modify is null");
	    return this;
	}

	public Criteria andGmtModifyIsNotNull() {
	    addCriterion("gmt_modify is not null");
	    return this;
	}

	public Criteria andGmtModifyEqualTo(Date value) {
	    addCriterion("gmt_modify =", value, "gmtModify");
	    return this;
	}

	public Criteria andGmtModifyNotEqualTo(Date value) {
	    addCriterion("gmt_modify <>", value, "gmtModify");
	    return this;
	}

	public Criteria andGmtModifyGreaterThan(Date value) {
	    addCriterion("gmt_modify >", value, "gmtModify");
	    return this;
	}

	public Criteria andGmtModifyGreaterThanOrEqualTo(Date value) {
	    addCriterion("gmt_modify >=", value, "gmtModify");
	    return this;
	}

	public Criteria andGmtModifyLessThan(Date value) {
	    addCriterion("gmt_modify <", value, "gmtModify");
	    return this;
	}

	public Criteria andGmtModifyLessThanOrEqualTo(Date value) {
	    addCriterion("gmt_modify <=", value, "gmtModify");
	    return this;
	}

	public Criteria andGmtModifyIn(List values) {
	    addCriterion("gmt_modify in", values, "gmtModify");
	    return this;
	}

	public Criteria andGmtModifyNotIn(List values) {
	    addCriterion("gmt_modify not in", values, "gmtModify");
	    return this;
	}

	public Criteria andGmtModifyBetween(Date value1, Date value2) {
	    addCriterion("gmt_modify between", value1, value2, "gmtModify");
	    return this;
	}

	public Criteria andGmtModifyNotBetween(Date value1, Date value2) {
	    addCriterion("gmt_modify not between", value1, value2, "gmtModify");
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
    }
}