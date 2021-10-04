package test.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import test.client.decode.MessagesDecoder;
import test.client.encode.ClientMessageEncoder;
import test.client.handlers.ClientConnectionHandler;
import test.client.handlers.InitialGameDataMessageHandler;
import test.client.handlers.StateMessageHandler;
import test.client.messages.InitialGameDataMessage;
import test.interfaces.Action;
import test.serialization.GSonSerializer;
import test.serialization.ISerializer;

public class ClientInitializer extends ChannelInitializer<SocketChannel> {
    private final ISerializer serializer = new GSonSerializer();
    final Action.Arg1<ChannelHandlerContext> onConnect;
    final Action onDisconnect;
    final Action.Arg2<ChannelHandlerContext, InitialGameDataMessage> onInitialGameDataReceived;
    final Action.Arg1<test.client.messages.State> onStateReceived;

    public ClientInitializer(
            final Action.Arg1<ChannelHandlerContext> onConnect,
            final Action onDisconnect,
            final Action.Arg2<ChannelHandlerContext, InitialGameDataMessage> onInitialGameDataReceived,
            final Action.Arg1<test.client.messages.State> onStateReceived) {
        this.onConnect = onConnect;
        this.onDisconnect = onDisconnect;
        this.onInitialGameDataReceived = onInitialGameDataReceived;
        this.onStateReceived = onStateReceived;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(
                new MessagesDecoder(serializer),
                new ClientMessageEncoder(),
                new InitialGameDataMessageHandler(onInitialGameDataReceived),
                new StateMessageHandler(onStateReceived),
                new ClientConnectionHandler(
                        onConnect,
                        onDisconnect
                )
        );
    }
}
