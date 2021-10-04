package test.server.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import test.interfaces.Action;
import test.server.messages.ServerCommand;
import test.server.messages.State;


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
