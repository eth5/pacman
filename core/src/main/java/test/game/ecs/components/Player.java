package test.game.ecs.components;

import com.artemis.Component;
import com.google.gson.annotations.Expose;

/**
 * Компонент игрока, хранит ткущий счет игрока
 */

public class Player extends Component implements IUpdate {
    public static final int EAT_ANIM = 1;

    @Expose
    public int score = 0;

    @Override
    public void update(Component component) {
        Player player = (Player) component;
        score = player.score;
    }
}
