package com.tspeiz.modules.util.mongodb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.MongoOptions;
import com.mongodb.util.JSON;
import com.tspeiz.modules.util.PropertiesUtil;
import com.tspeiz.modules.util.PythonVisitUtil;
import com.tspeiz.modules.util.oss.OSSConfigure;
import com.tspeiz.modules.util.oss.OSSManageUtil;

public class MongonDBUtil {
	private static Logger log = Logger.getLogger(MongoDBManager.class);
	private static Mongo mg = null;
	private static DB db = null;
	private static MongonDBUtil instance = null;
	private final static Map<String, MongonDBUtil> dbmaps = new HashMap<String, MongonDBUtil>();

	public static MongonDBUtil getInstance(String config) throws Exception {
		instance = dbmaps.get(config);
		if (instance == null) {
			instance = new MongonDBUtil();
			dbmaps.put(config, instance);
		}
		if (mg == null) {
			init();
		}
		db = mg.getDB(config);
		return instance;
	}

	private static void init() {
		String host = PropertiesUtil.getString("mongo_ip");// 主机名
		int port = PropertiesUtil.getInteger("mongo_port");// 端口
		int poolSize = new Integer(20);// 连接数量
		int blockSize = new Integer(10); // 等待队列长度
		// 其他参数根据实际情况进行添加
		try{
			mg = new Mongo(host, port);
		}catch(Exception e){
			e.printStackTrace();
		}
		MongoOptions opt = mg.getMongoOptions();
		opt.connectionsPerHost = poolSize;
		opt.threadsAllowedToBlockForConnectionMultiplier = blockSize;
	}

	// 获取表
	public static DBCollection getCollection(String collection) {
		return db.getCollection(collection);
	}

	// 插入json
	public String insert(String collection, JSONObject jobj) {
		DBObject dbObject = null;
		try {
			dbObject = (DBObject) JSON.parse(jobj.toString());
			getCollection(collection).insert(dbObject);

		} catch (MongoException e) {
			log.error("MongoException:" + e.getMessage());
		}
		return dbObject.get("_id").toString();
	}

	public String insert(String collection, JSONObject jobj, String _id) {
		DBObject dbObject = null;
		try {
			dbObject = (DBObject) JSON.parse(jobj.toString());
			System.out.println(jobj.toString());
			dbObject.put("_id", new ObjectId(_id));
			getCollection(collection).save(dbObject);

		} catch (MongoException e) {
			log.error("MongoException:" + e.getMessage());
		}
		return dbObject.get("_id").toString();
	}

	// 批量插入json数据
	public void insertBatch(String collection, JSONArray jarray) {
		if (jarray == null || jarray.isEmpty()) {
			return;
		}
		try {
			List<DBObject> listDB = new ArrayList<DBObject>();
			for (int i = 0; i < jarray.size(); i++) {
				DBObject dbObject = (DBObject) JSON.parse(jarray.getJSONObject(
						i).toString());
				listDB.add(dbObject);
			}
			getCollection(collection).insert(listDB);
		} catch (MongoException e) {
			log.error("MongoException:" + e.getMessage());
		}
	}

	public static void updateColData(String collection, String _id,
			String httpurl) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("_id", new ObjectId(_id));
		DBCollection dbCol = getCollection(collection);
		BasicDBObject queryCondition = new BasicDBObject();
		queryCondition.put("_id", new ObjectId(_id));
		MongoDBManager man = MongoDBManager.getInstance();
		DBObject res = man.findOne(collection, map);
		res.put("http_url", httpurl);
		dbCol.update(queryCondition, res, true, true);
		System.out.println("更新数据完成！");
		System.out.println("------------------------------");
	}

	// 查询所有
	public List<DBObject> findAll(String collection) {
		return getCollection(collection).find().toArray();
	}

	// 查询单个对象
	public DBObject findOne(String collection, Map<String, Object> map) {
		DBCollection coll = getCollection(collection);
		return coll.findOne(map2Obj(map));
	}

	// 查找过滤条件的所以对象
	public List<DBObject> find(String collection, Map<String, Object> map)
			throws Exception {
		DBCollection coll = getCollection(collection);
		DBCursor c = coll.find(map2Obj(map));
		if (c != null)
			return c.toArray();
		else
			return null;
	}

	// 删除
	public void delete(String collection, Map<String, Object> map) {
		DBObject obj = map2Obj(map);
		getCollection(collection).remove(obj);
	}

	public void delete(String collection, DBObject obj) {
		getCollection(collection).remove(obj);
	}

	private DBObject map2Obj(Map<String, Object> map) {
		DBObject fields = new BasicDBObject();
		if (map != null && map.size() > 0) {

			for (String key : map.keySet()) {
				fields.put(key, map.get(key));
			}
		}
		return fields;
	}

	// 清空mongodb表中相关记录
	public static void clear_mongodb_rela(String oid, String collection,
			String syncSeries) throws Exception {
		MongoDBManager manager = MongoDBManager.getInstance();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("admin_order_id", oid);
		if (StringUtils.isNotBlank(syncSeries)) {
			map.put("syncSeries_ad", syncSeries);
		}
		manager.delete(collection, map);
	}

	// 通用json数据入mongodb数据库
	public static String common_mongodb_insert(JSONObject jo,
			String collection, String oid) throws Exception {
		MongoDBManager manager = MongoDBManager.getInstance();
		jo.put("admin_order_id", oid);
		return manager.insert(collection, jo);
	}

	public static String common_mongodb_insert(JSONObject jo,
			String collection, String oid, String _id) throws Exception {
		MongoDBManager manager = MongoDBManager.getInstance();
		jo.put("admin_order_id", oid);
		return manager.insert(collection, jo, _id);
	}

	// 单条报告记录入mongodb
	public static void common_mongodb_lis_single_insert(JSONObject jo,
			String collection, String oid, String _id) throws Exception {
		Integer recordid = jo.getInt("ID");
		String ret = PythonVisitUtil.record_detail(recordid);
		JSONObject retObj = JSONObject.fromObject(ret);
		JSONObject eobj = retObj.getJSONObject("error");
		if (eobj.getString("id").equalsIgnoreCase("0")) {
			JSONObject _jo = retObj.getJSONObject("response");
			_jo.put("parent_lis", _id);
			common_mongodb_insert(_jo, collection, oid);
		}
	}

	public static void clear_mongodb_pacs_pics(String oid, String collection,
			String syncSeries) throws Exception {
		Map<String, Object> qmap = new HashMap<String, Object>();
		Map<String, Object> _qmap = null;
		qmap.put("admin_order_id", oid);
		if (StringUtils.isNotBlank(syncSeries)) {
			qmap.put("syncSeries_ad", syncSeries);
		}
		MongoDBManager manager = MongoDBManager.getInstance();
		List<DBObject> dbos = manager.find("pacs_tb", qmap);
		String parent_pacs = "";
		if (dbos != null && dbos.size() > 0) {
			for (DBObject _dbo : dbos) {
				parent_pacs = _dbo.get("_id").toString();
				_qmap = new HashMap<String, Object>();
				_qmap.put("parent_pacs", parent_pacs);
				deletePOss("pacs_sub_tb", _qmap);
				manager.delete("pacs_tb", _dbo);
			}
		}
	}

	// 删除pacs_sub_tb及oss图片信息
	public static void deletePOss(String collection, Map<String, Object> map)
			throws Exception {
		MongoDBManager manager = MongoDBManager.getInstance();
		List<DBObject> dbos = manager.find(collection, map);
		String http_url = "";
		if (dbos != null && dbos.size() > 0) {
			OSSConfigure ossConfigure = new OSSConfigure();
			for (DBObject _dbo : dbos) {
				http_url = _dbo.get("http_url").toString();
				OSSManageUtil.deleteFile(ossConfigure, http_url);
				manager.delete(collection, _dbo);
			}
		}
	}
	
	public  void  updateDataCommon(MongonDBUtil instance,String collection,String _id,Map<String,Object> params){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("_id", new ObjectId(_id));
		DBCollection dbCol = getCollection(collection);
		DBObject res = instance.findOne(collection, map);
		for(String key:params.keySet()){
			res.put(key, params.get(key));
		}
		dbCol.update(new BasicDBObject(), res, false, true);
		System.out.println("更新数据完成！");
		System.out.println("------------------------------");
	}
	

	public static void main(String[] args) throws Exception {
		//MongonDBUtil test = MongonDBUtil.getInstance("test");
		/*List<DBObject> dbos = test.findAll("lis_tb");
		System.out.println(dbos.size());*/
		MongonDBUtil test2 = MongonDBUtil.getInstance("test2");
		/*JSONObject obj = new JSONObject();
		obj.put("name", "heyongb");
		obj.put("age", "123");
		test2.insert("user", obj);*/
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("age", "20");
		test2.updateDataCommon(test2,"user", "576784893ac7502f24b01f2f",map);
		/*List<DBObject> dbos2 = test2.findAll("user");
		System.out.println(dbos2.size());*/
	}
}
