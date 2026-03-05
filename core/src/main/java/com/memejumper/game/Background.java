package com.memejumper.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Background {
    private Texture[] layers;
    private float[] offsets;
    private float[] speeds;

    public Background() {
        // In a real implementation, these would be loaded from assets
        layers = new Texture[3];
        offsets = new float[3];
        speeds = new float[]{0.2f, 0.5f, 1.0f}; // Different speeds for parallax
    }

    public void update(float delta, float gameSpeed) {
        for (int i = 0; i < offsets.length; i++) {
            offsets[i] += gameSpeed * speeds[i] * 100f * delta;
        }
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < layers.length; i++) {
            if (layers[i] != null) {
                float x = -(offsets[i] % layers[i].getWidth());
                batch.draw(layers[i], x, 0);
                batch.draw(layers[i], x + layers[i].getWidth(), 0);
            }
        }
    }

    public void dispose() {
        for (Texture t : layers) {
            if (t != null) t.dispose();
        }
    }
}
