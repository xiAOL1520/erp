package com.nju.edu.erp.service;

import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.enums.sheetState.FinancePayBillState;
import com.nju.edu.erp.enums.sheetState.FinanceReceiveBillState;
import com.nju.edu.erp.model.vo.CustomerVO;
import com.nju.edu.erp.model.vo.UserVO;
import com.nju.edu.erp.model.vo.finance.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class FinancePayBillTest {
    @Autowired
    FinancePayBillService financePayBillService;
    @Test
    public void makebilltest(){
        UserVO userVO = UserVO.builder()
                .name("test1")
                .role(Role.FINANCIAL_STAFF)
                .build();

        List<FinancePayBillContentVO> financePayBillContentVOS=new ArrayList<>();
        financePayBillContentVOS.add(FinancePayBillContentVO.builder()
                .bankVO(bankVO.builder()
                        .id(1)
                        .name("hjb")
                        .remainingSum(BigDecimal.valueOf(114814.00))
                        .build())
                .remark("test1-bank1")
                .amount(BigDecimal.valueOf(300))
                .build());
        financePayBillContentVOS.add(FinancePayBillContentVO.builder()
                .bankVO(bankVO.builder()
                        .id(2)
                        .name("哦哈哟")
                        .remainingSum(BigDecimal.valueOf(1200.00))
                        .build())
                .remark("test1-bank2")
                .amount(BigDecimal.valueOf(200))
                .build());
        FinancePayBillVO test1= FinancePayBillVO.builder()
                .transferlist(financePayBillContentVOS)
                .customerVO(CustomerVO.builder()
                        .id(2)
                        .type("供应商")
                        .level(2)
                        .name("hjb")
                        .phone("114514")
                        .address("nju")
                        .zipcode("114514")
                        .email("114514@nju.edu.cn")
                        .lineOfCredit(BigDecimal.valueOf(1919810))
                        .receivable(BigDecimal.valueOf(114514))
                        .payable(BigDecimal.valueOf(1919810))
                        .operator("thankbro")
                        .build())
                .build();

        financePayBillService.makeFinancePayBill(userVO,test1);
    }
    @Test
    public void approvaltest(){
        financePayBillService.approval("FKD-20220706-00001", FinancePayBillState.SUCCESS);
    }

    @Test
    public void findByStateTest(){
        List<FinancePayBillVO> test1=financePayBillService.getFinancePayBillByState(FinancePayBillState.SUCCESS);
        System.out.println(test1);
    }

    @Test
    public void findByCodeTest(){
        FinancePayBillVO test1=financePayBillService.getFinancePayByCode("FKD-20220706-00001");
        System.out.println(test1);
    }
}
