package com.nju.edu.erp.model.po.salary;

import com.nju.edu.erp.enums.sheetState.SalaryBillState;
import com.nju.edu.erp.enums.sheetState.SaleSheetState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SalaryBillPO {

    /**
     * 工资所属的日期，格式为yyyy-MM-dd
     * dd为00表示按月发放。MM为00，dd为00表示按年发放
     */
    private String date;
    /**
     * 员工id
     */
    private Integer id;
    /**
     * 出勤天数
     */
    private Integer attendance;
    /**
     * 审批状态
     */
    private SalaryBillState state;

}
