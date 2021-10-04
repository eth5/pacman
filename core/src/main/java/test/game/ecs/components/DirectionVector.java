package test.game.ecs.components;

import com.artemis.Component;
import com.artemis.PooledComponent;
import com.google.gson.annotations.Expose;

/**
 * Компонент хранит вектор направления сущности
 */

public class DirectionVector extends PooledComponent implements IUpdate{
    @Expose
    public int x;
    @Expose
    public int y;

    @Override
    protected void reset() {
        x = 0;
        y = 0;
    }

    @Override
    public void update(Component component) {
        DirectionVector directionVector = (DirectionVector) component;
        x = directionVector.x;
        y = directionVector.y;
    }
}
