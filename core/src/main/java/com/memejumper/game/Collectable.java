package com.memejumper.game;

import com.badlogic.gdx.math.Rectangle;

public class Collectable {
    public enum Type { MEMECOIN, ROCKET, SHIELD, MAGNET }

    private float x, y;
    private float width, height;
    private Type type;
    private boolean collected;
    private boolean active;

    public Collectable() {
        this.active = false;
        this.collected = false;
        this.width = 40f;
        this.height = 40f;
    }

    public void init(float x, float y, Type type) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.active = true;
        this.collected = false;
    }

    public void update(float delta, float gameSpeed) {
        float speedMultiplier = 100f;
        x -= gameSpeed * speedMultiplier * delta;

        if (x + width < 0) {
            active = false;
        }
    }

    public void onCollect(GameManager gm) {
        collected = true;
        active = false;

        switch (type) {
            case MEMECOIN:
                gm.addCoins(1);
                gm.addScore(50);
                break;
            case ROCKET:
                gm.activatePowerUp(Type.ROCKET, 5f);
                break;
            case SHIELD:
                gm.activatePowerUp(Type.SHIELD, 10f); // Or until hit
                break;
            case MAGNET:
                gm.activatePowerUp(Type.MAGNET, 10f);
                break;
        }
    }

    public Rectangle getHitbox() {
        return new Rectangle(x, y, width, height);
    }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public float getX() { return x; }
    public float getY() { return y; }
    public Type getType() { return type; }
}
