package test.game.ecs.components;

import com.artemis.PooledComponent;
import com.badlogic.gdx.math.Vector2;
import test.interfaces.Action;



public class MoveToCommand extends PooledComponent {
    private static final Action.Arg1<Integer> defAction = (entityId) -> {
    };
    public final Vector2 targetPosition = new Vector2();
    public final Vector2 speed = new Vector2();
    public final Vector2 velocity = new Vector2();
    public Action.Arg1<Integer> onTarget = defAction;

    @Override
    protected void reset() {
        targetPosition.setZero();
        speed.setZero();
        velocity.setZero();

        onTarget = defAction;
    }
}
