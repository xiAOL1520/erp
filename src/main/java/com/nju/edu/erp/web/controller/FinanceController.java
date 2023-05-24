package com.nju.edu.erp.web.controller;

import com.nju.edu.erp.auth.Authorized;
import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.enums.SheetType;
import com.nju.edu.erp.model.vo.finance.FinanceManageProcessVO;
import com.nju.edu.erp.model.vo.finance.FinanceSaleDetailVO;
import com.nju.edu.erp.service.FinanceService;
import com.nju.edu.erp.web.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/finance")
public class FinanceController {
    private final FinanceService financeService;
    @Autowired
    public  FinanceController(FinanceService financeService){this.financeService=financeService;}

    /**
     * 查询销售明细表
     * @param financeSaleDetailVO
     * @return
     */
    @PostMapping("/querySaleDetail")
    @Authorized(roles = {Role.ADMIN,Role.GM,Role.FINANCIAL_STAFF})
    public Response querySaleDetail(@RequestBody FinanceSaleDetailVO financeSaleDetailVO){
        return Response.buildSuccess(financeService.querySaleDetail(financeSaleDetailVO));
    }

    @PostMapping("/queryManagementProcess")
/*    @Authorized(roles = {Role.ADMIN,Role.GM,Role.FINANCIAL_STAFF})*/
    public Response queryManagementProcess(@RequestBody FinanceManageProcessVO financeManageProcessVO){
        //注意：VO中salesman属性只在销售类中代表业务员，其余类中代表操作员operator；customerId在查询工资单时为员工ID
        /*VO中除了SheetType作为标识查询的表单类型，其余能作为筛选条件的情况：
        销售类：时间区间，customerId客户id，salesman业务员
        库存类：时间区间，salesman操作员
        进货类：时间区间，customerId客户id，salesman操作员
        财务类：时间区间，customerId客户id，salesman操作员
        工资单：时间区间，customerId 员工id
        * */
        /*System.out.println("1:"+financeManageProcessVO);*/
        SheetType[] typeList={SheetType.SALE,SheetType.WAREHOUSE,SheetType.PURCHASE,SheetType.FINANCE};
        Response[] responsesList={Response.buildSuccess(financeService.querySaleManage(financeManageProcessVO)),
                Response.buildSuccess(financeService.queryWarehouseManage(financeManageProcessVO)),
                Response.buildSuccess(financeService.queryPurchaseManage(financeManageProcessVO)),
                Response.buildSuccess(financeService.queryFinancialManage(financeManageProcessVO)),
        };
       /* System.out.println("2:"+financeManageProcessVO);*/
        if(financeManageProcessVO.getSheetType()==SheetType.SALARY){
            return Response.buildSuccess(Response.buildSuccess(financeService.querySalaryManage(financeManageProcessVO)));
        }
        for(int i=0;i<typeList.length;i++){
            if(financeManageProcessVO.getSheetType().equals(typeList[i])){
                return responsesList[i];
            }
        }
        return Response.buildFailed("0007","查询经营历程表失败！");
    }

    @GetMapping("/queryManagementSituation")
//    @Authorized(roles = {Role.ADMIN,Role.GM,Role.FINANCIAL_STAFF})
    public Response queryManagementSituation(@RequestParam String beginDateStr,@RequestParam String endDateStr){
        return Response.buildSuccess(financeService.queryFinanceManageSituation(beginDateStr,endDateStr));
    }
}
