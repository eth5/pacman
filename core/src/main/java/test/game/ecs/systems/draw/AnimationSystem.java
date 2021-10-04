package test.game.ecs.systems.draw;

import com.artemis.ComponentMapper;
import com.artemis.annotations.All;
import com.artemis.systems.IteratingSystem;
import test.game.ecs.components.animation.Animations;
import test.game.ecs.components.DrawCp;
import test.game.ecs.components.animation.AnimationEvent;

/**
 * Примитивная система анимации, обновлеяет ткущий DrawCp.textureRegion регионом из Animations.animationData
 * при окончании события устанавливает DrawCp.textureRegion, если TextureRegion был сохранен в Animations
 */


@All({AnimationEvent.class, DrawCp.class, Animations.class})
public class AnimationSystem extends IteratingSystem {
    private ComponentMapper<DrawCp> drawCpCM;
    private ComponentMapper<AnimationEvent> animationEventCM;
    private ComponentMapper<Animations> animationsCM;

    @Override
    protected void process(int entityId) {
        AnimationEvent animationEvent = animationEventCM.get(entityId);
        DrawCp drawCp = drawCpCM.get(entityId);

        if (animationEvent.loop){

            drawCp.textureRegion = animationEvent.animationData.update(world.delta);

        } else if (animationEvent.time > 0){

            animationEvent.time -= world.delta;
            drawCp.textureRegion = animationEvent.animationData.update(world.delta);

        }else {
            Animations animations = animationsCM.get(entityId);
            if (animations.idleTextureRegion != null) drawCp.textureRegion = animations.idleTextureRegion;
            animationEventCM.remove(entityId);

        }
    }

}
