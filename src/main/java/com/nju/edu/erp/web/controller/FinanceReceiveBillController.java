package com.nju.edu.erp.web.controller;

import com.nju.edu.erp.auth.Authorized;
import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.enums.sheetState.FinanceReceiveBillState;
import com.nju.edu.erp.enums.sheetState.PurchaseSheetState;
import com.nju.edu.erp.model.vo.UserVO;
import com.nju.edu.erp.model.vo.finance.FinanceReceiveBillVO;
import com.nju.edu.erp.service.FinanceReceiveBillService;
import com.nju.edu.erp.web.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/financeReceive")
public class FinanceReceiveBillController {
    private final FinanceReceiveBillService financeReceiveBillService;
    @Autowired
    public  FinanceReceiveBillController(FinanceReceiveBillService financeReceiveBillService){this.financeReceiveBillService=financeReceiveBillService;}

    /**
     * 财务人员制定收款单
     * @param userVO
     * @param financeReceiveBillVO
     * @return
     */
    @PostMapping("/makeReceiveBill")
    @Authorized(roles = {Role.FINANCIAL_STAFF,Role.ADMIN})
    public Response makeFinanceReceiveBill(UserVO userVO, @RequestBody FinanceReceiveBillVO financeReceiveBillVO){
        financeReceiveBillService.makeFinanceReceiveBill(userVO,financeReceiveBillVO);
        return Response.buildSuccess();
    }

    /**
     * 收款单审批
     * @param code
     * @param state
     * @return
     */
    @Authorized (roles = {Role.GM, Role.ADMIN})
    @GetMapping(value = "/approval")
    public Response Approval(@RequestParam("code") String code, @RequestParam("state") FinanceReceiveBillState state)  {
        if(state.equals(FinanceReceiveBillState.FAILURE) || state.equals(FinanceReceiveBillState.SUCCESS)) {
            financeReceiveBillService.approval(code, state);
            return Response.buildSuccess();
        } else {
            return Response.buildFailed("000000","操作失败"); // code可能得改一个其他的
        }
    }

    /**
     * 根据状态查看收款单
     */
    @GetMapping(value = "/billShow")
    public Response showBillByState(@RequestParam(value = "state", required = false) FinanceReceiveBillState state)  {
        return Response.buildSuccess(financeReceiveBillService.getFinanceReceiveBillByState(state));
    }


    /**
     * 根据收款单code搜索收款单信息
     * @param code 收款单code
     * @return 收款单全部信息
     */
    @GetMapping(value = "/findBill")
    public Response findBySheetId(@RequestParam(value = "code") String code)  {
        return Response.buildSuccess(financeReceiveBillService.getFinanceReceiveByCode(code));
    }
}
