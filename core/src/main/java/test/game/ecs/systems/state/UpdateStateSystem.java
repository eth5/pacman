package test.game.ecs.systems.state;

import com.artemis.ComponentMapper;
import com.artemis.annotations.One;
import com.artemis.annotations.Wire;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.utils.IntMap;
import test.game.ecs.components.events.UpdateStateEvent;

/**
 * Обновляет или создает состояние сущностей из UpdateStateEvent
 */

@One(UpdateStateEvent.class)
public class UpdateStateSystem extends IteratingSystem {

    @Wire(name="remoteToLocalEntities")
    private IntMap<Integer> remoteToLocalEntities;

    private StateEntitySynchronizer entityManager;
    private ComponentMapper<UpdateStateEvent> gameStateEventCM;
    @Override
    protected void initialize() {
        entityManager = new StateEntitySynchronizer(world, remoteToLocalEntities);
    }

    @Override
    protected void process(int entityId) {
        UpdateStateEvent updateStateEvent = gameStateEventCM.get(entityId);

        entityManager.newState(updateStateEvent.state);
        world.delete(entityId);
    }
}
