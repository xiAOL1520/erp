package com.nju.edu.erp.service.Impl;

import com.nju.edu.erp.dao.PromotionDao;
import com.nju.edu.erp.model.po.PromotionPO;
import com.nju.edu.erp.model.vo.PromotionVO;
import com.nju.edu.erp.service.PromotionService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class PromotionServiceImpl implements PromotionService {

    private final PromotionDao promotionDao;

    @Autowired
    public PromotionServiceImpl (PromotionDao promotionDao) {
        this.promotionDao = promotionDao;
    }

    @Override
    @Transactional
    public void makePromotion(PromotionVO promotionVO) {
        PromotionPO promotionPO = new PromotionPO();
        BeanUtils.copyProperties(promotionVO, promotionPO);
        System.out.println(promotionPO);
        promotionDao.addPromotion(promotionPO);
    }

    @Override
    @Transactional
    public void deletePromotion(Integer promotionId) {
        promotionDao.deletePromotion(promotionId);
    }

    @Override
    @Transactional
    public List<PromotionVO> getAllPromotions() {
        List<PromotionPO> promotionPOList = promotionDao.getAllPromotions();
        List<PromotionVO> promotionVOList = new ArrayList<>();
        for(PromotionPO promotionPO : promotionPOList) {
            PromotionVO promotionVO = new PromotionVO();
            BeanUtils.copyProperties(promotionPO, promotionVO);
            promotionVOList.add(promotionVO);
        }
        return promotionVOList;
    }

}
