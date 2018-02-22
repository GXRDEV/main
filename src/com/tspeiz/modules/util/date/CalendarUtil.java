package com.tspeiz.modules.util.date;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.tspeiz.modules.util.DateUtil;

public class CalendarUtil {

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	public static List<String> gaintimes(String start, String end,
			Integer weekday) throws Exception {
		Calendar cal_start = Calendar.getInstance();
		cal_start.setTime(sdf.parse(start));
		Calendar cal_end = Calendar.getInstance();
		cal_end.setTime(sdf.parse(end));
		CalendarUtil cu = new CalendarUtil();
		List<Date> dates = cu.getDates(cal_start, cal_end);
		List<String> times = new ArrayList<String>();
		List<String> _times = new ArrayList<String>();
		times.add(start);
		for (Date date : dates) {
			times.add(sdf.format(date));
		}
		times.add(end);
		Integer dw = null;
		for (String string : times) {
			dw = DateUtil.dayForWeek(string);
			if (dw.equals(weekday))
				_times.add(string);
		}
		return _times;
	}

	@SuppressWarnings("static-access")
	public List<Date> getDates(Calendar p_start, Calendar p_end) {
		List<Date> result = new ArrayList<Date>();
		Calendar temp = p_start.getInstance();
		temp.add(Calendar.DAY_OF_YEAR, 1);
		while (temp.before(p_end)) {
			result.add(temp.getTime());
			temp.add(Calendar.DAY_OF_YEAR, 1);
		}
		return result;
	}

	public static String gainGroupWeekDates(Integer n) {
		// n为推迟的周数，1本周，-1向前推迟一周，2下周，依次类推
		Calendar cal = Calendar.getInstance();
		Integer _n = 0;
		if (n == 1) {
			return gainCurrentWeekDate(cal);
		} else {
			_n = n - 1;
			cal.add(Calendar.DATE, -_n * 7);
			return gainCurrentWeekDate(cal);
		}
	}
	

	public static String gainCurrentWeekDate(Calendar cal) {
		String datestr = "";
		int day_of_week = cal.get(Calendar.DAY_OF_WEEK) - 2;
		cal.add(Calendar.DATE, -day_of_week);
		String begin = sdf.format(cal.getTime());
		cal.add(Calendar.DATE, 6);
		String end = sdf.format(cal.getTime());
		datestr = begin + ";" + end;
		return datestr;
	}

	public static Map<String, Object> gainWeeksData(Integer n) {
		Map<String, Object> _map = new HashMap<String, Object>();
		List<String> categorys = new ArrayList<String>();
		List<String> dates = new ArrayList<String>();
		for (int i = n; i >= 1; i--) {
			if (i == 1) {
				categorys.add("本周");
			} else {
				categorys.add("前" + i + "周");
			}
			dates.add(gainGroupWeekDates(i));
		}
		_map.put("categorys", categorys);
		_map.put("dates", dates);
		return _map;
	}
	
	public static String gainCurrentMonthDate(){
		Calendar cal = Calendar.getInstance();
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH));
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
		String begin=sdf.format(cal.getTime());
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		String end=sdf.format(cal.getTime());
		return begin+";"+end;
	}

	public static void main(String[] args) {
		/*
		 * System.out.println("===");
		 * System.out.println(JSONArray.fromObject(gainWeeksData
		 * (12).get("categorys")).toString());
		 * System.out.println(JSONArray.fromObject
		 * (gainWeeksData(12).get("dates")).toString());
		 */
		gainCurrentMonthDate();
	}

}