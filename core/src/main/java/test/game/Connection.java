package test.game;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import test.log.Log;

/**
 * Имплементация соединения с сервером
 */

public class Connection {
    private final ChannelHandlerContext ctx;
    public Connection(ChannelHandlerContext ctx){
        this.ctx = ctx;
    }


    public void send(Object object){
        if (!ctx.channel().isWritable()) {
            Log.e(this, "ctx not writable!");
            close();
            return;
        }
        ChannelFuture channelFuture = ctx.writeAndFlush(object);
        try {
            channelFuture.sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void close(){
        ctx.close();
    }
}
