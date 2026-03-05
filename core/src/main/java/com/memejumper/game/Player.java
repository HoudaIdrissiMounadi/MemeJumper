package com.memejumper.game;

import com.badlogic.gdx.math.Rectangle;

public class Player {
    public enum State { RUNNING, JUMPING, DUCKING, DASHING }

    private float x, y;
    private float velocityY;
    private float speed;
    private int lives;
    private State state;
    private float dashTimer;
    private float duckTimer;

    private static final float GRAVITY = 1500f;
    private static final float JUMP_VELOCITY = 600f;
    private static final float DASH_DURATION = 0.3f;
    private static final float DASH_SPEED_BOOST = 2.0f;
    private static final float GROUND_Y = 50f;
    private static final float WIDTH = 50f;
    private static final float HEIGHT = 100f;
    private static final float DUCK_HEIGHT = 50f;

    public Player() {
        this.x = 100f;
        this.y = GROUND_Y;
        this.speed = 5.0f;
        this.lives = 3;
        this.state = State.RUNNING;
    }

    public void update(float delta, boolean isRocketActive) {
        if (isRocketActive) {
            // Fly upwards or stay at a certain height
            float targetY = GROUND_Y + 200f;
            if (y < targetY) y += 200f * delta;
            else if (y > targetY + 10f) y -= 100f * delta;
            state = State.RUNNING; // Or a new FLYING state
        } else if (state == State.JUMPING) {
            y += velocityY * delta;
            velocityY -= GRAVITY * delta;
            if (y <= GROUND_Y) {
                y = GROUND_Y;
                state = State.RUNNING;
                velocityY = 0;
            }
        } else if (state == State.DASHING) {
            dashTimer -= delta;
            if (dashTimer <= 0) {
                state = State.RUNNING;
            }
        } else if (state == State.DUCKING) {
            // In a real game, ducking might be held down.
            // Here we could implement a timer or wait for release.
            // For now, let's assume it's state-based and managed by input.
        }
    }

    public void jump() {
        if (state == State.RUNNING || state == State.DUCKING) {
            state = State.JUMPING;
            velocityY = JUMP_VELOCITY;
        }
    }

    public void duck(boolean isDucking) {
        if (state == State.RUNNING && isDucking) {
            state = State.DUCKING;
        } else if (state == State.DUCKING && !isDucking) {
            state = State.RUNNING;
        }
    }

    public void dash() {
        if (state == State.RUNNING || state == State.DUCKING) {
            state = State.DASHING;
            dashTimer = DASH_DURATION;
        }
    }

    public Rectangle getHitbox() {
        float currentHeight = (state == State.DUCKING) ? DUCK_HEIGHT : HEIGHT;
        return new Rectangle(x, y, WIDTH, currentHeight);
    }

    public boolean isInvulnerable() {
        return state == State.DASHING;
    }

    // Getters and Setters
    public float getX() { return x; }
    public float getY() { return y; }
    public float getSpeed() { return state == State.DASHING ? speed * DASH_SPEED_BOOST : speed; }
    public void setSpeed(float speed) { this.speed = speed; }
    public int getLives() { return lives; }
    public void setLives(int lives) { this.lives = lives; }
    public State getState() { return state; }
}
