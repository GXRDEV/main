package com.tspeiz.modules.util.date;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatesUtil {
	private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

	// 获取下几个月的所有周几的具体日期
	public static List<String> gainNextMonthBegin(Integer num, Integer day) {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, num);
		c.set(Calendar.DATE, c.getActualMaximum(Calendar.DATE));
		String nowday = format.format(c.getTime());
		List<String> list = new ArrayList<String>();
		for (int i = 0; i <= 4 * num; i++) {
			String time = gainNextWeekBegin(i+1, day);
			if (nowday.compareTo(time) >= 0)
				list.add(time);
		}
		return list;
	}

	public static List<String> gainNextWeekDays(Integer num, Integer day) {
		List<String> list = new ArrayList<String>();
		if (num.equals(0)) {
			list.add(gainNextWeekBegin(0, day));
		} else {
			for (int i = 0; i < num; i++) {
				list.add(gainNextWeekBegin(i + 1, day));
			}
		}
		return list;
	}

	// 获取下几周的所有周几的具体日期（day:0--周一,1--周二,2--周三,3--周四,4--周五,5--周六,6--周日）
	public static String gainNextWeekBegin(Integer num, Integer day) {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.WEEK_OF_MONTH, num);
		c.add(Calendar.DAY_OF_WEEK, day);
		return format.format(c.getTime());
	}

	// 获取起始和结束日期
	public static Map<String, String> gainStartAndEndTime(Integer weeks) {
		Map<String, String> map = new HashMap<String, String>();
		String start = "";
		String end = "";
		Calendar calc = Calendar.getInstance();
		if(weeks.equals(0)){
			calc.add(Calendar.WEEK_OF_YEAR, weeks);
		}else{
			calc.add(Calendar.WEEK_OF_YEAR, 1);
		}
		
		int initDay = calc.getFirstDayOfWeek();
		calc.set(Calendar.DAY_OF_WEEK, initDay + 1);
		start = format.format(calc.getTime());
		calc.add(Calendar.DAY_OF_MONTH, 6);
		if(weeks>1){
			calc.add(Calendar.WEEK_OF_YEAR, weeks-1);
		}
		end=format.format(calc.getTime());
		map.put("start", start);
		map.put("end", end);
		return map;
	}
}
