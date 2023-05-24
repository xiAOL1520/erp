package com.nju.edu.erp.enums;

public enum SheetType implements BaseEnum<SheetType, String>{
    SALE("销售类"),
    PURCHASE("进货类"),
    FINANCE("财务类"),
    WAREHOUSE("库存类"),
    SALARY("工资单");


    private final String value;

    SheetType(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }
}
