package test.game;

import io.netty.channel.Channel;
import test.game.ecs.EcsWorldBuilder;
import test.game.ecs.Game;
import test.presentation.IScreen;


public class GameWrapper {
    public final IScreen screen;
    private final Game game;
    private Channel channel;

    public GameWrapper(Game game){
        this.game  = game;
        this.screen = game;
    }
    public void onConnecting(Channel channel){
        this.channel = channel;
    }

    public void onDisconnected() {
        channel = null;
    }

    public void clearGame(){
        game.dispose();
    }

    public void startGame(EcsWorldBuilder ecsWorldBuilder, int remotePlayerId) {
        game.setWorld(ecsWorldBuilder);
        game.createEntityForLocalPlayer(remotePlayerId, new Connection(channel));
    }
}
