package test.game.ecs.systems.draw;

import com.artemis.ComponentMapper;
import com.artemis.annotations.All;
import com.artemis.annotations.Wire;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.viewport.Viewport;
import test.game.ecs.components.Client;
import test.game.ecs.components.Player;

/**
 * Отображаем очки клиента
 */

@All({Player.class, Client.class})
public class DrawPlayerDataSystem extends IteratingSystem {
    private ComponentMapper<Player> playerCM;

    @Wire(name = "batch")
    private Batch batch;
    @Wire(name="viewport")
    private Viewport viewport;
    private Camera camera;

    private final BitmapFont bitmapFont = new BitmapFont();

    @Override
    protected void initialize() {
        camera = viewport.getCamera();
    }

    @Override
    protected void process(int entityId) {
        Player player = playerCM.get(entityId);
        batch.begin();

        float cameraX = camera.position.x - camera.viewportWidth/2;
        float cameraY = camera.position.y + camera.viewportHeight/2;


        bitmapFont.draw(batch,"Score: " + player.score, cameraX,cameraY);

        batch.end();
    }
}
