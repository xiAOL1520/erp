package com.nju.edu.erp.web.controller;

import com.nju.edu.erp.auth.Authorized;
import com.nju.edu.erp.model.vo.HumanResource.EmployeeVO;
import com.nju.edu.erp.service.HumanResourceService;
import com.nju.edu.erp.web.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.nju.edu.erp.enums.Role;

import java.util.List;
@RestController
@RequestMapping(path = "/humanResource")

public class HumanResourceController {
    private final HumanResourceService humanResourceService;

    @Autowired
    public HumanResourceController(HumanResourceService humanResourceService) {
        this.humanResourceService = humanResourceService;
    }

    /**
     * 员工入职时新增员工
     * @param employeeVO employeeVO
     * @return Response.buildSuccess(id)
     */
    @Authorized (roles = {Role.HR,Role.ADMIN})
    @PostMapping (value = "addEmployee")
    public Response addEmployee(@RequestBody EmployeeVO employeeVO) {
        try {
            Integer id = humanResourceService.addEmployee(employeeVO);
            return Response.buildSuccess(id);
        } catch (RuntimeException e) {
            return Response.buildFailed("00001", e);
        }
    }

    /**
     * 根据员工id获取员工信息
     * @param employeeId 员工id
     * @return Response.buildSuccess(employeeVO)
     */
    @Authorized (roles = {Role.HR,Role.ADMIN})
    @GetMapping (value = "getEmployee")
    public Response getEmployee(@RequestParam("employeeId") Integer employeeId) {
        try {
            EmployeeVO employeeVO = humanResourceService.getEmployee(employeeId);
            return Response.buildSuccess(employeeVO);
        } catch (RuntimeException e) {
            return Response.buildFailed("00001", e);
        }
    }

    /**
     * 更新员工信息
     * @param employeeVO employeeVO
     * @return Response.buildSuccess()
     */
    @Authorized (roles = {Role.HR,Role.ADMIN})
    @PostMapping (value = "updateEmployee")
    public Response updateEmployee(@RequestBody EmployeeVO employeeVO) {
        try {
            humanResourceService.updateEmployee(employeeVO);
            return Response.buildSuccess();
        } catch (RuntimeException e) {
            return Response.buildFailed("00001", e);
        }
    }

    /**
     * 员工离职时删除员工
     * @param employeeId 员工id
     * @return Response.buildSuccess()
     */
    @Authorized (roles = {Role.HR,Role.ADMIN})
    @GetMapping (value = "deleteEmployee")
    public Response deleteEmployee(@RequestParam("employeeId") Integer employeeId) {
        try {
            humanResourceService.deleteEmployee(employeeId);
            return Response.buildSuccess();
        } catch (RuntimeException e) {
            return Response.buildFailed("00001", e);
        }
    }

    /**
     * 获取所有员工的信息
     * @return Response.buildSuccess(employeeVOList)
     */
    @Authorized (roles = {Role.HR,Role.ADMIN,Role.FINANCIAL_STAFF})
    @PostMapping (value = "getAllEmployees")
    public Response getAllEmployees() {
        try {
            List<EmployeeVO> employeeVOList = humanResourceService.getAllEmployees();
            return Response.buildSuccess(employeeVOList);
        } catch (RuntimeException e) {
            return Response.buildFailed("00001", e);
        }
    }

}
