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

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.context.Data;

/**
 * 这是一个一体化 数据{@code -->}转成Excel/CVS{@code -->}保存文件到硬盘{@code -->}变成附件发邮件给相关人员 的组件.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @param <T>
 *            the generic type
 * @since 2.1.0
 */
public class DataFileZipEmailComponent<T extends Data> extends DataFileEmailComponent<T>{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(DataFileZipEmailComponent.class);

    //---------------------------------------------------------------

    /** The output zip path. */
    private String              outputZipPathExpression;

    //---------------------------------------------------------------

    /** Post construct. */
    @Override
    @PostConstruct
    protected void postConstruct(){
        // Validate.notBlank(outputZipPathExpression, "outputZipPathExpression can't be blank!");
        LOGGER.info("outputZipPathExpression:[{}]", outputZipPathExpression);
        super.setFileReworker(new ZipFileReworker(outputZipPathExpression));
    }

    //---------------------------------------------------------------
    /**
     * 设置 output zip path.
     *
     * @param outputZipPathExpression
     *            the outputZipPathExpression to set
     */
    public void setOutputZipPathExpression(String outputZipPathExpression){
        this.outputZipPathExpression = outputZipPathExpression;
    }

}
