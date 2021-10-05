package test.game.ecs;

import com.artemis.World;
import com.badlogic.gdx.utils.Queue;
import test.client.messages.State;
import test.game.Connection;
import test.game.ecs.components.CameraTarget;
import test.game.ecs.components.Client;
import test.game.ecs.components.Player;
import test.game.ecs.components.events.InitialEvent;
import test.game.ecs.util.Events;
import test.log.Log;
import test.presentation.IScreen;

/**
 * Слой обертка для связи сервера и ecs world
 */

public class Game implements IScreen {

    private final World world;
    private int connectionEntityId = -1;
    private final Queue<State> states;

    public Game(EcsWorldBuilder ecsWorldBuilder, Queue<State> states){
        world = ecsWorldBuilder.build();
        this.states = states;
    }


    // не самое лучшее решение для получения локального id сущности локального игрока
    // можно было просто зарезервировать первую сущность(id = 0) под локального игрока
    public int createEntityForLocalPlayer(){

        int localPlayerId = world.create();
        world.getMapper(CameraTarget.class).create(localPlayerId);
        world.getMapper(Player.class).create(localPlayerId);
        world.getMapper(InitialEvent.class).create(localPlayerId);
        return localPlayerId;
    }

    // добавляем соединение к сущности локального игрока
    public void onConnecting(int playerId, Connection connection){
        connectionEntityId = playerId;
        Client client = world.getMapper(Client.class).create(playerId);
        client.connection = connection;
        Log.i(this, "Connected!");
    }

    // удаляем сущность локального игрока при закрытии соединения
    public void onDisconnection(){
        world.delete(connectionEntityId);
        connectionEntityId = -1;
    }

    @Override
    public void update(float dt) {
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
        world.dispose();
    }

}
