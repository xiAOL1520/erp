package com.nju.edu.erp.service;

import com.nju.edu.erp.dao.BonusDao;
import com.nju.edu.erp.dao.SalaryBillDao;
import com.nju.edu.erp.enums.SheetType;
import com.nju.edu.erp.enums.employee.Department;
import com.nju.edu.erp.enums.employee.DepartmentLevel;
import com.nju.edu.erp.enums.employee.Sex;
import com.nju.edu.erp.enums.sheetState.SalaryBillState;
import com.nju.edu.erp.model.vo.HumanResource.EmployeeVO;
import com.nju.edu.erp.model.vo.finance.FinanceManageProcessVO;
import com.nju.edu.erp.model.vo.salary.SalaryBillVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class SalaryTest {

    @Autowired
    SalaryService salaryService;

    @Autowired
    HumanResourceService humanResourceService;

    @Autowired
    AttendanceService attendanceService;

    /**
     * 测试salaryService的基本功能
     */
    @Test
    @Transactional
    @Rollback(value = true)
    public void salaryBasicTest1() {

        EmployeeVO employeeVO1 = EmployeeVO.builder()
                .name("Akira")
                .sex(Sex.WOMAN)
                .birthday("2022-07-04")
                .phone("12312345678")
                .department(Department.SALE_STAFF)
                .departmentLevel(DepartmentLevel.JUNIOR)
                .build();
        Integer id1 = humanResourceService.addEmployee(employeeVO1);
        String date1 = "2022-06-07";

        SalaryBillVO salaryBillVO1 = salaryService.makeSalaryBill(id1, date1);
        Assertions.assertEquals(salaryBillVO1.getDate(), "2022-06-00");
        System.out.println(salaryBillVO1.getPayable());
        Assertions.assertTrue(salaryBillVO1.getPayable().compareTo(new BigDecimal(2800)) == 0);
        Assertions.assertTrue(salaryBillVO1.getTax().compareTo(new BigDecimal(84)) == 0);
        Assertions.assertTrue(salaryBillVO1.getSalary().compareTo(new BigDecimal(2716)) == 0);

        salaryService.addBonus(id1, date1, new BigDecimal(500));
        salaryBillVO1 = salaryService.makeSalaryBill(id1, date1);
        Assertions.assertTrue(salaryBillVO1.getBonus().compareTo(new BigDecimal(500)) == 0);

        Assertions.assertThrows(RuntimeException.class, () -> {
            salaryService.GMAudit(id1, date1, SalaryBillState.SUCCESS);
        });
        salaryService.HRAudit(id1, date1, SalaryBillState.PENDING_LEVEL_2);
        salaryService.GMAudit(id1, date1, SalaryBillState.SUCCESS);

        String date2 = "2022-12-24";
        salaryService.setYearBonus(id1, date2, new BigDecimal(10000));
        SalaryBillVO salaryBillVO2 = salaryService.makeSalaryBill(id1, date2);
        Assertions.assertTrue(salaryBillVO2.getPayable().compareTo(new BigDecimal(12800)) == 0);
        salaryService.setYearBonus(id1, date2, new BigDecimal(5000));
        salaryBillVO2 = salaryService.makeSalaryBill(id1, date2);
        Assertions.assertTrue(salaryBillVO2.getPayable().compareTo(new BigDecimal(7800)) == 0);
        salaryService.getYearBonus(date2);

    }

    /**
     * 测试salaryService的基本功能
     * 测试makeMonthSalaryBills
     */
    @Test
    @Transactional
    @Rollback(value = true)
    public void salaryBasicTest2() {

        EmployeeVO employeeVO1 = EmployeeVO.builder()
                .name("Akira")
                .sex(Sex.WOMAN)
                .birthday("2022-07-04")
                .phone("12312345678")
                .department(Department.INVENTORY_MANAGER)
                .departmentLevel(DepartmentLevel.JUNIOR)
                .build();
        Integer id1 = humanResourceService.addEmployee(employeeVO1);
        EmployeeVO employeeVO2 = EmployeeVO.builder()
                .name("Rin")
                .sex(Sex.MAN)
                .birthday("2003-01-10")
                .phone("12312384293")
                .department(Department.HR)
                .departmentLevel(DepartmentLevel.SENIOR)
                .build();
        Integer id2 = humanResourceService.addEmployee(employeeVO2);
        EmployeeVO employeeVO3 = EmployeeVO.builder()
                .name("Umi")
                .sex(Sex.WOMAN)
                .birthday("1934-12-30")
                .phone("12315397628")
                .department(Department.GM)
                .departmentLevel(DepartmentLevel.PRINCIPLE)
                .build();
        Integer id3 = humanResourceService.addEmployee(employeeVO3);
        EmployeeVO employeeVO4 = EmployeeVO.builder()
                .name("Eri")
                .sex(Sex.WOMAN)
                .birthday("1998-01-01")
                .phone("12317359824")
                .department(Department.INVENTORY_MANAGER)
                .departmentLevel(DepartmentLevel.INTERN)
                .build();
        Integer id4 = humanResourceService.addEmployee(employeeVO4);

        List<SalaryBillVO> salaryBillVOList = salaryService.makeMonthSalaryBills("2022-08-06");
        Assertions.assertTrue(salaryBillVOList.size() >2);

    }

    /**
     * 测试findSalaryBills是否正常工作
     */
    @Test
    @Transactional
    @Rollback(value = true)
    public void findSalaryBillsTest() {

        List<Integer> employeeIdList = new ArrayList<>();
        List<String> datePair = new ArrayList<>();
        List<SalaryBillVO> salaryBillVOList = new ArrayList<>();

        EmployeeVO employeeVO = EmployeeVO.builder()
                .name("Akira")
                .sex(Sex.WOMAN)
                .birthday("2022-07-04")
                .phone("12312345678")
                .department(Department.INVENTORY_MANAGER)
                .departmentLevel(DepartmentLevel.INTERN)
                .build();
        Integer id1 = humanResourceService.addEmployee(employeeVO);

        String[] dates = {
                "2000-03-07",
                "2010-04-18", "2010-11-04",
                "2010-07-09", "2010-07-15", "2010-07-27",
                "2020-01-31"};

        SalaryBillVO salaryBillVO1 = salaryService.makeSalaryBill(id1, dates[4]);

        // 无条件查询
        salaryBillVOList = salaryService.findSalaryBills(employeeIdList, datePair);
        Assertions.assertNotEquals(salaryBillVOList.size(), 0);

        // 查询指定id时无匹配的情况
        employeeIdList.add(1919810);
        salaryBillVOList = salaryService.findSalaryBills(employeeIdList, datePair);
        Assertions.assertEquals(salaryBillVOList.size(), 0);

        // 查询指定id时有匹配的情况
        SalaryBillVO salaryBillVO2 = salaryService.makeSalaryBill(id1, dates[5]);
        employeeIdList = new ArrayList<>();
        employeeIdList.add(id1);
        salaryBillVOList = salaryService.findSalaryBills(employeeIdList, datePair);
        Assertions.assertEquals(salaryBillVOList.size(), 2);

        // 查询指定日期区间内
        datePair.add("2010-07-01");
        datePair.add("2010-07-31");
        salaryBillVOList = salaryService.findSalaryBills(employeeIdList, datePair);
        Assertions.assertEquals(salaryBillVOList.size(), 2);

        // 查询指定日期边界
        datePair = new ArrayList<>();
        datePair.add("2010-07-15");
        datePair.add("2010-07-16");
        salaryBillVOList = salaryService.findSalaryBills(employeeIdList, datePair);
        Assertions.assertEquals(salaryBillVOList.size(), 1);

    }

    /**
     * 测试AttendanceService的基本功能
     */
    @Test
    @Transactional
    @Rollback(value = true)
    public void attendanceTest() {

        EmployeeVO employeeVO = EmployeeVO.builder()
                .name("Akira")
                .sex(Sex.WOMAN)
                .birthday("2002-07-04")
                .phone("12312345678")
                .department(Department.SALE_STAFF)
                .departmentLevel(DepartmentLevel.JUNIOR)
                .build();
        Integer id = humanResourceService.addEmployee(employeeVO);
        String date = LocalDate.now().toString();

        Assertions.assertEquals(attendanceService.viewByEmployee(id).size(), 0);
        attendanceService.clockIn(id);
        Assertions.assertEquals(attendanceService.viewByEmployee(id).size(), 1);
        Assertions.assertEquals(attendanceService.viewByDate(date).get(0), id);

    }

}
