/**
 * Copyright (c) 2011-2013, Lukas Eder, lukas.eder@gmail.com
 * All rights reserved.
 *
 * This software is licensed to you under the Apache License, Version 2.0
 * (the "License"); You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * . Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * . Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * . Neither the name "jOOR" nor the names of its contributors may be
 *   used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.jooq.tools.reflect;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A wrapper for an {@link Object} or {@link Class} upon which reflective calls
 * can be made.
 * <p>
 * An example of using <code>Reflect</code> is <code><pre>
 * // Static import all reflection methods to decrease verbosity
 * import static org.joor.Reflect.*;
 *
 * // Wrap an Object / Class / class name with the on() method:
 * on("java.lang.String")
 * // Invoke constructors using the create() method:
 * .create("Hello World")
 * // Invoke methods using the call() method:
 * .call("toString")
 * // Retrieve the wrapped object
 *
 * @author Lukas Eder
 */
public class Reflect {

    // ---------------------------------------------------------------------
    // Static API used as entrance points to the fluent API
    // ---------------------------------------------------------------------

    /**
     * Wrap a class name.
     * <p>
     * This is the same as calling <code>on(Class.forName(name))</code>
     *
     * @param name A fully qualified class name
     * @return A wrapped class object, to be used for further reflection.
     * @throws ReflectException If any reflection exception occurred.
     * @see #on(Class)
     */
    public static Reflect on(String name) throws ReflectException {
        return on(forName(name));
    }

    /**
     * Wrap a class.
     * <p>
     * Use this when you want to access static fields and methods on a
     * {@link Class} object, or as a basis for constructing objects of that
     * class using {@link #create(Object...)}
     *
     * @param clazz The class to be wrapped
     * @return A wrapped class object, to be used for further reflection.
     */
    public static Reflect on(Class<?> clazz) {
        return new Reflect(clazz);
    }

    /**
     * Wrap an object.
     * <p>
     * Use this when you want to access instance fields and methods on any
     * {@link Object}
     *
     * @param object The object to be wrapped
     * @return A wrapped object, to be used for further reflection.
     */
    public static Reflect on(Object object) {
        return new Reflect(object);
    }

    /**
     * Conveniently render an {@link AccessibleObject} accessible
     *
     * @param accessible The object to render accessible
     * @return The argument object rendered accessible
     */
    public static <T extends AccessibleObject> T accessible(T accessible) {
        if (accessible == null) {
            return null;
        }

        if (!accessible.isAccessible()) {
            accessible.setAccessible(true);
        }

        return accessible;
    }

    // ---------------------------------------------------------------------
    // Members
    // ---------------------------------------------------------------------

    /**
     * The wrapped object
     */
    private final Object  object;

    /**
     * A flag indicating whether the wrapped object is a {@link Class} (for
     * accessing static fields and methods), or any other type of {@link Object}
     * (for accessing instance fields and methods).
     */
    private final boolean isClass;

    // ---------------------------------------------------------------------
    // Constructors
    // ---------------------------------------------------------------------

    private Reflect(Class<?> type) {
        this.object = type;
        this.isClass = true;
    }

    private Reflect(Object object) {
        this.object = object;
        this.isClass = false;
    }

    // ---------------------------------------------------------------------
    // Fluent Reflection API
    // ---------------------------------------------------------------------

    /**
     * Get the wrapped object
     *
     * @param <T> A convenience generic parameter for automatic unsafe casting
     */
    @SuppressWarnings("unchecked")
    public <T> T get() {
        return (T) object;
    }

    /**
     * Set a field value.
     * <p>
     * This is roughly equivalent to {@link Field#set(Object, Object)}. If the
     * wrapped object is a {@link Class}, then this will set a value to a static
     * member field. If the wrapped object is any other {@link Object}, then
     * this will set a value to an instance member field.
     *
     * @param name The field name
     * @param value The new field value
     * @return The same wrapped object, to be used for further reflection.
     * @throws ReflectException If any reflection exception occurred.
     */
    public Reflect set(String name, Object value) throws ReflectException {
        try {

            // Try setting a public field
            Field field = type().getField(name);
            field.set(object, unwrap(value));
            return this;
        }
        catch (Exception e1) {

            // Try again, setting a non-public field
            try {
                accessible(type().getDeclaredField(name)).set(object, unwrap(value));
                return this;
            }
            catch (Exception e2) {
                throw new ReflectException(e2);
            }
        }
    }

    /**
     * Get a field value.
     * <p>
     * This is roughly equivalent to {@link Field#get(Object)}. If the wrapped
     * object is a {@link Class}, then this will get a value from a static
     * member field. If the wrapped object is any other {@link Object}, then
     * this will get a value from an instance member field.
     * <p>
     * If you want to "navigate" to a wrapped version of the field, use
     * {@link #field(String)} instead.
     *
     * @param name The field name
     * @return The field value
     * @throws ReflectException If any reflection exception occurred.
     * @see #field(String)
     */
    public <T> T get(String name) throws ReflectException {
        return field(name).<T>get();
    }

    /**
     * Get a wrapped field.
     * <p>
     * This is roughly equivalent to {@link Field#get(Object)}. If the wrapped
     * object is a {@link Class}, then this will wrap a static member field. If
     * the wrapped object is any other {@link Object}, then this wrap an
     * instance member field.
     *
     * @param name The field name
     * @return The wrapped field
     * @throws ReflectException If any reflection exception occurred.
     */
    public Reflect field(String name) throws ReflectException {
        try {

             // Try getting a public field
            Field field = type().getField(name);
            return on(field.get(object));
        }
        catch (Exception e1) {

            // Try again, getting a non-public field
            try {
                return on(accessible(type().getDeclaredField(name)).get(object));
            }
            catch (Exception e2) {
                throw new ReflectException(e2);
            }
        }
    }

    /**
     * Get a Map containing field names and wrapped values for the fields'
     * values.
     * <p>
     * If the wrapped object is a {@link Class}, then this will return static
     * fields. If the wrapped object is any other {@link Object}, then this will
     * return instance fields.
     * <p>
     * These two calls are equivalent <code><pre>
     * on(object).field("myField");
     * on(object).fields().get("myField");
     * </pre></code>
     *
     * @return A map containing field names and wrapped values.
     */
    public Map<String, Reflect> fields() {
        Map<String, Reflect> result = new LinkedHashMap<String, Reflect>();

        for (Field field : type().getFields()) {
            if (!isClass ^ Modifier.isStatic(field.getModifiers())) {
                String name = field.getName();
                result.put(name, field(name));
            }
        }

        return result;
    }

    /**
     * Call a method by its name.
     * <p>
     * This is a convenience method for calling
     * <code>call(name, new Object[0])</code>
     *
     * @param name The method name
     * @return The wrapped method result or the same wrapped object if the
     *         method returns <code>void</code>, to be used for further
     *         reflection.
     * @throws ReflectException If any reflection exception occurred.
     * @see #call(String, Object...)
     */
    public Reflect call(String name) throws ReflectException {
        return call(name, new Object[0]);
    }

    /**
     * Call a method by its name.
     * <p>
     * This is roughly equivalent to {@link Method#invoke(Object, Object...)}.
     * If the wrapped object is a {@link Class}, then this will invoke a static
     * method. If the wrapped object is any other {@link Object}, then this will
     * invoke an instance method.
     * <p>
     * Just like {@link Method#invoke(Object, Object...)}, this will try to wrap
     * primitive types or unwrap primitive type wrappers if applicable. If
     * several methods are applicable, by that rule, the first one encountered
     * is called. i.e. when calling <code><pre>
     * on(...).call("method", 1, 1);
     * </pre></code> The first of the following methods will be called:
     * <code><pre>
     * public void method(int param1, Integer param2);
     * public void method(Integer param1, int param2);
     * public void method(Number param1, Number param2);
     * public void method(Number param1, Object param2);
     * public void method(int param1, Object param2);
     * </pre></code>
     *
     * @param name The method name
     * @param args The method arguments
     * @return The wrapped method result or the same wrapped object if the
     *         method returns <code>void</code>, to be used for further
     *         reflection.
     * @throws ReflectException If any reflection exception occurred.
     */
    public Reflect call(String name, Object... args) throws ReflectException {
        Class<?>[] types = types(args);

        // Try invoking the "canonical" method, i.e. the one with exact
        // matching argument types
        try {
            Method method = type().getMethod(name, types);
            return on(method, object, args);
        }

        // If there is no exact match, try to find one that has a "similar"
        // signature if primitive argument types are converted to their wrappers
        catch (NoSuchMethodException e) {
            for (Method method : type().getMethods()) {
                if (method.getName().equals(name) && match(method.getParameterTypes(), types)) {
                    return on(method, object, args);
                }
            }

            throw new ReflectException(e);
        }
    }

    /**
     * Call a constructor.
     * <p>
     * This is a convenience method for calling
     * <code>create(new Object[0])</code>
     *
     * @return The wrapped new object, to be used for further reflection.
     * @throws ReflectException If any reflection exception occurred.
     * @see #create(Object...)
     */
    public Reflect create() throws ReflectException {
        return create(new Object[0]);
    }

    /**
     * Call a constructor.
     * <p>
     * This is roughly equivalent to {@link Constructor#newInstance(Object...)}.
     * If the wrapped object is a {@link Class}, then this will create a new
     * object of that class. If the wrapped object is any other {@link Object},
     * then this will create a new object of the same type.
     * <p>
     * Just like {@link Constructor#newInstance(Object...)}, this will try to
     * wrap primitive types or unwrap primitive type wrappers if applicable. If
     * several constructors are applicable, by that rule, the first one
     * encountered is called. i.e. when calling <code><pre>
     * on(C.class).create(1, 1);
     * </pre></code> The first of the following constructors will be applied:
     * <code><pre>
     * public C(int param1, Integer param2);
     * public C(Integer param1, int param2);
     * public C(Number param1, Number param2);
     * public C(Number param1, Object param2);
     * public C(int param1, Object param2);
     * </pre></code>
     *
     * @param args The constructor arguments
     * @return The wrapped new object, to be used for further reflection.
     * @throws ReflectException If any reflection exception occurred.
     */
    public Reflect create(Object... args) throws ReflectException {
        Class<?>[] types = types(args);

        // Try invoking the "canonical" constructor, i.e. the one with exact
        // matching argument types
        try {
            Constructor<?> constructor = type().getConstructor(types);
            return on(constructor, args);
        }

        // If there is no exact match, try to find one that has a "similar"
        // signature if primitive argument types are converted to their wrappers
        catch (NoSuchMethodException e) {
            for (Constructor<?> constructor : type().getConstructors()) {
                if (match(constructor.getParameterTypes(), types)) {
                    return on(constructor, args);
                }
            }

            throw new ReflectException(e);
        }
    }

    /**
     * Create a proxy for the wrapped object allowing to typesafely invoke
     * methods on it using a custom interface
     *
     * @param proxyType The interface type that is implemented by the proxy
     * @return A proxy for the wrapped object
     */
    @SuppressWarnings("unchecked")
    public <P> P as(Class<P> proxyType) {
        final boolean isMap = (object instanceof Map);
        final InvocationHandler handler = new InvocationHandler() {
            @SuppressWarnings("null")
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                String name = method.getName();

                // Actual method name matches always come first
                try {
                    return on(object).call(name, args).get();
                }

                // [#14] Simulate POJO behaviour on wrapped map objects
                catch (ReflectException e) {
                    if (isMap) {
                        Map<String, Object> map = (Map<String, Object>) object;
                        int length = (args == null ? 0 : args.length);

                        if (length == 0 && name.startsWith("get")) {
                            return map.get(property(name.substring(3)));
                        }
                        else if (length == 0 && name.startsWith("is")) {
                            return map.get(property(name.substring(2)));
                        }
                        else if (length == 1 && name.startsWith("set")) {
                            map.put(property(name.substring(3)), args[0]);
                            return null;
                        }
                    }

                    throw e;
                }
            }
        };

        return (P) Proxy.newProxyInstance(proxyType.getClassLoader(), new Class[] { proxyType }, handler);
    }

    /**
     * Get the POJO property name of an getter/setter
     */
    private static String property(String string) {
        int length = string.length();

        if (length == 0) {
            return "";
        }
        else if (length == 1) {
            return string.toLowerCase();
        }
        else {
            return string.substring(0, 1).toLowerCase() + string.substring(1);
        }
    }

    // ---------------------------------------------------------------------
    // Object API
    // ---------------------------------------------------------------------

    /**
     * Check whether two arrays of types match, converting primitive types to
     * their corresponding wrappers.
     */
    private boolean match(Class<?>[] declaredTypes, Class<?>[] actualTypes) {
        if (declaredTypes.length == actualTypes.length) {
            for (int i = 0; i < actualTypes.length; i++) {
                if (!wrapper(declaredTypes[i]).isAssignableFrom(wrapper(actualTypes[i]))) {
                    return false;
                }
            }

            return true;
        }
        else {
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return object.hashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Reflect) {
            return object.equals(((Reflect) obj).get());
        }

        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return object.toString();
    }

    // ---------------------------------------------------------------------
    // Utility methods
    // ---------------------------------------------------------------------

    /**
     * Wrap an object created from a constructor
     */
    private static Reflect on(Constructor<?> constructor, Object... args) throws ReflectException {
        try {
            return on(constructor.newInstance(args));
        }
        catch (Exception e) {
            throw new ReflectException(e);
        }
    }

    /**
     * Wrap an object returned from a method
     */
    private static Reflect on(Method method, Object object, Object... args) throws ReflectException {
        try {
            accessible(method);

            if (method.getReturnType() == void.class) {
                method.invoke(object, args);
                return on(object);
            }
            else {
                return on(method.invoke(object, args));
            }
        }
        catch (Exception e) {
            throw new ReflectException(e);
        }
    }

    /**
     * Unwrap an object
     */
    private static Object unwrap(Object object) {
        if (object instanceof Reflect) {
            return ((Reflect) object).get();
        }

        return object;
    }

    /**
     * Get an array of types for an array of objects
     *
     * @see Object#getClass()
     */
    private static Class<?>[] types(Object... values) {
        if (values == null) {
            return new Class[0];
        }

        Class<?>[] result = new Class[values.length];

        for (int i = 0; i < values.length; i++) {
            Object value = values[i];
            result[i] = value == null ? Object.class : value.getClass();
        }

        return result;
    }

    /**
     * Load a class
     *
     * @see Class#forName(String)
     */
    private static Class<?> forName(String name) throws ReflectException {
        try {
            return Class.forName(name);
        }
        catch (Exception e) {
            throw new ReflectException(e);
        }
    }

    /**
     * Get the type of the wrapped object.
     *
     * @see Object#getClass()
     */
    public Class<?> type() {
        if (isClass) {
            return (Class<?>) object;
        }
        else {
            return object.getClass();
        }
    }

    /**
     * Get a wrapper type for a primitive type, or the argument type itself, if
     * it is not a primitive type.
     */
    public static Class<?> wrapper(Class<?> type) {
        if (type == null) {
            return null;
        }
        else if (type.isPrimitive()) {
            if (boolean.class == type) {
                return Boolean.class;
            }
            else if (int.class == type) {
                return Integer.class;
            }
            else if (long.class == type) {
                return Long.class;
            }
            else if (short.class == type) {
                return Short.class;
            }
            else if (byte.class == type) {
                return Byte.class;
            }
            else if (double.class == type) {
                return Double.class;
            }
            else if (float.class == type) {
                return Float.class;
            }
            else if (char.class == type) {
                return Character.class;
            }
            else if (void.class == type) {
                return Void.class;
            }
        }

        return type;
    }
}
