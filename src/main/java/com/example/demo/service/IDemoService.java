package com.example.demo.service;

import com.example.demo.domain.dto.ExportLogDto;
import com.example.demo.domain.dto.SelectLogDto;
import com.example.demo.domain.entity.InmobiLog;
import com.example.demo.framework.InvalidReqException;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface IDemoService {

    public void importLog(MultipartFile multipartFile) throws InvalidReqException;

    public Map<String,Object> selectLog(SelectLogDto selectLogDto);

    public void updateLog(InmobiLog inmobiLog);

    public void  deleteLog(List<Integer> inmobiLogId);

    public void exportLog(HttpServletResponse response, HttpServletRequest request, ExportLogDto exportLogDto);
}
