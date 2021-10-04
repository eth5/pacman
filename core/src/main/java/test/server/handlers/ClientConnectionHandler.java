package test.server.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import test.interfaces.Action;


public class ClientConnectionHandler extends ChannelInboundHandlerAdapter {
    private final Action.Arg1<ChannelHandlerContext> onConnect;
    private final Action onDisconnect;
    public ClientConnectionHandler(Action.Arg1<ChannelHandlerContext> onConnect, Action onDisconnect){
        this.onConnect = onConnect;
        this.onDisconnect = onDisconnect;
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {

        super.channelRegistered(ctx);
        onConnect.invoke(ctx);
    }
    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        onDisconnect.invoke();
        super.channelUnregistered(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
