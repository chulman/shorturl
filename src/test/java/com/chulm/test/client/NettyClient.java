package com.chulm.test.client;


import java.util.UUID;

import com.chulm.test.handler.NettyHttpInitalizer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.multipart.HttpPostRequestEncoder;

public class NettyClient {

    ChannelFuture cf;
    EventLoopGroup group;

    public void connect(String host, int port) {

        group = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true)
                    .handler(new NettyHttpInitalizer(group));

            cf = b.connect(host, port).sync();
//			cf.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createRequest(String host, int port, String url) throws Exception {


        HttpRequest request = null;
        HttpPostRequestEncoder postRequestEncoder = null;

        request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST, "/create"
//					,Unpooled.copiedBuffer(url.getBytes(CharsetUtil.UTF_8))
        );
        request.headers().set(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED);
        request.headers().set(HttpHeaderNames.HOST, host+":"+port);
        request.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
//			request.headers().set(HttpHeaderNames.CONTENT_LENGTH, url.length());

        postRequestEncoder = new HttpPostRequestEncoder(request, false);
        postRequestEncoder.addBodyAttribute("url", url);
        request=postRequestEncoder.finalizeRequest();
        postRequestEncoder.close();
//			cf.channel().writeAndFlush(request).addListener(ChannelFutureListener.CLOSE);
        cf.channel().writeAndFlush(request);
    }

    public void close() {
        cf.channel().close();
        group.shutdownGracefully();
    }


}