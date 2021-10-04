package test.game.ecs.components;

import com.artemis.Component;

/**
 * Интерфейс для апдейта создания состояния компонента
 */

public interface IUpdate {
    void update(Component component);
}
