package com.tspeiz.modules.util.date;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TimecsUtil {
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
	private static SimpleDateFormat _sdf = new SimpleDateFormat("MM");
	private static SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM");
	public static Map<String, Object> getYearMonth(int num) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		List<String> categorys = new ArrayList<String>();
		List<String> dates = new ArrayList<String>();
		// 获取当前年份
		String currYear = sdf.format(new Date());
		num = num - Integer.parseInt(_sdf.format(new Date())) + 1;
		int _num = Integer.parseInt(_sdf.format(new Date())) - 1;
		boolean hasCalLastYear = false;
		int m=0;
		for (int i = -12; i < _num; i++) {
			m++;
			Calendar calendar = Calendar.getInstance();
			//calendar.add(Calendar.DATE, i);
			calendar.add(Calendar.MONTH, i);
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH) + 1;
			dates.add(sdf2.format(sdf2.parse(year + "-" + month)));
			if (currYear.equalsIgnoreCase(year + 1 + "") && !hasCalLastYear
					&& (month% 12 == 0)) {
				// 开始上一年
				categorys.add(month + "月" + "(" + year + ")");
				hasCalLastYear = true;
				break;
			} else {
				categorys.add(month + "月");
			}
		}
		
		for (int k = -_num; k < 0; k++) {
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.MONTH, k);
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH) + 1;
			dates.add(sdf2.format(sdf2.parse(year + "-" + month)));
			categorys.add(month + "月");
		}
		dates.add(sdf2.format(new Date()));
		Calendar _calendar = Calendar.getInstance();
		_calendar.setTime(new Date());
		categorys.add((_calendar.get(Calendar.MONTH) + 1) + "月(当前)");
		map.put("categorys", categorys);
		map.put("dates", dates);
		return map;
	}
}
