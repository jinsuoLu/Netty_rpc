package com.selfdemo.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.nio.charset.StandardCharsets;

/**
 * Date:2023/8/7
 * Author:ljs
 * Description:
 */

public class MyChannelHanler1 extends ChannelInboundHandlerAdapter {

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.println("客户端已经收到了消息：-->" + byteBuf.toString(CharsetUtil.UTF_8));
        //可以通过ctx 获取channel
        //ctx.channel().writeAndFlush("hello,client");
    }
}
