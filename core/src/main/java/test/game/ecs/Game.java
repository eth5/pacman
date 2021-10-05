package test.game.ecs;

import com.artemis.World;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.Queue;
import test.client.messages.State;
import test.game.Connection;
import test.game.ecs.components.CameraTarget;
import test.game.ecs.components.Client;
import test.game.ecs.components.Player;
import test.game.ecs.components.events.InitialEvent;
import test.game.ecs.util.Events;
import test.presentation.IScreen;

/**
 * Слой обертка для связи сервера и ecs world
 */

public class Game implements IScreen {
    // карта соотвествий удаленных(remote) id сущностей - локальным
    // для инекции зависимостей в системы
    private final IntMap<Integer> remoteToLocalEntities = new IntMap<>();

    private boolean isReady = false;
    private World world;
    private final Queue<State> states;

    public Game(Queue<State> states) {
        this.states = states;
    }

    public void setWorld(EcsWorldBuilder ecsWorldBuilder){
        ecsWorldBuilder.dependencyInjection(worldConfiguration->{
            worldConfiguration.register("remoteToLocalEntities",remoteToLocalEntities);
        });
        this.world = ecsWorldBuilder.build();
        isReady = true;
    }

    public void createEntityForLocalPlayer(int remotePlayerId, Connection connection){

        int localPlayerId = world.create();
        remoteToLocalEntities.put(remotePlayerId, localPlayerId);

        world.getMapper(CameraTarget.class).create(localPlayerId);
        world.getMapper(Player.class).create(localPlayerId);
        world.getMapper(InitialEvent.class).create(localPlayerId);
        world.getMapper(Client.class).create(localPlayerId).connection = connection;
    }

    @Override
    public void update(float dt) {
        if (!isReady)return;

        if (!states.isEmpty()){
            // т.к. сервер и игра работают в разных потоках возможен пропуск обноления состяния
            // в данном случае каждое обновление состояние обноляется в разных тиках в нужной последовтельности
            Events.updateState(world,states.removeFirst());
        }
        world.setDelta(dt);
        world.process();
    }

    @Override
    public void dispose() {
        isReady = false;
        if (world!=null) world.dispose();
        remoteToLocalEntities.clear();
    }

}
