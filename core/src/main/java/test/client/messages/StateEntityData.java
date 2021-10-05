package test.client.messages;

import com.artemis.Component;
import com.artemis.utils.Bag;
import com.google.gson.annotations.Expose;
import io.netty.util.Recycler;

import java.util.ArrayList;
import java.util.List;

/**
 * Состояние одной сущности полученное от сервера
 * нужно оптимизировать для GC!
 */

public class StateEntityData implements IRecycler {
    private static final Recycler<StateEntityData> RECYCLER = new Recycler<StateEntityData>() {
        @Override
        protected StateEntityData newObject(Handle<StateEntityData> handle) {
            return new StateEntityData(handle);
        }
    };
    public static StateEntityData getInstance(){
        return RECYCLER.get();
    }

    public static StateEntityData getInstance(int entityId, boolean destroy, Bag<? extends Component> components){
        StateEntityData stateEntityData = RECYCLER.get();
        stateEntityData.entityId = entityId;
        stateEntityData.destroy = destroy;
        for (Component component : components) {
            // if (component instanceof Client) continue;
            stateEntityData.components.add(component);
        }
        return stateEntityData;
    }


    @Expose
    public int entityId;
    @Expose
    public final List<Component> components = new ArrayList<>();
    @Expose
    public boolean destroy;

    private final Recycler.Handle<StateEntityData> handle;
    public StateEntityData(Recycler.Handle<StateEntityData> handle){
        this.handle = handle;
    }

    @Override
    public void recycle(){
        components.clear();
        entityId = -1;
        destroy = false;
        handle.recycle(this);
    }
}
