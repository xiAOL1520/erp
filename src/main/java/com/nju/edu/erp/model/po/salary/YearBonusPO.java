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
public class YearBonusPO {

    /**
     * 年终奖所属的日期，格式为yyyy-00-00
     */
    private String date;
    /**
     * 员工id
     */
    private Integer id;
    /**
     * 年终奖
     */
    private BigDecimal yearBonus;

}
