package com.chulm.test.client;


import com.chulm.test.client.handler.NettyHttpInitalizer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.HttpPostRequestEncoder;

public class NettyClient {

    ChannelFuture cf;
    EventLoopGroup group;
    String host;
    int port;

    public void connect(String host, int port) {

        this.host = host;
        this.port = port;
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

    public void apiRequest(String endPoint) {
        HttpRequest request = null;
        HttpPostRequestEncoder postRequestEncoder = null;

        request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, endPoint);
        request.headers().set(HttpHeaderNames.HOST,  host + ":" + port);
        request.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);

        ChannelFuture future = cf.channel().writeAndFlush(request);

        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                //
            }
        });
    }

    public ChannelFuture createRequest(String url) throws Exception {


        HttpRequest request = null;
        HttpPostRequestEncoder postRequestEncoder = null;

        request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST, "/create"
//					,Unpooled.copiedBuffer(url.getBytes(CharsetUtil.UTF_8))
        );
        request.headers().set(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED);
        request.headers().set(HttpHeaderNames.HOST, host + ":" + port);
        request.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
//			request.headers().set(HttpHeaderNames.CONTENT_LENGTH, url.length());

        postRequestEncoder = new HttpPostRequestEncoder(request, false);
        postRequestEncoder.addBodyAttribute("url", url);
        request = postRequestEncoder.finalizeRequest();
        postRequestEncoder.close();
//			cf.channel().writeAndFlush(request).addListener(ChannelFutureListener.CLOSE);
        return cf.channel().writeAndFlush(request);
    }

    public void close() {
        cf.channel().close();
        group.shutdownGracefully();
    }


}