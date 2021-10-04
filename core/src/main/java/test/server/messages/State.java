package test.server.messages;

import com.google.gson.annotations.Expose;
import io.netty.util.Recycler;

import java.util.ArrayList;
import java.util.List;

/**
 * Содержит состояние сущностей полученное от сервера
 * нужно оптимизировать для GC!
 */

public class State {
    private static final Recycler<State> RECYCLER = new Recycler<State>() {
        @Override
        protected State newObject(Handle<State> handle) {
                return new State(handle);
        }
    };
    public static State getInstance(){
        return RECYCLER.get();
    }
    private final Recycler.Handle<State> handler;
    private State(Recycler.Handle<State> handler){
        this.handler = handler;
    }
    public void recycle(){
        for (StateEntityData entity : entities) {
            entity.recycle();
        }
        entities.clear();
        handler.recycle(this);
    }

    @Expose
    public final List<StateEntityData> entities = new ArrayList<>();
}
