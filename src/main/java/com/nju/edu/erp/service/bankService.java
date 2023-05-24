package com.nju.edu.erp.service;

import com.nju.edu.erp.model.po.bankPO;
import com.nju.edu.erp.model.vo.finance.bankVO;

import java.util.List;

public interface bankService {
    /**
     *
     * 更新账户
     * @param bankVO
     * @return
     */
    void updateOne(bankVO bankVO);

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
     * @param bankVO
     */
    void addOne(bankVO bankVO);

    /**
     * 删除指定账户
     * @param id 账户id
     */
    void deleteOneById(int id);
}
