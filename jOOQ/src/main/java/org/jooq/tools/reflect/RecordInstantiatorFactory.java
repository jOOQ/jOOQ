package org.jooq.tools.reflect;

import org.jooq.Field;
import org.jooq.Record;
import org.jooq.impl.RecordImpl;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by aroger on 28/11/14.
 */
public class RecordInstantiatorFactory {


    public static <T> RecordInstantiator<T> newInstantiator(Class<T> type) throws NoSuchMethodException {

        if (type == RecordImpl.class || type == Record.class) {
            return new RecordInstantiator<T>() {
                @Override
                public T newInstance(Field<?>[] fields) throws IllegalAccessException, InvocationTargetException, InstantiationException {
                    return (T) new RecordImpl(fields);
                }
            };
        }
        Constructor<T> constructor = type.getDeclaredConstructor();
        if (!constructor.isAccessible())
            constructor.setAccessible(true);
        return new ConstructorInstantiator<T>(constructor);
    }
}
