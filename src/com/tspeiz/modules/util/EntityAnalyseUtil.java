package com.tspeiz.modules.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.persistence.Id;

import org.apache.log4j.Logger;

public class EntityAnalyseUtil {

	public static Logger log = Logger.getLogger(EntityAnalyseUtil.class);

	static final String tag = "EntityAnalyseUtil";

	public static void updateEntity(Object entity,
			Map<String, Object> updateFields) {
		try {
			Class<?> entity_class = entity.getClass();
			Field[] fs = entity_class.getDeclaredFields();
			for (Field f : fs) {

				if (!isIdField(f)) {

					Method set = getSetMethod(entity_class, f);
					if (set != null) {

						Class<?> type = f.getType();

						if (updateFields.containsKey(f.getName())) {
							Object value = updateFields.get(f.getName());

							if (type == String.class) {
								set.invoke(entity,
										value == null ? null : value.toString());

							} else if (type == int.class
									|| type == Integer.class) {
								set.invoke(entity, value == null ? null
										: Integer.parseInt(value.toString()));
							} else if (type == float.class
									|| type == Float.class) {
								set.invoke(
										entity,
										value == null ? null : Float
												.parseFloat(value.toString()));
							} else if (type == long.class || type == Long.class) {
								set.invoke(
										entity,
										value == null ? null : Long
												.parseLong(value.toString()));
							} else if (type == Date.class) {
								set.invoke(entity, value == null ? null
										: stringToDateTime(value.toString()));
							} else {
								set.invoke(entity, value);
							}
						}
					}
				}

			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	public static String getFilterSql(Class<?> entity_class,
			Map<String, Object> conditionMap) {
		try {

			if (conditionMap == null) {
				return null;
			}

			StringBuilder sqlBuilder = new StringBuilder();
			Field[] fs = entity_class.getDeclaredFields();
			for (Field f : fs) {

				Class<?> type = f.getType();
				if (conditionMap.containsKey(f.getName())) {

					Object value = conditionMap.get(f.getName());

					if (type == String.class) {

						if (sqlBuilder.length() != 0) {

							sqlBuilder.append(" AND ");
						}

						if (value == null) {
							sqlBuilder.append(f.getName() + " IS NULL");
						} else {
							sqlBuilder.append(f.getName() + "='"
									+ value.toString() + "'");
						}

					} else if (type == int.class || type == Integer.class
							|| type == float.class || type == Float.class
							|| type == long.class || type == Long.class) {

						if (sqlBuilder.length() != 0) {

							sqlBuilder.append(" AND ");
						}

						if (value == null) {
							sqlBuilder.append(f.getName() + " IS NULL");
						} else {
							sqlBuilder.append(f.getName() + "="
									+ value.toString());

						}

						// 有待处理
					} else if (type == Date.class) {

						if (sqlBuilder.length() != 0) {
							sqlBuilder.append(" AND ");
						}

						if (value == null) {
							sqlBuilder.append(f.getName() + " IS NULL");
						} else {
							sqlBuilder.append(f.getName() + ">='"
									+ value.toString() + "'");
						}

					} else {
						if (sqlBuilder.length() != 0) {

							sqlBuilder.append(" AND ");
						}

						if (value == null) {
							sqlBuilder.append(f.getName() + " IS NULL");
						} else {
							sqlBuilder.append(f.getName() + "='"
									+ value.toString() + "'");
						}

					}
				}

			}

			return sqlBuilder.toString();

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return null;
	}

	private static boolean isIdField(Field f) {
		Annotation an = f.getAnnotation(Id.class);
		if (an != null) {
			return true;
		}
		return false;
	}

	private static Method getGetMethod(Class<?> entity_class, Field f) {
		String fn = f.getName();
		String mn = "get" + fn.substring(0, 1).toUpperCase() + fn.substring(1);
		try {
			return entity_class.getDeclaredMethod(mn);
		} catch (NoSuchMethodException e) {
			log.error(tag + "Method: " + mn + " not found.");

			return null;
		}
	}

	private static Method getSetMethod(Class<?> entity_class, Field f) {
		String fn = f.getName();
		String mn = "set" + fn.substring(0, 1).toUpperCase() + fn.substring(1);
		try {
			return entity_class.getDeclaredMethod(mn, f.getType());
		} catch (NoSuchMethodException e) {
			log.error(tag + "Method: " + mn + " not found.");

			return null;
		}
	}

	private static Date stringToDateTime(String s) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (s != null) {
			try {
				return sdf.parse(s);
			} catch (ParseException e) {
				log.error(tag + "����ʱ�����: " + s, e);
			}
		}
		return null;
	}

	private static String dateTimeToDateString(Date d) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (d != null) {
			try {
				return sdf.format(d);
			} catch (Exception e) {
				log.error(tag + "����ʱ�����: " + d.toString(), e);
			}
		}
		return null;
	}

}
