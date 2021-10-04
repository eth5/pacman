package test.game.ecs.systems.camera;

import com.artemis.ComponentMapper;
import com.artemis.annotations.All;
import com.artemis.annotations.Wire;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.utils.viewport.Viewport;
import test.Main;
import test.game.ecs.components.CameraTarget;
import test.game.ecs.components.Position;

/**
 * не реализовано
 */

@All({CameraTarget.class, Position.class})
public class CameraMoveSystem extends IteratingSystem
{
    private int screenHalfW, screenHalfH;
    @Wire(name = "viewport")
    private Viewport viewport;
    private Camera camera;

    @Wire(name = "remotePlayerId")
    private int remotePlayerId;
    @Wire(name = "field_width")
    private int width;
    @Wire(name = "field_height")
    private int height;


    private ComponentMapper<CameraTarget> cameraTargetCm;
    private ComponentMapper<Position> positionCm;

    @Override
    protected void initialize()
    {
        camera = viewport.getCamera();
        screenHalfW = viewport.getScreenWidth()/2;
        screenHalfH = viewport.getScreenHeight()/2;
    }

    @Override
    protected void process(int entityId) {
        Position position = positionCm.get(entityId);
        camera.position.x = position.lineIndex * Main.BLOCK_SIZE;
        camera.position.y = height * Main.BLOCK_SIZE - position.line * Main.BLOCK_SIZE;

    }

}
