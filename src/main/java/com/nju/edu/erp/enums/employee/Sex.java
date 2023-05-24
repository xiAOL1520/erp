package com.nju.edu.erp.enums.employee;

import com.nju.edu.erp.enums.BaseEnum;

public enum Sex implements BaseEnum<Sex, String> {
    MAN("男"),
    WOMAN("女");

    private final String value;

    Sex(String value) { this.value = value; }

    @Override
    public String getValue() { return value;}
}
