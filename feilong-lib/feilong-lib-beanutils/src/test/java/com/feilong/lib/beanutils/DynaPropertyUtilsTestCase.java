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

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.DynaBean;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Test accessing DynaBeans transparently via PropertyUtils.
 *
 * @version $Id$
 */

public class DynaPropertyUtilsTestCase extends TestCase{

    // ----------------------------------------------------- Instance Variables

    /**
     * The basic test bean for each test.
     */
    protected DynaBean bean        = null;

    /**
     * The set of properties that should be described.
     */
    protected String   describes[] = {
                                       "booleanProperty",
                                       "booleanSecond",
                                       "doubleProperty",
                                       "floatProperty",
                                       "intArray",
                                       "intIndexed",
                                       "intProperty",
                                       "listIndexed",
                                       "longProperty",
                                       "mappedObjects",
                                       "mappedProperty",
                                       "mappedIntProperty",
                                       "nested",
                                       "nullProperty",
                                       //      "readOnlyProperty",
                                       "shortProperty",
                                       "stringArray",
                                       "stringIndexed",
                                       "stringProperty" };

    /**
     * The nested bean pointed at by the "nested" property.
     */
    protected TestBean nested      = null;

    // ----------------------------------------------------------- Constructors

    /**
     * Construct a new instance of this test case.
     *
     * @param name
     *            Name of the test case
     */
    public DynaPropertyUtilsTestCase(final String name){

        super(name);

    }

    // --------------------------------------------------- Overall Test Methods

    /**
     * Set up instance variables required by this test case.
     */
    @Override
    public void setUp() throws Exception{

        // Instantiate a new DynaBean instance
        final DynaClass dynaClass = createDynaClass();
        bean = dynaClass.newInstance();

        // Initialize the DynaBean's property values (like TestBean)
        bean.set("booleanProperty", new Boolean(true));
        bean.set("booleanSecond", new Boolean(true));
        bean.set("doubleProperty", new Double(321.0));
        bean.set("floatProperty", new Float((float) 123.0));
        final int intArray[] = { 0, 10, 20, 30, 40 };
        bean.set("intArray", intArray);
        final int intIndexed[] = { 0, 10, 20, 30, 40 };
        bean.set("intIndexed", intIndexed);
        bean.set("intProperty", new Integer(123));
        final List<String> listIndexed = new ArrayList<String>();
        listIndexed.add("String 0");
        listIndexed.add("String 1");
        listIndexed.add("String 2");
        listIndexed.add("String 3");
        listIndexed.add("String 4");
        bean.set("listIndexed", listIndexed);
        bean.set("longProperty", new Long(321));
        final HashMap<String, Object> mapProperty = new HashMap<String, Object>();
        mapProperty.put("First Key", "First Value");
        mapProperty.put("Second Key", "Second Value");
        bean.set("mapProperty", mapProperty);
        final HashMap<String, Object> mappedObjects = new HashMap<String, Object>();
        mappedObjects.put("First Key", "First Value");
        mappedObjects.put("Second Key", "Second Value");
        bean.set("mappedObjects", mappedObjects);
        final HashMap<String, Object> mappedProperty = new HashMap<String, Object>();
        mappedProperty.put("First Key", "First Value");
        mappedProperty.put("Second Key", "Second Value");
        bean.set("mappedProperty", mappedProperty);
        final HashMap<String, Integer> mappedIntProperty = new HashMap<String, Integer>();
        mappedIntProperty.put("One", new Integer(1));
        mappedIntProperty.put("Two", new Integer(2));
        bean.set("mappedIntProperty", mappedIntProperty);
        nested = new TestBean();
        bean.set("nested", nested);
        // Property "nullProperty" is not initialized, so it should return null
        bean.set("shortProperty", new Short((short) 987));
        final String stringArray[] = { "String 0", "String 1", "String 2", "String 3", "String 4" };
        bean.set("stringArray", stringArray);
        final String stringIndexed[] = { "String 0", "String 1", "String 2", "String 3", "String 4" };
        bean.set("stringIndexed", stringIndexed);
        bean.set("stringProperty", "This is a string");

    }

    /**
     * Return the tests included in this test suite.
     */
    public static Test suite(){

        return (new TestSuite(DynaPropertyUtilsTestCase.class));

    }

    /**
     * Tear down instance variables required by this test case.
     */
    @Override
    public void tearDown(){

        bean = null;
        nested = null;

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
        assertEquals("booleanProperty", false, ((Boolean) bean.get("booleanProperty")).booleanValue());
        assertEquals("doubleProperty", 333.0, ((Double) bean.get("doubleProperty")).doubleValue(), 0.005);
        assertEquals("floatProperty", (float) 222.0, ((Float) bean.get("floatProperty")).floatValue(), (float) 0.005);
        assertEquals("intProperty", 111, ((Integer) bean.get("intProperty")).intValue());
        assertEquals("longProperty", 444, ((Long) bean.get("longProperty")).longValue());
        assertEquals("shortProperty", (short) 555, ((Short) bean.get("shortProperty")).shortValue());
        assertEquals("stringProperty", "New String Property", (String) bean.get("stringProperty"));

        // Indexed Properties
        final String dupProperty[] = (String[]) bean.get("dupProperty");
        assertNotNull("dupProperty present", dupProperty);
        assertEquals("dupProperty length", 3, dupProperty.length);
        assertEquals("dupProperty[0]", "New 0", dupProperty[0]);
        assertEquals("dupProperty[1]", "New 1", dupProperty[1]);
        assertEquals("dupProperty[2]", "New 2", dupProperty[2]);
        final int intArray[] = (int[]) bean.get("intArray");
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
            assertTrue("Got correct value", ((Boolean) value).booleanValue() == true);
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
            assertEquals("Got correct value", ((Double) value).doubleValue(), 321.0, 0.005);
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
            assertEquals("Got correct value", ((Float) value).floatValue(), (float) 123.0, (float) 0.005);
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
            assertEquals("Got correct value", ((Integer) value).intValue(), 123);
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
            assertEquals("Got correct value", ((Long) value).longValue(), 321);
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
     * Test getSimpleProperty on a short property.
     */
    public void testGetSimpleShort(){

        try{
            final Object value = PropertyUtils.getSimpleProperty(bean, "shortProperty");
            assertNotNull("Got a value", value);
            assertTrue("Got correct type", (value instanceof Short));
            assertEquals("Got correct value", ((Short) value).shortValue(), (short) 987);
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
            assertEquals("Got correct value", (String) value, "This is a string");
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
            assertEquals("Unknown property 'unknown' on dynaclass '" + bean.getDynaClass() + "'", e.getMessage());
        }

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
    public void testSetSimpleBoolean(){

        try{
            final boolean oldValue = ((Boolean) bean.get("booleanProperty")).booleanValue();
            final boolean newValue = !oldValue;
            PropertyUtils.setSimpleProperty(bean, "booleanProperty", new Boolean(newValue));
            assertTrue("Matched new value", newValue == ((Boolean) bean.get("booleanProperty")).booleanValue());
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
     * Test setSimpleProperty on a double property.
     */
    public void testSetSimpleDouble(){

        try{
            final double oldValue = ((Double) bean.get("doubleProperty")).doubleValue();
            final double newValue = oldValue + 1.0;
            PropertyUtils.setSimpleProperty(bean, "doubleProperty", new Double(newValue));
            assertEquals("Matched new value", newValue, ((Double) bean.get("doubleProperty")).doubleValue(), 0.005);
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
     * Test setSimpleProperty on a float property.
     */
    public void testSetSimpleFloat(){

        try{
            final float oldValue = ((Float) bean.get("floatProperty")).floatValue();
            final float newValue = oldValue + (float) 1.0;
            PropertyUtils.setSimpleProperty(bean, "floatProperty", new Float(newValue));
            assertEquals("Matched new value", newValue, ((Float) bean.get("floatProperty")).floatValue(), (float) 0.005);
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
    public void testSetSimpleIndexed(){

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
    public void testSetSimpleInt(){

        try{
            final int oldValue = ((Integer) bean.get("intProperty")).intValue();
            final int newValue = oldValue + 1;
            PropertyUtils.setSimpleProperty(bean, "intProperty", new Integer(newValue));
            assertEquals("Matched new value", newValue, ((Integer) bean.get("intProperty")).intValue());
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
     * Test setSimpleProperty on a long property.
     */
    public void testSetSimpleLong(){

        try{
            final long oldValue = ((Long) bean.get("longProperty")).longValue();
            final long newValue = oldValue + 1;
            PropertyUtils.setSimpleProperty(bean, "longProperty", new Long(newValue));
            assertEquals("Matched new value", newValue, ((Long) bean.get("longProperty")).longValue());
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
     * Negative test setSimpleProperty on a nested property.
     */
    public void testSetSimpleNested(){

        try{
            PropertyUtils.setSimpleProperty(bean, "nested.stringProperty", "New String Value");
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
     * Test setSimpleProperty on a short property.
     */
    public void testSetSimpleShort(){

        try{
            final short oldValue = ((Short) bean.get("shortProperty")).shortValue();
            short newValue = oldValue;
            newValue++;
            PropertyUtils.setSimpleProperty(bean, "shortProperty", new Short(newValue));
            assertEquals("Matched new value", newValue, ((Short) bean.get("shortProperty")).shortValue());
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
     * Test setSimpleProperty on a String property.
     */
    public void testSetSimpleString(){

        try{
            final String oldValue = (String) bean.get("stringProperty");
            final String newValue = oldValue + " Extra Value";
            PropertyUtils.setSimpleProperty(bean, "stringProperty", newValue);
            assertEquals("Matched new value", newValue, (String) bean.get("stringProperty"));
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
            assertEquals("Unknown property 'unknown' on dynaclass '" + bean.getDynaClass() + "'", e.getMessage());
        }

    }

    // ------------------------------------------------------ Protected Methods

    /**
     * Create and return a <code>DynaClass</code> instance for our test
     * <code>DynaBean</code>.
     */
    protected DynaClass createDynaClass(){

        final int intArray[] = new int[0];
        final String stringArray[] = new String[0];

        final DynaClass dynaClass = new BasicDynaClass(
                        "TestDynaClass",
                        null,
                        new DynaProperty[] {
                                             new DynaProperty("booleanProperty", Boolean.TYPE),
                                             new DynaProperty("booleanSecond", Boolean.TYPE),
                                             new DynaProperty("doubleProperty", Double.TYPE),
                                             new DynaProperty("dupProperty", stringArray.getClass()),
                                             new DynaProperty("floatProperty", Float.TYPE),
                                             new DynaProperty("intArray", intArray.getClass()),
                                             new DynaProperty("intIndexed", intArray.getClass()),
                                             new DynaProperty("intProperty", Integer.TYPE),
                                             new DynaProperty("listIndexed", List.class),
                                             new DynaProperty("longProperty", Long.TYPE),
                                             new DynaProperty("mapProperty", Map.class),
                                             new DynaProperty("mappedObjects", Map.class),
                                             new DynaProperty("mappedProperty", Map.class),
                                             new DynaProperty("mappedIntProperty", Map.class),
                                             new DynaProperty("nested", TestBean.class),
                                             new DynaProperty("nullProperty", String.class),
                                             new DynaProperty("shortProperty", Short.TYPE),
                                             new DynaProperty("stringArray", stringArray.getClass()),
                                             new DynaProperty("stringIndexed", stringArray.getClass()),
                                             new DynaProperty("stringProperty", String.class), });
        return (dynaClass);

    }

}
