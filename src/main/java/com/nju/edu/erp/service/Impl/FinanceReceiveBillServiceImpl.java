package com.nju.edu.erp.service.Impl;

import com.nju.edu.erp.dao.FinanceReceiveBillDao;
import com.nju.edu.erp.enums.sheetState.FinanceReceiveBillState;
import com.nju.edu.erp.enums.sheetState.PurchaseSheetState;
import com.nju.edu.erp.model.po.*;
import com.nju.edu.erp.model.vo.CustomerVO;
import com.nju.edu.erp.model.vo.UserVO;
import com.nju.edu.erp.model.vo.finance.FinanceReceiveBillContentVO;
import com.nju.edu.erp.model.vo.finance.FinanceReceiveBillVO;
import com.nju.edu.erp.model.vo.finance.bankVO;
import com.nju.edu.erp.model.vo.purchase.PurchaseSheetContentVO;
import com.nju.edu.erp.model.vo.purchase.PurchaseSheetVO;
import com.nju.edu.erp.service.CustomerService;
import com.nju.edu.erp.service.FinanceReceiveBillService;
import com.nju.edu.erp.service.bankService;
import com.nju.edu.erp.utils.IdGenerator;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class FinanceReceiveBillServiceImpl implements FinanceReceiveBillService {
    @Autowired
    FinanceReceiveBillDao financeReceiveBillDao;
    @Autowired
    CustomerService customerService;
    @Autowired
    bankService bankService;


    @Override
    public void makeFinanceReceiveBill(UserVO userVO, FinanceReceiveBillVO financeReceiveBillVO){
        FinanceReceiveBillPO financeReceiveBillPO=new FinanceReceiveBillPO();
        BeanUtils.copyProperties(financeReceiveBillVO,financeReceiveBillPO);
        // 此处根据制定单据人员确定操作员
        financeReceiveBillPO.setOperator(userVO.getName());
        //设置时间
        financeReceiveBillPO.setCreateTime(new Date());
        //设置收款单编号
        FinanceReceiveBillPO latest=financeReceiveBillDao.getLatest();
        String code = IdGenerator.generateSheetId(latest == null ? null : latest.getCode(), "SKD");
        financeReceiveBillPO.setCode(code);
        //设置客户id
        financeReceiveBillPO.setCustomerId(financeReceiveBillVO.getCustomerVO().getId());
        //设置审批等级
        financeReceiveBillPO.setState(FinanceReceiveBillState.PENDING_LEVEL_1);
        //总额
        BigDecimal sum = BigDecimal.ZERO;
        //转账列表
        List<FinanceReceiveBillContentPO> financeReceiveBillContentPOList=new ArrayList<>();
        for(FinanceReceiveBillContentVO financeReceiveBillContentVO:financeReceiveBillVO.getTransferlist()){
            FinanceReceiveBillContentPO financeReceiveBillContentPO=new FinanceReceiveBillContentPO();
            BeanUtils.copyProperties(financeReceiveBillContentVO,financeReceiveBillContentPO);
            //设置收款单code
            financeReceiveBillContentPO.setReceiveBillCode(code);
            //设置银行账户id
            financeReceiveBillContentPO.setBankId(financeReceiveBillContentVO.getBankVO().getId());
            financeReceiveBillContentPOList.add(financeReceiveBillContentPO);
            //计算总额
            BigDecimal amount=financeReceiveBillContentPO.getAmount();
            sum=sum.add(amount);
        }
        //设置总额
        financeReceiveBillPO.setSum(sum);
        //保存
        financeReceiveBillDao.saveTransferlist(financeReceiveBillContentPOList);
        financeReceiveBillDao.save(financeReceiveBillPO);
    }

    @Override
    public void approval(String code, FinanceReceiveBillState state){
        if(state.equals(FinanceReceiveBillState.FAILURE)){
            FinanceReceiveBillPO financeReceiveBillPO = financeReceiveBillDao.findOneByCode(code);
            if(financeReceiveBillPO.getState() == FinanceReceiveBillState.SUCCESS) throw new RuntimeException("状态更新失败");
            int effectLines = financeReceiveBillDao.updateState(code, state);
            if(effectLines == 0) throw new RuntimeException("状态更新失败");
        }else{
            FinanceReceiveBillState prevState;
            if(state.equals(FinanceReceiveBillState.SUCCESS)) {
                prevState = FinanceReceiveBillState.PENDING_LEVEL_1;
            } else {
                throw new RuntimeException("状态更新失败");
            }
            int effectLines = financeReceiveBillDao.updateStateV2(code, prevState, state);
            if(effectLines == 0) throw new RuntimeException("状态更新失败");
            if(state.equals(FinanceReceiveBillState.SUCCESS)){
                //  审批完成, 修改一系列状态
                // 修改客户的payable字段
                FinanceReceiveBillPO financeReceiveBillPO = financeReceiveBillDao.findOneByCode(code);
                CustomerPO customerPO = customerService.findCustomerById(financeReceiveBillPO.getCustomerId());
                customerPO.setPayable(customerPO.getPayable().subtract(financeReceiveBillPO.getSum()));
                customerService.updateCustomer(customerPO);
                //修改银行账户
                List<FinanceReceiveBillContentPO> financeReceiveBillContentPOList=financeReceiveBillDao.findContentByCode(code);
                for(FinanceReceiveBillContentPO po:financeReceiveBillContentPOList){
                    bankPO temp=bankService.findOneById(po.getBankId());
                    BigDecimal new_remainingSum=temp.getRemainingSum().add(po.getAmount());
                    temp.setRemainingSum(new_remainingSum);
                    bankVO res=new bankVO();
                    BeanUtils.copyProperties(temp,res);
                    bankService.updateOne(res);
                }
            }
        }
    }
    /**
     * 根据状态获取收款单(state == null 则获取所有进货单)
     * @param state 收款单状态
     * @return 收款单
     */
    @Override
    public List<FinanceReceiveBillVO> getFinanceReceiveBillByState(FinanceReceiveBillState state){
        List<FinanceReceiveBillVO> res = new ArrayList<>();
        List<FinanceReceiveBillPO> all;
        if(state == null) {
            all = financeReceiveBillDao.findAll();
        } else {
            all = financeReceiveBillDao.findAllByState(state);
        }
        for(FinanceReceiveBillPO po: all) {//po转vo
            FinanceReceiveBillVO vo = new FinanceReceiveBillVO();
            BeanUtils.copyProperties(po, vo);
            //添加客户VO
            CustomerVO cvo=new CustomerVO();
            BeanUtils.copyProperties(customerService.findCustomerById(po.getCustomerId()),cvo);
            vo.setCustomerVO(cvo);
            //添加转账列表
            List<FinanceReceiveBillContentPO> alll =financeReceiveBillDao.findContentByCode(po.getCode());
            List<FinanceReceiveBillContentVO> vos = new ArrayList<>();
            for (FinanceReceiveBillContentPO p : alll) {
                FinanceReceiveBillContentVO v = new FinanceReceiveBillContentVO();
                BeanUtils.copyProperties(p, v);
                //添加账户
                bankVO temp=new bankVO();
                BeanUtils.copyProperties(bankService.findOneById(p.getBankId()),temp);
                v.setBankVO(temp);
                vos.add(v);
            }
            vo.setTransferlist(vos);
            res.add(vo);
        }
        return res;
    }

    /**
     * 根据收款单code搜索收款单单信息
     * @param code 收款单code
     * @return 收款单全部信息
     */
    @Override
    public FinanceReceiveBillVO getFinanceReceiveByCode(String code){
        FinanceReceiveBillPO financeReceiveBillPO=financeReceiveBillDao.findOneByCode(code);
        if(financeReceiveBillPO==null) return null;
        FinanceReceiveBillVO financeReceiveBillVO=new FinanceReceiveBillVO();
        BeanUtils.copyProperties(financeReceiveBillPO,financeReceiveBillVO);
        //添加客户VO
        CustomerVO cvo=new CustomerVO();
        BeanUtils.copyProperties(customerService.findCustomerById(financeReceiveBillPO.getCustomerId()),cvo);
        financeReceiveBillVO.setCustomerVO(cvo);

        List<FinanceReceiveBillContentPO> contentPOS=financeReceiveBillDao.findContentByCode(code);
        List<FinanceReceiveBillContentVO> contentVOS=new ArrayList<>();
        for (FinanceReceiveBillContentPO p : contentPOS) {
            FinanceReceiveBillContentVO v = new FinanceReceiveBillContentVO();
            BeanUtils.copyProperties(p, v);
            //添加账户
            bankVO temp=new bankVO();
            BeanUtils.copyProperties(bankService.findOneById(p.getBankId()),temp);
            v.setBankVO(temp);
            contentVOS.add(v);
        }
        financeReceiveBillVO.setTransferlist(contentVOS);
        return financeReceiveBillVO;
    }
}
