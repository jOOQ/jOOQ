package org.jooq.tools.reflect;

import org.jooq.Field;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by aroger on 28/11/14.
 */
public interface RecordInstantiator<T> {
    T newInstance(Field<?>[] fields) throws IllegalAccessException, InvocationTargetException, InstantiationException;
}
