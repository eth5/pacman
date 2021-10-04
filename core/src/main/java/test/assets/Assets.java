package test.assets;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

public class Assets extends AssetManager implements IAssets {
    private final ObjectMap<String, TextureAtlas.AtlasRegion> textures = new ObjectMap<>();
    public boolean isLoaded;

    @Override
    public synchronized boolean update() {
        boolean loaded = super.update();
        if (loaded) handleOnLoaded();
        return loaded;
    }

    private void handleOnLoaded() {
        Array<TextureAtlas> array = new Array<>();
        getAll(TextureAtlas.class, array);
        for (TextureAtlas atlas : array) {
            putTexturesByName(atlas, textures);
        }
        isLoaded = true;
    }

    private void putTexturesByName(TextureAtlas atlas, ObjectMap<String, TextureAtlas.AtlasRegion> map) {
        Array<TextureAtlas.AtlasRegion> regions = atlas.getRegions();
        for (TextureAtlas.AtlasRegion region : regions) {
            if (map.containsKey(region.name)) throw new Error("дублирование имени текстуры!");
            map.put(region.name, region);
        }
    }

    @Override
    public TextureAtlas.AtlasRegion getTexture(String name) {
        return textures.get(name);
    }

    @Override
    public Array<TextureAtlas.AtlasRegion> getArrayTextures(String animationName, int frames) {
        Array<TextureAtlas.AtlasRegion> array = new Array<>(frames);
        for (int i = 0; i < frames; i++) {
            array.add(getTexture(animationName + "/" + i));
        }
        return array;
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
