package com.nju.edu.erp.model.po;

import com.nju.edu.erp.enums.sheetState.FinanceReceiveBillState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FinanceReceiveBillPO {
    /**
     * 收款单单据编号 格式SKD-yyyyMMdd-xxxxx
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
    private FinanceReceiveBillState state;
    /**
     * 创建时间
     */
    private Date createTime;
}
