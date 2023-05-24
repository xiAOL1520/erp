package com.nju.edu.erp.model.po.financeManageProcess;

import com.nju.edu.erp.enums.sheetState.FinancePayBillState;
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
public class FinanceFinancialPO {
    /**
     * 单据类型
     */
    private String type;
    /**
     * 单据编号
     */
    private String code;
    /**
     * 客户id
     */
    private int customerId;
    /**
     * 操作员
     */
    private String operator;
    /**
     *总额汇总
     */
    private BigDecimal sum;
    /**
     * 单据状态
     */
    private String state;
    /**
     * 创建时间
     */
    private Date createTime;
}
