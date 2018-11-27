package com.chulm.test.testcase.stress;

import com.chulm.shorturl.util.Base62Codec;
import com.chulm.test.client.NettyClient;

public class ShortUrlAPI_Test {

    private static int num = 100;
    private final static NettyClient[] clientArr = new NettyClient[num];
    public static long start = 0;

    public static void main(String []args) throws Exception {
        String host = "localhost";
        int port = 8080;

        start =System.currentTimeMillis();


        for (int i = 0; i < clientArr.length; i++) {
            String end_point = "http://localhost:8080/api/v1/short-url/" + Base62Codec.encode(i++);

            clientArr[i] = new NettyClient();
            clientArr[i].connect(host, port);
            clientArr[i].apiRequest(end_point);
            Thread.sleep(10);
            clientArr[i].close();
        }
    }
}
