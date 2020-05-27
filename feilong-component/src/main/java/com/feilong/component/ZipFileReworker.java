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
package com.feilong.component;

import static com.feilong.core.Validator.isNotNullOrEmpty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.context.FileReworker;
import com.feilong.io.FilenameUtil;
import com.feilong.spring.expression.SpelUtil;
import com.feilong.zip.CompressZipHandler;
import com.feilong.zip.ZipHandler;

/**
 * The Class ZipFileReworker.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 2.1.0
 */
public class ZipFileReworker implements FileReworker{

    /** The Constant log. */
    private static final Logger LOGGER     = LoggerFactory.getLogger(ZipFileReworker.class);

    //---------------------------------------------------------------
    /** The zip handler. */
    private ZipHandler          zipHandler = new CompressZipHandler();

    /** The output zip path expression. */
    private String              outputZipPathExpression;

    //---------------------------------------------------------------

    /**
     * Instantiates a new zip file reworker.
     *
     * @param outputZipPathExpression
     *            the output zip path expression
     */
    public ZipFileReworker(String outputZipPathExpression){
        super();
        this.outputZipPathExpression = outputZipPathExpression;
    }

    /**
     * Instantiates a new zip file reworker.
     */
    public ZipFileReworker(){
        super();
    }

    /**
     * Instantiates a new zip file reworker.
     *
     * @param zipHandler
     *            the zip handler
     * @param outputZipPathExpression
     *            the output zip path expression
     */
    public ZipFileReworker(ZipHandler zipHandler, String outputZipPathExpression){
        super();
        this.zipHandler = zipHandler;
        this.outputZipPathExpression = outputZipPathExpression;
    }

    //---------------------------------------------------------------

    /**
     * Rework.
     *
     * @param filePath
     *            the file path
     * @return the string
     */
    @Override
    public String rework(String filePath){
        String zipFilePath = build(filePath, outputZipPathExpression);
        //---------------------------------------------------------------

        LOGGER.debug("filePath:[{}],outputZipPathExpression:[{}],zipFilePath:[{}]", filePath, outputZipPathExpression, zipFilePath);

        //---------------------------------------------------------------
        zipHandler.zip(filePath, zipFilePath);
        return zipFilePath;
    }

    /**
     * Builds the.
     * 
     * <h3>说明:</h3>
     * <blockquote>
     * <ol>
     * <li>如果指定了 {@code outputZipPathExpression},将使用spel 来解析</li>
     * <li>如果没有指定{@code outputZipPathExpression},那么默认将filePath 替换其后缀为zip</li>
     * </ol>
     * </blockquote>
     *
     * @param filePath
     *            the file path
     * @param outputZipPathExpression
     *            the output zip path expression
     * @return the string
     */
    private static String build(String filePath,String outputZipPathExpression){
        if (isNotNullOrEmpty(outputZipPathExpression)){
            return SpelUtil.getTemplateValue(outputZipPathExpression);
        }

        //---------------------------------------------------------------
        String newPostfixName = "zip";
        return FilenameUtil.getNewFileName(filePath, newPostfixName);
    }

    //---------------------------------------------------------------

    /**
     * Sets the output zip path expression.
     *
     * @param outputZipPathExpression
     *            the outputZipPathExpression to set
     */
    public void setOutputZipPathExpression(String outputZipPathExpression){
        this.outputZipPathExpression = outputZipPathExpression;
    }

    /**
     * 设置 zip handler.
     *
     * @param zipHandler
     *            the zipHandler to set
     */
    public void setZipHandler(ZipHandler zipHandler){
        this.zipHandler = zipHandler;
    }
}
