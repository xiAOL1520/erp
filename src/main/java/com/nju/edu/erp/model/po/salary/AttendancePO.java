package com.nju.edu.erp.model.po.salary;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AttendancePO {

    /**
     * 员工id
     */
    private Integer id;
    /**
     * 员工出勤的日期，格式为yyyy-MM-dd
     */
    private String date;

}
