package com.nju.edu.erp.service;

import java.util.List;

public interface AttendanceService {

    /**
     * 员工今日打卡
     * @param employeeId employeeId
     */
    void clockIn(Integer employeeId);

    /**
     * 查看某日打卡情况
     * @param date date
     * @return attendanceVOList
     */
    List<Integer> viewByDate(String date);

    /**
     * 查看某个员工打卡情况
     * @param employeeId employeeId
     * @return dateList
     */
    List<String> viewByEmployee(Integer employeeId);
}
