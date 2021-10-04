package test.presentation;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Экран-заглушка перед установкой соединения
 */

public class ConnectingScreen implements IScreen {
    private final String text = "Connecting...";
    private final SpriteBatch batch;
    private final Viewport viewport;
    private final OrthographicCamera camera;
    public ConnectingScreen(Viewport viewport, SpriteBatch spriteBatch){
        this.viewport = viewport;
        this.camera = (OrthographicCamera) viewport.getCamera();
        this.batch = spriteBatch;
    }

    @Override
    public void update(float dt) {
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        //...
    }

    @Override
    public void dispose() {

    }
}
