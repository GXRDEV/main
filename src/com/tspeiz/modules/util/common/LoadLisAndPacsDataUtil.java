package com.tspeiz.modules.util.common;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;

import net.sf.json.JSONArray;

import com.tspeiz.modules.common.bean.Groups;
import com.tspeiz.modules.common.bean.weixin.ReSourceBean;

public class LoadLisAndPacsDataUtil {
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static SimpleDateFormat date_format = new SimpleDateFormat("yyyy/MM/dd");
	private static SimpleDateFormat time_format = new SimpleDateFormat("HH:mm");

	// lis 分组
	public static List<Groups> gainLisGroups(List<ReSourceBean> records)
			throws Exception {
		List<Groups> groups = new ArrayList<Groups>();
		String dtime = "";
		String ttime = "";
		Map<String, List<ReSourceBean>> beansmap = new TreeMap<String, List<ReSourceBean>>();
		List<ReSourceBean> beans = null;
		if (records != null && records.size() > 0) {
			for (ReSourceBean bean : records) {
				Map<Object, Object> map = bean.getKvs();
				String ctime = map.get("sample_at").toString();
				dtime = date_format.format(sdf.parse(ctime));
				beans = beansmap.get(dtime);
				if (beans == null)
					beans = new ArrayList<ReSourceBean>();
				ttime = time_format.format(sdf.parse(ctime));
				bean.setRemark(ttime);
				beans.add(bean);
				beansmap.put(dtime, beans);
			}
		}
		for (String tim : beansmap.keySet()) {
			Groups g = new Groups();
			List<ReSourceBean> _beans = beansmap.get(tim);
			Collections.sort(_beans, new Comparator<ReSourceBean>() {
				public int compare(ReSourceBean o1, ReSourceBean o2) {
					// TODO Auto-generated method stub
					return o1.getRemark().compareTo(o2.getRemark());
				}
			});
			g.setBeans(_beans);
			g.setKey(tim);
			groups.add(g);
		}
		Collections.sort(groups, new Comparator<Groups>() {
			public int compare(Groups o1, Groups o2) {
				// TODO Auto-generated method stub
				return o2.getKey().compareTo(o1.getKey());
			}
		});
		System.out.println("==lis==" + JSONArray.fromObject(groups).toString());
		return groups;
	}
	

	

	// pacs 分组
	public static List<Groups> gainPacsGroup(List<ReSourceBean> pacs)
			throws Exception {
		List<Groups> groups = new ArrayList<Groups>();
		Map<String, List<ReSourceBean>> beansmap = new TreeMap<String, List<ReSourceBean>>();
		List<ReSourceBean> beans = null;
		if (pacs != null && pacs.size() > 0) {
			for (ReSourceBean bean : pacs) {
				Map<Object, Object> map = bean.getKvs();
				String modality = map.get("Modality").toString();
				beans = beansmap.get(modality);
				if (beans == null)
					beans = new ArrayList<ReSourceBean>();
				if (map.containsKey("REPORT_DATE")
						&& StringUtils.isNotBlank(map.get("REPORT_DATE")
								.toString())
						&& !map.get("REPORT_DATE").toString()
								.equalsIgnoreCase("None")) {
					bean.setRemark(date_format.format(sdf.parse(map.get(
							"REPORT_DATE").toString())));
				}

				beans.add(bean);
				beansmap.put(modality, beans);
			}
		}
		for (String _key : beansmap.keySet()) {
			Groups g = new Groups();
			g.setKey(_key);
			List<ReSourceBean> _beans = beansmap.get(_key);
			Collections.sort(_beans, new Comparator<ReSourceBean>() {
				public int compare(ReSourceBean o1, ReSourceBean o2) {
					// TODO Auto-generated method stub
					if (StringUtils.isNotBlank(o2.getRemark())
							&& StringUtils.isNotBlank(o1.getRemark()))
						return o2.getRemark().compareTo(o1.getRemark());
					return 0;
				}
			});
			g.setBeans(_beans);
			groups.add(g);
		}
		System.out.println("===groups=="
				+ JSONArray.fromObject(groups).toString());
		return groups;
	}
}
