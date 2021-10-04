package test.client;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import test.client.messages.InitialGameDataMessage;
import test.interfaces.Action;
import test.log.Log;

public class GameClient {
    final EventLoopGroup workerGroup = new NioEventLoopGroup();
    private final String host;
    private final int port;

    public GameClient(String host, int port) {
        this.host = host;
        this.port = port;
    }
    private Thread connectionThread;
    private boolean isRun;

    public GameClient connect(
            final Action.Arg1<ChannelHandlerContext> onConnect,
            final Action onDisconnect,
            final Action.Arg2<ChannelHandlerContext, InitialGameDataMessage> onInitialGameDataReceived,
            final Action.Arg1<test.client.messages.State> onStateReceived
    ) {
        if (isRun) throw new IllegalStateException("сервер уже запущен!");
        isRun = true;
        connectionThread = new Thread(() -> {
            try {
                Bootstrap bootstrap = new Bootstrap()
                        .group(workerGroup)
                        .channel(NioSocketChannel.class)
                        .option(ChannelOption.SO_KEEPALIVE, true)
                        .handler(
                                new ClientInitializer(
                                        onConnect,
                                        onDisconnect,
                                        onInitialGameDataReceived,
                                        onStateReceived)
                        );

                Log.i("Try connect to " + host + ":" + port);
                ChannelFuture f = bootstrap.connect(host, port).sync();

                f.channel().closeFuture().sync();
                Log.i("Close connect");

            } catch (InterruptedException exception) {

                exception.printStackTrace();

            } finally {
                workerGroup.shutdownGracefully();
                Log.i(this, "end :(");
            }
        });
        connectionThread.start();
        return this;
    }

    public void dispose() {
        workerGroup.shutdownGracefully();
    }
}
