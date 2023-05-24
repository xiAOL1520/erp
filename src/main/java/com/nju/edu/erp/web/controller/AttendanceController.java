package com.nju.edu.erp.web.controller;

import com.nju.edu.erp.auth.Authorized;
import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.service.AttendanceService;
import com.nju.edu.erp.web.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    @Autowired
    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    /**
     * 员工今日打卡
     * @param employeeId employeeId
     * @return Response.buildSuccess()
     */
    @GetMapping(value = "clockIn")
    public Response clockIn(@RequestParam Integer employeeId) {
        try {
            attendanceService.clockIn(employeeId);
            return Response.buildSuccess();
        } catch (RuntimeException e) {
            return Response.buildFailed("00001", e);
        }
    }

    /**
     * 查看某日打卡情况
     * @param date date
     * @return Response.buildSuccess(attendanceService.viewByDate(date))
     */
    @Authorized (roles = {Role.HR, Role.ADMIN})
    @GetMapping(value = "/viewByDate")
    public Response viewByDate(@RequestParam String date) {
        try {
            return Response.buildSuccess(attendanceService.viewByDate(date));
        } catch (RuntimeException e) {
            return Response.buildFailed("00001", e);
        }
    }

    /**
     * 查看某个员工打卡情况
     * @param employeeId employeeId
     * @return Response.buildSuccess(attendanceService.viewByEmployee())
     */
    @Authorized (roles = {Role.HR, Role.ADMIN})
    @GetMapping(value = "/viewByEmployee")
    public Response viewByEmployee(@RequestParam Integer employeeId) {
        try {
            return Response.buildSuccess(attendanceService.viewByEmployee(employeeId));
        } catch (RuntimeException e) {
            return Response.buildFailed("00001", e);
        }
    }

}
