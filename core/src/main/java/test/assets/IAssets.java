package test.assets;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;

public interface IAssets {
    TextureAtlas.AtlasRegion getTexture(String name);
    Array<TextureAtlas.AtlasRegion> getArrayTextures(String animationName, int frames);
    void dispose();
}
