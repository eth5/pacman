package test.game.ecs.components;

import com.artemis.Component;
import com.artemis.PooledComponent;
import com.google.gson.annotations.Expose;

/**
 * Компонент хранит данные о позиции сущности выраженные в игровых клетках на игровом поле
 */

public class Position extends PooledComponent implements IUpdate {
    @Expose
    public int lineIndex;
    @Expose
    public int line;

    @Override
    protected void reset() {
        lineIndex = 0;
        line = 0;
    }

    @Override
    public void update(Component component){
        Position position = (Position) component;
        lineIndex = position.lineIndex;
        line = position.line;
    }
}
