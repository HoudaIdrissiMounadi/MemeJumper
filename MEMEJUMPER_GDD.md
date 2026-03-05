# MemeJumper - Game Design Document

## 1. Game Concept & Theme
**MemeJumper** is a fast-paced, 2D endless runner designed specifically for Generation Z. The game blends high-speed platforming with the chaotic and ever-evolving world of internet memes.

*   **Gameplay**: Players control a customizable avatar running through a procedurally generated world filled with obstacles and power-ups inspired by iconic memes.
*   **Objectives**: Survive for as long as possible, maximize distance, collect "Memecoins," and climb the global leaderboard.
*   **Target Audience**: Gen Z (ages 10-25), social media users, and fans of internet subcultures.
*   **Unique Selling Points (USPs)**:
    *   **Meme-Centric Content**: Obstacles, power-ups, and backgrounds are all based on popular memes (e.g., Doge, Stonks, Nyan Cat).
    *   **Dynamic Difficulty**: The game scales speed and obstacle density in real-time.
    *   **Viral Shareability**: Integrated features to share "epic fail" screenshots or high scores directly to social media.

## 2. Player Mechanics
The player must navigate a path of increasing difficulty using precise timing and quick reflexes.

*   **Controls**:
    *   **Jump**: `Space` / `Up Arrow` / `Tap`. Short press for a hop, long press for a high jump.
    *   **Duck**: `Down Arrow` / `Swipe Down`. Shrinks the hitbox to slide under high obstacles.
    *   **Dash**: `Shift` / `Double Tap`. A quick forward burst that provides temporary invulnerability to minor obstacles.
*   **Movement Logic**:
    *   The player is locked to the left side of the screen while the environment moves leftward at a variable `speed`.
    *   Gravity is applied constantly when the player is not grounded.
*   **Collision Handling**:
    *   **Hard Collision**: Hitting a meme obstacle reduces `lives` by 1. At 0 lives, the game ends.
    *   **Soft Collision**: Collecting items (coins/power-ups) triggers an immediate effect without stopping movement.
*   **Player Attributes**:
    *   `speed`: Initial speed of 5.0, capped at 15.0.
    *   `lives`: Starts at 3.
    *   `scoreMultiplier`: Starts at 1.0x, increased by power-ups.

## 3. Obstacles & Collectables
Variety is key to keeping the Gen Z audience engaged.

### Obstacles (Meme Characters)
| Name | Type | Behavior |
| :--- | :--- | :--- |
| **Doge** | Ground | Static obstacle, requires a simple jump. |
| **Grumpy Cat** | Ground | Slightly wider hitbox, requires precise timing. |
| **"Is this a bird?" Butterfly** | Air | Flying obstacle, requires the player to duck. |
| **Rickroll Sign** | Ground | Moves slightly towards the player. |

### Collectables & Power-ups
*   **Memecoins**: Gold coins with a "B" (for Bitcoin/Meme vibes). Each coin adds +10 to the score and increases the currency balance.
*   **Power-ups**:
    *   **Rocket (Nyan Cat Mode)**: Player rides a rainbow rocket, flying over all obstacles for 5 seconds.
    *   **Shield (Harambe's Spirit)**: A glowing aura that absorbs the next collision.
    *   **Magnet (Stonks)**: Automatically pulls all Memecoins within a 300px radius to the player for 10 seconds.

## 4. Level Design & Difficulty
The game uses a progressive difficulty curve to maintain high player engagement.

*   **Endless Scaling**:
    *   **Speed**: Increases by 0.1 every 10 seconds of gameplay.
    *   **Spawn Frequency**: The delay between obstacle spawns decreases by 5% every 500 points scored.
    *   **Power-up Balance**: Power-up drop rate decreases as speed increases, forcing more skillful play.
*   **Game Progression Logic**:
    *   **Early Game (0-1000 pts)**: High frequency of Memecoins, infrequent obstacles, low speed.
    *   **Mid Game (1001-5000 pts)**: Introduce complex "Is this a bird?" obstacles that require ducking, speed is moderate.
    *   **Late Game (5000+ pts)**: High speed, combination obstacles (jump then immediately duck), rare power-ups.
*   **Milestones**:
    *   Every 2000 points, the background theme changes (e.g., from "Vaporwave" to "Deep Fried").

## 5. Scoring System & Leaderboard
The core loop rewards both survival and active collection.

*   **Scoring Formula**:
    *   `Total Score = (Distance Traveled * Multiplier) + (Coins Collected * 50)`.
    *   `Distance Traveled`: Measured in "Pixels" (1 pixel = 1 point).
*   **Leaderboard Logic**:
    *   **Local**: Top 10 scores stored in a local `.json` or `.txt` file on the device.
    *   **Remote (API Integration)**: Scores are sent via a simple POST request to a REST API.
*   **Score Sharing**:
    *   Integration with system share intents to post high scores to Twitter/Instagram with the hashtag #MemeJumper.

## 6. UI/UX Design
The interface is designed to be loud, colorful, and intuitive.

*   **HUD (Heads-Up Display)**:
    *   **Top Left**: Score and Coin Counter (pulsing animation when coins are collected).
    *   **Top Right**: Lives (represented by "Hearts" or "Doge faces").
    *   **Bottom Center**: Active Power-up duration bar.
*   **Screens**:
    *   **Start Menu**: Minimalist but vibrant. Features a "Start" button, "Skins" shop, and "Leaderboard" access.
    *   **Game Over**: Displays final score, "New High Score!" splash if applicable, and "Retry" / "Share" buttons.
    *   **Power-up Indicators**: A small icon appears next to the player when a power-up is active.
*   **Visual Style**:
    *   **Theme**: "Modern Internet Chaos." High contrast, neon accents.
    *   **Parallax Backgrounds**: At least 3 layers of background moving at different speeds to create depth (e.g., distant mountains, city skyline, foreground clouds).

## 7. Audio & Visuals
High-energy feedback loops are essential for Gen Z engagement.

*   **Audio**:
    *   **BGM**: Lo-fi hip hop or fast-paced Synthwave that speeds up as the game gets harder.
    *   **SFX**:
        *   `Jump`: Classic 8-bit jump sound.
        *   `Collect`: "Ka-ching" or a meme-sound bite (e.g., "Wow").
        *   `Collision`: "Oof" or "Bonk" sound effect.
        *   `Power-up`: Ascension sound or Nyan Cat melody snippet.
*   **Visuals**:
    *   **Sprite Recommendations**:
        *   **Player**: Pixel-art humanoid or a generic "Chad" character.
        *   **Memes**: High-quality PNGs converted to sprites with simple 2-frame "walking/bouncing" animations.
    *   **Transitions**: Smooth fade-to-black between screens or "glitch" effect transitions.

## 8. Classes & Code Structure (Java)
**Recommended Library**: **LibGDX** (for cross-platform support and high-performance 2D rendering).

### Core Class Structure
| Class Name | Attributes | Key Methods |
| :--- | :--- | :--- |
| **Player** | `x, y, speed, lives, state (running, jumping, ducking)` | `jump()`, `duck()`, `dash()`, `update(float delta)`, `getHitbox()` |
| **Obstacle** | `x, y, width, height, type, active` | `update(float delta)`, `reset(float startX)`, `getHitbox()` |
| **Collectable**| `x, y, type, collected` | `onCollect(Player p)`, `update(float delta)` |
| **GameManager**| `score, coins, speed, currentLevel, isGameOver` | `spawnObstacle()`, `checkCollisions()`, `updateScore()`, `saveHighScore()` |
| **Background** | `List<Layer> layers` | `render(SpriteBatch batch)`, `update(float delta)` |

### Pseudo-Code Snippets

#### Movement & Gravity (Player.java)
```java
void update(float delta) {
    if (isJumping) {
        y += velocityY * delta;
        velocityY -= GRAVITY * delta;
        if (y <= groundY) {
            y = groundY;
            isJumping = false;
        }
    }
}
```

#### Spawning Logic (GameManager.java)
```java
void spawnObstacle() {
    float waitTime = baseSpawnDelay / (speed * 0.5f);
    if (timeSinceLastSpawn >= waitTime) {
        Obstacle obs = obstaclePool.obtain();
        obs.init(screenWidth, groundY, getRandomMemeType());
        activeObstacles.add(obs);
        timeSinceLastSpawn = 0;
    }
}
```

#### Collision Detection (GameManager.java)
```java
void checkCollisions() {
    Rectangle playerHitbox = player.getHitbox();
    for (Obstacle obs : activeObstacles) {
        if (playerHitbox.overlaps(obs.getHitbox())) {
            if (!player.isInvulnerable()) {
                handleGameOver();
            }
        }
    }
}
```

## 9. Bonus Features
Extra features to enhance replayability and viral potential.

*   **Unlockable Skins/Avatars**: Purchase skins using Memecoins (e.g., "Deep Fried Avatar," "Gigachad," "Crying Jordan Mask").
*   **Daily Challenges**: "Collect 50 coins in a single run" or "Jump over 20 Doges." Rewards bonus coins.
*   **Share Score on Social Media**: Dynamic "Game Over" screen generation with a funny meme background and the player's final score for easy Instagram Story sharing.
*   **Particle Effects**: "Confetti" when collecting coins and "Dust clouds" when landing from a jump.
