package com.nju.edu.erp.web.controller;

import com.nju.edu.erp.auth.Authorized;
import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.enums.sheetState.SalaryBillState;
import com.nju.edu.erp.model.po.EmployeePO;
import com.nju.edu.erp.model.po.salary.SalaryRulePO;
import com.nju.edu.erp.model.vo.salary.SalaryBillVO;
import com.nju.edu.erp.model.vo.salary.YearBonusVO;
import com.nju.edu.erp.service.SalaryService;
import com.nju.edu.erp.web.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping(path = "salary")
public class SalaryController {

    private final SalaryService salaryService;

    @Autowired
    public SalaryController (SalaryService salaryService) {
        this.salaryService = salaryService;
    }

    /**
     * 生成一条工资单
     * 日薪制员工当日发放工资，date即是工作日期，同时也是发放工资的日期
     * 月薪制员工次月5日发放上月工资，date是上月某日的日期，会自动转换为正确格式yyyy-MM-00
     * 总经理12月发放工资，date可以是任意日期，会自动转换为正确格式yyyy-00-00
     * @param employeeId 员工id
     * @param date 工作日期，格式为yyyy-MM-dd
     * @return Response.buildSuccess(salaryBillVO)
     */
    @Authorized (roles = {Role.HR, Role.ADMIN})
    @GetMapping (value = "makeSalaryBill")
    public Response makeSalaryBill (@RequestParam Integer employeeId, @RequestParam String date) {
        try {
            SalaryBillVO salaryBillVO = salaryService.makeSalaryBill(employeeId, date);
            return Response.buildSuccess(salaryBillVO);
        }catch (RuntimeException e) {
            return Response.buildFailed("00001", e);
        }
    }

    /**
     * 生成所有月薪制员工的工资单
     * @param date 工作日期，格式为yyyy-MM-00
     * @return Response.buildSuccess(salaryBillVOList)
     */
    @Authorized (roles = {Role.HR, Role.ADMIN})
    @GetMapping (value = "makeMonthSalaryBills")
    public Response makeMonthSalaryBills (@RequestParam("date") String date) {
        try {
            List<SalaryBillVO> salaryBillVOList = salaryService.makeMonthSalaryBills(date);
            return Response.buildSuccess(salaryBillVOList);
        }catch (RuntimeException e) {
            return Response.buildFailed("00001", e);
        }
    }

    /**
     * 人力资源人员审批工资单
     * @param employeeId 员工id
     * @param date 工作日期
     * @return Response.buildSuccess()
     */
    @Authorized (roles = {Role.HR, Role.ADMIN})
    @GetMapping (value = "HRAudit")
    public Response HRAudit (@RequestParam Integer employeeId, @RequestParam String date, @RequestParam SalaryBillState salaryBillState) {
        try {
            salaryService.HRAudit(employeeId, date, salaryBillState);
            return Response.buildSuccess();
        }catch (RuntimeException e) {
            return Response.buildFailed("00001", e);
        }
    }

    /**
     * 总经理审批工资单
     * @param employeeId 员工id
     * @param date 工作日期
     * @return Response.buildSuccess()
     */
    @Authorized (roles = {Role.GM, Role.ADMIN})
    @GetMapping (value = "GMAudit")
    public Response GMAudit (@RequestParam Integer employeeId, @RequestParam String date, @RequestParam SalaryBillState salaryBillState) {
        try {
            salaryService.GMAudit(employeeId, date, salaryBillState);
            return Response.buildSuccess();
        }catch (RuntimeException e) {
            return Response.buildFailed("00001", e);
        }
    }

    /**
     * 为销售人员增加提成
     * @param employeeId 销售人员id
     * @param date 月份，格式为yyyy-MM-dd，会自动转换为正确格式yyyy-MM-00
     * @param bonus 某月提成
     * @return Response.buildSuccess()
     */
    @Authorized (roles = {Role.HR, Role.ADMIN})
    @GetMapping (value = "addBonus")
    public Response addBonus (@RequestParam Integer employeeId, @RequestParam String date, @RequestParam BigDecimal bonus) {
        try {
            salaryService.addBonus(employeeId, date, bonus);
            return Response.buildSuccess();
        } catch (RuntimeException e) {
            return Response.buildFailed("00001", e);
        }
    }

    /**
     * 总经理设置年终奖
     * @param employeeId 员工id
     * @param date 年份，格式为yyyy-MM-dd，会自动转换为正确格式yyyy-00-00
     * @param yearBonus 年终奖
     * @return Response.buildSuccess()
     */
    @Authorized (roles = {Role.GM, Role.ADMIN})
    @GetMapping (value = "setYearBonus")
    public Response setYearBonus (@RequestParam Integer employeeId, @RequestParam String date, @RequestParam BigDecimal yearBonus) {
        try {
            salaryService.setYearBonus(employeeId, date, yearBonus);
            return Response.buildSuccess();
        } catch (RuntimeException e) {
            return Response.buildFailed("00001", e);
        }
    }

    @Authorized (roles = {Role.GM, Role.ADMIN})
    @GetMapping (value = "getYearBonus")
    public Response getYearBonus (@RequestParam String date) {
        try {
            List<YearBonusVO> yearBonusVOList = salaryService.getYearBonus(date);
            return Response.buildSuccess(yearBonusVOList);
        } catch (RuntimeException e) {
            return Response.buildFailed("00001", e);
        }
    }


    /**
     * 获取所有岗位所有级别的工资制度信息
     * @return Response.buildSuccess(salaryRulePOList)
     */
    @GetMapping (value = "getAllSalaryRules")
    public Response getAllSalaryRules () {
        List<SalaryRulePO> salaryRulePOList = salaryService.getAllSalaryRules();
        return Response.buildSuccess(salaryRulePOList);
    }

    /**
     * 更改某岗位某级别的工资制度信息
     * @param salaryRulePO salaryRulePO
     */
    @GetMapping (value = "updateSalaryRule")
    public Response updateSalaryRule (SalaryRulePO salaryRulePO) {
         salaryService.updateSalaryRule(salaryRulePO);
        return Response.buildSuccess();
    }

}
