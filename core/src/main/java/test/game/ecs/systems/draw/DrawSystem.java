package test.game.ecs.systems.draw;

import com.artemis.Aspect;
import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.artemis.EntitySubscription;
import com.artemis.annotations.Wire;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.viewport.Viewport;
import test.Main;
import test.game.ecs.components.DrawCp;
import test.game.ecs.components.Position;
import test.log.Log;

/**
 *  Система рисующяя текстуры из DrawCp послойно.
 */

public class DrawSystem extends BaseSystem {
    @Wire(name = "batch")
    private Batch batch;
    @Wire(name = "viewport")
    private Viewport viewport;
    private Camera camera;
    private ComponentMapper<DrawCp> drawCpMp;
    private ComponentMapper<Position> positionMp;

    private final IntArray layer0 = new IntArray(100);
    private final IntArray layer1 = new IntArray(100);

    @Override
    protected void initialize() {
        camera = viewport.getCamera();
        // подписываемся на сущности с компонентом DrawCp и помещяем в соотвествующий слой
        world.getAspectSubscriptionManager().get(Aspect.all(DrawCp.class))
                .addSubscriptionListener(new EntitySubscription.SubscriptionListener() {
                    @Override
                    public void inserted(IntBag entities) {
                        final int[] entitiesIds = entities.getData();
                        final int size = entities.size();
                        for (int i = 0; i < size; i++) {
                            int entityId = entitiesIds[i];
                            int layer = drawCpMp.get(entityId).layer;
                            switch (layer) {
                                case 0:
                                    layer0.add(entityId);
                                    break;
                                case 1:
                                    layer1.add(entityId);
                                    break;
                                default:
                                    throw new IllegalStateException("неизвестный layer!");
                            }
                        }
                    }

                    @Override
                    public void removed(IntBag entities) {
                        final int[] entitiesIds = entities.getData();
                        final int size = entities.size();
                        for (int i = 0; i < size; i++) {
                            int entityId = entitiesIds[i];
                            int layer = drawCpMp.get(entityId).layer;
                            switch (layer) {
                                case 0:
                                    layer0.removeValue(entityId);
                                    break;
                                case 1:
                                    layer1.removeValue(entityId);
                                    break;
                                default:
                                    throw new IllegalStateException("неизвестный layer!");
                            }
                        }
                    }
                });
    }

    @Override
    protected void begin() {
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
    }

    @Override
    protected void end() {
        batch.end();
    }

    @Override
    protected void processSystem() {
        drawLayer(layer0);
        drawLayer(layer1);
    }

    protected void drawLayer(IntArray layer) {
        int entityId;
        for (int i = 0; i < layer.size; i++) {
            entityId = layer.get(i);

            DrawCp drawCp = drawCpMp.get(entityId);
            batch.setColor(drawCp.color);

            boolean flippedX = drawCp.textureRegion.isFlipX() != drawCp.flip;
            if (flippedX) drawCp.textureRegion.flip(flippedX, false);

            batch.draw(
                    drawCp.textureRegion,
                    drawCp.drawPosition.x,
                    drawCp.drawPosition.y,
                    Main.BLOCK_SIZE / 2,
                    Main.BLOCK_SIZE / 2,
                    Main.BLOCK_SIZE,
                    Main.BLOCK_SIZE,
                    1f, 1f,
                    drawCp.rotation);
        }
    }
}
