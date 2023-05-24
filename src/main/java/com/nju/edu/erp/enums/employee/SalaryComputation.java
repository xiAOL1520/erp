package com.nju.edu.erp.enums.employee;

import com.nju.edu.erp.enums.BaseEnum;

public enum SalaryComputation implements BaseEnum<SalaryComputation, String> {
    BASIC("基本工资"),
    BASIC_DEPART("基本工资+岗位工资"),
    BASIC_BONES("基本工资+奖金/绩效工资"),
    BASIC_DEPART_BONES("基本工资+岗位工资+奖金/绩效工资");

    private final String value;

    SalaryComputation(String value) { this.value = value; }

    @Override
    public String getValue() { return value;}

}
