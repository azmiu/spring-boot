package com.zhxg.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * <p>
 * CopyRright (c)20014-2016: Azmiu
 * <p>
 * Project: zhxg
 * <p>
 * Module ID: <模块类编号可以引用系统设计中的类编号>
 * <p>
 * Comments: 时间工具类
 * <p>
 * JDK version used: JDK1.8
 * <p>
 * NameSpace: com.zhxg.utils.DateUtil.java
 * <p>
 * Author: azmiu
 * <p>
 * Create Date: 2016年11月8日
 * <p>
 * Modified By: <修改人中文名或拼音缩写>
 * <p>
 * Modified Date: <修改日期>
 * <p>
 * Why & What is modified: <修改原因描述>
 * <p>
 * Version: v1.0
 */
public class DateUtil {

    /**
     * 特定时间增加天数
     * 
     * @return
     * @throws Exception
     */
    public static Date addDay(Date date, int day) {
        Date dd = new Date();
        Calendar g = Calendar.getInstance();
        g.setTime(dd);
        g.add(Calendar.DAY_OF_MONTH, day);
        return g.getTime();
    }

    /**
     * date转yyyyMMddhhmmss格式String类型时间
     * 
     * @param time
     * @return
     * @throws Exception
     */
    public static String dateToString(Date time) {
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat("yyyyMMdd");
        String ctime = formatter.format(time);
        return ctime;
    }

    /**
     * date转yyyyMMddhhmmss格式String类型时间
     * 
     * @param time
     * @return
     * @throws Exception
     */
    public static String dateToyyyyMMdd(Date time) {
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat("yyyy-MM-dd");
        String ctime = formatter.format(time);
        return ctime;
    }

    /**
     * 获取当前系统时间
     * 
     * @return
     * @throws Exception
     */
    public static Date getSystemDate() {
        return new Date();
    }

    public static boolean betweenAndDate(String before, String after) {
        Calendar ncalendar = Calendar.getInstance();
        // 小时
        System.out.println(ncalendar.get(Calendar.HOUR_OF_DAY));
        // 分
        System.out.println(ncalendar.get(Calendar.MINUTE));
        // hour minute
        SimpleDateFormat sim1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date1 = null;
        try {
            date1 = sim1.parse(sim1.format(new Date()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            if ((date1.before(sim1.parse(before)) && date1.after(sim1.parse(after)))) {
                return true;
            } else {
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }
}
