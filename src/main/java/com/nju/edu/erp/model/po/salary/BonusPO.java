package com.nju.edu.erp.model.po.salary;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BonusPO {

    /**
     * 工作日期，格式为yyyy-MM-00
     */
    private String date;
    /**
     * 员工id
     * 员工应为销售人员
     */
    private Integer id;
    /**
     * 当月奖金/绩效工资
     */
    private BigDecimal bonus;
}
