package com.nju.edu.erp.dao;

import com.nju.edu.erp.model.po.PromotionPO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface PromotionDao {

    /**
     * 存入一条促销策略记录
     * @param promotionPO promotionPO
     */
    void addPromotion(PromotionPO promotionPO);

    /**
     * 删除一条促销策略记录
     * @param id promotionId
     */
    void deletePromotion(Integer id);

    /**
     * 获取所有的促销策略记录
     * @return promotionPOList
     */
    List<PromotionPO> getAllPromotions();

}
