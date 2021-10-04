package test.game.ecs.components;

import com.artemis.Component;
import com.artemis.PooledComponent;
import com.google.gson.annotations.Expose;

/**
 * Компонент делающий сущность наградой, данные о вознаграждении (score) в текущей реализации не используются,
 * но могут быть пригодны для акцинирования внимания на сущностях с более высокой наградой
 */

public class Reward extends PooledComponent implements IUpdate{
    public static final int REWARD_IDLE = 1;
    @Expose
    public int score = 0;
    @Override
    protected void reset() {
        score = 0;
    }

    @Override
    public void update(Component component) {
        Reward reward = (Reward) component;
        score = reward.score;
    }
}
