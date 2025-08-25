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
package com.feilong.template;

import static com.feilong.core.CharsetType.UTF8;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.apache.velocity.Template;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;
import org.apache.velocity.exception.VelocityException;

import com.feilong.core.Validate;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * {@link VelocityEngine} 解析器.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 1.12.5
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
class VelocityEngineParser{

    /** The encoding. */
    private static final String ENCODING = UTF8;

    //---------------------------------------------------------------

    /**
     * Builds the string value.
     *
     * @param velocityEngine
     *            the velocity engine
     * @param templateName
     *            the template name
     * @param context
     *            the context
     * @return 如果 <code>context</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>velocityEngine</code> 是null,抛出 {@link NullPointerException}<br>
     */
    static String parse(VelocityEngine velocityEngine,String templateName,Context context){
        Validate.notNull(velocityEngine, "velocityEngine can't be null!");
        Validate.notNull(context, "context can't be null!");

        //---------------------------------------------------------------
        Writer writer = new StringWriter();

        //if not set encoding,will use org.apache.velocity.runtime.RuntimeInstance#getDefaultEncoding()
        //config key input.encoding ,otherwise is ENCODING_DEFAULT = "ISO-8859-1"
        //see org.apache.velocity.runtime.RuntimeConstants.ENCODING_DEFAULT
        Template template = velocityEngine.getTemplate(templateName, ENCODING);
        template.merge(context, writer);
        try{
            writer.flush();
        }catch (IOException e){
            throw new VelocityException(e.getMessage(), e);
        }
        //---------------------------------------------------------------
        return writer.toString();
    }
}
