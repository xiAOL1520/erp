package com.nju.edu.erp.service.Impl;

import com.nju.edu.erp.dao.bankDao;
import com.nju.edu.erp.model.po.bankPO;
import com.nju.edu.erp.model.vo.finance.bankVO;
import com.nju.edu.erp.service.bankService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class bankServiceImpl implements bankService {
    private final bankDao bankDao;
    @Autowired
    public bankServiceImpl(bankDao bankDao) {
        this.bankDao = bankDao;
    }

    @Override
    public void updateOne(bankVO bankVO) {
        bankPO bankPO=new bankPO();
        BeanUtils.copyProperties(bankVO,bankPO);
        bankDao.updateOne(bankPO);
    }

    @Override
    public bankPO findOneById(int id) {
        return bankDao.findOneById(id);
    }

    @Override
    public List<bankPO> findByName(String name) {
        return bankDao.findByName(name);
    }

    @Override
    public List<bankPO> findAll() {
        return bankDao.findAll();
    }

    @Override
    public void addOne(bankVO bankVO) {
        bankPO bankPO=new bankPO();
        BeanUtils.copyProperties(bankVO,bankPO);
        bankDao.addOne(bankPO);
    }

    @Override
    public void deleteOneById(int id) {
        bankDao.deleteOneById(id);
        bankDao.updateAfterDelete(id);
        bankDao.updateAfter();
    }
}
