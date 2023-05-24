package com.nju.edu.erp.service.Impl;

import com.nju.edu.erp.dao.FinanceDao;
import com.nju.edu.erp.model.po.FinanceSaleDetailPO;
import com.nju.edu.erp.model.po.financeManageProcess.FinanceFinancialPO;
import com.nju.edu.erp.model.po.financeManageProcess.FinancePurchasePO;
import com.nju.edu.erp.model.po.financeManageProcess.FinanceSalePO;
import com.nju.edu.erp.model.po.financeManageProcess.FinanceWarehousePO;
import com.nju.edu.erp.model.vo.finance.FinanceManageProcessVO;
import com.nju.edu.erp.model.vo.finance.FinanceManageSituationVO;
import com.nju.edu.erp.model.vo.finance.FinanceSaleDetailVO;
import com.nju.edu.erp.model.vo.salary.SalaryBillVO;
import com.nju.edu.erp.service.FinanceService;
import com.nju.edu.erp.service.SalaryService;
import com.zaxxer.hikari.util.FastList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Service
public class FinanceServiceImpl implements FinanceService {
    @Autowired
    FinanceDao financeDao;
    @Autowired
    SalaryService salaryService;
    /**
     * 查询销售明细
     * @param financeSaleDetailVO
     * @return
     */
    @Override
    public List<FinanceSaleDetailPO> querySaleDetail(FinanceSaleDetailVO financeSaleDetailVO){
        //销售明细筛选条件
        String beginDateStr=financeSaleDetailVO.getBeginDateStr();
        String endDateStr=financeSaleDetailVO.getEndDateStr();
        String productName=financeSaleDetailVO.getProductName();
        int customerId=financeSaleDetailVO.getCustomerId()==null?0:financeSaleDetailVO.getCustomerId();
        String salesman=financeSaleDetailVO.getSalesman();

        List<FinanceSaleDetailPO> res=new ArrayList<>();
        if(beginDateStr==null&&endDateStr==null){
            res=financeDao.querySaleDetail(null,null,productName,customerId,salesman);
            return res;
        }
        DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
        try{
            Date beginTime =dateFormat.parse(beginDateStr);
            Date endTime=dateFormat.parse(endDateStr);
            if(beginTime.compareTo(endTime)>0){
                return res;
            }else{
                res=financeDao.querySaleDetail(beginTime,endTime,productName,customerId,salesman);
            }
        }catch (ParseException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 查看库存类单据经营里程表
     * @return
     */
    @Override
    public List<FinanceWarehousePO> queryWarehouseManage(FinanceManageProcessVO financeManageProcessVO){
        String beginDateStr=financeManageProcessVO.getBeginDateStr();
        String endDateStr=financeManageProcessVO.getEndDateStr();
        String operator=financeManageProcessVO.getSalesman();

        List<FinanceWarehousePO> res=new ArrayList<>();
        if(beginDateStr==null&&endDateStr==null){
            res=financeDao.queryWarehouseManage(null,null,operator);
            return res;
        }DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
        try{
            Date beginTime =dateFormat.parse(beginDateStr);
            Date endTime=dateFormat.parse(endDateStr);
            if(beginTime.compareTo(endTime)>0){
                return res;
            }else{
                res=financeDao.queryWarehouseManage(beginTime,endTime,operator);
            }
        }catch (ParseException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 查看销售类单据经营里程表
     * @param financeManageProcessVO
     * @return
     */
    @Override
    public List<FinanceSalePO> querySaleManage(FinanceManageProcessVO financeManageProcessVO){
        String beginDateStr=financeManageProcessVO.getBeginDateStr();
        String endDateStr=financeManageProcessVO.getEndDateStr();
        int customerId= financeManageProcessVO.getCustomerId()==null?0: financeManageProcessVO.getCustomerId();
        String salesman=financeManageProcessVO.getSalesman();

        List<FinanceSalePO> res=new ArrayList<>();
        if(beginDateStr==null&&endDateStr==null){
            res=financeDao.querySaleManage(null,null,customerId,salesman);
            return res;
        }DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
        try{
            Date beginTime =dateFormat.parse(beginDateStr);
            Date endTime=dateFormat.parse(endDateStr);
            if(beginTime.compareTo(endTime)>0){
                return res;
            }else{
                res=financeDao.querySaleManage(beginTime,endTime,customerId,salesman);
            }
        }catch (ParseException e) {
            e.printStackTrace();
        }
        return res;
    }
    /**
     * 查看进货类单据经营里程表
     * @return
     */
    @Override
    public List<FinancePurchasePO> queryPurchaseManage(FinanceManageProcessVO financeManageProcessVO){
        String beginDateStr=financeManageProcessVO.getBeginDateStr();
        String endDateStr=financeManageProcessVO.getEndDateStr();
        int customerId= financeManageProcessVO.getCustomerId()==null?0: financeManageProcessVO.getCustomerId();
        String operator=financeManageProcessVO.getSalesman();

        List<FinancePurchasePO> res=new ArrayList<>();
        if(beginDateStr==null&&endDateStr==null){
            res=financeDao.queryPurchaseManage(null,null,customerId,operator);
            return res;
        }DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
        try{
            Date beginTime =dateFormat.parse(beginDateStr);
            Date endTime=dateFormat.parse(endDateStr);
            if(beginTime.compareTo(endTime)>0){
                return res;
            }else{
                res=financeDao.queryPurchaseManage(beginTime,endTime,customerId,operator);
            }
        }catch (ParseException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 查看财务类单据经营里程表
     * @return
     */
    @Override
    public List<FinanceFinancialPO> queryFinancialManage(FinanceManageProcessVO financeManageProcessVO){
        String beginDateStr=financeManageProcessVO.getBeginDateStr();
        String endDateStr=financeManageProcessVO.getEndDateStr();
        int customerId= financeManageProcessVO.getCustomerId()==null?0: financeManageProcessVO.getCustomerId();
        String operator=financeManageProcessVO.getSalesman();

        List<FinanceFinancialPO> res=new ArrayList<>();
        if(beginDateStr==null&&endDateStr==null){
            res=financeDao.queryFinancialManage(null,null,customerId,operator);
            return res;
        }DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
        try{
            Date beginTime =dateFormat.parse(beginDateStr);
            Date endTime=dateFormat.parse(endDateStr);
            if(beginTime.compareTo(endTime)>0){
                return res;
            }else{
                res=financeDao.queryFinancialManage(beginTime,endTime,customerId,operator);
            }
        }catch (ParseException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 查看一段时间内的经营收支情况
     * @param beginDateStr
     * @param endDateStr
     * @return
     */
    @Override
    public FinanceManageSituationVO queryFinanceManageSituation(String beginDateStr,String endDateStr){
        if(beginDateStr==null||endDateStr==null) throw new RuntimeException("请输入日期");
        FinanceManageSituationVO res=new FinanceManageSituationVO();
        BigDecimal rawTotalInput=BigDecimal.ZERO;
        BigDecimal discountAmount=BigDecimal.ZERO;
        BigDecimal realTotalInput=BigDecimal.ZERO;
        BigDecimal totalOutput=BigDecimal.ZERO;
        BigDecimal totalProfit=BigDecimal.ZERO;
        //销售单总和,用于计算销售收入
        List<FinanceSalePO> salePOList=new ArrayList<>();
        //进货类单据总和，用于计算进货支出和进货退货收入
        List<FinancePurchasePO> purchasePOList=new ArrayList<>();
        //工资单，用于计算人工成本
        List<SalaryBillVO> salaryBillVOList=new ArrayList<>();
        //获取时间段内相关单据
        DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
        try{
            Date beginTime =dateFormat.parse(beginDateStr);
            Date endTime=dateFormat.parse(endDateStr);
            if(beginTime.compareTo(endTime)>0){
                return res;
            }else{
                salePOList=financeDao.querySaleManage(beginTime,endTime,0,null);
                purchasePOList=financeDao.queryPurchaseManage(beginTime,endTime,0,null);
            }
        }catch (ParseException e) {
            e.printStackTrace();
        }
        //获取工资单
        List<String> DateStr=new ArrayList<>();
        DateStr.add(beginDateStr);
        DateStr.add(endDateStr);
        List<Integer> employeeId=new ArrayList<>();
        salaryBillVOList=salaryService.findSalaryBills(employeeId,DateStr);

        //计算折让前总收入和实际总收入
         for(FinanceSalePO financeSalePO:salePOList){
             rawTotalInput=rawTotalInput.add(financeSalePO.getRawTotalAmount());
             realTotalInput=realTotalInput.add(financeSalePO.getFinalAmount());
         }
         //计算折让金额
        discountAmount=rawTotalInput.subtract(realTotalInput);
         //计算进货支出与进货退货收入
         for(FinancePurchasePO financePurchasePO:purchasePOList){
             if(financePurchasePO.getType().equals("进货单")){
             totalOutput=totalOutput.add(financePurchasePO.getTotalAmount());
             }else{
                 realTotalInput=realTotalInput.add(financePurchasePO.getTotalAmount());
             }
         }
         //计算人工成本
         for(SalaryBillVO salaryBillVO:salaryBillVOList){
             totalOutput=totalOutput.add(salaryBillVO.getSalary());
         }
         //计算总利润
        totalProfit=realTotalInput.subtract(totalOutput);
         //设置返回值
        res.setRawTotalInput(rawTotalInput);
        res.setDiscountAmount(discountAmount);
        res.setRealTotalInput(realTotalInput);
        res.setTotalOutput(totalOutput);
        res.setTotalProfit(totalProfit);
        return res;
    }

    /**
     * 查看工资单
     * @param financeManageProcessVO
     * @return
     */
    @Override
    public List<SalaryBillVO> querySalaryManage(FinanceManageProcessVO financeManageProcessVO){
        List<String> DateStr=new ArrayList<>();
        if(financeManageProcessVO.getBeginDateStr()!=null&&financeManageProcessVO.getEndDateStr()!=null){
        DateStr.add(financeManageProcessVO.getBeginDateStr());
        DateStr.add(financeManageProcessVO.getEndDateStr());
        }
        List<Integer> employeeId=new ArrayList<>();
        if(financeManageProcessVO.getCustomerId() != null){
        employeeId.add(financeManageProcessVO.getCustomerId());
        }
        return salaryService.findSalaryBills(employeeId,DateStr);
    }
}
