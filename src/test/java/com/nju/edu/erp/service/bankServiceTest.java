package com.nju.edu.erp.service;

import com.nju.edu.erp.model.po.bankPO;
import com.nju.edu.erp.model.vo.finance.bankVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

@SpringBootTest
public class bankServiceTest {
    @Autowired
    bankService bankService;

    @Test
    public void addtest(){
        bankVO test1=bankVO.builder()
                .id(1)
                .name("account2")
                .remainingSum(BigDecimal.valueOf(800.00))
                .build();


        bankService.addOne(test1);
    }

    @Test
    public void updateTest(){
        bankVO test1=bankVO.builder()
                .id(2)
                .name("哦哈哟")
                .remainingSum(BigDecimal.valueOf(1000))
                .build();

        bankService.updateOne(test1);
    }

    @Test
    public void findTest(){
        bankPO test1=bankService.findOneById(1);
        List<bankPO> test2=bankService.findByName("hj");
        List<bankPO> test3=bankService.findAll();
        System.out.println("result:\n"+test1+"\n"+test2+"\n"+test3);
    }
    @Test
    public void deleteTest(){
        bankService.deleteOneById(1);
    }

    @Test
    public void clear(){
        for(int i=1;i<4;i++){
            bankService.deleteOneById(i);
        }
    }

}
