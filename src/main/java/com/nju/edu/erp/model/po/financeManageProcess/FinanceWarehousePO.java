package com.nju.edu.erp.model.po.financeManageProcess;

import com.nju.edu.erp.enums.sheetState.WarehouseInputSheetState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FinanceWarehousePO {
    /**
     * 单据具体类型 出库/入库
     */
    String type;
    /**
     * 单据编号
     */
    private String id;
    /**
     * 操作员
     */
    private String operator;
    /**
     * 操作时间
     */
    private Date createTime;
    /**
     * 关联的单据
     */
    private String SheetId;
    /**
     * 单据状态
     */
    private String state;
}
