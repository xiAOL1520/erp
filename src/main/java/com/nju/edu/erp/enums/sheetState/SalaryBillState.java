package com.nju.edu.erp.enums.sheetState;

import com.nju.edu.erp.enums.BaseEnum;

public enum SalaryBillState implements BaseEnum<SalaryBillState, String> {

    PENDING_LEVEL_1("待一级审批"), // 待人力资源人员审批
    PENDING_LEVEL_2("待二级审批"), // 待总经理审批
    SUCCESS("审批成功"),
    FAILURE("审批失败");

    private final String value;

    SalaryBillState(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }
}
