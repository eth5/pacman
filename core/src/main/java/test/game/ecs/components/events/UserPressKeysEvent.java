package test.game.ecs.components.events;

import com.artemis.PooledComponent;

/**
 * Компонент-событие содержит нажатые пользователем коды клавиши в текущем тике
 * keys - массив кодов клавишь
 * size - количество значимих кодов от начала массива в текущем тике.
 */

public class UserPressKeysEvent extends PooledComponent {
    public final int[] keys = new int[5];
    public int size = 0;
    @Override
    protected void reset() {
        size = 0;
    }
}
