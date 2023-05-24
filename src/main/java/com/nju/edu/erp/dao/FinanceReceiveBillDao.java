package com.nju.edu.erp.dao;

import com.nju.edu.erp.enums.sheetState.FinanceReceiveBillState;
import com.nju.edu.erp.enums.sheetState.PurchaseSheetState;
import com.nju.edu.erp.model.po.FinanceReceiveBillContentPO;
import com.nju.edu.erp.model.po.FinanceReceiveBillPO;
import com.nju.edu.erp.model.po.PurchaseSheetContentPO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface FinanceReceiveBillDao {
    /**
     * 获取最近一条收款单
     * @return
     */
    FinanceReceiveBillPO getLatest();

    /**
     * 存入一条收款单记录
     * @param financeReceiveBillPO
     * @return 影响的行数
     */
    int save(FinanceReceiveBillPO financeReceiveBillPO);

    /**
     * 保存收款的转账列表
     * @param financeReceiveBillContentPOList
     */
    void saveTransferlist(List<FinanceReceiveBillContentPO> financeReceiveBillContentPOList);

    /**
     * 更新收款单状态
     * @param code
     * @param state
     * @return
     */
    int updateState(String code, FinanceReceiveBillState state);
    int updateStateV2(String code,FinanceReceiveBillState prevState, FinanceReceiveBillState state);

    /**
     * 根据单据编号查询
     * @param code
     * @return
     */
    FinanceReceiveBillPO findOneByCode(String code);

    /**
     * 根据编号获取单据转账列表
     * @param receiveBillCode
     * @return
     */
    List<FinanceReceiveBillContentPO> findContentByCode(String receiveBillCode);

    /**
     * 返回所有收款单
     * @return
     */
    List<FinanceReceiveBillPO> findAll();

    /**
     * 根据状态查询多个单据
     * @param state
     * @return
     */
    List<FinanceReceiveBillPO> findAllByState(FinanceReceiveBillState state);
}
