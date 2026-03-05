package com.memejumper.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MemeJumper extends ApplicationAdapter {
    private SpriteBatch batch;
    private Player player;
    private Background background;
    private GameManager gameManager;
    private BitmapFont font;

    // Placeholder textures
    private Texture playerTexture;
    private Texture dogeTexture;
    private Texture coinTexture;

    @Override
    public void create() {
        batch = new SpriteBatch();
        player = new Player();
        background = new Background();
        gameManager = new GameManager(player, background);
        font = new BitmapFont();

        loadAssets();
    }

    private void loadAssets() {
        if (Gdx.files.internal("memerunner.png").exists() && Gdx.files.internal("memerunner.png").length() > 0) {
            playerTexture = new Texture("memerunner.png");
        } else {
            Gdx.app.log("MemeJumper", "memerunner.png missing or empty");
        }

        if (Gdx.files.internal("doge.png").exists() && Gdx.files.internal("doge.png").length() > 0) {
            dogeTexture = new Texture("doge.png");
        } else {
            Gdx.app.log("MemeJumper", "doge.png missing or empty");
        }

        if (Gdx.files.internal("memecoin.png").exists() && Gdx.files.internal("memecoin.png").length() > 0) {
            coinTexture = new Texture("memecoin.png");
        } else {
            Gdx.app.log("MemeJumper", "memecoin.png missing or empty");
        }
    }

    @Override
    public void render() {
        handleInput();
        float delta = Gdx.graphics.getDeltaTime();
        gameManager.update(delta);

        Gdx.gl.glClearColor(0.1f, 0.1f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        // Render Background
        background.render(batch);

        // Render Player
        if (playerTexture != null) {
            batch.draw(playerTexture, player.getX(), player.getY(), player.getHitbox().width, player.getHitbox().height);
        } else {
            // Draw a rectangle as placeholder
            font.draw(batch, "PLAYER", player.getX(), player.getY() + 50);
        }

        // Render Obstacles
        for (Obstacle obs : gameManager.getActiveObstacles()) {
            if (dogeTexture != null) {
                batch.draw(dogeTexture, obs.getX(), obs.getHitbox().y, obs.getHitbox().width, obs.getHitbox().height);
            } else {
                font.draw(batch, obs.getType().toString(), obs.getX(), obs.getHitbox().y + 20);
            }
        }

        // Render Collectables
        for (Collectable coll : gameManager.getActiveCollectables()) {
            if (coinTexture != null) {
                batch.draw(coinTexture, coll.getX(), coll.getY(), coll.getHitbox().width, coll.getHitbox().height);
            } else {
                font.draw(batch, coll.getType().toString(), coll.getX(), coll.getY() + 20);
            }
        }

        // Render HUD
        font.draw(batch, "Score: " + gameManager.getScore(), 20, Gdx.graphics.getHeight() - 20);
        font.draw(batch, "Coins: " + gameManager.getCoins(), 20, Gdx.graphics.getHeight() - 40);
        font.draw(batch, "Lives: " + player.getLives(), 20, Gdx.graphics.getHeight() - 60);

        if (gameManager.isGameOver()) {
            font.draw(batch, "GAME OVER! Press R to Restart", Gdx.graphics.getWidth()/2f - 100, Gdx.graphics.getHeight()/2f);
        }

        batch.end();
    }

    private void handleInput() {
        if (gameManager.isGameOver()) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
                gameManager.reset();
            }
            return;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            player.jump();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            player.duck(true);
        } else {
            player.duck(false);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SHIFT_LEFT)) {
            player.dash();
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        background.dispose();
        if (playerTexture != null) playerTexture.dispose();
        if (dogeTexture != null) dogeTexture.dispose();
        if (coinTexture != null) coinTexture.dispose();
        font.dispose();
    }
}
