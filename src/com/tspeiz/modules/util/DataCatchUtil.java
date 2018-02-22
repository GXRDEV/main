package com.tspeiz.modules.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import redis.clients.jedis.Jedis;

import com.mongodb.DBObject;
import com.tspeiz.modules.common.bean.DisplayHelper;
import com.tspeiz.modules.common.bean.weixin.ReSourceBean;
import com.tspeiz.modules.common.entity.RemoteConsultation;
import com.tspeiz.modules.common.entity.newrelease.BusinessVedioOrder;
import com.tspeiz.modules.util.mongodb.MongoDBManager;
import com.tspeiz.modules.util.redis.RedisManageUtil;
import com.tspeiz.modules.util.redis.RedisUtil;

public class DataCatchUtil {
	private static Logger log = Logger.getLogger(DataCatchUtil.class);
	
	//保存cases数据
	/*public static void clearedCaseData(String oid,String caseid){
		//获取缓存数据
		Jedis edis=RedisUtil.getJedis();
		String response=edis.get(oid+"_cases_response");
	}*/
	
	
	// 获取lis数据列表
	@SuppressWarnings("unchecked")
	public static List<ReSourceBean> gainLisData(
			BusinessVedioOrder order) throws Exception {
		// 先从缓存里获取lis列表数据
		String cacheName = order.getId() + "_lis";
		String collection = "lis_tb";
		Jedis edis = RedisUtil.getJedis();
		String lis_cache = edis.get(cacheName);
		if (StringUtils.isNotBlank(lis_cache)) {
			RedisUtil.returnResource(edis);
			log.info("=====从缓存中获取lis数据====="+lis_cache);
			JSONArray response = JSONArray.fromObject(lis_cache);
			if (response != null && response.size() > 0) {
				Map<String,Class> classMap=new HashMap<String,Class>();
				classMap.put("beans", ReSourceBean.class);
				List<ReSourceBean> records=JSONArray.toList(response, ReSourceBean.class,classMap);
				//map.put("records", records);
				return records;
			}
		} else {
			// 从mongodb中获取数据
			log.info("====从mongodb中获取lis数据====");
			Map<String, Object> querymap = new HashMap<String, Object>();
			querymap.put("admin_order_id", order.getId()+"");
			MongoDBManager mmb = MongoDBManager.getInstance();
			List<DBObject> dbos = mmb.find(collection, querymap);
			JSONObject _jo = null;
			if (dbos != null && dbos.size() > 0) {
				List<ReSourceBean> beans = new ArrayList<ReSourceBean>();
				for (DBObject dbObject : dbos) {
					ReSourceBean rb = new ReSourceBean();
					String _id=dbObject.get("_id").toString();
					rb.setKey(_id);
					_jo = JSONObject.fromObject(dbObject);
					//查询该报告单的详细信息
					List<ReSourceBean> detal_beans=gainDetailBeans(_id,"lis_sub_tb");
					Map<Object, Object> _rmap = new HashMap<Object, Object>();
					for (Object key : _jo.keySet()) {
						if(!key.equals("_id"))_rmap.put(key, _jo.get(key));
					}
					rb.setKvs(_rmap);
					rb.setBeans(detal_beans);
					beans.add(rb);
					//map.put("records", beans);
				}
				// 缓存数据
				edis.set(cacheName, JSONArray.fromObject(beans).toString());
				RedisUtil.returnResource(edis);
				return beans;
			}
		}
		return null;
	}
	
	//获取详细报告单
	private static List<ReSourceBean> gainDetailBeans(String _id,String collection) throws Exception{
		List<ReSourceBean> beans=new ArrayList<ReSourceBean>();
		Map<String, Object> querymap = new HashMap<String, Object>();
		querymap.put("parent_lis", _id);
		MongoDBManager mmb = MongoDBManager.getInstance();
		List<DBObject> dbos = mmb.find(collection, querymap);
		JSONObject _jo = null;
		if (dbos != null && dbos.size() > 0) {
			for (DBObject dbObject : dbos) {
				ReSourceBean rb = new ReSourceBean();
				Map<Object,Object>kvs=new HashMap<Object,Object>();
				_jo=JSONObject.fromObject(dbObject);
				for (Object key : _jo.keySet()) {
					if(!key.equals("_id"))
					kvs.put(key, _jo.get(key));
				}
				rb.setKvs(kvs);
				beans.add(rb);
			}
		}
		return beans;
	}
	
	
	// 获取pacs数据列表
	@SuppressWarnings("unchecked")
	public static List<ReSourceBean> gainPacsData(
			String orderid) throws Exception {
		String cacheName = orderid + "_pacs";
		String collection = "pacs_tb";
		Jedis edis = RedisUtil.getJedis();
		String pacs_cache = edis.get(cacheName);
		if (StringUtils.isNotBlank(pacs_cache)&&!pacs_cache.equalsIgnoreCase("[]")) {
			RedisUtil.returnResource(edis);
			log.info("=====从缓存中获取pacs数据====="+pacs_cache);
			JSONArray response = JSONArray.fromObject(pacs_cache);
			if (response != null && response.size() > 0) {
				Map<String,Class> classMap=new HashMap<String,Class>();
				classMap.put("beans", ReSourceBean.class);
				List<ReSourceBean> beans =JSONArray.toList(response, ReSourceBean.class,classMap);
				return beans;
				//map.put("pac_records", beans);
			}
		} else {
			log.info("====从mongodb中获取pacs数据====");
			Map<String, Object> querymap = new HashMap<String, Object>();
			querymap.put("admin_order_id", orderid);
			MongoDBManager mmb = MongoDBManager.getInstance();
			List<DBObject> dbos = mmb.find(collection, querymap);
			JSONObject _jo = null;
			if (dbos != null && dbos.size() > 0) {
				List<ReSourceBean> beans = new ArrayList<ReSourceBean>();
				for (DBObject dbObject : dbos) {
					ReSourceBean bean = new ReSourceBean();
					_jo = JSONObject.fromObject(dbObject);
					bean.setKey(dbObject.get("_id").toString());
					bean.setStudyId(dbObject.get("study_id") == null ? ""
							: dbObject.get("study_id").toString());
					bean.setSeriesId(dbObject.get("series_id") == null ? ""
							: dbObject.get("series_id").toString());
					bean.setInstanceId(dbObject.get("instance_id") == null ? ""
							: dbObject.get("instance_id").toString());
					Map<Object, Object> kvsmap = new HashMap<Object, Object>();
					for (Object key : _jo.keySet()) {
						if(!key.equals("_id")){
							if(!_jo.get(key).toString().equalsIgnoreCase("null"))
							kvsmap.put(key, _jo.get(key));
						}
					}
					bean.setKvs(kvsmap);
					beans.add(bean);
					
				}
				//map.put("pac_records", beans);;
				System.out.println("====pacs图片信息=="+JSONArray.fromObject(beans).toString());
				edis.set(cacheName, JSONArray.fromObject(beans).toString());
				RedisUtil.returnResource(edis);
				return beans;
			}
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static List<ReSourceBean> gainPacsDataCase(
			String caseUuid) throws Exception {
		String collection = "pacs_tb";
		Map<String, Object> querymap = new HashMap<String, Object>();
		querymap.put("admin_case_id", caseUuid);
		MongoDBManager mmb = MongoDBManager.getInstance();
		List<DBObject> dbos = mmb.find(collection, querymap);
		JSONObject _jo = null;
		if (dbos != null && dbos.size() > 0) {
			List<ReSourceBean> beans = new ArrayList<ReSourceBean>();
			for (DBObject dbObject : dbos) {
				ReSourceBean bean = new ReSourceBean();
				_jo = JSONObject.fromObject(dbObject);
				bean.setKey(dbObject.get("_id").toString());
				bean.setStudyId(dbObject.get("study_id") == null ? ""
						: dbObject.get("study_id").toString());
				bean.setSeriesId(dbObject.get("series_id") == null ? ""
						: dbObject.get("series_id").toString());
				bean.setInstanceId(dbObject.get("instance_id") == null ? ""
						: dbObject.get("instance_id").toString());
				Map<Object, Object> kvsmap = new HashMap<Object, Object>();
				for (Object key : _jo.keySet()) {
					if(!key.equals("_id")){
						if(!_jo.get(key).toString().equalsIgnoreCase("null"))
						kvsmap.put(key, _jo.get(key));
					}
				}
				bean.setKvs(kvsmap);
				beans.add(bean);
				
			}
			return beans;
		}
		return null;
	}

	// 整理lis数据
	@SuppressWarnings("unchecked")
	public static void clearedLisData(String lis_ids, String oid)
			throws Exception {
		MongoDBManager manager = MongoDBManager.getInstance();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("admin_order_id", oid + "");
		if (StringUtils.isNotBlank(lis_ids)) {
			String[] _lis_ids = lis_ids.split(",");
			if (_lis_ids != null && _lis_ids.length > 0) {
				// mongodb更新
				List<DBObject> dbos = manager.find("lis_tb", map);
				for (DBObject dbObject : dbos) {
					String _id = dbObject.get("_id").toString();
					if (!contains(_id, _lis_ids)) {
						// 需要删除
						//删除子表中数据
						map.clear();
						map.put("parent_lis", _id);
						manager.delete("lis_sub_tb", map);
						//删除父表数据
						manager.delete("lis_tb", dbObject);
					}
				}
				// 缓存更新
				String cacheName = oid + "_lis";
				Jedis edis = RedisUtil.getJedis();
				String cachestr = edis.get(cacheName);
				JSONArray lisarray = JSONArray.fromObject(cachestr);
				Map<String,Class> _map=new HashMap<String,Class>();
				_map.put("beans", ReSourceBean.class);
				List<ReSourceBean> records=JSONArray.toList(lisarray, ReSourceBean.class,_map);
				records = updateRecords(_lis_ids, records);
				// 更新到缓存
				edis.set(cacheName, JSONArray.fromObject(records).toString());
				RedisUtil.returnResource(edis);
			}
		}else{
			//需要全部删除
			List<DBObject> dbos = manager.find("lis_tb", map);
			for (DBObject dbObject : dbos) {
				String _id = dbObject.get("_id").toString();
				map.clear();
				map.put("parent_lis", _id);
				manager.delete("lis_sub_tb", map);
				//删除父表数据
				manager.delete("lis_tb", dbObject);	
			}
			String cacheName = oid + "_lis";
			Jedis edis = RedisUtil.getJedis();
			edis.set(cacheName, "");
			RedisUtil.returnResource(edis);
		}
	}
	
	// 整理pacs数据
	@SuppressWarnings("unchecked")
	public static void clearedPacsData(String pacs_ids, String oid)
			throws Exception {
		MongoDBManager manager = MongoDBManager.getInstance();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("admin_order_id", oid + "");
		if (StringUtils.isNotBlank(pacs_ids)) {
			String[] _pacs_ids = pacs_ids.split(",");
			if (_pacs_ids != null && _pacs_ids.length > 0) {
				// mongodb更新
				List<DBObject> dbos = manager.find("pacs_tb", map);
				for (DBObject dbObject : dbos) {
					String _id = dbObject.get("_id").toString();
					if (!contains(_id, _pacs_ids)) {
						// 需要删除
						// 删除子表中数据
						Map<String, Object> dmap = new HashMap<String, Object>();
						dmap.put("parent_pacs", _id);
						MongoDBManager.deletePOss("pacs_sub_tb", dmap);
						// 删除主表
						manager.delete("pacs_tb", dbObject);
					}
				}
				// 缓存更新
				String cacheName = oid + "_pacs";
				Jedis edis = RedisUtil.getJedis();
				String cachestr = edis.get(cacheName);
				JSONArray pacsarray = JSONArray.fromObject(cachestr);
				List<ReSourceBean> records=JSONArray.toList(pacsarray, ReSourceBean.class);
				records = updateRecords(_pacs_ids, records);
				// 更新到缓存
				edis.set(cacheName, JSONArray.fromObject(records).toString());
				RedisUtil.returnResource(edis);
			}
		}else{
			List<DBObject> dbos = manager.find("pacs_tb", map);
			for (DBObject dbObject : dbos) {
				String _id = dbObject.get("_id").toString();
				// 需要删除
				// 删除子表中数据
				Map<String, Object> dmap = new HashMap<String, Object>();
				dmap.put("parent_pacs", _id);
				MongoDBManager.deletePOss("pacs_sub_tb", dmap);
				// 删除主表
				manager.delete("pacs_tb", dbObject);	
			}
			// 缓存更新
			String cacheName = oid + "_pacs";
			Jedis edis = RedisUtil.getJedis();
			// 更新到缓存
			edis.set(cacheName,"");
			RedisUtil.returnResource(edis);
			
		}
	}
	
	@SuppressWarnings("unchecked")
	public static void clearedPacsData_case(String pacs_ids, String caseUuid)
			throws Exception {
		MongoDBManager manager = MongoDBManager.getInstance();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("admin_case_id", caseUuid);
		if (StringUtils.isNotBlank(pacs_ids)) {
			String[] _pacs_ids = pacs_ids.split(",");
			if (_pacs_ids != null && _pacs_ids.length > 0) {
				// mongodb更新
				List<DBObject> dbos = manager.find("pacs_tb", map);
				for (DBObject dbObject : dbos) {
					String _id = dbObject.get("_id").toString();
					if (!contains(_id, _pacs_ids)) {
						// 需要删除
						// 删除子表中数据
						Map<String, Object> dmap = new HashMap<String, Object>();
						dmap.put("parent_pacs", _id);
						MongoDBManager.deletePOss("pacs_sub_tb", dmap);
						// 删除主表
						manager.delete("pacs_tb", dbObject);
					}
				}
			}
		}else{
			List<DBObject> dbos = manager.find("pacs_tb", map);
			for (DBObject dbObject : dbos) {
				String _id = dbObject.get("_id").toString();
				// 需要删除
				// 删除子表中数据
				Map<String, Object> dmap = new HashMap<String, Object>();
				dmap.put("parent_pacs", _id);
				MongoDBManager.deletePOss("pacs_sub_tb", dmap);
				// 删除主表
				manager.delete("pacs_tb", dbObject);	
			}
		}
	}

	private static List<ReSourceBean> updateRecords(String[] ars,
			List<ReSourceBean> records) {
		List<ReSourceBean> _records = new ArrayList<ReSourceBean>();
		for (ReSourceBean _record : records) {
			if (contains(_record.getKey().toString(), ars)) {
				_records.add(_record);
			}
		}
		return _records;
	}

	private static boolean contains(String _id, String[] ars) {
		for (String _str : ars) {
			if (_id.equalsIgnoreCase(_str))
				return true;
		}
		return false;
	}
}
