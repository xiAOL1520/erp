package com.nju.edu.erp.model.vo.finance;

import com.nju.edu.erp.enums.sheetState.FinancePayBillState;
import com.nju.edu.erp.enums.sheetState.FinanceReceiveBillState;
import com.nju.edu.erp.model.vo.CustomerVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FinancePayBillVO {
    /**
     * 付款单单据编号 格式FKD-yyyyMMdd-xxxxx
     */
    private String code;
    /**
     * 客户
     */
    private CustomerVO customerVO;
    /**
     * 操作员
     */
    private String operator;
    /**
     * 转账列表
     */
    private List<FinancePayBillContentVO> transferlist;
    /**
     *总额汇总;新建单据时前端传null(在后端计算总金额
     */
    private BigDecimal sum;
    /**
     * 单据状态, 新建单据时前端传null
     */
    private FinancePayBillState state;
}
