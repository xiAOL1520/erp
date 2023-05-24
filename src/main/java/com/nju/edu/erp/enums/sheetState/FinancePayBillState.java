package com.nju.edu.erp.enums.sheetState;

import com.nju.edu.erp.enums.BaseEnum;

public enum FinancePayBillState implements BaseEnum<FinancePayBillState, String> {
    PENDING_LEVEL_1("待审批"), // 待总经理审批
    SUCCESS("审批完成"),
    FAILURE("审批失败");

    private final String value;

    FinancePayBillState(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }
}
