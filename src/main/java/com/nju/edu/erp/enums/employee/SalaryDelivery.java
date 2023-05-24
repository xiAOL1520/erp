package com.nju.edu.erp.enums.employee;

import com.nju.edu.erp.enums.BaseEnum;

public enum SalaryDelivery implements BaseEnum<SalaryDelivery, String> {
    DAILY("日薪制"),
    MONTHLY("月薪制"),
    YEARLY("年薪制");

    private final String value;

    SalaryDelivery(String value) { this.value = value; }

    @Override
    public String getValue() { return value;}

}
