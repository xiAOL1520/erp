package com.nju.edu.erp.service;

import com.nju.edu.erp.enums.sheetState.SalaryBillState;
import com.nju.edu.erp.model.po.salary.SalaryRulePO;
import com.nju.edu.erp.model.vo.salary.SalaryBillVO;
import com.nju.edu.erp.model.vo.salary.YearBonusVO;

import java.math.BigDecimal;
import java.util.List;

public interface SalaryService {

    /**
     * 生成一条工资单
     * @param employeeId 员工id
     * @param date 工资所属的日期，格式为yyyy-MM-dd
     * @return salaryBillVO
     */
    SalaryBillVO makeSalaryBill (Integer employeeId, String date);

    /**
     * 生成所有月薪制员工的工资单
     * @param date 工资所属的日期，格式为yyyy-MM-00
     * @return salaryBillVOList
     */
    List<SalaryBillVO> makeMonthSalaryBills(String date);

    /**
     * 查找指定时间区间内的员工的工资单
     * @param employeeIdList 要查找的员工id列表。如查找所有员工，提供空列表
     * @param datePair 要查找的时间区间。
     *                 提供零个或两个参数，不必按顺序，可以相等。
     *                 提供两个以上的参数也不会报错，只取前两个。提供一个参数会报错。
     *                 格式为yyyy-MM-dd
     * @return salaryBillVOList
     */
    List<SalaryBillVO> findSalaryBills(List<Integer> employeeIdList, List<String> datePair);

    /**
     * 人力资源人员审批工资单
     * @param employeeId 员工id
     * @param date 工作日期
     */
    void HRAudit (Integer employeeId, String date,  SalaryBillState salaryBillState);

    /**
     * 财务人员审批工资单
     * @param employeeId 员工id
     * @param date 工作日期
     */
    void GMAudit (Integer employeeId, String date, SalaryBillState salaryBillState);

    /**
     * 给销售人员增加提成
     * @param employeeId 员工id
     * @param date 工作日期
     * @param bonus 提成金额
     */
    void addBonus (Integer employeeId, String date, BigDecimal bonus);

    /**
     * 设定某个员工本年的年终奖
     * @param employeeId 员工id
     * @param date 日期
     * @param yearBonus 年终奖
     */
    void setYearBonus (Integer employeeId, String date, BigDecimal yearBonus);

    /**
     * 更改某岗位某级别的工资制度信息
     * @param salaryRulePO salaryRulePO
     */
    void updateSalaryRule(SalaryRulePO salaryRulePO);
    /**
     * 获取所有岗位所有级别的工资制度信息
     * @return salaryRulePOList
     */
    List<SalaryRulePO> getAllSalaryRules();

    /**
     * 获取所有的年终奖信息
     * @return yearBonusVOList
     */
    List<YearBonusVO> getYearBonus(String date);

}
