package com.nju.edu.erp.model.vo.HumanResource;

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
public class EmployeeVO {

    /**
     * 员工id，唯一标识，由自动增量指定
     */
    private Integer id;
    /**
     * 姓名，必填项
     */
    private String name;
    /**
     * 性别，必填项
     */
    private Sex sex;
    /**
     * 出生日期，格式为yyyy-MM-dd，必填项
     */
    private String birthday;
    /**
     * 电话号码，为11位数字，必填项
     */
    private String phone;
    /**
     * 工作岗位，必填项
     */
    private Department department;
    /**
     * 岗位级别，必填项
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