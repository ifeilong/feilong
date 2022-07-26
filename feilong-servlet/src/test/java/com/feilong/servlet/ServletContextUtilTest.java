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
package com.feilong.servlet;

import static com.feilong.core.bean.ConvertUtil.toList;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;

import java.util.List;

import org.hamcrest.MatcherAssert;
import org.junit.Test;

/**
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 3.2.0
 */
public class ServletContextUtilTest{

    @Test
    public void test(){
        List<String> list = toList(ServletContextUtil.IGNORE_KEYS);
        MatcherAssert.assertThat(
                        list,
                        allOf(
                                        hasItem("org.apache.catalina.jsp_classpath"),
                                        hasItem("org.apache.catalina.resources"),
                                        hasItem("org.apache.jasper.compiler.ELInterpreter"),
                                        hasItem("org.apache.jasper.compiler.TldLocationsCache"),
                                        hasItem("org.apache.jasper.runtime.JspApplicationContextImpl"),
                                        hasItem("org.apache.tomcat.InstanceManager"),
                                        hasItem("org.apache.tomcat.JarScanner"),
                                        hasItem("org.apache.tomcat.util.scan.MergedWebXml"),
                                        not(hasItem("111"))));

    }
}
