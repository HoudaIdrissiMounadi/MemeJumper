package com.memejumper.game;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PlayerTest {
    private Player player;

    @BeforeEach
    public void setUp() {
        player = new Player();
    }

    @Test
    public void testInitialState() {
        assertEquals(Player.State.RUNNING, player.getState());
        assertEquals(3, player.getLives());
        assertEquals(50f, player.getY());
    }

    @Test
    public void testJump() {
        player.jump();
        assertEquals(Player.State.JUMPING, player.getState());
        player.update(0.1f, false);
        assertTrue(player.getY() > 50f);
    }

    @Test
    public void testDuck() {
        player.duck(true);
        assertEquals(Player.State.DUCKING, player.getState());
        assertEquals(50f, player.getHitbox().height);
        player.duck(false);
        assertEquals(Player.State.RUNNING, player.getState());
        assertEquals(100f, player.getHitbox().height);
    }

    @Test
    public void testDash() {
        player.dash();
        assertEquals(Player.State.DASHING, player.getState());
        assertTrue(player.isInvulnerable());
        player.update(0.4f, false); // Dash duration is 0.3f
        assertEquals(Player.State.RUNNING, player.getState());
        assertFalse(player.isInvulnerable());
    }
}
