package test.game.ecs.systems.state;

import com.artemis.Component;
import com.artemis.ComponentMapper;
import com.artemis.World;
import com.badlogic.gdx.utils.IntMap;
import test.game.ecs.components.*;
import test.game.ecs.components.events.InitialEvent;
import test.log.Log;
import test.server.messages.StateEntityData;
import test.server.messages.State;

/**
 * Имплементация синхронизации стетйта удаленных(remote) и локальных сущностей
 */

public class StateEntitySynchronizer {
    private final World world;

    //Содержит данные о соответствии remote id сущностей с локальными
    private final IntMap<Integer> remoteToLocalEntities;
    private final ComponentMapper<InitialEvent> initialCM;
    public StateEntitySynchronizer(World world, IntMap<Integer> remoteToLocalEntities){
        this.world = world;
        this.initialCM = world.getMapper(InitialEvent.class);
        this.remoteToLocalEntities = remoteToLocalEntities;
    }

    public void newState(State state){
        for (StateEntityData entity : state.entities) {
            newState(entity);
        }
    }

    public void newState(StateEntityData stateEntityData){
        if (remoteToLocalEntities.containsKey(stateEntityData.entityId)) {
            //если новый стейт принадлежит сущности которая уже имеет локальный стейт, то обновляем ее
            updateEntityState(remoteToLocalEntities.get(stateEntityData.entityId), stateEntityData);
        }else{
            //если новый стейт принадлежит сущности которая не имеет локальный стейт, то
            // создаем сущность под этот стейт и обновляем
            int entityId = world.create();
            remoteToLocalEntities.put(stateEntityData.entityId, entityId);
            updateEntityState(entityId, stateEntityData);

            //инициализируем для создание локальных копонентов, например: DrawCp
            initialCM.create(entityId);
        }
    }

    private void updateEntityState(int entityId, StateEntityData stateEntityData){
        if (stateEntityData.destroy) {
            //стейт сущности помещен как удаленный(delete)
            //значит удаляем локальную сущность и освобождаем привязку к удаленной
            world.delete(entityId);
            remoteToLocalEntities.remove(stateEntityData.entityId);
            return;
        }
        for (Component component : stateEntityData.components) {
            if (component==null){ //проверям, что в стайте все компоненты десерелизовались, null быть не должно
                Log.e(this, "Component is null!!");
                continue;
            }
            ComponentMapper<? extends Component> mapper = world.getMapper(component.getClass());
            Component component1 = mapper.create(entityId);
            if (component1 instanceof IUpdate){
                // если компонент обновляемый, то обновляем данные компонента
                ((IUpdate)component1).update(component);
            }
        }
    }


}
