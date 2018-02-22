package com.tspeiz.modules.util.mongodb;

import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.util.JSON;
import com.tspeiz.modules.util.PropertiesUtil;

public class MongoDBLogUtil {
	private static Logger log = Logger.getLogger(MongoDBManager.class);
	private static Mongo mg = null;
	private static DB db = null;
	private final static MongoDBLogUtil instance = new MongoDBLogUtil();

	public static MongoDBLogUtil getInstance() throws Exception {
		return instance;
	}

	static {
		try {
			mg = new Mongo(PropertiesUtil.getString("mongo_ip"),
					PropertiesUtil.getInteger("mongo_port"));
			db = mg.getDB("mydb");
		} catch (Exception e) {
			log.error("Can't connect MongoDB!");
			e.printStackTrace();
		}
	}

	// 获取表
	public static DBCollection getCollection(String collection) {
		return db.getCollection(collection);
	}

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

	// 查询单个对象
	public DBObject findOne(String collection, Map<String, Object> map) {
		DBCollection coll = getCollection(collection);
		return coll.findOne(map2Obj(map));
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

	public static void main(String[] args) {
		MongoDBLogUtil mon=new MongoDBLogUtil();
		DBObject dbobj=mon.findOne("optlog", null);
		System.out.println(dbobj);
	}
}
