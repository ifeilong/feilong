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
package com.feilong.context.log;

import static com.feilong.core.Validator.isNotNullOrEmpty;

import com.feilong.core.util.ServiceLoaderUtil;

import lombok.extern.slf4j.Slf4j;

/** The Constant log. */
@Slf4j
public class ContextLogCreator{

    /**
     * 创建固定的上下文.
     * 
     * <p>
     * 分为两部分, 第一部分会自动获取http请求参数和地址 (如果是http请求 比如controller ),第二部分支持获取自定义的上下文, 比如后台应用 获取登录用户id name ; rpc应用获取调用应用名字和ip等等.
     * </p>
     *
     * @return 如果有异常, 返回exception:和异常信息
     * @since 4.4.0
     */
    public static String getContextLog(){
        StringBuilder sb = new StringBuilder();

        //---------------------------------------------------------------
        //http的上下文
        FixedContextLogCreator fixedContextLogCreator = HttpSpringFixedContextLogCreator.INSTANCE;
        String httpContextLog = fixedContextLogCreator.createContextLog();
        if (isNotNullOrEmpty(httpContextLog)){//实现已经处理了异常
            sb.append(httpContextLog).append(" ");
        }

        //---------------------------------------------------------------
        //自定义的固定上下文

        //此处可以有登录用户的id  name 等; 或者rpc 调用的应用和ip 等
        String customFixContextLog = getCustomFixContextLog();
        if (isNotNullOrEmpty(customFixContextLog)){ //实现已经处理了异常
            sb.append(customFixContextLog).append(" ");
        }

        return sb.toString();
    }

    //---------------------------------------------------------------

    /**
     * 基于SPI（Service Provider Interface） 获取自定义固定context log,比如后台应用 获取登录用户id name ; rpc应用获取调用应用名字和ip等等.
     * 
     * <h3>项目实现步骤:</h3>
     * <blockquote>
     * <ol>
     * 
     * <li>需要在具体项目 META-INF/services/目录下查找以接口全限定名命名的文件（如com.feilong.context.log.CustomFixContextLogCreator） <br>
     * 文件内容为实现类的全限定名​（每行一个） 比如 com.project.CustomFixContextLogCreator</li>
     * 
     * <li>若存在多个实现类，ServiceLoader按配置文件的声明顺序加载并返回迭代器</li>
     * </ol>
     * </blockquote>
     * 
     * ServiceLoader通过线程上下文类加载器（Thread.currentThread().getContextClassLoader()）加载配置文件中声明的实现类
     * 利用反射（Class.forName().newInstance()）创建实现类的实例，并缓存到LinkedHashMap中;
     * 
     * 具体的使用案例有
     * 
     * <h3>说明:</h3>
     * <blockquote>
     * <ol>
     * <li>日志框架适配 SLF4J作为日志门面，通过SPI绑定具体实现（如Logback、Log4j2) 在slf4j-log4j12.jar中配置org.slf4j.spi.SLF4JServiceProvider的实现类</li>
     * <li>Spring Boot自动装配​ Spring Boot通过扩展SPI机制（spring.factories文件）实现自动配置类加载。 在spring-boot-autoconfigure.jar的META-INF/spring.factories中定义
     * org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
     * com.example.MyAutoConfiguration</li>
     * <li>JDBC数据库驱动加载​</li>
     * </ol>
     * </blockquote>
     *
     * @return the custom fix context log
     * @since 4.4.0
     */
    private static String getCustomFixContextLog(){
        try{
            CustomFixedContextLogCreator customFixedContextLogCreator = ServiceLoaderUtil.loadFirst(CustomFixedContextLogCreator.class);
            // 无实现时返回null
            return customFixedContextLogCreator == null ? null : customFixedContextLogCreator.createContextLog();
        }catch (Throwable e){
            return "CustomFixedContextLogCreatorException:" + e.getMessage();
        }
    }

}
