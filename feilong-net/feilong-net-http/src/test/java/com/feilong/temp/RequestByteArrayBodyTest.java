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
package com.feilong.temp;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;

import org.junit.Test;

import com.feilong.io.entity.MimeType;
import com.feilong.json.JsonUtil;
import com.feilong.lib.io.IOUtils;
import com.feilong.net.http.HttpClientUtil;
import com.feilong.net.http.HttpMethodType;
import com.feilong.net.http.HttpRequest;
import com.feilong.net.http.RequestByteArrayBody;
import com.feilong.test.AbstractTest;

@lombok.extern.slf4j.Slf4j
public class RequestByteArrayBodyTest extends AbstractTest{

    @Test
    public void test(){
        try{
            String upLoadUrl = "https://store-036.blobstore.apple.com/itmspod12-assets-massilia-036001/PodcastSource126%2Fv4%2Fd8%2Fcb%2Fa2%2Fd8cba2a2-84bc-546d-3982-a9c71978d417%2FnuZMp_DnMgUeHd_HtsqshMRFqbHgqos6flKvRah2TcE_U003d-1697451978749?uploadId=71af7450-6c0e-11ee-bc8a-506b4b72c921&Signature=bWXvRFofLfAr%2Bnf3gf565Gk2mfs%3D&AWSAccessKeyId=MKIAWFNBQY0UJV874T4D&partNumber=1&Expires=1698056778";

            InputStream inputStream = new BufferedInputStream(new FileInputStream("/Users/feilong/Downloads/OIP-C.png"));
            byte[] bytes = IOUtils.toByteArray(inputStream);

            //---------------------------------------------------------------

            HttpRequest httpRequest = new HttpRequest(upLoadUrl, HttpMethodType.PUT);
            httpRequest.setRequestByteArrayBody(new RequestByteArrayBody(bytes, 0, 4269567, MimeType.PNG));

            log.debug(JsonUtil.format(HttpClientUtil.getHttpResponse(httpRequest)));
        }catch (Exception e){
            log.error("", e);
        }
    }

    //    private static void uploadFile(String uploadUrl,InputStream inputStream,int offset,int length,MimeType mimeType) throws IOException{
    //        //            System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2,SSLv3");
    //
    //        HttpClient client = HttpClients.custom()
    //                        .setDefaultRequestConfig(RequestConfig.custom().setConnectionRequestTimeout(5000).setSocketTimeout(5000).build())
    //                        .build();
    //
    //        HttpPut put = new HttpPut(uploadUrl);
    //        put.setHeader("Content-Type", mimeType.getMime());
    //        put.setEntity(new ByteArrayEntity(bytes, offset, length, ContentType.create(mimeType.getMime())));
    //
    //        HttpResponse response = client.execute(put);
    //        System.out.println(response.getStatusLine());
    //
    //        IOUtil.closeQuietly(inputStream);
    //    }
}
