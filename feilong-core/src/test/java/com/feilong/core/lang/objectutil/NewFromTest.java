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
package com.feilong.core.lang.objectutil;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.feilong.core.lang.ObjectUtil;
import com.feilong.store.member.User;

public class NewFromTest{

    @Test(expected = NullPointerException.class)
    public void nullClass(){
        ObjectUtil.newFrom(null, null);
    }

    @Test
    public void nullFrom(){
        User user = ObjectUtil.newFrom(User.class, null);

        assertThat(user, allOf(hasProperty("id", is(0L)), hasProperty("name", is("feilong"))));
    }

    //---------------------------------------------------------------

}