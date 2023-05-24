package com.nju.edu.erp.dao;

import com.nju.edu.erp.model.po.bankPO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface bankDao {
    /**
     *
     * 更新账户
     * @param bankPO
     * @return
     */
    int updateOne(bankPO bankPO);

    /**
     *  通过id查询单个账户
     * @param id
     * @return
     */
    bankPO findOneById(int id);

    /**
     * 通过名称查询单个账户
     * @param name
     * @return
     */
    List<bankPO> findByName(String name);

    /**
     *  查询所有账户
     * @return
     */
    List<bankPO> findAll();

    /**
     * 添加一个账户
     * @param toSave
     */
    void addOne(bankPO toSave);

    /**
     * 删除指定账户
     * @param id 账户id
     */
    void deleteOneById(int id);

    /**
     * 删除后id自减
     * @param id
     */
    void updateAfterDelete(int id);
    void updateAfter();
}
