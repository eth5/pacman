package test.client.messages;

import com.google.gson.annotations.Expose;

/**
 * не используется, сделал для экспериментов на будущее :)
 */

public class ServerCommand {
    @Expose
    public final int remotePlayerId;
    public ServerCommand(int playerId){
        this.remotePlayerId = playerId;
    }
}
