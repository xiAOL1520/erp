package com.nju.edu.erp.model.po.salary;

import com.nju.edu.erp.enums.employee.Department;
import com.nju.edu.erp.enums.employee.DepartmentLevel;
import com.nju.edu.erp.enums.employee.SalaryComputation;
import com.nju.edu.erp.enums.employee.SalaryDelivery;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SalaryRulePO {

    /**
     * 岗位，不可更改
     */
    private Department department;
    /**
     * 岗位级别，不可更改
     */
    private DepartmentLevel departmentLevel;
    /**
     * 岗位工资
     */
    private BigDecimal departmentSalary;
    /**
     * 薪资计算方式
     */
    private SalaryComputation salaryComputation;
    /**
     * 薪资发放方式
     */
    private SalaryDelivery salaryDelivery;

}
