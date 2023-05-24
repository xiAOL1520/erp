package com.nju.edu.erp.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
@Mapper
public interface BonusDao {

    /**
     * 查询销售人员在指定月份的提成
     * @return bonus 提成/绩效工资
     */
    BigDecimal getBonus(Integer id, String date);

    /**
     * 设置销售人员在指定月份的提成
     * @param id 员工id
     * @param date 工作日期
     * @param bonus 提成
     */
    void setBonus(Integer id, String date, BigDecimal bonus);

    /**
     * 更改销售人员在指定月份的提成
     * @param id 员工id
     * @param date 工作日期
     * @param bonus 提成
     */
    void updateBonus(Integer id, String date, BigDecimal bonus);
}
