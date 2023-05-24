package com.nju.edu.erp.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface AttendanceDao {

    /**
     * 查询指定员工的出勤日期
     * @param id 员工id
     * @return attendance 出勤天数
     */
    List<String> getAttendance (Integer id);

    /**
     * 员工打卡
     * @param id 员工id
     * @param date 当日日期
     */
    void clockIn (Integer id, String date);

    /**
     * 查询某个员工的打卡日期
     * @param id 员工id
     * @return dateList
     */
    List<String> getById (Integer id);

    /**
     * 查询某日打卡情况
     * @param date date
     * @return employeeIdList
     */
    List<Integer> getByDate (String date);

}
