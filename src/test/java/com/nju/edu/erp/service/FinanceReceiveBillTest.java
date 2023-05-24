package com.nju.edu.erp.service;

import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.enums.sheetState.FinanceReceiveBillState;
import com.nju.edu.erp.model.vo.CustomerVO;
import com.nju.edu.erp.model.vo.UserVO;
import com.nju.edu.erp.model.vo.finance.FinanceReceiveBillContentVO;
import com.nju.edu.erp.model.vo.finance.FinanceReceiveBillVO;
import com.nju.edu.erp.model.vo.finance.bankVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class FinanceReceiveBillTest {
    @Autowired
    FinanceReceiveBillService financeReceiveBillService;
    @Test
    public void makebilltest(){
        UserVO userVO = UserVO.builder()
                .name("test1")
                .role(Role.FINANCIAL_STAFF)
                .build();

        List<FinanceReceiveBillContentVO> financeReceiveBillContentVOList=new ArrayList<>();
        financeReceiveBillContentVOList.add(FinanceReceiveBillContentVO.builder()
                .bankVO(bankVO.builder()
                        .id(1)
                        .name("account2")
                        .remainingSum(BigDecimal.valueOf(800.00))
                        .build())
                .remark("test1-bank1")
                .amount(BigDecimal.valueOf(300))
                .build());
        financeReceiveBillContentVOList.add(FinanceReceiveBillContentVO.builder()
                .bankVO(bankVO.builder()
                        .id(2)
                        .name("account3")
                        .remainingSum(BigDecimal.valueOf(900.00))
                        .build())
                .remark("test1-bank2")
                .amount(BigDecimal.valueOf(200))
                .build());
        FinanceReceiveBillVO test1= FinanceReceiveBillVO.builder()
                .transferlist(financeReceiveBillContentVOList)
                .customerVO(CustomerVO.builder()
                        .id(3)
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

        financeReceiveBillService.makeFinanceReceiveBill(userVO,test1);
    }

    @Test
    public void approvaltest(){
        financeReceiveBillService.approval("SKD-20220704-00002", FinanceReceiveBillState.SUCCESS);
    }

    @Test
    public void findByStateTest(){
        List<FinanceReceiveBillVO> test1=financeReceiveBillService.getFinanceReceiveBillByState(FinanceReceiveBillState.SUCCESS);
        System.out.println(test1);
    }

    @Test
    public void findByCodeTest(){
        FinanceReceiveBillVO test1=financeReceiveBillService.getFinanceReceiveByCode("SKD-20220704-00001");
        System.out.println(test1);
    }
}
