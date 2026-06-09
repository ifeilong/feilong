/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.feilong.lib.lang3;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Operates on classes without using reflection.
 * </p>
 *
 * <p>
 * This class handles invalid {@code null} inputs as best it can.
 * Each method documents its behaviour in more detail.
 * </p>
 *
 * <p>
 * The notion of a {@code canonical name} includes the human
 * readable name for the type, for example {@code int[]}. The
 * non-canonical method variants work with the JVM names, such as
 * {@code [I}.
 * </p>
 *
 * @since 2.0
 */
public class ClassUtils{

    private static final String                EMPTY                      = "";

    /**
     * The package separator character: {@code '&#x2e;' == {@value}}.
     */
    public static final char                   PACKAGE_SEPARATOR_CHAR     = '.';

    /**
     * The package separator String: {@code "&#x2e;"}.
     */
    public static final String                 PACKAGE_SEPARATOR          = String.valueOf(PACKAGE_SEPARATOR_CHAR);

    /**
     * The inner class separator character: {@code '$' == {@value}}.
     */
    public static final char                   INNER_CLASS_SEPARATOR_CHAR = '$';

    /**
     * The inner class separator String: {@code "$"}.
     */
    public static final String                 INNER_CLASS_SEPARATOR      = String.valueOf(INNER_CLASS_SEPARATOR_CHAR);

    /**
     * Maps names of primitives to their corresponding primitive {@code Class}es.
     */
    private static final Map<String, Class<?>> namePrimitiveMap           = new HashMap<>();
    static{
        namePrimitiveMap.put("boolean", Boolean.TYPE);
        namePrimitiveMap.put("byte", Byte.TYPE);
        namePrimitiveMap.put("char", Character.TYPE);
        namePrimitiveMap.put("short", Short.TYPE);
        namePrimitiveMap.put("int", Integer.TYPE);
        namePrimitiveMap.put("long", Long.TYPE);
        namePrimitiveMap.put("double", Double.TYPE);
        namePrimitiveMap.put("float", Float.TYPE);
        namePrimitiveMap.put("void", Void.TYPE);
    }

    /**
     * Maps primitive {@code Class}es to their corresponding wrapper {@code Class}.
     */
    private static final Map<Class<?>, Class<?>> primitiveWrapperMap = new HashMap<>();
    static{
        primitiveWrapperMap.put(Boolean.TYPE, Boolean.class);
        primitiveWrapperMap.put(Byte.TYPE, Byte.class);
        primitiveWrapperMap.put(Character.TYPE, Character.class);
        primitiveWrapperMap.put(Short.TYPE, Short.class);
        primitiveWrapperMap.put(Integer.TYPE, Integer.class);
        primitiveWrapperMap.put(Long.TYPE, Long.class);
        primitiveWrapperMap.put(Double.TYPE, Double.class);
        primitiveWrapperMap.put(Float.TYPE, Float.class);
        primitiveWrapperMap.put(Void.TYPE, Void.TYPE);
    }

    /**
     * Maps wrapper {@code Class}es to their corresponding primitive types.
     */
    private static final Map<Class<?>, Class<?>> wrapperPrimitiveMap = new HashMap<>();
    static{
        for (final Map.Entry<Class<?>, Class<?>> entry : primitiveWrapperMap.entrySet()){
            final Class<?> primitiveClass = entry.getKey();
            final Class<?> wrapperClass = entry.getValue();
            if (!primitiveClass.equals(wrapperClass)){
                wrapperPrimitiveMap.put(wrapperClass, primitiveClass);
            }
        }
    }

    /**
     * Maps a primitive class name to its corresponding abbreviation used in array class names.
     */
    private static final Map<String, String> abbreviationMap;

    /**
     * Maps an abbreviation used in array class names to corresponding primitive class name.
     */
    private static final Map<String, String> reverseAbbreviationMap;
    // Feed abbreviation maps
    static{
        final Map<String, String> m = new HashMap<>();
        m.put("int", "I");
        m.put("boolean", "Z");
        m.put("float", "F");
        m.put("long", "J");
        m.put("short", "S");
        m.put("byte", "B");
        m.put("double", "D");
        m.put("char", "C");
        final Map<String, String> r = new HashMap<>();
        for (final Map.Entry<String, String> e : m.entrySet()){
            r.put(e.getValue(), e.getKey());
        }
        abbreviationMap = Collections.unmodifiableMap(m);
        reverseAbbreviationMap = Collections.unmodifiableMap(r);
    }

    /**
     * <p>
     * ClassUtils instances should NOT be constructed in standard programming.
     * Instead, the class should be used as
     * {@code ClassUtils.getShortClassName(cls)}.
     * </p>
     *
     * <p>
     * This constructor is public to permit tools that require a JavaBean
     * instance to operate.
     * </p>
     */
    public ClassUtils(){
        super();
    }

    // Short class name

    /**
     * <p>
     * Gets the class name minus the package name from a {@code Class}.
     * </p>
     *
     * <p>
     * This method simply gets the name using {@code Class.getName()} and then calls
     * {@link #getShortClassName(Class)}. See relevant notes there.
     * </p>
     *
     * @param cls
     *            the class to get the short name for.
     * @return the class name without the package name or an empty string. If the class
     *         is an inner class then the returned value will contain the outer class
     *         or classes separated with {@code .} (dot) character.
     */
    public static String getShortClassName(final Class<?> cls){
        if (cls == null){
            return EMPTY;
        }
        return getShortClassName(cls.getName());
    }

    /**
     * <p>
     * Gets the class name minus the package name from a String.
     * </p>
     *
     * <p>
     * The string passed in is assumed to be a class name - it is not checked. The string has to be formatted the way
     * as the JDK method {@code Class.getName()} returns it, and not the usual way as we write it, for example in import
     * statements, or as it is formatted by {@code Class.getCanonicalName()}.
     * </p>
     *
     * <p>
     * The difference is is significant only in case of classes that are inner classes of some other
     * classes. In this case the separator between the outer and inner class (possibly on multiple hierarchy level) has
     * to be {@code $} (dollar sign) and not {@code .} (dot), as it is returned by {@code Class.getName()}
     * </p>
     *
     * <p>
     * Note that this method is called from the {@link #getShortClassName(Class)} method using the string
     * returned by {@code Class.getName()}.
     * </p>
     *
     * <p>
     * Note that this method differs from {@link #getSimpleName(Class)} in that this will
     * return, for example {@code "Map.Entry"} whilst the {@code java.lang.Class} variant will simply
     * return {@code "Entry"}. In this example the argument {@code className} is the string
     * {@code java.util.Map$Entry} (note the {@code $} sign.
     * </p>
     *
     * @param className
     *            the className to get the short name for. It has to be formatted as returned by
     *            {@code Class.getName()} and not {@code Class.getCanonicalName()}
     * @return the class name of the class without the package name or an empty string. If the class is
     *         an inner class then value contains the outer class or classes and the separator is replaced
     *         to be {@code .} (dot) character.
     */
    public static String getShortClassName(String className){
        if (StringUtils.isEmpty(className)){
            return EMPTY;
        }

        final StringBuilder arrayPrefix = new StringBuilder();

        // Handle array encoding
        if (className.startsWith("[")){
            while (className.charAt(0) == '['){
                className = className.substring(1);
                arrayPrefix.append("[]");
            }
            // Strip Object type encoding
            if (className.charAt(0) == 'L' && className.charAt(className.length() - 1) == ';'){
                className = className.substring(1, className.length() - 1);
            }

            if (reverseAbbreviationMap.containsKey(className)){
                className = reverseAbbreviationMap.get(className);
            }
        }

        final int lastDotIdx = className.lastIndexOf(PACKAGE_SEPARATOR_CHAR);
        final int innerIdx = className.indexOf(INNER_CLASS_SEPARATOR_CHAR, lastDotIdx == -1 ? 0 : lastDotIdx + 1);
        String out = className.substring(lastDotIdx + 1);
        if (innerIdx != -1){
            out = out.replace(INNER_CLASS_SEPARATOR_CHAR, PACKAGE_SEPARATOR_CHAR);
        }
        return out + arrayPrefix;
    }

    /**
     * <p>
     * Null-safe version of {@code cls.getSimpleName()}
     * </p>
     *
     * @param cls
     *            the class for which to get the simple name; may be null
     * @return the simple class name or the empty string in case the argument is {@code null}
     * @since 3.0
     * @see Class#getSimpleName()
     */
    public static String getSimpleName(final Class<?> cls){
        return getSimpleName(cls, EMPTY);
    }

    /**
     * <p>
     * Null-safe version of {@code cls.getSimpleName()}
     * </p>
     *
     * @param cls
     *            the class for which to get the simple name; may be null
     * @param valueIfNull
     *            the value to return if null
     * @return the simple class name or {@code valueIfNull} if the
     *         argument {@code cls} is {@code null}
     * @since 3.0
     * @see Class#getSimpleName()
     */
    public static String getSimpleName(final Class<?> cls,final String valueIfNull){
        return cls == null ? valueIfNull : cls.getSimpleName();
    }

    // Superclasses/Superinterfaces
    // ----------------------------------------------------------------------
    /**
     * <p>
     * Gets a {@code List} of superclasses for the given class.
     * </p>
     *
     * @param cls
     *            the class to look up, may be {@code null}
     * @return the {@code List} of superclasses in order going up from this one
     *         {@code null} if null input
     */
    public static List<Class<?>> getAllSuperclasses(final Class<?> cls){
        if (cls == null){
            return null;
        }
        final List<Class<?>> classes = new ArrayList<>();
        Class<?> superclass = cls.getSuperclass();
        while (superclass != null){
            classes.add(superclass);
            superclass = superclass.getSuperclass();
        }
        return classes;
    }

    /**
     * <p>
     * Gets a {@code List} of all interfaces implemented by the given
     * class and its superclasses.
     * </p>
     *
     * <p>
     * The order is determined by looking through each interface in turn as
     * declared in the source file and following its hierarchy up. Then each
     * superclass is considered in the same way. Later duplicates are ignored,
     * so the order is maintained.
     * </p>
     *
     * @param cls
     *            the class to look up, may be {@code null}
     * @return the {@code List} of interfaces in order,
     *         {@code null} if null input
     */
    public static List<Class<?>> getAllInterfaces(final Class<?> cls){
        if (cls == null){
            return null;
        }

        final LinkedHashSet<Class<?>> interfacesFound = new LinkedHashSet<>();
        getAllInterfaces(cls, interfacesFound);

        return new ArrayList<>(interfacesFound);
    }

    /**
     * Gets the interfaces for the specified class.
     *
     * @param cls
     *            the class to look up, may be {@code null}
     * @param interfacesFound
     *            the {@code Set} of interfaces for the class
     */
    private static void getAllInterfaces(Class<?> cls,final HashSet<Class<?>> interfacesFound){
        while (cls != null){
            final Class<?>[] interfaces = cls.getInterfaces();

            for (final Class<?> i : interfaces){
                if (interfacesFound.add(i)){
                    getAllInterfaces(i, interfacesFound);
                }
            }

            cls = cls.getSuperclass();
        }
    }

    /**
     * <p>
     * Checks if an array of Classes can be assigned to another array of Classes.
     * </p>
     *
     * <p>
     * This method calls {@link #isAssignable(Class, Class) isAssignable} for each
     * Class pair in the input arrays. It can be used to check if a set of arguments
     * (the first parameter) are suitably compatible with a set of method parameter types
     * (the second parameter).
     * </p>
     *
     * <p>
     * Unlike the {@link Class#isAssignableFrom(java.lang.Class)} method, this
     * method takes into account widenings of primitive classes and
     * {@code null}s.
     * </p>
     *
     * <p>
     * Primitive widenings allow an int to be assigned to a {@code long},
     * {@code float} or {@code double}. This method returns the correct
     * result for these cases.
     * </p>
     *
     * <p>
     * {@code Null} may be assigned to any reference type. This method will
     * return {@code true} if {@code null} is passed in and the toClass is
     * non-primitive.
     * </p>
     *
     * <p>
     * Specifically, this method tests whether the type represented by the
     * specified {@code Class} parameter can be converted to the type
     * represented by this {@code Class} object via an identity conversion
     * widening primitive or widening reference conversion. See
     * <em><a href="http://docs.oracle.com/javase/specs/">The Java Language Specification</a></em>,
     * sections 5.1.1, 5.1.2 and 5.1.4 for details.
     * </p>
     *
     * @param classArray
     *            the array of Classes to check, may be {@code null}
     * @param toClassArray
     *            the array of Classes to try to assign into, may be {@code null}
     * @param autoboxing
     *            whether to use implicit autoboxing/unboxing between primitives and wrappers
     * @return {@code true} if assignment possible
     */
    public static boolean isAssignable(Class<?>[] classArray,Class<?>[] toClassArray,final boolean autoboxing){
        if (!ArrayUtils.isSameLength(classArray, toClassArray)){
            return false;
        }
        if (classArray == null){
            classArray = ArrayUtils.EMPTY_CLASS_ARRAY;
        }
        if (toClassArray == null){
            toClassArray = ArrayUtils.EMPTY_CLASS_ARRAY;
        }
        for (int i = 0; i < classArray.length; i++){
            if (!isAssignable(classArray[i], toClassArray[i], autoboxing)){
                return false;
            }
        }
        return true;
    }

    /**
     * Returns whether the given {@code type} is a primitive or primitive wrapper ({@link Boolean}, {@link Byte}, {@link Character},
     * {@link Short}, {@link Integer}, {@link Long}, {@link Double}, {@link Float}).
     *
     * @param type
     *            The class to query or null.
     * @return true if the given {@code type} is a primitive or primitive wrapper ({@link Boolean}, {@link Byte}, {@link Character},
     *         {@link Short}, {@link Integer}, {@link Long}, {@link Double}, {@link Float}).
     * @since 3.1
     */
    public static boolean isPrimitiveOrWrapper(final Class<?> type){
        if (type == null){
            return false;
        }
        return type.isPrimitive() || isPrimitiveWrapper(type);
    }

    /**
     * Returns whether the given {@code type} is a primitive wrapper ({@link Boolean}, {@link Byte}, {@link Character}, {@link Short},
     * {@link Integer}, {@link Long}, {@link Double}, {@link Float}).
     *
     * @param type
     *            The class to query or null.
     * @return true if the given {@code type} is a primitive wrapper ({@link Boolean}, {@link Byte}, {@link Character}, {@link Short},
     *         {@link Integer}, {@link Long}, {@link Double}, {@link Float}).
     * @since 3.1
     */
    public static boolean isPrimitiveWrapper(final Class<?> type){
        return wrapperPrimitiveMap.containsKey(type);
    }

    /**
     * <p>
     * Checks if one {@code Class} can be assigned to a variable of
     * another {@code Class}.
     * </p>
     *
     * <p>
     * Unlike the {@link Class#isAssignableFrom(java.lang.Class)} method,
     * this method takes into account widenings of primitive classes and
     * {@code null}s.
     * </p>
     *
     * <p>
     * Primitive widenings allow an int to be assigned to a long, float or
     * double. This method returns the correct result for these cases.
     * </p>
     *
     * <p>
     * {@code Null} may be assigned to any reference type. This method
     * will return {@code true} if {@code null} is passed in and the
     * toClass is non-primitive.
     * </p>
     *
     * <p>
     * Specifically, this method tests whether the type represented by the
     * specified {@code Class} parameter can be converted to the type
     * represented by this {@code Class} object via an identity conversion
     * widening primitive or widening reference conversion. See
     * <em><a href="http://docs.oracle.com/javase/specs/">The Java Language Specification</a></em>,
     * sections 5.1.1, 5.1.2 and 5.1.4 for details.
     * </p>
     *
     * <p>
     * <strong>Since Lang 3.0,</strong> this method will default behavior for
     * calculating assignability between primitive and wrapper types <em>corresponding
     * to the running Java version</em>; i.e. autoboxing will be the default
     * behavior in VMs running Java versions &gt; 1.5.
     * </p>
     *
     * @param cls
     *            the Class to check, may be null
     * @param toClass
     *            the Class to try to assign into, returns false if null
     * @return {@code true} if assignment possible
     */
    public static boolean isAssignable(final Class<?> cls,final Class<?> toClass){
        return isAssignable(cls, toClass, true);
    }

    /**
     * <p>
     * Checks if one {@code Class} can be assigned to a variable of
     * another {@code Class}.
     * </p>
     *
     * <p>
     * Unlike the {@link Class#isAssignableFrom(java.lang.Class)} method,
     * this method takes into account widenings of primitive classes and
     * {@code null}s.
     * </p>
     *
     * <p>
     * Primitive widenings allow an int to be assigned to a long, float or
     * double. This method returns the correct result for these cases.
     * </p>
     *
     * <p>
     * {@code Null} may be assigned to any reference type. This method
     * will return {@code true} if {@code null} is passed in and the
     * toClass is non-primitive.
     * </p>
     *
     * <p>
     * Specifically, this method tests whether the type represented by the
     * specified {@code Class} parameter can be converted to the type
     * represented by this {@code Class} object via an identity conversion
     * widening primitive or widening reference conversion. See
     * <em><a href="http://docs.oracle.com/javase/specs/">The Java Language Specification</a></em>,
     * sections 5.1.1, 5.1.2 and 5.1.4 for details.
     * </p>
     *
     * @param cls
     *            the Class to check, may be null
     * @param toClass
     *            the Class to try to assign into, returns false if null
     * @param autoboxing
     *            whether to use implicit autoboxing/unboxing between primitives and wrappers
     * @return {@code true} if assignment possible
     */
    public static boolean isAssignable(Class<?> cls,final Class<?> toClass,final boolean autoboxing){
        if (toClass == null){
            return false;
        }
        // have to check for null, as isAssignableFrom doesn't
        if (cls == null){
            return !toClass.isPrimitive();
        }
        //autoboxing:
        if (autoboxing){
            if (cls.isPrimitive() && !toClass.isPrimitive()){
                cls = primitiveToWrapper(cls);
                if (cls == null){
                    return false;
                }
            }
            if (toClass.isPrimitive() && !cls.isPrimitive()){
                cls = wrapperToPrimitive(cls);
                if (cls == null){
                    return false;
                }
            }
        }
        if (cls.equals(toClass)){
            return true;
        }
        if (cls.isPrimitive()){
            if (!toClass.isPrimitive()){
                return false;
            }
            if (Integer.TYPE.equals(cls)){
                return Long.TYPE.equals(toClass) || Float.TYPE.equals(toClass) || Double.TYPE.equals(toClass);
            }
            if (Long.TYPE.equals(cls)){
                return Float.TYPE.equals(toClass) || Double.TYPE.equals(toClass);
            }
            if (Boolean.TYPE.equals(cls)){
                return false;
            }
            if (Double.TYPE.equals(cls)){
                return false;
            }
            if (Float.TYPE.equals(cls)){
                return Double.TYPE.equals(toClass);
            }
            if (Character.TYPE.equals(cls)){
                return Integer.TYPE.equals(toClass) || Long.TYPE.equals(toClass) || Float.TYPE.equals(toClass)
                                || Double.TYPE.equals(toClass);
            }
            if (Short.TYPE.equals(cls)){
                return Integer.TYPE.equals(toClass) || Long.TYPE.equals(toClass) || Float.TYPE.equals(toClass)
                                || Double.TYPE.equals(toClass);
            }
            if (Byte.TYPE.equals(cls)){
                return Short.TYPE.equals(toClass) || Integer.TYPE.equals(toClass) || Long.TYPE.equals(toClass) || Float.TYPE.equals(toClass)
                                || Double.TYPE.equals(toClass);
            }
            // should never get here
            return false;
        }
        return toClass.isAssignableFrom(cls);
    }

    /**
     * <p>
     * Converts the specified primitive Class object to its corresponding
     * wrapper Class object.
     * </p>
     *
     * <p>
     * NOTE: From v2.2, this method handles {@code Void.TYPE},
     * returning {@code Void.TYPE}.
     * </p>
     *
     * @param cls
     *            the class to convert, may be null
     * @return the wrapper class for {@code cls} or {@code cls} if
     *         {@code cls} is not a primitive. {@code null} if null input.
     * @since 2.1
     */
    public static Class<?> primitiveToWrapper(final Class<?> cls){
        Class<?> convertedClass = cls;
        if (cls != null && cls.isPrimitive()){
            convertedClass = primitiveWrapperMap.get(cls);
        }
        return convertedClass;
    }

    /**
     * <p>
     * Converts the specified wrapper class to its corresponding primitive
     * class.
     * </p>
     *
     * <p>
     * This method is the counter part of {@code primitiveToWrapper()}.
     * If the passed in class is a wrapper class for a primitive type, this
     * primitive type will be returned (e.g. {@code Integer.TYPE} for
     * {@code Integer.class}). For other classes, or if the parameter is
     * <b>null</b>, the return value is <b>null</b>.
     * </p>
     *
     * @param cls
     *            the class to convert, may be <b>null</b>
     * @return the corresponding primitive type if {@code cls} is a
     *         wrapper class, <b>null</b> otherwise
     * @see #primitiveToWrapper(Class)
     * @since 2.4
     */
    public static Class<?> wrapperToPrimitive(final Class<?> cls){
        return wrapperPrimitiveMap.get(cls);
    }

    // Class loading
    // ----------------------------------------------------------------------
    /**
     * Returns the class represented by {@code className} using the
     * {@code classLoader}. This implementation supports the syntaxes
     * "{@code java.util.Map.Entry[]}", "{@code java.util.Map$Entry[]}",
     * "{@code [Ljava.util.Map.Entry;}", and "{@code [Ljava.util.Map$Entry;}".
     *
     * @param classLoader
     *            the class loader to use to load the class
     * @param className
     *            the class name
     * @param initialize
     *            whether the class must be initialized
     * @return the class represented by {@code className} using the {@code classLoader}
     * @throws ClassNotFoundException
     *             if the class is not found
     */
    public static Class<?> getClass(final ClassLoader classLoader,final String className,final boolean initialize)
                    throws ClassNotFoundException{
        try{
            Class<?> clazz;
            if (namePrimitiveMap.containsKey(className)){
                clazz = namePrimitiveMap.get(className);
            }else{
                clazz = Class.forName(toCanonicalName(className), initialize, classLoader);
            }
            return clazz;
        }catch (final ClassNotFoundException ex){
            // allow path separators (.) as inner class name separators
            final int lastDotIndex = className.lastIndexOf(PACKAGE_SEPARATOR_CHAR);

            if (lastDotIndex != -1){
                try{
                    return getClass(
                                    classLoader,
                                    className.substring(0, lastDotIndex) + INNER_CLASS_SEPARATOR_CHAR
                                                    + className.substring(lastDotIndex + 1),
                                    initialize);
                }catch (final ClassNotFoundException ex2){ // NOPMD
                    // ignore exception
                }
            }

            throw ex;
        }
    }

    /**
     * Returns the (initialized) class represented by {@code className}
     * using the {@code classLoader}. This implementation supports
     * the syntaxes "{@code java.util.Map.Entry[]}",
     * "{@code java.util.Map$Entry[]}", "{@code [Ljava.util.Map.Entry;}",
     * and "{@code [Ljava.util.Map$Entry;}".
     *
     * @param classLoader
     *            the class loader to use to load the class
     * @param className
     *            the class name
     * @return the class represented by {@code className} using the {@code classLoader}
     * @throws ClassNotFoundException
     *             if the class is not found
     */
    public static Class<?> getClass(final ClassLoader classLoader,final String className) throws ClassNotFoundException{
        return getClass(classLoader, className, true);
    }

    /**
     * Returns the (initialized) class represented by {@code className}
     * using the current thread's context class loader. This implementation
     * supports the syntaxes "{@code java.util.Map.Entry[]}",
     * "{@code java.util.Map$Entry[]}", "{@code [Ljava.util.Map.Entry;}",
     * and "{@code [Ljava.util.Map$Entry;}".
     *
     * @param className
     *            the class name
     * @return the class represented by {@code className} using the current thread's context class loader
     * @throws ClassNotFoundException
     *             if the class is not found
     */
    public static Class<?> getClass(final String className) throws ClassNotFoundException{
        return getClass(className, true);
    }

    /**
     * Returns the class represented by {@code className} using the
     * current thread's context class loader. This implementation supports the
     * syntaxes "{@code java.util.Map.Entry[]}", "{@code java.util.Map$Entry[]}",
     * "{@code [Ljava.util.Map.Entry;}", and "{@code [Ljava.util.Map$Entry;}".
     *
     * @param className
     *            the class name
     * @param initialize
     *            whether the class must be initialized
     * @return the class represented by {@code className} using the current thread's context class loader
     * @throws ClassNotFoundException
     *             if the class is not found
     */
    public static Class<?> getClass(final String className,final boolean initialize) throws ClassNotFoundException{
        final ClassLoader contextCL = Thread.currentThread().getContextClassLoader();
        final ClassLoader loader = contextCL == null ? ClassUtils.class.getClassLoader() : contextCL;
        return getClass(loader, className, initialize);
    }

    // Public method
    // ----------------------------------------------------------------------
    /**
     * <p>
     * Returns the desired Method much like {@code Class.getMethod}, however
     * it ensures that the returned Method is from a public class or interface and not
     * from an anonymous inner class. This means that the Method is invokable and
     * doesn't fall foul of Java bug
     * <a href="http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4071957">4071957</a>).
     * </p>
     *
     * <pre>
     *  <code>Set set = Collections.unmodifiableSet(...);
     *  Method method = ClassUtils.getPublicMethod(set.getClass(), "isEmpty",  new Class[0]);
     *  Object result = method.invoke(set, new Object[]);</code>
     * </pre>
     *
     * @param cls
     *            the class to check, not null
     * @param methodName
     *            the name of the method
     * @param parameterTypes
     *            the list of parameters
     * @return the method
     * @throws NullPointerException
     *             if the class is null
     * @throws SecurityException
     *             if a security violation occurred
     * @throws NoSuchMethodException
     *             if the method is not found in the given class
     *             or if the method doesn't conform with the requirements
     */
    public static Method getPublicMethod(final Class<?> cls,final String methodName,final Class<?>...parameterTypes)
                    throws NoSuchMethodException{

        final Method declaredMethod = cls.getMethod(methodName, parameterTypes);
        if (Modifier.isPublic(declaredMethod.getDeclaringClass().getModifiers())){
            return declaredMethod;
        }

        final List<Class<?>> candidateClasses = new ArrayList<>();
        candidateClasses.addAll(getAllInterfaces(cls));
        candidateClasses.addAll(getAllSuperclasses(cls));

        for (final Class<?> candidateClass : candidateClasses){
            if (!Modifier.isPublic(candidateClass.getModifiers())){
                continue;
            }
            Method candidateMethod;
            try{
                candidateMethod = candidateClass.getMethod(methodName, parameterTypes);
            }catch (final NoSuchMethodException ex){
                continue;
            }
            if (Modifier.isPublic(candidateMethod.getDeclaringClass().getModifiers())){
                return candidateMethod;
            }
        }

        throw new NoSuchMethodException("Can't find a public method for " + methodName + " " + ArrayUtils.toString(parameterTypes));
    }

    // ----------------------------------------------------------------------
    /**
     * Converts a class name to a JLS style class name.
     *
     * @param className
     *            the class name
     * @return the converted name
     */
    private static String toCanonicalName(String className){
        className = StringUtils.deleteWhitespace(className);
        Validate.notNull(className, "className must not be null.");
        if (className.endsWith("[]")){
            final StringBuilder classNameBuffer = new StringBuilder();
            while (className.endsWith("[]")){
                className = className.substring(0, className.length() - 2);
                classNameBuffer.append("[");
            }
            final String abbreviation = abbreviationMap.get(className);
            if (abbreviation != null){
                classNameBuffer.append(abbreviation);
            }else{
                classNameBuffer.append("L").append(className).append(";");
            }
            className = classNameBuffer.toString();
        }
        return className;
    }

    /**
     * <p>
     * Converts an array of {@code Object} in to an array of {@code Class} objects.
     * If any of these objects is null, a null element will be inserted into the array.
     * </p>
     *
     * <p>
     * This method returns {@code null} for a {@code null} input array.
     * </p>
     *
     * @param array
     *            an {@code Object} array
     * @return a {@code Class} array, {@code null} if null array input
     * @since 2.4
     */
    public static Class<?>[] toClass(final Object...array){
        if (array == null){
            return null;
        }else if (array.length == 0){
            return ArrayUtils.EMPTY_CLASS_ARRAY;
        }
        final Class<?>[] classes = new Class[array.length];
        for (int i = 0; i < array.length; i++){
            classes[i] = array[i] == null ? null : array[i].getClass();
        }
        return classes;
    }

    // Short canonical name

}
