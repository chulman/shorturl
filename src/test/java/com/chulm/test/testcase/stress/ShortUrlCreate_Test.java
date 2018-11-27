package com.chulm.test.testcase.stress;

import com.chulm.test.client.NettyClient;
import io.netty.channel.ChannelFuture;

public class ShortUrlCreate_Test {

    private static int num = 100;
    private final static NettyClient[] clientArr = new NettyClient[num];

    private static long start ;
    public static void main(String []args) throws Exception {
        String host = "localhost";
        int port = 8080;

        start =System.currentTimeMillis();

        /*
         * N 개의 클라이언트가 접속하고 url 저장 요청 및 link
         */
        for (int i = 0; i < clientArr.length; i++) {
            String testUrl = "http://www.google.com/" + i;

            clientArr[i] = new NettyClient();
            clientArr[i].connect(host, port);
            clientArr[i].createRequest(testUrl);
            Thread.sleep(10);
            clientArr[i].close();
        }

        //using thread or time sleep.
        Thread.sleep(5000);
    }
}
