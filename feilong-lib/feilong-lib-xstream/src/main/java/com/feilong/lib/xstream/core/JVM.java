/*
 * Copyright (C) 2004, 2005, 2006 Joe Walnes.
 * Copyright (C) 2006, 2007, 2008, 2010, 2011, 2012, 2013, 2014, 2015, 2016, 2017, 2018 XStream Committers.
 * All rights reserved.
 *
 * The software in this package is published under the terms of the BSD
 * style license a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 *
 * Created on 09. May 2004 by Joe Walnes
 */
package com.feilong.lib.xstream.core;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import com.feilong.lib.xstream.converters.reflection.FieldDictionary;
import com.feilong.lib.xstream.converters.reflection.ObjectAccessException;
import com.feilong.lib.xstream.converters.reflection.PureJavaReflectionProvider;
import com.feilong.lib.xstream.converters.reflection.ReflectionProvider;
import com.feilong.lib.xstream.core.util.Base64Encoder;
import com.feilong.lib.xstream.core.util.CustomObjectOutputStream;
import com.feilong.lib.xstream.core.util.DependencyInjectionFactory;
import com.feilong.lib.xstream.core.util.PresortedMap;
import com.feilong.lib.xstream.core.util.PresortedSet;

public class JVM implements Caching{

    private ReflectionProvider       reflectionProvider;

    private static final boolean     isAWTAvailable;

    private static final boolean     isSwingAvailable;

    private static final boolean     isSQLAvailable;

    private static final boolean     canAllocateWithUnsafe;

    private static final boolean     canWriteWithUnsafe;

    private static final boolean     optimizedTreeSetAddAll;

    private static final boolean     optimizedTreeMapPutAll;

    private static final boolean     canParseUTCDateFormat;

    private static final boolean     canParseISO8601TimeZoneInDateFormat;

    private static final boolean     canCreateDerivedObjectOutputStream;

    private static final String      vendor               = System.getProperty("java.vm.vendor");

    private static final float       majorJavaVersion     = getMajorJavaVersion();

    private static final float       DEFAULT_JAVA_VERSION = 1.4f;

    private static final boolean     reverseFieldOrder    = false;

    private static final Class       reflectionProviderType;

    private static final StringCodec base64Codec;

    static class Test{

        private Object  o;

        private char    c;

        private byte    b;

        private short   s;

        private int     i;

        private long    l;

        private float   f;

        private double  d;

        private boolean bool;

        Test(){
            throw new UnsupportedOperationException();
        }
    }

    static{
        boolean test = true;
        Object unsafe = null;
        try{
            Class unsafeClass = Class.forName("sun.misc.Unsafe");
            Field unsafeField = unsafeClass.getDeclaredField("theUnsafe");
            unsafeField.setAccessible(true);
            unsafe = unsafeField.get(null);
            Method allocateInstance = unsafeClass.getDeclaredMethod("allocateInstance", new Class[] { Class.class });
            allocateInstance.setAccessible(true);
            test = allocateInstance.invoke(unsafe, new Object[] { Test.class }) != null;
        }catch (Exception e){
            test = false;
        }catch (Error e){
            test = false;
        }
        canAllocateWithUnsafe = test;
        test = false;
        Class type = PureJavaReflectionProvider.class;
        if (canUseSunUnsafeReflectionProvider()){
            Class cls = loadClassForName("com.feilong.lib.xstream.converters.reflection.SunUnsafeReflectionProvider");
            if (cls != null){
                try{
                    ReflectionProvider provider = (ReflectionProvider) DependencyInjectionFactory.newInstance(cls, null);
                    Test t = (Test) provider.newInstance(Test.class);
                    try{
                        provider.writeField(t, "o", "object", Test.class);
                        provider.writeField(t, "c", new Character('c'), Test.class);
                        provider.writeField(t, "b", new Byte((byte) 1), Test.class);
                        provider.writeField(t, "s", new Short((short) 1), Test.class);
                        provider.writeField(t, "i", new Integer(1), Test.class);
                        provider.writeField(t, "l", new Long(1), Test.class);
                        provider.writeField(t, "f", new Float(1), Test.class);
                        provider.writeField(t, "d", new Double(1), Test.class);
                        provider.writeField(t, "bool", Boolean.TRUE, Test.class);
                        test = true;
                    }catch (IncompatibleClassChangeError e){
                        cls = null;
                    }catch (ObjectAccessException e){
                        cls = null;
                    }
                    if (cls == null){
                        cls = loadClassForName("com.feilong.lib.xstream.converters.reflection.SunLimitedUnsafeReflectionProvider");
                    }
                    type = cls;
                }catch (ObjectAccessException e){}
            }
        }
        reflectionProviderType = type;
        canWriteWithUnsafe = test;
        Comparator comparator = (o1,o2) -> {
            throw new RuntimeException();
        };
        SortedMap map = new PresortedMap(comparator);
        map.put("one", null);
        map.put("two", null);
        try{
            new TreeMap(comparator).putAll(map);
            test = true;
        }catch (RuntimeException e){
            test = false;
        }
        optimizedTreeMapPutAll = test;
        SortedSet set = new PresortedSet(comparator);
        set.addAll(map.keySet());
        try{
            new TreeSet(comparator).addAll(set);
            test = true;
        }catch (RuntimeException e){
            test = false;
        }
        optimizedTreeSetAddAll = test;
        try{
            new SimpleDateFormat("z").parse("UTC");
            test = true;
        }catch (ParseException e){
            test = false;
        }
        canParseUTCDateFormat = test;
        try{
            new SimpleDateFormat("X").parse("Z");
            test = true;
        }catch (final ParseException e){
            test = false;
        }catch (final IllegalArgumentException e){
            test = false;
        }
        canParseISO8601TimeZoneInDateFormat = test;
        try{
            test = new CustomObjectOutputStream(null) != null;
        }catch (RuntimeException e){
            test = false;
        }catch (IOException e){
            test = false;
        }
        canCreateDerivedObjectOutputStream = test;

        isAWTAvailable = loadClassForName("java.awt.Color", false) != null;
        isSwingAvailable = loadClassForName("javax.swing.LookAndFeel", false) != null;
        isSQLAvailable = loadClassForName("java.sql.Date") != null;

        StringCodec base64 = null;
        Class base64Class = loadClassForName("com.feilong.lib.xstream.core.util.Base64JavaUtilCodec");
        if (base64Class == null){
            base64Class = loadClassForName("com.feilong.lib.xstream.core.util.Base64JAXBCodec");
        }
        if (base64Class != null){
            try{
                base64 = (StringCodec) base64Class.newInstance();
            }catch (final Exception e){}catch (final Error e){}
        }
        if (base64 == null){
            base64 = new Base64Encoder();
        }
        base64Codec = base64;
    }

    /**
     * @deprecated As of 1.4.5 use the static methods of JVM.
     */
    @Deprecated
    public JVM(){
    }

    /**
     * Parses the java version system property to determine the major java version, i.e. 1.x
     *
     * @return A float of the form 1.x
     */
    private static final float getMajorJavaVersion(){
        try{
            return isAndroid() ? 1.5f : Float.parseFloat(System.getProperty("java.specification.version"));
        }catch (NumberFormatException e){
            // Some JVMs may not conform to the x.y.z java.version format
            return DEFAULT_JAVA_VERSION;
        }
    }

    /**
     * @deprecated As of 1.4.4, minimal JDK version will be 1.7 for next major release
     */
    @Deprecated
    public static boolean is15(){
        return isVersion(5);
    }

    /**
     * Checks current runtime against provided major Java version.
     *
     * @param version
     *            the requested major Java version
     * @return true if current runtime is at least the provided major version
     * @since 1.4.11
     */
    public static boolean isVersion(final int version){
        if (version < 1){
            throw new IllegalArgumentException("Java version range starts with at least 1.");
        }
        final float v = majorJavaVersion < 9 ? 1f + version * 0.1f : version;
        return majorJavaVersion >= v;
    }

    private static boolean isIBM(){
        return vendor.indexOf("IBM") != -1;
    }

    /**
     * @since 1.4
     */
    private static boolean isAndroid(){
        return vendor.indexOf("Android") != -1;
    }

    /**
     * Load a XStream class for the given name.
     * <p>
     * This method is not meant to use loading arbitrary classes. It is used by XStream bootstrap until it is able to
     * use the user provided or the default {@link ClassLoader}.
     * </p>
     *
     * @since 1.4.5
     */
    public static Class loadClassForName(String name){
        return loadClassForName(name, true);
    }

    /**
     * @deprecated As of 1.4.5 use {@link #loadClassForName(String)}
     */
    @Deprecated
    public Class loadClass(String name){
        return loadClassForName(name, true);
    }

    /**
     * Load a XStream class for the given name.
     * <p>
     * This method is not meant to use loading arbitrary classes. It is used by XStream bootstrap until it is able to
     * use the user provided or the default {@link ClassLoader}.
     * </p>
     *
     * @since 1.4.5
     */
    public static Class loadClassForName(String name,boolean initialize){
        try{
            Class clazz = Class.forName(name, initialize, JVM.class.getClassLoader());
            return clazz;
        }catch (LinkageError e){
            return null;
        }catch (ClassNotFoundException e){
            return null;
        }
    }

    /**
     * @since 1.4.4
     * @deprecated As of 1.4.5 use {@link #loadClassForName(String, boolean)}
     */
    @Deprecated
    public Class loadClass(String name,boolean initialize){
        return loadClassForName(name, initialize);
    }

    /**
     * Create the best matching ReflectionProvider.
     *
     * @return a new instance
     * @since 1.4.5
     */
    public static ReflectionProvider newReflectionProvider(){
        return (ReflectionProvider) DependencyInjectionFactory.newInstance(reflectionProviderType, null);
    }

    /**
     * Create the best matching ReflectionProvider.
     *
     * @param dictionary
     *            the FieldDictionary to use by the ReflectionProvider
     * @return a new instance
     * @since 1.4.5
     */
    public static ReflectionProvider newReflectionProvider(FieldDictionary dictionary){
        return (ReflectionProvider) DependencyInjectionFactory.newInstance(reflectionProviderType, new Object[] { dictionary });
    }

    /**
     * Get the XMLInputFactory implementation used normally by the current Java runtime as
     * standard.
     * <p>
     * In contrast to XMLInputFactory.newFactory() this method will ignore any implementations
     * provided with the system property <em>javax.xml.stream.XMLInputFactory</em>,
     * implementations configured in <em>lib/stax.properties</em> or registered with the Service
     * API.
     * </p>
     *
     * @return the XMLInputFactory implementation or null
     * @throws ClassNotFoundException
     *             if the standard class cannot be found
     * @since 1.4.5
     */
    public static Class getStaxInputFactory() throws ClassNotFoundException{
        if (isVersion(6)){
            if (isIBM()){
                return Class.forName("com.ibm.xml.xlxp.api.stax.XMLInputFactoryImpl");
            }else{
                return Class.forName("com.sun.xml.internal.stream.XMLInputFactoryImpl");
            }
        }
        return null;
    }

    /**
     * Get the XMLOutputFactory implementation used normally by the current Java runtime as
     * standard.
     * <p>
     * In contrast to XMLOutputFactory.newFactory() this method will ignore any implementations
     * provided with the system property <em>javax.xml.stream.XMLOutputFactory</em>,
     * implementations configured in <em>lib/stax.properties</em> or registered with the Service
     * API.
     * </p>
     *
     * @return the XMLOutputFactory implementation or null
     * @throws ClassNotFoundException
     *             if the standard class cannot be found
     * @since 1.4.5
     */
    public static Class getStaxOutputFactory() throws ClassNotFoundException{
        if (isVersion(6)){
            if (isIBM()){
                return Class.forName("com.ibm.xml.xlxp.api.stax.XMLOutputFactoryImpl");
            }else{
                return Class.forName("com.sun.xml.internal.stream.XMLOutputFactoryImpl");
            }
        }
        return null;
    }

    /**
     * Get an available Base64 implementation. Prefers java.util.Base64 over DataTypeConverter from JAXB over XStream's
     * own implementation.
     *
     * @return a Base64 codec implementation
     * @since 1.4.11
     */
    public static StringCodec getBase64Codec(){
        return base64Codec;
    }

    /**
     * @deprecated As of 1.4.5 use {@link #newReflectionProvider()}
     */
    @Deprecated
    public synchronized ReflectionProvider bestReflectionProvider(){
        if (reflectionProvider == null){
            reflectionProvider = newReflectionProvider();
        }
        return reflectionProvider;
    }

    private static boolean canUseSunUnsafeReflectionProvider(){
        return canAllocateWithUnsafe;
    }

    private static boolean canUseSunLimitedUnsafeReflectionProvider(){
        return canWriteWithUnsafe;
    }

    /**
     * @deprecated As of 1.4.5
     */
    @Deprecated
    public static boolean reverseFieldDefinition(){
        return reverseFieldOrder;
    }

    /**
     * Checks if AWT is available.
     *
     * @since 1.4.5
     */
    public static boolean isAWTAvailable(){
        return isAWTAvailable;
    }

    /**
     * Checks if the JVM supports AWT.
     *
     * @deprecated As of 1.4.5 use {@link #isAWTAvailable()}
     */
    @Deprecated
    public boolean supportsAWT(){
        return JVM.isAWTAvailable;
    }

    /**
     * Checks if Swing is available.
     *
     * @since 1.4.5
     */
    public static boolean isSwingAvailable(){
        return isSwingAvailable;
    }

    /**
     * Checks if the JVM supports Swing.
     *
     * @deprecated As of 1.4.5 use {@link #isSwingAvailable()}
     */
    @Deprecated
    public boolean supportsSwing(){
        return JVM.isSwingAvailable;
    }

    /**
     * Checks if SQL is available.
     *
     * @since 1.4.5
     */
    public static boolean isSQLAvailable(){
        return isSQLAvailable;
    }

    /**
     * Checks if the JVM supports SQL.
     *
     * @deprecated As of 1.4.5 use {@link #isSQLAvailable()}
     */
    @Deprecated
    public boolean supportsSQL(){
        return JVM.isSQLAvailable;
    }

    /**
     * Checks if TreeSet.addAll is optimized for SortedSet argument.
     *
     * @since 1.4
     */
    public static boolean hasOptimizedTreeSetAddAll(){
        return optimizedTreeSetAddAll;
    }

    /**
     * Checks if TreeMap.putAll is optimized for SortedMap argument.
     *
     * @since 1.4
     */
    public static boolean hasOptimizedTreeMapPutAll(){
        return optimizedTreeMapPutAll;
    }

    public static boolean canParseUTCDateFormat(){
        return canParseUTCDateFormat;
    }

    /**
     * @since 1.4.8
     */
    public static boolean canParseISO8601TimeZoneInDateFormat(){
        return canParseISO8601TimeZoneInDateFormat;
    }

    /**
     * @since 1.4.6
     */
    public static boolean canCreateDerivedObjectOutputStream(){
        return canCreateDerivedObjectOutputStream;
    }

    /**
     * @deprecated As of 1.4.5 no functionality
     */
    @Deprecated
    @Override
    public void flushCache(){
    }

}
