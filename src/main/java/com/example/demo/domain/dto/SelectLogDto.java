package com.example.demo.domain.dto;

import com.example.demo.domain.entity.InmobiLog;
import lombok.Data;

@Data
public class SelectLogDto extends InmobiLog {

    private Integer page=1;

    private Integer perpage=10;

    private String beginDate;

    private String endDate;


}
