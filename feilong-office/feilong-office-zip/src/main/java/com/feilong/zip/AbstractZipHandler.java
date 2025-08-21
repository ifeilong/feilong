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
package com.feilong.zip;

import static com.feilong.core.date.DateUtil.formatDurationUseBeginTimeMillis;
import static com.feilong.core.lang.StringUtil.formatPattern;

import java.io.IOException;
import java.io.UncheckedIOException;

import com.feilong.core.Validate;

/**
 * 抽象的压缩.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 2.1.0
 */
@lombok.extern.slf4j.Slf4j
public abstract class AbstractZipHandler implements ZipHandler{

    /**
     * Zip.
     *
     * @param tobeZipFilePath
     *            the tobe zip file path
     * @param outputZipPath
     *            the output zip path
     */
    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.tools.compress.UnzipManager#unZip(java.lang.String, java.lang.String)
     */
    @Override
    public void zip(String tobeZipFilePath,String outputZipPath){
        Validate.notBlank(tobeZipFilePath, "tobeZipFilePath can't be blank!");
        Validate.notBlank(outputZipPath, "outputZipPath can't be blank!");

        //---------------------------------------------------------------
        long beginTimeMillis = System.currentTimeMillis();
        if (log.isInfoEnabled()){
            log.info("begin zip:[{}] to outputZipPath:[{}]", tobeZipFilePath, outputZipPath);
        }

        //---------------------------------------------------------------
        try{
            handle(tobeZipFilePath, outputZipPath);
        }catch (IOException e){
            String message = formatPattern("tobeZipFilePath:[{}],outputZipPath:[{}]", tobeZipFilePath, outputZipPath);
            throw new UncheckedIOException(message, e);
        }
        //---------------------------------------------------------------
        if (log.isInfoEnabled()){
            log.info(
                            "use time:[{}],end zip:[{}],outputZipPath:[{}]",
                            formatDurationUseBeginTimeMillis(beginTimeMillis),
                            tobeZipFilePath,
                            outputZipPath);
        }
    }

    //---------------------------------------------------------------

    /**
     * 处理.
     *
     * @param tobeZipFilePath
     *            the un zip file path
     * @param outputZipPath
     *            the output directory
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    protected abstract void handle(String tobeZipFilePath,String outputZipPath) throws IOException;

}
