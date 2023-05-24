package com.nju.edu.erp.service;

import com.nju.edu.erp.enums.employee.*;
import com.nju.edu.erp.model.vo.HumanResource.EmployeeVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@SpringBootTest
public class HumanResourceTest {

    @Autowired
    HumanResourceService humanResourceService;

    /**
     * 测试员工管理的方法是否正常运行
     */
    @Test
    @Transactional
    @Rollback(value = true)
    public void employeeBasicTest() {

        EmployeeVO employeeVO1 = EmployeeVO.builder()
                .name("Akira")
                .sex(Sex.WOMAN)
                .birthday("2022-07-04")
                .phone("12312345678")
                .department(Department.INVENTORY_MANAGER)
                .departmentLevel(DepartmentLevel.JUNIOR)
                .build();

        Integer id1 = humanResourceService.addEmployee(employeeVO1);

        EmployeeVO employeeVO2 = humanResourceService.getEmployee(id1);
        Assertions.assertEquals(employeeVO2.getName(), "Akira");
        Assertions.assertEquals(employeeVO2.getSex(), Sex.WOMAN);
        Assertions.assertEquals(employeeVO2.getBirthday(), "2022-07-04");
        Assertions.assertEquals(employeeVO2.getPhone(), "12312345678");
        Assertions.assertEquals(employeeVO2.getDepartment(), Department.INVENTORY_MANAGER);
        Assertions.assertEquals(employeeVO2.getDepartmentLevel(), DepartmentLevel.JUNIOR);
        Assertions.assertTrue(employeeVO2.getBasicSalary().compareTo(new BigDecimal(3000)) == 0);
        Assertions.assertTrue(employeeVO2.getDepartmentSalary().compareTo(new BigDecimal(3000)) == 0);
        Assertions.assertEquals(employeeVO2.getSalaryComputation(), SalaryComputation.BASIC_DEPART);
        Assertions.assertEquals(employeeVO2.getSalaryDelivery(), SalaryDelivery.MONTHLY);

        employeeVO1.setName("Umi");
        humanResourceService.updateEmployee(employeeVO1);
        employeeVO2 = humanResourceService.getEmployee(id1);
        Assertions.assertEquals(employeeVO2.getName(), "Umi");

        EmployeeVO employeeVO3 = EmployeeVO.builder()
                .name("Maki")
                .sex(Sex.MAN)
                .birthday("2008-12-21")
                .phone("12345675315")
                .department(Department.HR)
                .departmentLevel(DepartmentLevel.PRINCIPLE)
                .build();
        humanResourceService.addEmployee(employeeVO3);
        EmployeeVO employeeVO4 = EmployeeVO.builder()
                .name("Rin")
                .sex(Sex.WOMAN)
                .birthday("1963-04-01")
                .phone("12315397528")
                .department(Department.SALE_STAFF)
                .departmentLevel(DepartmentLevel.SENIOR)
                .build();
        humanResourceService.addEmployee(employeeVO4);
        Assertions.assertTrue(humanResourceService.getAllEmployees().size() >= 3);

        humanResourceService.deleteEmployee(0);

    }

    /**
     * 测试更新失败时是否会抛出异常
     */
    @Test
    @Transactional
    @Rollback(value = true)
    public void employeeExceptionTest1() {

        EmployeeVO employeeVO1 = EmployeeVO.builder()
                .name("Akira")
                .sex(Sex.WOMAN)
                .birthday("2022-07-04")
                .phone("12312345678")
                .department(Department.INVENTORY_MANAGER)
                .departmentLevel(DepartmentLevel.JUNIOR)
                .build();

        Assertions.assertThrows(RuntimeException.class, () -> {
            humanResourceService.updateEmployee(employeeVO1);
        });

        Integer id1 = humanResourceService.addEmployee(employeeVO1);
        employeeVO1.setId(id1);

        employeeVO1.setBirthday("123456");
        Assertions.assertThrows(RuntimeException.class, () -> {
            humanResourceService.updateEmployee(employeeVO1);
        });
        employeeVO1.setBirthday("2022-07-04");

        employeeVO1.setPhone("123456");
        Assertions.assertThrows(RuntimeException.class, () -> {
            humanResourceService.updateEmployee(employeeVO1);
        });
        employeeVO1.setPhone("12312345678");

        employeeVO1.setName(null);
        Assertions.assertThrows(RuntimeException.class, () -> {
            humanResourceService.updateEmployee(employeeVO1);
        });
        employeeVO1.setName("Akira");

        employeeVO1.setBasicSalary(new BigDecimal(2000));
        Assertions.assertThrows(RuntimeException.class, () -> {
            humanResourceService.updateEmployee(employeeVO1);
        });
        employeeVO1.setBasicSalary(new BigDecimal(3000));

        employeeVO1.setDepartmentLevel(DepartmentLevel.PRINCIPLE);
        Assertions.assertThrows(RuntimeException.class, () -> {
            humanResourceService.updateEmployee(employeeVO1);
        });
        employeeVO1.setDepartmentLevel(DepartmentLevel.JUNIOR);

    }

    /**
     * 测试查询失败时是否会抛出异常
     * 测试插入异常数据时是否会抛出异常
     */
    @Test
    @Transactional
    @Rollback(value = true)
    public void employeeExceptionTest2() {

        EmployeeVO employeeVO1 = EmployeeVO.builder()
                .name("Akira")
                .sex(Sex.WOMAN)
                .birthday("2022-07-04")
                .phone("12312345678")
                .department(Department.INVENTORY_MANAGER)
                .departmentLevel(DepartmentLevel.JUNIOR)
                .build();

        Assertions.assertThrows(RuntimeException.class, () -> {
            humanResourceService.getEmployee(20000);
        });

        employeeVO1.setBirthday("201-12-30");
        Assertions.assertThrows(RuntimeException.class, () -> {
            humanResourceService.addEmployee(employeeVO1);
        });
        employeeVO1.setBirthday("2022-07-04");

        employeeVO1.setPhone("1261651");
        Assertions.assertThrows(RuntimeException.class, () -> {
            humanResourceService.addEmployee(employeeVO1);
        });
        employeeVO1.setPhone("12312345678");

        employeeVO1.setDepartmentLevel(DepartmentLevel.PRINCIPLE);
        Assertions.assertThrows(RuntimeException.class, () -> {
            humanResourceService.addEmployee(employeeVO1);
        });

    }
}
