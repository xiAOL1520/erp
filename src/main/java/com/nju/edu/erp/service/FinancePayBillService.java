package com.nju.edu.erp.service;

import com.nju.edu.erp.enums.sheetState.FinancePayBillState;
import com.nju.edu.erp.enums.sheetState.FinanceReceiveBillState;
import com.nju.edu.erp.model.vo.UserVO;
import com.nju.edu.erp.model.vo.finance.FinancePayBillVO;
import com.nju.edu.erp.model.vo.finance.FinanceReceiveBillVO;

import java.util.List;

public interface FinancePayBillService {
    /**
     * 创建付款单
     * @param userVO
     * @param financePayBillVO
     */
    void makeFinancePayBill(UserVO userVO, FinancePayBillVO financePayBillVO);
    /**
     * 付款单审批
     * @param code
     * @param state
     */
    void approval(String code, FinancePayBillState state);
    /**
     * 根据状态获取付款单(state == null 则获取所有进货单)
     * @param state 付款单状态
     * @return 付款单
     */
    List<FinancePayBillVO> getFinancePayBillByState(FinancePayBillState state);

    /**
     * 根据付款单code搜索收款单单信息
     * @param code 付款单code
     * @return 付款单全部信息
     */
    FinancePayBillVO getFinancePayByCode(String code);
}
