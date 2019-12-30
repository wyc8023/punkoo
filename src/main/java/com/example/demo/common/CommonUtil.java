package com.example.demo.common;

import com.example.demo.framework.InvalidReqException;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CommonUtil {
    private final  static org.slf4j.Logger LOGGER = LoggerFactory.getLogger(CommonUtil.class);

    public static Date StringToDate(String dateString,String dateStyle) throws InvalidReqException {
    //注意：SimpleDateFormat构造函数的样式与strDate的样式必须相符
    SimpleDateFormat sDateFormat=new SimpleDateFormat(dateStyle);
    //必须捕获异常
        try {
        Date date=sDateFormat.parse(dateString);
            return date;
    } catch(ParseException px) {
            LOGGER.error("转换时间失败",px);
        throw new InvalidReqException("转换时间失败");
    }
}

    public static String deleteQuotationMarks(String dataString)  {
        if (StringUtils.isEmpty(dataString)){
            return dataString;
        }
        return dataString.replace("\"", "");
    }

    public static String dateToString(Date date,String dateStyle)  {
        //注意：SimpleDateFormat构造函数的样式与strDate的样式必须相符
        SimpleDateFormat sDateFormat=new SimpleDateFormat(dateStyle);
        //必须捕获异常
        return sDateFormat.format(date);
    }


    public static void main(String[] args) {
        // TODO Auto-generated method stub
        MyThread3 mt=new MyThread3();
        new Thread(mt).start();
        new Thread(mt).start();


    }
    static class MyThread2 implements Runnable{
        private int ticket = 5;
        public void run(){
            while(true){
                System.out.println("Runnable ticket = " + ticket--);
                if(ticket < 0){
                    break;
                }
            }
        }
    }
    static class MyThread3 extends Thread{
        private int ticket = 5;
        public void run(){
            while(true){
                System.out.println("Runnable ticket = " + ticket--);
                if(ticket < 0){
                    break;
                }
            }
        }
    }

}
