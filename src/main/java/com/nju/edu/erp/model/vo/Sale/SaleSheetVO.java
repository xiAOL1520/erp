package com.nju.edu.erp.model.vo.Sale;

import com.nju.edu.erp.enums.sheetState.SaleSheetState;
import com.nju.edu.erp.model.vo.purchase.PurchaseSheetContentVO;
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
public class SaleSheetVO {

    /**
     * 销售单单据编号（格式为：XSD-yyyyMMdd-xxxxx), 新建单据时前端传null
     */
    private String id;
    /**
     * 供应商id
     */
    private Integer supplier;
    /**
     * 客户
     */
    private String salesman;
    /**
     * 操作员
     */
    private String operator;
    /**
     * 备注
     */
    private String remark;
    /**
     * 折让前总额
     */
    private BigDecimal rawTotalAmount;
    /**
     * 单据状态, 新建单据时前端传null
     */
    private SaleSheetState state;
    /**
     * 折让后总额, 新建单据时前端传null
     */
    private BigDecimal finalAmount;
    /**
     * 折扣
     */
    private BigDecimal discount;
    /**
     * 使用代金券总额
     */
    private BigDecimal voucherAmount;
    /**
     * 销售单具体内容
     */
    List<SaleSheetContentVO> saleSheetContent;
}
