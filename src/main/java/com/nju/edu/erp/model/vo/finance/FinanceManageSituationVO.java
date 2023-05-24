package com.nju.edu.erp.model.vo.finance;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FinanceManageSituationVO {
    /**
     * 折让前总收入
     */
    private BigDecimal rawTotalInput;
    /**
     * 折让总额
     */
    private BigDecimal discountAmount;
    /**
     * 折让后实际总收入
     */
    private BigDecimal realTotalInput;
    /**
     * 总支出
     */
    private BigDecimal totalOutput;
    /**
     *  总利润
     */
    private BigDecimal totalProfit;
}
