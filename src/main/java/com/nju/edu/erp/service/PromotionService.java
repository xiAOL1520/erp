package com.nju.edu.erp.service;

import com.nju.edu.erp.model.vo.PromotionVO;

import java.util.List;

public interface PromotionService {

    /**
     * 将制定的促销策略存入数据库
     * @param promotionVO promotionVO
     */
    void makePromotion(PromotionVO promotionVO);

    /**
     * 删除一条促销策略
     * @param promotionId promotionId
     */
    void deletePromotion(Integer promotionId);

    /**
     * 获取所有的促销策略
     * @return promotionVOList
     */
    List<PromotionVO> getAllPromotions();


}
