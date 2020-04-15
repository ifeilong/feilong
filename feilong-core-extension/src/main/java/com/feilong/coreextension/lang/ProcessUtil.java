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
package com.feilong.coreextension.lang;

import java.io.IOException;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.core.UncheckedIOException;
import com.feilong.core.lang.StringUtil;
import com.feilong.json.jsonlib.JsonUtil;

/**
 * The Class ProcessUtil.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @version 1.2.2 2015年7月6日 下午5:33:51
 * @since 1.2.2
 */
public class ProcessUtil{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessUtil.class);

    //---------------------------------------------------------------

    /** Don't let anyone instantiate this class. */
    private ProcessUtil(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * 在单独的进程中执行指定的字符串命令.
     * 
     * <p>
     * <span style="color:red">某些命令win7 需要管理员权限运行.</span>
     * </p>
     *
     * @param command
     *            一条指定的系统命令
     * @return 一个新的 Process 对象,用于管理子进程<br>
     *         如果 <code>command</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>command</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     * @see java.lang.Runtime#exec(String, String[], java.io.File)
     */
    public static Process exec(String command){
        Validate.notBlank(command, "command can't be null/empty!");
        LOGGER.debug("[command]:{}", command);

        //---------------------------------------------------------------
        String[] cmdarray = StringUtil.tokenizeToStringArray(command, " \t\n\r\f");
        return exec(cmdarray);
    }

    //---------------------------------------------------------------

    /**
     * 在单独的进程中执行指定命令和变量.
     * 
     * <p>
     * <span style="color:red">某些命令win7 需要管理员权限运行.</span>
     * </p>
     *
     * @param cmdarray
     *            包含所调用命令及其参数的数组.
     * @return 一个新的 Process 对象,用于管理子进程
     */
    public static Process exec(String...cmdarray){
        Validate.notEmpty(cmdarray, "cmdarray can't be null/empty!");
        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("[cmdarray]:{}", JsonUtil.format(cmdarray));
        }

        //---------------------------------------------------------------
        Runtime runtime = Runtime.getRuntime();
        try{
            return runtime.exec(cmdarray);
        }catch (IOException e){
            throw new UncheckedIOException(e);
        }
    }
}
