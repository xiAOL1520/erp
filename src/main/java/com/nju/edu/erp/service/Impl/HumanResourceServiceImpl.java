package com.nju.edu.erp.service.Impl;

import com.nju.edu.erp.dao.EmployeeDao;
import com.nju.edu.erp.dao.SalaryRuleDao;
import com.nju.edu.erp.dao.UserDao;
import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.enums.employee.*;
import com.nju.edu.erp.model.po.EmployeePO;
import com.nju.edu.erp.model.po.User;
import com.nju.edu.erp.model.po.salary.SalaryRulePO;
import com.nju.edu.erp.model.vo.HumanResource.EmployeeVO;
import com.nju.edu.erp.service.HumanResourceService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class HumanResourceServiceImpl implements HumanResourceService {

    private final EmployeeDao employeeDao;
    private final UserDao userDao;
    private final SalaryRuleDao salaryRuleDao;
    private List<SalaryRulePO> salaryRulePOList = new ArrayList<>();

    @Autowired
    public HumanResourceServiceImpl(EmployeeDao employeeDao, UserDao userDao, SalaryRuleDao salaryRuleDao) {
        this.employeeDao = employeeDao;
        this.userDao = userDao;
        this.salaryRuleDao = salaryRuleDao;
        this.salaryRulePOList = salaryRuleDao.getAllSalaryRules();
    }

    @Override
    @Transactional
    public Integer addEmployee(EmployeeVO employeeVO){

        Integer latestId = employeeDao.getLatestId();
        Integer id;
        if(latestId == null)
            id = 1000;
        else
            id = latestId + 1;
        employeeVO.setId(id);

        blankInfo(employeeVO);

        EmployeePO employeePO = new EmployeePO();
        BeanUtils.copyProperties(employeeVO,employeePO);

        if(!Pattern.matches("\\d\\d\\d\\d-\\d\\d-\\d\\d", employeePO.getBirthday())) {
            throw new RuntimeException("无效出生日期：格式应为yyyy-MM-dd！");
        }
        if(!Pattern.matches("\\d{11}", employeePO.getPhone())) {
            throw new RuntimeException("无效电话号码：格式应为11位数字！");
        }
        employeePO.setBasicSalary(new BigDecimal(3000));

        Department d = employeePO.getDepartment();
        DepartmentLevel dl = employeePO.getDepartmentLevel();
        employeePO.setDepartmentSalary((BigDecimal) salaryDetails(d, dl, "departmentSalary"));
        employeePO.setSalaryComputation( (SalaryComputation) salaryDetails(d, dl, "salaryComputation"));
        employeePO.setSalaryDelivery( (SalaryDelivery) salaryDetails(d, dl, "salaryDelivery"));

        User user = new User();
        user.setId(employeePO.getId());
        user.setName(employeePO.getName());
        user.setPassword("123456");
        user.setRole(Role.valueOf(employeePO.getDepartment().toString()));
        userDao.createUser(user);

        employeeDao.setEmployee(employeePO);

        return id;
    }

    @Override
    @Transactional
    public EmployeeVO getEmployee(Integer employeeId){
        if(employeeDao.getEmployeeById(employeeId) == null) {
            throw new RuntimeException("查找员工失败：员工不存在！");
        }

        EmployeePO employeePO = employeeDao.getEmployeeById(employeeId);
        EmployeeVO employeeVO = new EmployeeVO();
        BeanUtils.copyProperties(employeePO,employeeVO);
        return employeeVO;
    }

    @Override
    @Transactional
    public void updateEmployee(EmployeeVO employeeVO){
        if(employeeDao.getEmployeeById(employeeVO.getId()) == null) {
            throw new RuntimeException("更新员工信息失败：员工不存在！");
        }

        blankInfo(employeeVO);

        if(!Pattern.matches("\\d\\d\\d\\d-\\d\\d-\\d\\d", employeeVO.getBirthday())) {
            throw new RuntimeException("无效出生日期：格式应为yyyy-MM-dd！");
        }
        if(!Pattern.matches("\\d{11}", employeeVO.getPhone())) {
            throw new RuntimeException("无效电话号码：格式应为11位数字！");
        }
        if(employeeVO.getBasicSalary() == null) { employeeVO.setBasicSalary(new BigDecimal(3000)); }
        if(employeeVO.getBasicSalary().compareTo(new BigDecimal(3000)) != 0) {
            throw new RuntimeException("请勿修改基本工资！");
        }
        matchSalary(employeeVO);

        EmployeePO employeePO = new EmployeePO();
        BeanUtils.copyProperties(employeeVO, employeePO);
        employeeDao.updateEmployee(employeePO);
    }

    @Override
    @Transactional
    public void deleteEmployee(Integer employeeId){
        employeeDao.deleteEmployee(employeeId);
    }

    private Object salaryDetails(Department department, DepartmentLevel departmentLevel, String get) {
        for(int i=0; i < 13; i++) {
            SalaryRulePO salaryRulePO = salaryRulePOList.get(i);
            if( department == salaryRulePO.getDepartment() && departmentLevel == salaryRulePO.getDepartmentLevel() )
            if(get.equals("departmentSalary"))
                return salaryRulePO.getDepartmentSalary();
            else if(get.equals("salaryComputation"))
                return salaryRulePO.getSalaryComputation();
            else if(get.equals("salaryDelivery"))
                return salaryRulePO.getSalaryDelivery();
        }

        throw new RuntimeException("岗位级别不匹配：该岗位无该级别！");
    }

    @Override
    @Transactional
    public List<EmployeeVO> getAllEmployees() {

        List<EmployeePO> employeePOList = employeeDao.getAllEmployees();
        List<EmployeeVO> employeeVOList = new ArrayList<>();
        for(EmployeePO employeePO : employeePOList) {
            EmployeeVO employeeVO = new EmployeeVO();
            BeanUtils.copyProperties(employeePO, employeeVO);
            employeeVOList.add(employeeVO);
        }

        return employeeVOList;
    }

    private EmployeeVO matchSalary(EmployeeVO employeeVO) {

        Department d = employeeVO.getDepartment();
        DepartmentLevel dl = employeeVO.getDepartmentLevel();
        BigDecimal ds = employeeVO.getDepartmentSalary();
        SalaryComputation sc = employeeVO.getSalaryComputation();
        SalaryDelivery sd = employeeVO.getSalaryDelivery();

        boolean d_dl = false;
        for(int i=0; i < 13; i++) {
            SalaryRulePO salaryRulePO = salaryRulePOList.get(i);
            if(d == salaryRulePO.getDepartment() && dl == salaryRulePO.getDepartmentLevel()) {
                d_dl = true;
                break;
            }
        }
        if(!d_dl) { throw new RuntimeException("岗位级别不匹配：该岗位无该级别！"); }

        boolean dsIn = false;
        boolean scIn = false;
        boolean sdIn = false;
        for(int i=0; i < 13; i++) {
            SalaryRulePO salaryRulePO = salaryRulePOList.get(i);
            if(d == salaryRulePO.getDepartment() && dl == salaryRulePO.getDepartmentLevel()) {
                if(ds == null) { ds = salaryRulePO.getDepartmentSalary(); }
                if(ds.compareTo( salaryRulePO.getDepartmentSalary() ) == 0)
                    dsIn = true;
                if(sc == null) { sc = salaryRulePO.getSalaryComputation(); }
                if(sc == salaryRulePO.getSalaryComputation())
                    scIn = true;
                if(sd == null) { sd = salaryRulePO.getSalaryDelivery(); }
                if(sd == salaryRulePO.getSalaryDelivery())
                    sdIn = true;
                break;
            }
        }
        if(!dsIn) { throw new RuntimeException("岗位薪资不匹配！"); }
        if(!scIn) { throw new RuntimeException("薪资计算方式不匹配！"); }
        if(!sdIn) { throw new RuntimeException("薪资发放方式不匹配！"); }

        employeeVO.setDepartmentSalary(ds);
        employeeVO.setSalaryComputation(sc);
        employeeVO.setSalaryDelivery(sd);
        return employeeVO;
    }

    private void blankInfo(EmployeeVO employeeVO) {
        Object[] infos = new Object[6];
        infos[0] = employeeVO.getName();
        infos[1] = employeeVO.getSex();
        infos[2] = employeeVO.getBirthday();
        infos[3] = employeeVO.getPhone();
        infos[4] = employeeVO.getDepartment();
        infos[5] = employeeVO.getDepartmentLevel();
        String[] strs = {"姓名", "性别", "出生日期", "电话号码", "工作岗位", "岗位级别"};

        for(int i=0; i < 6; i++)
            if(infos[i] == null)
                throw new RuntimeException(strs[i] + "为必填项！");
    }

}
