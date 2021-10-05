package test.game.ecs.util;

import com.artemis.ComponentMapper;
import com.artemis.World;
import test.client.messages.State;
import test.game.ecs.components.animation.AnimationData;
import test.game.ecs.components.animation.AnimationEvent;
import test.game.ecs.components.animation.Animations;
import test.game.ecs.components.events.UpdateStateEvent;
import test.log.Log;

public class Events {
    private Events(){}

    public static void playAnimation(World world, int entityId, int animationState, boolean loop, float time){
        ComponentMapper<Animations> mapper = world.getMapper(Animations.class);
        if ( !mapper.has(entityId)){
            Log.e("Events.playAnimation", "Сущность не имеет компонента анимации");
            return;
        }
        Animations animations = mapper.get(entityId);
        if (!animations.animationsData.containsKey(animationState)){
            Log.e("Events.playAnimation", "Сущность не имеет такой анимации анимации " + animationState);
            return;
        }
        AnimationData animationData = animations.animationsData.get(animationState);
        AnimationEvent animationEvent = world.getMapper(AnimationEvent.class).create(entityId);
        animationEvent.time = time;
        animationEvent.loop = loop;
        animationEvent.animationData = animationData;
    }

    public static void updateState(World world, State state){
        int entityId = world.create();
        UpdateStateEvent updateStateEvent = world.getMapper(UpdateStateEvent.class).create(entityId);
        updateStateEvent.state = state;
    }
}
