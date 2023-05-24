package com.nju.edu.erp.dao;

import com.nju.edu.erp.enums.CustomerType;
import com.nju.edu.erp.model.po.CustomerPO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface CustomerDao {
    int updateOne(CustomerPO customerPO);

    CustomerPO findOneById(Integer supplier);

    List<CustomerPO> findAllByType(CustomerType customerType);

    List<CustomerPO> findAll();

    /**
     * 添加一个客户
     * @param toSave
     */
    void addOne(CustomerPO toSave);

    /**
     * 删除指定客户
     * @param customerid 客户id
     */
    void deleteOne(int customerid);

    /**
     * 删除后id自减
     * @param id
     */
    void updateAfterDelete(int id);
    void updateAfter();
}
