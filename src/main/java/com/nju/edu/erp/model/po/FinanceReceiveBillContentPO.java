package com.nju.edu.erp.model.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FinanceReceiveBillContentPO {
    /**
     * 自增id
     */
    private Integer id;
    /**
     * 收款单id
     */
    private String receiveBillCode;
    /**
     * 银行账户id
     */
    private int bankId;
    /**
     * 转账金额（入账
     */
    private BigDecimal amount;
    /**
     *  备注
     */
    private String remark;
}
