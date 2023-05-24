package com.nju.edu.erp.service.Impl;

import com.nju.edu.erp.dao.AttendanceDao;
import com.nju.edu.erp.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceDao attendanceDao;

    @Autowired
    public AttendanceServiceImpl(AttendanceDao attendanceDao) {
        this.attendanceDao = attendanceDao;
    }

    @Override
    @Transactional
    public void clockIn(Integer employeeId) {
        attendanceDao.clockIn(employeeId, LocalDate.now().toString());
    }

    @Override
    @Transactional
    public List<Integer> viewByDate(String date) {
        return attendanceDao.getByDate(date);
    }

    @Override
    @Transactional
    public List<String> viewByEmployee(Integer employeeId) {
        return attendanceDao.getById(employeeId);
    }

}
