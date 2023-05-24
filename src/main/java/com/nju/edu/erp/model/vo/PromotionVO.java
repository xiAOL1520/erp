package com.nju.edu.erp.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PromotionVO {

    /**
     * id
     */
    private Integer id;
    /**
     * 客户级别
     */
    private Integer customerLevel;
    /**
     * 促销金额范围
     */
    private BigDecimal minPiece;
    private BigDecimal maxPiece;
    /**
     * 时间间隔
     * 从开始时间零点开始，至结束时间24点结束
     * 格式为yyyy-MM-dd
     */
    private String startDate;
    private String endDate;
    /**
     * 折扣
     */
    private BigDecimal discount;
    /**
     * 代金券
     */
    private BigDecimal voucher;

}
