package test.game.ecs.components.events;

import com.artemis.PooledComponent;

/**
 * Компонет-событие хранит данные для интерполяции
 */

public class InterpolationEvent extends PooledComponent {

    public float step;
    public float maxValue;
    public float currentValue;

    @Override
    protected void reset() {
        maxValue = 0;
        step = 0;
        currentValue = 0;
    }
}
