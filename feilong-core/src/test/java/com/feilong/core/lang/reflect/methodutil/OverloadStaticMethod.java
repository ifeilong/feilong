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
package com.feilong.core.lang.reflect.methodutil;

public class OverloadStaticMethod{

    public static String age(int age){
        return "static age int:" + age;
    }

    public static String age(Integer age){
        return "static age Integer:" + age;
    }

    @SuppressWarnings("unused")
    private static String agePrivate(Integer age){
        return "static age Integer:" + age;
    }

    @SuppressWarnings("unused")
    private static <T> String agePrivateWithClass(String age,Class<T> klass){
        return "static age Integer:" + age;
    }
}
