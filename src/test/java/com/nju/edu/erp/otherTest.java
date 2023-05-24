package com.nju.edu.erp;

import com.nju.edu.erp.auth.Authorized;
import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.model.po.CustomerPO;
import com.nju.edu.erp.model.vo.CustomerVO;
import com.nju.edu.erp.model.vo.UserVO;
import com.nju.edu.erp.service.CustomerService;
import com.nju.edu.erp.service.Impl.WarehouseServiceImpl;
import com.nju.edu.erp.service.WarehouseService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;

@SpringBootTest
public class otherTest {

    @Autowired
    WarehouseService warehouseService;
    @Autowired
    CustomerService customerService;

    @Test
    @Transactional
    @Rollback
    public void exportExcelTest(){
        HSSFWorkbook workbook = warehouseService.exportExcel();
        FileOutputStream fileOutputStream= null;
        try {
            fileOutputStream = new FileOutputStream("C:/Users/dell/Desktop/test.xls");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            workbook.write(fileOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void addCustomer(){
        for(int i=1;i<5;i++) {
            UserVO userVO = UserVO.builder()
                    .name("thankbro")
                    .role(Role.SALE_MANAGER)
                    .build();

            CustomerVO customerVO = CustomerVO.builder()
                    .id(i+2)
                    .type("供应商")
                    .level(2)
                    .name("hjb")
                    .phone("114514")
                    .address("nju")
                    .zipcode("114514")
                    .email("114514@nju.edu.cn")
                    .lineOfCredit(BigDecimal.valueOf(1919810))
                    .receivable(BigDecimal.valueOf(114514))
                    .payable(BigDecimal.valueOf(1919810))
                    .operator("thankbro")
                    .build();

            customerService.addOne(userVO, customerVO);
        }

    }
    @Test
    public void addOne(){
        UserVO userVO = UserVO.builder()
                .name("thankbro")
                .role(Role.SALE_MANAGER)
                .build();

        CustomerVO customerVO = CustomerVO.builder()
                .id(2)
                .type("供应商")
                .level(2)
                .name("hjb")
                .phone("114514")
                .address("nju")
                .zipcode("114514")
                .email("114514@nju.edu.cn")
                .lineOfCredit(BigDecimal.valueOf(1919810))
                .receivable(BigDecimal.valueOf(114514))
                .payable(BigDecimal.valueOf(1919810))
                .operator("thankbro")
                .build();

        customerService.addOne(userVO, customerVO);
    }

    @Test
    public void deleteOne(){
        customerService.deleteOne(11);
    }
}
