package com.huqi.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FormatUtil {

    /**
     * 字符串转Date , 2020-05-14 09:25:09 转 Thu May 14 09:25:09 CST 2020
     * @param date
     * @param format
     * @return
     */
    public static Date getFormatDate(String date, String format){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Date date1 = null;

        try {
            date1 = simpleDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date1;
    }

    public static String getDateFormatString(Date date, String format){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(date);

    }

}
