package com.nju.edu.erp.model.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FinanceSaleDetailPO {
    /**
     * 销售出货/退货
     */
    private String Type;
    /**
     * 时间
     */
    private Date date;
    /**
     * 商品名
     */
    private String productName;
    /**
     * 商品型号
     */
    private String productType;
    /**
     * 数量
     */
    private int quantity;
    /**
     * 单价
     */
    private BigDecimal unitPrice;
    /**
     * 总额
     */
    private BigDecimal totalAmount;
}
