package test.game.ecs.components.animation;

import com.artemis.PooledComponent;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.IntMap;

/**
 * Компонент храняций возможные анимации сущности в виде ключ(int) = данные анимации(AnimationData)
 *
 */

public class Animations extends PooledComponent {
    public TextureAtlas.AtlasRegion idleTextureRegion;
    public final IntMap<AnimationData> animationsData = new IntMap<>(1);

    @Override
    protected void reset() {
        animationsData.clear();
        idleTextureRegion = null;
    }
}
