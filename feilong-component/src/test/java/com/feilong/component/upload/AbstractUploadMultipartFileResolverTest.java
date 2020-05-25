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
package com.feilong.component.upload;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;

abstract class AbstractUploadMultipartFileResolverTest{

    protected final MultipartFileResolver multipartFileResolver = new DefaultMultipartFileResolver();

    //---------------------------------------------------------------

    protected MultipartFile build(){
        return new MultipartFile(){

            @Override
            public String getName(){
                return null;
            }

            @Override
            public String getOriginalFilename(){
                return null;
            }

            @Override
            public String getContentType(){
                return null;
            }

            @Override
            public boolean isEmpty(){
                return false;
            }

            @Override
            public long getSize(){
                return 0;
            }

            @Override
            public byte[] getBytes() throws IOException{
                return null;
            }

            @Override
            public InputStream getInputStream() throws IOException{
                return null;
            }

            @Override
            public void transferTo(File dest) throws IOException,IllegalStateException{

            }
        };
    }

}
