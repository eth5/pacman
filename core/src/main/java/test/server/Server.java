package test.server;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import test.interfaces.Action;
import test.log.Log;
import test.serialization.GSonSerializer;
import test.serialization.ISerializer;
import test.server.decode.MessagesDecoder;
import test.server.encode.ClientMessageEncoder;
import test.server.handlers.ClientConnectionHandler;
import test.server.handlers.InitialGameDataMessageHandler;
import test.server.handlers.StateMessageHandler;
import test.server.messages.InitialGameDataMessage;

public class Server {
    final EventLoopGroup workerGroup = new NioEventLoopGroup();
    private final String host;
    private final int port;

    public Server(String host, int port) {
        this.host = host;
        this.port = port;
    }

    private Thread serverThread;
    private boolean isRun;


    public Server connect(
            final Action.Arg1<ChannelHandlerContext> onConnect,
            final Action onDisconnect,
            final Action.Arg2<ChannelHandlerContext, InitialGameDataMessage> onInitialGameDataReceived,
            final Action.Arg1<test.server.messages.State> onStateReceived
    ) {
        if (isRun) throw new IllegalStateException("сервер уже запущен!");
        isRun = true;
        serverThread = new Thread(() -> {
            final ISerializer serializer = new GSonSerializer();
            try {
                Bootstrap bootstrap = new Bootstrap()
                        .group(workerGroup)
                        .channel(NioSocketChannel.class)
                        .option(ChannelOption.SO_KEEPALIVE, true)
                        .handler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            public void initChannel(SocketChannel ch) throws Exception {
                                ch.pipeline().addLast(
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
                        });

                Log.i("Try connect to " + host + ":" + port);
                ChannelFuture f = bootstrap.connect(host, port).sync();

                f.channel().closeFuture().sync();
                Log.i("Close connect");

            } catch (InterruptedException exception) {

                exception.printStackTrace();

            } finally {
                workerGroup.shutdownGracefully();
                Log.e(this, "end :(");
            }
        });
        serverThread.start();
        return this;
    }

    public void dispose() {
        workerGroup.shutdownGracefully();
    }
}
