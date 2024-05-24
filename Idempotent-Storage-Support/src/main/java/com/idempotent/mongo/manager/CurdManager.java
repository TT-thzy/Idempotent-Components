package com.idempotent.mongo.manager;


import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Objects;

public interface CurdManager<D, PK extends Serializable> {

    D saveNew(D dto);

    D saveUpdate(D dto, PK id);

    void delete(PK id);

    D findById(PK id);

    default String getResourceName() {
        Class clazz;
        Type[] genericInterfaces = this.getClass().getGenericInterfaces();
        if (Objects.nonNull(genericInterfaces) && genericInterfaces.length > 0) {
            ParameterizedType type = (ParameterizedType) genericInterfaces[0];
            clazz = (Class) type.getActualTypeArguments()[0];
        } else {
            ParameterizedType genericSuperclass = (ParameterizedType) this.getClass().getGenericSuperclass();
            clazz = (Class) genericSuperclass.getActualTypeArguments()[0];
        }
        return clazz.getSimpleName();
    }
}
