package com.nju.edu.erp.service.Impl;

import com.nju.edu.erp.dao.*;
import com.nju.edu.erp.enums.employee.Department;
import com.nju.edu.erp.enums.employee.SalaryComputation;
import com.nju.edu.erp.enums.employee.SalaryDelivery;
import com.nju.edu.erp.enums.sheetState.SalaryBillState;
import com.nju.edu.erp.model.po.EmployeePO;
import com.nju.edu.erp.model.po.salary.SalaryBillPO;
import com.nju.edu.erp.model.po.salary.SalaryRulePO;
import com.nju.edu.erp.model.po.salary.YearBonusPO;
import com.nju.edu.erp.model.vo.salary.SalaryBillVO;
import com.nju.edu.erp.model.vo.salary.YearBonusVO;
import com.nju.edu.erp.service.SalaryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class SalaryServiceImpl implements SalaryService {

    private final SalaryBillDao salaryBillDao;
    private final EmployeeDao employeeDao;
    private final AttendanceDao attendanceDao;
    private final YearBonusDao yearBonusDao;
    private final BonusDao bonusDao;
    private final SalaryRuleDao salaryRuleDao;


    @Autowired
    public SalaryServiceImpl(SalaryBillDao salaryBillDao, EmployeeDao employeeDao, AttendanceDao attendanceDao, YearBonusDao yearBonusDao, BonusDao bonusDao, SalaryRuleDao salaryRuleDao) {
        this.salaryBillDao = salaryBillDao;
        this.employeeDao = employeeDao;
        this.attendanceDao = attendanceDao;
        this.yearBonusDao = yearBonusDao;
        this.bonusDao = bonusDao;
        this.salaryRuleDao = salaryRuleDao;
    }

    @Override
    @Transactional
    public SalaryBillVO makeSalaryBill (Integer employeeId, String date) {

        if(!Pattern.matches("\\d\\d\\d\\d-\\d\\d-\\d\\d", date)) {
            throw new RuntimeException("无效日期：格式应为yyyy-MM-dd！");
        }
        if(employeeDao.getEmployeeById(employeeId) == null) {
            throw new RuntimeException("查找员工失败：员工不存在！");
        }
        date = transDate(employeeId, date);

        SalaryBillPO salaryBillPO = salaryBillDao.getSalaryBill(employeeId, date);
        if(salaryBillPO == null) {
            salaryBillPO = new SalaryBillPO();
            salaryBillPO.setDate(date);
            salaryBillPO.setId(employeeId);
            salaryBillPO.setState(SalaryBillState.PENDING_LEVEL_1);

            salaryBillPO.setAttendance(getAttendance(attendanceDao.getAttendance(employeeId), date));
            salaryBillDao.addSalaryBill(salaryBillPO);
        }

        SalaryBillVO salaryBillVO = new SalaryBillVO();
        BeanUtils.copyProperties(salaryBillPO, salaryBillVO);
        EmployeePO employeePO = employeeDao.getEmployeeById(employeeId);
        BeanUtils.copyProperties(employeePO, salaryBillVO);

        BigDecimal bonus = bonusDao.getBonus(employeeId, date);
        if(bonus == null) { bonus = new BigDecimal(0); }
        salaryBillVO.setBonus(bonus);

        BigDecimal payable = computePayable(employeePO, bonus, date);
        salaryBillVO.setPayable(payable);
        BigDecimal tax = tax(payable);
        salaryBillVO.setTax(tax);
        salaryBillVO.setSalary(payable.subtract(tax));

        return salaryBillVO;
    }

    @Override
    @Transactional
    public List<SalaryBillVO> makeMonthSalaryBills(String date) {

        List<EmployeePO> employeePOList = employeeDao.getAllEmployees();
        List<SalaryBillVO> salaryBillVOList = new ArrayList<>();
        for(EmployeePO employeePO : employeePOList) {
            if(employeePO.getSalaryDelivery() != SalaryDelivery.MONTHLY)
                continue;

            Integer id = employeePO.getId();
            SalaryBillVO salaryBillVO = makeSalaryBill(id, date);
            salaryBillVOList.add(salaryBillVO);
        }

        return salaryBillVOList;
    }

    @Override
    @Transactional
    public List<SalaryBillVO> findSalaryBills(List<Integer> employeeIdList, List<String> datePair) {
        if(datePair.size() == 1) { throw new RuntimeException("查找工资单失败：请给定正确时间区间!"); }
        if(datePair.size() >= 2) {
            if(!Pattern.matches("\\d\\d\\d\\d-\\d\\d-\\d\\d", datePair.get(0))) {
                throw new RuntimeException("无效日期：格式应为yyyy-MM-dd！");
            }
            if(!Pattern.matches("\\d\\d\\d\\d-\\d\\d-\\d\\d", datePair.get(1))) {
                throw new RuntimeException("无效日期：格式应为yyyy-MM-dd！");
            }
        }

        List<SalaryBillPO> salaryBillPOList = salaryBillDao.getAllSalaryBills();

        if(employeeIdList.size() != 0) {
            int size = salaryBillPOList.size();
            for(int i=0; i < size; i++)
                if(!employeeIdList.contains(salaryBillPOList.get(i).getId())) {
                    salaryBillPOList.remove(i);
                    i--;
                    size--;
                }
        }

        if(datePair.size() != 0) {
            int dateLess = dateLess(datePair.get(0), datePair.get(1));
            String date1 = ( dateLess < 0 ) ? datePair.get(0) : datePair.get(1);
            String date2 = ( dateLess < 0 ) ? datePair.get(1) : datePair.get(0);

            int size = salaryBillPOList.size();
            for(int i=0; i < size; i++)
                if(!dateIn(date1, date2, salaryBillPOList.get(i).getDate())) {
                    salaryBillPOList.remove(i);
                    i--;
                    size--;
                }
        }

        List<SalaryBillVO> salaryBillVOList = new ArrayList<>();
        for(SalaryBillPO salaryBillPO : salaryBillPOList)
            salaryBillVOList.add(makeSalaryBill(salaryBillPO.getId(), salaryBillPO.getDate()));

        return salaryBillVOList;
    }

    @Override
    @Transactional
    public void HRAudit(Integer employeeId, String date, SalaryBillState salaryBillState) {
        date = transDate(employeeId, date);
        SalaryBillPO salaryBillPO = salaryBillDao.getSalaryBill(employeeId, date);
        if(salaryBillPO.getState() == SalaryBillState.SUCCESS)
            throw new RuntimeException("已审批完成，不可重复审批！");
        if(salaryBillState == SalaryBillState.SUCCESS || salaryBillState == SalaryBillState.PENDING_LEVEL_1)
            throw new RuntimeException("您只可审批失败或至待二级审批！");
        salaryBillPO.setState(salaryBillState);
        salaryBillDao.updateSalaryBill(salaryBillPO);
    }

    @Override
    @Transactional
    public void GMAudit(Integer employeeId, String date, SalaryBillState salaryBillState) {
        date = transDate(employeeId, date);
        SalaryBillPO salaryBillPO = salaryBillDao.getSalaryBill(employeeId, date);
        if(salaryBillPO.getState() == SalaryBillState.FAILURE
                || salaryBillPO.getState() == SalaryBillState.PENDING_LEVEL_1)
            throw new RuntimeException("请等待人力资源人员审批！");
        if(salaryBillState == SalaryBillState.PENDING_LEVEL_1 || salaryBillState == SalaryBillState.PENDING_LEVEL_2)
            throw new RuntimeException("您只可审批失败或审批成功！");
        salaryBillPO.setState(salaryBillState);
        salaryBillDao.updateSalaryBill(salaryBillPO);
    }

    @Override
    @Transactional
    public void addBonus(Integer employeeId, String date, BigDecimal bonus) {

        EmployeePO employeePO = employeeDao.getEmployeeById(employeeId);
        Department depart = employeePO.getDepartment();
        if(depart != Department.SALE_STAFF && depart != Department.SALE_MANAGER)
            throw new RuntimeException("增加提成失败：只有销售人员有提成！");
        date = transDate(employeeId, date);

        BigDecimal oldBonus = bonusDao.getBonus(employeeId, date);
        if(oldBonus == null) {
            oldBonus = new BigDecimal(0);
            oldBonus = oldBonus.add(bonus);
            bonusDao.setBonus(employeeId, date, oldBonus);
        } else {
            oldBonus = oldBonus.add(bonus);
            bonusDao.updateBonus(employeeId, date, oldBonus);
        }

    }

    @Override
    @Transactional
    public void setYearBonus(Integer employeeId, String date, BigDecimal yearBonus) {

        date = date.split("-")[0] + "-00-00";

        if(yearBonusDao.getYearBonus(date, employeeId) == null)
            yearBonusDao.setYearBonus(date, employeeId, yearBonus);
        else
            yearBonusDao.updateYearBonus(date, employeeId, yearBonus);

    }

    @Override
    @Transactional
    public List<YearBonusVO> getYearBonus(String date) {
        date = date.split("-")[0] + "-00-00";
        List<YearBonusVO> yearBonusVOList = new ArrayList<>();
        List<YearBonusPO> yearBonusPOList = yearBonusDao.getAllYearBonus(date);
        for(YearBonusPO yearBonusPO : yearBonusPOList) {
            YearBonusVO yearBonusVO = new YearBonusVO();
            BeanUtils.copyProperties(yearBonusPO, yearBonusVO);
            yearBonusVOList.add(yearBonusVO);
        }
        return yearBonusVOList;
    }

    @Override
    @Transactional
    public void updateSalaryRule(SalaryRulePO salaryRulePO) {
        salaryRuleDao.updateSalaryRule(salaryRulePO);
    }

    @Override
    @Transactional
    public List<SalaryRulePO> getAllSalaryRules() {
        return salaryRuleDao.getAllSalaryRules();
    }

    /**
     * 把日期转换为正确格式
     * dd为00表示按月发放。MM为00，dd为00表示按年发放
     * @param id 员工id
     * @param date 原日期，格式为yyyy-MM-dd
     * @return date 正确格式的日期
     */
    private String transDate (Integer id, String date) {

        EmployeePO employeePO = employeeDao.getEmployeeById(id);
        SalaryDelivery sd = employeePO.getSalaryDelivery();

        if(sd == SalaryDelivery.MONTHLY)
            date = date.split("-")[0] + "-" + date.split("-")[1] + "-00";
        if(sd == SalaryDelivery.YEARLY)
            date = date.split("-")[0] + "-00-00";

        return date;
    }

    /**
     * 计算员工的应付工资
     * @param employeePO 员工信息
     * @param bonus 销售人员提成
     * @param date 工作日期
     * @return payable 应付工资
     */
    private BigDecimal computePayable(EmployeePO employeePO, BigDecimal bonus, String date) {

        BigDecimal basic = employeePO.getBasicSalary();
        BigDecimal depart = employeePO.getDepartmentSalary();
        SalaryComputation sc = employeePO.getSalaryComputation();
        SalaryDelivery sd = employeePO.getSalaryDelivery();

        BigDecimal payable = new BigDecimal(0);

        if(sc == SalaryComputation.BASIC)
            payable = basic;
        else if(sc == SalaryComputation.BASIC_DEPART)
            payable = basic.add(depart);
        else if(sc == SalaryComputation.BASIC_BONES)
            payable = basic.add(bonus);
        else if(sc == SalaryComputation.BASIC_DEPART_BONES)
            payable = basic.add(depart).add(bonus);

        if(sd == SalaryDelivery.DAILY)
            payable = payable.divide(new BigDecimal(30));
        else if(sd == SalaryDelivery.YEARLY)
            payable = payable.multiply(new BigDecimal(12));

        if(!date.split("-")[1].equals("00") && date.split("-")[2].equals("00")) {
            int attendance = getAttendance(attendanceDao.getAttendance(employeePO.getId()), date);
            int absence = workDays(date) - attendance;
            payable = payable.subtract(new BigDecimal(100).multiply(new BigDecimal(absence)));
        }

        if(Pattern.matches("\\d\\d\\d\\d-12-\\d\\d", date)) {
            String year = date.split("-")[0] + "-00-00";
            BigDecimal yearBonus = yearBonusDao.getYearBonus(year, employeePO.getId());
            if(yearBonus == null) { throw new RuntimeException("获取年终奖失败：未设置该员工年终奖！"); }
            payable = payable.add(yearBonus);
        }

        return payable;
    }

    /**
     * 计算员工的扣税
     * 扣税仅基于应付工资计算
     * 扣税制度并非实际制度
     * @param payable 员工的应付工资
     * @return tax 扣税
     */
    private BigDecimal tax(BigDecimal payable) {

        BigDecimal[] stairs = {
                new BigDecimal(0),
                new BigDecimal(4000),
                new BigDecimal(8000),
                new BigDecimal(20000),
                new BigDecimal(60000),
                new BigDecimal(-1)
        };

        BigDecimal[] rates = {
                new BigDecimal(3).divide(new BigDecimal(100)),
                new BigDecimal(10).divide(new BigDecimal(100)),
                new BigDecimal(15).divide(new BigDecimal(100)),
                new BigDecimal(25).divide(new BigDecimal(100)),
                new BigDecimal(50).divide(new BigDecimal(100))
        };

        BigDecimal[] quick = {
                new BigDecimal(0),
                new BigDecimal(120),
                new BigDecimal(520),
                new BigDecimal(2320),
                new BigDecimal(12320)
        };

        BigDecimal tax = new BigDecimal(0);

        for(int i=0; i < 5; i++)
            if(payable.compareTo(stairs[i+1]) < 0 || stairs[i+1].equals(new BigDecimal(-1))) {
                tax = quick[i].add(payable.subtract(stairs[i]).multiply(rates[i]));
                break;
            }


        return tax;
    }

    /**
     * 判断date1是否早于date2。早于返回-1，等于返回0，晚于返回1
     * @param date1 date1
     * @param date2 date2
     * @return dateLess
     */
    private int dateLess(String date1, String date2) {

        String[] s1 = date1.split("-");
        int d1 = Integer.parseInt(s1[0] + s1[1] + s1[2]);
        String[] s2 = date2.split("-");
        int d2 = Integer.parseInt(s2[0] + s2[1] + s2[2]);

        if(d1 < d2)
            return -1;
        else if(d1 > d2)
            return 1;
        else
            return 0;

    }

    /**
     * 判断给定日期是否在给定日期区间中
     * @param date1 date1
     * @param date2 date2
     * @param date date
     * @return dateIn
     */
    private boolean dateIn(String date1, String date2, String date) {

        String[] s1 = date1.split("-");
        String[] s2 = date2.split("-");
        String[] s = date.split("-");

        if(s[1].equals("00"))
            if(Integer.parseInt(s1[0]) > Integer.parseInt(s[0])
                    || Integer.parseInt(s2[0]) < Integer.parseInt(s[0]))
                return false;
            else
                return true;

        if(s[2].equals("00"))
            if(Integer.parseInt(s1[0]) > Integer.parseInt(s[0])
                    || Integer.parseInt(s2[0]) < Integer.parseInt(s[0]))
                return false;
            else if(Integer.parseInt(s1[1]) > Integer.parseInt(s[1])
                        || Integer.parseInt(s2[1]) < Integer.parseInt(s[1]))
                return false;
            else
                return true;

        if(dateLess(date1, date) == 1 || dateLess(date2, date) == -1)
            return false;
        else
            return true;

    }

    /**
     * 计算某个月的工作日天数
     * @param date 某月，格式为yyyy-MM-00
     * @return 当月工作日天数
     */
    private Integer workDays(String date) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, Integer.parseInt(date.split("-")[0]));
        calendar.set(Calendar.MONTH, Integer.parseInt(date.split("-")[1]) - 1);
        calendar.set(Calendar.DATE, 1);

        int weekDays = 0;
        for(int i=1; i <= calendar.getActualMaximum(Calendar.DATE); i++) {
            date = date.split("-")[0] + "-" + date.split("-")[1] + "-";
            date += (i < 10) ? "0" + i : i;

            calendar.set(Calendar.DATE, i);
            if(calendar.get(Calendar.DAY_OF_WEEK) == 1 || calendar.get(Calendar.DAY_OF_WEEK) == 7)
                continue;
            weekDays++;
        }

        return weekDays;

    }

    private int getAttendance(List<String> dates, String date) {

        if(date.split("-")[1] == "00") { return 0; }

        int attendance = 0;
        for(int i=0; i < dates.size(); i++)
            if(dates.get(i).split("-")[0] == date.split("-")[0]
                    && dates.get(i).split("-")[1] == date.split("-")[1])
                attendance++;

        return attendance;

    }

}
