package test.game.ecs.systems.camera;

import com.artemis.ComponentMapper;
import com.artemis.annotations.All;
import com.artemis.annotations.Wire;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.utils.viewport.Viewport;
import test.game.ecs.components.CameraTarget;
import test.game.ecs.components.DrawCp;

/**
 * Примитивная система перемещения камеры за сущностью
 */


@All({CameraTarget.class, DrawCp.class})
public class CameraTargetSystem extends IteratingSystem {

    @Wire(name = "viewport")
    private Viewport viewport;
    private Camera camera;
    private ComponentMapper<DrawCp> drawCpCM;

    @Override
    protected void initialize() {
        camera = viewport.getCamera();
    }

    @Override
    protected void process(int entityId) {
        DrawCp drawCp = drawCpCM.get(entityId);
        camera.position.x = drawCp.drawPosition.x;
        camera.position.y = drawCp.drawPosition.y;
    }

}
