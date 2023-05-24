package com.nju.edu.erp.service;

import com.nju.edu.erp.model.vo.HumanResource.EmployeeVO;

import java.util.List;

public interface HumanResourceService {

    /**
     * 新增入职员工
     * @param employeeVO employeeVO
     * @return id 新增员工的自动增量id
     */
    Integer addEmployee(EmployeeVO employeeVO);

    /**
     * 根据员工id获取员工信息
     * @param employeeId 员工id
     * @return employeeVO
     */
    EmployeeVO getEmployee(Integer employeeId);

    /**
     * 修改员工信息
     * @param employeeVO employeeVO
     */
    void updateEmployee(EmployeeVO employeeVO);

    /**
     * 删除员工
     * @param employeeId 员工id
     */
    void deleteEmployee(Integer employeeId);

    /**
     * 获取所有员工的信息
     * @return Response.buildSuccess(employeeVOList)
     */
    List<EmployeeVO> getAllEmployees();

}
