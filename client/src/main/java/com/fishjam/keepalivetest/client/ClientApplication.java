package com.fishjam.keepalivetest.client;


import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@SpringBootApplication

@Slf4j
public class ClientApplication implements CommandLineRunner {
    final String GET_URL = "http://localhost:8080/timer/current";
    final int LOOP_COUNT = 10;
    final int SLEEP_TIME = 5 * 1000;

    public static void main(String[] args) {
        SpringApplication.run(ClientApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("enter run ,args={}", Arrays.toString(args));

        RequestConfig requestConfig = RequestConfig.custom()
                //.setConnectTimeout(10_000) //连接超时,即三次握手完成时间,10s.
                //.setSocketTimeout(60_000)  //读数据超时,数据传输过程中数据包之间间隔的最大时间.
                //.setConnectionRequestTimeout(10_000)   //使用连接池来管理连接,从连接池获取连接的超时时间
                .build();

        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(10);  //总最大连接数
        connectionManager.setDefaultMaxPerRoute(5); //每个站点最大同时连接数

        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(connectionManager)
                //.setDefaultCookieStore(cookieStore)
                .setDefaultRequestConfig(requestConfig)
                .build();

        for (int i = 0; i < LOOP_COUNT; i++){
            HttpGet httpget = new HttpGet(GET_URL);
            CloseableHttpResponse response = httpClient.execute(httpget);
            log.info("index={}, status={}", i, response.getStatusLine().getStatusCode());
            if (response != null) {
                response.getEntity().getContent().close();
                response.close();
            }
            Thread.sleep(SLEEP_TIME);
        }

    }
}
