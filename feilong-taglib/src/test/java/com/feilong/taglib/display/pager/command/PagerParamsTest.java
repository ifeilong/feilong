/*
 * Copyright (C) 2008 feilong
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.feilong.taglib.display.pager.command;

import static com.feilong.core.CharsetType.UTF8;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class PagerParamsTest.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.0.7
 */
public class PagerParamsTest{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(PagerParamsTest.class);

    /**
     * Test hash code.
     */
    @Test
    @SuppressWarnings("static-method")
    public void testHashCode(){
        PagerParams pagerParams1 = new PagerParams(0, "a");

        LOGGER.debug("" + pagerParams1.hashCode());
        pagerParams1.setCharsetType(null);
        LOGGER.debug("" + pagerParams1.hashCode());
    }

    /**
     * Test equals object.
     */
    @SuppressWarnings({ "cast", "static-method" })
    @Test
    public void testEqualsObject(){
        PagerParams pagerParams1 = new PagerParams(0, "a");
        PagerParams pagerParams2 = new PagerParams(0, "a");

        assertEquals(true, pagerParams1.equals(pagerParams1));
        assertEquals(false, pagerParams1.equals(null));
        assertEquals(true, pagerParams1.equals(pagerParams2));

        pagerParams2.setCharsetType(UTF8);
        assertEquals(false, pagerParams1.equals(pagerParams2));

        pagerParams1.setCharsetType(UTF8);
        assertEquals(true, pagerParams1.equals(pagerParams2));

        pagerParams1.setCharsetType(null);
        assertEquals(false, pagerParams1.equals(pagerParams2));
        assertEquals(false, (null instanceof PagerParams));
        assertEquals(true, (pagerParams1 instanceof PagerParams));
        assertEquals(false, (null instanceof Object));
    }
}
