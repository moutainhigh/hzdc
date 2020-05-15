package com.hzdc.mng.test;

import com.alibaba.fastjson.JSON;
import com.longan.biz.utils.Constants;
import com.longan.biz.utils.DateTool;
import org.apache.commons.lang.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class DemoTest {
    public static void main(String[] args) {
        String val = " 3  2".trim();
        boolean empty = StringUtils.isEmpty(val);
        System.out.println(empty);
        int value = val.indexOf("小大");
        System.out.println(value);
//        Date date = new Date();
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String format = simpleDateFormat.format(date);
//        try {//1589332932000
//             //1589332932
//            Date parse = simpleDateFormat.parse(" 2020-05-13 40:00:12");
//           Long time =  parse.getTime()/1000;
//            System.out.println(parse.getTime()/1000);
//            System.out.println(time- 1800);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }





//        System.out.println(format.length());
//        String substring = format.substring(11);
//        System.out.println(substring);
//        System.out.println(substring.substring(0, 2));
//        System.out.println(substring.substring(3, 5));
//        System.out.println(substring.substring(6, 8));

    }

}
