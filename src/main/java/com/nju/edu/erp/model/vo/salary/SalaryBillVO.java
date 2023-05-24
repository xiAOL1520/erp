package com.nju.edu.erp.model.vo.salary;

import com.nju.edu.erp.enums.employee.Department;
import com.nju.edu.erp.enums.employee.DepartmentLevel;
import com.nju.edu.erp.enums.sheetState.SalaryBillState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SalaryBillVO {

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
     * 姓名
     */
    private String name;
    /**
     * 工作岗位
     */
    private Department department;
    /**
     * 岗位级别
     */
    private DepartmentLevel departmentLevel;
    /**
     * 出勤天数
     */
    private Integer attendance;
    /**
     * 基本工资
     */
    private BigDecimal basicSalary;
    /**
     * 岗位工资
     */
    private BigDecimal departmentSalary;
    /**
     * 奖金/绩效工资
     */
    private BigDecimal bonus;
    /**
     * 应发工资
     */
    private BigDecimal payable;
    /**
     * 扣税
     */
    private BigDecimal tax;
    /**
     * 实付工资
     */
    private BigDecimal salary;
    /**
     * 审批状态
     */
    private SalaryBillState state;
}
