package com.tspeiz.modules.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringUtil { 
    /** Spring框架应用上下文对象 */  
    private static ApplicationContext factory = getApplicationContext();  
      
    static{  
        getApplicationContext();  
    }  
      
    public static void setFactoryBean(ApplicationContext factory){  
        SpringUtil.factory = factory;  
    }  
    /** 
     * 获得Spring框架应用上下文对象  
     * @return ApplicationContext 
     */  
    public static ApplicationContext getApplicationContext()  
    {  
        //判断如果 ApplicationContext 的对象 ＝＝ NULL  
        if ( factory == null )  
        {    
            try  
            {    
                factory = new ClassPathXmlApplicationContext(new String[]{"applicationContext.xml"  
                        ,"pacsconfig.xml"  
                        });  
            }  
            catch ( Exception e1 )  
            {  
                
                e1.printStackTrace();  
            }  
        }  
        //返回ApplicationContext  
        return factory;  
    }
}
