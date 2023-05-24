package com.nju.edu.erp.service;

import com.nju.edu.erp.enums.sheetState.FinanceReceiveBillState;
import com.nju.edu.erp.enums.sheetState.PurchaseSheetState;
import com.nju.edu.erp.model.vo.UserVO;
import com.nju.edu.erp.model.vo.finance.FinanceReceiveBillVO;
import com.nju.edu.erp.model.vo.purchase.PurchaseSheetVO;

import java.util.List;

public interface FinanceReceiveBillService {
    /**
     * 创建收款单
     * @param userVO
     * @param financeReceiveBillVO
     */
    void makeFinanceReceiveBill(UserVO userVO, FinanceReceiveBillVO financeReceiveBillVO);

    /**
     * 收款单审批
     * @param code
     * @param state
     */
    void approval(String code, FinanceReceiveBillState state);

    /**
     * 根据状态获取收款单(state == null 则获取所有进货单)
     * @param state 收款单状态
     * @return 收款单
     */
    List<FinanceReceiveBillVO> getFinanceReceiveBillByState(FinanceReceiveBillState state);

    /**
     * 根据收款单code搜索收款单单信息
     * @param code 收款单code
     * @return 收款单全部信息
     */
    FinanceReceiveBillVO getFinanceReceiveByCode(String code);
}
