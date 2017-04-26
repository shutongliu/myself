package com.test.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import java.text.NumberFormat;


/**
 * Created by liust on 2017/2/23.
 */
@Configuration
public class TimeServer {
    public void bind(int port) throws Exception{
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try{
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup,workerGroup).channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG,1024)
            .childHandler(new ChinaChannelHandleer());
            ChannelFuture f = b.bind(port).sync();
            f.channel().closeFuture().sync();

        }finally {
           bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
    private class ChinaChannelHandleer extends ChannelInitializer<SocketChannel> {
        @Override
        protected void initChannel(SocketChannel arg0) throws Exception{
            arg0.pipeline().addLast(new LineBasedFrameDecoder(1024));
            //ByteBuf d = Unpooled.copiedBuffer("$_".getBytes());
            //arg0.pipeline().addLast(new DelimiterBasedFrameDecoder(1024,d));
            //arg0.pipeline().addLast(new FixedLengthFrameDecoder(16));
            arg0.pipeline() .addLast(new StringDecoder());
            arg0.pipeline() .addLast(new TimeServerHandler());
        }
    }
    public static void main(String[] args) throws Exception{
        int port = 8081;
        if(args!=null && args.length>0){
            try{
                port=Integer.valueOf(args[0]);
            }catch (NumberFormatException e){

            }
        }
        new TimeServer().bind(port);
    }
//    @Bean
//    public TimeServer startServer() throws Exception{
//        TimeServer timeServer = new TimeServer();
//        timeServer.bind(8081);
//        return timeServer;
//    }
}
