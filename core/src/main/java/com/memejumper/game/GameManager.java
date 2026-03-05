package com.memejumper.game;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.math.MathUtils;
import java.util.HashMap;
import java.util.Map;

public class GameManager {
    private Player player;
    private Background background;
    private Array<Obstacle> activeObstacles;
    private Pool<Obstacle> obstaclePool;
    private Array<Collectable> activeCollectables;
    private Pool<Collectable> collectablePool;

    private long score;
    private int coins;
    private float gameSpeed;
    private float distanceTraveled;
    private boolean isGameOver;

    private float obstacleSpawnTimer;
    private float collectableSpawnTimer;
    private float speedIncreaseTimer;

    private Map<Collectable.Type, Float> activePowerUps;

    private static final float INITIAL_SPEED = 5.0f;
    private static final float MAX_SPEED = 15.0f;
    private static final float BASE_SPAWN_DELAY = 2.0f;
    private static final float GROUND_Y = 50f;
    private static final float SCREEN_WIDTH = 800f;

    public GameManager(Player player, Background background) {
        this.player = player;
        this.background = background;
        this.activeObstacles = new Array<>();
        this.obstaclePool = new Pool<Obstacle>() {
            @Override
            protected Obstacle newObject() {
                return new Obstacle();
            }
        };
        this.activeCollectables = new Array<>();
        this.collectablePool = new Pool<Collectable>() {
            @Override
            protected Collectable newObject() {
                return new Collectable();
            }
        };
        this.activePowerUps = new HashMap<>();
        reset();
    }

    public void reset() {
        score = 0;
        coins = 0;
        gameSpeed = INITIAL_SPEED;
        distanceTraveled = 0;
        isGameOver = false;
        obstacleSpawnTimer = 0;
        collectableSpawnTimer = 0;
        speedIncreaseTimer = 0;
        activeObstacles.clear();
        activeCollectables.clear();
        activePowerUps.clear();
        player.setLives(3);
        player.setSpeed(INITIAL_SPEED);
    }

    public void update(float delta) {
        if (isGameOver) return;

        // Update speed
        speedIncreaseTimer += delta;
        if (speedIncreaseTimer >= 10f) {
            gameSpeed = Math.min(gameSpeed + 0.1f, MAX_SPEED);
            player.setSpeed(gameSpeed);
            speedIncreaseTimer = 0;
        }

        // Update distance and score
        distanceTraveled += gameSpeed * 100f * delta;
        score = (long) distanceTraveled + (coins * 50);

        // Update Player
        player.update(delta, activePowerUps.containsKey(Collectable.Type.ROCKET));

        // Update Background
        if (background != null) {
            background.update(delta, gameSpeed);
        }

        // Update Obstacles
        obstacleSpawnTimer += delta;
        float spawnDelay = BASE_SPAWN_DELAY / (gameSpeed * 0.2f);
        if (obstacleSpawnTimer >= spawnDelay) {
            spawnObstacle();
            obstacleSpawnTimer = 0;
        }

        for (int i = activeObstacles.size - 1; i >= 0; i--) {
            Obstacle obs = activeObstacles.get(i);
            obs.update(delta, gameSpeed);
            if (!obs.isActive()) {
                activeObstacles.removeIndex(i);
                obstaclePool.free(obs);
            }
        }

        // Update Collectables
        collectableSpawnTimer += delta;
        if (collectableSpawnTimer >= 3f) {
            spawnCollectable();
            collectableSpawnTimer = 0;
        }

        for (int i = activeCollectables.size - 1; i >= 0; i--) {
            Collectable coll = activeCollectables.get(i);
            coll.update(delta, gameSpeed);

            // Magnet effect
            if (activePowerUps.containsKey(Collectable.Type.MAGNET)) {
                float dx = player.getX() - coll.getX();
                float dy = player.getY() - coll.getY();
                float dist = (float) Math.sqrt(dx*dx + dy*dy);
                if (dist < 300f) {
                    // Pull towards player (simplified)
                    coll.init(coll.getX() + dx * 5f * delta, coll.getY() + dy * 5f * delta, coll.getType());
                }
            }

            if (!coll.isActive()) {
                activeCollectables.removeIndex(i);
                collectablePool.free(coll);
            }
        }

        // Update Power-ups
        for (Collectable.Type type : Collectable.Type.values()) {
            if (activePowerUps.containsKey(type)) {
                float timeRemaining = activePowerUps.get(type) - delta;
                if (timeRemaining <= 0) {
                    activePowerUps.remove(type);
                } else {
                    activePowerUps.put(type, timeRemaining);
                }
            }
        }

        checkCollisions();
    }

    private void spawnObstacle() {
        Obstacle.Type type = Obstacle.Type.values()[MathUtils.random(Obstacle.Type.values().length - 1)];
        Obstacle obs = obstaclePool.obtain();
        obs.init(SCREEN_WIDTH, GROUND_Y, type);
        activeObstacles.add(obs);
    }

    private void spawnCollectable() {
        Collectable.Type type = MathUtils.random(1.0f) < 0.8f ? Collectable.Type.MEMECOIN :
            Collectable.Type.values()[MathUtils.random(1, Collectable.Type.values().length - 1)];
        Collectable coll = collectablePool.obtain();
        coll.init(SCREEN_WIDTH, GROUND_Y + MathUtils.random(0, 150f), type);
        activeCollectables.add(coll);
    }

    private void checkCollisions() {
        // Obstacle collisions
        if (!player.isInvulnerable() && !activePowerUps.containsKey(Collectable.Type.ROCKET)) {
            for (Obstacle obs : activeObstacles) {
                if (player.getHitbox().overlaps(obs.getHitbox())) {
                    if (activePowerUps.containsKey(Collectable.Type.SHIELD)) {
                        activePowerUps.remove(Collectable.Type.SHIELD);
                        obs.setActive(false);
                    } else {
                        player.setLives(player.getLives() - 1);
                        obs.setActive(false);
                        if (player.getLives() <= 0) {
                            isGameOver = true;
                        }
                    }
                }
            }
        }

        // Collectable collisions
        for (Collectable coll : activeCollectables) {
            if (player.getHitbox().overlaps(coll.getHitbox())) {
                coll.onCollect(this);
            }
        }
    }

    public void addCoins(int amount) { this.coins += amount; }
    public void addScore(long amount) { this.distanceTraveled += amount; } // Simple mapping

    public void activatePowerUp(Collectable.Type type, float duration) {
        activePowerUps.put(type, duration);
    }

    public long getScore() {
        return (long) distanceTraveled + (coins * 50);
    }
    public int getCoins() { return coins; }
    public boolean isGameOver() { return isGameOver; }
    public float getGameSpeed() { return gameSpeed; }
    public Array<Obstacle> getActiveObstacles() { return activeObstacles; }
    public Array<Collectable> getActiveCollectables() { return activeCollectables; }
    public Map<Collectable.Type, Float> getActivePowerUps() { return activePowerUps; }
}
