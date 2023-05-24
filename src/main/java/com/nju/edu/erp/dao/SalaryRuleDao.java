package com.nju.edu.erp.dao;

import com.nju.edu.erp.enums.employee.Department;
import com.nju.edu.erp.enums.employee.DepartmentLevel;
import com.nju.edu.erp.model.po.salary.SalaryRulePO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface SalaryRuleDao {

    /**
     * 更新某岗位某级别的工资制度信息
     * @param salaryRulePO salaryRulePO
     */
    void updateSalaryRule(SalaryRulePO salaryRulePO);

    /**
     * 获取某岗位某级别的工资制度信息
     * @return salaryRulePO
     */
    SalaryRulePO getSalaryRule(Department d, DepartmentLevel dl);

    /**
     * 获取所有岗位所有级别的工资制度信息
     * @return
     */
    List<SalaryRulePO> getAllSalaryRules();
}
