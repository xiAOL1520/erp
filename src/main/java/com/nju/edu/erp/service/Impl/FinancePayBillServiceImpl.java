package com.nju.edu.erp.service.Impl;

import com.nju.edu.erp.dao.FinancePayBillDao;
import com.nju.edu.erp.enums.sheetState.FinancePayBillState;
import com.nju.edu.erp.enums.sheetState.FinancePayBillState;
import com.nju.edu.erp.enums.sheetState.FinancePayBillState;
import com.nju.edu.erp.model.po.*;
import com.nju.edu.erp.model.vo.CustomerVO;
import com.nju.edu.erp.model.vo.UserVO;
import com.nju.edu.erp.model.vo.finance.*;
import com.nju.edu.erp.model.vo.finance.FinancePayBillContentVO;
import com.nju.edu.erp.service.CustomerService;
import com.nju.edu.erp.service.FinancePayBillService;
import com.nju.edu.erp.service.bankService;
import com.nju.edu.erp.utils.IdGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class FinancePayBillServiceImpl implements FinancePayBillService {
    @Autowired
    FinancePayBillDao financePayBillDao;
    @Autowired
    CustomerService customerService;
    @Autowired
    com.nju.edu.erp.service.bankService bankService;

    @Override
    /**
     * 创建付款单
     * @param userVO
     * @param financePayBillVO
     */
    public void makeFinancePayBill(UserVO userVO, FinancePayBillVO financePayBillVO){
        FinancePayBillPO financePayBillPO=new FinancePayBillPO();
        BeanUtils.copyProperties(financePayBillVO,financePayBillPO);
        // 此处根据制定单据人员确定操作员
        financePayBillPO.setOperator(userVO.getName());
        //设置时间
        financePayBillPO.setCreateTime(new Date());
        //设置付款单编号
        FinancePayBillPO latest=financePayBillDao.getLatest();
        String code = IdGenerator.generateSheetId(latest == null ? null : latest.getCode(), "FKD");
        financePayBillPO.setCode(code);
        //设置客户id
        financePayBillPO.setCustomerId(financePayBillVO.getCustomerVO().getId());
        //设置审批等级
        financePayBillPO.setState(FinancePayBillState.PENDING_LEVEL_1);
        //总额
        BigDecimal sum = BigDecimal.ZERO;
        //转账列表
        List<FinancePayBillContentPO> financePayBillContentPOList=new ArrayList<>();
        for(FinancePayBillContentVO financePayBillContentVO:financePayBillVO.getTransferlist()){
            FinancePayBillContentPO financePayBillContentPO=new FinancePayBillContentPO();
            BeanUtils.copyProperties(financePayBillContentVO,financePayBillContentPO);
            //设置付款单code
            financePayBillContentPO.setPayBillCode(code);
            //设置银行账户id
            financePayBillContentPO.setBankId(financePayBillContentVO.getBankVO().getId());
            financePayBillContentPOList.add(financePayBillContentPO);
            //计算总额
            BigDecimal amount=financePayBillContentPO.getAmount();
            sum=sum.add(amount);
        }
        //设置总额
        financePayBillPO.setSum(sum);
        //保存
        financePayBillDao.saveTransferlist(financePayBillContentPOList);
        financePayBillDao.save(financePayBillPO);
    }

    /**
     * 付款单审批
     * @param code
     * @param state
     */
    public void approval(String code, FinancePayBillState state){
        if(state.equals(FinancePayBillState.FAILURE)){
            FinancePayBillPO financePayBillPO = financePayBillDao.findOneByCode(code);
            if(financePayBillPO.getState() == FinancePayBillState.SUCCESS) throw new RuntimeException("状态更新失败");
            int effectLines = financePayBillDao.updateState(code, state);
            if(effectLines == 0) throw new RuntimeException("状态更新失败");
        }else{
            FinancePayBillState prevState;
            if(state.equals(FinancePayBillState.SUCCESS)) {
                prevState = FinancePayBillState.PENDING_LEVEL_1;
            } else {
                throw new RuntimeException("状态更新失败");
            }
            int effectLines = financePayBillDao.updateStateV2(code, prevState, state);
            if(effectLines == 0) throw new RuntimeException("状态更新失败");
            if(state.equals(FinancePayBillState.SUCCESS)){
                //  审批完成, 修改一系列状态
                // 修改客户的receivable字段
                FinancePayBillPO financePayBillPO = financePayBillDao.findOneByCode(code);
                CustomerPO customerPO = customerService.findCustomerById(financePayBillPO.getCustomerId());
                customerPO.setReceivable(customerPO.getReceivable().subtract(financePayBillPO.getSum()));
                customerService.updateCustomer(customerPO);
                //修改银行账户
                List<FinancePayBillContentPO> financePayBillContentPOList=financePayBillDao.findContentByCode(code);
                for(FinancePayBillContentPO po:financePayBillContentPOList){
                    bankPO temp=bankService.findOneById(po.getBankId());
                    BigDecimal new_remainingSum=temp.getRemainingSum().subtract(po.getAmount());
                    temp.setRemainingSum(new_remainingSum);
                    bankVO res=new bankVO();
                    BeanUtils.copyProperties(temp,res);
                    bankService.updateOne(res);
                }
            }
        }
    }

    /**
     * 根据状态获取付款单(state == null 则获取所有进货单)
     * @param state 付款单状态
     * @return 付款单
     */
    @Override
    public List<FinancePayBillVO> getFinancePayBillByState(FinancePayBillState state){
        List<FinancePayBillVO> res = new ArrayList<>();
        List<FinancePayBillPO> all;
        if(state == null) {
            all = financePayBillDao.findAll();
        } else {
            all = financePayBillDao.findAllByState(state);
        }
        for(FinancePayBillPO po: all) {//po转vo
            FinancePayBillVO vo = new FinancePayBillVO();
            BeanUtils.copyProperties(po, vo);
            //添加客户VO
            CustomerVO cvo=new CustomerVO();
            BeanUtils.copyProperties(customerService.findCustomerById(po.getCustomerId()),cvo);
            vo.setCustomerVO(cvo);
            //添加转账列表
            List<FinancePayBillContentPO> alll =financePayBillDao.findContentByCode(po.getCode());
            List<FinancePayBillContentVO> vos = new ArrayList<>();
            for (FinancePayBillContentPO p : alll) {
                FinancePayBillContentVO v = new FinancePayBillContentVO();
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
     * 根据付款单code搜索付款单信息
     * @param code 付款单code
     * @return 付款单全部信息
     */
    @Override
    public FinancePayBillVO getFinancePayByCode(String code){
        FinancePayBillPO financePayBillPO=financePayBillDao.findOneByCode(code);
        if(financePayBillPO==null) return null;
        FinancePayBillVO financePayBillVO=new FinancePayBillVO();
        BeanUtils.copyProperties(financePayBillPO,financePayBillVO);
        //添加客户VO
        CustomerVO cvo=new CustomerVO();
        BeanUtils.copyProperties(customerService.findCustomerById(financePayBillPO.getCustomerId()),cvo);
        financePayBillVO.setCustomerVO(cvo);

        List<FinancePayBillContentPO> contentPOS=financePayBillDao.findContentByCode(code);
        List<FinancePayBillContentVO> contentVOS=new ArrayList<>();
        for (FinancePayBillContentPO p : contentPOS) {
            FinancePayBillContentVO v = new FinancePayBillContentVO();
            BeanUtils.copyProperties(p, v);
            //添加账户
            bankVO temp=new bankVO();
            BeanUtils.copyProperties(bankService.findOneById(p.getBankId()),temp);
            v.setBankVO(temp);
            contentVOS.add(v);
        }
        financePayBillVO.setTransferlist(contentVOS);
        return financePayBillVO;
    }
}
