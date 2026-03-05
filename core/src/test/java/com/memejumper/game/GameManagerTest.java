package com.memejumper.game;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GameManagerTest {
    private GameManager gameManager;
    private Player player;

    @BeforeEach
    public void setUp() {
        player = new Player();
        gameManager = new GameManager(player, null);
    }

    @Test
    public void testInitialState() {
        assertEquals(0, gameManager.getScore());
        assertEquals(0, gameManager.getCoins());
        assertFalse(gameManager.isGameOver());
    }

    @Test
    public void testScoreIncrement() {
        gameManager.update(1.0f);
        assertTrue(gameManager.getScore() > 0);
    }

    @Test
    public void testCoinCollection() {
        gameManager.addCoins(1);
        assertEquals(1, gameManager.getCoins());
        assertEquals(50, gameManager.getScore()); // Coin adds 50 points
    }

    @Test
    public void testGameOver() {
        player.setLives(1);
        // Create an obstacle at the player's position
        Obstacle obs = new Obstacle();
        obs.init(player.getX(), player.getY(), Obstacle.Type.DOGE);
        gameManager.getActiveObstacles().add(obs);

        gameManager.update(0.1f);
        assertTrue(gameManager.isGameOver());
    }
}
