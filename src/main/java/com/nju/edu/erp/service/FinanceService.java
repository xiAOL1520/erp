package com.nju.edu.erp.service;

import com.nju.edu.erp.model.po.FinanceSaleDetailPO;
import com.nju.edu.erp.model.po.financeManageProcess.FinanceFinancialPO;
import com.nju.edu.erp.model.po.financeManageProcess.FinancePurchasePO;
import com.nju.edu.erp.model.po.financeManageProcess.FinanceSalePO;
import com.nju.edu.erp.model.po.financeManageProcess.FinanceWarehousePO;
import com.nju.edu.erp.model.vo.finance.FinanceManageProcessVO;
import com.nju.edu.erp.model.vo.finance.FinanceManageSituationVO;
import com.nju.edu.erp.model.vo.finance.FinanceSaleDetailVO;
import com.nju.edu.erp.model.vo.salary.SalaryBillVO;

import java.util.Date;
import java.util.List;

public interface FinanceService {
    /**
     *  查询销售明细
     * @param financeSaleDetailVO
     * @return
     */
    List<FinanceSaleDetailPO> querySaleDetail(FinanceSaleDetailVO financeSaleDetailVO);

    /**
     * 查看库存类单据经营里程表
     * @return
     */
    List<FinanceWarehousePO> queryWarehouseManage(FinanceManageProcessVO financeManageProcessVO);
    /**
     * 查看销售类单据经营里程表
     * @return
     */
    List<FinanceSalePO> querySaleManage(FinanceManageProcessVO financeManageProcessVO);
    /**
     * 查看进货类单据经营里程表
     * @return
     */
    List<FinancePurchasePO> queryPurchaseManage(FinanceManageProcessVO financeManageProcessVO);
    /**
     * 查看财务类单据经营里程表
     * @return
     */
    List<FinanceFinancialPO> queryFinancialManage(FinanceManageProcessVO financeManageProcessVO);

    /**
     * 查看工资单
     * @param financeManageProcessVO
     * @return
     */
    List<SalaryBillVO> querySalaryManage(FinanceManageProcessVO financeManageProcessVO);
    /**
     * 查看一段时间内的经营收支情况
     * @param beginDateStr
     * @param endDateStr
     * @return
     */
    FinanceManageSituationVO queryFinanceManageSituation(String beginDateStr, String endDateStr);
}
