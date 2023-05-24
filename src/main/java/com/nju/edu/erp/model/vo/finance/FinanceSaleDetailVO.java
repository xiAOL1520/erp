package com.nju.edu.erp.model.vo.finance;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FinanceSaleDetailVO {
    /**
     * 时间区间
     */
    private String beginDateStr;
    private String endDateStr;
    /**
     * 产品名
     */
    private String productName;
    /**
     * 客户id
     */
    private Integer customerId;
    /**
     * 业务员
     */
    private String salesman;
}
