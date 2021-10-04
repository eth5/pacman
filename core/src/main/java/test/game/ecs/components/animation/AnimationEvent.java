package test.game.ecs.components.animation;

import com.artemis.PooledComponent;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;


/**
 *  Компонент событие, включает анимацию у сущности.
 *  time - время анимации
 *  loop - если true анимация будет постоянная
 *  animationData - данные анимации
 */

public class AnimationEvent extends PooledComponent {
    public boolean loop = false;
    public float time;
    public AnimationData animationData;

    @Override
    protected void reset() {
        loop = false;
        animationData = null;
        time = 0;
    }
}
