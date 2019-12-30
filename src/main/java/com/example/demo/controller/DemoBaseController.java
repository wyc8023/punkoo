package com.example.demo.controller;

import com.example.demo.domain.vo.Result;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;

public class DemoBaseController {

    public Result getResult(Object obj) {
        Boolean IsEmptyResult=Boolean.FALSE;
        if(obj instanceof Collection) {
            if(CollectionUtils.isEmpty((List)obj)) {
                IsEmptyResult=Boolean.TRUE;
            }
        }else {
            if(obj==null) {
                IsEmptyResult=Boolean.TRUE;
            }
        }
        Result result = new Result();
        if(IsEmptyResult) {
            result.setCode("201");
            result.setMessage("result empty");
            result.setData("");
        }else {
            result.setCode("200");
            result.setMessage("success");
            result.setData(obj);
        }

        return result;
    }

    public  Result getSuccessResult() {
        Result result = new Result();
        result.setCode("200");
        result.setMessage("success");
        result.setData("");
        return result;
    }
}
