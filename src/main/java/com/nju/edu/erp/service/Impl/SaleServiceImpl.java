package com.nju.edu.erp.service.Impl;

import com.nju.edu.erp.dao.CustomerDao;
import com.nju.edu.erp.dao.ProductDao;
import com.nju.edu.erp.dao.PromotionDao;
import com.nju.edu.erp.dao.SaleSheetDao;
import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.enums.sheetState.SaleSheetState;
import com.nju.edu.erp.model.po.*;
import com.nju.edu.erp.model.vo.ProductInfoVO;
import com.nju.edu.erp.model.vo.Sale.SaleSheetContentVO;
import com.nju.edu.erp.model.vo.Sale.SaleSheetVO;
import com.nju.edu.erp.model.vo.UserVO;
import com.nju.edu.erp.model.vo.purchase.PurchaseSheetContentVO;
import com.nju.edu.erp.model.vo.warehouse.WarehouseOutputFormContentVO;
import com.nju.edu.erp.model.vo.warehouse.WarehouseOutputFormVO;
import com.nju.edu.erp.service.CustomerService;
import com.nju.edu.erp.service.ProductService;
import com.nju.edu.erp.service.SaleService;
import com.nju.edu.erp.service.WarehouseService;
import com.nju.edu.erp.utils.IdGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class SaleServiceImpl implements SaleService {

    private final SaleSheetDao saleSheetDao;

    private final ProductDao productDao;

    private final CustomerDao customerDao;

    private final ProductService productService;

    private final CustomerService customerService;

    private final WarehouseService warehouseService;

    private final PromotionDao promotionDao;

    @Autowired
    public SaleServiceImpl(SaleSheetDao saleSheetDao, ProductDao productDao, CustomerDao customerDao, ProductService productService, CustomerService customerService, WarehouseService warehouseService, PromotionDao promotionDao) {
        this.saleSheetDao = saleSheetDao;
        this.productDao = productDao;
        this.customerDao = customerDao;
        this.productService = productService;
        this.customerService = customerService;
        this.warehouseService = warehouseService;
        this.promotionDao = promotionDao;
    }

    @Override
    @Transactional
    public void makeSaleSheet(UserVO userVO, SaleSheetVO saleSheetVO) {
        // TODO
        // 需要持久化销售单（SaleSheet）和销售单content（SaleSheetContent），其中总价或者折后价格的计算需要在后端进行
        // 需要的service和dao层相关方法均已提供，可以不用自己再实现一遍
        SaleSheetPO saleSheetPO=new SaleSheetPO();
        BeanUtils.copyProperties(saleSheetVO,saleSheetPO);

        saleSheetPO.setOperator(userVO.getName());
        saleSheetPO.setCreateTime(new Date());
        SaleSheetPO latest=saleSheetDao.getLatestSheet();
        String id=IdGenerator.generateSheetId(latest==null?null: latest.getId(),"XSD");
        saleSheetPO.setId(id);
        saleSheetPO.setState(SaleSheetState.PENDING_LEVEL_1);
        BigDecimal rawtotalAmount= BigDecimal.ZERO;
        List<SaleSheetContentPO> saleSheetContentPOList=new ArrayList<>();
        for(SaleSheetContentVO saleSheetContentVO:saleSheetVO.getSaleSheetContent()){
            SaleSheetContentPO saleSheetContentPO=new SaleSheetContentPO();
            BeanUtils.copyProperties(saleSheetContentVO,saleSheetContentPO);
            saleSheetContentPO.setSaleSheetId(id);
            BigDecimal unitPrice=saleSheetContentPO.getUnitPrice();
            if(unitPrice==null){
                ProductPO product=productDao.findById(saleSheetContentPO.getPid());
                unitPrice=product.getRetailPrice();
                saleSheetContentPO.setUnitPrice(unitPrice);
            }
            saleSheetContentPO.setTotalPrice(unitPrice.multiply(BigDecimal.valueOf(saleSheetContentPO.getQuantity())));
            saleSheetContentPOList.add(saleSheetContentPO);
            rawtotalAmount=rawtotalAmount.add(saleSheetContentPO.getTotalPrice());
        }
        saleSheetDao.saveBatchSheetContent(saleSheetContentPOList);
        saleSheetPO.setRawTotalAmount(rawtotalAmount);
        BigDecimal finalAmount = computeFinalAmount(userVO.getRole(), rawtotalAmount, saleSheetPO.getDiscount(), saleSheetPO.getVoucherAmount(), saleSheetPO.getSupplier());
        saleSheetPO.setFinalAmount(finalAmount);
        saleSheetDao.saveSheet(saleSheetPO);
    }

    @Override
    @Transactional
    public List<SaleSheetVO> getSaleSheetByState(SaleSheetState state) {
        // TODO
        // 根据单据状态获取销售单（注意：VO包含SaleSheetContent）
        // 依赖的dao层部分方法未提供，需要自己实现
        List<SaleSheetVO> res = new ArrayList<>();
        List<SaleSheetPO> all;
        if(state == null) {
            all = saleSheetDao.findAllSheet();
        } else {
            all = saleSheetDao.findAllSheetByState(state);
        }
        for(SaleSheetPO po: all) {
            SaleSheetVO vo = new SaleSheetVO();
            BeanUtils.copyProperties(po, vo);
            List<SaleSheetContentPO> alll = saleSheetDao.findContentBySheetId(po.getId());
            List<SaleSheetContentVO> vos = new ArrayList<>();
            for (SaleSheetContentPO p : alll) {
                SaleSheetContentVO v = new SaleSheetContentVO();
                BeanUtils.copyProperties(p, v);
                vos.add(v);
            }
            vo.setSaleSheetContent(vos);
            res.add(vo);
        }
        return res;
    }

    /**
     * 根据销售单id进行审批(state == "待二级审批"/"审批完成"/"审批失败")
     * 在controller层进行权限控制
     *
     * @param saleSheetId 销售单id
     * @param state       销售单要达到的状态
     */
    @Override
    @Transactional
    public void approval(String saleSheetId, SaleSheetState state) {
        // TODO
        // 需要的service和dao层相关方法均已提供，可以不用自己再实现一遍
        /* 一些注意点：
            1. 二级审批成功之后需要进行
                 1. 修改单据状态
                 2. 更新商品表
                 3. 更新客户表
                 4. 新建出库草稿
            2. 一级审批状态不能直接到审批完成状态； 二级审批状态不能回到一级审批状态
         */
        if(state.equals(SaleSheetState.FAILURE)){
            SaleSheetPO saleSheet=saleSheetDao.findSheetById(saleSheetId);
            if(saleSheet.getState()==SaleSheetState.SUCCESS) throw new RuntimeException("状态更新失败");
            int effectlines=saleSheetDao.updateSheetState(saleSheetId,state);
            if(effectlines==0) throw new RuntimeException("状态更新失败");
        }else {
            SaleSheetState prevState;
            if(state.equals(SaleSheetState.SUCCESS)){
                prevState=SaleSheetState.PENDING_LEVEL_2;
            }else if(state.equals(SaleSheetState.PENDING_LEVEL_2)){
                prevState=SaleSheetState.PENDING_LEVEL_1;
            }else{
                throw new RuntimeException("状态更新失败");
            }
            int effectlines= saleSheetDao.updateSheetStateOnPrev(saleSheetId,prevState,state);
            if(effectlines==0) throw new RuntimeException("状态更新失败");
            if(state.equals(SaleSheetState.SUCCESS)){
                //更新商品表的最近零售价
                    // 根据saleSheetId查到对应的content -> 得到商品id和零售价
                    // 根据商品id和单价更新商品最近进价recentRp
                List<SaleSheetContentPO> sheetContentPOS=saleSheetDao.findContentBySheetId(saleSheetId);
                List<WarehouseOutputFormContentVO> warehouseOutputFormContentVOS=new ArrayList<>();

                for(SaleSheetContentPO content:sheetContentPOS){
                    ProductInfoVO productInfoVO=new ProductInfoVO();
                    productInfoVO.setId(content.getPid());
                    productInfoVO.setRecentRp(content.getUnitPrice());
                    productService.updateProduct(productInfoVO);

                    WarehouseOutputFormContentVO warehouseOutputFormContentVO=new WarehouseOutputFormContentVO();
                    warehouseOutputFormContentVO.setSalePrice(content.getUnitPrice());
                    warehouseOutputFormContentVO.setQuantity(content.getQuantity());
                    warehouseOutputFormContentVO.setRemark(content.getRemark());
                    warehouseOutputFormContentVO.setPid(content.getPid());
                    warehouseOutputFormContentVOS.add(warehouseOutputFormContentVO);
                }
                //更新客户表（更新应收字段receivable）
                SaleSheetPO saleSheetPO=saleSheetDao.findSheetById(saleSheetId);
                CustomerPO customerPO=customerService.findCustomerById(saleSheetPO.getSupplier());
                customerPO.setReceivable(customerPO.getReceivable().add(saleSheetPO.getFinalAmount()));
                customerService.updateCustomer(customerPO);
                //建立出库单草稿
                WarehouseOutputFormVO warehouseOutputFormVO=new WarehouseOutputFormVO();
                warehouseOutputFormVO.setOperator(null);
                warehouseOutputFormVO.setSaleSheetId(saleSheetId);
                warehouseOutputFormVO.setList(warehouseOutputFormContentVOS);
                warehouseService.productOutOfWarehouse(warehouseOutputFormVO);
            }
        }
    }

    /**
     * 获取某个销售人员某段时间内消费总金额最大的客户(不考虑退货情况,销售单不需要审批通过,如果这样的客户有多个，仅保留一个)
     * @param salesman 销售人员的名字
     * @param beginDateStr 开始时间字符串
     * @param endDateStr 结束时间字符串
     * @return
     */
    public CustomerPurchaseAmountPO getMaxAmountCustomerOfSalesmanByTime(String salesman,String beginDateStr,String endDateStr){
        DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try{
            Date beginTime =dateFormat.parse(beginDateStr);
            Date endTime=dateFormat.parse(endDateStr);
            if(beginTime.compareTo(endTime)>0){
                return null;
            }else{
                return saleSheetDao.getMaxAmountCustomerOfSalesmanByTime(salesman,beginTime,endTime);
            }
        }catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据销售单Id搜索销售单信息
     * @param saleSheetId 销售单Id
     * @return 销售单全部信息
     */
    @Override
    public SaleSheetVO getSaleSheetById(String saleSheetId) {
        SaleSheetPO saleSheetPO = saleSheetDao.findSheetById(saleSheetId);
        if(saleSheetPO == null) return null;
        List<SaleSheetContentPO> contentPO = saleSheetDao.findContentBySheetId(saleSheetId);
        SaleSheetVO sVO = new SaleSheetVO();
        BeanUtils.copyProperties(saleSheetPO, sVO);
        List<SaleSheetContentVO> saleSheetContentVOList = new ArrayList<>();
        for (SaleSheetContentPO content:
                contentPO) {
            SaleSheetContentVO sContentVO = new SaleSheetContentVO();
            BeanUtils.copyProperties(content, sContentVO);
            saleSheetContentVOList.add(sContentVO);
        }
        sVO.setSaleSheetContent(saleSheetContentVOList);
        return sVO;
    }

    private BigDecimal computeFinalAmount(Role role, BigDecimal rawTotalAmount, BigDecimal discount, BigDecimal voucher, int supplier) {

        List<PromotionPO> promotionPOList = promotionDao.getAllPromotions();
        CustomerPO customerPO = customerDao.findOneById(supplier);

        if(discount != null) {
            if(role == Role.SALE_STAFF && discount.doubleValue()<0.9){
                throw new RuntimeException("单据制定失败：折让金额超标！");
            }else if(role == Role.SALE_MANAGER && discount.doubleValue()<0.7){
                throw new RuntimeException("单据制定失败：折让金额超标！");
            }
            rawTotalAmount = rawTotalAmount.multiply(discount);
        } else {
            for(PromotionPO promotionPO : promotionPOList) {
                if(promotionPO.getCustomerLevel() != null && promotionPO.getCustomerLevel() != customerPO.getLevel())
                    continue;
                if(promotionPO.getMinPiece() != null && rawTotalAmount.compareTo(promotionPO.getMinPiece()) <= 0)
                    continue;
                if(promotionPO.getMaxPiece() != null && rawTotalAmount.compareTo(promotionPO.getMaxPiece()) >= 0)
                    continue;

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, Integer.parseInt(promotionPO.getStartDate().split("-")[0]));
                calendar.set(Calendar.MONTH, Integer.parseInt(promotionPO.getStartDate().split("-")[1]) - 1);
                calendar.set(Calendar.DATE, Integer.parseInt(promotionPO.getStartDate().split("-")[2]));
                Date startDate = calendar.getTime();
                calendar.set(Calendar.YEAR, Integer.parseInt(promotionPO.getEndDate().split("-")[0]));
                calendar.set(Calendar.MONTH, Integer.parseInt(promotionPO.getEndDate().split("-")[1]) - 1);
                calendar.set(Calendar.DATE, Integer.parseInt(promotionPO.getEndDate().split("-")[2]));
                Date endDate = calendar.getTime();

                if(new Date().before(startDate) || endDate.before(new Date()))
                    continue;

                if(promotionPO.getDiscount() != null) {
                    rawTotalAmount = rawTotalAmount.multiply(promotionPO.getDiscount());
                    break;
                }

            }
        }

        if(voucher != null) {
            rawTotalAmount = rawTotalAmount.subtract(voucher);
        } else {
            for(PromotionPO promotionPO : promotionPOList) {
                if(promotionPO.getCustomerLevel() != null && promotionPO.getCustomerLevel() != customerPO.getLevel())
                    continue;
                if(promotionPO.getMinPiece() != null && rawTotalAmount.compareTo(promotionPO.getMinPiece()) <= 0)
                    continue;
                if(promotionPO.getMaxPiece() != null && rawTotalAmount.compareTo(promotionPO.getMaxPiece()) >= 0)
                    continue;

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, Integer.parseInt(promotionPO.getStartDate().split("-")[0]));
                calendar.set(Calendar.MONTH, Integer.parseInt(promotionPO.getStartDate().split("-")[1]) - 1);
                calendar.set(Calendar.DATE, Integer.parseInt(promotionPO.getStartDate().split("-")[2]));
                Date startDate = calendar.getTime();
                calendar.set(Calendar.YEAR, Integer.parseInt(promotionPO.getEndDate().split("-")[0]));
                calendar.set(Calendar.MONTH, Integer.parseInt(promotionPO.getEndDate().split("-")[1]) - 1);
                calendar.set(Calendar.DATE, Integer.parseInt(promotionPO.getEndDate().split("-")[2]));
                Date endDate = calendar.getTime();

                if(new Date().before(startDate) || endDate.before(new Date()))
                    continue;

                if(promotionPO.getVoucher() != null) {
                    rawTotalAmount = rawTotalAmount.subtract(promotionPO.getVoucher());
                    break;
                }

            }
        }

        if(rawTotalAmount.compareTo(BigDecimal.ZERO) < 0)
            throw new RuntimeException("折让金额过多！请检查销售单或促销策略。");

        return rawTotalAmount;

    }
}
