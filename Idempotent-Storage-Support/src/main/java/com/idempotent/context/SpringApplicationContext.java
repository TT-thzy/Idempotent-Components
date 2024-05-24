package com.idempotent.context;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


public class SpringApplicationContext implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringApplicationContext.applicationContext = applicationContext;
    }

    public ApplicationContext getApplicationContext() {

        return SpringApplicationContext.applicationContext;
    }


    public static ApplicationContext getApplicationContextStatic() {

        return SpringApplicationContext.applicationContext;
    }


    @SuppressWarnings("unchecked")
    public <T> T lookup(String type) {
        if (applicationContext.containsBean(type)) {

            return (T) applicationContext.getBean(type);
        }
        return null;
    }


    public <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }


    public static <T> T getBeanStatic(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) {
        return (T) applicationContext.getBean(name);
    }
}


