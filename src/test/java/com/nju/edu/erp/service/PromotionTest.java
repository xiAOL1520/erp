package com.nju.edu.erp.service;

import com.nju.edu.erp.model.vo.PromotionVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@SpringBootTest
public class PromotionTest {

    @Autowired
    PromotionService promotionService;

    /**
     * 测试promotionService的基本功能
     */
    @Test
    @Transactional
    @Rollback(value = true)
    public void promotionBasicTest() {

        PromotionVO promotionVO = PromotionVO.builder()
                .id(00001)
                .customerLevel(1)
                .startDate("2022-07-01")
                .endDate("2022-07-09")
                .discount(new BigDecimal(10).divide(new BigDecimal(100)))
                .voucher(new BigDecimal(60))
                .build();
        promotionService.makePromotion(promotionVO);
        Assertions.assertTrue(promotionService.getAllPromotions().size() > 0);
        promotionService.deletePromotion(00001);

    }

}
