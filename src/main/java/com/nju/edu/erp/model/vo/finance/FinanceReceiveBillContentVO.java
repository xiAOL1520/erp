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
public class FinanceReceiveBillContentVO {
    /**
     * 自增id,新建单据时前端传null
     */
    private Integer id;
    /**
     * 收款单id,新建单据时前端传null
     */
    private String receiveBillCode;
    /**
     * 银行账户
     */
    private bankVO bankVO;
    /**
     * 转账金额（入账
     */
    private BigDecimal amount;
    /**
     *  备注
     */
    private String remark;
}
