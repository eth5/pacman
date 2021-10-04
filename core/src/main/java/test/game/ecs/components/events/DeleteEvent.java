package test.game.ecs.components.events;

import com.artemis.PooledComponent;

/**
 * Ничего не хранит и не используется заглушка для текущего сериализатора и совместимости при возможном рефакторинге.
 * Стейт удаленной сущности прилетает без компонентов сущности
 */

public class DeleteEvent extends PooledComponent {
    @Override
    protected void reset() {

    }
}
