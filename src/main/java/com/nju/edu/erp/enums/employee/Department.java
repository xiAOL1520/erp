package com.nju.edu.erp.enums.employee;

import com.nju.edu.erp.enums.BaseEnum;

public enum Department implements BaseEnum<Department, String> {
    INVENTORY_MANAGER("库存管理人员"), // 有INTERN,JUNIOR,SENIOR三级
    SALE_STAFF("进货销售人员"), // 有INTERN,JUNIOR,SENIOR三级
    FINANCIAL_STAFF("财务人员"), // 有JUNIOR,SENIOR两级
    SALE_MANAGER("销售经理"), // 有SENIOR,PRINCIPLE两级
    HR("人力资源人员"), // 有SENIOR,PRINCIPLE两级
    GM("总经理"); // 仅有PRINCIPLE一级

    private final String value;

    Department(String value) { this.value = value; }

    @Override
    public String getValue() { return value;}
}
