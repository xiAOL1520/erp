package com.nju.edu.erp.web.controller;

import com.nju.edu.erp.auth.Authorized;
import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.enums.sheetState.FinancePayBillState;
import com.nju.edu.erp.model.vo.UserVO;
import com.nju.edu.erp.model.vo.finance.FinancePayBillVO;
import com.nju.edu.erp.service.FinancePayBillService;
import com.nju.edu.erp.web.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/financePay")
public class FinancePayBillController {
    private final FinancePayBillService financePayBillService;
    @Autowired
    public  FinancePayBillController(FinancePayBillService financePayBillService){this.financePayBillService=financePayBillService;}

    /**
     * 财务人员制定付款单
     * @param userVO
     * @param financePayBillVO
     * @return
     */
    @PostMapping("/makePayBill")
    @Authorized(roles = {Role.FINANCIAL_STAFF,Role.ADMIN})
    public Response makeFinancePayBill(UserVO userVO, @RequestBody FinancePayBillVO financePayBillVO){
        financePayBillService.makeFinancePayBill(userVO,financePayBillVO);
        return Response.buildSuccess();
    }

    /**
     * 付款单审批
     * @param code
     * @param state
     * @return
     */
    @Authorized (roles = {Role.GM, Role.ADMIN})
    @GetMapping(value = "/approval")
    public Response Approval(@RequestParam("code") String code, @RequestParam("state") FinancePayBillState state)  {
        if(state.equals(FinancePayBillState.FAILURE) || state.equals(FinancePayBillState.SUCCESS)) {
            financePayBillService.approval(code, state);
            return Response.buildSuccess();
        } else {
            return Response.buildFailed("000000","操作失败"); // code可能得改一个其他的
        }
    }

    /**
     * 根据状态查看付款单
     */
    @GetMapping(value = "/billShow")
    public Response showBillByState(@RequestParam(value = "state", required = false) FinancePayBillState state)  {
        return Response.buildSuccess(financePayBillService.getFinancePayBillByState(state));
    }


    /**
     * 根据付款单code搜索付款单信息
     * @param code 付款单code
     * @return 付款单全部信息
     */
    @GetMapping(value = "/findBill")
    public Response findBySheetId(@RequestParam(value = "code") String code)  {
        return Response.buildSuccess(financePayBillService.getFinancePayByCode(code));
    }
}
