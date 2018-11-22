package com.chulm.test.handler;


import javax.net.ssl.SSLException;

import org.apache.logging.log4j.message.Message;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

public class NettyHttpInitalizer extends ChannelInitializer<SocketChannel>{

    private boolean ssl = false;
    private EventLoopGroup group;

    public NettyHttpInitalizer(EventLoopGroup group) {
        this.group = group;
    }
    @Override
    protected void initChannel(SocketChannel sc) throws Exception {

        ChannelPipeline p = sc.pipeline();
        if(ssl) {
            SslContext sslCtx = null;
            try {
                sslCtx = SslContextBuilder.forClient()
                        .trustManager(InsecureTrustManagerFactory.INSTANCE).build();
                p.addLast(sslCtx.newHandler(sc.alloc()));
            } catch (SSLException e1) {
                e1.printStackTrace();
            }
        }

        p.addLast(new MessageDecoder());
        /**
         * Chunk Response, "덩어리 응답"은 전체 페이지를 가공하지 않음
         * 즉, 서버측에서 html을 전부 생성한 후에 클라이언트에게 보내는 것이 아니라 html을 덩어리(chunk) 단위로 쪼개서 보낼 수 있다.
         * 브라우저에게 전체 컨텐츠 크기가 얼마나 큰지 알려주지 않아도됨.
         * 따라서, 동적인 크기의 컨텐츠 및 스트리밍에 적합하고 Chunked transfer encoding을 사용해야함.
         *
         */
        //chunked 된 응답을 집계하는 코덱
//        p.addLast("chunked",new HttpObjectAggregator(1048576));
//		// convert to HTTP Response
        p.addLast("codec",new HttpClientCodec());
        p.addLast(new NettyHttpHandler(group, sc));
    }
}