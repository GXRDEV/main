package com.tspeiz.modules.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * 时间方面工具类
 * 
 * @author heyongb
 * 
 */
public class DateUtil {
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	// 比较传送的时间参数与当前的时间相比较，如果<当前时间，则为timeOld 如果=当前时间则为current，如果>当前时间则为newtime
	public static Integer compareTimerTimeAndNow(Date timerTime) {
		Date now = new Date();
		String str = sdf.format(now);
		String str2 = sdf.format(timerTime);
		return str.compareTo(str2);
	}

	public static String generateTimerTime(Integer timePeriod,
			String periodFlag, String visitTime) {
		// 0---天为单位,1---周为单位,2---月为单位,3---年为单位
		GregorianCalendar cal = new GregorianCalendar();
		try {
			cal.setTime(sdf.parse(visitTime));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (periodFlag.equalsIgnoreCase("0")) {
			// 天计算
			cal.add(GregorianCalendar.DATE, timePeriod);
		} else if (periodFlag.equalsIgnoreCase("1")) {
			// 周计算
			cal.add(GregorianCalendar.DATE, 7 * timePeriod);
		} else if (periodFlag.equalsIgnoreCase("2")) {
			// 月计算
			cal.add(GregorianCalendar.MONTH, timePeriod);
		} else if (periodFlag.equalsIgnoreCase("3")) {
			// 年计算
			cal.add(GregorianCalendar.YEAR, timePeriod);
		}
		return sdf.format(cal.getTime());
	}

	/**
	 * 计算两个日期之间间隔的天数
	 * 
	 * @param fromDate
	 *            起始日期
	 * @param toDate
	 *            结束日期
	 * @return 间隔的天数
	 */
	public static Integer generateInterval(String fromDate, String toDate) {

		GregorianCalendar cal1 = new GregorianCalendar();
		GregorianCalendar cal2 = new GregorianCalendar();
		try {
			cal1.setTime(sdf.parse(fromDate));
			cal2.setTime(sdf.parse(toDate));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new Long((cal2.getTime().getTime() - cal1.getTime().getTime())
				/ (1000 * 60 * 60 * 24)).intValue();
	}

	private static final String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 字符串时间转LONG
	 * 
	 * @param sdate
	 * @return
	 */
	public static long string2long(String sdate) {
		if (sdate.length() < 11) {
			sdate = sdate + " 00:00:00";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_PATTERN);
		Date dt2 = null;
		try {
			dt2 = sdf.parse(sdate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		// 继续转换得到秒数的long型
		long lTime = dt2.getTime() / 1000;
		return lTime;
	}

	/**
	 * LONG时间转字符串
	 * 
	 * @param ldate
	 * @return
	 */
	public static String long2string(long ldate) {
		SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_PATTERN);
		// 前面的ldate是秒数，先乘1000得到毫秒数，再转为java.util.Date类型
		java.util.Date dt = new Date(ldate * 1000);
		String sDateTime = sdf.format(dt); // 得到精确到秒的表示
		if (sDateTime.endsWith("00:00:00")) {
			sDateTime = sDateTime.substring(0, 10);
		}
		return sDateTime;
	}
	
	public static String long2string(long ldate,String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		java.util.Date dt = new Date(ldate * 1000);
		String sDateTime = sdf.format(dt); // 得到精确到秒的表示
		return sDateTime;
	}

	public static long calculartorDateSecond(String from, String to) {
		Date d1 = null;
		Date d2 = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		try {
			d1 = sdf.parse(from);
			d2 = sdf.parse(to);
		} catch (ParseException pe) {
			System.out.println(pe.getMessage());
		}
		long dd1 = d1.getTime();
		long dd2 = d2.getTime();
		Long diff = dd2 - dd1; // 这样得到的差值是微秒级别
		Long second = diff / 1000;// 秒
		return second;
	}

	public static long calculartorDateDir(String from, String to) {
		Date d1 = null;
		Date d2 = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		try {
			d1 = sdf.parse(from);
			d2 = sdf.parse(to);
		} catch (ParseException pe) {
			System.out.println(pe.getMessage());
		}
		long dd1 = d1.getTime();
		long dd2 = d2.getTime();
		Long diff = dd2 - dd1;
		Long day = diff / (1000 * 60 * 60 * 24); // 以天数为单位取整
		Long hour = (diff / (60 * 60 * 1000) - day * 24); // 以小时为单位取整
		Long min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60); // 以分钟为单位取整
		Long second = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
		return second;
	}

	public static long calculartorDateMinute(String from, String to) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		try {
			Date now = df.parse(from);
			Date date = df.parse(to);
			long l = date.getTime() - now.getTime();
			long day = l / (24 * 60 * 60 * 1000);
			long hour = (l / (60 * 60 * 1000) - day * 24);
			long minite = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
			return hour * 60 + minite;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;

	}

	public static String calculaterChaDate(String oldTime, Integer timePeriod) {
		// 0---天为单位,1---周为单位,2---月为单位,3---年为单位
		GregorianCalendar cal = new GregorianCalendar();
		try {
			cal.setTime(sdf.parse(oldTime));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 天计算
		cal.add(GregorianCalendar.DATE, -timePeriod);
		return sdf.format(cal.getTime());
	}

	public static boolean isSameDay(Date dateA, Date dateB) {
		Calendar calDateA = Calendar.getInstance();
		calDateA.setTime(dateA);

		Calendar calDateB = Calendar.getInstance();
		calDateB.setTime(dateB);

		return calDateA.get(Calendar.YEAR) == calDateB.get(Calendar.YEAR)
				&& calDateA.get(Calendar.MONTH) == calDateB.get(Calendar.MONTH)
				&& calDateA.get(Calendar.DAY_OF_MONTH) == calDateB
						.get(Calendar.DAY_OF_MONTH);
	}

	public static long calculateSecond(String from, String to) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d1 = null;
		Date d2 = null;
		try {
			d1 = format.parse(from);
			d2 = format.parse(to);
		} catch (Exception e) {

		}
		long a = d1.getTime();
		long b = d2.getTime();
		int c = (int) ((b - a) / 1000);
		return c;
	}
	public static String timeStampToString(Timestamp ts,String pattern){  
	    String tsStr = "";   
	    DateFormat sdf = new SimpleDateFormat(pattern);   
	    try {    
	        tsStr = sdf.format(ts);     
	    } catch (Exception e) {   
	        e.printStackTrace();   
	    }  
	    return tsStr;
	}
	//根据日期判断星期几
	public static int dayForWeek(String pTime) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		c.setTime(format.parse(pTime));
		int dayForWeek = 0;
		if (c.get(Calendar.DAY_OF_WEEK) == 1) {
			dayForWeek = 0;
		} else {
			dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
		}
		return dayForWeek;
	}
	
	public static List<String> daysOfCount(Integer count) {
		 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		 Date now = new Date();
	     Calendar calendar = Calendar.getInstance();
	     calendar.setTime(now);
	     List<String> list = new ArrayList<String>();
	     for(int i = 0;i<count-1;i++){
	    	 Calendar _calendar = Calendar.getInstance();
	    	 _calendar.add(Calendar.DATE, i-count+1);
	    	 list.add(sdf.format(_calendar.getTime()));
	     }
	     list.add(sdf.format(calendar.getTime()));
		return list;
	}
}
