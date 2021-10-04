package test.client.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import test.client.messages.InitialGameDataMessage;
import test.interfaces.Action;


public class InitialGameDataMessageHandler extends SimpleChannelInboundHandler<InitialGameDataMessage> {
    private final Action.Arg2<ChannelHandlerContext, InitialGameDataMessage> onCommandReceived;
    public InitialGameDataMessageHandler(Action.Arg2<ChannelHandlerContext, InitialGameDataMessage> onMessageReceived){
        this.onCommandReceived = onMessageReceived;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, InitialGameDataMessage msg) throws Exception {
        onCommandReceived.invoke(ctx, msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
