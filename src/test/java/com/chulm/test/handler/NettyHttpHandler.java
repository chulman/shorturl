package com.chulm.test.handler;

import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.HttpPostRequestEncoder;
import io.netty.util.CharsetUtil;

public class NettyHttpHandler extends SimpleChannelInboundHandler<HttpObject> {

    private EventLoopGroup group;
    private SocketChannel sc;
    private String code = "";


    public NettyHttpHandler(EventLoopGroup group, SocketChannel sc) {
        this.group = group;
        this.sc = sc;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {

        if (msg instanceof HttpResponse) {
            HttpResponse response = (HttpResponse) msg;

//			System.err.println("STATUS: " + response.status());
//			System.err.println("VERSION: " + response.protocolVersion());

            if (!response.headers().isEmpty()) {
                for (CharSequence name : response.headers().names()) {
                    for (CharSequence value : response.headers().getAll(name)) {
//						System.err.println("HEADER: " + name + " = " + value);
                    }
                }
//				System.err.println();
            }

            if (HttpUtil.isTransferEncodingChunked(response)) {
//				System.err.println("CHUNKED CONTENT {");
            } else {
//				System.err.println("CONTENT {");
            }
        }
        if (msg instanceof HttpContent) {
            HttpContent content = (HttpContent) msg;

            code = content.content().toString(CharsetUtil.UTF_8);
            System.err.println("result :" + code);
            System.err.flush();

            if (content instanceof LastHttpContent) {
//				System.err.println("} END OF CONTENT");

            }
        }
    }

    public void redirectRequest(String shortUrl) {
        HttpRequest request = null;
        HttpPostRequestEncoder postRequestEncoder = null;

        request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, shortUrl);
        request.headers().set(HttpHeaderNames.HOST, sc.remoteAddress().getHostName() + ":" + sc.remoteAddress().getPort());
        request.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);

        ChannelFuture future = sc.pipeline().channel().writeAndFlush(request);

        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {

            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        cause.printStackTrace();
        ctx.close();
        sc.close();
        group.shutdownGracefully();
    }
}