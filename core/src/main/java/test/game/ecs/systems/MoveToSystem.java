package test.game.ecs.systems;

import com.artemis.ComponentMapper;
import com.artemis.annotations.All;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import test.Main;
import test.game.ecs.components.MoveToCommand;
import test.game.ecs.components.Position;



@All({Position.class, MoveToCommand.class})
public class MoveToSystem extends IteratingSystem
{
    public static final float MAX_SPEED = 10;
    private ComponentMapper<Position> positionCm;
    private ComponentMapper<MoveToCommand> moveToCm;
    private final Vector2 sPosition = new Vector2();
    private final Vector2 tPosition = new Vector2();
    
    private static final float STEP_DOWN = 0.5f;
    private final float[] values = new float[2];
    
    private void transformFieldPosition(Position position, Vector2 vector2, int index){
        vector2.x = position.line * index;
        vector2.y = position.lineIndex * index;
    }
    
    @Override
    protected void process(int entityId)
    {
        //if (velocityCm.has(entityId))velocityCm.get(entityId).velocity.setZero();
        Position position = positionCm.get(entityId);
        MoveToCommand moveToCommand = moveToCm.get(entityId);
        
        sPosition.set(position.line,position.lineIndex).scl(Main.BLOCK_SIZE);
        tPosition.set(moveToCommand.targetPosition.x, moveToCommand.targetPosition.y).scl(Main.BLOCK_SIZE);
        
        float speedX,speedY;
        
        if (!MathUtils
				.isEqual(
						sPosition.x,
						tPosition.x,
						moveToCommand.speed.x))
        {
        	speedX = moveToCommand.speed.x * Math.signum(tPosition.x - sPosition.x);
		}
        else if (!MathUtils
				.isEqual(
						sPosition.x,
						tPosition.x,
						0.1f))
        {
			moveToCommand.speed.x *= 0.1f;
			speedX = moveToCommand.speed.x;
		}
        else speedX = 0;

        if (!MathUtils
				.isEqual(
						sPosition.y,
						tPosition.y,
						moveToCommand.speed.y))
        {
        	speedY = moveToCommand.speed.y * Math.signum(tPosition.y - sPosition.y);
		}
        else if (!MathUtils
				.isEqual(
						sPosition.y,
						tPosition.y,
						0.1f))
        {
			moveToCommand.speed.y *= 0.1f;
			speedY = moveToCommand.speed.y;
		}
        else speedY = 0;

        if (MathUtils.isZero(speedX) && MathUtils.isZero(speedY))
        {
        	moveToCommand.onTarget.invoke(entityId);
        	moveToCm.remove(entityId);
		}
        else {


           // velocityCp.velocity.set(speedX, speedY);
        }
    }
}
