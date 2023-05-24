package com.nju.edu.erp.dao;

import com.nju.edu.erp.enums.sheetState.FinancePayBillState;
import com.nju.edu.erp.enums.sheetState.FinanceReceiveBillState;
import com.nju.edu.erp.model.po.FinancePayBillContentPO;
import com.nju.edu.erp.model.po.FinancePayBillPO;
import com.nju.edu.erp.model.po.FinanceReceiveBillContentPO;
import com.nju.edu.erp.model.po.FinanceReceiveBillPO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface FinancePayBillDao {
    /**
     * 获取最近一条付款单
     * @return
     */
    FinancePayBillPO getLatest();

    /**
     * 存入一条付款单记录
     * @param financePayBillPO
     * @return 影响的行数
     */
    int save(FinancePayBillPO financePayBillPO);

    /**
     * 保存付款的转账列表
     * @param financeReceiveBillContentPOList
     */
    void saveTransferlist(List<FinancePayBillContentPO> financeReceiveBillContentPOList);
    /**
     * 更新付款单状态
     * @param code
     * @param state
     * @return
     */
    int updateState(String code, FinancePayBillState state);
    int updateStateV2(String code,FinancePayBillState prevState, FinancePayBillState state);

    /**
     * 根据单据编号查询
     * @param code
     * @return
     */
    FinancePayBillPO findOneByCode(String code);

    /**
     * 根据编号获取单据转账列表
     * @param payBillCode
     * @return
     */
    List<FinancePayBillContentPO> findContentByCode(String payBillCode);

    /**
     * 返回所有付款单
     * @return
     */
    List<FinancePayBillPO> findAll();

    /**
     * 根据状态查询多个单据
     * @param state
     * @return
     */
    List<FinancePayBillPO> findAllByState(FinancePayBillState state);
}
