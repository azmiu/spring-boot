package com.zhxg.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * <p>
 * CopyRright (c)20014-2016: Azmiu
 * <p>
 * Project: zhxg
 * <p>
 * Module ID: <模块类编号可以引用系统设计中的类编号>
 * <p>
 * Comments: bean工具类
 * <p>
 * JDK version used: JDK1.8
 * <p>
 * NameSpace: com.zhxg.utils.BeanTools.java
 * <p>
 * Author: azmiu
 * <p>
 * Create Date: 2016年11月14日
 * <p>
 * Modified By: <修改人中文名或拼音缩写>
 * <p>
 * Modified Date: <修改日期>
 * <p>
 * Why & What is modified: <修改原因描述>
 * <p>
 * Version: v1.0
 */
@Configuration
public class BeanTools implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    private static Logger logger = LoggerFactory.getLogger(BeanTools.class);

    /**
     * 根据beanName获取bean
     *
     * @param classname
     * @return
     * @throws Exception
     */
    public static Object getBean(String classname) {
        try {
            Object _restTemplate = applicationContext.getBean(classname);
            return _restTemplate;
        } catch (Exception e) {
            logger.error("{}获取Bean失败：" + e);
            return "";
        }
    }

    /**
     * 获取StringRedisTemplate实例
     *
     * @param clazz
     * @return
     * @throws Exception
     */
    public static StringRedisTemplate getBean(Class<StringRedisTemplate> clazz) {
        return applicationContext.getBean(clazz);

    }


    @Override
    public void setApplicationContext(ApplicationContext context) {
        applicationContext = context;
    }
}
