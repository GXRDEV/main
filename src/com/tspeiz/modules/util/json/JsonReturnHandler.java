package com.tspeiz.modules.util.json;

import org.springframework.core.MethodParameter;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;

public class JsonReturnHandler {
/*
    @Override
    public boolean supportsReturnType(MethodParameter returnType) {  
        // 如果有我们自定义的 JSON 注解 就用我们这个Handler 来处理
    	System.out.println("=================");
        boolean hasJsonAnno= returnType.getMethodAnnotation(JSON.class) != null;
        return hasJsonAnno;
    }*/
/*
    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest) throws Exception {
        // 设置这个就是最终的处理类了，处理完不再去找下一个类进行处理
        mavContainer.setRequestHandled(true);

        // 获得注解并执行filter方法 最后返回
        HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
        Annotation[] annos = returnType.getMethodAnnotations();
        CustomerJsonSerializer jsonSerializer = new CustomerJsonSerializer();
        List<Annotation> list=Arrays.asList(annos);
        for (Annotation annotation : list) {
        	JSON json = (JSON) annotation;
            jsonSerializer.filter(json);
        }
        
        response.setContentType("UTF-8");
        String json = jsonSerializer.toJson(returnValue);
        response.getWriter().write(json);
    }*/
}
