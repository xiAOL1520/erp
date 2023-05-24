package com.nju.edu.erp.dao;

import com.nju.edu.erp.model.po.FinanceSaleDetailPO;
import com.nju.edu.erp.model.po.financeManageProcess.FinanceFinancialPO;
import com.nju.edu.erp.model.po.financeManageProcess.FinancePurchasePO;
import com.nju.edu.erp.model.po.financeManageProcess.FinanceSalePO;
import com.nju.edu.erp.model.po.financeManageProcess.FinanceWarehousePO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
@Mapper
public interface FinanceDao {
    /**
     * 查询销售明细
     * @param beginTime
     * @param endTime
     * @param productName
     * @param customerId
     * @param salesman
     * @return
     */
    List<FinanceSaleDetailPO> querySaleDetail(Date beginTime,Date endTime,String productName,int customerId,String salesman);

    /**
     * 查看库存类单据经营里程表
     * @param beginTime
     * @param endTime
     * @param operator
     * @return
     */
    List<FinanceWarehousePO> queryWarehouseManage(Date beginTime,Date endTime,String operator);

    /**
     * 查看销售类经营历程表
     * @param beginTime
     * @param endTime
     * @param customerId
     * @param salesman
     * @return
     */
    List<FinanceSalePO> querySaleManage(Date beginTime,Date endTime,int customerId,String salesman);

    /**
     * 查看销售类经营历程表
     * @param beginTime
     * @param endTime
     * @param customerId
     * @param operator
     * @return
     */
    List<FinancePurchasePO> queryPurchaseManage(Date beginTime, Date endTime, int customerId, String operator);
    /**
     * 查看销售类经营历程表
     * @param beginTime
     * @param endTime
     * @param customerId
     * @param operator
     * @return
     */
    List<FinanceFinancialPO> queryFinancialManage(Date beginTime, Date endTime, int customerId, String operator);
}
