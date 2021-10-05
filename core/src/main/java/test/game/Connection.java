package test.game;

import io.netty.channel.Channel;

/**
 * Имплементация соединения с сервером
 */

public class Connection {
    private final Channel channel;
    public Connection(Channel channel){
        this.channel = channel;
    }


    public void send(Object object){

        channel.writeAndFlush(object);
    }

    public void close(){
        channel.close();
    }
}
