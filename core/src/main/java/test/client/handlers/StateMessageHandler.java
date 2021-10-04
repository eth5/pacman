package test.client.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import test.client.messages.State;
import test.interfaces.Action;


public class StateMessageHandler extends SimpleChannelInboundHandler<State> {
    private final Action.Arg1<State> onCommandReceived;
    public StateMessageHandler(Action.Arg1<State> onMessageReceived){
        this.onCommandReceived = onMessageReceived;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, State msg) throws Exception {
        onCommandReceived.invoke( msg);
    }
}
