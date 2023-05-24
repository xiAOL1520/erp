package com.nju.edu.erp.dao;

import com.nju.edu.erp.model.po.salary.SalaryBillPO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface SalaryBillDao {

    /**
     * 查找一条工资单记录
     * @param id 员工id
     * @param date 工资所属的日期
     */
    SalaryBillPO getSalaryBill(Integer id, String date);

    /**
     * 插入一条工资单记录
     * @param salaryBillPO salaryBillPO
     */
    int addSalaryBill (SalaryBillPO salaryBillPO);

    /**
     * 更新工资单的审批和发放记录
     * @param salaryBillPO salaryBillPO
     */
    int updateSalaryBill(SalaryBillPO salaryBillPO);

    /**
     * 返回所有工资单记录
     * @return salaryBillPOList
     */
    List<SalaryBillPO> getAllSalaryBills();

}
