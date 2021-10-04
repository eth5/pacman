package test.game.ecs.systems.position;

import com.artemis.ComponentMapper;
import com.artemis.annotations.All;
import com.artemis.annotations.Wire;
import com.artemis.systems.IteratingSystem;
import test.Main;
import test.game.ecs.components.*;
import test.game.ecs.components.events.InterpolationEvent;
import test.game.ecs.util.Events;
import test.log.Log;

/**
 * Система конвертирует координаты игрового поля объекта из Position в удобные координаты для рисования.
 * после конвертации компонет Position удаляеися. При обновлении сущности с сервера этот компонент (Position)
 * восстанавливается и система запускается снова. Эта система рабоатет с сущностями у которых присутвует компонент
 * DirectionVector, это значит, что сущности могут динамически менять свое положение. Т.к. координаты отображаемого
 * мира имеют большую дискретность чем координаты игрового поля, то сконвертированные координаты записываются в
 * RealPosition и создается Event Interpolation. При первичном получении данных (realPosition.initialed = false)
 * или если сконвертированные координаты совпадают с координатами в DrawCp, то Event Interpolation не создается т.к.
 * сущность фактически не перемещается
 *
 * данная система подразумевает, что объект всегда перемещается на 1 игровую клетку
 **/

@All({DrawCp.class, DirectionVector.class, Position.class})
public class DynamicPositionConverterSystem extends IteratingSystem {
    private ComponentMapper<Position> positionCM;
    private ComponentMapper<InterpolationEvent> interpolationCM;
    private ComponentMapper<RealPosition> realPositionCM;
    private ComponentMapper<DrawCp> drawCpCM;

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

        if (realPosition.initialed && !realPosition.coord.equals(drawCp.drawPosition)){

            Events.playAnimation(world,entityId,Player.EAT_ANIM,false,0.1f);

            InterpolationEvent interpolationEvent = interpolationCM.create(entityId);

            // 0.1f - это согласованное значение времени, чаще которого сервер будет игнорировать команду на движение
            // поэтому запускаем интерполящию координат сущности между игровыми клетками,
            // можно это значение получать с сервера, например для реализации speedbooster
            // дефолтное значение в Position.BLOCK_TIME (код сервера)

            interpolationEvent.step =  Main.BLOCK_SIZE / 0.1f;
            interpolationEvent.maxValue = Main.BLOCK_SIZE;
            interpolationEvent.currentValue = 0;


        } else {
            realPosition.initialed = true;

            // просто перезаписываем данные даже если они совпадают это не частое событие
            drawCpCM.get(entityId).drawPosition.set(realPosition.coord);
        }

        positionCM.remove(entityId);
    }
}
