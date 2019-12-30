package com.example.demo.service.Impl;

import com.alibaba.fastjson.JSON;
import com.example.demo.common.CommonUtil;
import com.example.demo.domain.dto.ExportLogDto;
import com.example.demo.domain.dto.SelectLogDto;
import com.example.demo.domain.entity.InmobiLog;
import com.example.demo.framework.InvalidReqException;
import com.example.demo.service.IDemoService;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

@Service
public class DemoServiceImpl implements IDemoService {

    private final  static Logger LOGGER = LoggerFactory.getLogger(DemoServiceImpl.class);

    @Autowired
    private InmobiLogMapper inmobiLogMapper;


    @Override
    public void importLog(MultipartFile multipartFile) throws InvalidReqException {
        CSVReader csvReader = null;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(multipartFile.getInputStream(),"UTF-16LE"));
            csvReader = new CSVReader(br);
            //过滤表头
            List<InmobiLog> insertLogList= new ArrayList<>();
          // 读取全部每行的内容
            List<String[]> strs=csvReader.readAll();
            System.out.println(JSON.toJSONString(strs));
            if (!CollectionUtils.isEmpty(strs)&&strs.size()>1){
                for (int i = 1; i <strs.size() ; i++) {
                    InmobiLog inmobiLog = new InmobiLog();
                    String[] strArr = strs.get(i)[0].trim().split(",",-1);
                    inmobiLog.setDate(CommonUtil.StringToDate(CommonUtil.deleteQuotationMarks(strArr[0]),"yyyy-MM-dd"));
                    inmobiLog.setCampaignId(CommonUtil.deleteQuotationMarks(strArr[1]));
                    inmobiLog.setCampaignName(CommonUtil.deleteQuotationMarks(strArr[2]));
                    inmobiLog.setAdgroupId(CommonUtil.deleteQuotationMarks(strArr[3]));
                    inmobiLog.setAdgroupName(CommonUtil.deleteQuotationMarks(strArr[4]));
                    inmobiLog.setSiteId(CommonUtil.deleteQuotationMarks(strArr[5]));
                    inmobiLog.setSiteName(CommonUtil.deleteQuotationMarks(strArr[6]));
                    inmobiLog.setRequestUid(CommonUtil.deleteQuotationMarks(strArr[7]));
                    inmobiLog.setAdImpressionsRendered(CommonUtil.deleteQuotationMarks(strArr[8]));
                    inmobiLog.setCreateTime(new Date());
                    insertLogList.add(inmobiLog);
                }
            }

        if (!CollectionUtils.isEmpty(insertLogList)){
            inmobiLogMapper.batchInsert(insertLogList);
        }
        } catch (IOException e) {
            LOGGER.error("该文件不是合法文件，e={}",e);
            throw new InvalidReqException("该文件不是合法文件");
        } catch (CsvValidationException e) {
            LOGGER.error("读取文件失败，e={}",e);
            throw new InvalidReqException("读取文件失败");
        } catch (CsvException ex) {
            LOGGER.error("读取文件失败，e={}",ex);
            throw new InvalidReqException("读取文件失败");
        }  finally {
            if (csvReader!=null){
                try {
                    csvReader.close();
                } catch (IOException e) {
                    throw new InvalidReqException("关闭流失败");
                }
            }
        }
    }

    @Override
    public Map<String,Object> selectLog(SelectLogDto selectLogDto) {
        selectLogDto.setPage((selectLogDto.getPage()-1)*selectLogDto.getPerpage());
       List<InmobiLog> inmobiLogs = inmobiLogMapper.getInmobiLogs(selectLogDto);
       if (CollectionUtils.isEmpty(inmobiLogs)){
         return null;
       }
        Map<String,Object> result = new HashMap<>();
        result.put("resultList",inmobiLogs);
        result.put("resultCount", inmobiLogMapper.getInmobiLogsCount(selectLogDto));
        return result;
    }

    @Override
    public void updateLog(InmobiLog inmobiLog) {
        inmobiLog.setUpdateTime(new Date());
        inmobiLogMapper.updateByPrimaryKeySelective(inmobiLog);
    }

    @Override
    public void deleteLog(List<Integer> inmobiLogId) {
        inmobiLogMapper.deleteByLogIds(inmobiLogId);
    }

    @Override
    public void exportLog(HttpServletResponse response, HttpServletRequest request, ExportLogDto exportLogDto) {
        SelectLogDto selectLogDto = new SelectLogDto();
        List<InmobiLog> inmobiLogs =null;
        if (CollectionUtils.isEmpty(exportLogDto.getExportLogId())){
            BeanUtils.copyProperties(exportLogDto,selectLogDto);//复制对象
            selectLogDto.setPage(null); //导出符合条件全部导出
            inmobiLogs=inmobiLogMapper.getInmobiLogs(selectLogDto);
        }else{
            inmobiLogs=inmobiLogMapper.getInmobiLogsByLogId(exportLogDto.getExportLogId());
        }
        List<Map<String,Object>> dateList=new ArrayList<>();
        if (!CollectionUtils.isEmpty(inmobiLogs)){
            for (InmobiLog inmobiLog:inmobiLogs){
                Map<String,Object> dateMap = new HashMap<>();
                dateMap.put("date",CommonUtil.dateToString(inmobiLog.getDate(),"yyyy-MM-dd"));
                dateMap.put("campaign id",inmobiLog.getCampaignId());
                dateMap.put("campaign name",inmobiLog.getCampaignName());
                dateMap.put("adgroup id",inmobiLog.getAdgroupId());
                dateMap.put("adgroup name",inmobiLog.getAdgroupName());
                dateMap.put("site id",inmobiLog.getSiteId());
                dateMap.put("site name",inmobiLog.getSiteName());
                dateMap.put("request uid",inmobiLog.getRequestUid());
                dateMap.put("ad impressions rendered",inmobiLog.getAdImpressionsRendered());
                dateList.add(dateMap);
            }
        }
        Map<String,Object> headMap = new HashMap<>();
        headMap.put("date","date");
        headMap.put("campaign id","campaign id");
        headMap.put("campaign name","campaign name");
        headMap.put("adgroup id","adgroup id");
        headMap.put("adgroup name","adgroup name");
        headMap.put("site id","site id");
        headMap.put("site name","site name");
        headMap.put("request uid","request uid");
        headMap.put("ad impressions rendered","ad impressions rendered");
        dateList.add(0,headMap);
        HttpRequestWriteExcel.info(request,response,dateList);
    }


}
