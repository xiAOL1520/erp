package com.nju.edu.erp.enums.employee;

import com.nju.edu.erp.enums.BaseEnum;

public enum DepartmentLevel implements BaseEnum<DepartmentLevel, String> {
    INTERN("实习生"),
    JUNIOR("初级人员"),
    SENIOR("高级人员"),
    PRINCIPLE("总管");

    private final String value;

    DepartmentLevel(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }

}
