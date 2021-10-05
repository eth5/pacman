package test.client.messages;

/**
 * Содержит коды сообщений от сервера, для идентификации типа сообщения
 */

public class MessageType {
    private MessageType(){}
    public static final int SERVER_COMMAND = 1;
    public static final int STATE = 2;
    public static final int INITIAL_GAME_DATA = 3;
}
