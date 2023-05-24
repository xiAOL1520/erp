package com.nju.edu.erp.web.controller;

import com.nju.edu.erp.auth.Authorized;
import com.nju.edu.erp.enums.CustomerType;
import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.model.vo.CustomerVO;
import com.nju.edu.erp.model.vo.UserVO;
import com.nju.edu.erp.service.CustomerService;
import com.nju.edu.erp.web.Response;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(path = "/customer")
public class CustomerController {
    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/findById")
    public Response findById(@RequestParam int cid){
        CustomerVO customerVO=new CustomerVO();
        BeanUtils.copyProperties(customerService.findCustomerById(cid),customerVO);
        return Response.buildSuccess(customerVO);
    }

    @GetMapping("/findByType")
    public Response findByType(@RequestParam CustomerType type) {
        return Response.buildSuccess(customerService.getCustomersByType(type));
    }

    @PostMapping("/addOne")
    @Authorized(roles = {Role.ADMIN,Role.SALE_MANAGER,Role.SALE_STAFF,Role.GM})
    public Response addOne(UserVO userVO,@RequestBody CustomerVO customerVO){
        customerService.addOne(userVO,customerVO);
        return Response.buildSuccess();
    }

    @GetMapping("/deleteOne")
    @Authorized(roles = {Role.ADMIN,Role.SALE_MANAGER,Role.SALE_STAFF,Role.GM})
    public Response deleteOne(@RequestParam int cid){
        customerService.deleteOne(cid);
        return Response.buildSuccess();
    }
}
