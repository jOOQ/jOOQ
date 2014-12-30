package org.jooq.tools.reflect;

import org.jooq.Field;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by aroger on 28/11/14.
 */
public class ConstructorInstantiator<T> implements RecordInstantiator<T> {


    private final Constructor<T> constructor;

    public ConstructorInstantiator(Constructor<T> constructor) {
        this.constructor = constructor;
    }


    @Override
    public T newInstance(Field<?>[] fields) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        return constructor.newInstance();
    }
}
