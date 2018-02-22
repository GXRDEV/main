package com.tspeiz.modules.util.json;


public class CustomerJsonSerializer {
	/*ObjectMapper mapper = new ObjectMapper();
	JacksonJsonFilter jacksonFilter = new JacksonJsonFilter();

	public void filter(Class<?> clazz, String include, String filter) {
		if (clazz == null)
			return;
		if (StringUtils.isNotBlank(include)) {
			jacksonFilter.include(clazz, include);
		}
		if (StringUtils.isNotBlank(filter)) {
			jacksonFilter.filter(clazz, filter);
		}
		mapper.addMixIn(clazz, jacksonFilter.getClass());
	}

	public String toJson(Object object) throws JsonProcessingException {
		mapper.setFilterProvider(jacksonFilter);
		return mapper.writeValueAsString(object);
	}

	public void filter(JSON json) {
		this.filter(json.type(), json.include(), json.filter());
	}*/
}
