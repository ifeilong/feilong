package snippet;

import java.util.concurrent.TimeUnit;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.pool.PoolStats;

public class What21ClientEvictExpiredConnections{

    public static void main(String[] args) throws Exception{
        // 连接池
        PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager();
        poolingHttpClientConnectionManager.setMaxTotal(100);
        CloseableHttpClient httpclient = HttpClients.custom().setConnectionManager(poolingHttpClientConnectionManager)
                        .evictExpiredConnections().evictIdleConnections(5L, TimeUnit.SECONDS).build();
        // 创建URI数组
        String[] urisToGet = { "http://hc.apache.org/", "http://hc.apache.org/httpcomponents-core-ga/", "http://www.what21.com", };

        for (int i = 0; i < urisToGet.length; i++){
            String requestURI = urisToGet[i];
            HttpGet request = new HttpGet(requestURI);

            System.out.println("执行请求URL： " + requestURI);

            CloseableHttpResponse response = httpclient.execute(request);
            //System.out.println("----------------------------------------");
            //System.out.println("返回响应：" + response.getStatusLine());
            //System.out.println("响应内容：" + EntityUtils.toString(response.getEntity()));
            System.out.println("----------------------------------------");

        }

        PoolStats stats1 = poolingHttpClientConnectionManager.getTotalStats();
        System.out.println("Connection是否保持连接：" + stats1.getAvailable());

        // 休眠10秒
        Thread.sleep(10000);

        // 池状态
        PoolStats stats2 = poolingHttpClientConnectionManager.getTotalStats();
        System.out.println("Connection是否保持连接：" + stats2.getAvailable());

    }

}