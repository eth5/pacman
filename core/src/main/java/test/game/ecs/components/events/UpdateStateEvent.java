package test.game.ecs.components.events;

import com.artemis.PooledComponent;
import test.client.messages.State;

/**
 * Компонент-событие запускает систему обновления состояния у сущностей
 */

public class UpdateStateEvent extends PooledComponent {
    public State state;

    @Override
    protected void reset() {
        state = null;
    }
}
