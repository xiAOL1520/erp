package com.nju.edu.erp.service;

import com.nju.edu.erp.dao.FinanceDao;
import com.nju.edu.erp.enums.SheetType;
import com.nju.edu.erp.model.po.FinanceSaleDetailPO;
import com.nju.edu.erp.model.po.financeManageProcess.FinanceFinancialPO;
import com.nju.edu.erp.model.po.financeManageProcess.FinancePurchasePO;
import com.nju.edu.erp.model.po.financeManageProcess.FinanceSalePO;
import com.nju.edu.erp.model.po.financeManageProcess.FinanceWarehousePO;
import com.nju.edu.erp.model.vo.finance.FinanceManageProcessVO;
import com.nju.edu.erp.model.vo.finance.FinanceManageSituationVO;
import com.nju.edu.erp.model.vo.finance.FinanceSaleDetailVO;
import com.nju.edu.erp.web.Response;
import com.nju.edu.erp.web.controller.FinanceController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class FinanceTest {
    @Autowired
    FinanceService financeService;
    @Autowired
    FinanceDao financeDao;
    @Autowired
    FinanceController financeController;

    @Test
    public void querySaleDetailTest1(){//无筛选条件
        FinanceSaleDetailVO test= FinanceSaleDetailVO.builder().build();
        List<FinanceSaleDetailPO> testRes=financeService.querySaleDetail(test);
        System.out.println("TestResult: "+testRes);
    }

    @Test
    public void querySaleDetailTest2(){//时间
        FinanceSaleDetailVO test= FinanceSaleDetailVO.builder()
                .beginDateStr("2022-5-25")
                .endDateStr("2022-5-26")
                .build();
        List<FinanceSaleDetailPO> testRes=financeService.querySaleDetail(test);
        System.out.println("TestResult: "+testRes);
    }

    @Test
    public void queryManageProcessTest1(){
        FinanceManageProcessVO financeManageProcessVO= FinanceManageProcessVO.builder()
                .sheetType(SheetType.SALARY)
                .build();
        Response test=financeController.queryManagementProcess(financeManageProcessVO);
        System.out.println("TestResult: "+test);
    }

    @Test
    public void queryManageProcessTest2(){
        FinanceManageProcessVO financeManageProcessVO= FinanceManageProcessVO.builder()
                .customerId(2)
                .build();
        List<FinanceSalePO> test=financeService.querySaleManage(financeManageProcessVO);
        System.out.println("TestResult: "+test);
    }

    @Test
    public void queryManageProcessTest3(){
        FinanceManageProcessVO financeManageProcessVO= FinanceManageProcessVO.builder()
                .sheetType(SheetType.SALE)
                .customerId(1)
                .build();
        Response test=financeController.queryManagementProcess(financeManageProcessVO);
        System.out.println("TestResult: "+test);
    }

    @Test
    public void queryManageProcessTest4(){
        FinanceManageProcessVO financeManageProcessVO= FinanceManageProcessVO.builder().build();
        List<FinanceFinancialPO> test=financeService.queryFinancialManage(financeManageProcessVO);
        System.out.println("TestResult: "+test);
    }

    @Test
    public void queryManageSituationTest5(){
        String beginDateStr="2022-03-26";
        String endDateStr="2022-07-30";
        FinanceManageSituationVO test=financeService.queryFinanceManageSituation(beginDateStr,endDateStr);
        System.out.println("TestResult: "+test);
    }
}
