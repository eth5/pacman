package test.client.messages;

import com.google.gson.annotations.Expose;

/**
 * Содержит данные об игре:
 * размер игрового поля выраженное в игровых клетках
 * и удаленный (remote) id локального игрока
 */

public class InitialGameDataMessage {
    @Expose
    public int width;
    @Expose
    public int height;
    @Expose
    public int remotePlayerId;
}
