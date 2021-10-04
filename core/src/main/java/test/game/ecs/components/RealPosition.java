package test.game.ecs.components;

import com.artemis.PooledComponent;
import com.badlogic.gdx.math.Vector2;

/**
 * Компонент содерижит сконвертированные данные Position удобные для обработки
 */

public class RealPosition extends PooledComponent {
    public final Vector2 coord = new Vector2();
    public boolean initialed = false;
    @Override
    protected void reset() {
        coord.setZero();
        initialed = false;
    }
}
