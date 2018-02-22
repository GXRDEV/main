package com.tspeiz.modules.util.json;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("deprecation")
//@JsonFilter("JacksonFilter")
public class JacksonJsonFilter {

	Map<Class<?>, Set<String>> includeMap = new HashMap<>();
	Map<Class<?>, Set<String>> filterMap = new HashMap<>();

	public void include(Class<?> type, String fields) {
		String[] _fields = fields.split(",");
		addToMap(includeMap, type, _fields);
	}

	public void filter(Class<?> type, String fields) {
		String[] _fields = fields.split(",");
		addToMap(filterMap, type, _fields);
	}

	private void addToMap(Map<Class<?>, Set<String>> map, Class<?> type,
			String[] fields) {
		Set<String> fieldSet = map.get(type);
		if (fieldSet == null)
			fieldSet = new HashSet<String>();
		fieldSet.addAll(Arrays.asList(fields));
		map.put(type, fieldSet);
	}
/*
	@Override
	public BeanPropertyFilter findFilter(Object filterId) {
		throw new UnsupportedOperationException(
				"Access to deprecated filters not supported");
	}

	@Override
	public PropertyFilter findPropertyFilter(Object filterId,
			Object valueToFilter) {

		return new SimpleBeanPropertyFilter() {

			@Override
			public void serializeAsField(Object pojo, JsonGenerator jgen,
					SerializerProvider prov, PropertyWriter writer)
					throws Exception {
				if (apply(pojo.getClass(), writer.getName())) {
					writer.serializeAsField(pojo, jgen, prov);
				} else if (!jgen.canOmitFields()) {
					writer.serializeAsOmittedField(pojo, jgen, prov);
				}
			}
		};
	}

	public boolean apply(Class<?> type, String name) {
		Set<String> includeFields = includeMap.get(type);
		Set<String> filterFields = filterMap.get(type);
		if (includeFields != null && includeFields.contains(name)) {
			return true;
		} else if (filterFields != null && !filterFields.contains(name)) {
			return true;
		} else if (includeFields == null && filterFields == null) {
			return true;
		}
		return false;
	}*/
}
