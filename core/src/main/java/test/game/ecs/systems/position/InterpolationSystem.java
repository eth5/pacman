package test.game.ecs.systems.position;

import com.artemis.ComponentMapper;
import com.artemis.annotations.All;
import com.artemis.systems.IteratingSystem;
import test.game.ecs.components.DirectionVector;
import test.game.ecs.components.DrawCp;
import test.game.ecs.components.RealPosition;
import test.game.ecs.components.events.InterpolationEvent;


/**
 *  Система интерполирует координаты отображения сущности. Для плавного перемещения между игровыми клетками. Система
 *  подразумевает, что сущность движется в направлении заданном DirectionVector
 */

@All({DrawCp.class, InterpolationEvent.class, DirectionVector.class})
public class InterpolationSystem extends IteratingSystem {
    private ComponentMapper<InterpolationEvent> interpolationCM;
    private ComponentMapper<DrawCp> drawCpCM;
    private ComponentMapper<DirectionVector> directionVectorCM;

    @Override
    protected void process(int entityId) {

        final InterpolationEvent interpolationEvent = interpolationCM.get(entityId);

        float step = interpolationEvent.step * world.delta;
        interpolationEvent.currentValue += step;

        if (interpolationEvent.currentValue > interpolationEvent.maxValue) interpolationEvent.currentValue = interpolationEvent.maxValue;

        final DirectionVector directionVector = directionVectorCM.get(entityId);
        final DrawCp drawCp = drawCpCM.get(entityId);

        if (directionVector.x > 0){
            drawCp.rotation = 0;
            drawCp.flip = false;

            drawCp.drawPosition.x += step;

        }else if (directionVector.x < 0){
            drawCp.rotation = 0;
            drawCp.flip = true;
            drawCp.drawPosition.x -= step;

        } else if (directionVector.y > 0){
            drawCp.rotation = 90;
            drawCp.flip = false;
            drawCp.drawPosition.y += step;


        }else if (directionVector.y < 0){
            drawCp.rotation = -90;
            drawCp.flip = false;
            drawCp.drawPosition.y -= step;
        }

        if (interpolationEvent.currentValue == interpolationEvent.maxValue){
            interpolationCM.remove(entityId);
            drawCp.drawPosition.set(world.getMapper(RealPosition.class).get(entityId).coord );
        }
    }
}
