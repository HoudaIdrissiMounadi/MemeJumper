package com.memejumper.game;

import com.badlogic.gdx.math.Rectangle;

public class Obstacle {
    public enum Type { DOGE, GRUMPY_CAT, BUTTERFLY, RICKROLL }

    private float x, y;
    private float width, height;
    private Type type;
    private boolean active;

    public Obstacle() {
        this.active = false;
    }

    public void init(float x, float y, Type type) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.active = true;

        switch (type) {
            case DOGE:
                this.width = 60f;
                this.height = 60f;
                break;
            case GRUMPY_CAT:
                this.width = 80f;
                this.height = 60f;
                break;
            case BUTTERFLY:
                this.width = 40f;
                this.height = 40f;
                this.y += 100f; // Air obstacle
                break;
            case RICKROLL:
                this.width = 70f;
                this.height = 90f;
                break;
        }
    }

    public void update(float delta, float gameSpeed) {
        float speedMultiplier = 100f; // Scale game speed to pixels
        x -= gameSpeed * speedMultiplier * delta;

        if (type == Type.RICKROLL) {
            x -= 50f * delta; // Rickroll moves slightly towards player
        }

        if (x + width < 0) {
            active = false;
        }
    }

    public Rectangle getHitbox() {
        return new Rectangle(x, y, width, height);
    }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public float getX() { return x; }
    public Type getType() { return type; }
}
