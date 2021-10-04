package test.game.ecs.components;

import com.artemis.PooledComponent;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

/**
 * Компонент хранит данные для отображения сущности
 */

public class DrawCp extends PooledComponent {
    public boolean flip = false;
    public TextureAtlas.AtlasRegion textureRegion;

    public final Vector2 drawPosition = new Vector2();
    public final Vector2 size = new Vector2();

    public Color color = Color.WHITE;
    public float rotation;
    public int layer = 0;

    public void setTextureRegion(TextureAtlas.AtlasRegion textureRegion){
        this.textureRegion = textureRegion;
        size.x = textureRegion.getRegionWidth();
        size.y = textureRegion.getRegionHeight();
    }
    @Override
    protected void reset()
    {
        flip = false;
        color = Color.WHITE;
        textureRegion = null;
        rotation = 0;
        size.setZero();
        drawPosition.setZero();
        layer = 0;
    }
}
