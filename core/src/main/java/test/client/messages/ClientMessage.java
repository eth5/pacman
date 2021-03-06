package test.client.messages;
import io.netty.util.Recycler;

/**
 * Сообщения от клиента сервуру. Содержит нажатые клавиши
 */

public class ClientMessage implements IRecycler{
    private static final Recycler<ClientMessage> RECYCLER = new Recycler<ClientMessage>() {
        @Override
        protected ClientMessage newObject(Handle<ClientMessage> handle) {
            return new ClientMessage(handle);
        }
    };
    public static ClientMessage getInstance(){
        return RECYCLER.get();
    }

    public final int[] keys = new int[5];
    public int size;
    private final Recycler.Handle<ClientMessage> handle;

    private ClientMessage(Recycler.Handle<ClientMessage> handle){
        this.handle = handle;
    }

    @Override
    public void recycle() {
        size = 0;
        handle.recycle(this);
    }
}
