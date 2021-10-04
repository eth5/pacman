package test.game.ecs.systems.state;

import com.artemis.ComponentMapper;
import com.artemis.annotations.One;
import com.artemis.annotations.Wire;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import test.assets.IAssets;
import test.game.ecs.components.*;
import test.game.ecs.components.animation.AnimationData;
import test.game.ecs.components.animation.Animations;
import test.game.ecs.components.events.InitialEvent;
import test.game.ecs.util.Events;

/**
 *  После первого получения игровой сущности с сервера, необходимо ее инициализировать, эта система добавит необходимые
 *  компоненты о которых не знает сервер, такие как DrawCp, etc...  пока, что только DrawCp :)
 */

@One(InitialEvent.class)
public class InitialSystem extends IteratingSystem {
    @Wire(name = "assets")
    private IAssets assets;
    private ComponentMapper<InitialEvent> initialCM;
    private ComponentMapper<DrawCp> drawCpCM;
    private ComponentMapper<Wall> wallCM;
    private ComponentMapper<Road> roadCM;
    private ComponentMapper<Reward> rewardCM;
    private ComponentMapper<Player> playerCM;
    private ComponentMapper<RealPosition> realPositionCM;

    @Override
    protected void process(int entityId) {
        initialEntity(entityId);
        initialCM.remove(entityId);
    }

    public void initialEntity(int entityId){

        realPositionCM.create(entityId);

        if (wallCM.has(entityId)){

            DrawCp drawCp = drawCpCM.create(entityId);
            drawCp.setTextureRegion(assets.getTexture("wall2"));

        } else if (roadCM.has(entityId)){

            DrawCp drawCp = drawCpCM.create(entityId);
            drawCp.setTextureRegion(assets.getTexture("road"));
            // drawCp.color = Color.BLACK;

        } else if (rewardCM.has(entityId)){

            setDrawComponent(entityId,"reward/0", 1);
            setRewardAnimation(entityId);
            Events.playAnimation(world, entityId,Reward.REWARD_IDLE,true,0);

        } else if (playerCM.has(entityId)){

            setDrawComponent(entityId, "pac/0", 1);
            setPlayerPresentationComponents(entityId, world.getMapper(DrawCp.class).get(entityId).textureRegion);
        }
    }

    private void setDrawComponent(int entityId, String textureName, int layer){
        DrawCp drawCp = world.getMapper(DrawCp.class).create(entityId);
        drawCp.layer = layer;
        drawCp.setTextureRegion(assets.getTexture(textureName));
    }

    private void setRewardAnimation(int entityId){
        Animations animations = world.getMapper(Animations.class).create(entityId);
        Array<TextureAtlas.AtlasRegion> pac = assets.getArrayTextures("reward", 2);
        AnimationData animationData = new AnimationData(0.1f, Animation.PlayMode.LOOP, pac);
        animations.animationsData.put(Reward.REWARD_IDLE, animationData);
    }

    private void setPlayerPresentationComponents(int entityId, TextureAtlas.AtlasRegion idleRegion){

        Animations animations = world.getMapper(Animations.class).create(entityId);
        Array<TextureAtlas.AtlasRegion> pac = assets.getArrayTextures("pac", 2);
        AnimationData animationData = new AnimationData(0.1f, Animation.PlayMode.LOOP, pac);
        animations.animationsData.put(Player.EAT_ANIM, animationData);

        // сохраняем оригинальную текстуру чтобы восстанавливает ее при окончании анимации
        animations.idleTextureRegion = idleRegion;
    }
}
