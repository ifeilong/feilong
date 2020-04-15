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

import java.io.InputStream;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.core.bean.ConvertUtil;
import com.feilong.io.InputStreamUtil;

/**
 * 运行命令相关操作.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @version 1.0 2010-12-9 下午11:04:05
 * @since 1.0.0
 */
public final class RuntimeUtil{

    /** The Constant log. */
    private static final Logger LOGGER = LoggerFactory.getLogger(RuntimeUtil.class);

    //---------------------------------------------------------------

    /** Don't let anyone instantiate this class. */
    private RuntimeUtil(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * Exec cmd.
     *
     * @param command
     *            the command
     * @return the string
     */
    public static String execCmd(String command){
        Validate.notBlank(command, "command can't be blank!");

        Process process = ProcessUtil.exec(command);
        return execCmd(process);
    }

    /**
     * Exec cmd.
     *
     * @param cmdarray
     *            the cmdarray
     * @return the string
     */
    public static String execCmd(String...cmdarray){
        if (LOGGER.isInfoEnabled()){
            LOGGER.info(ConvertUtil.toString(cmdarray, " "));
        }
        //---------------------------------------------------------------
        Process process = ProcessUtil.exec(cmdarray);
        return execCmd(process);
    }

    //---------------------------------------------------------------

    /**
     * Exec cmd.
     *
     * @param process
     *            the process
     * @return the string
     */
    public static String execCmd(Process process){
        InputStream inputStream = process.getInputStream();
        return InputStreamUtil.toString(inputStream);
    }
}
