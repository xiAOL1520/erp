package com.nju.edu.erp.dao;

import com.nju.edu.erp.model.po.EmployeePO;
import com.nju.edu.erp.model.vo.HumanResource.EmployeeVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface EmployeeDao {

    /**
     * 根据员工id查找员工
     * @param id 员工id
     * @return employeePO
     */
    EmployeePO getEmployeeById(Integer id);

    /**
     * 把员工信息存入数据库
     * @param employeePO employeePO
     */
    int setEmployee(EmployeePO employeePO);

    /**
     * 修改员工信息
     * @param employeePO employeePO
     */
    int updateEmployee(EmployeePO employeePO);

    /**
     * 删除员工信息
     * @param id 员工id
     */
    int deleteEmployee(Integer id);

    /**
     * 返回所有员工
     * @return employeePOList
     */
    List<EmployeePO> getAllEmployees();

    /**
     * 获取最新增加的员工的id
     * @return latestId 最新增加的员工的id
     */
    Integer getLatestId();
}
