package com.example.demo.controller;

import com.example.demo.domain.dto.ExportLogDto;
import com.example.demo.domain.dto.SelectLogDto;
import com.example.demo.domain.entity.InmobiLog;
import com.example.demo.framework.InvalidReqException;
import com.example.demo.service.IDemoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/inmobi/demo/log")
public class DemoController extends DemoBaseController {

    private static final Logger logger = LoggerFactory.getLogger(DemoController.class);

    @Autowired
    private IDemoService demoService;

    @RequestMapping(value = "/index")
    public String welcomeIndex() {

      return "index";
    }

    /**
     * 导入
     * @param request
     * @return
     * @throws InvalidReqException
     */
    @RequestMapping(value = "/import",method = RequestMethod.POST)
    @ResponseBody
    public Object logImport(HttpServletRequest request) throws InvalidReqException  {
        //创建一个通用的多部分解析器.
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        //设置编码
        commonsMultipartResolver.setDefaultEncoding("utf-8");
        //判断 request 是否有文件上传,即多部分请求..
        if (!commonsMultipartResolver.isMultipart(request)){
            throw new InvalidReqException("请上传正确的文件~");
        }
        MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) request;
        MultipartFile file =mulReq.getFile("file");
        demoService.importLog(file);
        return getSuccessResult();
    }

    /**
     * 查询 （分页）
     * @param request
     * @param selectLogDto
     * @return
     */
    @RequestMapping(value = "/select",method = RequestMethod.GET)
    @ResponseBody
    public Object select(HttpServletRequest request, SelectLogDto selectLogDto)  {
        return getResult( demoService.selectLog(selectLogDto));
    }

    /**
     * 修改
     * @param request
     * @param inmobiLog
     * @return
     */
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    @ResponseBody
    public Object update(HttpServletRequest request, @RequestBody InmobiLog inmobiLog) {
        demoService.updateLog(inmobiLog);
         return getSuccessResult();
    }

    /**
     * 删除
     * @param request
     * @return
     */
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    @ResponseBody
    public Object delete(HttpServletRequest request, @RequestBody List<Integer> inmobiLoIds) {
        demoService.deleteLog(inmobiLoIds);
         return getSuccessResult();
    }

    /**
     * 导出
     * @param request
     * @param response
     * @param exportLogDto
     * @return
     */
    @RequestMapping(value = "/export",method = RequestMethod.GET)
    @ResponseBody
    public Object export(HttpServletRequest request, HttpServletResponse response, ExportLogDto exportLogDto) {
        demoService.exportLog(response,request,exportLogDto);
        return getSuccessResult();
    }
    }

