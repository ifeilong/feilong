package com.feilong;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

import com.feilong.io.IOUtil;
import com.feilong.io.entity.MimeType;
import com.feilong.lib.io.IOUtils;
import com.feilong.lib.org.apache.http.HttpResponse;
import com.feilong.lib.org.apache.http.client.HttpClient;
import com.feilong.lib.org.apache.http.client.config.RequestConfig;
import com.feilong.lib.org.apache.http.client.methods.HttpPut;
import com.feilong.lib.org.apache.http.entity.ByteArrayEntity;
import com.feilong.lib.org.apache.http.entity.ContentType;
import com.feilong.lib.org.apache.http.impl.client.HttpClients;

public class PodcastDeliveryFileClientImplTest{

    @Test
    public void test() throws IOException{
        int offset = 0;
        int length = 4269567;
        String upLoadUrl = "https://store-036.blobstore.apple.com/itmspod12-assets-massilia-036001/PodcastSource126%2Fv4%2Fd8%2Fcb%2Fa2%2Fd8cba2a2-84bc-546d-3982-a9c71978d417%2FnuZMp_DnMgUeHd_HtsqshMRFqbHgqos6flKvRah2TcE_U003d-1697451978749?uploadId=71af7450-6c0e-11ee-bc8a-506b4b72c921&Signature=bWXvRFofLfAr%2Bnf3gf565Gk2mfs%3D&AWSAccessKeyId=MKIAWFNBQY0UJV874T4D&partNumber=1&Expires=1698056778";

        InputStream inputStream = new BufferedInputStream(new FileInputStream("/Users/feilong/Downloads/OIP-C.png"));
        uploadFile(upLoadUrl, inputStream, offset, length, MimeType.PNG);
    }

    private static void uploadFile(String uploadUrl,InputStream inputStream,int offset,int length,MimeType mimeType) throws IOException{
        //            System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2,SSLv3");

        byte[] bytes = IOUtils.toByteArray(inputStream);
        HttpClient client = HttpClients.custom()
                        .setDefaultRequestConfig(RequestConfig.custom().setConnectionRequestTimeout(5000).setSocketTimeout(5000).build())
                        .build();

        HttpPut put = new HttpPut(uploadUrl);
        put.setHeader("Content-Type", mimeType.getMime());
        put.setEntity(new ByteArrayEntity(bytes, offset, length, ContentType.create(mimeType.getMime())));

        HttpResponse response = client.execute(put);
        System.out.println(response.getStatusLine());

        IOUtil.closeQuietly(inputStream);
    }

}