package com.nju.edu.erp.web.controller;

import com.nju.edu.erp.auth.Authorized;
import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.model.po.bankPO;
import com.nju.edu.erp.model.vo.finance.bankVO;
import com.nju.edu.erp.service.CategoryService;
import com.nju.edu.erp.service.bankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/bank")
public class bankController {
    private bankService bankService;

    @Autowired
    public bankController(bankService bankService) {
        this.bankService = bankService;
    }

    @GetMapping("/updateOne")
    @Authorized(roles = {Role.ADMIN})
    void updateOne(bankVO bankVO){
        bankService.updateOne(bankVO);
    }

    @GetMapping("/findOneById")
    @Authorized(roles = {Role.ADMIN})
    public bankPO findOneById(@RequestParam int id){
        return bankService.findOneById(id);
    }

    @GetMapping("/findByName")
    @Authorized(roles = {Role.ADMIN})
    List<bankPO> findOneByName(@RequestParam String name){
        return bankService.findByName(name);
    }

    @GetMapping("/findAll")
    @Authorized(roles = {Role.ADMIN})
    List<bankPO> findAll(){
        return bankService.findAll();
    }

    @GetMapping("/addOne")
    @Authorized(roles = {Role.ADMIN})
    void addOne(bankVO bankVO){
        bankService.addOne(bankVO);
    }

    @GetMapping("/deleteOne")
    @Authorized(roles = {Role.ADMIN})
    void deleteOneById(int id){
        bankService.deleteOneById(id);
    }
}
