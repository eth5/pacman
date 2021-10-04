package test.game.ecs.systems.position;

import com.artemis.ComponentMapper;
import com.artemis.annotations.All;
import com.artemis.annotations.Exclude;
import com.artemis.annotations.Wire;
import com.artemis.systems.IteratingSystem;
import test.Main;
import test.game.ecs.components.*;
import test.game.ecs.components.events.InterpolationEvent;

/**
 * Система конвертирует координаты поля из Position в удобные координаты для рисования.
 * после конвертации компонет Position удаляеися. При обновлении сущности с сервера этот компонент (Position) восстанавливается
 * и система запускается снова. Т.к. эта система работает только с неподвижными объектами, которые не имеют компонента DirectionVector,
 * то сконвертированные координаты сразу записываются в RealPosition и DrawCp без изменений.
 */

@All({Position.class, DrawCp.class})
@Exclude(DirectionVector.class)
public class PositionConverterSystem extends IteratingSystem {
    private ComponentMapper<DrawCp> drawCpCM;
    private ComponentMapper<Position> positionCM;
    private ComponentMapper<InterpolationEvent> moveAnimationCM;
    private ComponentMapper<RealPosition> realPositionCM;

    @Wire(name = "field_height")
    private float height;
    @Wire(name = "field_width")
    private float width;


    @Override
    protected void process(int entityId) {

        Position position = positionCM.get(entityId);
        RealPosition realPosition = realPositionCM.get(entityId);
        realPosition.coord.y = height - position.line * Main.BLOCK_SIZE;
        realPosition.coord.x = position.lineIndex * Main.BLOCK_SIZE;

        DrawCp drawCp = drawCpCM.get(entityId);
        drawCp.drawPosition.set(realPosition.coord);

        positionCM.remove(entityId);
    }
}
