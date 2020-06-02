package com.huqi.admin.entry;

import com.huqi.util.FormatUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Test {
    public static void main(String[] args) throws ParseException {


        String sq = "2020-05-14 09:25:09";
        Date formatDate = FormatUtil.getFormatDate(sq, "yyyy-MM-dd HH:mm:ss");

        //System.out.println(formatDate);

        //System.out.println(FormatUtil.getFormatDate(formatDate.toString(),"yyyy-MM-dd HH:mm:ss"));

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//注意月份是MM
        Date date = simpleDateFormat.parse(sq);
        System.out.println(date);   //Mon Sep 02 00:00:00 CST 2019
        System.out.println(simpleDateFormat.format(date));  //2019-09-02


    }
}
