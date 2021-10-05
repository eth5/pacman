package test.game.ecs.systems.input;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.EntitySubscription;
import com.artemis.annotations.One;
import com.artemis.systems.IteratingSystem;
import com.artemis.utils.IntBag;
import test.client.messages.ClientMessage;
import test.game.ecs.components.Client;
import test.game.ecs.components.Player;
import test.game.ecs.components.events.UserPressKeysEvent;
import test.log.Log;

/**
 * Система отслеживает UserPressKeysEvent и в случае подключенного клиента отправляет нажатые клавиши на сервер
 */
@One(UserPressKeysEvent.class)
public class SendKeysSystem extends IteratingSystem {
    private ComponentMapper<UserPressKeysEvent> inputEventCm;
    private int playerId = -1;
    private Client client;

    @Override
    protected void initialize() {
        // подписываемся на игрока который имеет компонент Client
        // игрок с 1 клиента только 1, больше 1 и быть не долно по логике, но так спокойнее :)
        world.getAspectSubscriptionManager().get(Aspect.all(Player.class, Client.class))
                .addSubscriptionListener( new EntitySubscription.SubscriptionListener() {
                    @Override
                    public void inserted( IntBag entities ) {
                        if (playerId != -1 || entities.size() > 1){
                            throw new IllegalStateException("Client может быть только 1 экземпляр!");
                        }
                        playerId = entities.get(0);
                        client = world.getMapper(Client.class).get(playerId);
                    }
                    @Override
                    public void removed( IntBag entities ) {
                        int size = entities.size();
                        int[] data = entities.getData();
                        for (int i = 0; i < size; i++) {
                            if (data[i] == playerId){
                                playerId =-1;
                                client = null;
                            }else{
                                Log.e(this,"Отписываются непонятные клиенты, wtf?");
                            }
                        }
                    }
                } );
    }
    @Override
    protected void process(int entityId) {
        if (playerId == -1 ) return;
        // Log.i("Send input keys");

        UserPressKeysEvent userPressKeysEvent = inputEventCm.get(entityId);

        ClientMessage clientMessage = ClientMessage.getInstance();
        clientMessage.size = userPressKeysEvent.size;
        System.arraycopy(userPressKeysEvent.keys, 0, clientMessage.keys, 0, userPressKeysEvent.size);
        client.connection.send(clientMessage);
    }
}
