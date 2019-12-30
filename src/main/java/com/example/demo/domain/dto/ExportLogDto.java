package com.example.demo.domain.dto;

import com.example.demo.domain.entity.InmobiLog;
import lombok.Data;

import java.util.List;
@Data
public class ExportLogDto extends InmobiLog {
    private List<Integer> exportLogId;

    private String beginDate;

    private String endDate;
}
