package com.nju.edu.erp.web.controller;

import com.nju.edu.erp.auth.Authorized;
import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.model.po.PromotionPO;
import com.nju.edu.erp.model.vo.PromotionVO;
import com.nju.edu.erp.service.PromotionService;
import com.nju.edu.erp.web.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/promotion")
public class PromotionController {

    private final PromotionService promotionService;

    @Autowired
    public PromotionController ( PromotionService promotionService ) {
        this.promotionService = promotionService;
    }

    /**
     * 将制定的促销策略存入数据库
     * @param promotionVO promotionVO
     * @return Response.buildSuccess()
     */
    @Authorized (roles = {Role.GM, Role.ADMIN} )
    @PostMapping ( value = "makePromotion")
    public Response makePromotion(@RequestBody PromotionVO promotionVO) {
        try {
            promotionService.makePromotion(promotionVO);
            return Response.buildSuccess();
        } catch (RuntimeException e) {
            return Response.buildFailed("00001", e);
        }
    }

    /**
     * 删除一条促销策略记录
     * @param promotionId promotionId
     * @return Response.buildSuccess()
     */
    @Authorized (roles = {Role.GM, Role.ADMIN} )
    @GetMapping( value = "deletePromotion")
    public Response deletePromotion(@RequestParam Integer promotionId) {
        try {
            promotionService.deletePromotion(promotionId);
            return Response.buildSuccess();
        } catch (RuntimeException e) {
            return Response.buildFailed("00001", e);
        }
    }

    /**
     * 获取所有的促销策略
     * @return promotionVOList
     */
    @Authorized (roles = {Role.GM, Role.ADMIN} )
    @GetMapping ( value = "getAllPromotions")
    public Response getAllPromotions() {
        try {
            List<PromotionVO> promotionVOList = promotionService.getAllPromotions();
            return Response.buildSuccess(promotionVOList);
        } catch (RuntimeException e) {
            return Response.buildFailed("00001", e);
        }
    }

}
