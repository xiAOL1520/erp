package com.nju.edu.erp.model.po;

import com.nju.edu.erp.enums.employee.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeePO {

    /**
     * 员工id，唯一标识，由自动增量指定
     */
    private Integer id;
    /**
     * 姓名
     */
    private String name;
    /**
     * 性别
     */
    private Sex sex;
    /**
     * 出生日期，格式为yyyy-MM-dd
     */
    private String birthday;
    /**
     * 电话号码，为11位数字
     */
    private String phone;
    /**
     * 工作岗位
     */
    private Department department;
    /**
     * 岗位级别
     */
    private DepartmentLevel departmentLevel;
    /**
     * 基本工资，3000
     */
    private BigDecimal basicSalary;
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
