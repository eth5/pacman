package test.game.ecs.systems.input;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.IntArray;
import test.game.ecs.components.events.UserPressKeysEvent;
import test.game.ecs.components.events.OneFrame;
import test.log.Log;

/**
 * Система отслежтвает ввод с клавиатуры и создает сущность-событие которая живет 1 тик(OneFrame),
 * все кто заинтересован в этом собии отслеживают это собите
 */

public class InputKeyboardSystem extends BaseSystem {
    private ComponentMapper<UserPressKeysEvent> inputEventCm;
    private ComponentMapper<OneFrame> oneFrameCm;
    private final IntArray keys = new IntArray(4);

    @Override
    protected void processSystem() {
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            // Log.i(this, "Up key down");
            keys.add(Input.Keys.UP);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            // Log.i(this, "Down key down");
            keys.add(Input.Keys.DOWN);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            // Log.i(this, "LEFT key down");
            keys.add(Input.Keys.LEFT);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            // Log.i(this, "Right key down");
            keys.add(Input.Keys.RIGHT);
        }
        //Log.i(this,"process");
        if (!keys.isEmpty()) {
            // Log.i(this,"нажато " + keys.size + " клавиш");
            int event = world.create();
            oneFrameCm.create(event);
            UserPressKeysEvent userPressKeysEvent = inputEventCm.create(event);
            userPressKeysEvent.size = keys.size;
            System.arraycopy(keys.items, 0, userPressKeysEvent.keys, 0, keys.size);
            keys.clear();
        }
    }
}
