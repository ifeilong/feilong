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

package com.feilong.lib.beanutils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.DynaBean;

import com.feilong.lib.beanutils.priv.PrivateBeanFactory;
import com.feilong.lib.beanutils.priv.PrivateDirect;
import com.feilong.lib.beanutils.priv.PublicSubBean;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * Test Case for the PropertyUtils class. The majority of these tests use
 * instances of the TestBean class, so be sure to update the tests if you
 * change the characteristics of that class.
 * </p>
 *
 * <p>
 * So far, this test case has tests for the following methods of the
 * <code>PropertyUtils</code> class:
 * </p>
 * <ul>
 * <li>getIndexedProperty(Object,String)</li>
 * <li>getIndexedProperty(Object,String,int)</li>
 * <li>getMappedProperty(Object,String)</li>
 * <li>getMappedProperty(Object,String,String</li>
 * <li>getNestedProperty(Object,String)</li>
 * <li>getPropertyDescriptor(Object,String)</li>
 * <li>getPropertyDescriptors(Object)</li>
 * <li>getPropertyType(Object,String)</li>
 * <li>getSimpleProperty(Object,String)</li>
 * <li>setIndexedProperty(Object,String,Object)</li>
 * <li>setIndexedProperty(Object,String,String,Object)</li>
 * <li>setMappedProperty(Object,String,Object)</li>
 * <li>setMappedProperty(Object,String,String,Object)</li>
 * <li>setNestedProperty(Object,String,Object)</li>
 * <li>setSimpleProperty(Object,String,Object)</li>
 * </ul>
 *
 * @version $Id$
 */

public class PropertyUtilsTestCase extends TestCase{

    // ---------------------------------------------------- Instance Variables
    //
    //    /**
    //     * The fully qualified class name of our private directly
    //     * implemented interface.
    //     */
    //    private static final String       PRIVATE_DIRECT_CLASS   = "org.apache.commons.beanutils.priv.PrivateDirect";
    //
    //    /**
    //     * The fully qualified class name of our private indirectly
    //     * implemented interface.
    //     */
    //    private static final String       PRIVATE_INDIRECT_CLASS = "org.apache.commons.beanutils.priv.PrivateIndirect";
    //
    //    /**
    //     * The fully qualified class name of our test bean class.
    //     */
    //    private static final String       TEST_BEAN_CLASS        = "org.apache.commons.beanutils.TestBean";

    /**
     * The basic test bean for each test.
     */
    protected TestBean                bean                = null;

    /**
     * The "package private subclass" test bean for each test.
     */
    protected TestBeanPackageSubclass beanPackageSubclass = null;

    /**
     * The test bean for private access tests.
     */
    protected PrivateDirect           beanPrivate         = null;

    /**
     * The test bean for private access tests of subclasses.
     */
    protected PrivateDirect           beanPrivateSubclass = null;

    /**
     * The "public subclass" test bean for each test.
     */
    protected TestBeanPublicSubclass  beanPublicSubclass  = null;

    /**
     * The set of properties that should be described.
     */
    protected String                  describes[]         = {
                                                              "booleanProperty",
                                                              "booleanSecond",
                                                              "doubleProperty",
                                                              "floatProperty",
                                                              "intArray",
                                                              //      "intIndexed",
                                                              "intProperty",
                                                              "listIndexed",
                                                              "longProperty",
                                                              //      "mappedObjects",
                                                              //      "mappedProperty",
                                                              //      "mappedIntProperty",
                                                              "nested",
                                                              "nullProperty",
                                                              //      "readOnlyProperty",
                                                              "shortProperty",
                                                              "stringArray",
                                                              //      "stringIndexed",
                                                              "stringProperty" };

    /**
     * The set of property names we expect to have returned when calling
     * <code>getPropertyDescriptors()</code>. You should update this list
     * when new properties are added to TestBean.
     */
    protected final static String[]   properties          = {
                                                              "booleanProperty",
                                                              "booleanSecond",
                                                              "doubleProperty",
                                                              "dupProperty",
                                                              "floatProperty",
                                                              "intArray",
                                                              "intIndexed",
                                                              "intProperty",
                                                              "listIndexed",
                                                              "longProperty",
                                                              "nested",
                                                              "nullProperty",
                                                              "readOnlyProperty",
                                                              "shortProperty",
                                                              "stringArray",
                                                              "stringIndexed",
                                                              "stringProperty",
                                                              "writeOnlyProperty", };

    // ---------------------------------------------------------- Constructors

    /**
     * Construct a new instance of this test case.
     *
     * @param name
     *            Name of the test case
     */
    public PropertyUtilsTestCase(final String name){

        super(name);

    }

    // -------------------------------------------------- Overall Test Methods

    /**
     * Set up instance variables required by this test case.
     */
    @Override
    public void setUp(){

        bean = new TestBean();
        beanPackageSubclass = new TestBeanPackageSubclass();
        beanPrivate = PrivateBeanFactory.create();
        beanPrivateSubclass = PrivateBeanFactory.createSubclass();
        beanPublicSubclass = new TestBeanPublicSubclass();

        final DynaProperty[] properties = new DynaProperty[] {
                                                               new DynaProperty("stringProperty", String.class),
                                                               new DynaProperty("nestedBean", TestBean.class),
                                                               new DynaProperty("nullDynaBean", DynaBean.class) };
        final BasicDynaClass dynaClass = new BasicDynaClass("nestedDynaBean", BasicDynaBean.class, properties);
        final BasicDynaBean nestedDynaBean = new BasicDynaBean(dynaClass);
        nestedDynaBean.set("nestedBean", bean);
        bean.setNestedDynaBean(nestedDynaBean);
    }

    /**
     * Return the tests included in this test suite.
     */
    public static Test suite(){

        return (new TestSuite(PropertyUtilsTestCase.class));

    }

    /**
     * Tear down instance variables required by this test case.
     */
    @Override
    public void tearDown(){

        bean = null;
        beanPackageSubclass = null;
        beanPrivate = null;
        beanPrivateSubclass = null;
        beanPublicSubclass = null;

    }

    // ------------------------------------------------ Individual Test Methods

    /**
     * Test copyProperties() when the origin is a a <code>Map</code>.
     */
    public void testCopyPropertiesMap(){

        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("booleanProperty", Boolean.FALSE);
        map.put("doubleProperty", new Double(333.0));
        map.put("dupProperty", new String[] { "New 0", "New 1", "New 2" });
        map.put("floatProperty", new Float((float) 222.0));
        map.put("intArray", new int[] { 0, 100, 200 });
        map.put("intProperty", new Integer(111));
        map.put("longProperty", new Long(444));
        map.put("shortProperty", new Short((short) 555));
        map.put("stringProperty", "New String Property");

        try{
            PropertyUtils.copyProperties(bean, map);
        }catch (final Throwable t){
            fail("Threw " + t.toString());
        }

        // Scalar properties
        assertEquals("booleanProperty", false, bean.getBooleanProperty());
        assertEquals("doubleProperty", 333.0, bean.getDoubleProperty(), 0.005);
        assertEquals("floatProperty", (float) 222.0, bean.getFloatProperty(), (float) 0.005);
        assertEquals("intProperty", 111, bean.getIntProperty());
        assertEquals("longProperty", 444, bean.getLongProperty());
        assertEquals("shortProperty", (short) 555, bean.getShortProperty());
        assertEquals("stringProperty", "New String Property", bean.getStringProperty());

        // Indexed Properties
        final String dupProperty[] = bean.getDupProperty();
        assertNotNull("dupProperty present", dupProperty);
        assertEquals("dupProperty length", 3, dupProperty.length);
        assertEquals("dupProperty[0]", "New 0", dupProperty[0]);
        assertEquals("dupProperty[1]", "New 1", dupProperty[1]);
        assertEquals("dupProperty[2]", "New 2", dupProperty[2]);
        final int intArray[] = bean.getIntArray();
        assertNotNull("intArray present", intArray);
        assertEquals("intArray length", 3, intArray.length);
        assertEquals("intArray[0]", 0, intArray[0]);
        assertEquals("intArray[1]", 100, intArray[1]);
        assertEquals("intArray[2]", 200, intArray[2]);

    }

    /**
     * Test the describe() method.
     */
    public void testDescribe(){

        Map<String, Object> map = null;
        try{
            map = PropertyUtils.describe(bean);
        }catch (final Exception e){
            fail("Threw exception " + e);
        }

        // Verify existence of all the properties that should be present
        for (String describe : describes){
            assertTrue("Property '" + describe + "' is present", map.containsKey(describe));
        }
        assertTrue("Property 'writeOnlyProperty' is not present", !map.containsKey("writeOnlyProperty"));

        // Verify the values of scalar properties
        assertEquals("Value of 'booleanProperty'", Boolean.TRUE, map.get("booleanProperty"));
        assertEquals("Value of 'doubleProperty'", new Double(321.0), map.get("doubleProperty"));
        assertEquals("Value of 'floatProperty'", new Float((float) 123.0), map.get("floatProperty"));
        assertEquals("Value of 'intProperty'", new Integer(123), map.get("intProperty"));
        assertEquals("Value of 'longProperty'", new Long(321), map.get("longProperty"));
        assertEquals("Value of 'shortProperty'", new Short((short) 987), map.get("shortProperty"));
        assertEquals("Value of 'stringProperty'", "This is a string", (String) map.get("stringProperty"));

    }

    /**
     * Corner cases on getPropertyDescriptor invalid arguments.
     */
    public void testGetDescriptorArguments() throws Exception{

        try{
            PropertyUtils.getPropertyDescriptor(null, "stringProperty");
            fail("Should throw IllegalArgumentException 1");
        }catch (final IllegalArgumentException e){
            // Expected response
        }

        try{
            PropertyUtils.getPropertyDescriptor(bean, null);
            fail("Should throw IllegalArgumentException 2");
        }catch (final IllegalArgumentException e){
            // Expected response
        }

    }

    /**
     * Positive getPropertyDescriptor on property <code>booleanProperty</code>.
     */
    public void testGetDescriptorBoolean() throws Exception{
        testGetDescriptorBase("booleanProperty", "getBooleanProperty", "setBooleanProperty");

    }

    /**
     * Positive getPropertyDescriptor on property <code>doubleProperty</code>.
     */
    public void testGetDescriptorDouble() throws Exception{
        testGetDescriptorBase("doubleProperty", "getDoubleProperty", "setDoubleProperty");

    }

    /**
     * Positive getPropertyDescriptor on property <code>floatProperty</code>.
     */
    public void testGetDescriptorFloat() throws Exception{
        testGetDescriptorBase("floatProperty", "getFloatProperty", "setFloatProperty");

    }

    /**
     * Positive getPropertyDescriptor on property <code>intProperty</code>.
     */
    public void testGetDescriptorInt() throws Exception{
        testGetDescriptorBase("intProperty", "getIntProperty", "setIntProperty");

    }

    /**
     * <p>
     * Negative tests on an invalid property with two different boolean
     * getters (which is fine, according to the JavaBeans spec) but a
     * String setter instead of a boolean setter.
     * </p>
     *
     * <p>
     * Although one could logically argue that this combination of method
     * signatures should not identify a property at all, there is a sentence
     * in Section 8.3.1 making it clear that the behavior tested for here
     * is correct: "If we find only one of these methods, then we regard
     * it as defining either a read-only or write-only property called
     * <em>&lt;property-name&gt;</em>.
     * </p>
     */
    public void testGetDescriptorInvalidBoolean() throws Exception{
        final PropertyDescriptor pd = PropertyUtils.getPropertyDescriptor(bean, "invalidBoolean");
        assertNotNull("invalidBoolean is a property", pd);
        assertNotNull("invalidBoolean has a getter method", pd.getReadMethod());
        assertNull("invalidBoolean has no write method", pd.getWriteMethod());
        assertTrue(
                        "invalidBoolean getter method is isInvalidBoolean or getInvalidBoolean",
                        Arrays.asList("isInvalidBoolean", "getInvalidBoolean").contains(pd.getReadMethod().getName()));
    }

    /**
     * Positive getPropertyDescriptor on property <code>longProperty</code>.
     */
    public void testGetDescriptorLong() throws Exception{
        testGetDescriptorBase("longProperty", "getLongProperty", "setLongProperty");

    }

    /**
     * Test getting mapped descriptor with periods in the key.
     */
    public void testGetDescriptorMappedPeriods(){

        bean.getMappedIntProperty("xyz"); // initializes mappedIntProperty

        PropertyDescriptor desc;
        final Integer testIntegerValue = new Integer(1234);

        bean.setMappedIntProperty("key.with.a.dot", testIntegerValue.intValue());
        assertEquals("Can retrieve directly", testIntegerValue, new Integer(bean.getMappedIntProperty("key.with.a.dot")));
        try{
            desc = PropertyUtils.getPropertyDescriptor(bean, "mappedIntProperty(key.with.a.dot)");
            assertEquals("Check descriptor type (A)", Integer.TYPE, ((MappedPropertyDescriptor) desc).getMappedPropertyType());
        }catch (final Exception e){
            fail("Threw exception (A): " + e);
        }

        bean.setMappedObjects("nested.property", new TestBean(testIntegerValue.intValue()));
        assertEquals(
                        "Can retrieve directly",
                        testIntegerValue,
                        new Integer(((TestBean) bean.getMappedObjects("nested.property")).getIntProperty()));
        try{
            desc = PropertyUtils.getPropertyDescriptor(bean, "mappedObjects(nested.property).intProperty");
            assertEquals("Check descriptor type (B)", Integer.TYPE, desc.getPropertyType());
        }catch (final Exception e){
            fail("Threw exception (B): " + e);
        }
    }

    /**
     * Positive getPropertyDescriptor on property
     * <code>readOnlyProperty</code>.
     */
    public void testGetDescriptorReadOnly() throws Exception{
        testGetDescriptorBase("readOnlyProperty", "getReadOnlyProperty", null);

    }

    /**
     * Positive getPropertyDescriptor on property <code>booleanSecond</code>
     * that uses an "is" method as the getter.
     */
    public void testGetDescriptorSecond() throws Exception{
        testGetDescriptorBase("booleanSecond", "isBooleanSecond", "setBooleanSecond");

    }

    /**
     * Positive getPropertyDescriptor on property <code>shortProperty</code>.
     */
    public void testGetDescriptorShort() throws Exception{
        testGetDescriptorBase("shortProperty", "getShortProperty", "setShortProperty");

    }

    /**
     * Positive getPropertyDescriptor on property <code>stringProperty</code>.
     */
    public void testGetDescriptorString() throws Exception{
        testGetDescriptorBase("stringProperty", "getStringProperty", "setStringProperty");

    }

    /**
     * Negative getPropertyDescriptor on property <code>unknown</code>.
     */
    public void testGetDescriptorUnknown() throws Exception{
        testGetDescriptorBase("unknown", null, null);

    }

    /**
     * Positive getPropertyDescriptor on property
     * <code>writeOnlyProperty</code>.
     */
    public void testGetDescriptorWriteOnly() throws Exception{
        testGetDescriptorBase("writeOnlyProperty", null, "setWriteOnlyProperty");

    }

    /**
     * Corner cases on getPropertyDescriptors invalid arguments.
     */
    public void testGetDescriptorsArguments(){
        try{
            PropertyUtils.getPropertyDescriptors(null);
            fail("Should throw IllegalArgumentException");
        }catch (final IllegalArgumentException e){
            // Expected response
        }catch (final Throwable t){
            fail("Threw " + t + " instead of IllegalArgumentException");
        }

    }

    /**
     * Test getting an indexed value out of a multi-dimensional array
     */
    public void testGetIndexedArray(){
        final String[] firstArray = new String[] { "FIRST-1", "FIRST-2", "FIRST-3" };
        final String[] secondArray = new String[] { "SECOND-1", "SECOND-2", "SECOND-3", "SECOND-4" };
        final String[][] mainArray = { firstArray, secondArray };
        final TestBean bean = new TestBean(mainArray);
        try{
            assertEquals("firstArray[0]", firstArray[0], PropertyUtils.getProperty(bean, "string2dArray[0][0]"));
            assertEquals("firstArray[1]", firstArray[1], PropertyUtils.getProperty(bean, "string2dArray[0][1]"));
            assertEquals("firstArray[2]", firstArray[2], PropertyUtils.getProperty(bean, "string2dArray[0][2]"));
            assertEquals("secondArray[0]", secondArray[0], PropertyUtils.getProperty(bean, "string2dArray[1][0]"));
            assertEquals("secondArray[1]", secondArray[1], PropertyUtils.getProperty(bean, "string2dArray[1][1]"));
            assertEquals("secondArray[2]", secondArray[2], PropertyUtils.getProperty(bean, "string2dArray[1][2]"));
            assertEquals("secondArray[3]", secondArray[3], PropertyUtils.getProperty(bean, "string2dArray[1][3]"));
        }catch (final Throwable t){
            fail("Threw " + t + "");
        }
    }

    /**
     * Test getting an indexed value out of List of Lists
     */
    public void testGetIndexedList(){
        final String[] firstArray = new String[] { "FIRST-1", "FIRST-2", "FIRST-3" };
        final String[] secondArray = new String[] { "SECOND-1", "SECOND-2", "SECOND-3", "SECOND-4" };
        final List<Object> mainList = new ArrayList<Object>();
        mainList.add(Arrays.asList(firstArray));
        mainList.add(Arrays.asList(secondArray));
        final TestBean bean = new TestBean(mainList);
        try{
            assertEquals("firstArray[0]", firstArray[0], PropertyUtils.getProperty(bean, "listIndexed[0][0]"));
            assertEquals("firstArray[1]", firstArray[1], PropertyUtils.getProperty(bean, "listIndexed[0][1]"));
            assertEquals("firstArray[2]", firstArray[2], PropertyUtils.getProperty(bean, "listIndexed[0][2]"));
            assertEquals("secondArray[0]", secondArray[0], PropertyUtils.getProperty(bean, "listIndexed[1][0]"));
            assertEquals("secondArray[1]", secondArray[1], PropertyUtils.getProperty(bean, "listIndexed[1][1]"));
            assertEquals("secondArray[2]", secondArray[2], PropertyUtils.getProperty(bean, "listIndexed[1][2]"));
            assertEquals("secondArray[3]", secondArray[3], PropertyUtils.getProperty(bean, "listIndexed[1][3]"));
        }catch (final Throwable t){
            fail("Threw " + t + "");
        }
    }

    /**
     * Test getting a value out of a mapped Map
     */
    public void testGetIndexedMap(){
        final Map<String, Object> firstMap = new HashMap<String, Object>();
        firstMap.put("FIRST-KEY-1", "FIRST-VALUE-1");
        firstMap.put("FIRST-KEY-2", "FIRST-VALUE-2");
        final Map<String, Object> secondMap = new HashMap<String, Object>();
        secondMap.put("SECOND-KEY-1", "SECOND-VALUE-1");
        secondMap.put("SECOND-KEY-2", "SECOND-VALUE-2");

        final List<Object> mainList = new ArrayList<Object>();
        mainList.add(firstMap);
        mainList.add(secondMap);
        final TestBean bean = new TestBean(mainList);
        try{
            assertEquals("listIndexed[0](FIRST-KEY-1)", "FIRST-VALUE-1", PropertyUtils.getProperty(bean, "listIndexed[0](FIRST-KEY-1)"));
            assertEquals("listIndexed[0](FIRST-KEY-2)", "FIRST-VALUE-2", PropertyUtils.getProperty(bean, "listIndexed[0](FIRST-KEY-2)"));
            assertEquals("listIndexed[1](SECOND-KEY-1)", "SECOND-VALUE-1", PropertyUtils.getProperty(bean, "listIndexed[1](SECOND-KEY-1)"));
            assertEquals("listIndexed[1](SECOND-KEY-2)", "SECOND-VALUE-2", PropertyUtils.getProperty(bean, "listIndexed[1](SECOND-KEY-2)"));
        }catch (final Throwable t){
            fail("Threw " + t + "");
        }
    }

    /**
     * Test getting an indexed value out of a mapped array
     */
    public void testGetMappedArray(){
        final TestBean bean = new TestBean();
        final String[] array = new String[] { "abc", "def", "ghi" };
        bean.getMapProperty().put("mappedArray", array);
        try{
            assertEquals("abc", PropertyUtils.getProperty(bean, "mapProperty(mappedArray)[0]"));
            assertEquals("def", PropertyUtils.getProperty(bean, "mapProperty(mappedArray)[1]"));
            assertEquals("ghi", PropertyUtils.getProperty(bean, "mapProperty(mappedArray)[2]"));
        }catch (final Throwable t){
            fail("Threw " + t + "");
        }
    }

    /**
     * Test getting an indexed value out of a mapped List
     */
    public void testGetMappedList(){
        final TestBean bean = new TestBean();
        final List<Object> list = new ArrayList<Object>();
        list.add("klm");
        list.add("nop");
        list.add("qrs");
        bean.getMapProperty().put("mappedList", list);
        try{
            assertEquals("klm", PropertyUtils.getProperty(bean, "mapProperty(mappedList)[0]"));
            assertEquals("nop", PropertyUtils.getProperty(bean, "mapProperty(mappedList)[1]"));
            assertEquals("qrs", PropertyUtils.getProperty(bean, "mapProperty(mappedList)[2]"));
        }catch (final Throwable t){
            fail("Threw " + t + "");
        }
    }

    /**
     * Test getting a value out of a mapped Map
     */
    public void testGetMappedMap(){
        final TestBean bean = new TestBean();
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("sub-key-1", "sub-value-1");
        map.put("sub-key-2", "sub-value-2");
        map.put("sub-key-3", "sub-value-3");
        bean.getMapProperty().put("mappedMap", map);
        try{
            assertEquals("sub-value-1", PropertyUtils.getProperty(bean, "mapProperty(mappedMap)(sub-key-1)"));
            assertEquals("sub-value-2", PropertyUtils.getProperty(bean, "mapProperty(mappedMap)(sub-key-2)"));
            assertEquals("sub-value-3", PropertyUtils.getProperty(bean, "mapProperty(mappedMap)(sub-key-3)"));
        }catch (final Throwable t){
            fail("Threw " + t + "");
        }
    }

    /**
     * When a bean has a null property which is reference by the standard access language,
     * this should throw a NestedNullException.
     */
    public void testThrowNestedNull() throws Exception{
        final NestedTestBean nestedBean = new NestedTestBean("base");
        // don't init!

        try{
            PropertyUtils.getProperty(nestedBean, "simpleBeanProperty.indexedProperty[0]");
            fail("NestedNullException not thrown");
        }catch (final NestedNullException e){
            // that's what we wanted!
        }
    }

    /**
     * Test accessing a public sub-bean of a package scope bean
     */
    public void testGetPublicSubBean_of_PackageBean(){

        final PublicSubBean bean = new PublicSubBean();
        bean.setFoo("foo-start");
        bean.setBar("bar-start");
        Object result = null;

        // Get Foo
        try{
            result = PropertyUtils.getProperty(bean, "foo");
        }catch (final Throwable t){
            fail("getProperty(foo) threw " + t);
        }
        assertEquals("foo property", "foo-start", result);

        // Get Bar
        try{
            result = PropertyUtils.getProperty(bean, "bar");
        }catch (final Throwable t){
            fail("getProperty(bar) threw " + t);
        }
        assertEquals("bar property", "bar-start", result);
    }

    /**
     * Corner cases on getSimpleProperty invalid arguments.
     */
    public void testGetSimpleArguments(){

        try{
            PropertyUtils.getSimpleProperty(null, "stringProperty");
            fail("Should throw IllegalArgumentException 1");
        }catch (final IllegalArgumentException e){
            // Expected response
        }catch (final Throwable t){
            fail("Threw " + t + " instead of IllegalArgumentException 1");
        }

        try{
            PropertyUtils.getSimpleProperty(bean, null);
            fail("Should throw IllegalArgumentException 2");
        }catch (final IllegalArgumentException e){
            // Expected response
        }catch (final Throwable t){
            fail("Threw " + t + " instead of IllegalArgumentException 2");
        }

    }

    /**
     * Test getSimpleProperty on a boolean property.
     */
    public void testGetSimpleBoolean(){

        try{
            final Object value = PropertyUtils.getSimpleProperty(bean, "booleanProperty");
            assertNotNull("Got a value", value);
            assertTrue("Got correct type", (value instanceof Boolean));
            assertTrue("Got correct value", ((Boolean) value).booleanValue() == bean.getBooleanProperty());
        }catch (final IllegalAccessException e){
            fail("IllegalAccessException");
        }catch (final IllegalArgumentException e){
            fail("IllegalArgumentException");
        }catch (final InvocationTargetException e){
            fail("InvocationTargetException");
        }catch (final NoSuchMethodException e){
            fail("NoSuchMethodException");
        }

    }

    /**
     * Test getSimpleProperty on a double property.
     */
    public void testGetSimpleDouble(){

        try{
            final Object value = PropertyUtils.getSimpleProperty(bean, "doubleProperty");
            assertNotNull("Got a value", value);
            assertTrue("Got correct type", (value instanceof Double));
            assertEquals("Got correct value", ((Double) value).doubleValue(), bean.getDoubleProperty(), 0.005);
        }catch (final IllegalAccessException e){
            fail("IllegalAccessException");
        }catch (final IllegalArgumentException e){
            fail("IllegalArgumentException");
        }catch (final InvocationTargetException e){
            fail("InvocationTargetException");
        }catch (final NoSuchMethodException e){
            fail("NoSuchMethodException");
        }

    }

    /**
     * Test getSimpleProperty on a float property.
     */
    public void testGetSimpleFloat(){

        try{
            final Object value = PropertyUtils.getSimpleProperty(bean, "floatProperty");
            assertNotNull("Got a value", value);
            assertTrue("Got correct type", (value instanceof Float));
            assertEquals("Got correct value", ((Float) value).floatValue(), bean.getFloatProperty(), (float) 0.005);
        }catch (final IllegalAccessException e){
            fail("IllegalAccessException");
        }catch (final IllegalArgumentException e){
            fail("IllegalArgumentException");
        }catch (final InvocationTargetException e){
            fail("InvocationTargetException");
        }catch (final NoSuchMethodException e){
            fail("NoSuchMethodException");
        }

    }

    /**
     * Negative test getSimpleProperty on an indexed property.
     */
    public void testGetSimpleIndexed(){

        try{
            PropertyUtils.getSimpleProperty(bean, "intIndexed[0]");
            fail("Should have thrown IllegalArgumentException");
        }catch (final IllegalAccessException e){
            fail("IllegalAccessException");
        }catch (final IllegalArgumentException e){
            // Correct result for this test
        }catch (final InvocationTargetException e){
            fail("InvocationTargetException");
        }catch (final NoSuchMethodException e){
            fail("NoSuchMethodException");
        }

    }

    /**
     * Test getSimpleProperty on an int property.
     */
    public void testGetSimpleInt(){

        try{
            final Object value = PropertyUtils.getSimpleProperty(bean, "intProperty");
            assertNotNull("Got a value", value);
            assertTrue("Got correct type", (value instanceof Integer));
            assertEquals("Got correct value", ((Integer) value).intValue(), bean.getIntProperty());
        }catch (final IllegalAccessException e){
            fail("IllegalAccessException");
        }catch (final IllegalArgumentException e){
            fail("IllegalArgumentException");
        }catch (final InvocationTargetException e){
            fail("InvocationTargetException");
        }catch (final NoSuchMethodException e){
            fail("NoSuchMethodException");
        }

    }

    /**
     * Test getSimpleProperty on a long property.
     */
    public void testGetSimpleLong(){

        try{
            final Object value = PropertyUtils.getSimpleProperty(bean, "longProperty");
            assertNotNull("Got a value", value);
            assertTrue("Got correct type", (value instanceof Long));
            assertEquals("Got correct value", ((Long) value).longValue(), bean.getLongProperty());
        }catch (final IllegalAccessException e){
            fail("IllegalAccessException");
        }catch (final IllegalArgumentException e){
            fail("IllegalArgumentException");
        }catch (final InvocationTargetException e){
            fail("InvocationTargetException");
        }catch (final NoSuchMethodException e){
            fail("NoSuchMethodException");
        }

    }

    /**
     * Negative test getSimpleProperty on a nested property.
     */
    public void testGetSimpleNested(){

        try{
            PropertyUtils.getSimpleProperty(bean, "nested.stringProperty");
            fail("Should have thrown IllegaArgumentException");
        }catch (final IllegalAccessException e){
            fail("IllegalAccessException");
        }catch (final IllegalArgumentException e){
            // Correct result for this test
        }catch (final InvocationTargetException e){
            fail("InvocationTargetException");
        }catch (final NoSuchMethodException e){
            fail("NoSuchMethodException");
        }

    }

    /**
     * Test getSimpleProperty on a read-only String property.
     */
    public void testGetSimpleReadOnly(){

        try{
            final Object value = PropertyUtils.getSimpleProperty(bean, "readOnlyProperty");
            assertNotNull("Got a value", value);
            assertTrue("Got correct type", (value instanceof String));
            assertEquals("Got correct value", (String) value, bean.getReadOnlyProperty());
        }catch (final IllegalAccessException e){
            fail("IllegalAccessException");
        }catch (final IllegalArgumentException e){
            fail("IllegalArgumentException");
        }catch (final InvocationTargetException e){
            fail("InvocationTargetException");
        }catch (final NoSuchMethodException e){
            fail("NoSuchMethodException");
        }

    }

    /**
     * Test getSimpleProperty on a short property.
     */
    public void testGetSimpleShort(){

        try{
            final Object value = PropertyUtils.getSimpleProperty(bean, "shortProperty");
            assertNotNull("Got a value", value);
            assertTrue("Got correct type", (value instanceof Short));
            assertEquals("Got correct value", ((Short) value).shortValue(), bean.getShortProperty());
        }catch (final IllegalAccessException e){
            fail("IllegalAccessException");
        }catch (final IllegalArgumentException e){
            fail("IllegalArgumentException");
        }catch (final InvocationTargetException e){
            fail("InvocationTargetException");
        }catch (final NoSuchMethodException e){
            fail("NoSuchMethodException");
        }

    }

    /**
     * Test getSimpleProperty on a String property.
     */
    public void testGetSimpleString(){

        try{
            final Object value = PropertyUtils.getSimpleProperty(bean, "stringProperty");
            assertNotNull("Got a value", value);
            assertTrue("Got correct type", (value instanceof String));
            assertEquals("Got correct value", (String) value, bean.getStringProperty());
        }catch (final IllegalAccessException e){
            fail("IllegalAccessException");
        }catch (final IllegalArgumentException e){
            fail("IllegalArgumentException");
        }catch (final InvocationTargetException e){
            fail("InvocationTargetException");
        }catch (final NoSuchMethodException e){
            fail("NoSuchMethodException");
        }

    }

    /**
     * Negative test getSimpleProperty on an unknown property.
     */
    public void testGetSimpleUnknown(){

        try{
            PropertyUtils.getSimpleProperty(bean, "unknown");
            fail("Should have thrown NoSuchMethodException");
        }catch (final IllegalAccessException e){
            fail("IllegalAccessException");
        }catch (final IllegalArgumentException e){
            fail("IllegalArgumentException");
        }catch (final InvocationTargetException e){
            fail("InvocationTargetException");
        }catch (final NoSuchMethodException e){
            // Correct result for this test
            assertEquals("Unknown property 'unknown' on class '" + bean.getClass() + "'", e.getMessage());
        }

    }

    /**
     * Test getSimpleProperty on a write-only String property.
     */
    public void testGetSimpleWriteOnly(){

        try{
            PropertyUtils.getSimpleProperty(bean, "writeOnlyProperty");
            fail("Should have thrown NoSuchMethodException");
        }catch (final IllegalAccessException e){
            fail("IllegalAccessException");
        }catch (final IllegalArgumentException e){
            fail("IllegalArgumentException");
        }catch (final InvocationTargetException e){
            fail("InvocationTargetException");
        }catch (final NoSuchMethodException e){
            // Correct result for this test
            assertEquals("Property 'writeOnlyProperty' has no getter method in class '" + bean.getClass() + "'", e.getMessage());
        }

    }

    /**
     * Test isWriteable() method.
     */
    public void testIsWriteable(){
        String property = null;
        try{
            property = "stringProperty";
            assertTrue("Property " + property + " isWriteable expeced TRUE", PropertyUtils.isWriteable(bean, property));
        }catch (final Throwable t){
            fail("Property " + property + " isWriteable Threw exception: " + t);
        }
        try{
            property = "stringIndexed";
            assertTrue("Property " + property + " isWriteable expeced TRUE", PropertyUtils.isWriteable(bean, property));
        }catch (final Throwable t){
            fail("Property " + property + " isWriteable Threw exception: " + t);
        }
        try{
            property = "mappedProperty";
            assertTrue("Property " + property + " isWriteable expeced TRUE", PropertyUtils.isWriteable(bean, property));
        }catch (final Throwable t){
            fail("Property " + property + " isWriteable Threw exception: " + t);
        }

        try{
            property = "nestedDynaBean";
            assertTrue("Property " + property + " isWriteable expeced TRUE", PropertyUtils.isWriteable(bean, property));
        }catch (final Throwable t){
            fail("Property " + property + " isWriteable Threw exception: " + t);
        }

        try{
            property = "nestedDynaBean.stringProperty";
            assertTrue("Property " + property + " isWriteable expeced TRUE", PropertyUtils.isWriteable(bean, property));
        }catch (final Throwable t){
            t.printStackTrace();
            fail("Property " + property + " isWriteable Threw exception: " + t);
        }

        try{
            property = "nestedDynaBean.nestedBean";
            assertTrue("Property " + property + " isWriteable expeced TRUE", PropertyUtils.isWriteable(bean, property));
        }catch (final Throwable t){
            fail("Property " + property + " isWriteable Threw exception: " + t);
        }

        try{
            property = "nestedDynaBean.nestedBean.nestedDynaBean";
            assertTrue("Property " + property + " isWriteable expeced TRUE", PropertyUtils.isWriteable(bean, property));
        }catch (final Throwable t){
            fail("Property " + property + " isWriteable Threw exception: " + t);
        }

        try{
            property = "nestedDynaBean.nestedBean.nestedDynaBean.stringProperty";
            assertTrue("Property " + property + " isWriteable expeced TRUE", PropertyUtils.isWriteable(bean, property));
        }catch (final Throwable t){
            fail("Property " + property + " isWriteable Threw exception: " + t);
        }

        try{
            property = "nestedDynaBean.nullDynaBean";
            assertTrue("Property " + property + " isWriteable expeced TRUE", PropertyUtils.isWriteable(bean, property));
        }catch (final Throwable t){
            fail("Property " + property + " isWriteable Threw exception: " + t);
        }

        try{
            property = "nestedDynaBean.nullDynaBean.foo";
            assertTrue("Property " + property + " isWriteable expeced TRUE", PropertyUtils.isWriteable(bean, property));
            fail("Property " + property + " isWriteable expected NestedNullException");
        }catch (final NestedNullException e){
            // expected result
        }catch (final Throwable t){
            fail("Property " + property + " isWriteable Threw exception: " + t);
        }
    }

    /**
     * Test the mappedPropertyType of MappedPropertyDescriptor.
     */
    public void testMappedPropertyType() throws Exception{

        MappedPropertyDescriptor desc;

        // Check a String property
        desc = (MappedPropertyDescriptor) PropertyUtils.getPropertyDescriptor(bean, "mappedProperty");
        assertEquals(String.class, desc.getMappedPropertyType());

        // Check an int property
        desc = (MappedPropertyDescriptor) PropertyUtils.getPropertyDescriptor(bean, "mappedIntProperty");
        assertEquals(Integer.TYPE, desc.getMappedPropertyType());

    }

    /**
     * Test setting an indexed value out of a multi-dimensional array
     */
    public void testSetIndexedArray(){
        final String[] firstArray = new String[] { "FIRST-1", "FIRST-2", "FIRST-3" };
        final String[] secondArray = new String[] { "SECOND-1", "SECOND-2", "SECOND-3", "SECOND-4" };
        final String[][] mainArray = { firstArray, secondArray };
        final TestBean bean = new TestBean(mainArray);
        assertEquals("BEFORE", "SECOND-3", bean.getString2dArray(1)[2]);
        try{
            PropertyUtils.setProperty(bean, "string2dArray[1][2]", "SECOND-3-UPDATED");
        }catch (final Throwable t){
            fail("Threw " + t + "");
        }
        assertEquals("AFTER", "SECOND-3-UPDATED", bean.getString2dArray(1)[2]);
    }

    /**
     * Test setting an indexed value out of List of Lists
     */
    public void testSetIndexedList(){
        final String[] firstArray = new String[] { "FIRST-1", "FIRST-2", "FIRST-3" };
        final String[] secondArray = new String[] { "SECOND-1", "SECOND-2", "SECOND-3", "SECOND-4" };
        final List<Object> mainList = new ArrayList<Object>();
        mainList.add(Arrays.asList(firstArray));
        mainList.add(Arrays.asList(secondArray));
        final TestBean bean = new TestBean(mainList);
        assertEquals("BEFORE", "SECOND-4", ((List<?>) bean.getListIndexed().get(1)).get(3));
        try{
            PropertyUtils.setProperty(bean, "listIndexed[1][3]", "SECOND-4-UPDATED");
        }catch (final Throwable t){
            fail("Threw " + t + "");
        }
        assertEquals("AFTER", "SECOND-4-UPDATED", ((List<?>) bean.getListIndexed().get(1)).get(3));
    }

    /**
     * Test setting a value out of a mapped Map
     */
    public void testSetIndexedMap(){
        final Map<String, Object> firstMap = new HashMap<String, Object>();
        firstMap.put("FIRST-KEY-1", "FIRST-VALUE-1");
        firstMap.put("FIRST-KEY-2", "FIRST-VALUE-2");
        final Map<String, Object> secondMap = new HashMap<String, Object>();
        secondMap.put("SECOND-KEY-1", "SECOND-VALUE-1");
        secondMap.put("SECOND-KEY-2", "SECOND-VALUE-2");

        final List<Object> mainList = new ArrayList<Object>();
        mainList.add(firstMap);
        mainList.add(secondMap);
        final TestBean bean = new TestBean(mainList);

        assertEquals("BEFORE", null, ((Map<?, ?>) bean.getListIndexed().get(0)).get("FIRST-NEW-KEY"));
        assertEquals("BEFORE", "SECOND-VALUE-1", ((Map<?, ?>) bean.getListIndexed().get(1)).get("SECOND-KEY-1"));
        try{
            PropertyUtils.setProperty(bean, "listIndexed[0](FIRST-NEW-KEY)", "FIRST-NEW-VALUE");
            PropertyUtils.setProperty(bean, "listIndexed[1](SECOND-KEY-1)", "SECOND-VALUE-1-UPDATED");
        }catch (final Throwable t){
            fail("Threw " + t + "");
        }
        assertEquals("BEFORE", "FIRST-NEW-VALUE", ((Map<?, ?>) bean.getListIndexed().get(0)).get("FIRST-NEW-KEY"));
        assertEquals("AFTER", "SECOND-VALUE-1-UPDATED", ((Map<?, ?>) bean.getListIndexed().get(1)).get("SECOND-KEY-1"));
    }

    /**
     * Test setting an indexed value out of a mapped array
     */
    public void testSetMappedArray(){
        final TestBean bean = new TestBean();
        final String[] array = new String[] { "abc", "def", "ghi" };
        bean.getMapProperty().put("mappedArray", array);

        assertEquals("BEFORE", "def", ((String[]) bean.getMapProperty().get("mappedArray"))[1]);
        try{
            PropertyUtils.setProperty(bean, "mapProperty(mappedArray)[1]", "DEF-UPDATED");
        }catch (final Throwable t){
            fail("Threw " + t + "");
        }
        assertEquals("AFTER", "DEF-UPDATED", ((String[]) bean.getMapProperty().get("mappedArray"))[1]);
    }

    /**
     * Test setting an indexed value out of a mapped List
     */
    public void testSetMappedList(){
        final TestBean bean = new TestBean();
        final List<Object> list = new ArrayList<Object>();
        list.add("klm");
        list.add("nop");
        list.add("qrs");
        bean.getMapProperty().put("mappedList", list);

        assertEquals("BEFORE", "klm", ((List<?>) bean.getMapProperty().get("mappedList")).get(0));
        try{
            PropertyUtils.setProperty(bean, "mapProperty(mappedList)[0]", "KLM-UPDATED");
        }catch (final Throwable t){
            fail("Threw " + t + "");
        }
        assertEquals("AFTER", "KLM-UPDATED", ((List<?>) bean.getMapProperty().get("mappedList")).get(0));
    }

    /**
     * Test setting a value out of a mapped Map
     */
    public void testSetMappedMap(){
        final TestBean bean = new TestBean();
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("sub-key-1", "sub-value-1");
        map.put("sub-key-2", "sub-value-2");
        map.put("sub-key-3", "sub-value-3");
        bean.getMapProperty().put("mappedMap", map);

        assertEquals("BEFORE", "sub-value-3", ((Map<?, ?>) bean.getMapProperty().get("mappedMap")).get("sub-key-3"));
        try{
            PropertyUtils.setProperty(bean, "mapProperty(mappedMap)(sub-key-3)", "SUB-KEY-3-UPDATED");
        }catch (final Throwable t){
            fail("Threw " + t + "");
        }
        assertEquals("AFTER", "SUB-KEY-3-UPDATED", ((Map<?, ?>) bean.getMapProperty().get("mappedMap")).get("sub-key-3"));
    }

    /**
     * Corner cases on setSimpleProperty invalid arguments.
     */
    public void testSetSimpleArguments(){

        try{
            PropertyUtils.setSimpleProperty(null, "stringProperty", "");
            fail("Should throw IllegalArgumentException 1");
        }catch (final IllegalArgumentException e){
            // Expected response
        }catch (final Throwable t){
            fail("Threw " + t + " instead of IllegalArgumentException 1");
        }

        try{
            PropertyUtils.setSimpleProperty(bean, null, "");
            fail("Should throw IllegalArgumentException 2");
        }catch (final IllegalArgumentException e){
            // Expected response
        }catch (final Throwable t){
            fail("Threw " + t + " instead of IllegalArgumentException 2");
        }

    }

    /**
     * Test setSimpleProperty on a boolean property.
     */
    public void testSetSimpleBoolean() throws Exception{

        final boolean oldValue = bean.getBooleanProperty();
        final boolean newValue = !oldValue;
        PropertyUtils.setSimpleProperty(bean, "booleanProperty", new Boolean(newValue));
        assertTrue("Matched new value", newValue == bean.getBooleanProperty());

    }

    /**
     * Test setSimpleProperty on a double property.
     */
    public void testSetSimpleDouble() throws Exception{

        final double oldValue = bean.getDoubleProperty();
        final double newValue = oldValue + 1.0;
        PropertyUtils.setSimpleProperty(bean, "doubleProperty", new Double(newValue));
        assertEquals("Matched new value", newValue, bean.getDoubleProperty(), 0.005);

    }

    /**
     * Test setSimpleProperty on a float property.
     */
    public void testSetSimpleFloat(){

        try{
            final float oldValue = bean.getFloatProperty();
            final float newValue = oldValue + (float) 1.0;
            PropertyUtils.setSimpleProperty(bean, "floatProperty", new Float(newValue));
            assertEquals("Matched new value", newValue, bean.getFloatProperty(), (float) 0.005);
        }catch (final IllegalAccessException e){
            fail("IllegalAccessException");
        }catch (final IllegalArgumentException e){
            fail("IllegalArgumentException");
        }catch (final InvocationTargetException e){
            fail("InvocationTargetException");
        }catch (final NoSuchMethodException e){
            fail("NoSuchMethodException");
        }

    }

    /**
     * Negative test setSimpleProperty on an indexed property.
     */
    public void testSetSimpleIndexed() throws Exception{

        try{
            PropertyUtils.setSimpleProperty(bean, "stringIndexed[0]", "New String Value");
            fail("Should have thrown IllegalArgumentException");
        }catch (final IllegalAccessException e){
            fail("IllegalAccessException");
        }catch (final IllegalArgumentException e){
            // Correct result for this test
        }catch (final InvocationTargetException e){
            fail("InvocationTargetException");
        }catch (final NoSuchMethodException e){
            fail("NoSuchMethodException");
        }

    }

    /**
     * Test setSimpleProperty on a int property.
     */
    public void testSetSimpleInt() throws Exception{

        final int oldValue = bean.getIntProperty();
        final int newValue = oldValue + 1;
        PropertyUtils.setSimpleProperty(bean, "intProperty", new Integer(newValue));
        assertEquals("Matched new value", newValue, bean.getIntProperty());

    }

    /**
     * Test setSimpleProperty on a long property.
     */
    public void testSetSimpleLong() throws Exception{

        final long oldValue = bean.getLongProperty();
        final long newValue = oldValue + 1;
        PropertyUtils.setSimpleProperty(bean, "longProperty", new Long(newValue));
        assertEquals("Matched new value", newValue, bean.getLongProperty());

    }

    /**
     * Negative test setSimpleProperty on a nested property.
     */
    public void testSetSimpleNested() throws Exception{

        try{
            PropertyUtils.setSimpleProperty(bean, "nested.stringProperty", "New String Value");
            fail("Should have thrown IllegalArgumentException");
        }catch (final IllegalArgumentException e){
            // Correct result for this test
        }
    }

    /**
     * Test setSimpleProperty on a read-only String property.
     */
    public void testSetSimpleReadOnly() throws Exception{

        try{
            final String oldValue = bean.getWriteOnlyPropertyValue();
            final String newValue = oldValue + " Extra Value";
            PropertyUtils.setSimpleProperty(bean, "readOnlyProperty", newValue);
            fail("Should have thrown NoSuchMethodException");
        }catch (final NoSuchMethodException e){
            // Correct result for this test
            assertEquals("Property 'readOnlyProperty' has no setter method in class '" + bean.getClass() + "'", e.getMessage());
        }

    }

    /**
     * Test setSimpleProperty on a short property.
     */
    public void testSetSimpleShort() throws Exception{

        final short oldValue = bean.getShortProperty();
        short newValue = oldValue;
        newValue++;
        PropertyUtils.setSimpleProperty(bean, "shortProperty", new Short(newValue));
        assertEquals("Matched new value", newValue, bean.getShortProperty());

    }

    /**
     * Test setSimpleProperty on a String property.
     */
    public void testSetSimpleString() throws Exception{

        final String oldValue = bean.getStringProperty();
        final String newValue = oldValue + " Extra Value";
        PropertyUtils.setSimpleProperty(bean, "stringProperty", newValue);
        assertEquals("Matched new value", newValue, bean.getStringProperty());

    }

    /**
     * Test setSimpleProperty on an unknown property name.
     */
    public void testSetSimpleUnknown(){

        try{
            final String newValue = "New String Value";
            PropertyUtils.setSimpleProperty(bean, "unknown", newValue);
            fail("Should have thrown NoSuchMethodException");
        }catch (final IllegalAccessException e){
            fail("IllegalAccessException");
        }catch (final IllegalArgumentException e){
            fail("IllegalArgumentException");
        }catch (final InvocationTargetException e){
            fail("InvocationTargetException");
        }catch (final NoSuchMethodException e){
            // Correct result for this test
            assertEquals("Unknown property 'unknown' on class '" + bean.getClass() + "'", e.getMessage());
        }

    }

    /**
     * Test setSimpleProperty on a write-only String property.
     * 
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public void testSetSimpleWriteOnly() throws IllegalAccessException,InvocationTargetException,NoSuchMethodException{

        final String oldValue = bean.getWriteOnlyPropertyValue();
        final String newValue = oldValue + " Extra Value";
        PropertyUtils.setSimpleProperty(bean, "writeOnlyProperty", newValue);
        assertEquals("Matched new value", newValue, bean.getWriteOnlyPropertyValue());

    }

    // ------------------------------------------------------ Protected Methods

    /**
     * Base for testGetDescriptorXxxxx() series of tests.
     *
     * @param name
     *            Name of the property to be retrieved
     * @param read
     *            Expected name of the read method (or null)
     * @param write
     *            Expected name of the write method (or null)
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    protected void testGetDescriptorBase(final String name,final String read,final String write)
                    throws IllegalAccessException,InvocationTargetException,NoSuchMethodException{

        final PropertyDescriptor pd = PropertyUtils.getPropertyDescriptor(bean, name);
        if ((read != null) || (write != null)){
            assertNotNull("Got descriptor", pd);
        }else{
            assertNull("Got descriptor", pd);
            return;
        }
        final Method rm = pd.getReadMethod();
        if (read != null){
            assertNotNull("Got read method", rm);
            assertEquals("Got correct read method", rm.getName(), read);
        }else{
            assertNull("Got read method", rm);
        }
        final Method wm = pd.getWriteMethod();
        if (write != null){
            assertNotNull("Got write method", wm);
            assertEquals("Got correct write method", wm.getName(), write);
        }else{
            assertNull("Got write method", wm);
        }

    }

    public void testNestedWithIndex() throws Exception{
        final NestedTestBean nestedBean = new NestedTestBean("base");
        nestedBean.init();
        nestedBean.getSimpleBeanProperty().init();

        NestedTestBean

        // test first calling properties on indexed beans

        value = (NestedTestBean) PropertyUtils.getProperty(nestedBean, "indexedProperty[0]");
        assertEquals("Cannot get simple index(1)", "Bean@0", value.getName());
        assertEquals("Bug in NestedTestBean", "NOT SET", value.getTestString());

        value = (NestedTestBean) PropertyUtils.getProperty(nestedBean, "indexedProperty[1]");
        assertEquals("Cannot get simple index(1)", "Bean@1", value.getName());
        assertEquals("Bug in NestedTestBean", "NOT SET", value.getTestString());

        String prop = (String) PropertyUtils.getProperty(nestedBean, "indexedProperty[0].testString");
        assertEquals("Get property on indexes failed (1)", "NOT SET", prop);

        prop = (String) PropertyUtils.getProperty(nestedBean, "indexedProperty[1].testString");
        assertEquals("Get property on indexes failed (2)", "NOT SET", prop);

        PropertyUtils.setProperty(nestedBean, "indexedProperty[0].testString", "Test#1");
        assertEquals("Cannot set property on indexed bean (1)", "Test#1", nestedBean.getIndexedProperty(0).getTestString());

        PropertyUtils.setProperty(nestedBean, "indexedProperty[1].testString", "Test#2");
        assertEquals("Cannot set property on indexed bean (2)", "Test#2", nestedBean.getIndexedProperty(1).getTestString());

        // test first calling indexed properties on a simple property

        value = (NestedTestBean) PropertyUtils.getProperty(nestedBean, "simpleBeanProperty");
        assertEquals("Cannot get simple bean", "Simple Property Bean", value.getName());
        assertEquals("Bug in NestedTestBean", "NOT SET", value.getTestString());

        value = (NestedTestBean) PropertyUtils.getProperty(nestedBean, "simpleBeanProperty.indexedProperty[3]");
        assertEquals("Cannot get index property on property", "Bean@3", value.getName());
        assertEquals("Bug in NestedTestBean", "NOT SET", value.getTestString());

        PropertyUtils.setProperty(nestedBean, "simpleBeanProperty.indexedProperty[3].testString", "Test#3");
        assertEquals(
                        "Cannot set property on indexed property on property",
                        "Test#3",
                        nestedBean.getSimpleBeanProperty().getIndexedProperty(3).getTestString());
    }

    /** Text case for setting properties on inner classes */
    public void testGetSetInnerBean() throws Exception{
        final BeanWithInnerBean bean = new BeanWithInnerBean();

        PropertyUtils.setProperty(bean, "innerBean.fish(loiterTimer)", "5");
        String out = (String) PropertyUtils.getProperty(bean.getInnerBean(), "fish(loiterTimer)");
        assertEquals("(1) Inner class property set/get property failed.", "5", out);

        out = (String) PropertyUtils.getProperty(bean, "innerBean.fish(loiterTimer)");

        assertEquals("(2) Inner class property set/get property failed.", "5", out);
    }

    /** Text case for setting properties on parent */
    public void testGetSetParentBean() throws Exception{

        final SonOfAlphaBean bean = new SonOfAlphaBean("Roger");

        final String out = (String) PropertyUtils.getProperty(bean, "name");
        assertEquals("(1) Get/Set On Parent.", "Roger", out);

        PropertyUtils.setProperty(bean, "name", "abcd");
        assertEquals("(2) Get/Set On Parent.", "abcd", bean.getName());
    }

    /**
     * Test accessing a public sub-bean of a package scope bean
     */
    public void testSetPublicSubBean_of_PackageBean(){

        final PublicSubBean bean = new PublicSubBean();
        bean.setFoo("foo-start");
        bean.setBar("bar-start");

        // Set Foo
        try{
            PropertyUtils.setProperty(bean, "foo", "foo-updated");
        }catch (final Throwable t){
            fail("setProperty(foo) threw " + t);
        }
        assertEquals("foo property", "foo-updated", bean.getFoo());

        // Set Bar
        try{
            PropertyUtils.setProperty(bean, "bar", "bar-updated");
        }catch (final Throwable t){
            fail("setProperty(bar) threw " + t);
        }
        assertEquals("bar property", "bar-updated", bean.getBar());
    }

    /**
     * Returns a single string containing all the keys in the map,
     * sorted in alphabetical order and separated by ", ".
     * <p>
     * If there are no keys, an empty string is returned.
     */
    private String keysToString(final Map<?, ?> map){
        final Object[] mapKeys = map.keySet().toArray();
        java.util.Arrays.sort(mapKeys);
        final StringBuilder buf = new StringBuilder();
        for (int i = 0; i < mapKeys.length; ++i){
            if (i != 0){
                buf.append(", ");
            }
            buf.append(mapKeys[i]);
        }
        return buf.toString();
    }

    /**
     * Test {@link PropertyUtilsBean}'s invoke method throwing an IllegalArgumentException
     * and check that the "cause" has been properly initialized for JDK 1.4+
     * See BEANUTILS-266 for changes and reason for test
     */
    public void testExceptionFromInvoke() throws Exception{
        if (BeanUtilsTestCase.isPre14JVM()){
            return;
        }
        try{
            PropertyUtils.setSimpleProperty(bean, "intProperty", "XXX");
        }catch (final IllegalArgumentException t){
            final Throwable cause = (Throwable) PropertyUtils.getProperty(t, "cause");
            assertNotNull("Cause not found", cause);
            assertTrue(
                            "Expected cause to be IllegalArgumentException, but was: " + cause.getClass(),
                            cause instanceof IllegalArgumentException);
            // JDK 1.6 doesn't have "argument type mismatch" message
            // assertEquals("Check error message", "argument type mismatch", cause.getMessage());
        }catch (final Throwable t){
            fail("Expected IllegalArgumentException, but threw " + t);
        }
    }

    /**
     * Finds the descriptor of the name property.
     *
     * @param desc
     *            the array with descriptors
     * @return the found descriptor or null
     */
    private static PropertyDescriptor findNameDescriptor(final PropertyDescriptor[] desc){
        for (PropertyDescriptor element : desc){
            if (element.getName().equals("name")){
                return element;
            }
        }
        return null;
    }
}
