package com.nju.edu.erp.model.vo.finance;

import com.nju.edu.erp.enums.SheetType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FinanceManageProcessVO {
    /**
     * 查询单据类型
     */
    private SheetType sheetType;
    /**
     * 时间区间
     */
    private String beginDateStr;
    private String endDateStr;
    /**
     * 客户id/员工id
     */
    private Integer customerId;
    /**
     * 业务员/操作员
     */
    private String salesman;
}
