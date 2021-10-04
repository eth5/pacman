package test.game.ecs.systems;

import com.artemis.annotations.One;
import com.artemis.systems.IteratingSystem;
import test.game.ecs.components.events.OneFrame;


/**
 * Система удаляет сущности в компонентом OneFrame
 * обычно используется для проброса сущностей-событий по всем системам
 */

@One(OneFrame.class)
public class OneFrameSystem extends IteratingSystem {
    @Override
    protected void process(int entityId) {
        world.delete(entityId);
    }
}
