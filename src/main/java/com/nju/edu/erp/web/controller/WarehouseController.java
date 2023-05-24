package com.nju.edu.erp.web.controller;

import com.nju.edu.erp.auth.Authorized;
import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.enums.sheetState.WarehouseInputSheetState;
import com.nju.edu.erp.enums.sheetState.WarehouseOutputSheetState;
import com.nju.edu.erp.exception.MyServiceException;
import com.nju.edu.erp.model.po.*;
import com.nju.edu.erp.model.vo.UserVO;
import com.nju.edu.erp.model.vo.warehouse.GetWareProductInfoParamsVO;
import com.nju.edu.erp.model.vo.warehouse.WarehouseInputFormVO;
import com.nju.edu.erp.model.vo.warehouse.WarehouseOutputFormVO;
import com.nju.edu.erp.service.WarehouseService;
import com.nju.edu.erp.web.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/warehouse")
public class WarehouseController {

    public WarehouseService warehouseService;
    private Object IOException;

    @Autowired
    public WarehouseController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

//    // 已废弃, 出库入库现在与销售进货关联
//    @PostMapping("/input")
//    @Authorized(roles = {Role.ADMIN, Role.GM, Role.INVENTORY_MANAGER})
//    public Response warehouseInput(@RequestBody WarehouseInputFormVO warehouseInputFormVO){
//        log.info(warehouseInputFormVO.toString());
//        warehouseService.productWarehousing(warehouseInputFormVO);
//        return Response.buildSuccess();
//    }

//    //已废弃
//    @PostMapping("/output")
//    @Authorized(roles = {Role.ADMIN, Role.GM, Role.INVENTORY_MANAGER})
//    public Response warehouseOutput(@RequestBody WarehouseOutputFormVO warehouseOutputFormVO){
//        log.info(warehouseOutputFormVO.toString());
//        warehouseService.productOutOfWarehouse(warehouseOutputFormVO);
//        return Response.buildSuccess();
//    }

    @PostMapping("/product/count")
    @Authorized(roles = {Role.ADMIN, Role.GM, Role.INVENTORY_MANAGER})
    public Response warehouseOutput(@RequestBody GetWareProductInfoParamsVO getWareProductInfoParamsVO){
        return Response.buildSuccess(warehouseService.getWareProductInfo(getWareProductInfoParamsVO));
    }

//    @GetMapping("/inputSheet/pending")
//    @Authorized(roles = {Role.ADMIN, Role.INVENTORY_MANAGER})
//    public Response warehouseInputSheetPending(UserVO user,
//                                               @RequestParam(value = "sheetId") String sheetId,
//                                               @RequestParam(value = "state") WarehouseInputSheetState state) {
//        if (state.equals(WarehouseInputSheetState.PENDING)) {
//            warehouseService.approval(user, sheetId, state);
//        }
//        else {
//            throw new MyServiceException("C00001", "越权访问！");
//        }
//        return Response.buildSuccess();
//    }

    @GetMapping("/inputSheet/approve")
    @Authorized(roles = {Role.ADMIN, Role.GM, Role.INVENTORY_MANAGER})
    public Response warehouseInputSheetApprove(UserVO user,
                                               @RequestParam(value = "sheetId") String sheetId,
                                               @RequestParam(value = "state") WarehouseInputSheetState state) {
        if (state.equals(WarehouseInputSheetState.FAILURE) || state.equals(WarehouseInputSheetState.SUCCESS)) {
            warehouseService.approvalInputSheet(user, sheetId, state);
        }
        else {
            throw new MyServiceException("C00001", "越权访问！");
        }
        return Response.buildSuccess();
    }

    @GetMapping("/inputSheet/state")
    @Authorized(roles = {Role.ADMIN, Role.INVENTORY_MANAGER})
    public Response getWarehouseInputSheet(@RequestParam(value = "state", required = false) WarehouseInputSheetState state) {
        List<WarehouseInputSheetPO> ans = warehouseService.getWareHouseInputSheetByState(state);
        return Response.buildSuccess(ans);
    }


    @GetMapping("/outputSheet/state")
    @Authorized(roles = {Role.ADMIN, Role.INVENTORY_MANAGER, Role.GM})
    public Response getWarehouseOutSheet(@RequestParam(value = "state", required = false) WarehouseOutputSheetState state) {
        List<WarehouseOutputSheetPO> ans = warehouseService.getWareHouseOutSheetByState(state);
        return Response.buildSuccess(ans);
    }

    @GetMapping("/inputSheet/content")
    @Authorized(roles = {Role.ADMIN, Role.INVENTORY_MANAGER, Role.GM})
    public Response getWarehouseInputSheetContent(@RequestParam(value = "sheetId") String sheetId) {
        List<WarehouseInputSheetContentPO> ans = warehouseService.getWHISheetContentById(sheetId);
        return Response.buildSuccess(ans);
    }

    @GetMapping("/outputSheet/content")
    @Authorized(roles = {Role.ADMIN, Role.INVENTORY_MANAGER, Role.GM})
    public Response getWarehouseOutputSheetContent(@RequestParam(value = "sheetId") String sheetId) {
        List<WarehouseOutputSheetContentPO> ans = warehouseService.getWHOSheetContentById(sheetId);
        return Response.buildSuccess(ans);
    }

    @GetMapping("/outputSheet/approve")
    @Authorized(roles = {Role.ADMIN, Role.GM, Role.INVENTORY_MANAGER})
    public Response warehouseOutputSheetApprove(UserVO user,
                                                @RequestParam(value = "sheetId") String sheetId,
                                                @RequestParam(value = "state") WarehouseOutputSheetState state) {
        if (state.equals(WarehouseOutputSheetState.FAILURE) || state.equals(WarehouseOutputSheetState.SUCCESS)) {
            warehouseService.approvalOutputSheet(user, sheetId, state);
        }
        else {
            throw new MyServiceException("C00001", "越权访问！");
        }
        return Response.buildSuccess();
    }



    /**
     *库存查看：查询指定时间段内出/入库数量/金额/商品信息/分类信息
     * @param beginDateStr 格式：“yyyy-MM-dd HH:mm:ss”，如“2022-05-12 11:38:30”
     * @param endDateStr   格式：“yyyy-MM-dd HH:mm:ss”，如“2022-05-12 11:38:30”
     * @return
     */
    @GetMapping("/sheetContent/time")
    @Authorized(roles = {Role.ADMIN,Role.INVENTORY_MANAGER})
    public Response getWarehouseIODetailByTime(@RequestParam String beginDateStr,@RequestParam String endDateStr) throws ParseException {
        List<WarehouseIODetailPO> ans=warehouseService.getWarehouseIODetailByTime(beginDateStr,endDateStr);
        return Response.buildSuccess(ans);
    }

    /**
     *库存查看：一个时间段内的入库数量合计
     * @param beginDateStr 格式：“yyyy-MM-dd HH:mm:ss”，如“2022-05-12 11:38:30”
     * @param endDateStr   格式：“yyyy-MM-dd HH:mm:ss”，如“2022-05-12 11:38:30”
     * @return
     */
    @GetMapping("/inputSheet/time/quantity")
    @Authorized(roles = {Role.ADMIN,Role.INVENTORY_MANAGER})
    public Response getWarehouseInputProductQuantityByTime(@RequestParam String beginDateStr,@RequestParam String endDateStr) throws ParseException{
        int quantity= warehouseService.getWarehouseInputProductQuantityByTime(beginDateStr,endDateStr);
        return Response.buildSuccess(quantity);
    }

    /**
     *库存查看：一个时间段内的出库数量合计
     * @param beginDateStr 格式：“yyyy-MM-dd HH:mm:ss”，如“2022-05-12 11:38:30”
     * @param endDateStr   格式：“yyyy-MM-dd HH:mm:ss”，如“2022-05-12 11:38:30”
     * @return
     */
    @GetMapping("/outputSheet/time/quantity")
    @Authorized(roles = {Role.ADMIN,Role.INVENTORY_MANAGER})
    public Response getWarehouseOutputProductQuantityByTime(@RequestParam String beginDateStr,@RequestParam String endDateStr) throws ParseException{
        int quantity= warehouseService.getWarehouseOutProductQuantityByTime(beginDateStr,endDateStr);
        return Response.buildSuccess(quantity);
    }

    /**
     * 库存盘点
     * 盘点的是当天的库存快照，包括当天的各种商品的
     * 名称，型号，库存数量，库存均价，批次，批号，出厂日期，并且显示行号。
     * 要求可以导出Excel
     *
     */
    @GetMapping("/warehouse/counting")
    @Authorized(roles = {Role.ADMIN,Role.INVENTORY_MANAGER})
    public Response getWarehouseCounting() {
        return Response.buildSuccess(warehouseService.warehouseCounting());
    }

    @GetMapping("/warehouse/export-excel")
    @Authorized(roles={Role.ADMIN,Role.INVENTORY_MANAGER})
    public ResponseEntity<byte[]> exportExcel(){
        HSSFWorkbook workbook = warehouseService.exportExcel();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date now=new Date();
            String today= df.format(now);
            String filename = "库存快照 "+today+".xls";

        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        try {
            workbook.write(byteArrayOutputStream);
        } catch (java.io.IOException e) {
            e.printStackTrace();
        } finally {
            try {
                byteArrayOutputStream.close();
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }
        HttpHeaders httpHeaders=new HttpHeaders();
        httpHeaders.setContentDispositionFormData("attachment",filename);
        httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        ResponseEntity<byte[]> filebyte=new ResponseEntity<byte[]>(byteArrayOutputStream.toByteArray(),httpHeaders, HttpStatus.CREATED);
        try {
            byteArrayOutputStream.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        return filebyte;
    }
    /*public void exportExcel(HttpServletResponse response){
        // 创建工作表
        HSSFWorkbook workbook = warehouseService.exportExcel();
        String filename = null;
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date now=new Date();
            String today= df.format(now);
            filename = URLEncoder.encode("库存快照 "+today+".xls", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        response.reset();
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment;filename="+filename);
        // 文件导出
        try {
            workbook.write(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
}
