package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LevelBulletsTest {

    private static final float LONG_ENOUGH_TO_FLY_A_TILE = 1f;

    private Level level;
    private Tank player;

    @BeforeEach
    void setUp() {
        level = new Level(10, 8);
        player = new Tank(new GridPoint2(1, 1), Direction.RIGHT);
        level.addPlayer(player);
    }

    @Test
    void firedBulletBecomesPartOfTheLevel() {
        player.shoot(level);

        assertEquals(2, level.getObjects().size());
    }

    @Test
    void bulletFiredIntoTheWallOfTheFieldNeverAppears() {
        Tank cornered = new Tank(new GridPoint2(9, 4), Direction.RIGHT);
        level.addCombatant(cornered);

        cornered.shoot(level);

        // only the two tanks are on the level
        assertEquals(2, level.getObjects().size());
    }

    @Test
    void bulletDisappearsWhenItFliesOutOfTheField() {
        Tank border = new Tank(new GridPoint2(8, 4), Direction.RIGHT);
        level.addCombatant(border);
        border.shoot(level);

        // it lands on (9,4) and the tile after that is outside
        level.update(LONG_ENOUGH_TO_FLY_A_TILE);

        assertEquals(2, level.getObjects().size());
    }

    @Test
    void bulletBreaksAgainstATreeAndTheTreeStays() {
        level.addObstacle(new Tree(new GridPoint2(4, 1)));
        player.shoot(level);

        for (int i = 0; i < 5; i++) {
            level.update(LONG_ENOUGH_TO_FLY_A_TILE);
        }

        // the player and the tree are left, the bullet is gone
        assertEquals(2, level.getObjects().size());
        assertFalse(level.isFree(new GridPoint2(4, 1)));
    }

    @Test
    void bulletDamagesTheTankItHits() {
        Tank target = new Tank(new GridPoint2(4, 1), Direction.LEFT);
        level.addCombatant(target);
        player.shoot(level);

        for (int i = 0; i < 5; i++) {
            level.update(LONG_ENOUGH_TO_FLY_A_TILE);
        }

        assertEquals(Tank.DEFAULT_HEALTH - Bullet.DEFAULT_DAMAGE, target.getHealth());
        assertEquals(2, level.getObjects().size());
    }

    @Test
    void tankWithNegativeHealthLeavesTheLevel() {
        Tank target = new Tank(new GridPoint2(4, 1), Direction.LEFT, 0.4f, 0, 0f);
        level.addCombatant(target);
        player.shoot(level);

        for (int i = 0; i < 5; i++) {
            level.update(LONG_ENOUGH_TO_FLY_A_TILE);
        }

        // health went 0 -> -1, so both the target and the bullet are gone
        assertEquals(1, level.getObjects().size());
        assertTrue(level.isFree(new GridPoint2(4, 1)));
    }

    @Test
    void enemyStandingRightNextToTheBarrelIsHitAtOnce() {
        Tank target = new Tank(new GridPoint2(2, 1), Direction.LEFT);
        level.addCombatant(target);

        player.shoot(level);
        level.update(0.01f);

        assertEquals(Tank.DEFAULT_HEALTH - Bullet.DEFAULT_DAMAGE, target.getHealth());
        assertEquals(2, level.getObjects().size());
    }

    @Test
    void twoBulletsDestroyEachOther() {
        Tank opponent = new Tank(new GridPoint2(5, 1), Direction.LEFT);
        level.addCombatant(opponent);

        player.shoot(level);
        opponent.shoot(level);
        for (int i = 0; i < 4; i++) {
            level.update(LONG_ENOUGH_TO_FLY_A_TILE);
        }

        // both tanks are untouched and both bullets are gone
        assertEquals(2, level.getObjects().size());
        assertEquals(Tank.DEFAULT_HEALTH, player.getHealth());
        assertEquals(Tank.DEFAULT_HEALTH, opponent.getHealth());
    }

    @Test
    void bulletsDoNotBlockTanks() {
        player.shoot(level);

        assertTrue(level.isFree(new GridPoint2(2, 1)));
    }
}
