package com.nju.edu.erp.dao;

import com.nju.edu.erp.model.po.salary.YearBonusPO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
@Mapper
public interface YearBonusDao {

    /**
     * 查询某员工本年年终奖
     * @param date 日期，格式为yyyy-00-00
     * @return yearBonus
     */
    BigDecimal getYearBonus(String date, Integer id);

    /**
     * 设定某员工本年年终奖
     * @param date 日期，格式为yyyy-00-00
     * @return yearBonus
     */
    void setYearBonus(String date, Integer id, BigDecimal yearBonus);

    /**
     * 修改某员工本年年终奖
     * @param date 日期，格式为yyyy-00-00
     * @return yearBonus
     */
    void updateYearBonus(String date, Integer id, BigDecimal yearBonus);

    /**
     * 获取所有年终奖信息
     * @return yearBonusPOList
     */
    List<YearBonusPO> getAllYearBonus(String date);
}
